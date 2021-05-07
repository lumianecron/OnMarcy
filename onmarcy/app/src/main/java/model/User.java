package model;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.onmarcy.MainActivity;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.android.onmarcy.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class User {
    @SerializedName("hash")
    private String hash;

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

    @SerializedName("city_name")
    private String cityName;

    @SerializedName("user_type")
    private int userType;

    @SerializedName("refferal")
    private String refferal;

    @SerializedName("active")
    private int isActive;

    @NonNull
    public String getHash() {
        return hash;
    }

    public void setHash(@NonNull String hash) {
        this.hash = hash;
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getRefferal() {
        return refferal;
    }

    public void setRefferal(String refferal) {
        this.refferal = refferal;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
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
            this.hash = json.has("hash") ? json.getString("hash") : "";
            this.username = json.has("username") ? json.getString("username") : "";
            this.password = json.has("password") ? json.getString("password") : "";
            this.email = json.has("email") ? json.getString("email") : "";
            this.name = json.has("name") ? json.getString("name") : "";
            this.phone = json.has("phone") ? json.getString("phone") : "";
            this.cityCode = json.has("city") ? json.getInt("city") : 0;
            this.cityName = json.has("city_name") ? json.getString("city_name") : "";
            this.userType = json.has("user_type") ? json.getInt("user_type") : 0;
            this.refferal = json.has("refferal") ? json.getString("refferal") : "";
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //LOGOUT
    public static void logout() {
        Global.setShared(Global.SHARED_INDEX.USER, null);
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
                    callback.success(json.getJSONObject(Global.RESPONSE_DATA));
                } else {
                    callback.error();
                }
            } catch (Exception e) {
                Global.showLoading(activity.get(), "", e.getMessage());
                e.printStackTrace();
//                callback.error();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (useLoading) Global.showLoading(activity.get(), "", "Loading");
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
//                callback.error();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (useLoading) Global.showLoading(activity.get(), "", "Loading");
        }
    }

    //UPDATE
    public static void updateBrand(Activity activity, String name, String phone, int city, Boolean useLoading, Callback callback) {
        new updateBrand(activity, name, phone, city, useLoading, callback).execute("v1/profile/update_brand");
    }

    private static class updateBrand extends AsyncTask<String, Void, String> {
        final WeakReference<Activity> activity;
        final Callback callback;
        final Boolean useLoading;
        final String name;
        final String phone;
        final int city;

        private updateBrand(Activity activity, String name, String phone, int city, Boolean useLoading, Callback callback) {
            this.activity = new WeakReference<>(activity);
            this.callback = callback;
            this.useLoading = useLoading;
            this.name = name;
            this.phone = phone;
            this.city = city;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                User user = new User(new JSONObject(Global.getShared(Global.SHARED_INDEX.USER, "{}")));
                jsonObject.put("hash", user.getHash());
                jsonObject.put("phone", phone);
                jsonObject.put("address", "");
                jsonObject.put("city_code", city);
                jsonObject.put("name", name);
                jsonObject.put("photo", "");
                jsonObject.put("gender", 1);
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

    //UPDATE
    public static void updateMarketer(Activity activity, String name, String phone, int city, Boolean useLoading, Callback callback) {
        new updateMarketer(activity, name, phone, city, useLoading, callback).execute("v1/profile/update_marketer");
    }

    private static class updateMarketer extends AsyncTask<String, Void, String> {
        final WeakReference<Activity> activity;
        final Callback callback;
        final Boolean useLoading;
        final String name;
        final String phone;
        final int city;

        private updateMarketer(Activity activity, String name, String phone, int city, Boolean useLoading, Callback callback) {
            this.activity = new WeakReference<>(activity);
            this.callback = callback;
            this.useLoading = useLoading;
            this.name = name;
            this.phone = phone;
            this.city = city;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                User user = new User(new JSONObject(Global.getShared(Global.SHARED_INDEX.USER, "{}")));
                jsonObject.put("hash", user.getHash());
                jsonObject.put("phone", phone);
                jsonObject.put("address", "");
                jsonObject.put("city_code", city);
                jsonObject.put("name", name);
                jsonObject.put("photo", "");
                jsonObject.put("gender", 1);
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

    //VERIFY EMAIL
    public static void verifyEmail(Activity activity, String username, String email, Boolean useLoading, CallbackSelect callback) {
        new verifyEmail(activity, username, email, useLoading, callback).execute("v1/send_email_verification");
    }

    private static class verifyEmail extends AsyncTask<String, Void, String> {

        final WeakReference<Activity> activity;
        final CallbackSelect callback;
        final Boolean useLoading;
        final String username;
        final String email;

        private verifyEmail(Activity activity, String username, String email, Boolean useLoading, CallbackSelect callback) {
            this.activity = new WeakReference<>(activity);
            this.callback = callback;
            this.useLoading = useLoading;
            this.username = username;
            this.email = email;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", username);
                jsonObject.put("email", email);
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
                    callback.success(json.getJSONObject(Global.RESPONSE_DATA));
                } else {
                    callback.error();
                }
            } catch (Exception e) {
                Global.showLoading(activity.get(), "", e.getMessage());
                e.printStackTrace();
//                callback.error();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (useLoading) Global.showLoading(activity.get(), "", "Loading");
        }
    }

    //FORGOT PASSWORD
    public static void forgotPassword(Activity activity, String email, Boolean useLoading, CallbackSelect callback) {
        new forgotPassword(activity, email, useLoading, callback).execute("v1/send_email_forgot_pass");
    }

    private static class forgotPassword extends AsyncTask<String, Void, String> {

        final WeakReference<Activity> activity;
        final CallbackSelect callback;
        final Boolean useLoading;
        final String email;

        private forgotPassword(Activity activity, String email, Boolean useLoading, CallbackSelect callback) {
            this.activity = new WeakReference<>(activity);
            this.callback = callback;
            this.useLoading = useLoading;
            this.email = email;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("parameter", email);
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
                    callback.success(json.getJSONObject(Global.RESPONSE_DATA));
                } else {
                    callback.error();
                }
            } catch (Exception e) {
                Global.showLoading(activity.get(), "", e.getMessage());
                e.printStackTrace();
//                callback.error();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (useLoading) Global.showLoading(activity.get(), "", "Loading");
        }
    }

    //UPLOAD PICTURE
    public static void uploadPicture(Activity activity, String photo, Boolean useLoading, CallbackSelect callback) {
        new uploadPicture(activity, photo, useLoading, callback).execute("v1/profile/upload_picture");
    }

    private static class uploadPicture extends AsyncTask<String, Void, String> {

        final WeakReference<Activity> activity;
        final CallbackSelect callback;
        final Boolean useLoading;
        final String photo;

        private uploadPicture(Activity activity, String photo, Boolean useLoading, CallbackSelect callback) {
            this.activity = new WeakReference<>(activity);
            this.callback = callback;
            this.useLoading = useLoading;
            this.photo = photo;
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                User user = new User(new JSONObject(Global.getShared(Global.SHARED_INDEX.USER, "{}")));
                jsonObject.put("hash", user.getHash());
                jsonObject.put("image", photo);
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
                    callback.success(json.getJSONObject(Global.RESPONSE_DATA));
                } else {
                    callback.error();
                }
            } catch (Exception e) {
                Global.showLoading(activity.get(), "", e.getMessage());
                e.printStackTrace();
//                callback.error();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (useLoading) Global.showLoading(activity.get(), "", "Loading");
        }
    }
}
