package com.lamtt.learnenglish.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.lamtt.learnenglish.fragment.PhraseItemFragment;
import com.lamtt.learnenglish.object.Phrase;

import java.util.List;

public class PageAdapter extends FragmentStatePagerAdapter {

    List<Phrase> phraseList;

    public PageAdapter(FragmentManager fm, List<Phrase> phraseList) {
        super(fm);
        this.phraseList = phraseList;
    }

    @Override
    public Fragment getItem(int i) {
        return new PhraseItemFragment(phraseList.get(i));
    }

    @Override
    public int getCount() {
        return phraseList.size();
    }
}
