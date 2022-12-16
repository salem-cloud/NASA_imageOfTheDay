package com.example.nasa_imageoftheday;

import androidx.annotation.NonNull;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.navigation.NavigationView;
import android.os.Bundle;
import android.view.Menu;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import android.content.Intent;
import androidx.drawerlayout.widget.DrawerLayout;

public class InfoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar tbar = (Toolbar)findViewById(R.id.tbar);
        setSupportActionBar(tbar);
        DrawerLayout drwr = (DrawerLayout) findViewById(R.id.layout_drwr);
        ActionBarDrawerToggle tgl = new ActionBarDrawerToggle(this, drwr, tbar, R.string.open, R.string.close);
        drwr.addDrawerListener(tgl);
        tgl.syncState();
        NavigationView viewNav = findViewById(R.id.view_navigation);
        viewNav.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem itm) {

        switch(itm.getItemId())
        {
            case R.id.menuHome:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.menuFavs:
                startActivity(new Intent(this, FavsActivity.class));
                break;
            case R.id.menuUser:
                startActivity(new Intent(this, AccountUserActivity.class));
                break;
            case R.id.menuInfo:
                startActivity(new Intent(this, InfoActivity.class));
                break;
        }

        DrawerLayout layoutDrwr = findViewById(R.id.layout_drwr);
        layoutDrwr.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu men) {
        MenuInflater infl = getMenuInflater();
        infl.inflate(R.menu.help_button, men);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem itm) {

        switch(itm.getItemId())
        {
            case R.id.ButtonHelp:

                AlertDialog.Builder alertMsg = new AlertDialog.Builder(this);
                alertMsg.setTitle(R.string.useHow)
                        .setMessage(R.string.abtHow)
                        .setNegativeButton(R.string.close, (click, arg) -> {

                        })
                        .create().show();
                break;
            case R.id.BtnHelpHome:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.BtnHelpFavs:
                startActivity(new Intent(this, FavsActivity.class));
                break;
            case R.id.BtnHelpAcc:
                startActivity(new Intent(this, AccountUserActivity.class));
                break;
            case R.id.BtnHelpInfo:
                startActivity(new Intent(this, InfoActivity.class));
                break;
        }
        return true;
    }
}