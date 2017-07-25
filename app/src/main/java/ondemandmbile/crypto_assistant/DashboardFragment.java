package ondemandmbile.crypto_assistant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.orhanobut.logger.Logger;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import ondemandmbile.crypto_assistant.models.Currency;

/**
 * Created by robertnyangate on 22/07/2017.
 */

public class DashboardFragment extends SuperFragment {
    private Realm realm;
    private RecyclerView currencyRecycler;
    private RealmResults<Currency>rates;
    private MarketAdapter marketAdapter;

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
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateViews();
    }

    private void updateViews() {
        /**
         * Loads the current rates every 5 seconds
         * */
        currencyRecycler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadCurrentRates();

                updateViews();
            }
        }, 5000);
    }
    /**
     * Loads current rates from the block chain apis
     * Url is inside
     * */
    private void loadCurrentRates() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(30000);
        client.get(getContext(), "https://blockchain.info/ticker", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                try {
                    final JSONObject vars = new JSONObject(response);
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {

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
                                    realm.copyToRealmOrUpdate(curr);
                                    keys.remove();
                                } while (keys.hasNext());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.d(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Logger.d(error);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            realm.close();
        }catch (Exception e){
            Logger.d(e);
        }
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
                Logger.d(">>changes noted");
            }
        });

    }
}
