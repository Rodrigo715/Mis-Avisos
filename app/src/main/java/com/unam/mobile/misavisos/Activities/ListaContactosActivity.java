package com.unam.mobile.misavisos.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unam.mobile.misavisos.AdaptadorContacto;
import com.unam.mobile.misavisos.Contacto;
import com.unam.mobile.misavisos.R;
import com.unam.mobile.misavisos.UtilsClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListaContactosActivity extends AppCompatActivity {

    public List<Contacto> contactoList=new ArrayList<>();
    public Contacto contacto;
    Cursor contactoCursor;
    AdaptadorContacto adaptadorContacto;
    UtilsClass utilsClass;

    @BindView(R.id.contactsList)
    ListView listaContactos;
    @BindView(R.id.botonGuardar)
    ImageButton botonGuardar;

    public List<Contacto> contactosGuardados = new ArrayList<>();
    public List<Contacto> telefonosContactos= new ArrayList<>();
    Gson gson;
    SharedPreferences sharedPreferences, preferences;
    SharedPreferences.Editor editor;
    Intent intent;
    String contactos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contactos);
        ButterKnife.bind(this);
        utilsClass= new UtilsClass();
        adaptadorContacto= new AdaptadorContacto(this,contactoList,this);
        listaContactos.setAdapter(adaptadorContacto);
        intent = new Intent(ListaContactosActivity.this,MainActivity.class);
        obtenerContactos();
        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d("Estado","Guardar contactos");
                guardarContactos();
            }
        });
    }


    //Obtiene la información de los contactos del dispositivo, ordenandolos alfabeticamente y guardarndolos en una lista
    public void obtenerContactos(){
        String[] projection= new String[]{ContactsContract.Data._ID,ContactsContract.Data.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE};
        String where= ContactsContract.Data.MIMETYPE+"='"+ ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE+"' AND "+ ContactsContract.CommonDataKinds.Phone.NUMBER+ " IS NOT NULL";
        String orden= ContactsContract.Data.DISPLAY_NAME+" ASC";
        contactos=recuperaContactos(this);
        if(contactos.equals("vacio")){
            //Log.d("Estado", "Vacio");
            contactoCursor=getContentResolver().query(ContactsContract.Data.CONTENT_URI,projection,where,null,orden);
            while (contactoCursor.moveToNext()){
                try {
                    contactoCursor.moveToNext();
                    contacto= new Contacto();contacto.setIdContacto(contactoCursor.getString(0));
                    contacto.setNombre(contactoCursor.getString(1));
                    contacto.setTelefono(contactoCursor.getString(2));
                    contacto.setSelected(false);
                    contactoList.add(contacto);
                    adaptadorContacto.notifyDataSetChanged();
                }catch (CursorIndexOutOfBoundsException ex){
                    ex.printStackTrace();
                }
            }
        }else{
            contactoCursor=getContentResolver().query(ContactsContract.Data.CONTENT_URI,projection,where,null,orden);
            while (contactoCursor.moveToNext()){
                try {
                    contactoCursor.moveToNext();
                    contacto= new Contacto();
                    contacto.setSelected(false);
                    contacto.setIdContacto(contactoCursor.getString(0));
                    contacto.setNombre(contactoCursor.getString(1));
                    contacto.setTelefono(contactoCursor.getString(2));
                    try{
                        JSONArray jsonArray= new JSONArray(contactos);
                        for(int i=0; i<jsonArray.length(); i++){
                            JSONObject contactoJson=jsonArray.getJSONObject(i);
                            String nombreContacto=contactoJson.getString("nombre");
                            if(nombreContacto.equals(contactoCursor.getString(1))){
                                contacto.setSelected(true);
                            }
                        }
                    }catch (JSONException ex){
                        ex.printStackTrace();
                    }

                    contactoList.add(contacto);
                    adaptadorContacto.notifyDataSetChanged();
                }catch (CursorIndexOutOfBoundsException ex){
                    ex.printStackTrace();
                }
            }
        }

    }

    //Guarda la lista de contactos seleccionados en un archivo JSON, utilizando la biblioteca GSON, y SharedPreferences
    public void guardarContactos(){
        for(int i=0; i<AdaptadorContacto.contactoList.size();i++){
            if(AdaptadorContacto.contactoList.get(i).getSelected()){
                contactosGuardados.add(contactoList.get(i));
                //Log.d("Contacto guardado",contactoList.get(i).getNombre());
            }
        }
        gson= new GsonBuilder().setPrettyPrinting().create();
        String jsonContactos=gson.toJson(contactosGuardados);
       // System.out.println("JSON:\n"+jsonContactos);

        //Guarda JSON con lista de contactos seleccionada
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        editor=sharedPreferences.edit();
        editor.remove("lista").commit();
        editor.putString("lista",jsonContactos);
        editor.commit();
        //startActivity(intent);
        finish();

    }

    //Permite mostrar solamente una vez la actividad de guardar contactos
    public void isFirstTIme(){
        SharedPreferences preferences=getPreferences(MODE_PRIVATE);
        boolean isFirst=preferences.getBoolean("isFirst",false);
        if(isFirst==false){
           // Log.d("Estado","Es la primera vez");
            SharedPreferences.Editor editor=preferences.edit();
            editor.putBoolean("isFirst",true);
            editor.commit();
        }else {
            //Log.d("Estado","No es la primera vez");
            Intent intent= new Intent(ListaContactosActivity.this,MainActivity.class);
            //Banderas necesarias para no regresar a la activity de Seleccionar y guardar contactos
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public String recuperaContactos(Context context){
        String jsonContactos;
        preferences= PreferenceManager.getDefaultSharedPreferences(context);
        jsonContactos=preferences.getString("lista","vacio");
        //Log.d("Contactos",jsonContactos);
        return jsonContactos;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                guardarContactos();
        }
        return super.onOptionsItemSelected(item);
    }
}