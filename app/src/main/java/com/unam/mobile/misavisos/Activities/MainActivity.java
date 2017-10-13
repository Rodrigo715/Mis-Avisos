package com.unam.mobile.misavisos.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.unam.mobile.misavisos.Fragments.ComoEncuentras;
import com.unam.mobile.misavisos.Fragments.DondeEstas;
import com.unam.mobile.misavisos.Fragments.EnCuantoPueda;
import com.unam.mobile.misavisos.R;
import com.unam.mobile.misavisos.UtilsClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ComoEncuentras.ComoEncuentrasSelected,
        DondeEstas.DondeEstasSelected,
        EnCuantoPueda.EnCuantoPuedaSelected,
        View.OnClickListener {

    private static final String CADENA_ENVIADA = "cadenaEnviada";

    @BindView(R.id.tv_cadena_formada)
    TextView cadenaFormada;
    @BindView(R.id.btn_911)
    ImageButton btn_911;

    @BindView(R.id.barra)
    ImageView barra;

    @BindView(android.R.id.content)
    View parentLayout;

    @BindView(R.id.btn_enviarMsjPersonal)
    ImageButton enviarMsjPersonal;

    private FragmentManager fragmentManager;
    private String newTextMessage;

    private String textoComoEstas;
    private String textoDondeEstas;
    private String textoEnCuantoPueda;

    //private String textoFinal;

    private List<String> telefonosContactos = new ArrayList<>();
    private SharedPreferences preferences;


    private int num_pregunta;
    private static final int MULTIPLE_PERMISSIONS = 1;


    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationIcon(R.drawable.settings);


        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setFirstFragment();
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (!preferences.getBoolean("firstTime", false)) {
            checkAndRequestPermissions();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }

        enviarMsjPersonal.setOnClickListener(this);
        btn_911.setOnClickListener(this);
    }


    public void setFirstFragment() {
        btn_911.setVisibility(View.VISIBLE);
        num_pregunta = 0;
        cadenaFormada.setText("");
        fragmentManager = getSupportFragmentManager();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
        ComoEncuentras comoEncuentras = new ComoEncuentras();
        fragmentManager.beginTransaction()
                .add(R.id.pregunta_container, comoEncuentras)
                .addToBackStack(null)
                .commit();
        barra.setImageResource(R.drawable.barra1);
    }


    @Override
    public void comoEncuentrasSelected(int id) {
        switch (id) {
            case R.id.btn_bien:
                textoComoEstas = getString(R.string.bien);
                break;
            case R.id.btn_peligro:
                textoComoEstas = getString(R.string.peligro);
                break;
            case R.id.btn_auxilio:
                textoComoEstas = getString(R.string.auxilio);
                break;
            default:
                break;
        }
        cadenaFormada.append(textoComoEstas);
        DondeEstas dondeEstas = new DondeEstas();
        fragmentManager.beginTransaction()
                .replace(R.id.pregunta_container, dondeEstas)
                .addToBackStack(null)
                .commit();
        btn_911.setVisibility(View.GONE);
        barra.setImageResource(R.drawable.barra2);
        num_pregunta = 1;
    }


    @Override
    public void dondeEstasSelected(int id, String textoPersonal) {
        switch (id) {
            case R.id.btn_escuela:
                textoDondeEstas = getString(R.string.escuela);

                break;
            case R.id.btn_trabajo:
                textoDondeEstas = getString(R.string.trabajo);

                break;
            case R.id.btn_casa:
                textoDondeEstas = getString(R.string.casa);

                break;
            case R.id.btn_fuera:
                textoDondeEstas = getString(R.string.fuera);

                break;
            case R.id.btn_edificio:
                textoDondeEstas = getString(R.string.edificio);
                break;
            case R.id.btn_personal:
                textoDondeEstas=textoPersonal;
                break;
            default:
                break;
        }

        cadenaFormada.append(textoDondeEstas);
        EnCuantoPueda enCuantoPueda = new EnCuantoPueda();
        fragmentManager.beginTransaction()
                .replace(R.id.pregunta_container, enCuantoPueda)
                .addToBackStack(null)
                .commit();
        barra.setImageResource(R.drawable.barra3);
        num_pregunta = 2;
    }


    @Override
    public void enCuantoPuedaSelected(int id) {
        switch (id) {
            case R.id.btn_llamame:
                textoEnCuantoPueda = getString(R.string.llamame);
                break;
            case R.id.btn_teLlamo:
                textoEnCuantoPueda = getString(R.string.teLlamo);
                break;
            default:
                break;
        }
        cadenaFormada.append(textoEnCuantoPueda);
        //Enviar SMS
        sendSMS(cadenaFormada.getText().toString());
    }


    private void sendSMS(String mensaje) {
        telefonosContactos = UtilsClass.leeJSON(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(parentLayout, getString(R.string.permSms), Snackbar.LENGTH_SHORT).show();
        } else {
            if (telefonosContactos.isEmpty()) {
                Snackbar.make(parentLayout, getString(R.string.contactosVacio), Snackbar.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < telefonosContactos.size(); i++) {
                    UtilsClass.sendSms(telefonosContactos.get(i), mensaje, this);
                }
                Toast.makeText(this, getString(R.string.mensajeEnviado), Toast.LENGTH_SHORT).show();
                num_pregunta = 3;
                Intent intent = new Intent(this, LocationActivity.class);
                intent.putExtra(CADENA_ENVIADA,mensaje);
                startActivity(intent);
            }
        }
    }


    public void checkAndRequestPermissions() {
        int permissionSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        /*int permissionCall = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE);*/
        int permissionLocation = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionContacts = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS);

        List<String> permissionsNeeded = new ArrayList<>();
        if (permissionSMS != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        /*if (permissionCall != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }*/
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permissionContacts != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }

        if (!permissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsNeeded.toArray(new String[permissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            //return false;
        }
        //return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();

                perms.put(Manifest.permission.SEND_SMS, PackageManager.PERMISSION_GRANTED);
                //perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);

                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        perms.put(permissions[i], grantResults[i]);
                    }
                    if (perms.get(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {

                    } else {
                    }

                    if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    } else {

                    }


                    if (perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {

                    } else {
                    }
                }
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_911:
                UtilsClass.makePhoneCall(this);
                break;
            case R.id.btn_enviarMsjPersonal:
                String mensajePersonal = preferences.getString("mensajePersonal", "");
                if (mensajePersonal.isEmpty()) {
                    Snackbar.make(parentLayout, getString(R.string.introduceMsj), Snackbar.LENGTH_SHORT).show();
                } else {
                    sendSMS(mensajePersonal);
                }
                break;
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.mis_contactos_item) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(parentLayout,getString(R.string.permCont), Snackbar.LENGTH_SHORT ).show();
            } else {
                Intent intent = new Intent(MainActivity.this, ListaContactosActivity.class);
                startActivity(intent);
            }
            return true;
        } else if (id == R.id.medidas_seguridad) {
            Toast.makeText(this, getString(R.string.enDesarrollo), Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (num_pregunta == 0) {
            finish();
        } else {
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
            }
            String texto = cadenaFormada.getText().toString();
            if (num_pregunta == 1) {
                newTextMessage = texto.replace(textoComoEstas, "");
                barra.setImageResource(R.drawable.barra1);
                btn_911.setVisibility(View.VISIBLE);
            } else if (num_pregunta == 2) {
                newTextMessage = texto.replace(textoDondeEstas, "");
                barra.setImageResource(R.drawable.barra2);
            } else if (num_pregunta == 3) {
                newTextMessage = texto.replace(textoEnCuantoPueda, "");
                barra.setImageResource(R.drawable.barra3);
            }
            num_pregunta -= 1;
            cadenaFormada.setText(newTextMessage);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setFirstFragment();
    }


}
