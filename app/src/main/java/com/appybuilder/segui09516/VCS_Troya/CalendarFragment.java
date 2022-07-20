package com.appybuilder.segui09516.VCS_Troya;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarFragment extends Fragment {

    private View view;
    private int nJornada;
    private DatabaseReference myDatabaseReference;
    private DatabaseReference myRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendar, container, false);

        String title = "Calendario";
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);

        myDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Troya");

        TextView txt_jornada = view.findViewById(R.id.txt_jornada);
        ImageButton btn_back = view.findViewById(R.id.btn_jornada_back);
        ImageButton btn_forward = view.findViewById(R.id.btn_jornada_forward);

        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        int actual_week = calendar.get(Calendar.WEEK_OF_YEAR);

        final ArrayList<Match> matches = ((MainActivity) getActivity()).getDataApp().getMatches();

        int i = 0;
        boolean found = false;
        while (i < matches.size() && !found) {
            try {
                String s = matches.get(i).getDate();
                Date d = new SimpleDateFormat("dd/MM/yyyy").parse(s);
                Calendar calendar2 = new GregorianCalendar();
                calendar2.setTime(d);
                int match_week = calendar2.get(Calendar.WEEK_OF_YEAR);

                if (actual_week == match_week) {
                    found = true;
                } else {
                    i++;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        final int j = i;

        if (found) {
            nJornada = ((MainActivity) getActivity()).getDataApp().getMatches().get(j).getN_Jornada();
            txt_jornada.setText("Jornada " + Integer.toString(nJornada));
            loadJornada();
        } else {
            nJornada = 15;
            txt_jornada.setText("Jornada " + Integer.toString(nJornada));
            loadJornada();
        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nJornada>1){
                    nJornada--;
                    loadJornada();
                }
            }
        });

        btn_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nJornada<30){
                    nJornada++;
                    loadJornada();
                }
            }
        });


        return view;
    }


    private void loadJornada(){
        try {
        ImageView img_jornada = view.findViewById(R.id.img_jornada);
        String filename = "j"+nJornada+".png";

        TextView txt_jornada = view.findViewById(R.id.txt_jornada);
        txt_jornada.setText("Jornada " + Integer.toString(nJornada));

        InputStream stream = getActivity().getAssets().open(filename);
        img_jornada.setImageDrawable(Drawable.createFromStream(stream, null));

        final TextView txt_resultado_jornada = view.findViewById(R.id.txt_resultado_jornada);

        myRef = myDatabaseReference.child("ResultadosPartidos");

        myRef= myRef.child(Integer.toString(nJornada));

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                txt_resultado_jornada.setText((String) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
