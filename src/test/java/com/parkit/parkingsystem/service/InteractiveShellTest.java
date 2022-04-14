package com.parkit.parkingsystem.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class InteractiveShellTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Test
    @DisplayName("Test loadMenu()")
    public void helloUser() throws Exception{
        String input = "test";
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
    @DisplayName("Test when user presses 1")
    public void case1InLoadMenuTest() throws Exception {
        String input = "1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        System.setOut(new PrintStream(outContent));

        InteractiveShell.loadInterface();
        in.close();
    }

    @Test
    @DisplayName("Test when user presses 2")
    public void case2InLoadMenuTest() throws Exception {
        String input = "2\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        System.setOut(new PrintStream(outContent));

        InteractiveShell.loadInterface();
        in.close();
    }

    @Test
    @DisplayName("Test when user presses 3")
    public void menuTest() throws Exception {
        String input = "3\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        System.setOut(new PrintStream(outContent));

        InteractiveShell.loadInterface();
        in.close();
    }
    @Test
    @DisplayName("Test when user presses 4 = wrong number")
    public void nullInLoadMenuTest() throws Exception {
        String input = "4\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        System.setOut(new PrintStream(outContent));

        InteractiveShell.loadInterface();

        assertTrue(outContent.toString().contains("Unsupported option. Please enter a number corresponding to the provided menu"));
        in.close();
    }
}