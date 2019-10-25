package com.tianzhuan.healthstatusview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private HealthStatusView mHealthStatusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHealthStatusView=findViewById(R.id.hsView);

    }

    public void refresh(View view) {
        mHealthStatusView.refreshView();
    }
}
