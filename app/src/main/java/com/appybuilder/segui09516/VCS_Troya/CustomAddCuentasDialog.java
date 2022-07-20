package com.appybuilder.segui09516.VCS_Troya;

import android.app.Dialog;
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

import es.dmoral.toasty.Toasty;

public class CustomAddCuentasDialog extends DialogFragment {

    private DatabaseReference myDatabaseReference;
    private DatabaseReference myRefCuentas;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.custom_add_cuentas_layout,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Añadir línea");
        builder.setView(view);

        final EditText concept = view.findViewById(R.id.conceptCuentas);
        final EditText value = view.findViewById(R.id.valueCuentas);

        builder.setNegativeButton("Cancelar" , null);
        builder.setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String concepto = concept.getText().toString();
                String precio = value.getText().toString();

                if(!concepto.equals("") && !precio.equals("")) {
                    precio = precio.toString() + " €";
                    myDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Troya");
                    myRefCuentas = myDatabaseReference.child("Cuentas");
                    myRefCuentas.push().setValue(new Cuentas(toTitleCase(concepto), precio));

                    Toasty.success(getContext(),"Item creado con éxito!").show();
                    Intent intent = new Intent();
                    getTargetFragment().onActivityResult(
                            getTargetRequestCode(), 999, intent);
                }
                else{
                    Toasty.warning(getContext(), "Item no creado!").show();
                }

            }
        });


        return builder.create();
    }

    public static String toTitleCase(String str) {

        if (str == null) {
            return null;
        }

        boolean space = true;
        StringBuilder builder = new StringBuilder(str);
        final int len = builder.length();

        for (int i = 0; i < len; ++i) {
            char c = builder.charAt(i);
            if (space) {
                if (!Character.isWhitespace(c)) {
                    // Convert to title case and switch out of whitespace mode.
                    builder.setCharAt(i, Character.toTitleCase(c));
                    space = false;
                }
            } else if (Character.isWhitespace(c)) {
                space = true;
            } else {
                builder.setCharAt(i, Character.toLowerCase(c));
            }
        }

        return builder.toString();
    }
}
