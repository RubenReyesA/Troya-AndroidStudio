package com.appybuilder.segui09516.VCS_Troya;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shreyaspatil.MaterialDialog.MaterialDialog;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class CustomEntrenoPassDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.custom_convocatoria_pass_layout,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Contraseña");
        builder.setMessage("La contraseña te permite apuntarte al entrenamiento!!");
        builder.setView(view);

        final EditText pass = view.findViewById(R.id.passConvocatoria);

        builder.setNegativeButton("Cancelar" , null);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Bundle b = getArguments();

                String name = b.getString("Name");

                ArrayList<Player> players = ((MainActivity)getActivity()).getDataApp().getPlayers();

                int i = 0;
                boolean found = false;
                int code = -1;

                while (i<players.size() && !found){
                    if(players.get(i).getName_player().equals(name)){
                        code= players.get(i).getNum_player();
                        found=true;
                    }
                    else{
                        i++;
                    }
                }

                if(code!=-1) {
                    if (pass.getText().toString().equals(Integer.toString(code))) {

                        final int finalCode = code;

                        MaterialDialog mDialog = new MaterialDialog.Builder(getActivity())
                                .setTitle("Marca tu asistencia al entrenamiento")
                                .setMessage("¿Vas a asistir al entreno?")
                                .setCancelable(false)
                                .setPositiveButton("Voy", R.drawable.ic_check, new MaterialDialog.OnClickListener() {
                                    @Override
                                    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                                        changePlayerStatusConvocatoria(finalCode, true);
                                        dialogInterface.dismiss();
                                    }
                                })
                                .setNegativeButton("No Voy", R.drawable.ic_close, new MaterialDialog.OnClickListener() {
                                    @Override
                                    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                                        changePlayerStatusConvocatoria(finalCode, false);
                                        dialogInterface.dismiss();
                                    }
                                })
                                .build();

                        mDialog.show();

                    } else {
                        Toasty.error(getActivity().getApplicationContext(), "Contraseña incorrecta").show();
                    }
                }

            }
        });

        return builder.create();
    }

    Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    private void changePlayerStatusConvocatoria(int code, boolean assist){

        DatabaseReference myDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Troya");
        DatabaseReference myRefEntrenamiento = myDatabaseReference.child("EntrenamientoPartido");

        if(assist){
            myRefEntrenamiento.child(Integer.toString(code)).setValue("y");
            Toasty.success(context,"Te has apuntado correctamente a la convocatoria!").show();
        }
        else{
            myRefEntrenamiento.child(Integer.toString(code)).setValue("n");
            Toasty.success(context,"Esperamos verte la próxima semana!!").show();
        }

        Intent intent = new Intent();
        getTargetFragment().onActivityResult(
                getTargetRequestCode(), 999, intent);

    }
}
