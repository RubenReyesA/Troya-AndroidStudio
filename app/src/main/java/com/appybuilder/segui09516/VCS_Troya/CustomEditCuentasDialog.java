package com.appybuilder.segui09516.VCS_Troya;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class CustomEditCuentasDialog extends DialogFragment {

    private DatabaseReference myDatabaseReference;
    private DatabaseReference myRefCuentas;
    private ArrayList<Cuentas> cuentasArrayList;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.custom_edit_cuentas_layout, null);

        myDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Troya");
        myRefCuentas = myDatabaseReference.child("Cuentas");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Editar línea");
        builder.setView(view);

        final Spinner concept = view.findViewById(R.id.conceptCuentas);
        final EditText newConcept = view.findViewById(R.id.newConcept);
        final EditText value = view.findViewById(R.id.valueCuentas);

        Bundle b = getArguments();

        ArrayList<String> conceptos = b.getStringArrayList("conceptos");

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_dropdown_item, conceptos);

        concept.setAdapter(spinnerArrayAdapter);

        concept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                myRefCuentas.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        cuentasArrayList = new ArrayList<>();
                        for (DataSnapshot item_snapshot : dataSnapshot.getChildren()) {
                            cuentasArrayList.add(item_snapshot.getValue(Cuentas.class));
                        }

                        final String conceptValue = concept.getSelectedItem().toString();

                        boolean found = false;
                        int i = 0;

                        while (i < cuentasArrayList.size() && !found) {
                            if (cuentasArrayList.get(i).getConcepto().equals(conceptValue)) {
                                found = true;
                            } else {
                                i++;
                            }
                        }

                        newConcept.setHint(cuentasArrayList.get(i).getConcepto());
                        value.setHint(cuentasArrayList.get(i).getPrecio());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendResponse(0, "Item no editado!");
            }
        });

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final String conceptoSpin = concept.getSelectedItem().toString();

                final String conceptnew = newConcept.getText().toString();
                String precio = value.getText().toString();

                int option = -1;

                if (!conceptnew.equals("") && (!precio.equals(""))) {
                    option = 2;
                } else if (!precio.equals("")) {
                    option = 1;
                } else if (!conceptnew.equals("")) {
                    option = 0;
                }

                switch (option) {
                    case 0:
                        myRefCuentas.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot item_snapshot : dataSnapshot.getChildren()) {

                                    String conceptoString = (String) item_snapshot.child("concepto").getValue();

                                    if (conceptoString.equals(conceptoSpin)) {
                                        item_snapshot.getRef().child("concepto").setValue(toTitleCase(conceptnew));
                                        sendResponse(1, "Item editado con éxito!");
                                    }

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                        break;
                    case 1:
                        precio = precio.toString() + " €";

                        final String finalPrecio = precio;
                        myRefCuentas.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot item_snapshot : dataSnapshot.getChildren()) {

                                    String conceptoString = (String) item_snapshot.child("concepto").getValue();

                                    if (conceptoString.equals(conceptoSpin)) {
                                        item_snapshot.getRef().child("precio").setValue(finalPrecio);
                                        sendResponse(1, "Item editado con éxito!");
                                    }

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                        break;
                    case 2:
                        precio = precio.toString() + " €";

                        final String finalPrecio2 = precio;
                        myRefCuentas.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot item_snapshot : dataSnapshot.getChildren()) {

                                    String conceptoString = (String) item_snapshot.child("concepto").getValue();

                                    if (conceptoString.equals(conceptoSpin)) {
                                        item_snapshot.getRef().child("concepto").setValue(toTitleCase(conceptnew));
                                        item_snapshot.getRef().child("precio").setValue(finalPrecio2);
                                        sendResponse(1, "Item editado con éxito!");
                                    }

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                        break;
                    default:
                        sendResponse(0, "Item no editado!");
                        break;
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

    private void sendResponse(int option, String text) {
        if (option == 0) {
            Toasty.warning(context, text).show();
        } else if (option == 1) {
            Toasty.success(context, text).show();
        }
        Intent intent = new Intent();
        getTargetFragment().onActivityResult(
                getTargetRequestCode(), 999, intent);
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
