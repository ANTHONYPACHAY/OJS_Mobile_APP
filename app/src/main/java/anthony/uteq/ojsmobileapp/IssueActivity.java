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
import com.google.gson.JsonObject;
import com.mindorks.placeholderview.PlaceHolderView;

import java.util.ArrayList;
import java.util.List;

import anthony.uteq.ojsmobileapp.controller.Card_issueManager;
import anthony.uteq.ojsmobileapp.controller.Card_journlaManager;
import anthony.uteq.ojsmobileapp.models.IssueObject;
import anthony.uteq.ojsmobileapp.models.JournalObject;
import anthony.uteq.ojsmobileapp.utiles.Alerts;
import anthony.uteq.ojsmobileapp.utiles.DataStatic;
import anthony.uteq.ojsmobileapp.utiles.Methods;
import anthony.uteq.ojsmobileapp.utiles.MyLogs;

public class IssueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);
        getSupportActionBar().hide();

        onLoadIssueData();
    }

    private void onLoadIssueData(){
        Bundle bundle = this.getIntent().getExtras();
        String data = bundle.getString("journalObject");
        JsonObject jso = Methods.stringToJSON(data);
        Gson gson = new Gson();
        JournalObject journalObject = gson.fromJson(jso, JournalObject.class);

        ImageView imgJournal = (ImageView)findViewById(R.id.imgJournal);
        TextView journalName = (TextView)findViewById(R.id.journalName);
        TextView journalDescription = (TextView)findViewById(R.id.journalDescription);
        journalDescription.setMovementMethod(new ScrollingMovementMethod());


        journalName.setText(journalObject.getName());
        journalDescription.setText(Html.fromHtml(journalObject.getDescription()));
        Glide.with(IssueActivity.this).load(journalObject.getPortada()).into(imgJournal);

        getIssues(journalObject.getJournal_id());
    }

    public void createElements(List<IssueObject> elements){
        for(IssueObject elem :elements){
            PlaceHolderView CardElement = (PlaceHolderView)findViewById(R.id.journalsView);
            CardElement.addView(new Card_issueManager(this.getApplicationContext(), CardElement, elem));
        }
    }

    private void getIssues(String issue_id){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                DataStatic.gerUrlApi("issues.php?j_id=" + issue_id),
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MyLogs.info("ws todo bien");
                        //Procesar las respuesta y armar un Array con estos
                        List<IssueObject> data = processResponse(response);
                        //método para mostrar los datos en el activity
                        createElements(data);
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MyLogs.error("we error"+ error.getMessage()+ "\n"+ DataStatic.gerUrlApi("journals.php"));
                        Alerts.MessageToast(IssueActivity.this, "Data Error");
                    }
                }
        );
        queue.add(request);
    }

    private List<IssueObject> processResponse(String response){
        List<IssueObject> data = new ArrayList<>();
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
                    IssueObject item = gson.fromJson(jso.toString(), IssueObject.class);
                    //agrega el item a la lista
                    data.add(item);
                }
            }
        }else{
            //Toast indicando la ausencia de elementos
            Alerts.MessageToast(IssueActivity.this, "No hay registros.");
        }
        //retorno de la lista con todos los elementos
        return data;
    }

    public void back(View view){
        onBackPressed();
    }
}