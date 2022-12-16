package com.example.nasa_imageoftheday;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.empty_activity_layout);
        Bundle passedData = getIntent().getExtras();
        FavsFragment favFrag = new FavsFragment();
        favFrag.setArguments( passedData );
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frag_favs, favFrag)
                .commit();
    }
}
