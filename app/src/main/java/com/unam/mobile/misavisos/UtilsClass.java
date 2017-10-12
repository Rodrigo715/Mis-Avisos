package com.unam.mobile.misavisos;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UtilsClass {

    private static SharedPreferences preferences;
    private static String jsonContactos;
    private static List<String> telefonosContactos = new ArrayList<>();

    private static String json;

    public static void sendSms(String numero, String mensaje, Context context) {
        SmsManager smsManager = SmsManager.getDefault();
        PendingIntent sentPI;
        String SENT="SMS_SENT";

        sentPI=PendingIntent.getBroadcast(context, 0, new Intent(SENT), 0 );
        smsManager.sendTextMessage(numero, null, mensaje, sentPI, null);
        //smsManager.sendTextMessage(numero, null, mensaje, null, null);

        Log.e("Cadena",mensaje);
        Log.e("Long",String.valueOf(mensaje.length()));
        Log.e("Mensaje","enviado");
    }

    public static void makePhoneCall(Context context) {
        String number = "911";
        //Intent intent = new Intent(Intent.ACTION_CALL);
        // Intent.ACTION_DIAL no requiere permission.CALL_PHONE
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        context.startActivity(intent);
    }


    //Guarda los telefonos de los contactos en una lista para enviar un mensaje a cada uno de ellos
    public static List<String> leeJSON(Context context) {
        json = recuperaContactos(context);
        try {
            JSONArray jsonArray = new JSONArray(json);
            telefonosContactos.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject contacto = jsonArray.getJSONObject(i);
                String telefono = contacto.getString("telefono");
                telefonosContactos.add(telefono);
                //Log.d("Telefono agregado",telefono);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return telefonosContactos;
    }


    //Obtiene la lista de contactos guardados por usuario almacenada con SharedPreferences
    private static String recuperaContactos(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        jsonContactos = preferences.getString("lista", "vacio");
        //Log.d("Contactos",jsonContactos);
        return jsonContactos;
    }

}