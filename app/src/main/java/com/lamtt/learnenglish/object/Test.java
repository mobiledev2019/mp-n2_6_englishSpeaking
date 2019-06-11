package com.lamtt.learnenglish.object;

public class Test {
    private String testId;
    private int isAnswer;
    private int trueAnswer;
    private long time; // Đơn vị là (s)

    public Test() {

    }

    public Test(String testId, int isAnswer, int trueAnswer, long time) {
        this.testId = testId;
        this.isAnswer = isAnswer;
        this.trueAnswer = trueAnswer;
        this.time = time;
    }

    public String getTestId() {
        return testId;
    }

    public int getIsAnswer() {
        return isAnswer;
    }

    public int getTrueAnswer() {
        return trueAnswer;
    }


    public long getTime() {
        return time;
    }
}
