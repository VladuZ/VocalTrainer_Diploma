package com.example.testing;

import java.util.List;

public class Exercise {
    private String name;
    private int bpm;

    private List<ViewGrid.Cell> cells;

    public Exercise(String name, int bpm, List<ViewGrid.Cell> cells) {
        this.name = name;
        this.bpm = bpm;
        this.cells = cells;
    }

    public String getName() {
        return name;
    }

    public int getBpm() {
        return bpm;
    }

    public List<ViewGrid.Cell> getCells(){
        return cells;
    }
}
