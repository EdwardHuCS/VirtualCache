package com.example.chirag.virtualcachedesign.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chirag.virtualcachedesign.service.ListItem;
import com.example.chirag.virtualcachedesign.R;
import com.example.chirag.virtualcachedesign.viewholders.RelatedVideoViewHolder;

import java.util.ArrayList;

/**
 * Created by chirag on 5/2/17.
 */

public class RelatedVideoAdapter extends RecyclerView.Adapter<RelatedVideoViewHolder> {

    private Context context;
    private ArrayList<ListItem> items = new ArrayList<>();

    public RelatedVideoAdapter(Context context, ArrayList<ListItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RelatedVideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new RelatedVideoViewHolder(context, rowView);
    }

    @Override
    public void onBindViewHolder(RelatedVideoViewHolder holder, int position) {
        holder.setItem(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
