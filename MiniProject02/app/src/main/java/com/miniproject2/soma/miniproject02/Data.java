package com.miniproject2.soma.miniproject02;

/**
 * Created by jiyoungpark on 15. 9. 5..
 */
public class Data {

    String word;
    String mean;
    int count;

    public Data() {
        this.word = null;
        this.mean = null;
        this.count = 0;
    }


    public void setWord(String word) {
        this.word = word;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getWord() {
        return word;
    }

    public String getMean() {
        return mean;
    }

    public int getCount() {
        return count;
    }

    public int increaseCount() {
        return ++count;
    }
}
