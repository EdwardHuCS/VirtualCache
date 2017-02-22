package com.example.chirag.virtualcachedesign.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.chirag.virtualcachedesign.R;
import com.example.chirag.virtualcachedesign.adapters.SearchAdapter;
import com.example.chirag.virtualcachedesign.service.VideoItem;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    @Bind(R.id.ls_video)
    RecyclerView recyclerView;

    private String userID;
    private YouTube youtube;
    private String nextPageToken;
    private SearchAdapter searchAdapter;
    private String query;
    private static final long NUMBER_OF_VIDEOS_RETURNED = 20;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        recyclerView.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        handleIntent(getIntent());
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            try {
                if(intent.hasExtra("account")) {
                    userID = intent.getStringExtra("account");
                }
                ArrayList<VideoItem> items = new SearchQuery().execute(query).get();
                SearchAdapter searchAdapter = new SearchAdapter(this, items);
                recyclerView.setAdapter(searchAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public class SearchQuery extends AsyncTask<String, ArrayList<VideoItem>, ArrayList<VideoItem>> {

        @Override
        protected ArrayList<VideoItem> doInBackground(String[] params) {
            return fetchResults(params[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<VideoItem> result) {
            return;
        }
    }

    public void fetchNewSearchResults() {
        try {
            if (nextPageToken != null) {
                ArrayList<VideoItem> result = new SearchQuery().execute(query).get();
                searchAdapter.updateItems(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startPlayingActivity(String videoTitle, String videoID) {
        Intent intent = new Intent(this, PlayVideoActivity.class);
        intent.putExtra("VIDEOID", videoID);
        intent.putExtra("VIDEONAME",videoTitle);
        Location temp = getLastLocation(mGoogleApiClient);
        double[] foo={};
        if(temp!=null) {
            foo = new double[]{temp.getLongitude(), temp.getLatitude()};
        }
        intent.putExtra("LOCATION", foo);
        intent.putExtra("account", userID);
        startActivity(intent);
    }

    protected Location getLastLocation(GoogleApiClient mGoogleApiClient) {
        Log.i("VC","Get last location");
        Location mLastLocation;
        Log.d("google play onConnected", "connected");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        return mLastLocation;
    }

    private ArrayList<VideoItem> fetchResults(String keyword) {
        ArrayList<VideoItem> myArr = new ArrayList<>();
        try{
            // This object is used to make YouTube Data API requests. The last
            // argument is required, but since we don't need anything
            // initialized when the HttpRequest is initialized, we override
            // the interface and provide a no-op function.
            youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("qualityTest").build();

            // Prompt the user to enter a query term.
            String queryTerm = keyword;

            // Define the API request for retrieving search results.
            YouTube.Search.List search = youtube.search().list("id,snippet");

            search.setKey("AIzaSyAzmrXIdc2sU6zqUUhCBLsxCtoB1EtoicM");
            //search.setOauthToken(token);
            search.setQ(queryTerm);

            // Restrict the search results to only include videos. See:
            // https://developers.google.com/youtube/v3/docs/search/list#type
            search.setType("video");

            if(nextPageToken != null) {
                search.setPageToken(nextPageToken);
            }

            // To increase efficiency, only retrieve the fields that the
            // application uses.
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url),nextPageToken");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

            // Call the API and print results.
            SearchListResponse searchResponse = search.execute();
            if (searchResponse == null) System.out.println("search result is null");

            nextPageToken = searchResponse.getNextPageToken();
            List<SearchResult> searchResultList = searchResponse.getItems();
            Iterator<SearchResult> iteratorSearchResults = searchResultList.iterator();

            if (searchResultList != null) {
//                Log.i("searchResponse", "search result is not null");

                final List<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();
                if (!iteratorSearchResults.hasNext()) {
//                    Log.i("SearchYoutube"," There aren't any results for your query.");
                }

                while (iteratorSearchResults.hasNext()) {
                    SearchResult singleVideo = iteratorSearchResults.next();
                    ResourceId rId = singleVideo.getId();
//                    prettyPrint(searchResultList.iterator(), queryTerm);
                    if (rId.getKind().equals("youtube#video")) {
                        Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
                        String url = thumbnail.getUrl();
                        String channelTitle = singleVideo.getSnippet().getChannelTitle();

                        VideoItem newItem = new VideoItem();

                        System.out.println("URL is " + url);
                        newItem.setThumbnailURL(url.toString());
                        newItem.setTitle(singleVideo.getSnippet().getTitle().toString());
                        newItem.setId(rId.getVideoId().toString());
                        newItem.setChannelTitle(channelTitle);
                        myArr.add(newItem);
                    }
                }
                Log.i("Before Return", "return ArrayList");
            }
        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return myArr;
    }
}