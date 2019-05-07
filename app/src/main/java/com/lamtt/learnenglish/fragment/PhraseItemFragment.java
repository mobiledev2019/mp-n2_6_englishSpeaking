package com.lamtt.learnenglish.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lamtt.learnenglish.R;
import com.lamtt.learnenglish.object.Phrase;
import com.lamtt.learnenglish.utils.Constant;

import net.gotev.speech.GoogleVoiceTypingDisabledException;
import net.gotev.speech.Speech;
import net.gotev.speech.SpeechDelegate;
import net.gotev.speech.SpeechRecognitionNotAvailable;

import java.util.List;
import java.util.Locale;


@SuppressLint("ValidFragment")
public class PhraseItemFragment extends Fragment implements View.OnClickListener {

    private Phrase phrase;
    TextView tvEng, tvVi, tvResultSpeech;
    ProgressBar progressBar;
    Button btnSpeak;
    final String TAG = "PhraseItemFragment";
    final int REQUEST_MICROPHONE = 1997;
    TextToSpeech tts;

    public PhraseItemFragment(Phrase phrase) {
        this.phrase = phrase;
        Log.d(TAG, phrase.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "[onCreateView]");
        Speech.init(getActivity(), getActivity().getPackageName());
        Speech.getInstance().setLocale(Locale.US);
        initTTS();

        View view = inflater.inflate(R.layout.fragment_phrase_item, container, false);
        tvEng = view.findViewById(R.id.tv_Eng);
        tvVi = view.findViewById(R.id.tv_Vi);
        tvResultSpeech = view.findViewById(R.id.tv_ReultSpeech);
        progressBar = view.findViewById(R.id.progressBar);


        btnSpeak = view.findViewById(R.id.btn_speak);
        btnSpeak.setOnClickListener(this);
        tvEng.setText(phrase.getEnglish());
        tvVi.setText(phrase.getVietnamese());

        return view;
    }


    @Override
    public void onDestroy() {
        try {
            Speech.getInstance().shutdown();

            if(tts != null){
                tts.stop();
                tts.shutdown();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_speak :
                checkPermisstion();
                break;

            case R.id.imbtnListen :
                tts.speak(phrase.getEnglish(), TextToSpeech.QUEUE_FLUSH, null);
                break;

            case R.id.imbtnFavourite :

                break;
        }
    }

    private void checkPermisstion() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_MICROPHONE);
        } else {
            if (Constant.isNetWorkAvailable(getActivity())) {

                try {
                    Speech.getInstance().startListening(new SpeechDelegate() {
                        @Override
                        public void onStartOfSpeech() {
                            Log.i("speech", "speech recognition is now active");
                        }

                        @Override
                        public void onSpeechRmsChanged(float value) {
                            Log.d("speech11111", "rms is now: " + value);
                            progressBar.setProgress(Math.round(value * 7));
                        }


                        @Override
                        public void onSpeechPartialResults(List<String> results) {
                            StringBuilder str = new StringBuilder();
                            for (String res : results) {
                                str.append(res).append(" ");
                            }

                            Log.i("speech", "partial result: " + str.toString().trim());
                        }

                        @Override
                        public void onSpeechResult(String result) {
                            Log.i("speech", "result: " + result);
                            progressBar.setProgress(0);
                            if (result.length() == 0) {
                                tvResultSpeech.setText(getResources()
                                        .getString(R.string.cant_recognize_speech));
                                tvResultSpeech.setTextColor(Color.parseColor("#F44336"));
                            } else {

                                String temp = phrase.getEnglish().toLowerCase();
                                if (temp.endsWith(".") || temp.endsWith("!") || temp.endsWith("?")) {
                                    temp = temp.substring(0, temp.length() - 1);
                                }

                                if (result.toLowerCase().equals(temp)) {
                                    tvResultSpeech.setText(getResources().getString(R.string.exactly));
                                    tvResultSpeech.setTextColor(Color.parseColor("#42b309"));

                                } else {
                                    tvResultSpeech.setTextColor(Color.parseColor("#f44336"));
                                    tvResultSpeech.setText(getResources().getString(R.string.not_exactly)
                                            + " " + result);
                                }
                            }
                        }
                    });
                } catch (SpeechRecognitionNotAvailable exc) {
                    Log.e("speech", "Speech recognition is not available on this device!");
                    // You can prompt the user if he wants to install Google App to have
                    // speech recognition, and then you can simply call:
                    //
                    // SpeechUtil.redirectUserToGoogleAppOnPlayStore(this);
                    //
                    // to redirect the user to the Google App page on Play Store
                } catch (GoogleVoiceTypingDisabledException exc) {
                    Log.e("speech", "Google voice typing must be enabled!");
                }
            } else  {
                Constant.showToat(getActivity(), getString(R.string.not_connect_internet));
            }
        }
    }

    private void initTTS() {
        tts=new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                    Log.d(TAG, "[init TTS success]");
                }
            }
        });
    }
}
