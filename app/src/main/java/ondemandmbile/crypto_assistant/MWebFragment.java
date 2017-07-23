package ondemandmbile.crypto_assistant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.web_view, container,
                false);
        initializeViews(rootView);
        setUrl(webView,url);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
    }

    private void setUrl(WebView webView,String url) {

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
            }
        });

        webView.loadUrl(url);

    }
}
