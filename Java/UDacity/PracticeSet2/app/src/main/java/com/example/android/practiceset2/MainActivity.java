package com.example.android.practiceset2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String firstName = "Lyla";
        String lastName = "Fujiwara";
        String contactInfo = firstName + " " + lastName;
        contactInfo = contactInfo + "<" + lastName + "." + firstName + "@justjava.com>";
        display1(contactInfo);
    }

    void display1(String i) {
        TextView t = (TextView) findViewById(R.id.display_text_view1);
        t.setText(i);
    }

    void display2(String i) {
        TextView t = (TextView) findViewById(R.id.display_text_view2);
        t.setText(i);
    }
    void display3(String i) {
        TextView t = (TextView) findViewById(R.id.display_text_view3);
        t.setText(i);
    }
}