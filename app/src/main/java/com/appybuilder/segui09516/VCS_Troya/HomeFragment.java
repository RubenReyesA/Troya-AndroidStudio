package com.appybuilder.segui09516.VCS_Troya;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import es.dmoral.toasty.Toasty;

public class HomeFragment extends Fragment {

    private View view;
    private DatabaseReference myDatabaseReference;
    private DatabaseReference myRef;
    private Team localTeam = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        setHasOptionsMenu(true);

        myDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Troya");

        String title = "⚽" + " V.C.S. TROYA " + "⚽";
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);

        Date date = new Date();
        final Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        int actual_week = calendar.get(Calendar.WEEK_OF_YEAR);

        setTrainingData();

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
            final DatabaseReference myRef2 = myDatabaseReference.child("Resultado");

            final String[] resultado = {"null"};

            myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    resultado[0] = (String) dataSnapshot.getValue();

                    try {
                        Calendar calendar1 = new GregorianCalendar();
                        int week_day = calendar1.get(Calendar.DAY_OF_WEEK);
                        if (week_day != Calendar.SUNDAY && !resultado[0].equals("null")) {
                            myRef2.setValue("null");
                            resetConvocatoriaPartido();
                        }

                        if (resultado[0].equals("null")) {

                            LinearLayout layout = view.findViewById(R.id.score_home_ly);
                            layout.setVisibility(View.VISIBLE);

                            TextView next_match = view.findViewById(R.id.txt_next_match);
                            next_match.setText("Próximo Partido");
                            next_match.setVisibility(View.VISIBLE);

                            TextView match_day = view.findViewById(R.id.txt_match_day);
                            match_day.setText(matches.get(j).getDay());
                            match_day.setVisibility(View.VISIBLE);


                            myRef = myDatabaseReference.child("Hora");

                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    TextView match_time = view.findViewById(R.id.txt_match_time);
                                    match_time.setText((String) dataSnapshot.getValue());
                                    match_time.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            ImageView local_team_img = view.findViewById(R.id.img_local_team);
                            InputStream stream = getActivity().getAssets().open(matches.get(j).getLocalTeam().getFileNameTeam());
                            local_team_img.setImageDrawable(Drawable.createFromStream(stream, null));
                            local_team_img.setVisibility(View.VISIBLE);

                            localTeam = matches.get(j).getLocalTeam();

                            ImageView visitante_team_img = view.findViewById(R.id.img_visitante_team);
                            stream = getActivity().getAssets().open(matches.get(j).getVisitanteTeam().getFileNameTeam());
                            visitante_team_img.setImageDrawable(Drawable.createFromStream(stream, null));
                            visitante_team_img.setVisibility(View.VISIBLE);

                            TextView local_team_text = view.findViewById(R.id.txt_local_team);
                            local_team_text.setText(matches.get(j).getLocalTeam().getNameTeam());
                            local_team_text.setVisibility(View.VISIBLE);

                            TextView visitante_team_text = view.findViewById(R.id.txt_visitante_team);
                            visitante_team_text.setText(matches.get(j).getVisitanteTeam().getNameTeam());
                            visitante_team_text.setVisibility(View.VISIBLE);


                            view.findViewById(R.id.img_VS).setVisibility(View.VISIBLE);
                            view.findViewById(R.id.img_dash_teams_score).setVisibility(View.GONE);

                            view.findViewById(R.id.space_home1).setVisibility(View.GONE);
                            view.findViewById(R.id.space_home2).setVisibility(View.VISIBLE);
                            view.findViewById(R.id.space_home3).setVisibility(View.VISIBLE);
                            view.findViewById(R.id.space_home4).setVisibility(View.VISIBLE);
                            view.findViewById(R.id.space_home5).setVisibility(View.VISIBLE);

                            view.findViewById(R.id.img_local_score).setVisibility(View.GONE);
                            view.findViewById(R.id.img_visitante_score).setVisibility(View.GONE);

                            myRef = myDatabaseReference.child("Comentario");

                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    TextView txt_comment = view.findViewById(R.id.txt_match_comment);
                                    txt_comment.setText((String) dataSnapshot.getValue());
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        } else {
                            LinearLayout layout = view.findViewById(R.id.score_home_ly);
                            layout.setVisibility(View.VISIBLE);

                            TextView next_match = view.findViewById(R.id.txt_next_match);
                            next_match.setText("Resultado del Partido");
                            next_match.setVisibility(View.VISIBLE);

                            TextView match_day = view.findViewById(R.id.txt_match_day);
                            match_day.setText(matches.get(j).getDay());
                            match_day.setVisibility(View.VISIBLE);

                            myRef = myDatabaseReference.child("Hora");

                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    TextView match_time = view.findViewById(R.id.txt_match_time);
                                    match_time.setText((String) dataSnapshot.getValue());
                                    match_time.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            ImageView local_team_img = view.findViewById(R.id.img_local_team);
                            InputStream stream = getActivity().getAssets().open(matches.get(j).getLocalTeam().getFileNameTeam());
                            local_team_img.setImageDrawable(Drawable.createFromStream(stream, null));
                            local_team_img.setVisibility(View.VISIBLE);

                            localTeam = matches.get(j).getLocalTeam();

                            ImageView visitante_team_img = view.findViewById(R.id.img_visitante_team);
                            stream = getActivity().getAssets().open(matches.get(j).getVisitanteTeam().getFileNameTeam());
                            visitante_team_img.setImageDrawable(Drawable.createFromStream(stream, null));
                            visitante_team_img.setVisibility(View.VISIBLE);

                            TextView local_team_text = view.findViewById(R.id.txt_local_team);
                            local_team_text.setText(matches.get(j).getLocalTeam().getNameTeam());
                            local_team_text.setVisibility(View.VISIBLE);

                            TextView visitante_team_text = view.findViewById(R.id.txt_visitante_team);
                            visitante_team_text.setText(matches.get(j).getVisitanteTeam().getNameTeam());
                            visitante_team_text.setVisibility(View.VISIBLE);

                            String s = resultado[0];
                            String[] s_array = {null};
                            s_array = s.split("-");

                            ImageView local_team_score_img = view.findViewById(R.id.img_local_score);
                            stream = getActivity().getAssets().open("n" + s_array[0] + ".png");
                            local_team_score_img.setImageDrawable(Drawable.createFromStream(stream, null));
                            local_team_score_img.setVisibility(View.VISIBLE);

                            ImageView visitante_team_score_img = view.findViewById(R.id.img_visitante_score);
                            stream = getActivity().getAssets().open("n" + s_array[1] + ".png");
                            visitante_team_score_img.setImageDrawable(Drawable.createFromStream(stream, null));
                            visitante_team_score_img.setVisibility(View.VISIBLE);

                            view.findViewById(R.id.img_dash_teams_score).setVisibility(View.VISIBLE);

                            view.findViewById(R.id.img_VS).setVisibility(View.GONE);

                            view.findViewById(R.id.space_home1).setVisibility(View.VISIBLE);
                            view.findViewById(R.id.space_home2).setVisibility(View.VISIBLE);
                            view.findViewById(R.id.space_home3).setVisibility(View.VISIBLE);
                            view.findViewById(R.id.space_home4).setVisibility(View.VISIBLE);
                            view.findViewById(R.id.space_home5).setVisibility(View.GONE);

                            myRef = myDatabaseReference.child("Comentario");

                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    TextView txt_comment = view.findViewById(R.id.txt_match_comment);
                                    txt_comment.setText((String) dataSnapshot.getValue());
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else {
            try {
                TextView next_match = view.findViewById(R.id.txt_next_match);
                next_match.setText("Esta semana no hay partido");
                next_match.setVisibility(View.VISIBLE);

                ImageView no_match = view.findViewById(R.id.img_no_match);
                InputStream stream = getActivity().getAssets().open("team0.png");

                no_match.setImageDrawable(Drawable.createFromStream(stream, null));
                no_match.setVisibility(View.VISIBLE);

                myRef = myDatabaseReference.child("Comentario");

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        TextView txt_comment = view.findViewById(R.id.txt_match_comment);
                        txt_comment.setText((String) dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return view;

    }


    private void resetConvocatoriaPartido() {
        DatabaseReference myRefConvocatoria = myDatabaseReference.child("ConvocatoriaPartido");

        for (Player p : ((MainActivity) getActivity()).getDataApp().getPlayers()) {
            myRefConvocatoria.child(Integer.toString(p.getNum_player())).setValue("d");
        }

    }

    private void resetEntrenamiento() {
        DatabaseReference myRefEntrenamiento = myDatabaseReference.child("EntrenamientoPartido");

        for (Player p : ((MainActivity) getActivity()).getDataApp().getPlayers()) {
            myRefEntrenamiento.child(Integer.toString(p.getNum_player())).setValue("d");
        }

    }

    private void setTrainingData() {
        Calendar calendar1 = new GregorianCalendar();

        final int day = calendar1.get(Calendar.DAY_OF_WEEK);

        final DatabaseReference myRef4 = myDatabaseReference.child("ResultadoEntreno");

        myRef4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String data = (String) dataSnapshot.getValue();

                if (day != Calendar.WEDNESDAY && !data.equals("null")) {
                    myRef4.setValue("null");
                    resetEntrenamiento();
                } else if (day == Calendar.WEDNESDAY) {
                    myRef4.setValue("not_null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if ((item.getItemId() == R.id.mLocation) && (localTeam != null)) {

            double tLatitud = localTeam.getLatitud();
            double tLongitud = localTeam.getLongitud();
            String tName = localTeam.getNameTeam();

            String to_open = "geo:<" + Double.toString(tLatitud) + ">,<" + Double.toString(tLongitud) + ">?z=16&q=<" +
                    Double.toString(tLatitud) + ">,<" + Double.toString(tLongitud) + ">(" + tName + ")";

            Toasty.info(getContext(), tName).show();

            Uri uri = Uri.parse(to_open);
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
        else{
            Toasty.error(getContext(), "No hay partido establecido!!").show();
        }

        return true;

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
    }

}
