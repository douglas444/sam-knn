package br.com.douglas444.dsframework;

import br.com.douglas444.common.Point;

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
    public Point next() throws IOException, NumberFormatException {

        String line = bufferedReaderData.readLine();

        if (line == null) {
            return null;
        }

        String[] splittedLine = line.split(this.separator);
        double[] x = new double[splittedLine.length];

        for (int i = 0; i < splittedLine.length; ++i) {
            x[i] = Double.parseDouble(splittedLine[i]);
        }

        line = bufferedReaderLabel.readLine();

        double y = Double.parseDouble(line);

        return new Point(x, y);
    }

}
