package com.luminos.test;

import com.luminos.Engine;
import com.luminos.GameLogic;

public class Main {
 
    public static void main(String[] args) {
        try {
            boolean vSync = true;
            GameLogic gameLogic = new DummyGame();
            Engine gameEng = new Engine("GAME", 600, 480, vSync, gameLogic);
            gameEng.start();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}