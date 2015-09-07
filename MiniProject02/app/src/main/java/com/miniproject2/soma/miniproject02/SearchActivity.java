package com.miniproject2.soma.miniproject02;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    EditText editText_word;
    Button button_search;
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
        listView_result = (ListView) findViewById(R.id.listView_result);

        listView_result.setFocusable(false);
    }

    private void initController() {
        customAdapter = new CustomAdapter(getApplicationContext(), source);
        listView_result.setAdapter(customAdapter);
    }

    private void initListener() {

        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(editText_word.getText())) {

                    Toast.makeText(getApplicationContext(), "Value cannot be null", Toast.LENGTH_LONG).show();
                    return;
                }

                Data data = new Data();

                data.word = editText_word.getText().toString();
                data.mean = "임시";


                new HttpConnectionThread(data.word).execute();

                source.add(0, data);

                customAdapter.notifyDataSetChanged();

                editText_word.setText("");
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
}
