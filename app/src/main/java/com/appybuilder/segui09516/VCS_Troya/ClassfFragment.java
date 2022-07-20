package com.appybuilder.segui09516.VCS_Troya;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ClassfFragment extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_classf,container,false);
        String title = "Clasificación";
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);

        WebView wb = view.findViewById(R.id.wb_Classf);
        wb.getSettings().setBuiltInZoomControls(true);
        wb.getSettings().setSupportZoom(true);
        wb.loadUrl("http://www.aec84.com/cdo.htm");

        return view;
    }
}
