package com.appybuilder.segui09516.VCS_Troya;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ConvocatoriaFragment extends Fragment {

    private View view;
    private DatabaseReference myDatabaseReference;
    private DatabaseReference myRefConvocatoria;
    private DatabaseReference myRefTime;
    private Fragment f = this;

    private Spinner spinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_convocatoria, container, false);

        String title = "Convocatoria";
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);

        myDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Troya");
        myRefConvocatoria = myDatabaseReference.child("ConvocatoriaPartido");
        myRefTime = myDatabaseReference.child("Hora");

        final ArrayList<String> keys = new ArrayList<>();
        final ArrayList<String> values = new ArrayList<>();

        TextView dia = view.findViewById(R.id.ConvocatoriaDia);
        final TextView hora = view.findViewById(R.id.ConvocatoriaHora);
        TextView partido = view.findViewById(R.id.ConvocatoriaPartido);

        myRefTime.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hora.setText((String) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dia.setText(getDay());
        partido.setText(getMatch());

        myRefConvocatoria.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                keys.clear();
                values.clear();


                for (DataSnapshot item_snapshot : dataSnapshot.getChildren()) {
                    keys.add(item_snapshot.getKey().toString());
                    values.add(item_snapshot.getValue().toString());

                }

                ArrayList<Player> players = ((MainActivity) getActivity()).getDataApp().getPlayers();

                TextView[] tx = new TextView[players.size()];

                int l_si = 0, l_no = 0, l_duda = 0;

                LinearLayout duda = view.findViewById(R.id.LY_partido_DUDA);
                LinearLayout yes = view.findViewById(R.id.LY_partido_SI);
                LinearLayout no = view.findViewById(R.id.LY_partido_NO);

                if(duda.getChildCount()>1) {
                    duda.removeViews(1,duda.getChildCount()-1);
                }
                if(yes.getChildCount()>1) {
                    yes.removeViews(1,yes.getChildCount()-1);
                }
                if(no.getChildCount()>1) {
                    no.removeViews(1,no.getChildCount()-1);
                }

                float scale = getResources().getDisplayMetrics().density;
                int dpAsPixels = (int) (7 * scale + 0.5f);

                for (int i = 0; i < players.size(); i++) {

                    tx[i] = new TextView(getActivity().getApplicationContext());
                    tx[i] = new TextView(getActivity().getApplicationContext());
                    tx[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    tx[i].setText(players.get(i).getName_player());
                    tx[i].setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
                    tx[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    tx[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

                    int id = players.get(i).getNum_player();

                    if (values.get(id).equals("d")) {
                        duda.addView(tx[i]);
                        l_duda++;

                    } else if (values.get(id).equals("y")) {
                        yes.addView(tx[i]);
                        l_si++;

                    } else if (values.get(id).equals("n")) {
                        no.addView(tx[i]);
                        l_no++;
                    }
                }

                TextView labelSI = view.findViewById(R.id.label_partido_YES);
                TextView labelNO = view.findViewById(R.id.label_partido_NO);
                TextView labelDUDA = view.findViewById(R.id.label_partido_DUDA);

                labelSI.setText("VOY - " + Integer.toString(l_si) +" -");
                labelNO.setText("NO VOY - " + Integer.toString(l_no) +" -");
                labelDUDA.setText("DUDA - " + Integer.toString(l_duda) +" -");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        spinner = view.findViewById(R.id.spinnerConvocatoria);

        ArrayList<String> players = ((MainActivity)getActivity()).getDataApp().getPlayersNames();

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_dropdown_item, players);

        spinner.setAdapter(spinnerArrayAdapter);

        Button confirmar = view.findViewById(R.id.btnConvocatoria);

        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });


        return view;
    }

    private String getDay() {

        String return_day = "";

        Date date = new Date();
        final Calendar calendar = new GregorianCalendar();
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
            return_day=matches.get(j).getDay();
        }
        else{
            return_day="No hay partido!";
        }

        return return_day;
    }


    private String getMatch(){
        String return_match = "";

        Date date = new Date();
        final Calendar calendar = new GregorianCalendar();
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
            return_match=matches.get(j).getLocalTeam().getNameTeam() +" -vs- "
                    + matches.get(j).getVisitanteTeam().getNameTeam();
        }
        else{
            return_match="... -vs- ...";
        }

        return return_match;
    }


    private void openDialog(){
        CustomConvocatoriaPassDialog customConvocatoriaPassDialog = new CustomConvocatoriaPassDialog();
        Bundle b = new Bundle();
        b.putString("Name", spinner.getSelectedItem().toString());
        customConvocatoriaPassDialog.setArguments(b);
        customConvocatoriaPassDialog.setTargetFragment(this, 999);
        customConvocatoriaPassDialog.show(getActivity().getSupportFragmentManager(),"pass_dialog");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 999 &&
                resultCode == 999) {

            Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.detach(currentFragment);
            fragmentTransaction.attach(currentFragment);
            fragmentTransaction.commit();

        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
