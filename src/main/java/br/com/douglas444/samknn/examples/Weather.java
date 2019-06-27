package br.com.douglas444.samknn.examples;

import br.com.douglas444.dsframework.DSFileReader;
import br.com.douglas444.dsframework.RunnableStream;
import br.com.douglas444.samknn.SAMKNNController;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Weather {

    public static void main(String[] args) {

        FileReader dataFile;
        FileReader labelFile;

        try {
            dataFile = new FileReader(new File("./NEweather_data.csv"));
            labelFile = new FileReader(new File("./NEweather_class.csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try {
            RunnableStream.start(new SAMKNNController(), new DSFileReader(",", dataFile, labelFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
