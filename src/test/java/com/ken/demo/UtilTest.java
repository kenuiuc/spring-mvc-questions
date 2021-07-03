package com.ken.demo;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalTime;

public class UtilTest {
    @Test
    public void parseUri() {
        String nullStr = null;
        String emptyStr = "";
//        try {
//            URI nullUri = new URI(nullStr);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
        try {
            URI emptyUri = new URI(emptyStr);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void hourTest() {
              int hour = LocalTime.now().getHour();
    }
}
