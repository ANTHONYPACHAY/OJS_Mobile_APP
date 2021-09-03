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
import anthony.uteq.ojsmobileapp.R;
import anthony.uteq.ojsmobileapp.models.JournalObject;
import anthony.uteq.ojsmobileapp.utiles.MyLogs;

@NonReusable
@Animate(Animate.CARD_TOP_IN_DESC)
@Layout(R.layout.card_journal)
public class Card_journlaManager {
    @View(R.id.journalCardElement)
    CardView card;

    @View(R.id.imgpportada)
    public ImageView imageView;

    @View(R.id.journalName)
    public TextView journalName;

    @View(R.id.journalAbbreviation)
    public TextView journalAbbreviation;

    public JournalObject journalObject;
    public Context Ctx;
    public PlaceHolderView thisPlaceHolderElement;

    public Card_journlaManager(Context context, PlaceHolderView placeHolderView, JournalObject journalObject) {
        this.Ctx = context;
        this.thisPlaceHolderElement = placeHolderView;
        this.journalObject = journalObject;
    }

    @Resolve
    public void onResolved() {
        journalName.setText(this.journalObject.getName());
        journalAbbreviation.setText(this.journalObject.getAbbreviation());

        Glide.with(Ctx).load(journalObject.getPortada()).into(imageView);
        this.card.setAnimation(AnimationUtils.loadAnimation(Ctx, R.anim.rigth_to_left));
    }

    @Click(R.id.journalCardElement)
    public void onLongClick(){
        MyLogs.info("click en la tarjeta: "+ this.journalObject.getJournal_id());
        viewJournal();
    }

    public void viewJournal(){
        Intent intent = new Intent(Ctx, IssueActivity.class);
        Bundle b = new Bundle();
        Gson gson = new Gson();
        String json = gson.toJson(this.journalObject);
        b.putString("journalObject", json);
        intent.putExtras(b);
        Ctx.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
