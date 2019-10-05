package com.example.main.myapplication9.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.main.myapplication9.R;
import com.example.main.myapplication9.activity.DataBaseHelper;
import com.example.main.myapplication9.activity.MyAdapter;

import java.util.ArrayList;

;

public class TabTwoFragment extends Fragment {
    RecyclerView recyclerView;
    int position ;
    ArrayList<String> list_fav= new ArrayList<>();

    public TabTwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tabtwo, container, false);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.rv_recycler_viewtabtwo);
        recyclerView.setHasFixedSize(true);
        list_fav.add("Insult Stupid Related Jokes");
        list_fav.add("Parents Child Related Jokes");
        list_fav.add("Santa Banta Related Jokes");
        list_fav.add("Sarabi Related Jokes");
        list_fav.add("School & College Life Related Jokes");
        list_fav.add("Teacher & Student Related Jokes");
        MyAdapter adapter = new MyAdapter(list_fav);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());

        //For Horizontal scrolling in the list
        // rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
        recyclerView.setLayoutManager(llm);
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
                    if (position == 0) {
                        // getSupportActionBar().setTitle(R.string.nav_gift);
                        DataBaseHelper.DB_NAME="InsultStupidJokesDB.db";
                        DisplayFragment displayFragment = new DisplayFragment();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, displayFragment, "gift");
                        fragmentTransaction.commit();
                    }else if(position==1){
                        DataBaseHelper.DB_NAME="ParentsChildJokesDB.db";
                        DisplayFragment displayFragment = new DisplayFragment();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, displayFragment, "gift");
                        fragmentTransaction.commit();

                    }
                    else if(position==2){
                        DataBaseHelper.DB_NAME="SantaPapuJokesDB.db";
                        DisplayFragment displayFragment = new DisplayFragment();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, displayFragment, "gift");
                        fragmentTransaction.commit();

                    }
                    else if(position==3){
                        DataBaseHelper.DB_NAME="SarabiJokesDB.db";
                        DisplayFragment displayFragment = new DisplayFragment();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, displayFragment, "gift");
                        fragmentTransaction.commit();

                    }
                    else if(position==4){
                        DataBaseHelper.DB_NAME="SchoolCollegeLifeJokesDB.db";
                        DisplayFragment displayFragment = new DisplayFragment();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, displayFragment, "gift");
                        fragmentTransaction.commit();

                    }
                    else if(position==5){
                        DataBaseHelper.DB_NAME="TeacherstudentJokesDB.db";
                        DisplayFragment displayFragment = new DisplayFragment();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, displayFragment, "gift");
                        fragmentTransaction.commit();

                    }

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




        return rootView;
    }
}