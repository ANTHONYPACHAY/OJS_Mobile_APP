package anthony.uteq.ojsmobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mindorks.placeholderview.PlaceHolderView;

import java.util.ArrayList;
import java.util.List;

import anthony.uteq.ojsmobileapp.controller.Card_journlaManager;
import anthony.uteq.ojsmobileapp.models.JournalObject;
import anthony.uteq.ojsmobileapp.utiles.Alerts;
import anthony.uteq.ojsmobileapp.utiles.DataStatic;
import anthony.uteq.ojsmobileapp.utiles.Methods;
import anthony.uteq.ojsmobileapp.utiles.MyLogs;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        getJournals();
    }

    public void createElements(List<JournalObject> elements){
        for(JournalObject elem :elements){
            PlaceHolderView CardElement = (PlaceHolderView)findViewById(R.id.journalsView);
            CardElement.addView(new Card_journlaManager(this.getApplicationContext(), CardElement, elem));
        }
    }

    private void getJournals(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                DataStatic.gerUrlApi("journals.php"),
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MyLogs.info("ws todo bien");
                        //Procesar las respuesta y armar un Array con estos
                        List<JournalObject> data = processResponse(response);
                        //método para mostrar los datos en el activity
                        createElements(data);
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MyLogs.error("we error"+ error.getMessage()+ "\n"+ DataStatic.gerUrlApi("journals.php"));
                        Alerts.MessageToast(MainActivity.this, "Data Error");
                    }
                }
        );
        queue.add(request);
    }

    private List<JournalObject> processResponse(String response){
        List<JournalObject> data = new ArrayList<>();
        Gson gson = new Gson();//convertidor de jsonObjecto a Object.class
        //Convertir el string de respuesta(con formato JsonArray) en un JsonArray
        JsonArray jarr = Methods.stringToJsonArray(response);
        //validar cantidad de elementos, para informar en caso de no encontrar alguno
        if(jarr.size() > 0){
            //recorrer los items
            for(int ind = 0; ind <jarr.size(); ind++) {
                //convertir el elemento del json en jsonObject(por defecto los items dentro de
                // JsonArray son de tipo JsonElement)
                JsonObject jso = Methods.JsonElementToJSO(jarr.get(ind));
                //Verificar cantidad de keys dentor del json (si hay 0 lo mas probable es que haya
                // ocurrido algún problema durante la conversión de JsonElement a JsonObject)
                if(jso.size() > 0) {
                    //Casteo de JsonObject s Java.class (en este caso SuperItem)
                    JournalObject item = gson.fromJson(jso.toString(), JournalObject.class);
                    //agrega el item a la lista
                    data.add(item);
                }
            }
        }else{
            //Toast indicando la ausencia de elementos
            Alerts.MessageToast(MainActivity.this, "No hay registros.");
        }
        //retorno de la lista con todos los elementos
        return data;
    }
}