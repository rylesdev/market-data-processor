package com.ryles.marketdataprocessor;

import com.ryles.marketdataprocessor.controller.Controller;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        Controller controller = new Controller();
        controller.start();
    }
}
