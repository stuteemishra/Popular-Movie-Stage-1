package com.example.stutee.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    private ImageView mDisplayImage;

    private TextView mDisplayDetails;

    private String mDetails;




    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mDisplayImage = (ImageView) findViewById(R.id.tv_movie_poster);

        mDisplayDetails = (TextView) findViewById(R.id.tv_detail);


        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                mDetails = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
                String abc = mDetails.substring(0,63);
                String xyz = mDetails.substring(64);
                Picasso.with(mContext).load(abc).into(mDisplayImage);
                mDisplayDetails.setText(xyz);

            }
        }






        //Picasso.with(mContext).load(mImageUrl).into(mDisplayImage);


    }
}
