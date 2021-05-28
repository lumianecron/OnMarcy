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

public class Portfolio {
    @SerializedName("code")
    private int code;

    @SerializedName("image")
    private String imageUrl;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public interface CallbackSelect {
        void success(JSONArray data);
        void error();
    }

    public interface Callback {
        void success();
        void error();
    }

    public Portfolio(JSONObject json) {
        try {
            this.code = json.has("code") ? json.getInt("code") : 0;
            this.imageUrl = json.has("image") ? json.getString("image") : "";
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void select(Activity activity, int socialMediaCode, CallbackSelect callback) {
        new select(activity, socialMediaCode, callback).execute("v1/socialmedia/select_portfolio");
    }

    private static class select extends AsyncTask<String, Void, String> {
        final WeakReference<Activity> activity;
        final int socialMediaCode;
        final CallbackSelect callback;

        public select(Activity activity, int socialMediaCode, CallbackSelect callback) {
            this.activity = new WeakReference<>(activity);
            this.socialMediaCode = socialMediaCode;
            this.callback = callback;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                User user = new User(new JSONObject(Global.getShared(Global.SHARED_INDEX.USER, "{}")));
                jsonObject.put("hash", user.getHash());
                jsonObject.put("socialmedia_code", socialMediaCode);
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
                    callback.success(jsonObject.getJSONObject(Global.RESPONSE_DATA).getJSONArray("portfolio"));
                } else {
                    callback.error();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void insert(Activity activity, int socialMediaCode, String image, Callback callback) {
        new insert(activity, socialMediaCode, image, callback).execute("v1/socialmedia/insert_portfolio");
    }

    private static class insert extends AsyncTask<String, Void, String> {
        final WeakReference<Activity> activity;
        final int socialMediaCode;
        final String image;
        final Callback callback;

        public insert(Activity activity, int socialMediaCode, String image, Callback callback) {
            this.activity = new WeakReference<>(activity);
            this.socialMediaCode = socialMediaCode;
            this.image = image;
            this.callback = callback;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                User user = new User(new JSONObject(Global.getShared(Global.SHARED_INDEX.USER, "{}")));
                jsonObject.put("hash", user.getHash());
                jsonObject.put("socialmedia_code", socialMediaCode);
                jsonObject.put("image", image);
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
                    callback.success();
                } else {
                    callback.error();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void delete(Activity activity, int portfolioCode, Callback callback) {
        new delete(activity, portfolioCode, callback).execute("v1/socialmedia/delete_portfolio");
    }

    private static class delete extends AsyncTask<String, Void, String> {
        final WeakReference<Activity> activity;
        final int portfolioCode;
        final Callback callback;

        public delete(Activity activity, int portfolioCode, Callback callback) {
            this.activity = new WeakReference<>(activity);
            this.portfolioCode = portfolioCode;
            this.callback = callback;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                User user = new User(new JSONObject(Global.getShared(Global.SHARED_INDEX.USER, "{}")));
                jsonObject.put("hash", user.getHash());
                jsonObject.put("portfolio_code", portfolioCode);
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
                    callback.success();
                } else {
                    callback.error();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
