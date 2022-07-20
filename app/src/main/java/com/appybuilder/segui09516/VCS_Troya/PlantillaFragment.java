package com.appybuilder.segui09516.VCS_Troya;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.legacy.widget.Space;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class PlantillaFragment extends Fragment {

    private View view;
    private CardView[] cw;

    private int dpAsPixels(int dp, float scale) {
        return (int) (dp * scale + 0.5f);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_plantilla, container, false);

        String title = "Plantilla";
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);

        float scale = getResources().getDisplayMetrics().density;

        ImageView img_temporada = view.findViewById(R.id.img_Temporada);
        Glide.with(getContext()).load("file:///android_asset/temporada.png").into(img_temporada);


        ArrayList<Player> playerArrayList = ((MainActivity) getActivity()).getDataApp().getPlayers();

        cw = new CardView[playerArrayList.size() + 3];

        LinearLayout global = view.findViewById(R.id.plantillaGLOBAL);

        for (int i = 0; i < playerArrayList.size() - 1; i++) {

            cw[i] = new CardView(getActivity().getApplicationContext());
            cw[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ViewGroup.MarginLayoutParams cardMargin = (ViewGroup.MarginLayoutParams) cw[i].getLayoutParams();
            cardMargin.setMargins(0, dpAsPixels(20, scale), 0, 0);
            cw[i].requestLayout();
            cw[i].setRadius(dpAsPixels(10, scale));
            cw[i].setCardElevation(dpAsPixels(6, scale));
            cw[i].setPreventCornerOverlap(true);

            LinearLayout ly = new LinearLayout(getActivity().getApplicationContext());
            ly.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            ly.setOrientation(LinearLayout.HORIZONTAL);
            ly.setGravity(Gravity.CENTER);

            ImageView img1 = new ImageView(getActivity().getApplicationContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dpAsPixels(80, scale), dpAsPixels(80, scale));
            lp.setMarginStart(dpAsPixels(20, scale));
            img1.setLayoutParams(lp);
            Glide.with(getContext()).load("file:///android_asset/p" + playerArrayList.get(i).getNum_player() + ".png").into(img1);

            Space space = new Space(getActivity().getApplicationContext());
            space.setLayoutParams(new LinearLayout.LayoutParams(dpAsPixels(10, scale), dpAsPixels(10, scale)));

            ImageView img2 = new ImageView(getActivity().getApplicationContext());
            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(dpAsPixels(100, scale), dpAsPixels(200, scale));
            img2.setLayoutParams(lp2);
            Glide.with(getContext()).load("file:///android_asset/player" + playerArrayList.get(i).getNum_player() + ".png").into(img2);

            Space space2 = new Space(getActivity().getApplicationContext());
            space2.setLayoutParams(new LinearLayout.LayoutParams(dpAsPixels(10, scale), dpAsPixels(10, scale)));

            LinearLayout ly2 = new LinearLayout(getActivity().getApplicationContext());
            ly2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            ly2.setGravity(Gravity.CENTER);
            ly2.setOrientation(LinearLayout.VERTICAL);

            TextView tx1 = new TextView(getActivity().getApplicationContext());
            tx1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tx1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tx1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            tx1.setText(playerArrayList.get(i).getName_player());

            Space space3 = new Space(getActivity().getApplicationContext());
            space3.setLayoutParams(new LinearLayout.LayoutParams(dpAsPixels(10, scale), dpAsPixels(10, scale)));

            TextView tx2 = new TextView(getActivity().getApplicationContext());
            tx2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tx2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tx2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            tx2.setText(playerArrayList.get(i).getDescription_player());

            ly2.addView(tx1);
            ly2.addView(space3);
            ly2.addView(tx2);

            ly.addView(img1);
            ly.addView(space);
            ly.addView(img2);
            ly.addView(space2);
            ly.addView(ly2);

            cw[i].addView(ly);
        }

        addToPlantilla(playerArrayList.size() - 1, scale, "ct", "0",
                playerArrayList.get(playerArrayList.size() - 1).getName_player(), playerArrayList.get(playerArrayList.size() - 1).getDescription_player());

        addToPlantilla(playerArrayList.size(), scale, "en", "Coach", "Rodri", "Entrenador");
        addToPlantilla(playerArrayList.size() + 1, scale, "ct", "Trainer", "Antonio", "");
        addToPlantilla(playerArrayList.size() + 2, scale, "de", "Tech", "Rubén", "Informático");

        for (CardView c : cw) {
            global.addView(c);
        }

        return view;
    }

    private void addToPlantilla(int pos, float scale, String name_camiseta, String name_foto, String name, String description) {

        cw[pos] = new CardView(getActivity().getApplicationContext());
        cw[pos].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ViewGroup.MarginLayoutParams cardMargin = (ViewGroup.MarginLayoutParams) cw[pos].getLayoutParams();
        cardMargin.setMargins(0, dpAsPixels(20, scale), 0, 0);
        cw[pos].requestLayout();
        cw[pos].setRadius(dpAsPixels(10, scale));
        cw[pos].setCardElevation(dpAsPixels(6, scale));
        cw[pos].setPreventCornerOverlap(true);

        LinearLayout ly = new LinearLayout(getActivity().getApplicationContext());
        ly.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ly.setOrientation(LinearLayout.HORIZONTAL);
        ly.setGravity(Gravity.CENTER);

        ImageView img1 = new ImageView(getActivity().getApplicationContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dpAsPixels(80, scale), dpAsPixels(80, scale));
        lp.setMarginStart(dpAsPixels(20, scale));
        img1.setLayoutParams(lp);
        Glide.with(getContext()).load("file:///android_asset/p" + name_camiseta + ".png").into(img1);

        Space space = new Space(getActivity().getApplicationContext());
        space.setLayoutParams(new LinearLayout.LayoutParams(dpAsPixels(10, scale), dpAsPixels(10, scale)));

        ImageView img2 = new ImageView(getActivity().getApplicationContext());
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(dpAsPixels(100, scale), dpAsPixels(200, scale));
        img2.setLayoutParams(lp2);
        Glide.with(getContext()).load("file:///android_asset/player" + name_foto + ".png").into(img2);

        Space space2 = new Space(getActivity().getApplicationContext());
        space2.setLayoutParams(new LinearLayout.LayoutParams(dpAsPixels(10, scale), dpAsPixels(10, scale)));

        LinearLayout ly2 = new LinearLayout(getActivity().getApplicationContext());
        ly2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ly2.setGravity(Gravity.CENTER);
        ly2.setOrientation(LinearLayout.VERTICAL);

        TextView tx1 = new TextView(getActivity().getApplicationContext());
        tx1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tx1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tx1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        tx1.setText(name);

        Space space3 = new Space(getActivity().getApplicationContext());
        space3.setLayoutParams(new LinearLayout.LayoutParams(dpAsPixels(10, scale), dpAsPixels(10, scale)));

        TextView tx2 = new TextView(getActivity().getApplicationContext());
        tx2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tx2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tx2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        tx2.setText(description);

        ly2.addView(tx1);
        ly2.addView(space3);
        ly2.addView(tx2);

        ly.addView(img1);
        ly.addView(space);
        ly.addView(img2);
        ly.addView(space2);
        ly.addView(ly2);

        cw[pos].addView(ly);
    }
}
