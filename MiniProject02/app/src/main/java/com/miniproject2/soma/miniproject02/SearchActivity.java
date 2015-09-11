package com.miniproject2.soma.miniproject02;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
                        Collections.sort(source, new AscendingCompare());
                        break;
                    case 2:
                        Collections.sort(source, new DescendingCompare());
                        break;
                    case 3:
                        Collections.sort(source, new CountCompare());
                        break;
                    case 4:
                        break;
                }
                customAdapter.notifyDataSetChanged();
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
                        Log.e("들어옴", "");
                        source.get(i).increaseCount();
                        source.add(0, source.get(i));
                        source.remove(i+1);
                        customAdapter.notifyDataSetChanged();
                        editText_word.setText("");

                        return;
                    }
                }

                // Daum Dictionary에서 단어 뜻 가져오기
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
                        for(int i = 0 ; i < source.size() ; i++) {
                            // load된 데이터에 mean이 존재하지 않을 때 추가
                            if((source.get(i).word.equals(data.word)) && source.get(i).mean == null) {
                                source.get(i).increaseCount();
                                source.get(i).setMean(data.mean);
                                source.add(0, source.get(i));
                                source.remove(i+1); // 기존 listview에 있던 data 삭제
                                customAdapter.notifyDataSetChanged();
                                editText_word.setText("");
                                return;
                            }
                        }

                        // 처음 검색한 단어일 경우 listview에 추가
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

        FileInputStream fileInputStream = null;
        BufferedReader bufferedReader;
        String temp = null;
        String[] splite = null;
        String save = null;
        int i = 0;

        String path = getFilesDir().getAbsolutePath() + "/save.txt";

        File files = new File(path);

        if(files.exists() == true) {
            try {
                fileInputStream = openFileInput("save.txt");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, "EUC_KR"));
                try {
                    while ((temp = bufferedReader.readLine()) != null) {
                        data = new Data();
                        // 검색했던 결과의 경우 토큰을 통해 나눠서 listview에 보여줌
                        splite = temp.split("\\|");
                        data.word = splite[0];
                        data.mean = splite[1];
                        //data.count = Integer.parseInt(splite[2]);
                        source.add(i, data);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        else {
            try {
                InputStream inputStream = getResources().openRawResource(R.raw.word);
                FileOutputStream fileOutputStream;
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "EUC_KR"));

                fileOutputStream = openFileOutput("save.txt", MODE_PRIVATE);

                //파일의 전체 내용 읽어오기
                while ((temp = bufferedReader.readLine()) != null) {
                    data = new Data();
                    data.word = temp;
                    data.mean = null;
                    data.count = 0;

                    source.add(i, data);
                    save = data.word + "|" + data.mean + "|" + data.count + "\n";
                    fileOutputStream.write(save.getBytes());
                    i++;
                }
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    private class AscendingCompare implements Comparator<Data> {

        @Override
        public int compare(Data lhs, Data rhs) {
            return lhs.getWord().compareTo(rhs.getWord());
        }
    }

    private class DescendingCompare implements Comparator<Data> {

        @Override
        public int compare(Data lhs, Data rhs) {
            return rhs.getWord().compareTo(lhs.getWord());
        }
    }

    private class CountCompare implements Comparator<Data> {

        @Override
        public int compare(Data lhs, Data rhs) {
            return lhs.getCount() > rhs.getCount() ? -1 : lhs.getCount() < rhs.getCount() ? 1 : 0;
        }
    }
}