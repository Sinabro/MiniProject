package com.miniproject2.soma.miniproject02;

/**
 * Created by jiyoungpark on 15. 9. 5..
 */
public class Data {

    String word;
    String mean;
    int count;
    long time;

    public Data() {
        this.word = null;
        this.mean = null;
        this.count = 0;
        this.time = 0;
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

    public void setTime(long time) {
        this.time = time;
    }

    public String getWord() {
        return this.word;
    }

    public String getMean() {
        return this.mean;
    }

    public int getCount() {
        return this.count;
    }

    public long getTime() {
        return this.time;
    }

    public int increaseCount() {
        return ++count;
    }
}
