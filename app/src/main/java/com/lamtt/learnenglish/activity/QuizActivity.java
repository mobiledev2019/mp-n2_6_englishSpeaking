package com.lamtt.learnenglish.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lamtt.learnenglish.R;
import com.lamtt.learnenglish.database.DatabaseFirebase;
import com.lamtt.learnenglish.database.DatabaseHelper;
import com.lamtt.learnenglish.object.Phrase;
import com.lamtt.learnenglish.utils.Constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnCheck;
    private ImageButton btnListen;
    private int fistQtion;
    private int fourthQtion;
    private List<Integer> intRandomList;
    private String mChoice = "";
    private Random random;
    private int secondQtion;
    private int thirdQtion;
    private TextView tvAnswer1;
    private TextView tvAnswer2;
    private TextView tvAnswer3;
    private TextView tvAnswer4;
    private TextView tvHandleResult;
    private TextView tvQuestion, tvPinyin;
    private int idCategory = 0;
    private int isAnswer = 0;
    private int rightAnswer = 0;
    private List<Phrase> phraseList = new ArrayList<>();
    private String tag = "diadiem";
    DatabaseHelper databaseHelper;
    public static List<Integer> scentenceExitList = new ArrayList<>();
    final String TAG = "QuizActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        getSupportActionBar().setTitle("Quiz");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        databaseHelper = new DatabaseHelper(this);
        tag = getIntent().getStringExtra(Constant.CATEGORY_TAG);

        if (tag.equals(Constant.RANDOM_QUIZ_TAG)) {
            Log.d(TAG, "[random quiz]");
            phraseList = databaseHelper.randomListQuiz();
        } else if (tag.equals("yeuthich")){
            phraseList = databaseHelper.getListFavourite();
        } else {
            Log.d(TAG, "[topic quiz]");
            phraseList = databaseHelper.getListPhraseByTag(tag);
            idCategory = databaseHelper.getIdByTag(tag);
            Log.d(TAG, "[idCategory]_" + idCategory);

        }
        Log.d(TAG, "Size : " + phraseList.size());

        this.random = new Random();
        this.btnCheck = (Button) findViewById(R.id.btnCheckPractive);
        this.btnCheck.setOnClickListener(this);
        this.btnListen = (ImageButton) findViewById(R.id.btnListen);
        this.btnListen.setOnClickListener(this);
        this.tvPinyin = findViewById(R.id.tv_pinyin);
        this.tvQuestion = (TextView) findViewById(R.id.tv_question);
        this.tvAnswer1 = (TextView) findViewById(R.id.tv_answer1);
        this.tvAnswer2 = (TextView) findViewById(R.id.tv_answer2);
        this.tvAnswer3 = (TextView) findViewById(R.id.tv_answer3);
        this.tvAnswer4 = (TextView) findViewById(R.id.tv_answer4);
        this.tvAnswer1.setOnClickListener(this);
        this.tvAnswer2.setOnClickListener(this);
        this.tvAnswer3.setOnClickListener(this);
        this.tvAnswer4.setOnClickListener(this);
        this.tvHandleResult = (TextView) findViewById(R.id.tv_handleReult);
        this.tvHandleResult.setVisibility(View.GONE);
        this.btnCheck.setText(getResources().getString(R.string.check));

        setQuestion();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCheckPractive:
                if (this.mChoice.length() == 0) {
                    Constant.showToat(this, getResources().getString(R.string.no_answer_selected));
                } else if (this.mChoice.equals(
                        (phraseList.get(this.fistQtion)).getVietnamese())) {
                    handleAnswerQuestion(true);
                } else {
                    handleAnswerQuestion(false);
                }
                return;
            case R.id.btnListen:

                return;
            case R.id.tv_answer1:
                clickView(this.tvAnswer1, this.tvAnswer2, this.tvAnswer3, this.tvAnswer4);
                return;
            case R.id.tv_answer2:
                clickView(this.tvAnswer2, this.tvAnswer1, this.tvAnswer3, this.tvAnswer4);
                return;
            case R.id.tv_answer3:
                clickView(this.tvAnswer3, this.tvAnswer1, this.tvAnswer2, this.tvAnswer4);
                return;
            case R.id.tv_answer4:
                clickView(this.tvAnswer4, this.tvAnswer1, this.tvAnswer2, this.tvAnswer3);
                return;
            default:
                return;
        }

    }

    private void resetBackground() {
        this.tvAnswer1.setBackgroundColor(0);
        this.tvAnswer2.setBackgroundColor(0);
        this.tvAnswer3.setBackgroundColor(0);
        this.tvAnswer4.setBackgroundColor(0);
        tvPinyin.setText("");
    }


    private void setQuestion() {

        int number = phraseList.size();
        resetBackground();

        this.btnCheck.setBackground(getResources().getDrawable(R.drawable.bg_button_is_data_null));
        do {
            this.fistQtion = this.random.nextInt(number);
        } while (checkVocaExist(this.fistQtion));
        this.tvQuestion.setText((phraseList.get(this.fistQtion)).getEnglish());

        scentenceExitList.add(Integer.valueOf(this.fistQtion));

        do {
            this.secondQtion = this.random.nextInt(number);
        } while (this.secondQtion == this.fistQtion);
        while (true) {
            this.thirdQtion = this.random.nextInt(number);
            if (this.thirdQtion != this.fistQtion && this.thirdQtion != this.secondQtion) {
                break;
            }
        }
        while (true) {
            this.fourthQtion = this.random.nextInt(number);
            if (this.fourthQtion != this.fistQtion && this.fourthQtion != this.secondQtion && this.fourthQtion != this.thirdQtion) {
                break;
            }
        }
        this.intRandomList = new ArrayList();
        this.intRandomList.add(Integer.valueOf(this.fistQtion));
        this.intRandomList.add(Integer.valueOf(this.secondQtion));
        this.intRandomList.add(Integer.valueOf(this.thirdQtion));
        this.intRandomList.add(Integer.valueOf(this.fourthQtion));

        this.tvQuestion.setVisibility(View.VISIBLE);

        Collections.shuffle(this.intRandomList);

        this.tvAnswer1.setText(phraseList.get(intRandomList.get(0)).getVietnamese());
        this.tvAnswer2.setText(phraseList.get(intRandomList.get(1)).getVietnamese());
        this.tvAnswer3.setText(phraseList.get(intRandomList.get(2)).getVietnamese());
        this.tvAnswer4.setText(phraseList.get(intRandomList.get(3)).getVietnamese());
        this.intRandomList.clear();
    }

    public static boolean checkVocaExist(int pos) {
        for (int i = 0; i < scentenceExitList.size(); i++) {
            if (pos == ((Integer) scentenceExitList.get(i)).intValue()) {
                return true;
            }
        }
        return false;
    }

    private void clickView(TextView tvChoice, TextView r1, TextView r2, TextView r3) {
        tvChoice.setBackground(getResources().getDrawable(R.drawable.view_selected));
        r1.setBackgroundColor(0);
        r2.setBackgroundColor(0);
        r3.setBackgroundColor(0);
        this.mChoice = tvChoice.getText().toString();
        this.btnCheck.setBackground(getResources().getDrawable(R.drawable.bg_button));
    }

    private void handleAnswerQuestion(boolean b) {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_result_practive_1);
        TextView tvKhongphaila = (TextView) dialog.findViewById(R.id.tv_khongphaila);
        TextView tvCorrect = (TextView) dialog.findViewById(R.id.tvCorrect);
        TextView tvRSMean = (TextView) dialog.findViewById(R.id.tvResultMean);
        ImageView image = (ImageView) dialog.findViewById(R.id.iv_result);
        ((TextView) dialog.findViewById(R.id.tvResultWord)).setText(
                phraseList.get(fistQtion).getEnglish());
        tvRSMean.setText(phraseList.get(fistQtion).getVietnamese());
        if (b) {
            tvCorrect.setTextColor(getResources().getColor(R.color.green));
            tvCorrect.setText(getResources().getString(R.string.correct));
            rightAnswer++;
        } else {
            tvCorrect.setTextColor(getResources().getColor(R.color.red));
            tvCorrect.setText(getResources().getString(R.string.not_correct));
        }
        ((Button) dialog.findViewById(R.id.btnContinues)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mChoice = "";
                if (phraseList.size() == scentenceExitList.size()) {
                    //Insert result Test vào DB
                    new AlertDialog.Builder(QuizActivity.this)
                            .setTitle("Finish")
                            .setMessage("Result : " + rightAnswer + "/" + phraseList.size())
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setNeutralButton("Again", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    scentenceExitList.clear();
                                    rightAnswer = 0;
                                    isAnswer = 0;
                                    setQuestion();
                                }
                            }).show();
                    insertResult();

                } else {
                    isAnswer++;
                    setQuestion();
                }
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    private void insertResult() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userUid = user.getUid();
        DatabaseFirebase dbfb = new DatabaseFirebase();
        dbfb.insertResultTest(userUid, isAnswer + 1, rightAnswer, tag);

        //todo getObject testResultTopic


        //todo update testTopicResult
        if (rightAnswer * 1.0 / phraseList.size() > 0.5) {
            // numpass += 1

        }else {
            // numNotpass += 1


        }

        //update stautus active
        if (rightAnswer * 1.0 / phraseList.size() > 0.1) {
            databaseHelper.updateCategory(idCategory + 1, 1);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        phraseList.clear();
        scentenceExitList.clear();
        rightAnswer = 0;
        isAnswer = 0;
    }
}
