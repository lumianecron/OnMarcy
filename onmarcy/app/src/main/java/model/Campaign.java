package model;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.onmarcy.Global;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Date;

public class Campaign implements Parcelable {
    @SerializedName("code")
    private int code;

    @SerializedName("code_string")
    private String codeString;

    @SerializedName("brand_code")
    private String brandCode;

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

    @SerializedName("pay_link")
    private String link;

    @SerializedName("pay_code")
    private String payCode;

    @SerializedName("pay_status")
    private int payStatus;

    public String[] posts = new String[10];
    public String[] stories = new String[3];
    public String bio;
    public String caption;

    public String getCodeString() {
        return codeString;
    }

    public void setCodeString(String codeString) {
        this.codeString = codeString;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public int getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(int categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getAgeMin() {
        return ageMin;
    }

    public void setAgeMin(int ageMin) {
        this.ageMin = ageMin;
    }

    public int getAgeMax() {
        return ageMax;
    }

    public void setAgeMax(int ageMax) {
        this.ageMax = ageMax;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getApproach() {
        return approach;
    }

    public void setApproach(int approach) {
        this.approach = approach;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    protected Campaign(Parcel in) {
        code = in.readInt();
        codeString = in.readString();
        brandCode = in.readString();
        brandName = in.readString();
        categoryCode = in.readInt();
        categoryName = in.readString();
        cityCode = in.readInt();
        cityName = in.readString();
        title = in.readString();
        notes = in.readString();
        ageMin = in.readInt();
        ageMax = in.readInt();
        gender = in.readInt();
        duration = in.readInt();
        price = in.readInt();
        status = in.readInt();
        approach = in.readInt();
        date = in.readString();
        time = in.readString();
        link = in.readString();
    }

    public static final Creator<Campaign> CREATOR = new Creator<Campaign>() {
        @Override
        public Campaign createFromParcel(Parcel in) {
            return new Campaign(in);
        }

        @Override
        public Campaign[] newArray(int size) {
            return new Campaign[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(code);
        parcel.writeString(codeString);
        parcel.writeString(brandCode);
        parcel.writeString(brandName);
        parcel.writeInt(categoryCode);
        parcel.writeString(categoryName);
        parcel.writeInt(cityCode);
        parcel.writeString(cityName);
        parcel.writeString(title);
        parcel.writeString(notes);
        parcel.writeInt(ageMin);
        parcel.writeInt(ageMax);
        parcel.writeInt(gender);
        parcel.writeInt(duration);
        parcel.writeInt(price);
        parcel.writeInt(status);
        parcel.writeInt(approach);
        parcel.writeString(date);
        parcel.writeString(time);
        parcel.writeString(link);
    }

    public interface CallbackSelect {
        void success(JSONArray data);

        void error();
    }

    public interface Callback {
        void success();

        void error();
    }

    public Campaign(JSONObject jsonObject) {
        try {
            this.code = jsonObject.has("code") ? jsonObject.getInt("code") : 0;
            this.codeString = jsonObject.has("code_string") ? jsonObject.getString("code_string") : "";
            this.categoryCode = jsonObject.has("category_code") ? jsonObject.getInt("category_code") : 0;
            this.categoryName = jsonObject.has("category_name") ? jsonObject.getString("category_name") : "";
            this.brandCode = jsonObject.has("brand_code") ? jsonObject.getString("brand_code") : "";
            this.brandName = jsonObject.has("brand_name") ? jsonObject.getString("brand_name") : "";
            this.cityCode = jsonObject.has("city_code") ? jsonObject.getInt("city_code") : 0;
            this.cityName = jsonObject.has("city_name") ? jsonObject.getString("city_name") : "";
            this.title = jsonObject.has("title") ? jsonObject.getString("title") : "";
            this.notes = jsonObject.has("notes") ? jsonObject.getString("notes") : "";
            this.ageMax = jsonObject.has("market_age_max") ? jsonObject.getInt("market_age_max") : 0;
            this.ageMin = jsonObject.has("market_age_min") ? jsonObject.getInt("market_age_min") : 0;
            this.gender = jsonObject.has("market_gender") ? jsonObject.getInt("market_gender") : 0;
            this.duration = jsonObject.has("duration") ? jsonObject.getInt("duration") : 0;
            this.price = jsonObject.has("price") ? jsonObject.getInt("price") : 0;
            this.status = jsonObject.has("status") ? jsonObject.getInt("status") : 0;
            this.approach = jsonObject.has("approach") ? jsonObject.getInt("approach") : 0;
            this.date = jsonObject.has("date") ? jsonObject.getString("date") : "";
            this.time = jsonObject.has("time_posting") ? jsonObject.getString("time_posting") : "";
            this.link = jsonObject.has("pay_link") ? jsonObject.getString("pay_link") : "";
            this.payCode = jsonObject.has("pay_code") ? jsonObject.getString("pay_code") : "";
            this.payStatus = jsonObject.has("pay_status") ? jsonObject.getInt("pay_status") : 0;

            this.caption = jsonObject.has("caption") ? jsonObject.getString("caption") : "";
            this.posts[0] = jsonObject.has("post1") ? jsonObject.getString("post1") : "";
            this.posts[1] = jsonObject.has("post2") ? jsonObject.getString("post2") : "";
            this.posts[2] = jsonObject.has("post3") ? jsonObject.getString("post3") : "";
            this.posts[3] = jsonObject.has("post4") ? jsonObject.getString("post4") : "";
            this.posts[4] = jsonObject.has("post5") ? jsonObject.getString("post5") : "";
            this.posts[5] = jsonObject.has("post6") ? jsonObject.getString("post6") : "";
            this.posts[6] = jsonObject.has("post7") ? jsonObject.getString("post7") : "";
            this.posts[7] = jsonObject.has("post8") ? jsonObject.getString("post8") : "";
            this.posts[8] = jsonObject.has("post9") ? jsonObject.getString("post9") : "";
            this.posts[9] = jsonObject.has("post10") ? jsonObject.getString("post10") : "";
            this.stories[0] = jsonObject.has("story1") ? jsonObject.getString("story1") : "";
            this.stories[1] = jsonObject.has("story2") ? jsonObject.getString("story2") : "";
            this.stories[2] = jsonObject.has("story3") ? jsonObject.getString("story3") : "";
            this.bio = jsonObject.has("bio") ? jsonObject.getString("bio") : "";
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void select(Activity activity, String codeString, int categoryCode, String categoryName, String brandCode, String brandName, int cityCode, String cityName, String title
            , String notes, int ageMin, int ageMax, int gender, int duration, int price, int status, String date, String time, int start, int limit, int approach, CallbackSelect callback) {
        new Campaign.select(activity, codeString, categoryCode, categoryName, brandCode, brandName, cityCode, cityName, title, notes, ageMin, ageMax, gender, duration, price
                , status, date, time, start, limit, approach, callback).execute("v1/campaign/select");
    }

    private static class select extends AsyncTask<String, Void, String> {
        final WeakReference<Activity> activity;
        final CallbackSelect callback;
        final String codeString;
        final int categoryCode;
        final String categoryName;
        final String brandCode;
        final String brandName;
        final int cityCode;
        final String cityName;
        final String title;
        final String notes;
        final int ageMin;
        final int ageMax;
        final int gender;
        final int duration;
        final int price;
        final int status;
        final String date;
        final String time;
        final int approach;
        final int start;
        final int limit;

        public select(Activity activity, String codeString, int categoryCode, String categoryName, String brandCode, String brandName, int cityCode, String cityName, String title
                , String notes, int ageMin, int ageMax, int gender, int duration, int price, int status, String date, String time, int start, int limit, int approach, CallbackSelect callback) {
            this.activity = new WeakReference<>(activity);
            this.callback = callback;
            this.codeString = codeString;
            this.categoryCode = categoryCode;
            this.categoryName = categoryName;
            this.brandCode = brandCode;
            this.brandName = brandName;
            this.cityCode = cityCode;
            this.cityName = cityName;
            this.title = title;
            this.notes = notes;
            this.ageMin = ageMin;
            this.ageMax = ageMax;
            this.gender = gender;
            this.duration = duration;
            this.price = price;
            this.status = status;
            this.date = date;
            this.time = time;
            this.start = start;
            this.limit = limit;
            this.approach = approach;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                User user = new User(new JSONObject(Global.getShared(Global.SHARED_INDEX.USER, "{}")));
                jsonObject.put("hash", user.getHash());
                jsonObject.put("code_string", codeString);
                jsonObject.put("category_code", categoryCode);
                jsonObject.put("category_name", categoryName);
                jsonObject.put("brand_code", brandCode);
                jsonObject.put("brand_name", brandName);
                jsonObject.put("city_code", cityCode);
                jsonObject.put("city_name", cityName);
                jsonObject.put("title", title);
                jsonObject.put("notes", notes);
                jsonObject.put("market_age_min", ageMin);
                jsonObject.put("market_age_max", ageMax);
                jsonObject.put("market_gender", gender);
                jsonObject.put("duration", duration);
                jsonObject.put("price", price);
                jsonObject.put("status", status);
                jsonObject.put("date", date);
                jsonObject.put("time_posting", time);
                jsonObject.put("start", start);
                jsonObject.put("limit", limit);
                jsonObject.put("approach", approach);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return Global.executePost(urls[0], jsonObject, 3000);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getBoolean(Global.RESPONSE_SUCCESS)) {
                    callback.success(jsonObject.getJSONObject(Global.RESPONSE_DATA).getJSONArray("campaign"));
                } else {
                    callback.error();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void selectPending(Activity activity, CallbackSelect callback) {
        new selectPending(activity, callback).execute("v1/campaign/select_pending");
    }

    private static class selectPending extends AsyncTask<String, Void, String> {
        final WeakReference<Activity> activity;
        final CallbackSelect callback;

        public selectPending(Activity activity, CallbackSelect callback) {
            this.activity = new WeakReference<>(activity);
            this.callback = callback;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                User user = new User(new JSONObject(Global.getShared(Global.SHARED_INDEX.USER, "{}")));
                jsonObject.put("hash", user.getHash());
                jsonObject.put("status", 4);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return Global.executePost(urls[0], jsonObject, 3000);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getBoolean(Global.RESPONSE_SUCCESS)) {
                    callback.success(jsonObject.getJSONObject(Global.RESPONSE_DATA).getJSONArray("campaign"));
                } else {
                    callback.error();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // DETAIL CAMPAIGN
    public static void detail(Activity activity, int code, String codeString, CallbackSelect callback) {
        new detail(activity, code, codeString, callback).execute("v1/campaign/detail");
    }

    private static class detail extends AsyncTask<String, Void, String> {
        final WeakReference<Activity> activity;
        final CallbackSelect callback;
        final int code;
        final String codeString;

        public detail(Activity activity, int code, String codeString, CallbackSelect callback) {
            this.activity = new WeakReference<>(activity);
            this.callback = callback;
            this.code = code;
            this.codeString = codeString;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                User user = new User(new JSONObject(Global.getShared(Global.SHARED_INDEX.USER, "{}")));
                jsonObject.put("hash", user.getHash());
                jsonObject.put("code", code);
                jsonObject.put("code_string", codeString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return Global.executePost(urls[0], jsonObject, 3000);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getBoolean(Global.RESPONSE_SUCCESS)) {
                    callback.success(jsonObject.getJSONObject(Global.RESPONSE_DATA).getJSONArray("campaign"));
                } else {
                    callback.error();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // INSERT CAMPAIGN
    public static void insert(Activity activity, String username, int category, String title, String notes, int ageMin, int ageMax, int gender, int duration, int price, String date, String time, int cityCode, String caption, String bio, Boolean useLoading, CallbackSelect callback) {
        new insert(activity, username, category, title, notes, ageMin, ageMax, gender, duration, price, date, time, cityCode, caption, bio, useLoading, callback).execute("v1/campaign/insert");
    }

    private static class insert extends AsyncTask<String, Void, String> {
        final WeakReference<Activity> activity;
        final CallbackSelect callback;
        final Boolean useLoading;
        final int category;
        final String title;
        final String notes;
        final int ageMin;
        final int ageMax;
        final int gender;
        final int duration;
        final int price;
        final String date;
        final String time;
        final int cityCode;
        final String username;
        final String caption;
        final String bio;

        private insert(Activity activity, String username, int category, String title, String notes, int ageMin, int ageMax, int gender, int duration, int price, String date, String time, int cityCode, String caption, String bio, Boolean useLoading, CallbackSelect callback) {
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
            this.date = date;
            this.time = time;
            this.cityCode = cityCode;
            this.username = username;
            this.caption = caption;
            this.bio = bio;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                User user = new User(new JSONObject(Global.getShared(Global.SHARED_INDEX.USER, "{}")));
                jsonObject.put("hash", user.getHash());
                jsonObject.put("socialmedia_code", 1);
                jsonObject.put("socialmedia_username", username);
                jsonObject.put("category_code", category);
                jsonObject.put("city_code", cityCode);
                jsonObject.put("title", title);
                jsonObject.put("notes", notes);
                jsonObject.put("age_min", ageMin);
                jsonObject.put("age_max", ageMax);
                jsonObject.put("gender", gender);
                jsonObject.put("date", date);
                jsonObject.put("time", time);
                jsonObject.put("duration", duration);
                jsonObject.put("price", price);
                jsonObject.put("caption", caption);
                jsonObject.put("bio", bio);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return Global.executePost(urls[0], jsonObject, 3000);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (useLoading) Global.hideLoading();
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getBoolean(Global.RESPONSE_SUCCESS)) {
                    callback.success(jsonObject.getJSONObject(Global.RESPONSE_DATA).getJSONArray("campaign"));
                } else {
                    callback.error();
                }
            } catch (Exception e) {
                Global.showLoading(activity.get(), "", e.getMessage());
                e.printStackTrace();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (useLoading) Global.showLoading(activity.get(), "", "Loading");
        }
    }

    // UPDATE CAMPAIGN
    public static void update(Activity activity, String codeString, String username, int categoryCode, int cityCode, String title, String notes, int ageMin, int ageMax, int gender, String date, String time, int duration, String caption, String bio, Boolean useLoading, Callback callback) {
        new update(activity, codeString, username, categoryCode, cityCode, title, notes, ageMin, ageMax, gender, date, time, duration, caption, bio, useLoading, callback).execute("v1/campaign/update");
    }

    private static class update extends AsyncTask<String, Void, String> {
        final WeakReference<Activity> activity;
        final Callback callback;
        final Boolean useLoading;
        final String codeString;
        final String username;
        final int categoryCode;
        final int cityCode;
        final String title;
        final String notes;
        final int ageMin;
        final int ageMax;
        final int gender;
        final String date;
        final String time;
        final int duration;
        final String caption;
        final String bio;

        private update(Activity activity, String codeString, String username, int categoryCode, int cityCode, String title, String notes, int ageMin, int ageMax, int gender, String date, String time, int duration, String caption, String bio, Boolean useLoading, Callback callback) {
            this.activity = new WeakReference<>(activity);
            this.callback = callback;
            this.useLoading = useLoading;
            this.codeString = codeString;
            this.username = username;
            this.categoryCode = categoryCode;
            this.cityCode = cityCode;
            this.title = title;
            this.notes = notes;
            this.ageMin = ageMin;
            this.ageMax = ageMax;
            this.gender = gender;
            this.date = date;
            this.time = time;
            this.duration = duration;
            this.caption = caption;
            this.bio = bio;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                User user = new User(new JSONObject(Global.getShared(Global.SHARED_INDEX.USER, "{}")));
                jsonObject.put("hash", user.getHash());
                jsonObject.put("socialmedia_code", 1);
                jsonObject.put("socialmedia_username", username);
                jsonObject.put("code_string", codeString);
                jsonObject.put("category_code", categoryCode);
                jsonObject.put("city_code", cityCode);
                jsonObject.put("title", title);
                jsonObject.put("notes", notes);
                jsonObject.put("age_min", ageMin);
                jsonObject.put("age_max", ageMax);
                jsonObject.put("gender", gender);
                jsonObject.put("date", date);
                jsonObject.put("time", time);
                jsonObject.put("duration", duration);
                jsonObject.put("caption", caption);
                jsonObject.put("bio", bio);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return Global.executePost(urls[0], jsonObject, 3000);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (useLoading) Global.hideLoading();
            try {
                JSONObject json = new JSONObject(result);
                if (json.getBoolean(Global.RESPONSE_SUCCESS)) {
                    callback.success();
                } else {
                    callback.error();
                }
            } catch (Exception e) {
                Global.showLoading(activity.get(), "", e.getMessage());
                e.printStackTrace();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (useLoading) Global.showLoading(activity.get(), "", "Loading");
        }
    }

    // UPLOAD CONTENT PICTURE
    public static void uploadContentPicture(Activity activity, String codeString, String content, String image, Boolean useLoading, Callback callback) {
        new uploadContentPicture(activity, codeString, content, image, useLoading, callback).execute("v1/campaign/upload_content_picture");
    }

    private static class uploadContentPicture extends AsyncTask<String, Void, String> {
        final WeakReference<Activity> activity;
        final Callback callback;
        final Boolean useLoading;
        final String codeString;
        final String content;
        final String image;

        private uploadContentPicture(Activity activity, String codeString, String content, String image, Boolean useLoading, Callback callback) {
            this.activity = new WeakReference<>(activity);
            this.callback = callback;
            this.useLoading = useLoading;
            this.codeString = codeString;
            this.content = content;
            this.image = image;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                User user = new User(new JSONObject(Global.getShared(Global.SHARED_INDEX.USER, "{}")));
                jsonObject.put("hash", user.getHash());
                jsonObject.put("code_string", codeString);
                jsonObject.put("content", content);
                jsonObject.put("image", image);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return Global.executePost(urls[0], jsonObject, 3000);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (useLoading) Global.hideLoading();
            try {
                JSONObject json = new JSONObject(result);
                if (json.getBoolean(Global.RESPONSE_SUCCESS)) {
                    callback.success();
                } else {
                    callback.error();
                }
            } catch (Exception e) {
                Global.showLoading(activity.get(), "", e.getMessage());
                e.printStackTrace();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (useLoading) Global.showLoading(activity.get(), "", "Loading");
        }
    }

    public static void uploadResultPicture(Activity activity, int campaignCode, String result, String image, Boolean useLoading, Callback callback) {
        new uploadResultPicture(activity, campaignCode, result, image, useLoading, callback).execute("v1/campaign/upload_result_picture");
    }

    private static class uploadResultPicture extends AsyncTask<String, Void, String> {
        final WeakReference<Activity> activity;
        final Callback callback;
        final Boolean useLoading;
        final int campaignCode;
        final String result;
        final String image;

        private uploadResultPicture(Activity activity, int campaignCode, String result, String image, Boolean useLoading, Callback callback) {
            this.activity = new WeakReference<>(activity);
            this.callback = callback;
            this.useLoading = useLoading;
            this.campaignCode = campaignCode;
            this.result = result;
            this.image = image;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                User user = new User(new JSONObject(Global.getShared(Global.SHARED_INDEX.USER, "{}")));
                jsonObject.put("hash", user.getHash());
                jsonObject.put("campaign_code", campaignCode);
                jsonObject.put("result", result);
                jsonObject.put("image", image);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return Global.executePost(urls[0], jsonObject, 3000);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (useLoading) Global.hideLoading();
            try {
                JSONObject json = new JSONObject(result);
                if (json.getBoolean(Global.RESPONSE_SUCCESS)) {
                    callback.success();
                } else {
                    callback.error();
                }
            } catch (Exception e) {
                Global.showLoading(activity.get(), "", e.getMessage());
                e.printStackTrace();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (useLoading) Global.showLoading(activity.get(), "", "Loading");
        }
    }

    // DELETE CAMPAIGN
    public static void delete(Activity activity, String codeString, Boolean useLoading, Callback callback) {
        new delete(activity, codeString, useLoading, callback).execute("v1/campaign/delete");
    }

    private static class delete extends AsyncTask<String, Void, String> {
        final WeakReference<Activity> activity;
        final Callback callback;
        final Boolean useLoading;
        final String codeString;

        private delete(Activity activity, String codeString, Boolean useLoading, Callback callback) {
            this.activity = new WeakReference<>(activity);
            this.callback = callback;
            this.useLoading = useLoading;
            this.codeString = codeString;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                User user = new User(new JSONObject(Global.getShared(Global.SHARED_INDEX.USER, "{}")));
                jsonObject.put("hash", user.getHash());
                jsonObject.put("code_string", codeString);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return Global.executePost(urls[0], jsonObject, 3000);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (useLoading) Global.hideLoading();
            try {
                JSONObject json = new JSONObject(result);
                if (json.getBoolean(Global.RESPONSE_SUCCESS)) {
                    callback.success();
                } else {
                    callback.error();
                }
            } catch (Exception e) {
                Global.showLoading(activity.get(), "", e.getMessage());
                e.printStackTrace();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (useLoading) Global.showLoading(activity.get(), "", "Loading");
        }
    }

    // INSERT RESULT CAMPAIGN
    public static void insertResult(Activity activity, int code, int like, int comment, int save, int impression, int reach, int engagement, String notes, Boolean useLoading, Callback callback) {
        new insertResult(activity, code, like, comment, save, impression, reach, engagement, notes, useLoading, callback).execute("v1/campaign/result");
    }

    private static class insertResult extends AsyncTask<String, Void, String> {
        final WeakReference<Activity> activity;
        final Callback callback;
        final Boolean useLoading;
        final int code;
        final int like;
        final int comment;
        final int save;
        final int impression;
        final int reach;
        final int engagement;
        final String notes;

        private insertResult(Activity activity, int code, int like, int comment, int save, int impression, int reach, int engagement, String notes, Boolean useLoading, Callback callback) {
            this.activity = new WeakReference<>(activity);
            this.callback = callback;
            this.useLoading = useLoading;
            this.code = code;
            this.like = like;
            this.comment = comment;
            this.save = save;
            this.impression = impression;
            this.reach = reach;
            this.engagement = engagement;
            this.notes = notes;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                User user = new User(new JSONObject(Global.getShared(Global.SHARED_INDEX.USER, "{}")));
                jsonObject.put("hash", user.getHash());
                jsonObject.put("campaign_code", code);
                jsonObject.put("like", like);
                jsonObject.put("comment", comment);
                jsonObject.put("save", save);
                jsonObject.put("impression", impression);
                jsonObject.put("reach", reach);
                jsonObject.put("engagement", engagement);
                jsonObject.put("notes", notes);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return Global.executePost(urls[0], jsonObject, 3000);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (useLoading) Global.hideLoading();
            try {
                JSONObject json = new JSONObject(result);
                if (json.getBoolean(Global.RESPONSE_SUCCESS)) {
                    callback.success();
                } else {
                    callback.error();
                }
            } catch (Exception e) {
                Global.showLoading(activity.get(), "", e.getMessage());
                e.printStackTrace();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (useLoading) Global.showLoading(activity.get(), "", "Loading");
        }
    }

    // CHOOSE MARKETER
    public static void chooseMarketer(Activity activity, int code, String username, Boolean useLoading, Callback callback) {
        new chooseMarketer(activity, code, username, useLoading, callback).execute("v1/campaign/choose_marketer");
    }

    private static class chooseMarketer extends AsyncTask<String, Void, String> {
        final WeakReference<Activity> activity;
        final Callback callback;
        final Boolean useLoading;
        final int code;
        final String username;

        private chooseMarketer(Activity activity, int code, String username, Boolean useLoading, Callback callback) {
            this.activity = new WeakReference<>(activity);
            this.callback = callback;
            this.useLoading = useLoading;
            this.code = code;
            this.username = username;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                User user = new User(new JSONObject(Global.getShared(Global.SHARED_INDEX.USER, "{}")));
                jsonObject.put("hash", user.getHash());
                jsonObject.put("campaign_code", code);
                jsonObject.put("username", username);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return Global.executePost(urls[0], jsonObject, 3000);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (useLoading) Global.hideLoading();
            try {
                JSONObject json = new JSONObject(result);
                if (json.getBoolean(Global.RESPONSE_SUCCESS)) {
                    callback.success();
                } else {
                    callback.error();
                }
            } catch (Exception e) {
                Global.showLoading(activity.get(), "", e.getMessage());
                e.printStackTrace();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (useLoading) Global.showLoading(activity.get(), "", "Loading");
        }
    }

    // SELECT USER APPROACH
    public static void selectUserApproach(Activity activity, int code, CallbackSelect callback) {
        new selectUserApproach(activity, code, callback).execute("v1/campaign/select_user_approach");
    }

    private static class selectUserApproach extends AsyncTask<String, Void, String> {
        final WeakReference<Activity> activity;
        final CallbackSelect callback;
        final int code;

        public selectUserApproach(Activity activity, int code, CallbackSelect callback) {
            this.activity = new WeakReference<>(activity);
            this.callback = callback;
            this.code = code;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                User user = new User(new JSONObject(Global.getShared(Global.SHARED_INDEX.USER, "{}")));
                jsonObject.put("hash", user.getHash());
                jsonObject.put("campaign_code", code);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return Global.executePost(urls[0], jsonObject, 3000);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!result.equals("null")) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getBoolean(Global.RESPONSE_SUCCESS)) {
                        callback.success(jsonObject.getJSONObject(Global.RESPONSE_DATA).getJSONArray("marketer"));
                    } else {
                        callback.error();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                callback.error();
            }
        }
    }

    // SELECT MARKETER TASK
    public static void selectMarketerTask(Activity activity, CallbackSelect callback) {
        new selectMarketerTask(activity, callback).execute("v1/campaign/select_marketer_task");
    }

    private static class selectMarketerTask extends AsyncTask<String, Void, String> {
        final WeakReference<Activity> activity;
        final CallbackSelect callback;

        public selectMarketerTask(Activity activity/*, int code*/, CallbackSelect callback) {
            this.activity = new WeakReference<>(activity);
            this.callback = callback;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                User user = new User(new JSONObject(Global.getShared(Global.SHARED_INDEX.USER, "{}")));
                jsonObject.put("hash", user.getHash());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return Global.executePost(urls[0], jsonObject, 3000);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getBoolean(Global.RESPONSE_SUCCESS)) {
                    callback.success(jsonObject.getJSONObject(Global.RESPONSE_DATA).getJSONArray("task"));
                } else {
                    callback.error();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // SELECT MARKETER RESULT
    public static void selectCampaignResult(Activity activity, CallbackSelect callback) {
        new selectCampaignResult(activity, callback).execute("v1/campaign/select_campaign_result");
    }

    private static class selectCampaignResult extends AsyncTask<String, Void, String> {
        final WeakReference<Activity> activity;
        final CallbackSelect callback;

        public selectCampaignResult(Activity activity, CallbackSelect callback) {
            this.activity = new WeakReference<>(activity);
            this.callback = callback;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                User user = new User(new JSONObject(Global.getShared(Global.SHARED_INDEX.USER, "{}")));
                jsonObject.put("hash", user.getHash());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return Global.executePost(urls[0], jsonObject, 3000);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getBoolean(Global.RESPONSE_SUCCESS)) {
                    callback.success(jsonObject.getJSONObject(Global.RESPONSE_DATA).getJSONArray("result"));
                } else {
                    callback.error();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
