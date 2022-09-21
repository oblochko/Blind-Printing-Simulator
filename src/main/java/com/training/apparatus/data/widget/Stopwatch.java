package com.training.apparatus.data.widget;

public class Stopwatch {
    private long timer_s;
    private long timer_r;
    private long timer_f;

    public Stopwatch() {

    }

    public void start() {
        timer_s = System.currentTimeMillis();
        timer_f = timer_s;
    }

    public void stop() {
        timer_f = System.currentTimeMillis();
        timer_r = timer_f - timer_s;
    }

    public double getResultSec() {
        return (double)timer_r / 1000;
    }

    public double  getResultMin() {
        return (double)timer_r / 1000 / 60;
    }
}
