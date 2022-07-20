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

import es.dmoral.toasty.Toasty;

public class CustomCommentDialog extends DialogFragment {

    private DatabaseReference myDatabaseReference;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        myDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Troya");

        View view = getActivity().getLayoutInflater().inflate(R.layout.custom_comment_layout, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Nuevo Comentario");
        builder.setView(view);

        final EditText comment = view.findViewById(R.id.comment);

        builder.setNegativeButton("Cancelar", null);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

               if(!comment.toString().equals("")){
                   DatabaseReference myRefComment = myDatabaseReference.child("Comentario");
                   myRefComment.setValue(comment.getText().toString());

                   Toasty.success(getContext(),"Comentario guardado con éxito!").show();
               }
               else{
                   Toasty.error(getContext(), "No se puede guardar un comentario vacío").show();
               }
            }
        });


        return builder.create();
    }
}
