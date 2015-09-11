package com.miniproject2.soma.miniproject02;

/**
 * Created by jiyoungpark on 15. 9. 5..
 */
public class Data {

    String word;
    String mean;
    int count;
    String time;

    public Data() {
        this.word = null;
        this.mean = null;
        this.count = 1;
        this.time = null;
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

    public void setTime(String time) {
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

    public String getTime() {
        return this.time;
    }

    public int increaseCount() {
        return ++count;
    }

    @Override
    public String toString() {
        return String.format("%s\t%s\t%d\t%s\n", word, mean, count, time);
    }
}
