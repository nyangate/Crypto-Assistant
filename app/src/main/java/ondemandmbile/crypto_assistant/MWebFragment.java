package ondemandmbile.crypto_assistant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;

/**
 * Created by robertnyangate on 22/07/2017.
 */

public class MWebFragment extends Fragment {
    private String url;
    private WebView webView;
    private ContentLoadingProgressBar contentpbar;
    public static MWebFragment newInstance(String url) {

        Bundle args = new Bundle();

        MWebFragment fragment = new MWebFragment();
        fragment.setArguments(args);
        fragment.url=url;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get a database instance
    }
    public void loadUrl(String url){
        if(webView!=null)
        webView.loadUrl(url);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.web_view, container,
                false);

        initializeViews(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUrl(webView,url);
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
        webView=(WebView)rootView.findViewById(R.id.webview);
        contentpbar=(ContentLoadingProgressBar) rootView.findViewById(R.id.contentpbar);
    }

    private void setUrl(WebView vistaWeb,String url) {
        vistaWeb.clearCache(true);
        vistaWeb.clearHistory();
        vistaWeb.getSettings().setJavaScriptEnabled(true);
        vistaWeb.getSettings().setUseWideViewPort(true);
        vistaWeb.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

//        webView.getSettings().setJavaScriptEnabled(true);
        vistaWeb.getSettings().setLoadWithOverviewMode(true);
        vistaWeb.setWebViewClient(new WebViewClient(){
//
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                showProgressBar(true);
                view.loadUrl(url);

                return true;
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
                showProgressBar(false);
            }
        });

        vistaWeb.loadUrl(url);

    }

    private void showProgressBar(boolean b) {

        if(b)contentpbar.show();
        else contentpbar.hide();
    }

}
