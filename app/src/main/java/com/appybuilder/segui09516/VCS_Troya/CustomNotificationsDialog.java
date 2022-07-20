package com.appybuilder.segui09516.VCS_Troya;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class CustomNotificationsDialog extends DialogFragment {

    private DatabaseReference myDatabaseReference;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAp20SxXU:APA91bHWkG0JT7cZMySoAQQQwzYBqy8L4aGm0-R9hz2-1GD_2x96ze4qYCtOZem2xyJdcopPkiH4wZ7nWDvcsHcBZYTaCxvRNfwPHu6ZRrZxDX2i9mE7R7-zlxx5gfs3c5k8uZT3Z2zK";

    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String NOTIFICATION_TICKER;
    String TOPIC;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        myDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Troya");

        View view = getActivity().getLayoutInflater().inflate(R.layout.custom_notifications_layout, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Enviar notificaciones");
        builder.setView(view);

        final Button sendResultMatch = view.findViewById(R.id.sendResultMatch);
        final Button sendStatsUpdate = view.findViewById(R.id.sendStatsUpdate);
        final Button sendUpdateAvalaible = view.findViewById(R.id.sendUpdateAvalaible);

        sendResultMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference myRefResultado = myDatabaseReference.child("Resultado");
                final String[] res = {""};
                myRefResultado.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        res[0] = (String) dataSnapshot.getValue();
                        generateNotification("Fin del partido", "Resultado del partido: " + res[0], "Resultado del partido disponible!");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        sendStatsUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateNotification("Estadísticas", "Las estadísticas se han actualizado", "Estadísticas actualizadas!");
            }
        });

        sendUpdateAvalaible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateNotification("Actualización disponible", "Abre Google Play Store y actualiza la app", "Actualiza ya!");
            }
        });


        builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }

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
                        Toasty.success(getContext(), "Notification sent!").show();
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
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }
}