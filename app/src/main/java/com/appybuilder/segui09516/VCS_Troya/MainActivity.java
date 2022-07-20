package com.appybuilder.segui09516.VCS_Troya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shreyaspatil.material.navigationview.MaterialNavigationView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private boolean connection;
    private boolean is_logged = false;
    private boolean admin_logged = false;
    private boolean is_cuentas_logged = false;
    private DataApp dataApp = new DataApp();
    private MaterialNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            connection=true;
            readData();
            setUpFirebase();
        } else {
            connection=false;
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new HomeFragment()).commit();
                        break;
                    case R.id.nav_settings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new SettingsFragment()).commit();
                        break;
                    case R.id.nav_stats:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new StatsFragment()).commit();
                        break;
                    case R.id.nav_classf:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new ClassfFragment()).commit();
                        break;
                    case R.id.nav_calendar:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new CalendarFragment()).commit();
                        break;
                    case R.id.nav_gps:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new GPSFragment()).commit();
                        break;
                    case R.id.nav_convocatoria:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new ConvocatoriaFragment()).commit();
                        break;
                    case R.id.nav_entreno:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new EntrenamientoFragment()).commit();
                        break;
                    case R.id.nav_plantilla:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new PlantillaFragment()).commit();
                        break;
                    case R.id.nav_cuentas:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new CuentasFragment()).commit();
                        break;
                    case R.id.admin:
                        CustomLoginDialog customLoginDialog = new CustomLoginDialog();
                        customLoginDialog.show(getSupportFragmentManager(),"login_dialog");
                        break;
                }

                drawer.closeDrawer(GravityCompat.START);

                return true;
            }
        });

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(!connection){
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            Toasty.error(this, "Conéctate a Internet antes de usar la aplicación").show();
        }



        ImageButton facebookBtn = navigationView.getHeaderView(0).findViewById(R.id.facebook_btn);

        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String facebookId = "fb://group/1961202620777508";
                String urlPage = "http://www.facebook.com/groups/1961202620777508/";

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookId )));
                } catch (Exception e) {
                    //Abre url de pagina.
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlPage)));
                }

                drawer.closeDrawer(GravityCompat.START);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean is_logged() {
        return is_logged;
    }

    public void setIs_logged(boolean is_logged) {
        this.is_logged = is_logged;

        if(is_logged){
            navigationView.getMenu().findItem(R.id.nav_settings).setVisible(true);
        }
    }

    public DataApp getDataApp() {
        return dataApp;
    }

    public void setDataApp(DataApp dataApp) {
        this.dataApp = dataApp;
    }

    private void readData(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://score-1007.appspot.com/").child("datos.dat");

        try {
            final File f = File.createTempFile ("datos","dat");
            storageReference.getFile(f).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    try {
                        FileInputStream file = new FileInputStream(f);
                        ObjectInputStream ois = new ObjectInputStream(file);

                        setDataApp((DataApp) ois.readObject());
                        ois.close();

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new HomeFragment()).commit();

                        navigationView.setCheckedItem(R.id.nav_home);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public DrawerLayout getDrawer() {
        return drawer;
    }

    public boolean is_cuentas_logged() {
        return is_cuentas_logged;
    }

    public void setIs_cuentas_logged(boolean is_cuentas_logged) {
        this.is_cuentas_logged = is_cuentas_logged;
    }

    @Override
    protected void onPause() {
        finishAffinity();
        super.onPause();
        //La app esta en segundo plano (background).
    }

    public boolean isAdmin_logged() {
        return admin_logged;
    }

    public void setAdmin_logged(boolean admin_logged) {
        this.admin_logged = admin_logged;

        if(isAdmin_logged()){
            navigationView.getMenu().findItem(R.id.nav_settings).setVisible(true);
        }
    }

    private void setUpFirebase(){

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("firebase", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        Log.d("firebase", token);
                    }
                });

        FirebaseMessaging.getInstance().subscribeToTopic("userTroyaAPP")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("firebase", "substouserTroyaAPP");
                    }
                });

    }
}
