package br.com.douglas444.dsframework;

import java.io.IOException;

public class RunnableStream {

    public static void start(DSClassifierController dsClassifierController, DSFileReader dsFileReader) throws IOException {

        int t = 0;
        Point point;
        while ((point = dsFileReader.next()) != null) {
            dsClassifierController.predictAndUpdate(point);
            System.out.println(" Timestamp: " + (t++) + " Accuracy: " + dsClassifierController.getAccuracy());
        }
    }

}
