package br.com.douglas444.samknn.examples;

import br.com.douglas444.dsframework.DSClassifierExecutor;
import br.com.douglas444.dsframework.DSFileReader;
import br.com.douglas444.samknn.SAMKNNController;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MovingSquares {

    public static void main(String[] args) {

        final FileReader dataFile;
        final FileReader labelFile;

        try {
            dataFile = new FileReader(new File("./datasets/movingSquares.data"));
            labelFile = new FileReader(new File("./datasets/movingSquares.labels"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try {
            DSClassifierExecutor.start(new SAMKNNController(), new DSFileReader(" ", dataFile, labelFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
