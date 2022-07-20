package com.appybuilder.segui09516.VCS_Troya;

import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class SettingsFragment extends Fragment {

    private View view;
    private DatabaseReference myDatabaseReference;

    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";

    private static final int MODO_HORA_PARTIDO = 1;
    private static final int MODO_HORA_ENTRENAMIENTO = 2;

    public static final int MODO_RESULTADO_HOME = 1;
    public static final int MODO_RESULTADO_JORNADA = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        myDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Troya");

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Configuración");

        Button crear = view.findViewById(R.id.crearEquiposBTN);
        Button save = view.findViewById(R.id.guardarDATA);
        Button read = view.findViewById(R.id.leerDATA);

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTeams();
                createMatches();
                createPlayers();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readData();
            }
        });

        if (((MainActivity) getActivity()).isAdmin_logged()) {
            crear.setEnabled(true);
            save.setEnabled(true);
            read.setEnabled(true);
        } else {
            crear.setEnabled(false);
            save.setEnabled(false);
            read.setEnabled(false);
        }


        Button resetConvocatoria = view.findViewById(R.id.resetConvocatoriaPartido);
        Button resetEntrenamiento = view.findViewById(R.id.resetEntrenamientoPartido);
        Button enviarNotificaciones = view.findViewById(R.id.sendNotifications);
        Button cambiarHora = view.findViewById(R.id.changeHour);
        Button cambiarHoraEntreno = view.findViewById(R.id.changeHourTraining);
        Button cambiarComentario = view.findViewById(R.id.changeComment);
        Button ActualizarResultadoHome = view.findViewById(R.id.setHomeResultado);
        Button ActualizarResultadoJornada = view.findViewById(R.id.setResultadoJornada);

        resetConvocatoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetConvocatoriaPartido();
            }
        });

        resetEntrenamiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetEntrenameintoPartido();
            }
        });

        enviarNotificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotifications();
            }
        });

        cambiarHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeHour(MODO_HORA_PARTIDO);
            }
        });
        cambiarHoraEntreno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeHour(MODO_HORA_ENTRENAMIENTO);
            }
        });

        cambiarComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeComment();
            }
        });

        ActualizarResultadoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateResultado(MODO_RESULTADO_HOME);
            }
        });
        ActualizarResultadoJornada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateResultado(MODO_RESULTADO_JORNADA);
            }
        });


        return view;
    }

    private void createTeams() {
        ((MainActivity) getActivity()).getDataApp().clearTeams();

        ((MainActivity) getActivity()).getDataApp().addTeam(new Team("V.C.S. Troya", "team1.png", 41.51473296842232, 2.1132101325321173));
        ((MainActivity) getActivity()).getDataApp().addTeam(new Team("V. Turó de la Peira", "team2.png", 41.432634, 2.166692));
        ((MainActivity) getActivity()).getDataApp().addTeam(new Team("Can Buxeres", "team3.png", 41.366591, 2.095434));
        ((MainActivity) getActivity()).getDataApp().addTeam(new Team("UD. Lourdes A", "team4.png", 41.554245, 2.216818));
        ((MainActivity) getActivity()).getDataApp().addTeam(new Team("C.F. Damm", "team5.png", 41.442191, 2.170314));
        ((MainActivity) getActivity()).getDataApp().addTeam(new Team("V. Gladiador", "team6.png", 41.412973, 2.214562));
        ((MainActivity) getActivity()).getDataApp().addTeam(new Team("V. Lliçà de Vall", "team7.png", 41.588231, 2.237750));
        ((MainActivity) getActivity()).getDataApp().addTeam(new Team("UD. Andalucía", "team8.png", 41.519568, 2.130095));
        ((MainActivity) getActivity()).getDataApp().addTeam(new Team("Gornal Derbi", "team9.png", 41.357842, 2.117560));
        ((MainActivity) getActivity()).getDataApp().addTeam(new Team("V. Alzamora", "team10.png", 41.438204, 2.183895));
        ((MainActivity) getActivity()).getDataApp().addTeam(new Team("Esc. Ripollet", "team11.png", 41.499542, 2.149221));
        ((MainActivity) getActivity()).getDataApp().addTeam(new Team("UD. Lourdes B", "team12.png", 41.554245, 2.216818));
        ((MainActivity) getActivity()).getDataApp().addTeam(new Team("La Creueta", "team13.png", 41.507351, 2.194417));
        ((MainActivity) getActivity()).getDataApp().addTeam(new Team("La Celeste", "team14.png", 41.463363, 2.186299));
        ((MainActivity) getActivity()).getDataApp().addTeam(new Team("U. Bellvitge", "team15.png", 41.346803, 2.101849));
        ((MainActivity) getActivity()).getDataApp().addTeam(new Team("V. Singuerlín", "team16.png", 41.454416, 2.200734));
    }

    private void createMatches() {
        ((MainActivity) getActivity()).getDataApp().clearMatches();

        DataApp actual = ((MainActivity) getActivity()).getDataApp();

        actual.addMatch(new Match(1, "Domingo 15 de Septiembre del 2019", "15/09/2019", actual.getTeams().get(0), actual.getTeams().get(1)));
        actual.addMatch(new Match(2, "Domingo 22 de Septiembre del 2019", "22/09/2019", actual.getTeams().get(2), actual.getTeams().get(0)));
        actual.addMatch(new Match(3, "Domingo 29 de Septiembre del 2019", "29/09/2019", actual.getTeams().get(0), actual.getTeams().get(3)));
        actual.addMatch(new Match(4, "Domingo 6 de Octubre del 2019", "6/10/2019", actual.getTeams().get(4), actual.getTeams().get(0)));
        actual.addMatch(new Match(5, "Domingo 13 de Octubre del 2019", "13/10/2019", actual.getTeams().get(0), actual.getTeams().get(5)));
        actual.addMatch(new Match(6, "Domingo 20 de Octubre del 2019", "20/10/2019", actual.getTeams().get(6), actual.getTeams().get(0)));
        actual.addMatch(new Match(7, "Domingo 27 de Octubre del 2019", "27/10/2019", actual.getTeams().get(0), actual.getTeams().get(7)));
        actual.addMatch(new Match(8, "Domingo 3 de Noviembre del 2019", "3/11/2019", actual.getTeams().get(8), actual.getTeams().get(0)));
        actual.addMatch(new Match(9, "Domingo 10 de Noviembre del 2019", "10/11/2019", actual.getTeams().get(0), actual.getTeams().get(9)));
        actual.addMatch(new Match(10, "Domingo 17 de Noviembre del 2019", "17/11/2019", actual.getTeams().get(10), actual.getTeams().get(0)));
        actual.addMatch(new Match(11, "Domingo 24 de Noviembre del 2019", "24/11/2019", actual.getTeams().get(0), actual.getTeams().get(11)));
        actual.addMatch(new Match(12, "Domingo 1 de Diciembre del 2019", "1/12/2019", actual.getTeams().get(12), actual.getTeams().get(0)));
        actual.addMatch(new Match(13, "Domingo 15 de Diciembre del 2019", "15/12/2019", actual.getTeams().get(0), actual.getTeams().get(13)));
        actual.addMatch(new Match(14, "Domingo 22 de Diciembre del 2019", "22/12/2019", actual.getTeams().get(14), actual.getTeams().get(0)));

        actual.addMatch(new Match(15, "Domingo 12 de Enero del 2020", "12/1/2020", actual.getTeams().get(0), actual.getTeams().get(15)));
        actual.addMatch(new Match(16, "Domingo 19 de Enero del 2020", "19/1/2020", actual.getTeams().get(1), actual.getTeams().get(0)));
        actual.addMatch(new Match(17, "Domingo 26 de Enero del 2020", "26/1/2020", actual.getTeams().get(0), actual.getTeams().get(2)));
        actual.addMatch(new Match(18, "Domingo 2 de Febrero del 2020", "2/2/2020", actual.getTeams().get(3), actual.getTeams().get(0)));
        actual.addMatch(new Match(19, "Domingo 9 de Febrero del 2020", "9/2/2020", actual.getTeams().get(0), actual.getTeams().get(4)));
        actual.addMatch(new Match(20, "Domingo 16 de Febrero del 2020", "16/2/2020", actual.getTeams().get(5), actual.getTeams().get(0)));
        actual.addMatch(new Match(21, "Domingo 1 de Marzo del 2020", "1/3/2020", actual.getTeams().get(0), actual.getTeams().get(6)));
        actual.addMatch(new Match(22, "Domingo 8 de Marzo del 2020", "8/3/2020", actual.getTeams().get(7), actual.getTeams().get(0)));


        actual.addMatch(new Match(29, "Domingo 3 de Mayo del 2020", "3/5/2020", actual.getTeams().get(0), actual.getTeams().get(14)));
        actual.addMatch(new Match(30, "Domingo 10 de Mayo del 2020", "10/5/2020", actual.getTeams().get(15), actual.getTeams().get(0)));

        actual.addMatch(new Match(23, "Domingo 17 de Mayo del 2020", "17/5/2020", actual.getTeams().get(0), actual.getTeams().get(8)));
        actual.addMatch(new Match(24, "Domingo 24 de Mayo del 2020", "24/5/2020", actual.getTeams().get(9), actual.getTeams().get(0)));
        actual.addMatch(new Match(25, "Domingo 31 de Mayo del 2020", "31/5/2020", actual.getTeams().get(0), actual.getTeams().get(10)));
        actual.addMatch(new Match(26, "Domingo 7 de Junio del 2020", "7/6/2020", actual.getTeams().get(11), actual.getTeams().get(0)));
        actual.addMatch(new Match(27, "Domingo 14 de Junio del 2020", "14/6/2020", actual.getTeams().get(0), actual.getTeams().get(12)));
        actual.addMatch(new Match(28, "Domingo 21 de Junio del 2020", "21/6/2020", actual.getTeams().get(13), actual.getTeams().get(0)));


        ((MainActivity) getActivity()).setDataApp(actual);
    }

    private void createPlayers() {
        ((MainActivity) getActivity()).getDataApp().clearPlayers();

        ((MainActivity) getActivity()).getDataApp().addPlayer(new Player("David", 25, ""));
        ((MainActivity) getActivity()).getDataApp().addPlayer(new Player("Manu", 2, ""));
        ((MainActivity) getActivity()).getDataApp().addPlayer(new Player("Rafa", 3, ""));
        ((MainActivity) getActivity()).getDataApp().addPlayer(new Player("Juanito", 4, ""));
        ((MainActivity) getActivity()).getDataApp().addPlayer(new Player("Ziny", 5, ""));
        ((MainActivity) getActivity()).getDataApp().addPlayer(new Player("Reyes", 6, ""));
        ((MainActivity) getActivity()).getDataApp().addPlayer(new Player("R. Campos", 8, ""));
        ((MainActivity) getActivity()).getDataApp().addPlayer(new Player("Manolo", 9, ""));
        ((MainActivity) getActivity()).getDataApp().addPlayer(new Player("Lobito", 10, ""));
        ((MainActivity) getActivity()).getDataApp().addPlayer(new Player("Jorge Mario", 11, ""));
        ((MainActivity) getActivity()).getDataApp().addPlayer(new Player("Utrilla", 12, ""));
        ((MainActivity) getActivity()).getDataApp().addPlayer(new Player("Peque", 13, ""));
        ((MainActivity) getActivity()).getDataApp().addPlayer(new Player("Rodo", 14, ""));
        ((MainActivity) getActivity()).getDataApp().addPlayer(new Player("Dani N.", 15, ""));
        ((MainActivity) getActivity()).getDataApp().addPlayer(new Player("Sebas Br.", 16, ""));
        ((MainActivity) getActivity()).getDataApp().addPlayer(new Player("Álex", 17, ""));
        ((MainActivity) getActivity()).getDataApp().addPlayer(new Player("Carreto", 18, ""));
        ((MainActivity) getActivity()).getDataApp().addPlayer(new Player("Isaac", 19, ""));
        ((MainActivity) getActivity()).getDataApp().addPlayer(new Player("Benji", 20, ""));
        ((MainActivity) getActivity()).getDataApp().addPlayer(new Player("Paco", 21, ""));
        ((MainActivity) getActivity()).getDataApp().addPlayer(new Player("David N.", 22, ""));
        ((MainActivity) getActivity()).getDataApp().addPlayer(new Player("Joaquín Z.", 23, ""));
        ((MainActivity) getActivity()).getDataApp().addPlayer(new Player("Vargas", 24, ""));
        ((MainActivity) getActivity()).getDataApp().addPlayer(new Player("Misas", 0, ""));
    }

    private void saveData() {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://score-1007.appspot.com/").child("datos.dat");

        try {
            File f = File.createTempFile("datos", "dat");
            FileOutputStream file = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(file);
            oos.writeObject(((MainActivity) getActivity()).getDataApp());
            oos.close();
            UploadTask uploadTask = storageReference.putFile(Uri.fromFile(f));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void readData() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://score-1007.appspot.com/").child("datos.dat");

        try {
            final File f = File.createTempFile("datos", "dat");
            storageReference.getFile(f).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    try {
                        FileInputStream file = new FileInputStream(f);
                        ObjectInputStream ois = new ObjectInputStream(file);

                        ((MainActivity) getActivity()).setDataApp((DataApp) ois.readObject());

                        ois.close();

                        Toasty.success(getContext(), "READ DATA SUCCESSFULLY").show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void resetConvocatoriaPartido() {
        DatabaseReference myRefConvocatoria = myDatabaseReference.child("ConvocatoriaPartido");

        for (Player p : ((MainActivity) getActivity()).getDataApp().getPlayers()) {
            myRefConvocatoria.child(Integer.toString(p.getNum_player())).setValue("d");
        }

        Toasty.success(getContext(),"Convocatoria reseteada").show();

    }

    private void resetEntrenameintoPartido() {
        DatabaseReference myRefEntrenamiento = myDatabaseReference.child("EntrenamientoPartido");

        for (Player p : ((MainActivity) getActivity()).getDataApp().getPlayers()) {
            myRefEntrenamiento.child(Integer.toString(p.getNum_player())).setValue("d");
        }

        Toasty.success(getContext(),"Entrenamiento reseteado").show();


    }

    private void sendNotifications() {
        CustomNotificationsDialog customNotificationsDialog = new CustomNotificationsDialog();
        customNotificationsDialog.show(getActivity().getSupportFragmentManager(), "notifications_dialog");
    }

    private void changeHour(final int id) {
        //Calendario para obtener fecha & hora
        final Calendar c = Calendar.getInstance();

        //Variables para obtener la hora hora
        final int hora = c.get(Calendar.HOUR_OF_DAY);
        final int minuto = c.get(Calendar.MINUTE);

        TimePickerDialog recogerHora = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada = (hourOfDay < 10) ? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10) ? String.valueOf(CERO + minute) : String.valueOf(minute);

                //Muestro la hora con el formato deseado
                String time = horaFormateada + DOS_PUNTOS + minutoFormateado + "h";

                if (id == MODO_HORA_PARTIDO) {
                    DatabaseReference myRefTime = myDatabaseReference.child("Hora");
                    myRefTime.setValue(time);
                } else if (id == MODO_HORA_ENTRENAMIENTO) {
                    DatabaseReference myRefTimeTraining = myDatabaseReference.child("HoraEntreno");
                    myRefTimeTraining.setValue(time);
                }

                Toasty.success(getContext(), "Hora cambiada con éxito!").show();

            }

        }, hora, minuto, true);

        recogerHora.show();
    }

    private void changeComment() {
        CustomCommentDialog customCommentDialog = new CustomCommentDialog();
        customCommentDialog.show(getActivity().getSupportFragmentManager(), "comment_dialog");
    }

    private void updateResultado(final int id) {

        CustomResultadoDialog customResultadoDialog = new CustomResultadoDialog();
        Bundle b = new Bundle();
        b.putInt("option", id);

        customResultadoDialog.setArguments(b);
        customResultadoDialog.show(getActivity().getSupportFragmentManager(), "resultado_dialog");
    }


}
