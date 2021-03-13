package model;

import android.app.Activity;
import android.os.AsyncTask;

import com.android.onmarcy.Global;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class City {
    @SerializedName("code")
    private int code;

    @SerializedName("name")
    private String name;

    public City(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public interface CallbackSelect {
        void success(JSONArray data);
        void error();
    }

    public City(JSONObject json) {
        try {
            this.code = json.has("code") ? json.getInt("code") : 0;
            this.name = json.has("name") ? json.getString("name") : "";
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void select(Activity activity, CallbackSelect callback) {
        new select(activity, callback).execute("v1/system/city");
    }

    private static class select extends AsyncTask<String, Void, String> {
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

    @Override
    public String toString() {
        return name;
    }
}
