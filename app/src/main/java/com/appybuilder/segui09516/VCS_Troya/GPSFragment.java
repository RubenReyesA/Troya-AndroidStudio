package com.appybuilder.segui09516.VCS_Troya;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class GPSFragment extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gps, container, false);

        String title = "Campos de Fútbol";
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);

        final Spinner s_teams = view.findViewById(R.id.spinnerGPS);

        ArrayList<String> teams = ((MainActivity) getActivity()).getDataApp().getTeamsNames();

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, teams);

        s_teams.setAdapter(spinnerArrayAdapter);

        Button openMaps = view.findViewById(R.id.go_GPS);

        openMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Team t = ((MainActivity) getActivity()).getDataApp().getTeams().get(s_teams.getSelectedItemPosition());

                double tLatitud = t.getLatitud();
                double tLongitud = t.getLongitud();
                String tName = t.getNameTeam();

                String to_open = "geo:<" + Double.toString(tLatitud) + ">,<" + Double.toString(tLongitud) + ">?z=16&q=<" +
                        Double.toString(tLatitud) + ">,<" + Double.toString(tLongitud) + ">(" + tName + ")";

                Toasty.info(getContext(), tName).show();

                Uri uri = Uri.parse(to_open);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));

            }
        });

        return view;
    }
}
