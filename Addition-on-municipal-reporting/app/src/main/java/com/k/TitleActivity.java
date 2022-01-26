package com.k;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TitleActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnActTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        btnActTwo = (Button) findViewById(R.id.button_main_form);
        btnActTwo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_main_form:
                // TODO Call second activity
                //Intent intent = new Intent(this, MainActivity.class);
                Intent intent = new Intent(this, AuthorizationActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
