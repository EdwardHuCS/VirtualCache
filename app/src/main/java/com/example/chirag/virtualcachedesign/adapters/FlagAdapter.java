package com.example.chirag.virtualcachedesign.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chirag.virtualcachedesign.viewholders.FlagViewHolder;
import com.example.chirag.virtualcachedesign.R;

public class FlagAdapter extends RecyclerView.Adapter<FlagViewHolder>{
    private Context context;
    private int flags[] = {R.drawable.us, R.drawable.mx, R.drawable.in, R.drawable.uk,
            R.drawable.jp, R.drawable.fr, R.drawable.ca};
    private String flagCodes[] = {"US","MX","IN","UK","JP","FR","CA"};

    public FlagAdapter(Context context) {
        this.context = context;
    }

    @Override
    public FlagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.circle_view, parent, false);
        return new FlagViewHolder(rowView, context);
    }

    @Override
    public void onBindViewHolder(FlagViewHolder holder, int position) {
        holder.setFlag(flags[position], flagCodes[position]);
    }

    @Override
    public int getItemCount() {
        return flags.length;
    }
}
