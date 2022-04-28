package com.example.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void printToLogs(View view) {
        // Find first menu item TextView and print the text to the logs
        TextView textView1 = (TextView) findViewById(R.id.menu_item_1);
        Log.i("EnterprinseActivity.java", (String) textView1.getText());
        // Find second menu item TextView and print the text to the logs
        TextView textView2 = (TextView) findViewById(R.id.menu_item_2);
        Log.i("EnterprinseActivity.java", (String) textView2.getText());
        // Find third menu item TextView and print the text to the logs
        TextView textView3 = (TextView) findViewById(R.id.menu_item_3);
        Log.i("EnterprinseActivity.java", (String) textView3.getText());
    }
}