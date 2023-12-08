package CHG4343_Design_Project_DataStorage;

import CHG4343_Design_Project_CustomExcpetions.ArrayException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class NumericalDataStorage {
    public final String[] headers;
    private LinkedList<String[]> numericalData;
    public NumericalDataStorage(String[] headers) throws ArrayException {
        if(headers == null || headers.length == 0) throw new ArrayException("Headers array in data storage object is empty");
        this.headers = new String[headers.length];
        for(int i = 0; i < headers.length; i++) {
            this.headers[i] = headers[i];
        }
        this.numericalData = new LinkedList<String[]>();
    }
    public String[] getHeaders() {
        String[] tmpHeaders = new String[this.headers.length];
        for(int i = 0; i < this.headers.length; i++) {
            tmpHeaders[i] = this.headers[i];
        }
        return tmpHeaders;
    }
    public String[] getRow(int index) {
        return this.numericalData.get(index);
    }
    public void addDataRow(double[] dataRow) throws ArrayException {
        if(dataRow == null) throw new ArrayException("Empty data is added to the Numerical Data Storage",
                new NullPointerException("Data row in Numerical Data Storage is null"));
        if(this.headers.length != dataRow.length) throw new ArrayException("Attempting to add a data row that does not match headers");
        String[] convertedData = new String[dataRow.length];
        for(int i = 0; i < dataRow.length; i++) {
            convertedData[i] = ((Double) dataRow[i]).toString();
        }
        this.numericalData.add(convertedData);
    }
    public String toString() {
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < this.headers.length; i++) {
            if(i != this.headers.length - 1) s.append(this.headers[i]).append("    ");
            else s.append(this.headers[i]).append("\n");
        }
        for (String[] row : this.numericalData) {
            for (int i = 0; i < row.length; i++) {
                if (i != row.length - 1) s.append(row[i]).append("   ");
                else s.append(row[i]).append("\n");
            }
        }
        return s.toString();
    }
    public void writeToFile(String filePath) throws IOException {
        File file = new File(filePath);
        FileWriter fr = new FileWriter(file);
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < this.headers.length; i++) {
            if(i != this.headers.length - 1) s.append(this.headers[i]).append(",");
            else s.append(this.headers[i]).append("\n");
        }
        for (String[] row : this.numericalData) {
            for (int i = 0; i < row.length; i++) {
                if (i != row.length - 1) s.append(row[i]).append(",");
                else s.append(row[i]).append("\n");
            }
        }
        fr.write(s.toString());
    }
}
