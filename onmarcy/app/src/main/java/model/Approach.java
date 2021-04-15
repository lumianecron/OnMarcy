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

public class Approach implements Parcelable {
    @SerializedName("code")
    private int code;

    @SerializedName("notes")
    private String notes;

    public int getCode() { return code; }

    public void setCode(int code) { this.code = code; }

    public String getNotes() { return notes; }

    public void setNotes(String notes) { this.notes = notes; }

    protected Approach(Parcel in) {
        code = in.readInt();
        notes = in.readString();
    }

    public static final Creator<Approach> CREATOR = new Creator<Approach>() {
        @Override
        public Approach createFromParcel(Parcel in) {
            return new Approach(in);
        }

        @Override
        public Approach[] newArray(int size) { return new Approach[size]; }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(code);
        parcel.writeString(notes);
    }

    public interface CallbackSelect {
        void success(JSONArray data);
        void error();
    }

    public interface Callback {
        void success();
        void error();
    }

    public Approach(JSONObject jsonObject){
        try{
            this.code = jsonObject.has("code") ? jsonObject.getInt("code") : 0;
            this.notes = jsonObject.has("notes") ? jsonObject.getString("notes") : "";
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void approach(Activity activity, int code, String notes, Callback callback) {
        new approach(activity, code, notes, callback).execute("v1/campaign/approach");
    }

    private static class approach extends AsyncTask<String, Void, String> {
        final WeakReference<Activity> activity;
        final Callback callback;
        final int code;
        final String notes;

        public approach(Activity activity, int code, String notes, Callback callback) {
            this.activity = new WeakReference<>(activity);
            this.callback = callback;
            this.code = code;
            this.notes = notes;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                User user = new User(new JSONObject(Global.getShared(Global.SHARED_INDEX.USER, "{}")));
                jsonObject.put("hash", user.getHash());
                jsonObject.put("campaign_code", code);
                jsonObject.put("notes", notes);
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
                Global.showLoading(activity.get(), "", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void cekApproach(Activity activity, int code, Callback callback) {
        new cekApproach(activity, code, callback).execute("v1/campaign/cek_approach");
    }

    private static class cekApproach extends AsyncTask<String, Void, String> {
        final WeakReference<Activity> activity;
        final Callback callback;
        final int code;

        public cekApproach(Activity activity, int code, Callback callback) {
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
                    callback.success();
                } else {
                    callback.error();
                }
            } catch (Exception e) {
                Global.showLoading(activity.get(), "", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void cancelApproach(Activity activity, int code, Callback callback) {
        new cekApproach(activity, code, callback).execute("v1/campaign/approach_cancel");
    }

    private static class cancelApproach extends AsyncTask<String, Void, String> {
        final WeakReference<Activity> activity;
        final Callback callback;
        final int code;

        public cancelApproach(Activity activity, int code, Callback callback) {
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
                    callback.success();
                } else {
                    callback.error();
                }
            } catch (Exception e) {
                Global.showLoading(activity.get(), "", e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
