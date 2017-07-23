package ondemandmbile.crypto_assistant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.RealmResults;
import ondemandmbile.crypto_assistant.models.Article;


/**
 * Created by robertnyangate on 18/05/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter {
    private static final String TAG = "BambaAdapter";
    private Context context;
    private LayoutInflater layoutInflater;
    RealmResults<Article> articles;
    public NewsAdapter(Context context, RealmResults<Article> articles) {
        try {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
            this.articles = articles;
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static interface OnRemoveCompe {
        void onRemoveCompetitor(String id);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title, description,link,pubdate;
        public CardView cardView;

        public ViewHolder(View convertView) {
            super(convertView);
            title = (TextView) convertView.findViewById(R.id.title);
            description = (TextView) convertView.findViewById(R.id.description);
            pubdate = (TextView) convertView.findViewById(R.id.pubdate);
            cardView = (CardView) convertView.findViewById(R.id.card_view);


        }


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_feed_adapter, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final Article article= articles.get(position);
        ((ViewHolder)holder).title.setText(article.getTitle());
        ((ViewHolder)holder).pubdate.setHint(article.getPubDate());
        ((ViewHolder)holder).description.setText(Html.fromHtml(article.getDescription()));
        ((NewsAdapter.ViewHolder)holder).cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(new Intent(context,ArticleView.class));
                intent.putExtra("url",article.getLink());
                (context).startActivity(intent);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return articles.size();
    }


}