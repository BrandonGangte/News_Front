package com.studymate101.newsfront;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Brandon on 1/17/2018.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHandler> {

    private List<ListItem> searchItems;
    private Context context;

    public SearchAdapter(List<ListItem> searchItems, Context context) {
        this.searchItems = searchItems;
        this.context = context;
    }
    @Override
    public SearchAdapter.ViewHandler onCreateViewHolder(ViewGroup parent, int viewType) {
        View vi = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list, parent,false);
        return new ViewHandler(vi);
    }

    @Override
    public void onBindViewHolder(SearchAdapter.ViewHandler holder, int position) {

        final ListItem listItem = searchItems.get(position);
        holder.tv_heading2.setText(listItem.getHead());
        holder.tv_description2.setText(listItem.getDesc());
        holder.tv_source2.setText(listItem.getSource());
        holder.url_news2.setText(listItem.getUrl_news());
        holder.published.setText(listItem.getPublishedAt());
        Picasso.with(context).load(listItem.getImageUrl()).into(holder.imageView2);

        holder.linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(),NewsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("urlLoad",listItem.getUrl_news());
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);


            /*   Intent i = new Intent(context,NewsActivity.class);
               i.putExtra("urltoLoad",listItem.getUrl_news());
               context.startActivity(i);*/
                //Toast.makeText(context,"You'ev clicked "+listItem.getUrl_news(),Toast.LENGTH_LONG).show();
           /*     Intent web = new Intent(Intent.ACTION_VIEW);
                web.setData(Uri.parse(listItem.getUrl_news()));
                context.startActivity(web);*/


            }
        });


    }

    @Override
    public int getItemCount() {
        return searchItems.size();
    }


    public class ViewHandler extends RecyclerView.ViewHolder{

        public TextView tv_heading2,tv_source2,tv_description2;
        public ImageView imageView2;
        public TextView url_news2,published;
        public LinearLayout linearLayout2;
        //  public TextView nametxt;

        public ViewHandler(View itemView) {
            super(itemView);

            tv_heading2 = (TextView)itemView.findViewById(R.id.tv_heading2);
            tv_description2 = (TextView)itemView.findViewById(R.id.tv_description2);
            tv_source2 = (TextView)itemView.findViewById(R.id.tv_source2);
            imageView2 =(ImageView)itemView.findViewById(R.id.imageView2);
            url_news2 = (TextView)itemView.findViewById(R.id.url_news2);
            published = (TextView)itemView.findViewById(R.id.tv_published);
            linearLayout2 = (LinearLayout)itemView.findViewById(R.id.linearLayout2);


        }
    }
}
