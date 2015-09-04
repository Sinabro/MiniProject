package com.miniproject2.soma.miniproject02;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SearchActivity extends AppCompatActivity {

    private ListView listView;
    private CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        customAdapter = new CustomAdapter (this.getApplicationContext(), android.R.layout.activity_list_item, data);
        customAdapter.
    }

    private void init() {

        listView = (ListView) findViewById(R.id.list_item);

    }
}
