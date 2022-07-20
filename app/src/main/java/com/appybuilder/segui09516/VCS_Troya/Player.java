package com.appybuilder.segui09516.VCS_Troya;

import java.io.Serializable;

public class Player implements Serializable {

    private int num_player;
    private String name_player;
    private String description_player;

    public Player(String name_player, int num_player, String description_player) {
        this.name_player = name_player;
        this.num_player = num_player;
        this.description_player = description_player;
    }

    public String getName_player() {
        return name_player;
    }

    public int getNum_player() {
        return num_player;
    }

    public String getDescription_player() {
        return description_player;
    }
}
