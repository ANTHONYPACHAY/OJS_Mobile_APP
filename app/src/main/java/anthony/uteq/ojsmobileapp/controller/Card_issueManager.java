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

import anthony.uteq.ojsmobileapp.IssueActivity;
import anthony.uteq.ojsmobileapp.PubsActivity;
import anthony.uteq.ojsmobileapp.R;
import anthony.uteq.ojsmobileapp.models.IssueObject;
import anthony.uteq.ojsmobileapp.models.JournalObject;
import anthony.uteq.ojsmobileapp.utiles.MyLogs;

@NonReusable
@Animate(Animate.CARD_TOP_IN_DESC)
@Layout(R.layout.card_issue)
public class Card_issueManager {

    @View(R.id.issueCardElement)
    CardView card;

    @View(R.id.imgIssue)
    public ImageView imgIssue;

    @View(R.id.issueTittle)
    public TextView issueTitlecard;

    @View(R.id.issueNumercard)
    public TextView issueNumercard;

    @View(R.id.issueYearcard)
    public TextView issueYearcard;

    @View(R.id.issueDoicard)
    public TextView issueDoicard;

    @View(R.id.issueDatecard)
    public TextView issueDatecard;

    @View(R.id.issueVolumencard)
    public TextView issueVolumencard;

    public IssueObject issueObject;
    public Context Ctx;
    public PlaceHolderView thisPlaceHolderElement;

    public Card_issueManager(Context context, PlaceHolderView placeHolderView, IssueObject issueObject) {
        this.Ctx = context;
        this.thisPlaceHolderElement = placeHolderView;
        this.issueObject = issueObject;
    }

    @Resolve
    public void onResolved() {
        issueTitlecard.setText(this.issueObject.getTitle());
        issueNumercard.setText("Num: "+this.issueObject.getNumber()+ "\t");
        issueVolumencard.setText("Vol. "+this.issueObject.getVolume()+ "\t");
        issueDoicard.setText(this.issueObject.getDoi());
        issueYearcard.setText("año: " + this.issueObject.getYear());
        issueDatecard.setText(this.issueObject.getDate_published());

        //renderiza img
        Glide.with(Ctx).load(this.issueObject.getCover()).into(imgIssue);
        //añadir animación a la tarjeta
        card.setAnimation(AnimationUtils.loadAnimation(Ctx, R.anim.rigth_to_left));
    }

    @Click(R.id.issueCardElement)
    public void onLongClick(){
        MyLogs.info("click en la tarjeta: "+ this.issueObject.getIssue_id());
        viewIssue();
    }

    public void viewIssue(){
        Intent intent = new Intent(Ctx, PubsActivity.class);
        Bundle b = new Bundle();
        Gson gson = new Gson();
        String json = gson.toJson(this.issueObject);
        b.putString("issueObject", json);
        intent.putExtras(b);
        Ctx.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
