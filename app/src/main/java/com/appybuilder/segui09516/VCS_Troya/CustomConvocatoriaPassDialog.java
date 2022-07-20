package com.appybuilder.segui09516.VCS_Troya;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shreyaspatil.MaterialDialog.MaterialDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static com.android.volley.VolleyLog.TAG;

public class CustomConvocatoriaPassDialog extends DialogFragment {

    String namePlayer;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.custom_convocatoria_pass_layout,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Contraseña");
        builder.setMessage("La contraseña te permite apuntarte a la convocatoria!!");
        builder.setView(view);

        final EditText pass = view.findViewById(R.id.passConvocatoria);

        builder.setNegativeButton("Cancelar" , null);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Bundle b = getArguments();

                String name = b.getString("Name");
                namePlayer=name;

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
                                .setTitle("Marca tu asistencia al partido")
                                .setMessage("¿Vas a asistir al partido?")
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
        DatabaseReference myRefConvocatoria = myDatabaseReference.child("ConvocatoriaPartido");

        if(assist){
            myRefConvocatoria.child(Integer.toString(code)).setValue("y");
            generateNotification("Convocatoria", namePlayer + " se ha apuntado a la convocatoria. Recuerda apuntarte si no lo has hecho aún!", "Convocatoria");
            Toasty.success(context,"Te has apuntado correctamente a la convocatoria!").show();
        }
        else{
            myRefConvocatoria.child(Integer.toString(code)).setValue("n");
            generateNotification("Convocatoria", namePlayer + " no asistirá esta jornada. Recuerda apuntarte si no lo has hecho aún!", "Convocatoria");
            Toasty.success(context,"Esperamos verte la próxima semana!!").show();
        }

        Intent intent = new Intent();
        getTargetFragment().onActivityResult(
                getTargetRequestCode(), 999, intent);

    }

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAp20SxXU:APA91bHWkG0JT7cZMySoAQQQwzYBqy8L4aGm0-R9hz2-1GD_2x96ze4qYCtOZem2xyJdcopPkiH4wZ7nWDvcsHcBZYTaCxvRNfwPHu6ZRrZxDX2i9mE7R7-zlxx5gfs3c5k8uZT3Z2zK";

    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String NOTIFICATION_TICKER;
    String TOPIC;

    private void generateNotification(String title, String desc, String tick) {
        TOPIC = "/topics/userTroyaAPP"; //topic must match with what the receiver subscribed to
        NOTIFICATION_TITLE = title;
        NOTIFICATION_MESSAGE = desc;
        NOTIFICATION_TICKER = tick;


        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", NOTIFICATION_TITLE);
            notifcationBody.put("message", NOTIFICATION_MESSAGE);
            notifcationBody.put("ticker", NOTIFICATION_TICKER);

            notification.put("to", TOPIC);
            notification.put("data", notifcationBody);

        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage());
        }
        sendNotification(notification);
    }

    private void sendNotification(JSONObject notification) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
