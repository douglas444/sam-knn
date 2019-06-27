package br.com.douglas444.samknn.examples;

import br.com.douglas444.dsframework.DSException;
import br.com.douglas444.dsframework.DSFileReader;
import br.com.douglas444.dsframework.Point;
import br.com.douglas444.samknn.SAMKNNController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class RotatingHyperplane {
    public static void main(String[] args) {

        File dataFile = new File("./rotatingHyperplane.data");
        File labelFile = new File("./rotatingHyperplane.labels");

        DSFileReader dsFileReader;
        try {
            dsFileReader = new DSFileReader(" ", new FileReader(dataFile), new FileReader(labelFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        SAMKNNController samknnController= new SAMKNNController();

        int t = 0;
        Point point;
        while(true) {


            try {
                if ((point = dsFileReader.next()) == null) break;
            } catch (DSException e) {
                e.printStackTrace();
                return;
            }

            samknnController.predictAndUpdate(point);

            System.out.println(" Timestamp: " + (t++) + " Accuracy: " + samknnController.getAccuracy());
        }

    }

}