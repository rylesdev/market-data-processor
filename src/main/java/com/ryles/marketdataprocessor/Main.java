package com.ryles.marketdataprocessor;

import com.ryles.marketdataprocessor.controller.Controller;

public class Main {

    public static void main(String[] args) {
        try {
            Controller controller = new Controller();
            controller.start();
        } catch(Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
