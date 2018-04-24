package points;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class BMP {
    private int[][] data;
    private int height, width;
    private BMPHeader header;
    private LinkedList<int[][]> YCbCr;
    public BMP (String input) throws IOException {
        header = new BMPHeader();
        data = BMPImage.read(new File(input), header);
        height = header.getHeight();
        width = header.getWidth();
    }

    public void writeBMPFile (int[][] rgb, String outputFilename) throws IOException {
        BMPImage.write(new File(outputFilename), header, rgb);
    }

    public void RGBtoYCbCr (int[][] rgb){
        YCbCr = new LinkedList<>();
        int[][] y = new int[height][width];
        int[][] cb = new int[height][width];
        int[][] cr = new int[height][width];
        for (int i = 0; i < rgb.length; i++) {
            for (int j = 0; j < rgb[i].length; j++) {
                int pixel = rgb[i][j];
                int r = Utils.getComponent(pixel, "R");
                int g = Utils.getComponent(pixel, "G");
                int b = Utils.getComponent(pixel, "B");
                y[i][j] = (int)(0.299 * r + 0.587 * g + 0.114 * b);
                cb[i][j] = (int)(0.5643 * (b - y[i][j]) + 128);
                cr[i][j] = (int)(0.7132 * (r - y[i][j]) + 128);
            }
        }
        YCbCr.add(y);
        YCbCr.add(cb);
        YCbCr.add(cr);
    }

    public int[][] getY(){
        return YCbCr.get(0);
    }
    public int[][] getCb(){
        return YCbCr.get(1);
    }
    public int[][] getCr(){
        return YCbCr.get(2);
    }

    public int[][] getData(){
        return data;
    }
}
