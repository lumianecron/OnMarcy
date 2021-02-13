package com.android.onmarcy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Global {
    public static String RESPONSE_CODE = "code";
    public static String RESPONSE_SUCCESS = "success";
    public static String RESPONSE_DATA = "data";
    public static String RESPONSE_MESSAGE = "message";

    private static ProgressDialog loadingDialog;
    private static int progressMax = 0;
    private static int progressNow = 0;

    public static SharedPreferences sharedPreferences;
    public enum SHARED_INDEX{
        USER,
    }

    public Global(Context context){
        sharedPreferences = context.getSharedPreferences("shared", Context.MODE_PRIVATE);
    }

    //FOR GET SHAREDPREFERENCES
    public static void clearShared() {
        sharedPreferences.edit().clear().apply();
    }

    //FOR GET SHAREDPREFERENCES
    public static String getShared(SHARED_INDEX _key, String _defaultValue) {
        return sharedPreferences.getString(_key.name(), _defaultValue);
    }

    //FOR SET SHAREDPREFERENCES
    public static void setShared(SHARED_INDEX _key, String _param) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(_key.name(), _param);
        editor.apply();
    }

    public static void hideLoading()
    {
        if(loadingDialog != null && loadingDialog.isShowing()) loadingDialog.dismiss();
        progressMax = 0;
        progressNow = 0;
    }

    public static void showLoading(Context _context, String _title, String _message)
    {
        if(loadingDialog != null && loadingDialog.isShowing()) return;
        loadingDialog = new ProgressDialog(_context);
        if(!_title.equals("")) loadingDialog.setTitle(_title);
        loadingDialog.setMessage(_message);
        loadingDialog.setCancelable(true);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();
    }

    public static String executePost(String _targetURL, JSONObject _jsonObject, int _timeoutMiliSecond){
        String url = "octa.genoratory.com/";
        String hostUrl = "https://" + url;

        Log.wtf("HOST", hostUrl + _targetURL);

        InputStream inputStream;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpClient = new DefaultHttpClient();
            HttpParams httpParameters = httpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, _timeoutMiliSecond);
            HttpConnectionParams.setSoTimeout(httpParameters, _timeoutMiliSecond);
            HttpConnectionParams.setTcpNoDelay(httpParameters, true);

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost( hostUrl + _targetURL );

            // 3. convert JSONObject to JSON to String
            String json = _jsonObject.toString();

            // 4. ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity stringEntity = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(stringEntity);
            Log.wtf("PARAMETER", new Gson().toJson(_jsonObject));

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpClient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null) result = convertInputStreamToString(inputStream);
            else result = "Did not work!";

        }
        catch (Exception e) { e.printStackTrace(); }

        // 11. return result
        Log.wtf("RESPONSE", result);
        return result;
    }

    private static String convertInputStreamToString(InputStream _inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(_inputStream));
        String line;
        StringBuilder result = new StringBuilder();
        while((line = bufferedReader.readLine()) != null)
            result.append(line);

        _inputStream.close();
        return result.toString();
    }

    public static String convertToAsterisk(String _text, int startIndex){
        int offset = _text.length() - Math.abs(startIndex);
        String result = "";
        if(offset < 0){
            return _text;
        }else{
            for(int i = 0; i < offset; i++){
                result += "*";
            }
            result += _text.substring(offset, _text.length());
            return result;
        }
    }

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        return b[3];
    }

    public static String hashToMD5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));

            return hexString.toString();
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
