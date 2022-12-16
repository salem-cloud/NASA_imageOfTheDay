package com.example.nasa_imageoftheday;

import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

public class AppDao extends AppCompatActivity {

    public void openWebPage(String u) {
        Uri wpage = Uri.parse(u);
        Intent intent = new Intent(Intent.ACTION_VIEW, wpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
