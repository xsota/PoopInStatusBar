package com.xsota.poop.statusbarinpoop;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MachoActivity extends AppCompatActivity {

  int poopCount = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_macho);

    ButterKnife.bind(this);
  }

  @OnClick(R.id.poop)
  public void addPoop(){

    //Log.d("addPoop", "うんこがタップされたよ");

    Notification notification = new Notification.Builder(this)
        .setContentTitle("ぶりっ")
        .setContentText("(うんちの音)")
        //.setAutoCancel(true)
        .setSmallIcon(R.drawable.poopicon)
        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.poop))
        .build();

    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    notificationManager.notify(poopCount++, notification);
  }
}
