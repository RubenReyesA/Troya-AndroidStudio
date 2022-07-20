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

import es.dmoral.toasty.Toasty;

public class CustomLoginDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.custom_login_layout, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Iniciar Sesión");
        builder.setView(view);


        final EditText user = view.findViewById(R.id.user);
        final EditText pass = view.findViewById(R.id.pass);

        builder.setNegativeButton("Cancelar", null);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (user.getText().toString().equals("admin") && (pass.getText().toString().equals("troya"))) {
                    ((MainActivity) getActivity()).setIs_logged(true);
                    Toasty.success(getActivity().getApplicationContext(), "Sesión iniciada").show();
                } else if (user.getText().toString().equals("cuentas") && (pass.getText().toString().equals("misas"))) {
                    ((MainActivity) getActivity()).setIs_cuentas_logged(true);
                    Toasty.success(getActivity().getApplicationContext(), "Sesión iniciada").show();
                } else if (user.getText().toString().equals("admin") && (pass.getText().toString().equals("1007"))) {
                    ((MainActivity) getActivity()).setAdmin_logged(true);
                    Toasty.success(getActivity().getApplicationContext(), "Sesión iniciada").show();
                } else {
                    Toasty.error(getActivity().getApplicationContext(), "Usuario/Contraseña incorrecta").show();
                }

            }
        });


        return builder.create();
    }
}
