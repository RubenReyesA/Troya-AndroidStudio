package com.appybuilder.segui09516.VCS_Troya;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import es.dmoral.toasty.Toasty;

public class CustomResultadoDialog extends DialogFragment {

    private DatabaseReference myDatabaseReference;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        myDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Troya");

        View view = getActivity().getLayoutInflater().inflate(R.layout.custom_resultado_layout, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Establecer Resultado");
        builder.setView(view);

        final EditText localSCORE = view.findViewById(R.id.localSCORE);
        final EditText visitanteSCORE = view.findViewById(R.id.visitanteSCORE);

        builder.setNegativeButton("Cancelar", null);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String resultado = localSCORE.getText().toString() + "-" + visitanteSCORE.getText().toString();

                Bundle b = getArguments();

                int option = b.getInt("option");

                if (!localSCORE.toString().equals("") && (!visitanteSCORE.toString().equals(""))) {

                    if (option == SettingsFragment.MODO_RESULTADO_HOME) {
                        Calendar calendar1 = new GregorianCalendar();

                        final int day = calendar1.get(Calendar.DAY_OF_WEEK);

                        if (day == Calendar.SUNDAY){
                            DatabaseReference myRefResultado = myDatabaseReference.child("Resultado");
                            myRefResultado.setValue(resultado);

                            Toasty.success(getContext(), "Resultado establecido con éxito!").show();
                        }
                        else{
                            Toasty.error(getContext(), "No se puede modificar el resultado ahora mismo..").show();
                        }


                    } else if (option == SettingsFragment.MODO_RESULTADO_JORNADA) {

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
                            int nJornada = ((MainActivity) getActivity()).getDataApp().getMatches().get(j).getN_Jornada();
                            DatabaseReference myRefResultadoJornada = myDatabaseReference.child("ResultadosPartidos")
                                    .child(Integer.toString(nJornada));

                            myRefResultadoJornada.setValue(resultado);
                            Toasty.success(getContext(), "Resultado establecido con éxito!").show();
                        }
                        else{
                            Toasty.error(getContext(),"No se puede establecer un resultado en esta jornada");
                        }
                    }

                } else {
                    Toasty.error(getContext(), "No se puede guardar un resultado vacío").show();
                }
            }
        });


        return builder.create();
    }
}
