package com.example.chirag.virtualcachedesign.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chirag.virtualcachedesign.service.Constants;
import com.example.chirag.virtualcachedesign.R;
import com.example.chirag.virtualcachedesign.viewholders.SearchViewHolder;
import com.example.chirag.virtualcachedesign.service.SubListItem;
import com.example.chirag.virtualcachedesign.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<SearchViewHolder>{
    private Context context;
    private List<SubListItem.Item> items = new ArrayList<>();
    private int pageNumber=1;
    private static int RESULT_SIZE = 20;
    int fragment;

    public DataAdapter(Context context, List<SubListItem.Item> items, int fragment) {
        this.context = context;
        this.items = items;
        this.fragment = fragment;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_data, parent, false);
        return new SearchViewHolder(context, rowView, fragment);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        if(position==(RESULT_SIZE-1)*pageNumber) {
            if(fragment== Constants.TRENDING_FRAGMENT) {
                ((MainActivity) context).fetchNewSearchResults();
            } else if (fragment == Constants.SUBSCRIPTION_FRAGMENT) {
                ((MainActivity) context).fetchNewSearchResults();
            }
            pageNumber++;
        }
        holder.setSearchItem(items.get(position));
        holder.setOnClick();
    }

    @Override
    public int getItemCount() {
        if(items == null) {
            return 0;
        }
        return items.size();
    }

    public void updateItems(List<SubListItem.Item> results) {
        if(items==null) {
            items = new ArrayList<>();
        }
        items.addAll(results);
        notifyDataSetChanged();
    }

    public void clear() {
        if(items!=null) {
            items.clear();
        }
    }
}
