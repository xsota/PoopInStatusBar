package com.xsota.poop.poopinstatusbar;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;

public class MachoActivity extends AppCompatActivity {
  @Bind(R.id.twitter_login_button) public TwitterLoginButton loginButton;
  @Bind(R.id.twitter_logout_button) public Button logoutButton;


  int poopCount = 0;

  private boolean isTwitterLogin = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    TwitterAuthConfig authConfig = new TwitterAuthConfig(getResources().getString(R.string.apiKey), getResources().getString(R.string.apiSecret));
    Fabric.with(this, new Twitter(authConfig));
    setContentView(R.layout.activity_macho);

    ButterKnife.bind(this);

    // ログインしてたらログインボタン非表示にする
    if (Twitter.getSessionManager().getActiveSession() == null) {
      logoutButton.setVisibility(View.GONE);
      loginButton.setCallback(new Callback<TwitterSession>() {
        @Override
        public void success(Result<TwitterSession> result) {
          TwitterSession session = result.data;
          String userName = "@" + session.getUserName();
          Toast.makeText(getApplicationContext(), userName+"さん、こんにちは。", Toast.LENGTH_LONG).show();
          isTwitterLogin = true;
        }

        @Override
        public void failure(TwitterException exception) {
          Toast.makeText(getApplicationContext(), "Twitterログイン失敗", Toast.LENGTH_LONG).show();
        }
      });
    } else {
      loginButton.setVisibility(View.GONE);
      isTwitterLogin = true;
    }



  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    loginButton.onActivityResult(requestCode, resultCode, data);
  }


  @OnClick(R.id.poop)
  public void addPoop(){
    if (isTwitterLogin){
      tweet();
    }

    //Log.d("addPoop", "うんこがタップされたよ");

    Notification notification = new Notification.Builder(this)
        .setContentTitle("ぶりっ")
        .setContentText("(うんちの音)")
        //.setAutoCancel(true)
        .setSmallIcon(R.drawable.poopicon)
        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.poopiconlarge))
        .build();

    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    notificationManager.notify(poopCount++, notification);
  }

  private void tweet() {
    String[] messages = {"ぶりぶり", "うんち「出た」", "ぶりっ", "(うんちの音)", "ぶぶりぶりりぶりぶりりりりり", "うんちでだよ！", "うんち〜〜〜"};
    int unko  = ((Random) new Random()).nextInt(messages.length);

    TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
    StatusesService statusesService = twitterApiClient.getStatusesService();

    statusesService.update(messages[unko], null, false, null, null, null, false, null, null, new Callback<Tweet>() {
      @Override
      public void success(Result<Tweet> result) {
        Toast.makeText(getApplicationContext(), "Twitterにもうんちしたよ", Toast.LENGTH_SHORT).show();

      }
      public void failure(TwitterException exception) {
        Toast.makeText(getApplicationContext(), "便秘でTwitterにうんち出なかったよ", Toast.LENGTH_SHORT).show();

      }
    });
  }

  private void setUpLoginButton(){
    loginButton.setCallback(new Callback<TwitterSession>() {
      @Override
      public void success(Result<TwitterSession> result) {
        TwitterSession session = result.data;
        String userName = "@" + session.getUserName();
        Toast.makeText(getApplicationContext(), userName+"さん、こんにちは。", Toast.LENGTH_LONG).show();
        loginButton.setVisibility(View.GONE);
        logoutButton.setVisibility(View.VISIBLE);
        isTwitterLogin = true;
      }

      @Override
      public void failure(TwitterException exception) {
        Toast.makeText(getApplicationContext(), "Twitterログイン失敗", Toast.LENGTH_LONG).show();
      }
    });
  }

  @OnClick(R.id.twitter_logout_button)
  public void logoutTwitter(){
    Twitter.getSessionManager().clearActiveSession();
    Twitter.logOut();
    isTwitterLogin=false;
    logoutButton.setVisibility(View.GONE);
    loginButton.setVisibility(View.VISIBLE);
    setUpLoginButton();
  }

}
