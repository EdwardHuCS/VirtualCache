package com.example.chirag.virtualcachedesign.viewholders;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chirag.virtualcachedesign.service.ListItem;
import com.example.chirag.virtualcachedesign.R;
import com.example.chirag.virtualcachedesign.activities.PlayVideoActivity;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chirag on 5/2/17.
 */

public class RelatedVideoViewHolder extends RecyclerView.ViewHolder{

    @Bind(R.id.photo)
    ImageView photo;

    @Bind(R.id.name)
    TextView name;

    private Context context;
    private View view;
    private ListItem item;

    public RelatedVideoViewHolder(Context context, View view) {
        super(view);
        this.context = context;
        this.view = view;
        ButterKnife.bind(this, view);
    }

    public void setItem(ListItem item) {
        this.item = item;
        Picasso.with(context).load(item.getUrl()).into(photo);
        name.setText(item.getVideoTitle());
        Typeface font = Typeface.createFromAsset(context.getAssets(), "Avenir-Book.ttf");
        name.setTypeface(font);
        setOnCLick();
    }

    private void setOnCLick() {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PlayVideoActivity)context).play(item);
            }
        });
    }
}
