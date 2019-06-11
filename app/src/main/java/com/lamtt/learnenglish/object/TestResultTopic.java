package com.lamtt.learnenglish.object;

public class TestResultTopic {
    String tag;
    int numPass, numNotPass;

    public TestResultTopic(String tag, int numPass, int numNotPass) {
        this.tag = tag;
        this.numPass = numPass;
        this.numNotPass = numNotPass;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getNumPass() {
        return numPass;
    }

    public void setNumPass(int numPass) {
        this.numPass = numPass;
    }

    public int getNumNotPass() {
        return numNotPass;
    }

    public void setNumNotPass(int numNotPass) {
        this.numNotPass = numNotPass;
    }
}
