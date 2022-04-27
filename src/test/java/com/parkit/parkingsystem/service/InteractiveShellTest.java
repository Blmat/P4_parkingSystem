package com.parkit.parkingsystem.service;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

class InteractiveShellTest {

    @Test
    public void press1ToLoadMenu() throws IOException {
        String data = "1";
        InputStream in = new ByteArrayInputStream(data.getBytes());
        System.setIn(in);
        InteractiveShell.loadInterface();
        in.close();
    }
    @Test
    public void press2ToLoadMenu() throws IOException {
        String data = "2";
        InputStream in = new ByteArrayInputStream(data.getBytes());
        System.setIn(in);
        InteractiveShell.loadInterface();
        in.close();
    }

    @Test
    void press3ToLoadMenu() throws IOException {
        String data = "3";
        InputStream in = new ByteArrayInputStream(data.getBytes());
        System.setIn(in);
        InteractiveShell.loadInterface();
        in.close();
    }
}