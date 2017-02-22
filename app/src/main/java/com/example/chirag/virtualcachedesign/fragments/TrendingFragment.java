package com.example.chirag.virtualcachedesign.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chirag.virtualcachedesign.service.Constants;
import com.example.chirag.virtualcachedesign.adapters.DataAdapter;
import com.example.chirag.virtualcachedesign.adapters.FlagAdapter;
import com.example.chirag.virtualcachedesign.R;
import com.example.chirag.virtualcachedesign.service.SubListItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class TrendingFragment extends Fragment {

    @Bind(R.id.ls_video)
    RecyclerView recyclerView;

    @Bind(R.id.flags)
    RecyclerView flagRecyclerView;

    AsyncHttpClient trending_video_list = new AsyncHttpClient();
    SubListItem subListItem;
    private String nextPageToken;
    private int totalResults;
    private int resultsPerPage;
    HashMap<String, String> region_map = new HashMap<String, String>();
    private static int mCurrentCounter = 0;
    private DataAdapter dataAdapter;
    private FlagAdapter flagAdapter;
    private String countryCode;
    boolean moreResults;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_trending, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        recyclerView.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        flagRecyclerView.hasFixedSize();
        LinearLayoutManager linearFlagLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        flagRecyclerView.setLayoutManager(linearFlagLayoutManager);
        flagAdapter = new FlagAdapter(getActivity());
        flagRecyclerView.setAdapter(flagAdapter);
        dataAdapter = new DataAdapter(getActivity(), null, Constants.TRENDING_FRAGMENT);
        recyclerView.setAdapter(dataAdapter);
        countryCode = "US";
        fetchTrendingResults(countryCode);
    }

    public void fetchTrendingResults(String countryCode) {
        moreResults = false;
        String trend_url = "https://www.googleapis.com/youtube/v3/videos?part=contentDetails,statistics,status,snippet&chart=mostPopular&regionCode=" + countryCode +"&maxResults=25&key=AIzaSyAzmrXIdc2sU6zqUUhCBLsxCtoB1EtoicM";
        fetchResults(trend_url);
    }

    public void fetchResults(String trend_url) {
        AsyncHttpClient trending_video_list = new AsyncHttpClient();
        trending_video_list.get(trend_url, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("VirtualCache", "cannot obtain video infomation due to http request fail");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new GsonBuilder().create();
                subListItem = gson.fromJson(responseString, SubListItem.class);
                nextPageToken = subListItem.nextPageToken;
                totalResults = subListItem.pageInfo.totalResults;
                resultsPerPage = subListItem.pageInfo.resultsPerPage;
                mCurrentCounter = subListItem.items.size();
                if (moreResults) {
                    dataAdapter.updateItems(subListItem.items);
                } else {
                    dataAdapter.clear();
                    dataAdapter.updateItems(subListItem.items);
                }
            }
        });
    }

    public void fetchNewSearchResults() {
        moreResults = true;
        String trend_url = "https://www.googleapis.com/youtube/v3/videos?pageToken="
                + nextPageToken +"&part=contentDetails,statistics,status,snippet&chart=mostPopular&regionCode="
                + countryCode +"&maxResults=25&key=AIzaSyAzmrXIdc2sU6zqUUhCBLsxCtoB1EtoicM";
        fetchResults(trend_url);
    }
}