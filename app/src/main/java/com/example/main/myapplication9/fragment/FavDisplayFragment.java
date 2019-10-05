package com.example.main.myapplication9.fragment;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import java.util.ArrayList;

public class FavDisplayFragment extends Fragment implements View.OnClickListener {

    LinearLayout content1,content2;

    TextView textView,extra_text;
    Button Next, Prev, share, copy, download,home,link;
    static String mPreviousQuoteTitle;

    int counter = 0;
    String mTempDir, mSavedImageName;
    File mFile;
    int imgnum = 0;
    private AdView mAdView;
    ArrayList<String> list_fav = new ArrayList<>();
    DataBaseHelper mydbhelper;
    DataBaseHelper2 mydbhelper2;
    Cursor c;
    InterstitialAd mInterstitialAd;
     NativeExpressAdView adView;
    Boolean statusLocked;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.favdisplay_fragment, container, false);


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
        adView = (NativeExpressAdView) view.findViewById(R.id.adView1);
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
        content1 = (LinearLayout) view.findViewById(R.id.content1);



        textView = (TextView) view.findViewById(R.id.txt);
        Next = (Button) view.findViewById(R.id.Next);
        Prev = (Button) view.findViewById(R.id.Prev);
        share = (Button) view.findViewById(R.id.share);
        copy = (Button) view.findViewById(R.id.copy);
        home = (Button) view.findViewById(R.id.home);


        Next.setOnClickListener(this);
        Prev.setOnClickListener(this);
        share.setOnClickListener(this);
        copy.setOnClickListener(this);
        home.setOnClickListener(this);

        mydbhelper2 = new DataBaseHelper2(getActivity());
        list_fav = mydbhelper2.getAllFavourite();
        textView.setText(list_fav.get(counter));
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Next:
                AdRequest adRequest1 = new AdRequest.Builder().build();
                mInterstitialAd.loadAd(adRequest1);
                mInterstitialAd.setAdListener(new AdListener() {
                    public void onAdLoaded() {
                        showInterstitial();
                    }
                });
                mPreviousQuoteTitle = textView.getText().toString();
                list_fav.add(textView.getText().toString());
                counter++;
                if (counter >= list_fav.size())
                    counter = 0;
                textView.setText(list_fav.get(counter));


                break;
            case R.id.Prev:
                try {
                   int  size = list_fav.size();
                    //  Log.d(name, "Previous Quotes Size: " + size);
                    if (size != 0) {
                        mPreviousQuoteTitle = list_fav.get(size - 1);
                        textView.setText(mPreviousQuoteTitle);
                        list_fav.remove(size - 1);
                        size--;
                        if (size == 0) {
                            // textView.setEnabled(false);
                            Prev.setVisibility(View.VISIBLE);
                        }
                    } else {
                        //  mPreviousButton.setEnabled(false);
                        Prev.setVisibility(View.VISIBLE);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
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
                break;
            case R.id.home:
                HomeFragment homeFragment=new HomeFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, homeFragment, "gift");
                fragmentTransaction.commit();


        }
    }



        private void showInterstitial() {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }
        }

    }









