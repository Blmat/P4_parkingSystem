package com.parkit.parkingsystem.service;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class InteractiveShellTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Test
    public void menuTest() throws Exception {
        String input = "3\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        System.setOut(new PrintStream(outContent));

        InteractiveShell.loadInterface();

        assertTrue(outContent.toString().contains("Welcome to Parking System!"));
        assertTrue(outContent.toString().contains("Please select an option. Simply enter the number to choose an action"));
        assertTrue(outContent.toString().contains("1 New Vehicle Entering - Allocate Parking Space"));
        assertTrue(outContent.toString().contains("2 Vehicle Exiting - Generate Ticket Price"));
        assertTrue(outContent.toString().contains("3 Shutdown System"));

        in.close();
    }
    @Test
    public void nullInLoadMenuTest() throws Exception {
        String input ="4\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        System.setOut(new PrintStream(outContent));

        InteractiveShell.loadInterface();

        assertTrue(outContent.toString().contains("Unsupported option. Please enter a number corresponding to the provided menu"));

        in.close();
    }
}