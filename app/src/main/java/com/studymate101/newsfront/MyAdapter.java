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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Brandon on 12/25/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    private List<ListItem> listItems;
    private Context context;

    public MyAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent,false);
        return new ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

       // holder.nametxt.setText(listItems.get(position).toString());
        final ListItem listItem = listItems.get(position);
        holder.tv_heading.setText(listItem.getHead());
        holder.tv_description.setText(listItem.getDesc());
        holder.tv_source.setText(listItem.getSource());
        holder.url_news.setText(listItem.getUrl_news());
     //   String dateIpt = listItem.getPublishedAt();

        Picasso.with(context).load(listItem.getImageUrl()).into(holder.imageView);
      /*
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH);
        inputFormat.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        SimpleDateFormat outputFormat = new SimpleDateFormat("EEE, MMM d, ''yy" );
        Date date = null;
        try {
            date = inputFormat.parse(dateIpt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputText = outputFormat.format(date);*/


        holder.publishedAt.setText(listItem.getPublishedAt());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent  intent = new Intent(view.getContext(),NewsActivity.class);
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
        return listItems.size();
    }

   /* public void add(ListItem list){

        listItems.add(list);
        notifyDataSetChanged();

    }

    public void clear(){
        listItems.clear();
    }
*/
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tv_heading,tv_source,tv_description;
        public ImageView imageView;
        public TextView url_news,publishedAt;
        public LinearLayout linearLayout;

      //  public TextView nametxt;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_heading = (TextView)itemView.findViewById(R.id.tv_heading);
            tv_description = (TextView)itemView.findViewById(R.id.tv_description);
            tv_source = (TextView)itemView.findViewById(R.id.tv_source);
            imageView =(ImageView)itemView.findViewById(R.id.imageView);
            url_news = (TextView)itemView.findViewById(R.id.url_news);
            publishedAt = (TextView)itemView.findViewById(R.id.tv_time);
            linearLayout = (LinearLayout)itemView.findViewById(R.id.linearLayout);



        }
    }

}
