package com.lamtt.learnenglish.adapter;


import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lamtt.learnenglish.R;
import com.lamtt.learnenglish.object.Test;

import java.util.ArrayList;


public class ResultAdapter extends BaseAdapter {
    final ArrayList<Test> testList;

    public ResultAdapter(ArrayList testList) {
        this.testList = testList;
    }
    @Override
    public int getCount() {
        return testList.size();
    }

    @Override
    public Object getItem(int position) {
        return testList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View viewTest;
        if (convertView == null) {
            viewTest = View.inflate(parent.getContext(), R.layout.item_result, null);
        } else {
            viewTest = convertView;
        }

        Test test = (Test) getItem(position);

        ((TextView) viewTest.findViewById(R.id.tv_idTest)).setText("Chủ đề test: " + test.getTestId());
        ((TextView) viewTest.findViewById(R.id.tv_isAnswer)).setText("Số câu hỏi: " + String.valueOf(test.getIsAnswer()));
        ((TextView) viewTest.findViewById(R.id.tv_trueAnswer)).setText("Số câu đúng: " + String.valueOf(test.getTrueAnswer()));


        return viewTest;
    }
}
