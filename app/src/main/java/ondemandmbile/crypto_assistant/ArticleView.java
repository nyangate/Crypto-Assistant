package ondemandmbile.crypto_assistant;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.orhanobut.logger.Logger;

import ondemandmbile.crypto_assistant.R;

public class ArticleView extends AppCompatActivity {
    public OnbackClickedListner onbackClickedListner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_view);
        if(getIntent().hasExtra("url")){
            loadWebView();
        }else {
            finish();
        }
    }

    private void loadWebView() {
        try {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.md_content, MWebFragment.newInstance(getIntent()
                    .getStringExtra("url"),false))
                    .commitAllowingStateLoss();
        } catch (Exception e) {
            Logger.d(e);
        }
    }
    public interface OnbackClickedListner{
        public void onBackClicked();
    }
    @Override
    public void onBackPressed() {
        if(onbackClickedListner!=null) {
            onbackClickedListner.onBackClicked();
            Logger.d("interface actively checked");
        }
        else {
            super.onBackPressed();
        }

    }
}
