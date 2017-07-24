package ondemandmbile.crypto_assistant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TabHost;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import ondemandmbile.crypto_assistant.models.Site;


/**
 * Created by robertnyangate on 22/07/2017.
 */

public class MarketFragment extends Fragment{
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton fab;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private RealmResults<Site> sites;
    private Realm realm;

    public static MarketFragment newInstance() {

        Bundle args = new Bundle();

        MarketFragment fragment = new MarketFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm=Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Site site=new Site();
                site.setName("LocalBitcoins");
                site.setUrl("https://localbitcoins.com/");

                Site remitano=new Site();
                remitano.setName("Remitano");
                remitano.setUrl("https://remitano.com/");

                realm.copyToRealmOrUpdate(remitano);
                realm.copyToRealmOrUpdate(site);
            }
        });
        sites=realm.where(Site.class).findAll().sort("name", Sort.ASCENDING);

        //get a database instance
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.markets, container,
                false);

        initializeViews(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(mSectionsPagerAdapter);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //// TODO: 23/07/2017 add sites to show
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
        fab=(FloatingActionButton)rootView.findViewById(R.id.fab);
            tabLayout=(TabLayout)rootView.findViewById(R.id.tabs);
            viewPager=(ViewPager) rootView.findViewById(R.id.container);
        for (Site title:sites
             ) {
            TabLayout.Tab tab=tabLayout.newTab();
            tab.setText(title.getName());
            tabLayout.addTab(tab);
        }
        tabLayout.setupWithViewPager(viewPager);

    }
    private MWebFragment currFragment;



    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return MWebFragment.newInstance(sites.get(position).getUrl(),true);


        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return sites.size();
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
               default:
                   return sites.get(position).getName();
            }
        }
    }


}
