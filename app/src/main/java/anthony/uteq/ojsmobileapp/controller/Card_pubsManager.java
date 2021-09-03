package anthony.uteq.ojsmobileapp.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.mindorks.placeholderview.PlaceHolderView;
import com.mindorks.placeholderview.annotations.Animate;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

import anthony.uteq.ojsmobileapp.PubsActivity;
import anthony.uteq.ojsmobileapp.PubsViewActivity;
import anthony.uteq.ojsmobileapp.R;
import anthony.uteq.ojsmobileapp.models.IssueObject;
import anthony.uteq.ojsmobileapp.models.PubsObject;
import anthony.uteq.ojsmobileapp.utiles.Alerts;
import anthony.uteq.ojsmobileapp.utiles.MyLogs;

@NonReusable
@Animate(Animate.CARD_TOP_IN_DESC)
@Layout(R.layout.card_pubs)
public class Card_pubsManager {

    @View(R.id.pubsCardElement)
    CardView card;

    @View(R.id.pubstitle)
    public TextView pubstitle;

    @View(R.id.pubssection)
    public TextView pubssection;

    @View(R.id.pubsdate_published)
    public TextView pubsdate_published;

    public PubsObject pubsObject;
    public Context Ctx;
    public PlaceHolderView thisPlaceHolderElement;

    public Card_pubsManager(Context context, PlaceHolderView placeHolderView, PubsObject pubsObject) {
        this.Ctx = context;
        this.thisPlaceHolderElement = placeHolderView;
        this.pubsObject = pubsObject;
    }

    @Resolve
    public void onResolved() {
        pubstitle.setText(this.pubsObject.getTitle());
        pubssection.setText("Secction: "+this.pubsObject.getSection()+ "\t");
        pubsdate_published.setText(this.pubsObject.getDate_published());

        //añadir animación a la tarjeta
        card.setAnimation(AnimationUtils.loadAnimation(Ctx, R.anim.rigth_to_left));
    }

    @Click(R.id.pubsCardElement)
    public void onLongClick(){
        MyLogs.info("pubsObject: "+ this.pubsObject.getPublication_id());
        //Alerts.openBrowser(this.pubsObject.getDoi(), Ctx);
        viewPubs();
    }

    public void viewPubs(){
        Intent intent = new Intent(Ctx, PubsViewActivity.class);
        Bundle b = new Bundle();
        Gson gson = new Gson();
        String json = gson.toJson(this.pubsObject);
        b.putString("pubsObject", json);
        intent.putExtras(b);
        Ctx.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
