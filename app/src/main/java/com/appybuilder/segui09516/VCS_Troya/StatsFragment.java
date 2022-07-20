package com.appybuilder.segui09516.VCS_Troya;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StatsFragment extends Fragment {

    private View view;
    private DatabaseReference myDatabaseReference;
    private DatabaseReference myRefGoles;
    private DatabaseReference myRefAmarillas;
    private DatabaseReference myRefRojas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stats, container, false);
        myDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Troya");
        myRefGoles = myDatabaseReference.child("Goles");
        myRefAmarillas = myDatabaseReference.child("Amarillas");
        myRefRojas = myDatabaseReference.child("Rojas");

        String title = "Estadísticas";
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);

        LinearLayout LY_Jugador = view.findViewById(R.id.LY_Jugador);
        LinearLayout LY_Goles = view.findViewById(R.id.LY_Goles);
        LinearLayout LY_Amarillas = view.findViewById(R.id.LY_Amarillas);
        LinearLayout LY_Rojas = view.findViewById(R.id.LY_Rojas);

        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (7 * scale + 0.5f);

        final int size = ((MainActivity) getActivity()).getDataApp().getPlayers().size();

        final TextView[] tv = new TextView[size];
        final TextView[] tv2 = new TextView[size];
        final TextView[] tv3 = new TextView[size];
        final TextView[] tv4 = new TextView[size];

        int j = 0;

        for (Player p : ((MainActivity) getActivity()).getDataApp().getPlayers()) {

            tv[j] = new TextView(getActivity().getApplicationContext());
            tv[j].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv[j].setText(p.getName_player());
            tv[j].setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
            tv[j].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv[j].setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

            tv2[j] = new TextView(getActivity().getApplicationContext());
            tv2[j].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv2[j].setText("-");
            tv2[j].setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
            tv2[j].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv2[j].setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

            tv3[j] = new TextView(getActivity().getApplicationContext());
            tv3[j].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv3[j].setText("-");
            tv3[j].setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
            tv3[j].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv3[j].setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

            tv4[j] = new TextView(getActivity().getApplicationContext());
            tv4[j].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv4[j].setText("-");
            tv4[j].setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
            tv4[j].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv4[j].setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

            LY_Jugador.addView(tv[j]);
            LY_Goles.addView(tv2[j]);
            LY_Amarillas.addView(tv3[j]);
            LY_Rojas.addView(tv4[j]);

            j++;
        }

        final TextView update_text = view.findViewById(R.id.StatsUpdateText);

        for (int i = 0; i < size; i++) {
            myRefGoles = myRefGoles.child(Integer.toString(i));
            myRefAmarillas = myRefAmarillas.child(Integer.toString(i));
            myRefRojas = myRefRojas.child(Integer.toString(i));

            final int finalI = i;
            myRefGoles.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    tv2[finalI].setText((String) dataSnapshot.getValue());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            myRefAmarillas.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    tv3[finalI].setText((String) dataSnapshot.getValue());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            myRefRojas.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    tv4[finalI].setText((String) dataSnapshot.getValue());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            myRefGoles = myRefGoles.getParent();
            myRefAmarillas = myRefAmarillas.getParent();
            myRefRojas = myRefRojas.getParent();

        }


        Date date = new Date();
        SimpleDateFormat sp = new SimpleDateFormat("HH:mm:ss");
        update_text.setText("Actualizado a las " + sp.format(date));

        return view;
    }


}
