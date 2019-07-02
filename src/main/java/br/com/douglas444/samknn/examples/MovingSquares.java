package br.com.douglas444.samknn.examples;

import br.com.douglas444.datastream.DSFileReader;
import br.com.douglas444.datastream.DSRunnable;
import br.com.douglas444.samknn.SAMKNNController;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MovingSquares {

    public static void main(String[] args) {

        FileReader dataFile;
        FileReader labelFile;

        try {
            dataFile = new FileReader(new File("./datasets/movingSquares.data"));
            labelFile = new FileReader(new File("./datasets/movingSquares.labels"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try {
            DSRunnable.run(new SAMKNNController(), new DSFileReader(" ", dataFile, labelFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
