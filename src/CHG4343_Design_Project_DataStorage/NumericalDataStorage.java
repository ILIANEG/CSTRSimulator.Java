package CHG4343_Design_Project_DataStorage;

import CHG4343_Design_Project_CustomExcpetions.ArrayException;
import CHG4343_Design_Project_CustomExcpetions.EmptyArray;

public class NumericalDataStorage {
    public final String[] headers;
    private double[][] numericalData;
    public NumericalDataStorage(String[] headers) throws ArrayException {
        if(headers == null || headers.length == 0) throw new ArrayException("Headers array in data storage object is empty", new EmptyArray(headers));
        this.headers = new String[headers.length];
        for(int i = 0; i < headers.length; i++) {
            this.headers[i] = headers[i];
        }
        this.numericalData = new double[0][this.headers.length];
    }
    public String[] getHeaders() {
        String[] tmpHeaders = new String[this.headers.length];
        for(int i = 0; i < this.headers.length; i++) {
            tmpHeaders[i] = this.headers[i];
        }
        return tmpHeaders;
    }
    public double[] getLastRow() {
        double[] tmpRow = new double[this.headers.length];
        for(int i = 0; i < this.headers.length; i++) {
            tmpRow[i] = this.numericalData[this.numericalData.length - 1][i];
        }
        return tmpRow;
    }
    public double[] getRow(int index) {
        if(!(index < this.numericalData.length)) throw new ArrayIndexOutOfBoundsException("Index or row is out of bound of numerical data array");
        double[] tmpRow = new double[this.headers.length];
        for(int i = 0; i < this.headers.length; i++) {
            tmpRow[i] = this.numericalData[this.numericalData.length - 1][i];
        }
        return tmpRow;
    }
    public void addDataRow(double[] dataRow) throws ArrayException {
        if(dataRow == null) throw new ArrayException("Empty data is added to the Numerical Data Storage",
                new NullPointerException("Data row in Numerical Data Storage is null"));
        double[][] tmpNumericalData = new double[this.numericalData.length+1][this.headers.length];
        for(int i = 0; i < this.numericalData.length; i++) {
            for(int j = 0; j < this.numericalData[i].length; j++) {
                tmpNumericalData[i][j] = this.numericalData[i][j];
            }
        }
        for(int i = 0; i < dataRow.length; i++) {
            tmpNumericalData[this.numericalData.length][i] = dataRow[i];
        }
    }
}
