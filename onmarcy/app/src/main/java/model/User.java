package model;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.android.onmarcy.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class User implements Parcelable {
    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String name;

    @SerializedName("phone")
    private String phone;

    @SerializedName("city")
    private int cityCode;

    @SerializedName("user_type")
    private int userType;

    @SerializedName("refferal")
    private String refferal;

    @SerializedName("active")
    private int isActive;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String name, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    protected User(Parcel in) {
        username = in.readString();
        password = in.readString();
        name = in.readString();
        email = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(name);
        dest.writeString(email);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public interface CallbackSelect {
        void success(JSONObject data);
        void error();
    }

    public interface Callback {
        void success();
        void error();
    }

    // digunakan untuk mapping
    public User(JSONObject json) {
        try {
            this.username = json.has("username") ? json.getString("username") : "";
            this.password = json.has("password") ? json.getString("password") : "";
            this.email = json.has("email") ? json.getString("email") : "";
            this.name = json.has("name") ? json.getString("name") : "";
            this.phone = json.has("phone") ? json.getString("phone") : "";
            this.cityCode = json.has("city") ? json.getInt("city") : 0;
            this.userType = json.has("user_type") ? json.getInt("user_type") : 0;
            this.refferal = json.has("refferal") ? json.getString("refferal") : "";
        }
        catch (JSONException e) { e.printStackTrace(); }
    }

    //SELECT
    public static void select(Activity activity, String username, String password, Boolean useLoading, CallbackSelect callback) {
        new select(activity, username, password, useLoading, callback).execute("v1/login");
    }

    private static class select extends AsyncTask<String, Void, String> {

        final WeakReference<Activity> activity;
        final CallbackSelect callback;
        final Boolean useLoading;
        final String username;
        final String password;

        private select(Activity activity, String username, String password, Boolean useLoading, CallbackSelect callback) {
            this.activity = new WeakReference<>(activity);
            this.callback = callback;
            this.useLoading = useLoading;
            this.username = username;
            this.password = password;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", username);
                jsonObject.put("password", password);
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
                    callback.success(json.getJSONObject(Global.RESPONSE_DATA));
                }
                else {
                    callback.error();
                }
            }
            catch(Exception e) {
                Global.showLoading(activity.get(), "", e.getMessage());
                e.printStackTrace();
//                callback.error();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(useLoading) Global.showLoading(activity.get(), "", "Loading");
        }
    }

    //INSERT
    public static void insert(Activity activity, String username, String password, String email, String name, String phone, int cityCode, int userType, String refferal, Boolean useLoading, Callback callback) {
        new insert(activity, username, password, email, name, phone, cityCode, userType, refferal, useLoading, callback).execute("v1/register");
    }

    private static class insert extends AsyncTask<String, Void, String> {

        final WeakReference<Activity> activity;
        final Callback callback;
        final Boolean useLoading;
        final String username;
        final String password;
        final String email;
        final String name;
        final String phone;
        final int cityCode;
        final int userType;
        final String refferal;

        private insert(Activity activity, String username, String password, String email, String name, String phone, int cityCode, int userType, String refferal, Boolean useLoading, Callback callback) {
            this.activity = new WeakReference<>(activity);
            this.callback = callback;
            this.useLoading = useLoading;
            this.username = username;
            this.password = password;
            this.email = email;
            this.name = name;
            this.phone = phone;
            this.cityCode = cityCode;
            this.userType = userType;
            this.refferal = refferal;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", username);
                jsonObject.put("password", password);
                jsonObject.put("email", email);
                jsonObject.put("name", name);
                jsonObject.put("phone", phone);
                jsonObject.put("city", cityCode);
                jsonObject.put("user_type", userType);
                jsonObject.put("refferal", refferal);
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
//                callback.error();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(useLoading) Global.showLoading(activity.get(), "", "Loading");
        }
    }

    //UPDATE
    public static void update(Activity activity, String username, String password, String name, String email, Boolean useLoading, Callback callback) {
        new update(activity, username, password, name, email, useLoading, callback).execute("api/v1/user/update.php");
    }

    private static class update extends AsyncTask<String, Void, String> {

        final WeakReference<Activity> activity;
        final Callback callback;
        final Boolean useLoading;
        final String username;
        final String password;
        final String name;
        final String email;

        private update(Activity activity, String username, String password, String name, String email, Boolean useLoading, Callback callback) {
            this.activity = new WeakReference<>(activity);
            this.callback = callback;
            this.useLoading = useLoading;
            this.username = username;
            this.password = password;
            this.name = name;
            this.email = email;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", username);
                jsonObject.put("password", password);
                jsonObject.put("name", name);
                jsonObject.put("email", email);
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
//                callback.error();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(useLoading) Global.showLoading(activity.get(), "", "Loading");
        }
    }

    //DELETE
    public static void delete(Activity activity, String username, Boolean useLoading, CallbackSelect callback) {
        new delete(activity, username, useLoading, callback).execute("api/v1/user/delete.php");
    }

    private static class delete extends AsyncTask<String, Void, String> {

        final WeakReference<Activity> activity;
        final CallbackSelect callback;
        final Boolean useLoading;
        final String username;

        private delete(Activity activity, String username, Boolean useLoading, CallbackSelect callback) {
            this.activity = new WeakReference<>(activity);
            this.callback = callback;
            this.useLoading = useLoading;
            this.username = username;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", username);
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
                    callback.success(json.getJSONObject(Global.RESPONSE_DATA));
                }
                else {
                    callback.error();
                }
            }
            catch(Exception e) {
                Global.showLoading(activity.get(), "", e.getMessage());
                e.printStackTrace();
//                callback.error();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(useLoading) Global.showLoading(activity.get(), "", "Loading");
        }
    }
}
