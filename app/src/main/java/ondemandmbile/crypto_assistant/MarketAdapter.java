package ondemandmbile.crypto_assistant;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;

import io.realm.RealmResults;
import ondemandmbile.crypto_assistant.models.Currency;


/**
 * Created by robertnyangate on 18/05/2017.
 */

public class MarketAdapter extends RecyclerView.Adapter {
    private static final String TAG = "BambaAdapter";
    private Context context;
    private LayoutInflater layoutInflater;
    RealmResults<Currency> conversions;
    public MarketAdapter(Context context, RealmResults<Currency>conversions) {
        try {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
            this.conversions=conversions;
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static interface OnRemoveCompe {
        void onRemoveCompetitor(String id);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name, buy,sell;
        public Toolbar toolbar;
        public ImageView image;

        public ViewHolder(View convertView) {
            super(convertView);
            name = (TextView) convertView.findViewById(R.id.currency);
            buy = (TextView) convertView.findViewById(R.id.buying);
            sell = (TextView) convertView.findViewById(R.id.selling);


        }


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.convertions_adapter, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final Currency competitor=conversions.get(position);
        DecimalFormat decimalFormat=new DecimalFormat("#,###.##");
        ((ViewHolder)holder).name.setText(competitor.getName());
        ((ViewHolder)holder).buy.setText(competitor.getSymbol()+" "+decimalFormat.format
                (competitor.getBuy()));
        ((ViewHolder)holder).sell.setText(competitor.getSymbol()+" "+decimalFormat.format
                (competitor.getSell()));
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
        return conversions.size();
    }


}