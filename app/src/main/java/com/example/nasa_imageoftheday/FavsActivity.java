package com.example.nasa_imageoftheday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import android.content.Intent;
import java.io.File;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.database.sqlite.SQLiteDatabase;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import com.google.android.material.navigation.NavigationView;
import android.widget.TextView;
import android.view.ViewGroup;
import android.graphics.BitmapFactory;

public class FavsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TheAdapter adapter;
    ListView lView;
    SQLiteDatabase database;
    ArrayList<imageNasa> imageArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favs);
        Toolbar tbar = (Toolbar)findViewById(R.id.tbar);
        setSupportActionBar(tbar);
        DrawerLayout drwr = (DrawerLayout) findViewById(R.id.layout_drwr);
        ActionBarDrawerToggle tgl = new ActionBarDrawerToggle(this, drwr, tbar, R.string.open, R.string.close);
        drwr.addDrawerListener(tgl);
        tgl.syncState();
        NavigationView navView = findViewById(R.id.view_navigation);
        navView.setNavigationItemSelectedListener(this);
        imageArray = new ArrayList<>();
        adapter = new TheAdapter();
        lView = findViewById(R.id.list_favs);
        lView.setAdapter(adapter);

        dbLoad();
        lView.setOnItemClickListener( (list, item, position, id) -> {

            Bundle dataToPass = new Bundle();
            dataToPass.putString("TITLE", imageArray.get(position).getTitl());
            dataToPass.putString("HDURL", imageArray.get(position).getHDurl());
            dataToPass.putString("DATE", imageArray.get(position).getDate());
            dataToPass.putString("FILEPATH", getBaseContext().getFileStreamPath(imageArray.get(position).getNameFile()).getPath());
            dataToPass.putString("EXPLANATION", imageArray.get(position).getExplan());

            FavsFragment favsFragment = new FavsFragment();
            favsFragment.setArguments( dataToPass ); //pass data to the the fragment
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frag_favs, favsFragment)
                    .commit();
        });

        lView.setOnItemLongClickListener( (li, itm, pos, id) -> {
            String titleDel = imageArray.get(pos).getTitl();
            String dateDel = imageArray.get(pos).getDate();
            String idDel = String.valueOf(imageArray.get(pos).getId());
            String filenameDel = imageArray.get(pos).getNameFile();
            AlertDialog.Builder alertDeleteMsg = new AlertDialog.Builder(this);
            alertDeleteMsg.setTitle(R.string.popupDel)
                    .setMessage("\""+ titleDel + "\", the picture-of-the-day from " + dateDel + "?")
                    .setPositiveButton(getString(R.string.yes), (click, arg) -> {
                        database.delete(DBStore.TB_NAME, "_id=?", new String[] {idDel});
                        imageArray.remove(pos);
                        File file = getBaseContext().getFileStreamPath(filenameDel);
                        if (file.exists()) {
                            file.delete();
                        }
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton(getString(R.string.no), (click, arg) -> {

                    })
                    .create().show();

            return true;
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

        DrawerLayout drawLay = findViewById(R.id.layout_drwr);
        drawLay.closeDrawer(GravityCompat.START);

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
                        .setMessage(R.string.favsHow)
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

    public class TheAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return imageArray.size();
        }

        @Override
        public imageNasa getItem(int pos) {
            return imageArray.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return getItem(pos).getId();
        }

        @Override
        public View getView(int pos, View vi, ViewGroup pare) {
            View viewNew = null;
            LayoutInflater infl = getLayoutInflater();
            ImageView imgRow;
            TextView titleRow;
            TextView dateRow;
            imageNasa nasaImg = (imageNasa) getItem(pos);
            viewNew = infl.inflate(R.layout.favs_row, pare, false);
            imgRow = viewNew.findViewById(R.id.imageFavsRow);
            imgRow.setImageBitmap(BitmapFactory.decodeFile(getBaseContext().getFileStreamPath(nasaImg.getNameFile()).getPath()));
            titleRow = viewNew.findViewById(R.id.titleFavsRow);
            titleRow.setText(nasaImg.getTitl());
            dateRow = viewNew.findViewById(R.id.dateFavsRow);
            dateRow.setText(nasaImg.getDate());
            return viewNew;
        }
    }

    public boolean fileExistance(String fn) {
        File fi = getBaseContext().getFileStreamPath(fn);
        return fi.exists();
    }

    private void dbLoad() {

        DBStore dbOpen = new DBStore(this);
        database = dbOpen.getWritableDatabase();

        String [] cols = {DBStore.ID, DBStore.TITLE, DBStore.DATE, DBStore.FILENAME, DBStore.EXPLAIN, DBStore.HOUR};

        Cursor res = database.query( DBStore.TB_NAME, cols, null, null, null, null, null);

        int dateIdx = res.getColumnIndex(DBStore.DATE);
        int idColIdx = res.getColumnIndex(DBStore.ID);
        int urlColIdx = res.getColumnIndex(DBStore.HOUR);
        int explanColIdx = res.getColumnIndex(DBStore.EXPLAIN);
        int titleIdx = res.getColumnIndex(DBStore.TITLE);
        int fileColIdx = res.getColumnIndex(DBStore.FILENAME);

        while( res.moveToNext() ){
            long id = res.getLong(idColIdx);
            String titl = res.getString(titleIdx);
            String textDate = res.getString(dateIdx);
            String nameFile = res.getString(fileColIdx);
            String explan = res.getString(explanColIdx);
            String url = res.getString(urlColIdx);

            imageArray.add(new imageNasa(id, titl, textDate, nameFile, explan, url));
        }
        res.close();
    }
}