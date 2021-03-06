package com.example.chirag.virtualcachedesign.service;

import com.google.api.services.youtube.model.ChannelContentDetails;
import com.google.api.services.youtube.model.ResourceId;

import java.util.ArrayList;

public class SubListItem {
    public String kind;
    public String etag;
    public String nextPageToken;
    public String prevPageToken;
    public PageInfo pageInfo;
    public ArrayList<Item> items;

    public class PageInfo {
        public int totalResults;
        public int resultsPerPage;
    }

    public class Item {
        public String kind;
        public String etag;
        public String id;
        public Snippet snippet;
        public ContentDetails contentDetails;
        public SubscriberSnippet subscriberSnippet;
    }

    public class Snippet {
        public String publishedAt;
        public String channelTitle;
        public String title;
        public String description;
        public ResourceId resourceId;
        public String channelId;
        public Thumbnails thumbnails;
        public String playlistId;
        public int position;
    }

    public class Thumbnails {
        public Default aDefault;
        public Medium medium;
        public High high;
        public Standard standard;
        public Maxres maxres;
        public class Default {
            public String url;
            public int width;
            public int height;
        }
        public class Medium {
            public String url;
            public int width;
            public int height;
        }
        public class High {
            public String url;
            public int width;
            public int height;
        }
        public class Standard {
            public String url;
            public int width;
            public int height;
        }
        public class Maxres {
            public String url;
            public int width;
            public int height;
        }
    }

    public class ContentDetails {
        public int totalItemCount;
        public int newItemCount;
        public String activityType;
        public ChannelContentDetails.RelatedPlaylists relatedPlaylists;
    }

    public class SubscriberSnippet {
        public String title;
        public String description;
        public String channelId;
        public Thumbnails thumbnails;
    }
}


