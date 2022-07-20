package com.appybuilder.segui09516.VCS_Troya;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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

public class CustomDeleteCuentasDialog extends DialogFragment {

    private DatabaseReference myDatabaseReference;
    private DatabaseReference myRefCuentas;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.custom_delete_cuentas_layout,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Borrar línea");
        builder.setView(view);

        final Spinner concept = view.findViewById(R.id.conceptCuentas);

        Bundle b = getArguments();

        ArrayList<String> conceptos = b.getStringArrayList("conceptos");

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_dropdown_item, conceptos);

        concept.setAdapter(spinnerArrayAdapter);

        builder.setNegativeButton("Cancelar" , null);
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    myDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Troya");
                    myRefCuentas = myDatabaseReference.child("Cuentas");

                    myRefCuentas.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot d : dataSnapshot.getChildren()){
                                if(d.child("concepto").getValue().equals(concept.getSelectedItem().toString())){
                                    d.getRef().removeValue();
                                    sendResponse();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
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

    private void sendResponse(){
        Toasty.success(context, "Item eliminado con éxito!").show();
        Intent intent = new Intent();
        getTargetFragment().onActivityResult(
                getTargetRequestCode(), 999, intent);
    }
}
