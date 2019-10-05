package com.example.main.myapplication9.fragment;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.main.myapplication9.R;
import com.example.main.myapplication9.activity.DataBaseHelper;
import com.example.main.myapplication9.activity.DataBaseHelper2;
import com.example.main.myapplication9.activity.MyAdapter;

import java.io.IOException;
import java.util.ArrayList;

public class TabThreeFragment extends Fragment {
   public static ArrayList<String> list_fav = new ArrayList<>();
    Button delete;

    CardView cardView;
    String m;

    int i;
    ArrayList<String> r;
    DataBaseHelper2 mydbhelper2;
    Cursor c;
    AlertDialog alertDialog;

   public static RecyclerView recyclerView;
    int position ;

    public TabThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tabthree, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.card_recycler_view);
        mydbhelper2 = new DataBaseHelper2(getActivity());
        list_fav = mydbhelper2.getAllFavourite();
        initViews();

        return rootView;
    }

    public  void initViews() {

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        final RecyclerView.Adapter adapter = new MyAdapter(list_fav);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(final RecyclerView rv, MotionEvent e) {
                final View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {

                    position = rv.getChildAdapterPosition(child);
                    m = list_fav.get(position).toString();
                    final CharSequence items[] = new CharSequence[]{"Open", "Copy", "Share", "Delete"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    // builder.setTitle("Perform any Action :");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (items[which] == "Open") {
                             FavDisplayFragment favDisplayFragment=new FavDisplayFragment();
                                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.frame, favDisplayFragment, "gift");
                                fragmentTransaction.commit();

                            }
                            if (items[which] == "Delete") {
                                mydbhelper2.delete(list_fav.get(position).toString());
                                list_fav.remove(position);
                                adapter.notifyItemRemoved(position);
                            }
                            if (items[which] == "Share") {
                                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                String shareBody = list_fav.get(position).toString();
                                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, " हिन्दी चुटकुले");
                                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,shareBody);
                                startActivity(sharingIntent);
                            }
                            if (items[which] == "Copy") {
                                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("jokes",list_fav.get(position).toString());
                                clipboard.setPrimaryClip(clip);
                                Toast.makeText(getActivity(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                    builder.show();

                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {


            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }

        });


    }

}