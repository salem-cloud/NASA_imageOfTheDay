package com.example.nasa_imageoftheday;

import android.content.Context;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.os.Bundle;
import android.widget.EditText;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.Menu;

public class AccountUserActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_user);
        Toolbar tbar = (Toolbar)findViewById(R.id.tbar);
        setSupportActionBar(tbar);
        DrawerLayout dr = (DrawerLayout) findViewById(R.id.layout_drwr);
        ActionBarDrawerToggle tgl = new ActionBarDrawerToggle(this, dr, tbar, R.string.open, R.string.close);
        dr.addDrawerListener(tgl);
        tgl.syncState();
        NavigationView nav = findViewById(R.id.view_navigation);
        nav.setNavigationItemSelectedListener(this);

        SharedPreferences saveUname = getSharedPreferences("username", Context.MODE_PRIVATE);
        EditText editText = findViewById(R.id.edit_uname);
        editText.setText(saveUname.getString("username", ""));

        Button btn = findViewById(R.id.saveUname);
        btn.setOnClickListener( (click) -> {
            SharedPreferences.Editor unameSaved = saveUname.edit();
            String name = editText.getText().toString();
            unameSaved.putString("username", name);
            unameSaved.commit();
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
            View v = this.getCurrentFocus();
            if (v == null) {
                v = new View(this);
            }
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            Snackbar.make(btn, getString(R.string.savedUname), Snackbar.LENGTH_SHORT).show();
        });

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

        DrawerLayout dLayout = findViewById(R.id.layout_drwr);
        dLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu men) {

        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.help_button, men);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem itm) {

        switch(itm.getItemId())
        {
            case R.id.ButtonHelp:

                AlertDialog.Builder alertMsg = new AlertDialog.Builder(this);
                alertMsg.setTitle(R.string.useHow)
                        .setMessage(R.string.accHow)
                        .setNegativeButton(R.string.close, (click, arg) -> {
                            // nothing
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
    // End of Toolbar functions
}