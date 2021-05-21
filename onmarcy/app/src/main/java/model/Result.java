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

public class Result {
    @SerializedName("like")
    private int like;

    @SerializedName("comment")
    private int comment;

    @SerializedName("save")
    private int save;

    @SerializedName("impression")
    private int impression;

    @SerializedName("reach")
    private int reach;

    @SerializedName("engagement")
    private int engagement;

    @SerializedName("proof_post")
    private String linkPost;

    @SerializedName("proof_story")
    private String linkStory;

    @SerializedName("proof_bio")
    private String linkBio;

    @SerializedName("notes")
    private String notes;

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public int getSave() {
        return save;
    }

    public void setSave(int save) {
        this.save = save;
    }

    public int getImpression() {
        return impression;
    }

    public void setImpression(int impression) {
        this.impression = impression;
    }

    public int getReach() {
        return reach;
    }

    public void setReach(int reach) {
        this.reach = reach;
    }

    public int getEngagement() {
        return engagement;
    }

    public void setEngagement(int engagement) {
        this.engagement = engagement;
    }

    public String getLinkPost() {
        return linkPost;
    }

    public void setLinkPost(String linkPost) {
        this.linkPost = linkPost;
    }

    public String getLinkStory() {
        return linkStory;
    }

    public void setLinkStory(String linkStory) {
        this.linkStory = linkStory;
    }

    public String getLinkBio() {
        return linkBio;
    }

    public void setLinkBio(String linkBio) {
        this.linkBio = linkBio;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public interface CallbackSelect {
        void success(JSONArray data);
        void error();
    }

    public interface Callback {
        void success();
        void error();
    }

    public Result(JSONObject jsonObject){
        try{
            this.like = jsonObject.has("like") ? jsonObject.getInt("like") : 0;
            this.comment = jsonObject.has("comment") ? jsonObject.getInt("comment") : 0;
            this.save = jsonObject.has("save") ? jsonObject.getInt("save") : 0;
            this.impression = jsonObject.has("impression") ? jsonObject.getInt("impression") : 0;
            this.reach = jsonObject.has("reach") ? jsonObject.getInt("reach") : 0;
            this.engagement = jsonObject.has("engagement") ? jsonObject.getInt("engagement") : 0;
            this.notes = jsonObject.has("notes") ? jsonObject.getString("notes") : "";

            this.linkPost = jsonObject.has("proof_post") ? jsonObject.getString("proof_post") : "";
            this.linkStory = jsonObject.has("proof_story") ? jsonObject.getString("proof_story") : "";
            this.linkBio = jsonObject.has("proof_bio") ? jsonObject.getString("proof_bio") : "";
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void select(Activity activity, int code, Campaign.CallbackSelect callback) {
        new select(activity, code, callback).execute("v1/campaign/select_result");
    }

    private static class select extends AsyncTask<String, Void, String> {
        final WeakReference<Activity> activity;
        final Campaign.CallbackSelect callback;
        final int code;


        public select(Activity activity, int code, Campaign.CallbackSelect callback) {
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
}
