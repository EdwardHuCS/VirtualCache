package com.example.chirag.virtualcachedesign.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import com.example.chirag.virtualcachedesign.service.Constants;
import com.example.chirag.virtualcachedesign.R;
import com.example.chirag.virtualcachedesign.viewholders.SearchViewHolder;
import com.example.chirag.virtualcachedesign.service.VideoItem;
import com.example.chirag.virtualcachedesign.activities.SearchActivity;

import java.util.List;

/**
 * Created by chirag on 15/10/16.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder>{
    private Context context;
    private List<VideoItem> items = new ArrayList<>();
    private int pageNumber=1;
    private static int RESULT_SIZE = 20;

    public SearchAdapter(Context context, List<VideoItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_data, parent, false);
        return new SearchViewHolder(context, rowView, Constants.SEARCH_ACTIVITY);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        if(position==(RESULT_SIZE-1)*pageNumber) {
            ((SearchActivity)context).fetchNewSearchResults();
            pageNumber++;
        }
        holder.setSearchItem(items.get(position));
        holder.setOnClick();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItems(List<VideoItem> results) {
        items.addAll(results);
        notifyDataSetChanged();
    }
}
