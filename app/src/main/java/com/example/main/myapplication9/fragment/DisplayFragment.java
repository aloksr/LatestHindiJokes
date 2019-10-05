package com.example.main.myapplication9.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.main.myapplication9.R;
import com.example.main.myapplication9.activity.DataBaseHelper;
import com.example.main.myapplication9.activity.DataBaseHelper2;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class DisplayFragment extends Fragment implements View.OnClickListener {

    TextView textView, extra_text;
    Button buttonNext, buttonPrev, buttonFav, buttonLt, button_list_view, buttonShare, buttonCopy, buttonDownload, button_link;
    ArrayList<String> list_fav = new ArrayList<>();
    ArrayList<String> list_jokes = new ArrayList<>();
    static String mPreviousQuoteTitle;
    int counter = 0;
    int value;
    Cursor c;
    private AdView mAdView;
    DataBaseHelper mydbhelper;
    DataBaseHelper2 mydbhelper2;
    InterstitialAd mInterstitialAd;
    String term;
    Object object;
    LinearLayout content2;
    File mFile;
    String mTempDir;
    int imgnum = 0;

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.display_fragment, container, false);
        content2 = (LinearLayout) view.findViewById(R.id.content2);
        textView = (TextView) view.findViewById(R.id.txt);
        buttonNext = (Button) view.findViewById(R.id.nxt);
        buttonPrev = (Button) view.findViewById(R.id.pre);
        buttonFav = (Button) view.findViewById(R.id.fav);
        buttonLt = (Button) view.findViewById(R.id.lt);
        buttonShare = (Button) view.findViewById(R.id.share);
        buttonCopy = (Button) view.findViewById(R.id.copy);

        buttonNext.setOnClickListener(this);
        buttonPrev.setOnClickListener(this);
        buttonFav.setOnClickListener(this);
        buttonLt.setOnClickListener(this);
        buttonCopy.setOnClickListener(this);
        buttonShare.setOnClickListener(this);


        final NativeExpressAdView adView = (NativeExpressAdView) view.findViewById(R.id.adView1);
        adView.setVisibility(View.GONE);
        AdRequest request = new AdRequest.Builder()
                .addTestDevice(String.valueOf(R.string.appid))
                .build();
        adView.loadAd(request);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adView.setVisibility(View.VISIBLE);
            }
        });
        mInterstitialAd = new InterstitialAd(getActivity());

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));


        // Load ads into Interstitial Ads

        mAdView = (AdView) view.findViewById(R.id.adView);
        mAdView.setVisibility(View.GONE);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mAdView.setVisibility(View.VISIBLE);
            }
        });

        mydbhelper2 = new DataBaseHelper2(getActivity());
        try {
            mydbhelper2.createDatabase();

        } catch (IOException e) {
            throw new Error("Unable To create database");
        }
        try {

            mydbhelper2.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }
        TabThreeFragment.list_fav= mydbhelper2.getAllFavourite();
        mydbhelper = new DataBaseHelper(getActivity());
        try {
            mydbhelper.createDatabase();

        } catch (IOException e) {
            throw new Error("Unable To create database");
        }
        try {

            mydbhelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }
        list_jokes = mydbhelper.getAllJokes();
        textView.setText(list_jokes.get(counter));
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nxt:
                AdRequest adRequest1 = new AdRequest.Builder().build();
                mInterstitialAd.loadAd(adRequest1);
                mInterstitialAd.setAdListener(new AdListener() {
                    public void onAdLoaded() {
                        showInterstitial();
                    }
                });
                mPreviousQuoteTitle = textView.getText().toString();
                list_jokes.add(textView.getText().toString());
                counter++;
                if (counter >= list_jokes.size())
                    counter = 0;
                textView.setText(list_jokes.get(counter));

                //  mydbhelper.openDataBase();
                if (mydbhelper2.getAllFavourite().contains(String.valueOf(textView.getText().toString()))) {
                    buttonFav.setBackgroundResource(R.drawable.fav_fill);
                } else {
                    buttonFav.setBackgroundResource(R.drawable.fav_blank);
                }
                break;
            case R.id.pre:
                try {
                    int size = list_jokes.size();
                    //  Log.d(name, "Previous Quotes Size: " + size);
                    if (size != 0) {
                        mPreviousQuoteTitle = list_jokes.get(size - 1);
                        textView.setText(mPreviousQuoteTitle);
                        list_jokes.remove(size - 1);
                        size--;
                    }

                    //mydbhelper.openDataBase();
                    if (mydbhelper2.getAllFavourite().contains(String.valueOf(textView.getText().toString()))) {
                        buttonFav.setBackgroundResource(R.drawable.fav_fill);
                    } else {
                        buttonFav.setBackgroundResource(R.drawable.fav_blank);
                    }


                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.fav:
                if (buttonFav.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.fav_fill).getConstantState())) {
                    try {
                        //  mydbhelper.openDataBase();
                        mydbhelper2.delete(textView.getText().toString());
                        buttonFav.setBackgroundResource(R.drawable.fav_blank);
                        Toast.makeText(getActivity(), "Removed from Favourite list", Toast.LENGTH_SHORT).show();


                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        e.getMessage();
                    }

                } else {
                    //try {
                     mydbhelper2.openDataBase();
                    mydbhelper2.insertFavourite(textView.getText().toString());
                    list_fav = mydbhelper2.getAllFavourite();
                    buttonFav.setBackgroundResource(R.drawable.fav_fill);
                    Toast.makeText(getActivity(), "Added to Favourite list", Toast.LENGTH_SHORT).show();

                }

                break;
            case R.id.lt:
                if (mydbhelper2.getAllFavourite().isEmpty()) {
                    Toast.makeText(getActivity(), "Please select some favourite jokes", Toast.LENGTH_SHORT).show();

                } else {
                    FavDisplayFragment favDisplayFragment = new FavDisplayFragment();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame, favDisplayFragment, "gift");
                    fragmentTransaction.commit();

                    if (mydbhelper2.getAllFavourite().contains(String.valueOf(textView.getText().toString()))) {
                        buttonFav.setBackgroundResource(R.drawable.fav_fill);
                    } else {
                        buttonFav.setBackgroundResource(R.drawable.fav_blank);
                    }

                }
                break;
            case R.id.copy:
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Jokes", textView.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
                break;

            case R.id.share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = textView.getText().toString();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Latest Hindi Jokes");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(sharingIntent);
                if (mydbhelper.getAllFavourite().contains(String.valueOf(textView.getText().toString()))) {
                    buttonFav.setBackgroundResource(R.drawable.fav_fill);
                } else {
                    buttonFav.setBackgroundResource(R.drawable.fav_blank);
                }

                break;
        }
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mydbhelper2.openDataBase();
        if (mydbhelper2.getAllFavourite().contains(String.valueOf(textView.getText().toString())))
        {
            buttonFav.setBackgroundResource(R.drawable.fav_fill);
        } else {
            buttonFav.setBackgroundResource(R.drawable.fav_blank);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mydbhelper2.openDataBase();
        if (mydbhelper2.getAllFavourite().contains(String.valueOf(textView.getText().toString())))
        {
            buttonFav.setBackgroundResource(R.drawable.fav_fill);
        } else {
            buttonFav.setBackgroundResource(R.drawable.fav_blank);
        }
    }
}
