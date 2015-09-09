package com.miniproject2.soma.miniproject02;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        final String PARSING_TAG1 = "<div class=\"txt_means_KUEK\">";
        final String PARSING_TAG2 = "</div>";
        String regex1 = "\\<.*?\\>";
        String regex2 = "<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>";
        URL url = null;
        String response = "";
        String mean = "";

        try {
            url = new URL(DAUM_DICTIONARY_URL + query);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000 /* milliseconds */);
            httpURLConnection.setConnectTimeout(15000 /* milliseconds */);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            while((response = bufferedReader.readLine()) != null) {
                if(response.contains(PARSING_TAG1)){
                    while(!response.contains(PARSING_TAG2)) {
                        mean += response;
                        response = bufferedReader.readLine();
                    }
                    break;
                }
            }
            mean = mean.replaceAll(regex1, "");
            mean = mean.replaceAll(regex2, "");

            Log.e("A", mean);
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
