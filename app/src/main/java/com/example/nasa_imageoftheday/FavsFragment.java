package com.example.nasa_imageoftheday;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;

public class FavsFragment extends Fragment {

    private AppCompatActivity par;

    public FavsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater infl, ViewGroup con, Bundle savedState) {

        Bundle bunData = getArguments();
        View res = infl.inflate(R.layout.favs_details_fragment, con, false);
        TextView titl = res.findViewById(R.id.titleFavs);
        titl.setText("\""+bunData.getString("TITLE")+"\"");

        TextView dt = res.findViewById(R.id.dateFavs);
        dt.setText(bunData.getString("DATE"));

        TextView explan = res.findViewById(R.id.explanFavs);
        explan.setText(bunData.getString("EXPLANATION"));

        TextView url = res.findViewById(R.id.urlFavss);
        url.setText(bunData.getString("HDURL"));

        ImageView picture = res.findViewById(R.id.rowFavsDeets);
        picture.setImageBitmap(BitmapFactory.decodeFile(bunData.getString("FILEPATH")));

        Button buttonHide = res.findViewById(R.id.hideButton);
        buttonHide.setOnClickListener( (click) -> {
            par.getSupportFragmentManager().beginTransaction().remove(this).commit();
        });

        return res;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        par = (AppCompatActivity) context;
    }
}
