package com.lamtt.learnenglish.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.lamtt.learnenglish.R;
import com.lamtt.learnenglish.database.DatabaseHelper;
import com.lamtt.learnenglish.object.Category;
import com.lamtt.learnenglish.object.Phrase;
import com.lamtt.learnenglish.utils.Constant;

import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ServiceNotice extends IntentService implements TextToSpeech.OnInitListener {

    private final String TAG = "ServiceNotice";
    private static DatabaseHelper databaseHelper;
    private TextToSpeech textToSpeech;
    int status;
    int ttsLanguage;

    public ServiceNotice() {
        super(ServiceNotice.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.textToSpeech = new TextToSpeech(getApplicationContext(), this);
        if (this.textToSpeech != null) {Log.d("TTS", "not null");}
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        final String textSpeak = intent.getStringExtra(Constant.BROADCAST_TEXT_SPEAK);
        if (textSpeak != null) {
            if (this.status == TextToSpeech.SUCCESS) {
                this.ttsLanguage = textToSpeech.setLanguage(Locale.US);
                switch(this.ttsLanguage) {
                    case TextToSpeech.LANG_MISSING_DATA:
                        Log.d("TTS", "error: LANG_MISSING_DATA");
                        break;
                    case TextToSpeech.LANG_NOT_SUPPORTED:
                        Log.d("TTS", "error: LANG_NOT_SUPPORTED");
                        break;
                }
            } else {
                Log.d("TTS", "unable to initialize TTS");
            }
            textToSpeech.setEngineByPackageName(getPackageName());
            textToSpeech.speak(textSpeak, TextToSpeech.QUEUE_FLUSH, null, null);

        } else {

            databaseHelper = new DatabaseHelper(this);
            List<Category> categoryList = databaseHelper.getAllCategory();
            int randomCate = new Random().nextInt(categoryList.size() - 1) + 1;
            List<Phrase> phraseList =
                    databaseHelper.getListPhraseByTag(categoryList.get(randomCate).getTag());
            int randomPhrase = new Random().nextInt(phraseList.size());
            Phrase phrase = phraseList.get(randomPhrase);
            Log.d(TAG, phrase.toString());

            Intent speakIntent = new Intent(this, ServiceNotice.class);
            speakIntent.putExtra(Constant.BROADCAST_TEXT_SPEAK, phrase.getEnglish());

            PendingIntent pi = PendingIntent.getService(this, 0,
                    speakIntent, 0);

            RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_notification);
            contentView.setTextViewText(R.id.tvPharse, phrase.getEnglish());
            contentView.setTextViewText(R.id.tvMean, phrase.getVietnamese());
            contentView.setOnClickPendingIntent(R.id.imbtnSpeak, pi);

            Notification notification = new NotificationCompat.Builder(this)
                    .setOngoing(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContent(contentView)
                    .build();
            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notification);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("method", "onDestroy()");
        textToSpeech.shutdown();
    }

    @Override
    public void onInit(int status) {
        Log.d("method", "onInit()");
        this.status = status;
    }
}
