package com.appybuilder.segui09516.VCS_Troya;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CuentasFragment extends Fragment {

    private View view;
    private DatabaseReference myDatabaseReference;
    private DatabaseReference myRefCuentas;

    private Fragment actual;

    private ArrayList<Cuentas> cuentasArrayList;
    private Table table;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cuentas, container, false);
        actual = this;
        String title = "Cuentas";
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);

        if (((MainActivity) getActivity()).is_cuentas_logged()) {
            setHasOptionsMenu(true);
        }

        myDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Troya");
        myRefCuentas = myDatabaseReference.child("Cuentas");

        myRefCuentas.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                cuentasArrayList = new ArrayList<>();

                for (DataSnapshot valueRes : dataSnapshot.getChildren()) {
                    Cuentas c = valueRes.getValue(Cuentas.class);
                    cuentasArrayList.add(c);
                }

                TableLayout ly = view.findViewById(R.id.table);
                ly.removeAllViews();

                table = new Table(ly, actual);

                ArrayList<String> headers = new ArrayList<>();
                headers.add("Concepto");
                headers.add("Precio");

                table.addHeaders(headers);

                for (int i = 0; i < cuentasArrayList.size(); i++) {

                    ArrayList<String> row = new ArrayList<>();
                    row.add(cuentasArrayList.get(i).getConcepto());
                    row.add(cuentasArrayList.get(i).getPrecio());
                    table.addTableRow(row);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.cuentas_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.cuentas_add:
                CustomAddCuentasDialog addCuentasDialog = new CustomAddCuentasDialog();
                addCuentasDialog.show(getActivity().getSupportFragmentManager(), "cuentas_add_dialog");
                addCuentasDialog.setTargetFragment(this, 999);
                break;
            case R.id.cuentas_edit:
                CustomEditCuentasDialog editCuentasDialog = new CustomEditCuentasDialog();
                Bundle b1 = new Bundle();
                b1.putStringArrayList("conceptos", table.returnOfColumnValues(1));
                editCuentasDialog.setArguments(b1);
                editCuentasDialog.show(getActivity().getSupportFragmentManager(), "cuentas_edit_dialog");
                editCuentasDialog.setTargetFragment(this, 999);
                break;
            case R.id.cuentas_delete:
                CustomDeleteCuentasDialog deleteCuentasDialog = new CustomDeleteCuentasDialog();
                Bundle b2 = new Bundle();
                b2.putStringArrayList("conceptos", table.returnOfColumnValues(1));
                deleteCuentasDialog.setArguments(b2);
                deleteCuentasDialog.setTargetFragment(this, 999);
                deleteCuentasDialog.show(getActivity().getSupportFragmentManager(), "cuentas_delete_dialog");
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 999 &&
                resultCode == 999) {

            Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.detach(currentFragment);
            fragmentTransaction.attach(currentFragment);
            fragmentTransaction.commit();

        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
