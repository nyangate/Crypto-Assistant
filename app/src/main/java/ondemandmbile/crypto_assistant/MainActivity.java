package ondemandmbile.crypto_assistant;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

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
                    showNewsFrag();
                    return true;
            }
            return false;
        }

    };

    private void showNewsFrag() {


    }
    private SuperFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation= (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_dashboard);
    }
    private void showDashboard() {

        try {
            currentFragment=DashboardFragment.newInstance();
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.content, currentFragment)
                    .commitAllowingStateLoss();
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
        currentFragment.onBackPressed();

    }


}
