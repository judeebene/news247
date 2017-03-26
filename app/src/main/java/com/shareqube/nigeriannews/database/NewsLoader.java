package com.shareqube.nigeriannews.database;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

/**
 * Created by judeebene on 3/24/17.
 */

public class NewsLoader extends CursorLoader {

    public static NewsLoader newAllArticlesInstance(Context context) {
        return new NewsLoader(context, FeedContract.Entry.buildDirUri());
    }

    public static NewsLoader newInstanceForItemId(Context context, long itemId) {
        return new NewsLoader(context, FeedContract.Entry.buildItemUri(itemId));
    }

    private NewsLoader(Context context, Uri uri) {
        super(context, uri, Query.PROJECTION, null, null, FeedContract.Entry.COLUMN_NAME_PUBLISHED+ " DESC");
    }

    public interface Query {
        String[] PROJECTION = {
                FeedContract.Entry._ID,
                FeedContract.Entry.COLUMN_NAME_TITLE,
                FeedContract.Entry.COLUMN_NAME_PUBLISHED,
                FeedContract.Entry.COLUMN_NAME_LINK,
                FeedContract.Entry.COLUMN_NAME_DESCRIPTION,
                FeedContract.Entry.COLUMN_NAME_IMAGE_URL,
                FeedContract.Entry.COLUMN_NAME_IMAGE_URL_2,
        };

        int COLUMN_ID = 0;
        int COLUMN_TITLE = 1;
        int COLUMN_PUB_DATE = 2;
        int COLUMN_LINK = 3 ;
        int COLUMN_DESC = 4;
        int COLUMN_PHOTO_URL = 5;
        int COLUMN_PHOTO_URL2 = 6;
    }
}
