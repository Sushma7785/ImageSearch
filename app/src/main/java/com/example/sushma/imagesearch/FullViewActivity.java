package com.example.sushma.imagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by sushma on 6/24/16.
 */
public class FullViewActivity extends Activity{

    private ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String uri = intent.getStringExtra("uri");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_view);
        imgView = (ImageView) findViewById(R.id.full_view);
        new DownloadImageTask(imgView).execute(uri);

    }


}
