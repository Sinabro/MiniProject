package com.miniproject2.soma.miniproject02;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    Data data;
    EditText editText_word;
    Button button_search, button_load, button_save, button_clear;
    RadioGroup radioGroup_align;
    ArrayList<Data> source;
    ListView listView_result;
    CustomAdapter customAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initModel();
        initView();
        initController();
        initListener();
    }

    private void initModel() {

        source = new ArrayList<>();
    }

    private void initView() {

        editText_word = (EditText) findViewById(R.id.editText_word);
        button_search = (Button) findViewById(R.id.button_search);
        button_load = (Button) findViewById(R.id.button_load);
        button_save = (Button) findViewById(R.id.button_save);
        button_clear = (Button) findViewById(R.id.button_clear);
        radioGroup_align = (RadioGroup) findViewById(R.id.radioGroup_align);
        listView_result = (ListView) findViewById(R.id.listView_result);

        listView_result.setFocusable(false);
    }

    private void initController() {

        customAdapter = new CustomAdapter(getApplicationContext(), source);
        listView_result.setAdapter(customAdapter);
    }

    private void initListener() {

        radioGroup_align.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                }
            }
        });

        button_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loadText();
                    }
                }).start();
            }
        });

        button_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                source.clear();
                customAdapter.notifyDataSetChanged();
            }
        });

        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(editText_word.getText())) {
                    Toast.makeText(getApplicationContext(), "Value cannot be null", Toast.LENGTH_LONG).show();
                    return;
                }

                data = new Data();
                data.word = editText_word.getText().toString();
                data.mean = "";

                for(int i = 0 ; i < source.size() ; i++) {
                    // 기존에 검색된 결과라면 있는 결과를 보여줌
                    if((source.get(i).word.equals(data.word)) && source.get(i).mean != null) {
                        source.get(i).increaseCount();
                        source.add(0, source.get(i));
                        source.remove(i+1);
                        customAdapter.notifyDataSetChanged();
                        editText_word.setText("");
                        return;
                    }
                }

                AsyncTask<String, Void, String> mTask = new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... params) {

                        final String DAUM_DICTIONARY_URL = "http://small.dic.daum.net/search.do?q=";
                        final String PARSING_TAG1 = "<div class=\"txt_means_KUEK\">";
                        final String PARSING_TAG2 = "</div>";

                        String regex1 = "\\<.*?\\>";
                        String regex2 = "<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>";

                        URL url = null;
                        String response = "";

                        try {
                            url = new URL(DAUM_DICTIONARY_URL + data.word);

                            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                            httpURLConnection.setReadTimeout(10000 /* milliseconds */);
                            httpURLConnection.setConnectTimeout(15000 /* milliseconds */);

                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                            while ((response = bufferedReader.readLine()) != null) {
                                if (response.contains(PARSING_TAG1)) {
                                    while (!response.contains(PARSING_TAG2)) {
                                        data.mean += response;
                                        response = bufferedReader.readLine();
                                    }
                                    break;
                                }
                            }
                            data.mean = data.mean.replaceAll(regex1, "");
                            data.mean = data.mean.replaceAll(regex2, "");
                            data.mean = data.mean.replaceAll("\t", "");

                        } catch (IOException e) {
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        super.onPreExecute();
                        source.add(0, data);
                        customAdapter.notifyDataSetChanged();
                        editText_word.setText("");
                    }
                };

                mTask.execute();
            }
        });

        listView_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Data data = (Data) customAdapter.getItem(position);
                Toast.makeText(getApplicationContext(), data.word + "\n" + data.mean, Toast.LENGTH_LONG).show();
            }
        });

        listView_result.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Data data = (Data) customAdapter.getItem(position);
                source.remove(data);
                customAdapter.notifyDataSetChanged();

                return true;
            }
        });
    }

    private void loadText() {
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.word);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "EUC_KR"));
            String temp = null;
            int i = 0;

            //파일의 전체 내용 읽어오기
            while ((temp = bufferedReader.readLine()) != null) {
                data = new Data();
                data.word = temp;
                source.add(i, data);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/*
    private void re() {
        InputStream inputStream = getResources().openRawResource(R.raw.word);
        ArrayList arrayList = new ArrayList<String>();
        String data;

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "EUC_KR"));
            while ((data = bufferedReader.readLine()) != null) {
                arrayList.add(data);
                Log.e("A", data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void test() {
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.word);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "EUC_KR"));
            StringBuilder stringBuilder = new StringBuilder();

            String temp = null;
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();

            //파일의 전체 내용 읽어오기
            while ((temp = bufferedReader.readLine()) != null) {
                try {
                    jsonObject.put("word", temp);
                    jsonObject.put("mean", "");
                    jsonObject.put("count", "0");
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
