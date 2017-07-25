package ondemandmbile.crypto_assistant;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.orhanobut.logger.Logger;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView navigation;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showMarketsFragment();
                    return true;
                case R.id.navigation_dashboard:
                    showDashboard();
                    return true;
                case R.id.navigation_notifications:
                    showProfitsFragment();
                    return true;
            }
            return false;
        }

    };
    private NotifsFragment notifsFragment;
    private void showProfitsFragment() {
        try {
            notifsFragment=notifsFragment!=null?notifsFragment:NotifsFragment.newInstance();
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.content,notifsFragment)
                    .commitAllowingStateLoss();
        } catch (Exception e) {
            Logger.d(e);
        }
    }

    private SuperFragment currentFragment;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm=Realm.getDefaultInstance();
        navigation= (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_dashboard);

    }


    private DashboardFragment dashboardFrag;
    private void showDashboard() {

        try {
            dashboardFrag=dashboardFrag!=null?dashboardFrag:DashboardFragment.newInstance();
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.content, dashboardFrag)
                    .commit();
        } catch (Exception e) {
            Logger.d(e);
        }


    }
    private void showMarketsFragment() {

        try {
            currentFragment=MarketFragment.newInstance();
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.content,currentFragment )
                    .commitAllowingStateLoss();
        } catch (Exception e) {
            Logger.d(e);
        }


    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if(currentFragment!=null)
        currentFragment.onBackPressed();
        else super.onBackPressed();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
