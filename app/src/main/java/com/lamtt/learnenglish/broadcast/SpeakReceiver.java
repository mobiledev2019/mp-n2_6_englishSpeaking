package com.lamtt.learnenglish.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import com.lamtt.learnenglish.utils.Constant;

import java.util.Locale;

public class SpeakReceiver extends BroadcastReceiver {
    private TextToSpeech textToSpeech;

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d("SpeakReceiver", intent.getAction());
        if (intent.getAction().equals(Constant.BROADCAST_SPEAK)){
            final String text = intent.getStringExtra(Constant.BROADCAST_TEXT_SPEAK);
            Log.d("SpeakReceiver", "[text] : " + text);
            textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        int ttsLang = textToSpeech.setLanguage(Locale.US);

                        if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                                || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                            Log.e("TTS", "The Language is not supported!");
                        } else {
                            Log.i("TTS", "Language Supported.");
                            int speechStatus = textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                            if (speechStatus == TextToSpeech.ERROR) {
                                Log.e("TTS", "Error in converting Text to Speech!");
                            }
                        }
                        Log.i("TTS", "Initialization success.");
                    } else {
                        Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}
