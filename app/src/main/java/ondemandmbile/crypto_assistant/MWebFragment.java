package ondemandmbile.crypto_assistant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.orhanobut.logger.Logger;

/**
 * Created by robertnyangate on 22/07/2017.
 */

public class MWebFragment extends SuperFragment {
    private String url;
    private WebView webView;
    private boolean isMain;
    private ContentLoadingProgressBar contentpbar;

    public static MWebFragment newInstance(String url, boolean isMain) {

        Bundle args = new Bundle();

        MWebFragment fragment = new MWebFragment();
        fragment.setArguments(args);
        fragment.url = url;
        fragment.isMain = isMain;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get a database instance
    }

    public void loadUrl(String url) {
        if (webView != null)
            webView.loadUrl(url);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.web_view, container,
                false);

        initializeViews(rootView);
//        if(isMain) {
//            try {
//                ((MainActivity) getActivity()).onbackClickedListner = new MainActivity.OnbackClickedListner() {
//                    @Override
//                    public void onBackClicked() {
//                        if (webView.canGoBack()) {
//                            webView.goBack();
//                        } else {
//                            ((MainActivity) getActivity()).onBackPressed();
//                        }
//                    }
//                };
//            } catch (Exception e) {
//                Logger.d(e);
//            }
//        }
//        else {
//            try {
//                ((ArticleView) getActivity()).onbackClickedListner = new ArticleView.OnbackClickedListner() {
//                    @Override
//                    public void onBackClicked() {
//                        if (webView.canGoBack()) {
//                            webView.goBack();
//                        } else {
//                            ((ArticleView) getActivity()).onBackPressed();
//                        }
//                    }
//                };
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }


        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUrl(webView, url);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Initialize recyclerview and adapters.
     * call adapter.notifyDataSetChanged if the realm for currency is updated
     */
    private void initializeViews(View rootView) {
        webView = (WebView) rootView.findViewById(R.id.webview);
        contentpbar = (ContentLoadingProgressBar) rootView.findViewById(R.id.contentpbar);
    }

    private void setUrl(WebView vistaWeb, String url) {
        vistaWeb.clearCache(true);
        vistaWeb.clearHistory();
        vistaWeb.getSettings().setJavaScriptEnabled(true);
        vistaWeb.getSettings().setUseWideViewPort(true);
        vistaWeb.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

//        webView.getSettings().setJavaScriptEnabled(true);
        vistaWeb.getSettings().setLoadWithOverviewMode(true);
        vistaWeb.setWebViewClient(new WebViewClient() {
            //
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                showProgressBar(true);
                view.loadUrl(url);

                return true;
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
                contentpbar.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showProgressBar(false);
                    }
                }, 8000);

            }
        });

        vistaWeb.loadUrl(url);

    }

    private void showProgressBar(boolean b) {

        if (b) contentpbar.show();
        else contentpbar.hide();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        }else {
            getActivity().onBackPressed();
        }
    }
}
