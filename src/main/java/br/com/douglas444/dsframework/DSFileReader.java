package br.com.douglas444.dsframework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class DSFileReader {

    private String separator;
    private BufferedReader bufferedReaderData;
    private BufferedReader bufferedReaderLabel;

    public DSFileReader(String separator, Reader dataReader, Reader classReader) {
        this.separator = separator;
        this.bufferedReaderData = new BufferedReader(dataReader);
        this.bufferedReaderLabel = new BufferedReader(classReader);
    }

    /** Reads the next point of the stream.
     *
     * @return the next point of the stream.
     */
    public Point next() throws DSException {

        String line = null;
        try {
            line = bufferedReaderData.readLine();
        } catch (IOException e) {
            throw new DSException(e.getMessage());
        }

        if (line == null) {
            return null;
        }

        String[] splittedLine = line.split(this.separator);
        double[] x = new double[splittedLine.length];

        for (int i = 0; i < splittedLine.length; ++i) {
            try {
                x[i] = Double.parseDouble(splittedLine[i]);
            } catch (NumberFormatException e) {
                throw new DSException(e.getMessage());
            }
        }


        try {
            line = bufferedReaderLabel.readLine();
        } catch (IOException e) {
            throw new DSException(e.getMessage());
        }

        double y;
        try {
            y = Double.parseDouble(line);
        } catch(NumberFormatException e) {
            throw new DSException(e.getMessage());
        }

        return new Point(x, y);
    }

}
