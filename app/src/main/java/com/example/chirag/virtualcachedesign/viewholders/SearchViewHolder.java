package com.example.chirag.virtualcachedesign.viewholders;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.example.chirag.virtualcachedesign.service.Constants;
import com.example.chirag.virtualcachedesign.R;
import com.example.chirag.virtualcachedesign.service.SubListItem;
import com.example.chirag.virtualcachedesign.service.VideoItem;
import com.example.chirag.virtualcachedesign.activities.MainActivity;
import com.example.chirag.virtualcachedesign.activities.SearchActivity;
import com.squareup.picasso.Picasso;

/**
 * Created by chirag on 15/10/16.
 */

public class SearchViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.photo)
    ImageView imageView;

    @Bind(R.id.name)
    TextView itemName;

    @Bind(R.id.channelName)
    TextView channelName;

    @Bind(R.id.id)
    TextView videoId;

    private Context context;
    private int fragment;
    private SubListItem.Item item;
    private VideoItem result;

    public SearchViewHolder(final Context context, View itemView, final int fragment) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = context;
        this.fragment = fragment;
    }

    public void setSearchItem(VideoItem result) {
        this.result = result;
        Picasso.with(context).load(result.getThumbnailURL()).into(imageView);
        itemName.setText(result.getTitle());
        Typeface font = Typeface.createFromAsset(context.getAssets(), "Avenir-Book.ttf");
        itemName.setTypeface(font);
        channelName.setText(result.getChannelTitle());
        Typeface font1 = Typeface.createFromAsset(context.getAssets(), "Avenir-Light.ttf");
        channelName.setTypeface(font1);
        videoId.setText(result.getId());
    }

    public void setSearchItem(SubListItem.Item result) {
        this.item = result;
        Picasso.with(context).load(result.snippet.thumbnails.medium.url).into(imageView);
        itemName.setText(result.snippet.title);
        Typeface font = Typeface.createFromAsset(context.getAssets(), "Avenir-Book.ttf");
        itemName.setTypeface(font);
        channelName.setText(result.snippet.channelTitle);
        Typeface font1 = Typeface.createFromAsset(context.getAssets(), "Avenir-Light.ttf");
        channelName.setTypeface(font1);
        //videoId.setText(result.getId());
    }

    public void setOnClick() {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fragment== Constants.SUBSCRIPTION_FRAGMENT) {
                    ((MainActivity)context).startPlayingActivity(item.snippet.title,item.snippet.resourceId.getVideoId());
                } else if (fragment== Constants.TRENDING_FRAGMENT) {
                    ((MainActivity)context).startPlayingActivity(item.snippet.title, item.id);
                } else if(fragment== Constants.SEARCH_ACTIVITY) {
                    ((SearchActivity)context).startPlayingActivity(result.getTitle(), result.getId());
                }
            }
        });
    }
}
