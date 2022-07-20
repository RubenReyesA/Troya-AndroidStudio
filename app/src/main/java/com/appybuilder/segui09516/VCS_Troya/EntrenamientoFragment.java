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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class EntrenamientoFragment extends Fragment {

    private View view;
    private DatabaseReference myDatabaseReference;
    private DatabaseReference myRefEntrenamiento;
    private DatabaseReference myRefTimeTraining;
    private Fragment f = this;

    private Spinner spinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_entrenamiento, container, false);

        String title = "Entrenamiento";
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);

        myDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Troya");
        myRefEntrenamiento = myDatabaseReference.child("EntrenamientoPartido");
        myRefTimeTraining = myDatabaseReference.child("HoraEntreno");

        final ArrayList<String> keys = new ArrayList<>();
        final ArrayList<String> values = new ArrayList<>();

        TextView dia = view.findViewById(R.id.EntrenoDia);
        final TextView hora = view.findViewById(R.id.EntrenoHora);

        dia.setText(sumarRestarDiasFechahastaTraining());

        myRefTimeTraining.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hora.setText((String) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRefEntrenamiento.addListenerForSingleValueEvent(new ValueEventListener() {
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


        spinner = view.findViewById(R.id.spinnerEntreno);

        ArrayList<String> players = ((MainActivity)getActivity()).getDataApp().getPlayersNames();

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_dropdown_item, players);

        spinner.setAdapter(spinnerArrayAdapter);


        Button confirmar = view.findViewById(R.id.btnEntreno);

        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });


        return view;
    }

    public String sumarRestarDiasFechahastaTraining(){

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date()); // Configuramos la fecha que se recibe

        while(calendar.get(Calendar.DAY_OF_WEEK) != Calendar.WEDNESDAY){
            calendar.add(Calendar.DAY_OF_YEAR, 1);  // numero de días a añadir, o restar en caso de días<0
        }


        DateFormat dateFormat = new SimpleDateFormat("EEEE, d 'de' MMMM 'del' yyyy",new Locale("es"));

        String fecha = dateFormat.format(calendar.getTime());
        return fecha.toUpperCase().charAt(0) + fecha.substring(1,fecha.length()); // Devuelve el objeto String con los nuevos días añadidos

    }

    private void openDialog(){
        CustomEntrenoPassDialog customEntrenoPassDialog = new CustomEntrenoPassDialog();
        Bundle b = new Bundle();
        b.putString("Name", spinner.getSelectedItem().toString());
        customEntrenoPassDialog.setArguments(b);
        customEntrenoPassDialog.setTargetFragment(this, 999);
        customEntrenoPassDialog.show(getActivity().getSupportFragmentManager(),"pass_dialog");
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
