package model;

import android.app.Activity;
import android.os.AsyncTask;

import com.android.onmarcy.Global;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Date;

public class Campaign {
    @SerializedName("code_string")
    private String codeString;

    @SerializedName("brand_code")
    private int brandCode;

    @SerializedName("brand_name")
    private String brandName;

    @SerializedName("category_code")
    private int categoryCode;

    @SerializedName("category_name")
    private String categoryName;

    @SerializedName("city_code")
    private int cityCode;

    @SerializedName("city_name")
    private String cityName;

    @SerializedName("title")
    private String title;

    @SerializedName("notes")
    private String notes;

    @SerializedName("age_min")
    private int ageMin;

    @SerializedName("age_max")
    private int ageMax;

    @SerializedName("gender")
    private int gender;

    @SerializedName("duration")
    private int duration;

    @SerializedName("price")
    private int price;

    @SerializedName("status")
    private int status;

    @SerializedName("approach")
    private int approach;

    @SerializedName("date")
    private String date;

    @SerializedName("time")
    private String time;

    public interface CallbackSelect {
        void success(JSONArray data);
        void error();
    }

    public interface Callback {
        void success();
        void error();
    }

    public Campaign(JSONObject jsonObject){
        try{
            this.codeString = jsonObject.has("code_string") ? jsonObject.getString("code_string") : "";
            this.categoryCode = jsonObject.has("category_code") ? jsonObject.getInt("category_code") : 0;
            this.categoryName = jsonObject.has("category_name") ? jsonObject.getString("category_name") : "";
            this.brandCode = jsonObject.has("brand_code") ? jsonObject.getInt("brand_code") : 0;
            this.brandName = jsonObject.has("brand_name") ? jsonObject.getString("brand_name") : "";
            this.cityCode = jsonObject.has("city_code") ? jsonObject.getInt("city_code") : 0;
            this.cityName = jsonObject.has("city_name") ? jsonObject.getString("city_name") : "";
            this.title = jsonObject.has("title") ? jsonObject.getString("title") : "";
            this.notes = jsonObject.has("notes") ? jsonObject.getString("notes") : "";
            this.ageMax = jsonObject.has("age_max") ? jsonObject.getInt("age_max") : 0;
            this.ageMin = jsonObject.has("age_min") ? jsonObject.getInt("age_min") : 0;
            this.gender = jsonObject.has("gender") ? jsonObject.getInt("gender") : 0;
            this.duration = jsonObject.has("duration") ? jsonObject.getInt("duration") : 0;
            this.price = jsonObject.has("price") ? jsonObject.getInt("price") : 0;
            this.status = jsonObject.has("status") ? jsonObject.getInt("status") : 0;
            this.approach = jsonObject.has("approach") ? jsonObject.getInt("approach") : 0;
            this.date = jsonObject.has("date") ? jsonObject.getString("date") : "";
            this.time = jsonObject.has("time") ? jsonObject.getString("time") : "";
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void select(Activity activity, String codeString, int status, String date, int categoryCode, int approach, int start, int limit, CallbackSelect callback) {
        new select(activity, codeString, status, date, categoryCode, approach, start, limit, callback).execute("v1/campaign/select");
    }

    private static class select extends AsyncTask<String, Void, String> {
        final WeakReference<Activity> activity;
        final CallbackSelect callback;
        final String codeString;
        final int status;
        final String date;
        final int categoryCode;
        final int approach;
        final int start;
        final int limit;

        private select(Activity activity, String codeString, int status, String date, int categoryCode, int approach, int start, int limit, CallbackSelect callback) {
            this.activity = new WeakReference<>(activity);
            this.callback = callback;
            this.codeString = codeString;
            this.status = status;
            this.date = date;
            this.categoryCode = categoryCode;
            this.approach = approach;
            this.start = start;
            this.limit = limit;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                User user = new User(new JSONObject(Global.getShared(Global.SHARED_INDEX.USER, "{}")));
                jsonObject.put("hash", user.getHash());
                jsonObject.put("code_string", codeString);
                jsonObject.put("status", status);
                jsonObject.put("date", date);
                jsonObject.put("category_code", categoryCode);
                jsonObject.put("approach", approach);
                jsonObject.put("start", start);
                jsonObject.put("limit", limit);
            }
            catch (JSONException e) { e.printStackTrace(); }
            return Global.executePost(urls[0], jsonObject, 3000);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getBoolean(Global.RESPONSE_SUCCESS)) {
                    callback.success(jsonObject.getJSONArray(Global.RESPONSE_DATA));
                } else {
                    callback.error();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void insert(Activity activity, int category, String title, String notes, int ageMin, int ageMax, int gender, int duration, int price, Boolean useLoading, Callback callback) {
        new insert(activity, category, title, notes, ageMin, ageMax, gender, duration, price, useLoading, callback).execute("v1/campaign/insert");
    }

    private static class insert extends AsyncTask<String, Void, String> {
        final WeakReference<Activity> activity;
        final Callback callback;
        final Boolean useLoading;
        final int category;
        final String title;
        final String notes;
        final int ageMin;
        final int ageMax;
        final int gender;
        final int duration;
        final int price;

        private insert(Activity activity, int category, String title, String notes, int ageMin, int ageMax, int gender, int duration, int price, Boolean useLoading, Callback callback) {
            this.activity = new WeakReference<>(activity);
            this.callback = callback;
            this.useLoading = useLoading;
            this.category = category;
            this.title = title;
            this.notes = notes;
            this.ageMin = ageMin;
            this.ageMax = ageMax;
            this.gender = gender;
            this.duration = duration;
            this.price = price;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                User user = new User(new JSONObject(Global.getShared(Global.SHARED_INDEX.USER, "{}")));
                jsonObject.put("hash", user.getHash());
                jsonObject.put("socialmedia_code", 1);
                jsonObject.put("category_code", category);
                jsonObject.put("city_code", 205);
                jsonObject.put("title", title);
                jsonObject.put("notes", notes);
                jsonObject.put("age_min", ageMin);
                jsonObject.put("age_max", ageMax);
                jsonObject.put("gender", gender);
                jsonObject.put("date", "2021-03-10");
                jsonObject.put("time", "");
                jsonObject.put("duration", duration);
                jsonObject.put("price", price);
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
                } else {
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
