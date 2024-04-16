package com.xmut.onlinejudge.entity;

import lombok.Data;

@Data
public class TaskCase {
    private String input;
    private String output;

    public TaskCase(String input, String output) {
        this.input = input;
        this.output = output;
    }
}
