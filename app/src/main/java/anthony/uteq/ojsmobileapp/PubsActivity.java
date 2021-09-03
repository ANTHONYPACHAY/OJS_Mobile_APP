package anthony.uteq.ojsmobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mindorks.placeholderview.PlaceHolderView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import anthony.uteq.ojsmobileapp.controller.Card_pubsManager;
import anthony.uteq.ojsmobileapp.models.AuthorsObject;
import anthony.uteq.ojsmobileapp.models.IssueObject;
import anthony.uteq.ojsmobileapp.models.PubsObject;
import anthony.uteq.ojsmobileapp.models.JournalObject;
import anthony.uteq.ojsmobileapp.utiles.Alerts;
import anthony.uteq.ojsmobileapp.utiles.DataStatic;
import anthony.uteq.ojsmobileapp.utiles.Methods;
import anthony.uteq.ojsmobileapp.utiles.MyLogs;

public class PubsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pubs);

        getSupportActionBar().hide();
        onLoadPubsData();
    }

    private void onLoadPubsData() {
        Bundle bundle = this.getIntent().getExtras();
        String data = bundle.getString("issueObject");
        JsonObject jso = Methods.stringToJSON(data);
        Gson gson = new Gson();
        IssueObject issueObject = gson.fromJson(jso, IssueObject.class);

        ImageView imgIssue = (ImageView) findViewById(R.id.imgIssue);
        TextView journalName = (TextView) findViewById(R.id.journalName);


        journalName.setText(issueObject.getTitle());
        Glide.with(PubsActivity.this).load(issueObject.getCover()).into(imgIssue);

        gerIPubs(issueObject.getIssue_id());
        MyLogs.info("LLegue, dame las publicacionesss\n" + data);
    }

    public void createElements(List<PubsObject> elements) {
        for (PubsObject elem : elements) {
            PlaceHolderView CardElement = (PlaceHolderView) findViewById(R.id.pubsView);
            CardElement.addView(new Card_pubsManager(PubsActivity.this, CardElement, elem));
            MyLogs.info("agg 1 pubx1");
        }
    }

    private void gerIPubs(String pubs_id) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                DataStatic.gerUrlApi("pubs.php?i_id=" + pubs_id),
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MyLogs.info("ws todo bien");
                        //Procesar las respuesta y armar un Array con estos
                        List<PubsObject> data = processResponse(response);
                        MyLogs.info("Proceso las publicaciones");
                        //método para mostrar los datos en el activity
                        createElements(data);
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MyLogs.error("we error" + error.getMessage() + "\n" + DataStatic.gerUrlApi("journals.php"));
                        Alerts.MessageToast(PubsActivity.this, "Data Error");
                    }
                }
        );
        queue.add(request);
    }

    private List<PubsObject> processResponse(String response) {
        List<PubsObject> data = new ArrayList<>();
        Gson gson = new Gson();//convertidor de jsonObjecto a Object.class
        //Convertir el string de respuesta(con formato JsonArray) en un JsonArray
        JsonArray jarr = Methods.stringToJsonArray(response);
        //validar cantidad de elementos, para informar en caso de no encontrar alguno
        if (jarr.size() > 0) {
            //recorrer los items
            for (int ind = 0; ind < jarr.size(); ind++) {
                //convertir el elemento del json en jsonObject(por defecto los items dentro de
                // JsonArray son de tipo JsonElement)
                JsonObject jso = Methods.JsonElementToJSO(jarr.get(ind));
                //Verificar cantidad de keys dentor del json (si hay 0 lo mas probable es que haya
                // ocurrido algún problema durante la conversión de JsonElement a JsonObject)
                if (jso.size() > 0) {
                    //Casteo de JsonObject s Java.class (en este caso SuperItem)
                    MyLogs.info("leyendo");
                    PubsObject item = new PubsObject();
                    item.setSection(Methods.JsonToString(jso, "section", ""));
                    item.setPublication_id(Methods.JsonToString(jso, "publication_id", ""));
                    item.setTitle(Methods.JsonToString(jso, "title", ""));
                    item.setDoi(Methods.JsonToString(jso, "doi", ""));
                    item.setAbstract_(Methods.JsonToString(jso, "abstract", ""));
                    item.setDate_published(Methods.JsonToString(jso, "date_published", ""));
                    item.setSubmission_id(Methods.JsonToString(jso, "submission_id", ""));
                    item.setSection_id(Methods.JsonToString(jso, "section_id", ""));
                    item.setSeq(Methods.JsonToString(jso, "seq", ""));
                    item.setGaleys(Methods.JsonToString(jso, "galeys", ""));
                    JsonArray jarrKeywords = Methods.JsonToArray(jso, "keywords");
                    String keys = "";
                    for (int i = 0; i < jarrKeywords.size(); i++) {
                        JsonObject keyjso = Methods.JsonElementToJSO(jarrKeywords.get(i));
                        String minkey = Methods.JsonToString(keyjso, "keyword", "");
                        keys += minkey;
                        if (i < jarrKeywords.size() - 1) {
                            keys += minkey.length() > 0 ? ", " : "";
                        }
                    }
                    MyLogs.error("acutor: "+jarrKeywords.size());

                    item.setKeywords(keys);
                    String auth = "";
                    try {
                        JsonArray jarrAutors = Methods.JsonToArray(jso, "authors");
                        for (int i = 0; i < jarrAutors.size(); i++) {
                            JsonObject keyauth = Methods.JsonElementToJSO(jarrAutors.get(i));
                            String minkey = "";
                            minkey += "nombres: "+Methods.JsonToString(keyauth, "nombres", "") + "\n";
                            minkey += "filiacion: "+Methods.JsonToString(keyauth, "filiacion", "") + "\n";
                            minkey += "email:" + Methods.JsonToString(keyauth, "email", "") + "\n";

                            auth += minkey;
                            if (i < jarrKeywords.size() - 1) {
                                auth += minkey.length() > 0 ? "\n" : "";
                            }
                        }
                    }catch (Exception e){
                        MyLogs.error("[Autthos list]: " + e.getMessage());
                    }
                    item.setAuthors(auth);
                    //agrega el item a la lista
                    data.add(item);
                }
            }
        } else {
            //Toast indicando la ausencia de elementos
            Alerts.MessageToast(PubsActivity.this, "No hay registros.");
        }
        //retorno de la lista con todos los elementos
        return data;
    }

    public void back(View view){
        onBackPressed();
    }
}