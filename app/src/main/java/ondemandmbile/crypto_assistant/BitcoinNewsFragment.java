package ondemandmbile.crypto_assistant;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import ondemandmbile.crypto_assistant.models.Article;

/**
 * Created by robertnyangate on 22/07/2017.
 */

public class BitcoinNewsFragment extends Fragment {
    private RecyclerView articleRecyclerView;
    private Realm realm;
    private RealmResults<Article> articles;
    private NewsAdapter newsAdapter;

    public static BitcoinNewsFragment newInstance() {

        Bundle args = new Bundle();

        BitcoinNewsFragment fragment = new BitcoinNewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm=Realm.getDefaultInstance();
        articles=realm.where(Article.class).findAll();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.news_fragment, container,
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
        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                final ArrayList<Article>arts=new ArrayList<>();
                try {
                    URL url = new URL("https://bitcoin.einnews.com/rss/hk4wEjMelQToDbpB");
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(false);
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput(url.openConnection().getInputStream(),null);

                    boolean insideItem = false;

                    int eventType = xpp.getEventType();
                    Article article=null;
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_TAG) {

                            if (xpp.getName().equalsIgnoreCase("item")) {
                                insideItem = true;
                                if(article!=null) {
                                    arts.add(article);
                                }
                                article=new Article();
                            } else if (xpp.getName().equalsIgnoreCase("title")) {
                                if (insideItem) {
                                    article.setTitle(xpp.nextText());
                                }
                            } else if (xpp.getName().equalsIgnoreCase("link")) {
                                if (insideItem) {
                                    article.setLink(xpp.nextText());
                                }
                                // article
                            }else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                                if (insideItem) {
                                    article.setPubDate(xpp.nextText());
                                    Logger.d(">>"+article.getPubDate());

                                }
                                // article
                            } else if (xpp.getName().equalsIgnoreCase("description")) {
                                if (insideItem) {
                                    article.setDescription(xpp.nextText());
                                }
                                // link of
                                // article
                            }
                        } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                            insideItem = false;
                        }


                        eventType = xpp.next(); // move to next element
                    }

                } catch (MalformedURLException e) {
                    Logger.d(e);
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    Logger.d(e);
                    e.printStackTrace();
                } catch (IOException e) {
                    Logger.d(e);
                    e.printStackTrace();
                }
                Realm realm=Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        if(!arts.isEmpty()) {
                            realm.copyToRealmOrUpdate(arts);
                            Logger.d("Changes updated");
                            Logger.d("change size "+arts.size());
                        }
                    }
                });
                realm.close();
                return null;
            }
        }.execute();

    }

    private void initializeViews(View rootView) {
        articleRecyclerView = (RecyclerView) rootView.findViewById(R.id.articlesfeed);
        articleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager
                .VERTICAL, false));
        newsAdapter=new NewsAdapter(getActivity(),articles);
        articleRecyclerView.setAdapter(newsAdapter);
        articles.addChangeListener(new RealmChangeListener<RealmResults<Article>>() {
            @Override
            public void onChange(RealmResults<Article> articles) {
                newsAdapter.notifyDataSetChanged();
                Logger.d("articles changed");
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
