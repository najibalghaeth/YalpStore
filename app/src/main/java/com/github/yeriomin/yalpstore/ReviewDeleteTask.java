package com.github.yeriomin.yalpstore;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.github.yeriomin.yalpstore.fragment.details.Review;

import java.io.IOException;

public class ReviewDeleteTask extends AsyncTask<String, Void, Throwable> {

    private Context context;
    private Review manager;

    public ReviewDeleteTask(Context context, Review manager) {
        this.context = context;
        this.manager = manager;
    }

    @Override
    protected void onPostExecute(Throwable e) {
        if (null == e) {
            manager.clearUserReview();
        } else {
            Log.e(DetailsActivity.class.getName(), "Error deleting the review: " + e.getMessage());
        }
    }

    @Override
    protected Throwable doInBackground(String... params) {
        PlayStoreApiWrapper wrapper = new PlayStoreApiWrapper(context);
        try {
            wrapper.deleteReview(params[0]);
        } catch (IOException e) {
            return e;
        }
        return null;
    }
}
