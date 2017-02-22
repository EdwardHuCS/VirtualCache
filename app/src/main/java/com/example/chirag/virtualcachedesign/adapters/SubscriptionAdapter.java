package com.example.chirag.virtualcachedesign.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chirag.virtualcachedesign.R;
import com.example.chirag.virtualcachedesign.service.SubListItem;
import com.example.chirag.virtualcachedesign.viewholders.SubscriptionViewHolder;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionViewHolder>{

    private Context context;
    private List<SubListItem.Item> items = new ArrayList<>();

    public SubscriptionAdapter(Context context, List<SubListItem.Item> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public SubscriptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.circle_view, parent, false);
        return new SubscriptionViewHolder(rowView, context);
    }

    @Override
    public void onBindViewHolder(SubscriptionViewHolder holder, int position) {
        holder.setSubscriptionImage(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
