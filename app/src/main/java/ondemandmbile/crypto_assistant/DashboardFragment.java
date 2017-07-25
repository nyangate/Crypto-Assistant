package ondemandmbile.crypto_assistant;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.orhanobut.logger.Logger;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import ondemandmbile.crypto_assistant.models.Currency;

/**
 * Created by robertnyangate on 22/07/2017.
 */

public class DashboardFragment extends Fragment {
    private Realm realm;
    private RecyclerView currencyRecycler;
    private RealmResults<Currency>rates;
    private MarketAdapter marketAdapter;
    private Handler handler;
    private  ArrayList<Currency>currencies=new ArrayList<>();
    final String id = ""+new Date().getTime();


    public static DashboardFragment newInstance() {

        Bundle args = new Bundle();

        DashboardFragment fragment = new DashboardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get a database instance
        realm = Realm.getDefaultInstance();
        rates=realm.where(Currency.class)
                .findAll();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dashboard, container,
                false);
        initializeViews(rootView);
        updateRates();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getChildFragmentManager().beginTransaction().replace(R.id.fragment,BitcoinNewsFragment
                .newInstance()).commitAllowingStateLoss();
    }

    @Override
    public void onResume() {
        super.onResume();
        getChildFragmentManager().beginTransaction().replace(R.id.fragment,BitcoinNewsFragment
                .newInstance()).commitAllowingStateLoss();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }
    /**
     * Initialize recyclerview and adapters.
     * call adapter.notifyDataSetChanged if the realm for currency is updated
     * */
    private void initializeViews(View rootView) {
        currencyRecycler = (RecyclerView) rootView.findViewById(R.id.converstions);
        currencyRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager
                .HORIZONTAL, false));

        marketAdapter=new MarketAdapter(getActivity(),rates);
        currencyRecycler.setAdapter(marketAdapter);

        rates.addChangeListener(new RealmChangeListener<RealmResults<Currency>>() {
            @Override
            public void onChange(RealmResults<Currency> currencies) {
                marketAdapter.notifyDataSetChanged();

            }
        });

    }
    private void updateRates() {
        handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
//                if(!isHidden()&&!isDetached()&&isVisible()) {
//                    Logger.d(">>>very visible");
//                    loadCurrentRates();
//                }else {
//                    Logger.d(">>>not visible");
//                }
                loadCurrentRates();
                handler.postDelayed(this, 5000);
            }
        };
        currencyRecycler.postDelayed(r,1000);
    }
    /**
     * Loads current rates from the block chain apis
     * Url is inside
     * */
    private void loadCurrentRates() {
        currencies.clear();
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(30000);
        client.get(getActivity(), "https://blockchain.info/ticker", new AsyncHttpResponseHandler
                () {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                try {
                    final JSONObject vars = new JSONObject(response);
                    try {
                        Iterator<String> keys = vars.keys();
                        do {
                            String key = keys.next();
                            JSONObject con = vars.getJSONObject(key);
                            Currency curr=new Currency();
                            curr.setName(key);
                            curr.setBuy(con.getDouble("buy"));
                            curr.setSell(con.getDouble("sell"));
                            curr.setSymbol(con.getString("symbol"));
                            currencies.add(curr);
                            keys.remove();
                        } while (keys.hasNext());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
//                        Logger.d("finished updating rates");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.d(e);
                }
                if(!currencies.isEmpty()){
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.copyToRealmOrUpdate(currencies);
//                            Logger.d(">>changes noted "+id);
                        }
                    });
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Logger.d(error);
                error.printStackTrace();
            }
        });
    }

}
