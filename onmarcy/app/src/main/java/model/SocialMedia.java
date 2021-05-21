package model;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.android.onmarcy.Global;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class SocialMedia {
    @SerializedName("code")
    private int code;

    @SerializedName("id")
    private String id;

    @SerializedName("total_post")
    private int totalPost;

    @SerializedName("status_active")
    private int statusActive;

    @SerializedName("username")
    private String username;

    @SerializedName("socialmedia")
    private int socialmedia;

    @SerializedName("category_id")
    private int category;

    @SerializedName("category_name")
    private String categoryName;

    @SerializedName("total_follower")
    private int totalFollower;

    @SerializedName("total_following")
    private int totalFollowing;

    @SerializedName("total_comment")
    private int totalComment;

    @SerializedName("total_like")
    private int totalLike;

    @SerializedName("market_age_min")
    private int marketAgeMin;

    @SerializedName("market_age_max")
    private int marketAgeMax;

    @SerializedName("market_male")
    private int marketMale;

    @SerializedName("market_female")
    private int marketFemale;

    @SerializedName("time_posting")
    private String timePosting;

    @SerializedName("service_post")
    private int servicePost;

    @SerializedName("service_story")
    private int serviceStory;

    @SerializedName("service_bio")
    private int serviceBio;

    @SerializedName("status_verifiy")
    private int statusVerify;

    @SerializedName("grade")
    private double grade;

    @SerializedName("score")
    private double score;

    public int getCode() { return code; }

    public void setCode(int code) { this.code = code; }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatusActive() {return statusActive;}

    public void setStatusActive(int statusActive) {this.statusActive = statusActive;}

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public int getSocialmedia() {return socialmedia;}

    public void setSocialmedia(int socialmedia) {this.socialmedia = socialmedia;}

    public int getCategory() {return category;}

    public void setCategory(int category) {this.category = category;}

    public int getTotalPost() {return totalPost;}

    public void setTotalPost(int totalPost) {this.totalPost = totalPost;}

    public int getTotalFollower() {return totalFollower;}

    public void setTotalFollower(int totalFollower) {this.totalFollower = totalFollower;}

    public int getTotalFollowing() {return totalFollowing;}

    public void setTotalFollowing(int totalFollowing) {this.totalFollowing = totalFollowing;}

    public int getTotalComment() {return totalComment;}

    public void setTotalComment(int totalComment) {this.totalComment = totalComment;}

    public int getTotalLike() {return totalLike;}

    public void setTotalLike(int totalLike) {this.totalLike = totalLike;}

    public int getMarketAgeMin() {return marketAgeMin;}

    public void setMarketAgeMin(int marketAgeMin) {this.marketAgeMin = marketAgeMin;}

    public int getMarketAgeMax() {return marketAgeMax;}

    public void setMarketAgeMax(int marketAgeMax) {this.marketAgeMax = marketAgeMax;}

    public int getMarketMale() {return marketMale;}

    public void setMarketMale(int marketMale) {this.marketMale = marketMale;}

    public int getMarketFemale() {return marketFemale;}

    public void setMarketFemale(int marketFemale) {this.marketFemale = marketFemale;}

    public String getTimePosting() {return timePosting;}

    public void setTimePosting(String timePosting) {this.timePosting = timePosting;}

    public int getServicePost() {return servicePost;}

    public void setServicePost(int servicePost) {this.servicePost = servicePost;}

    public int getServiceStory() {return serviceStory;}

    public void setServiceStory(int serviceStory) {this.serviceStory = serviceStory;}

    public int getServiceBio() {return serviceBio;}

    public void setServiceBio(int serviceBio) {this.serviceBio = serviceBio;}

    public int getStatusVerify() {return statusVerify;}

    public void setStatusVerify(int statusVerify) {this.statusVerify = statusVerify;}

    public double getGrade() { return grade; }

    public void setGrade(double grade) { this.grade = grade; }

    public double getScore() { return score; }

    public void setScore(double score) { this.score = score; }

    public String getCategoryName() {return categoryName;}

    public void setCategoryName(String categoryName) {this.categoryName = categoryName;}

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
            this.code = jsonObject.has("code") ? jsonObject.getInt("code") : 0;
            this.id = jsonObject.has("id") ? jsonObject.getString("id") : "";
            this.category = jsonObject.has("category_id") ? jsonObject.getInt("category_id") : 0;
            this.categoryName = jsonObject.has("category_name") ? jsonObject.getString("category_name") : "";
            this.totalPost = jsonObject.has("total_post") ? jsonObject.getInt("total_post") : 0;
            this.statusActive = jsonObject.has("status_active") ? jsonObject.getInt("status_active") : 0;
            this.totalFollower = jsonObject.has("total_follower") ? jsonObject.getInt("total_follower") : 0;
            this.totalFollowing = jsonObject.has("total_following") ? jsonObject.getInt("total_following") : 0;
            this.totalComment = jsonObject.has("total_comment") ? jsonObject.getInt("total_comment") : 0;
            this.totalLike = jsonObject.has("total_like") ? jsonObject.getInt("total_like") : 0;
            this.marketAgeMax = jsonObject.has("market_age_max") ? jsonObject.getInt("market_age_max") : 0;
            this.marketAgeMin = jsonObject.has("market_age_min") ? jsonObject.getInt("market_age_min") : 0;
            this.marketMale = jsonObject.has("market_male") ? jsonObject.getInt("market_male") : 0;
            this.marketFemale = jsonObject.has("market_female") ? jsonObject.getInt("market_female") : 0;
            this.timePosting = jsonObject.has("time_posting") ? jsonObject.getString("time_posting") : "";
            this.grade = jsonObject.has("grade") ? jsonObject.getDouble("grade") : 0;
            this.score = jsonObject.has("score") ? jsonObject.getDouble("score") : 0;
            this.servicePost = jsonObject.has("service_post") ? jsonObject.getInt("service_post") : 0;
            this.serviceBio = jsonObject.has("service_bio") ? jsonObject.getInt("service_bio") : 0;
            this.serviceStory = jsonObject.has("service_story") ? jsonObject.getInt("service_story") : 0;
            this.statusVerify = jsonObject.has("status_verify") ? jsonObject.getInt("status_verify") : 0;
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
            try {
                User user = new User(new JSONObject(Global.getShared(Global.SHARED_INDEX.USER, "{}")));
                jsonObject.put("hash", user.getHash());
                jsonObject.put("socialmedia", 1);
            }
            catch (JSONException e) { e.printStackTrace(); }
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

    public static void detail(Activity activity, String username, CallbackSelect callback) {
        new detail(activity, username, callback).execute("v1/socialmedia/select");
    }

    private static class detail extends AsyncTask<String, Void, String>{
        final WeakReference<Activity> activity;
        final CallbackSelect callback;
        final String username;

        private detail(Activity activity, String username, CallbackSelect callback) {
            this.activity = new WeakReference<>(activity);
            this.callback = callback;
            this.username = username;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", username);
                jsonObject.put("socialmedia", 1);
            }
            catch (JSONException e) { e.printStackTrace(); }
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

    public static void verify(Activity activity, String verificationCode, Boolean useLoading, Callback callback) {
        new verify(activity, verificationCode, useLoading, callback).execute("v1/socialmedia/verify");
    }

    private static class verify extends AsyncTask<String, Void, String> {
        final WeakReference<Activity> activity;
        final Callback callback;
        final Boolean useLoading;
        final String verificationCode;

        private verify(Activity activity, String verificationCode, Boolean useLoading, Callback callback) {
            this.activity = new WeakReference<>(activity);
            this.callback = callback;
            this.useLoading = useLoading;
            this.verificationCode = verificationCode;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("code", verificationCode);
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
                    callback.error();
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

    // UPDATE SOCIAL MEDIA
    public static void update(Activity activity, String username, int socialmedia, int category, int city, String id, int totalPost, int totalFollower, int totalFollowing
            , int totalComment, int totalLike, int marketAgeMin, int marketAgeMax, int marketMale, int marketFemale, String timePosting, int servicePost
            , int serviceStory, int serviceBio, String code, Boolean useLoading, Callback callback) {
        new update(activity, username, socialmedia, category, city, id, totalPost, totalFollower, totalFollowing, totalComment, totalLike, marketAgeMin, marketAgeMax, marketMale, marketFemale, timePosting, servicePost, serviceStory, serviceBio, code, useLoading, callback).execute("v1/socialmedia/update");
    }

    private static class update extends AsyncTask<String, Void, String> {
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

        private update(Activity activity, String username, int socialmedia, int category, int city, String id, int totalPost, int totalFollower, int totalFollowing
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
                jsonObject.put("code", code);
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

    public static void update_info(Activity activity, String username, int socialmedia, int totalPost, int totalFollower, int totalFollowing
            , int totalComment, int totalLike, int marketAgeMin, int marketAgeMax, int marketMale, int marketFemale, String timePosting, int servicePost
            , int serviceStory, int serviceBio, String code, Boolean useLoading, Callback callback) {
        new update_info(activity, username, socialmedia, totalPost, totalFollower, totalFollowing, totalComment, totalLike, marketAgeMin, marketAgeMax, marketMale, marketFemale, timePosting, servicePost, serviceStory, serviceBio, code, useLoading, callback).execute("v1/socialmedia/update_info");
    }

    private static class update_info extends AsyncTask<String, Void, String> {
        final WeakReference<Activity> activity;
        final Callback callback;
        final Boolean useLoading;
        final String username;
        final int socialmedia;
//        final int category;
//        final int city;
//        final String id;
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

        private update_info(Activity activity, String username, int socialmedia, int totalPost, int totalFollower, int totalFollowing
                , int totalComment, int totalLike, int marketAgeMin, int marketAgeMax, int marketMale, int marketFemale, String timePosting, int servicePost
                , int serviceStory, int serviceBio, String code, Boolean useLoading, Callback callback) {
            this.activity = new WeakReference<>(activity);
            this.callback = callback;
            this.useLoading = useLoading;
            this.username = username;
            this.socialmedia = socialmedia;
//            this.category = category;
//            this.city = city;
//            this.id = id;
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
//                jsonObject.put("category", category);
//                jsonObject.put("city", city);
//                jsonObject.put("id", id);
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
                jsonObject.put("code", code);
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
                    callback.error();
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
