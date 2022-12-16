package com.example.nasa_imageoftheday;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import java.net.URL;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.navigation.NavigationView;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.io.BufferedReader;
import java.io.File;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String titl;
    private SQLiteDatabase database;
    private String explan;
    private nasaImageQuery request;
    private static TextView selectDate;
    private ProgressBar barProgress;
    private String textDate;
    private static String pickDate;
    private String url;
    public static final String JSON_URL = "https://api.nasa.gov/planetary/apod?api_key=MeUdjXL0FeXyLaUJOWRihbo3phuEX2o1BSafshoV";
    private String urlHD;
    private Bitmap picture;
    private String now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy pol = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(pol);
        SharedPreferences uname = getSharedPreferences("username", Context.MODE_PRIVATE);
        TextView uDisplay = findViewById(R.id.uname_main);
        String user = uname.getString("username", null);
        uDisplay.setText(getString(R.string.welcome)+" "+ user +"!");
        DBStore dbStore = new DBStore(this);
        database = dbStore.getWritableDatabase();
        barProgress = findViewById(R.id.progress_bar);
        barProgress.setVisibility(View.VISIBLE);
        SimpleDateFormat formatting = new SimpleDateFormat("yyyy-MM-dd");
        now = formatting.format(new Date());
        selectDate = findViewById(R.id.picked_date);
        selectDate.setText(now);
        request = new nasaImageQuery();
        request.execute(JSON_URL);
        Toolbar tbar = (Toolbar)findViewById(R.id.tbar);
        setSupportActionBar(tbar);
        DrawerLayout drwr = (DrawerLayout) findViewById(R.id.layout_drwr);
        ActionBarDrawerToggle tgl = new ActionBarDrawerToggle(this, drwr, tbar, R.string.open, R.string.close);
        drwr.addDrawerListener(tgl);
        tgl.syncState();
        NavigationView viewNav = findViewById(R.id.view_navigation);
        viewNav.setNavigationItemSelectedListener(this);
        Button btnDate = findViewById(R.id.datePickerBtn);
        btnDate.setOnClickListener( (click) -> {
            showPicker(btnDate);
        });
        Button btnFetch = findViewById(R.id.fetchDateContent);
        btnFetch.setOnClickListener( (click) -> {
            String temporary = JSON_URL + pickDate;
            nasaImageQuery requestTwo = new nasaImageQuery();
            requestTwo.execute(temporary);
        });
        Button favBtn = findViewById(R.id.FavThisImage);
        favBtn.setOnClickListener( (click) -> {

            String resp;
            String fname = titl + ".png";
            if (!fileExistance(fname)) {
                nasaImageSave(titl, url);
                resp = getString(R.string.savedImg);
            } else {
                resp = getString(R.string.alreadyFav);
            }
            Cursor result = database.rawQuery("select * from "+ dbStore.TB_NAME +" where DATE = ?", new String[] {textDate});
            if(result.getCount() == 0){
                ContentValues row = new ContentValues();
                row.put(dbStore.TITLE, titl);
                row.put(dbStore.DATE, textDate);
                row.put(dbStore.FILENAME, fname);
                row.put(dbStore.HOUR, urlHD);
                row.put(dbStore.EXPLAIN, explan);
                long id = database.insert(dbStore.TB_NAME, "null", row);
            }
            Toast.makeText(MainActivity.this, resp, Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem it) {

        switch(it.getItemId())
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

        DrawerLayout drwrLayt = findViewById(R.id.layout_drwr);
        drwrLayt.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem it) {

        switch(it.getItemId())
        {
            case R.id.ButtonHelp:

                AlertDialog.Builder alertMsg = new AlertDialog.Builder(this);
                alertMsg.setTitle(R.string.useHow)
                        .setMessage(R.string.mainHow)
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

    @Override
    public boolean onCreateOptionsMenu(Menu me) {

        MenuInflater infl = getMenuInflater();
        infl.inflate(R.menu.help_button, me);

        return true;
    }

    public class nasaImageQuery extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... args) {

            try {
                URL urlTemp = new URL(args[0]);
                HttpURLConnection conn = (HttpURLConnection) urlTemp.openConnection();
                InputStream resp = conn.getInputStream();
                BufferedReader rdr = new BufferedReader(new InputStreamReader(resp, "UTF-8"), 8);
                StringBuilder stringBuilder = new StringBuilder();
                String currLine = null;
                while ((currLine = rdr.readLine()) != null) {
                    stringBuilder.append(currLine + "\n");
                }
                String result = stringBuilder.toString();
                JSONObject json = new JSONObject(result);
                titl = json.getString("title");
                publishProgress(16);
                textDate = json.getString("date");
                publishProgress(32);
                explan = json.getString("explanation");
                publishProgress(48);
                url = json.getString("url");
                publishProgress(64);
                urlHD = json.getString("hdurl");
                publishProgress(80);
                picture = imageDwnldNasa(url);
                publishProgress(100);

            } catch(IOException | JSONException e) {

            }

            return "Done";
        }

        protected Bitmap imageDwnldNasa(String link) {
            HttpURLConnection conn;
            try {
                URL urlImg = new URL(link);
                conn = (HttpURLConnection) urlImg.openConnection();
                conn.connect();
                int codeResp = conn.getResponseCode();
                if (codeResp == 200) {
                    picture = BitmapFactory.decodeStream(conn.getInputStream());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return picture;
        }

        protected void onProgressUpdate(Integer ... val) { barProgress.setProgress(val[0]);};

        protected void onPostExecute(String fromDoInBackground) {
            ImageView img_layout = findViewById(R.id.view_image_main);
            TextView title_layout = findViewById(R.id.titleMain);
            TextView explan_layout = findViewById(R.id.explanationMainContent);
            TextView date_layout = findViewById(R.id.dateMainContent);
            TextView url_layout = findViewById(R.id.hdUrlMainContent);
            img_layout.setImageBitmap(picture);
            title_layout.setText(titl);
            explan_layout.setText(explan);
            date_layout.setText(textDate);
            url_layout.setText(urlHD);
        }
    }

    public static class PickDateFrag extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        public void onDateSet(DatePicker view, int year, int month, int day) {
            month++;
            String date;
            date = year + "-" + month + "-" + day;
            selectDate.setText(date);
            pickDate = "&date=" + date;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            DatePickerDialog dlg = new DatePickerDialog(getActivity(), this, year, month, day);
            dlg.getDatePicker().setMaxDate(new Date().getTime());
            return dlg;
        }


    }

    public boolean fileExistance(String fname) {
        File f = getBaseContext().getFileStreamPath(fname);
        return f.exists();
    }

    protected void nasaImageSave(String titl, String url) {
        HttpURLConnection conn;
        Bitmap pictureOne;
        try {
            URL urlImg = new URL(url);
            conn = (HttpURLConnection) urlImg.openConnection();
            conn.connect();
            int codeResp = conn.getResponseCode();
            if (codeResp == 200) {
                pictureOne = BitmapFactory.decodeStream(conn.getInputStream());
                FileOutputStream oStream = openFileOutput( titl + ".png", Context.MODE_PRIVATE);
                pictureOne.compress(Bitmap.CompressFormat.PNG, 100, oStream);
                oStream.flush();
                oStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void showPicker(View v) {
        DialogFragment fragNew = new PickDateFrag();
        fragNew.show(getSupportFragmentManager(), "datePicker");
    }




}