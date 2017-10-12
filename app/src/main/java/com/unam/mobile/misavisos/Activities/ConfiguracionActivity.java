package com.unam.mobile.misavisos.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.unam.mobile.misavisos.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConfiguracionActivity extends AppCompatActivity {

    @BindView(R.id.btn_agregar_contactos)
    ImageButton agregarContactos;
    @BindView(android.R.id.content)
    View parentView;
    @BindView(R.id.btn_guardarTexto)
    ImageButton guardarTexto;
    @BindView(R.id.msj_personal)
    EditText msj_personal;

    private Context context;
    private SharedPreferences preferences;
    private String mensajePersonal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        ButterKnife.bind(this);

        context = getApplicationContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        if(!preferences.getString("mensajePersonal","").isEmpty()){
            msj_personal.setText(preferences.getString("mensajePersonal",""));
        }


        guardarTexto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mensajePersonal=msj_personal.getText().toString();
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("mensajePersonal",mensajePersonal);
                editor.commit();
                //Log.e("MSJ PER",preferences.getString("mensajePersonal",""));
                Toast.makeText(context,"Mensaje Guardado",Toast.LENGTH_SHORT).show();
            }
        });

        agregarContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(parentView,getString(R.string.permCont), Snackbar.LENGTH_SHORT ).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), ListaContactosActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
    }*/
}
