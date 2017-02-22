package com.example.chirag.virtualcachedesign.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chirag.virtualcachedesign.R;
import com.example.chirag.virtualcachedesign.activities.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TicketFragment extends Fragment {

    @Bind(R.id.ticketCount)
    TextView ticketCount;

    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_tickets, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        final String userID = ((MainActivity)getActivity()).userID;
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    Typeface font = Typeface.createFromAsset(context.getAssets(), "Avenir-Heavy.ttf");
                    ticketCount.setTypeface(font);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (snapshot.exists()) {
                    ticketCount.setText(snapshot.getValue().toString());
                }
                else {
                    ticketCount.setText("0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
