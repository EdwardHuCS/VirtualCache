package com.example.chirag.virtualcachedesign.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chirag.virtualcachedesign.service.Constants;
import com.example.chirag.virtualcachedesign.adapters.DataAdapter;
import com.example.chirag.virtualcachedesign.R;
import com.example.chirag.virtualcachedesign.service.SubListItem;
import com.example.chirag.virtualcachedesign.adapters.SubscriptionAdapter;
import com.example.chirag.virtualcachedesign.activities.MainActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class SubscriptionFragment extends Fragment {

    @Bind(R.id.ls_video)
    RecyclerView recyclerView;

    @Bind(R.id.flags)
    RecyclerView subscriptionRecyclerView;

    private String nextPageToken;
    private String playListId;
    private int totalResults;
    private int resultsPerPage;
    private String UserID;
    private SubListItem subListItem;
    private SubscriptionAdapter subscriptionAdapter;
    private DataAdapter dataAdapter;
    AsyncHttpClient respClient = new AsyncHttpClient();
    AsyncHttpClient getListIDClient = new AsyncHttpClient();
    AsyncHttpClient getListClient = new AsyncHttpClient();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_trending, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        String accessToken;
        accessToken = ((MainActivity) getActivity()).accessToken;
        recyclerView.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        subscriptionRecyclerView.hasFixedSize();
        LinearLayoutManager linearFlagLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        subscriptionRecyclerView.setLayoutManager(linearFlagLayoutManager);

        respClient.get("https://www.googleapis.com/youtube/v3/subscriptions?part=snippet,contentDetails&mine=true&maxResults=50&access_token=" + accessToken, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                int j=0;
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new GsonBuilder().create();
                // Define Response class to correspond to the JSON response returned
                subListItem = gson.fromJson(responseString, SubListItem.class);
                nextPageToken = subListItem.nextPageToken;
                totalResults = subListItem.pageInfo.totalResults;
                resultsPerPage = subListItem.pageInfo.resultsPerPage;
                subscriptionAdapter = new SubscriptionAdapter(getActivity(), subListItem.items);
                subscriptionRecyclerView.setAdapter(subscriptionAdapter);
                if(subListItem.items.size()!=0) {
                    fetchChannels(subListItem.items.get(0));
                }
                //recyclerView.setAdapter(dataAdapter);
            }
        });
    }

    public void fetchChannels(SubListItem.Item item) {
        getListIDClient.get("https://www.googleapis.com/youtube/v3/channels?part=contentDetails&id="
                + item.snippet.resourceId.getChannelId() + "&maxResults=25&key=AIzaSyAzmrXIdc2sU6zqUUhCBLsxCtoB1EtoicM", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                int i=0;
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, final String res) {
                Gson gson = new GsonBuilder().create();
                // Define Response class to correspond to the JSON response returned
                SubListItem channelList = gson.fromJson(res, SubListItem.class);
                playListId = channelList.items.get(0).contentDetails.relatedPlaylists.getUploads();
                getListClient.get("https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=" + playListId + "&maxResults=25&key=AIzaSyAzmrXIdc2sU6zqUUhCBLsxCtoB1EtoicM", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Gson gson = new GsonBuilder().create();
                        subListItem = gson.fromJson(responseString, SubListItem.class);
                        nextPageToken = subListItem.nextPageToken;
                        totalResults = subListItem.pageInfo.totalResults;
                        resultsPerPage = subListItem.pageInfo.resultsPerPage;
                        dataAdapter = new DataAdapter(getActivity(), subListItem.items, Constants.SUBSCRIPTION_FRAGMENT);
                        recyclerView.setAdapter(dataAdapter);
                    }
                });

            }
        });
    }
}