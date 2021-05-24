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

public class Message {
    @SerializedName("code")
    private int code;

    @SerializedName("username")
    private String username;

    @SerializedName("message")
    private String message;

    @SerializedName("date")
    private String date;

    public interface CallbackSelect {
        void success(JSONArray data);
        void error();
    }

    public interface Callback {
        void success();
        void error();
    }

    public Message(JSONObject jsonObject){
        try{
            this.code = jsonObject.has("code") ? jsonObject.getInt("code") : 0;
            this.username = jsonObject.has("username") ? jsonObject.getString("username") : "";
            this.message = jsonObject.has("message") ? jsonObject.getString("message") : "";
            this.date = jsonObject.has("date") ? jsonObject.getString("date") : "";
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void select(Activity activity, int code, CallbackSelect callback) {
        new select(activity, code, callback).execute("v1/campaign/select_message");
    }

    private static class select extends AsyncTask<String, Void, String> {
        final WeakReference<Activity> activity;
        final CallbackSelect callback;
        final int code;


        public select(Activity activity, int code, CallbackSelect callback) {
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
                    callback.success(jsonObject.getJSONObject(Global.RESPONSE_DATA).getJSONArray("result"));
                } else {
                    callback.error();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void insert(Activity activity, int code, String message, Callback callback) {
        new insert(activity, code, message, callback).execute("v1/campaign/insert_message");
    }

    private static class insert extends AsyncTask<String, Void, String> {
        final WeakReference<Activity> activity;
        final Callback callback;
        final int code;
        final String message;


        public insert(Activity activity, int code, String message, Callback callback) {
            this.activity = new WeakReference<>(activity);
            this.callback = callback;
            this.code = code;
            this.message = message;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                User user = new User(new JSONObject(Global.getShared(Global.SHARED_INDEX.USER, "{}")));
                jsonObject.put("hash", user.getHash());
                jsonObject.put("campaign_code", code);
                jsonObject.put("message", message);
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
