package ondemandmbile.crypto_assistant;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.text.DecimalFormat;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;
import ondemandmbile.crypto_assistant.models.Currency;
import ondemandmbile.crypto_assistant.models.Site;


/**
 * Created by robertnyangate on 22/07/2017.
 */

public class NotifsFragment extends Fragment{
    private Realm realm;
    SharedPreferences sharedPreferences;
    private EditText capitalEd,bitcoinsEd;
    private TextView value,profit,btc_usd;
    private RealmResults<Currency>moneys;

    public static NotifsFragment newInstance() {

        Bundle args = new Bundle();

        NotifsFragment fragment = new NotifsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm=Realm.getDefaultInstance();
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
        moneys=realm.where(Currency.class).findAll();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.notits_frag, container,
                false);

        initializeViews(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        moneys.addChangeListener(new RealmChangeListener<RealmResults<Currency>>() {
            @Override
            public void onChange(RealmResults<Currency> currencies) {
                setValue();
            }
        });
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
        capitalEd=(EditText)rootView.findViewById(R.id.capital);
        value=(TextView) rootView.findViewById(R.id.value);
        btc_usd=(TextView) rootView.findViewById(R.id.btc_usd);
        profit=(TextView) rootView.findViewById(R.id.profit);
        capitalEd.setText(""+sharedPreferences.getFloat("capital",0));
        bitcoinsEd=(EditText)rootView.findViewById(R.id.bitcoins);
        bitcoinsEd.setText(""+sharedPreferences.getFloat("bitcoins",0));
        setValue();
        bitcoinsEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty())
                sharedPreferences.edit().putFloat("bitcoins",Float.valueOf(s.toString())).commit();
            }

            @Override
            public void afterTextChanged(Editable s) {
                setValue();
            }
        });
        capitalEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty())
                sharedPreferences.edit().putFloat("capital",Float.valueOf(s.toString())).commit();
            }

            @Override
            public void afterTextChanged(Editable s) {
                setValue();
            }
        });
    }

    private void setValue() {
        DecimalFormat df=new DecimalFormat("#,###.##");
        double usd_sell =0;
        double usd_buy=0;
        try {
             usd_sell =realm.where(Currency.class).equalTo("name","USD").findFirst().getSell();
             usd_buy =realm.where(Currency.class).equalTo("name","USD").findFirst().getBuy();

        }catch (Exception e){
            Logger.d(e);
            e.printStackTrace();
        }
        value.setText("USD "+df.format(usd_buy*sharedPreferences.getFloat("bitcoins",0)));
        profit.setText("USD "+df.format(usd_buy*sharedPreferences.getFloat("bitcoins",0)
                -sharedPreferences.getFloat("capital",0)));
        btc_usd.setText("USD "+df.format(usd_buy));

    }


}
