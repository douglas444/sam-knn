package br.com.douglas444.datastream;

import br.com.douglas444.common.Point;

import java.io.IOException;

public class DSRunnable {

    public static void run(DSClassifierController dsClassifierController, DSFileReader dsFileReader) throws IOException {

        int t = 0;
        Point point;
        while ((point = dsFileReader.next()) != null) {
            dsClassifierController.predictAndUpdate(point);
            System.out.println(" Timestamp: " + (t++) + " Accuracy: " + dsClassifierController.getAccuracy());
        }
    }

}
