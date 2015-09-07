package com.miniproject2.soma.miniproject02;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jiyoungpark on 15. 9. 6..
 */
public class HttpConnectionThread extends AsyncTask<String, Void, String> {

    String query;
    public HttpConnectionThread(String word) {

        this.query = word;
    }

    @Override
    protected String doInBackground(String... params) {

        final String DAUM_DICTIONARY_URL = "http://small.dic.daum.net/search.do?q=";
        URL url = null;
        String response = null;

        try {
            query = params.toString();
            url = new URL(DAUM_DICTIONARY_URL + query);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000 /* milliseconds */);
            httpURLConnection.setConnectTimeout(15000 /* milliseconds */);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);

            httpURLConnection.connect();

            response = httpURLConnection.getURL().toString();
            Log.e("RESPONSE", "The response is: " + response);
        }
        catch (IOException e) {

        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPreExecute();
    }

}
