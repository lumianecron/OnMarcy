package model;

import android.app.Activity;
import android.os.AsyncTask;

import com.android.onmarcy.Global;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class SocialMedia {
    @SerializedName("id")
    private String id;

    @SerializedName("total_post")
    private int total_post;

    @SerializedName("status_active")
    private int status_active;

    public SocialMedia(String id, int total_post, int status_active) {
        this.id = id;
        this.total_post = total_post;
        this.status_active = status_active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTotal_post() {
        return total_post;
    }

    public void setTotal_post(int total_post) {
        this.total_post = total_post;
    }

    public int getStatus_active() {
        return status_active;
    }

    public void setStatus_active(int status_active) {
        this.status_active = status_active;
    }

    public interface CallbackSelect{
        void success(JSONObject jsonObject);
        void error();
    }

    public interface Callback {
        void success();
        void error();
    }

    public SocialMedia(JSONObject jsonObject){
        try{
            this.id = jsonObject.has("id") ? jsonObject.getString("id") : "";
            this.total_post = jsonObject.has("total_post") ? jsonObject.getInt("total_post") : 0;
            this.status_active = jsonObject.has("status_active") ? jsonObject.getInt("status_active") : 0;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void select(Activity activity, CallbackSelect callback) {
        new select(activity, callback).execute("v1/socialmedia/select");
    }

    private static class select extends AsyncTask<String, Void, String>{
        final WeakReference<Activity> activity;
        final CallbackSelect callback;

        private select(Activity activity, CallbackSelect callback) {
            this.activity = new WeakReference<>(activity);
            this.callback = callback;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            return Global.executePost(urls[0], jsonObject, 3000);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try{
                JSONObject jsonObject = new JSONObject(result);
                if(jsonObject.getBoolean(Global.RESPONSE_SUCCESS)){
                    callback.success(jsonObject.getJSONObject(Global.RESPONSE_DATA));
                }else{
                    callback.error();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void insert(Activity activity, String username, int socialmedia, int category, int city, String id, int totalPost, int totalFollower, int totalFollowing
            , int totalComment, int totalLike, int marketAgeMin, int marketAgeMax, int marketMale, int marketFemale, String timePosting, int servicePost
            , int serviceStory, int serviceBio, String code, Boolean useLoading, Callback callback) {
        new insert(activity, username, socialmedia, category, city, id, totalPost, totalFollower, totalFollowing, totalComment, totalLike, marketAgeMin, marketAgeMax, marketMale, marketFemale, timePosting, servicePost, serviceStory, serviceBio, code, useLoading, callback).execute("v1/socialmedia/insert");
    }

    private static class insert extends AsyncTask<String, Void, String> {
        final WeakReference<Activity> activity;
        final Callback callback;
        final Boolean useLoading;
        final String username;
        final int socialmedia;
        final int category;
        final int city;
        final String id;
        final int totalPost;
        final int totalFollower;
        final int totalFollowing;
        final int totalComment;
        final int totalLike;
        final int marketAgeMin;
        final int marketAgeMax;
        final int marketMale;
        final int marketFemale;
        final String timePosting;
        final int servicePost;
        final int serviceStory;
        final int serviceBio;
        final String code;

        private insert(Activity activity, String username, int socialmedia, int category, int city, String id, int totalPost, int totalFollower, int totalFollowing
                , int totalComment, int totalLike, int marketAgeMin, int marketAgeMax, int marketMale, int marketFemale, String timePosting, int servicePost
                , int serviceStory, int serviceBio, String code, Boolean useLoading, Callback callback) {
            this.activity = new WeakReference<>(activity);
            this.callback = callback;
            this.useLoading = useLoading;
            this.username = username;
            this.socialmedia = socialmedia;
            this.category = category;
            this.city = city;
            this.id = id;
            this.totalPost = totalPost;
            this.totalFollower = totalFollower;
            this.totalFollowing = totalFollowing;
            this.totalComment = totalComment;
            this.totalLike = totalLike;
            this.marketAgeMin = marketAgeMin;
            this.marketAgeMax = marketAgeMax;
            this.marketMale = marketMale;
            this.marketFemale = marketFemale;
            this.timePosting = timePosting;
            this.servicePost = servicePost;
            this.serviceStory = serviceStory;
            this.serviceBio = serviceBio;
            this.code = code;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                User user = new User(new JSONObject(Global.getShared(Global.SHARED_INDEX.USER, "{}")));
                jsonObject.put("hash", user.getHash());
                jsonObject.put("socialmedia", socialmedia);
                jsonObject.put("category", category);
                jsonObject.put("city", city);
                jsonObject.put("id", id);
                jsonObject.put("total_post", totalPost);
                jsonObject.put("total_follower", totalFollower);
                jsonObject.put("total_following", totalFollowing);
                jsonObject.put("total_comment", totalComment);
                jsonObject.put("total_like", totalLike);
                jsonObject.put("market_age_min", marketAgeMin);
                jsonObject.put("market_age_max", marketAgeMax);
                jsonObject.put("market_male", marketMale);
                jsonObject.put("market_female", marketFemale);
                jsonObject.put("time_posting", timePosting);
                jsonObject.put("service_post", servicePost);
                jsonObject.put("service_story", serviceStory);
                jsonObject.put("service_bio", serviceBio);
//                jsonObject.put("code", code);
            }
            catch (JSONException e) { e.printStackTrace(); }

            return Global.executePost(urls[0], jsonObject, 3000);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(useLoading) Global.hideLoading();
            try {
                JSONObject json = new JSONObject(result);
                if(json.getBoolean(Global.RESPONSE_SUCCESS)) {
                    callback.success();
                }
                else {
                    if(Global.RESPONSE_CODE.equals("401")) User.logout();
                    else callback.error();
                }
            }
            catch(Exception e) {
                Global.showLoading(activity.get(), "", e.getMessage());
                e.printStackTrace();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(useLoading) Global.showLoading(activity.get(), "", "Loading");
        }
    }
}
