package com.lamtt.learnenglish.object;

public class Test {
    private int testId;
    private int numberQuestions;
    private int trueAnswers;
    private long time; // Đơn vị là (s)

    public Test(int testId, int numberQuestions, int trueAnswers, long time) {
        this.testId = testId;
        this.numberQuestions = numberQuestions;
        this.trueAnswers = trueAnswers;
        this.time = time;
    }

    public int getTestId() {
        return testId;
    }

    public int getNumberQuestions() {
        return numberQuestions;
    }

    public int getTrueAnswers() {
        return trueAnswers;
    }


    public long getTime() {
        return time;
    }
}
