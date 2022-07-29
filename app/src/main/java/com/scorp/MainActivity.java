package com.scorp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.scorp.ui.UserFragment;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, UserFragment.newInstance())
                .commit();
    }
}