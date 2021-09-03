package anthony.uteq.ojsmobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import anthony.uteq.ojsmobileapp.models.IssueObject;
import anthony.uteq.ojsmobileapp.models.PubsObject;
import anthony.uteq.ojsmobileapp.utiles.Alerts;
import anthony.uteq.ojsmobileapp.utiles.Methods;
import anthony.uteq.ojsmobileapp.utiles.MyLogs;

public class PubsViewActivity extends AppCompatActivity {

    public PubsObject pubsObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pubs_view);
        loadData();
    }

    private void loadData(){
        Bundle bundle = this.getIntent().getExtras();
        String data = bundle.getString("pubsObject");
        JsonObject jso = Methods.stringToJSON(data);
        Gson gson = new Gson();
        PubsObject pubsObject = gson.fromJson(jso, PubsObject.class);


        TextView title = (TextView) findViewById(R.id.title);
        TextView section = (TextView) findViewById(R.id.section);
        TextView date_published = (TextView) findViewById(R.id.date_published);
        TextView journalDescription = (TextView) findViewById(R.id.journalDescription);
        TextView author = (TextView) findViewById(R.id.author);
        journalDescription.setMovementMethod(new ScrollingMovementMethod());
        author.setMovementMethod(new ScrollingMovementMethod());

        title.setText(pubsObject.getTitle());
        section.setText(pubsObject.getSection());
        date_published.setText(pubsObject.getDate_published());
        journalDescription.setText(Html.fromHtml(pubsObject.getAbstract_()));
        author.setText(pubsObject.getAuthors());

        MyLogs.info(data);

        this.pubsObject = pubsObject;
    }


    public void back(View view){
        Alerts.openBrowser(this.pubsObject.getDoi(), PubsViewActivity.this);
    }
}