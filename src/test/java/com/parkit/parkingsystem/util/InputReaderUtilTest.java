package com.parkit.parkingsystem.util;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InputReaderUtilTest {
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() {
        inputReaderUtil = new InputReaderUtil();
    }

    @Test
    @DisplayName("Test the readSelection() method when the input is correct")
    public void InputCanBeParseToInt() throws IOException {
        String data = "1";
        InputStream in = new ByteArrayInputStream(data.getBytes());
        System.setIn(in);

        assertEquals(1, inputReaderUtil.readSelection());
    }

    @Test
    @DisplayName("Test the readSelection() method when the input is empty")
    public void InputIsEmpty() {
        String data = "";
        InputStream in = new ByteArrayInputStream(data.getBytes());
        System.setIn(in);

        assertEquals(-1, inputReaderUtil.readSelection());
    }

    @Test
    @DisplayName("Test the readVehicleRegistrationNumber() method when the input is correct")
    public void VehicleRegNumberIsCorrect() throws Exception {
        String data = "12345AZER";
        InputStream in = new ByteArrayInputStream(data.getBytes());
        System.setIn(in);

        assertEquals("12345AZER", inputReaderUtil.readVehicleRegistrationNumber());
    }

    @Test
    @DisplayName("Test the readVehicleRegistrationNumber() method when the input is empty")
    public void VehicleRegNumberIsEmpty() {
        String data = "";
        InputStream in = new ByteArrayInputStream(data.getBytes());
        System.setIn(in);

        assertThrows(NoSuchElementException.class, () -> inputReaderUtil.readVehicleRegistrationNumber());
    }

    @Test
    @DisplayName("Test the readVehicleRegistrationNumber() method when the input is too long")
    public void VehicleRegNumberIsTooLong() {
        String data = "12345678910";
        InputStream in = new ByteArrayInputStream(data.getBytes());
        System.setIn(in);

        assertThrows(IllegalArgumentException.class, () -> inputReaderUtil.readVehicleRegistrationNumber());
    }
}