package points;


import org.knowm.xchart.*;

import java.awt.*;
import java.io.IOException;
import java.util.*;

public class Utils {
    public static int[][] selectComponent (int[][] input, String color) throws IOException {
        int [][] resRGB = new int[input.length][];
        for (int i = 0; i < input.length; i++) {
            resRGB[i] = new int[input[i].length];
            for (int j = 0; j < input[i].length; j++) {
                int pixel = input[i][j];
                int resPixel;
                switch (color) {
                    case "R":
                        resPixel = (pixel >> 16) & 0xFF;
                        resRGB[i][j] = resPixel;
                        break;
                    case "G":
                        resPixel = (pixel >> 8) & 0xFF;
                        resRGB[i][j] = resPixel;
                        break;
                    case "B":
                        resPixel = pixel & 0xFF;
                        resRGB[i][j] = resPixel;
                        break;
                }
            }
        }
        return resRGB;
    }

    public static int[][] decimation (int[][] input, int size){
        int [][] result = new int[input.length / size][];
        for (int i = 0; i < input.length; i += size) {
            result[i / size] = new int[input[i].length / size];
            for (int j = 0; j < input[i].length; j += size) {
                result[i / size][j / size] = input[i][j];
            }
        }
        return result;
    }

    public static double psnr(int[][] a, int[][] b) {
        int sum = 0;
        int height = a.length;
        int width = a[0].length;
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                sum += Math.pow(a[i][j] - b[i][j], 2);
            }
        }
        return (10 * Math.log10((width * height * Math.pow(255, 2)) / sum));
    }

    public static int sat(int x) {
        int min = 0;
        int max = 255;
        if(x < min) {
            return min;
        } else if(x > max) {
            return max;
        } else {
            return x;
        }
    }

    public static int[][] recover(int[][] input, int size){
        int[][] recoverRGB = new int[input.length * size][input[0].length * size];
        for (int i = 0; i < recoverRGB.length; i++) {
            for (int j = 0; j < recoverRGB[i].length; j++) {
                recoverRGB[i][j] = input[i / size][j / size];
            }
        }
        return recoverRGB;
    }

    public static int getComponent (int pixel, String color){
        switch (color) {
            case "R":
                pixel = (pixel >> 16) & 0xFF;
                return pixel;
            case "G":
                pixel = (pixel >> 8) & 0xFF;
                return pixel;
            case "B":
                pixel = pixel & 0xFF;
                return pixel;
            default:
                throw new RuntimeException("Wrong color");
        }
    }

    public static double correlation(int[][] a, int[][] b) {
        int height = a.length;
        int width = a[0].length;
        int[][] tmp = new int[height][width];
        double ma = mathExpectation(a);
        double mb = mathExpectation(b);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                tmp[i][j] = (int) ((a[i][j] - ma) * (b[i][j] - mb));
            }
        }
        double da = deviation(a);
        double db = deviation(b);
        return mathExpectation(tmp) / (da * db);
    }

    static double deviation(int[][] array) {
        double sum = 0;
        double expectation = mathExpectation(array);
        int height = array.length;
        int width = array[0].length;
        for (int[] anArray : array) {
            for (int j = 0; j < width; j++) {
                sum += Math.pow(anArray[j] - expectation, 2);
            }
        }
        return Math.sqrt((sum / (width * (height - 1))));
    }

    static double mathExpectation(int[][] array) {
        double sum = 0;
        int height = array.length;
        int width = array[0].length;
        for (int[] anArray : array) {
            for (int j = 0; j < width; j++) {
                sum += anArray[j];
            }
        }
        return (sum / (width * height));
    }

    public static int[][] toColorBoard (int[][] input){
        int[][] result = new int[input.length][];
        Color color;
        for (int i = 0; i < input.length; i++) {
            result[i] = new int[input[i].length];
            for (int j = 0; j < input[i].length; j++) {
                int pixel = input[i][j];
                color = new Color(pixel, pixel, pixel);
                result[i][j] = color.getRGB();
            }
        }
        return result;
    }

    public static int[][] YCbCrtoRGB (int[][] Y, int[][] Cb, int[][]Cr){
        int[][] rgb = new int[Y.length][];
        for (int i = 0; i < Y.length; i++) {
            rgb[i] = new int[Y[i].length];
            for (int j = 0; j < Y[i].length; j++) {
                int g = (int)(Y[i][j] - 0.714 * (Cr[i][j] - 128) - 0.334 * (Cb[i][j] - 128));
                int r = (int)(Y[i][j] + 1.402 * (Cr[i][j] - 128));
                int b = (int)(Y[i][j] + 1.772 * (Cb[i][j] - 128));
                rgb[i][j] = new Color(sat(r), sat(g), sat(b)).getRGB();
            }
        }
        return rgb;
    }

    public static int[][] RGBtoYCbCr (int[][] rgb, String component){
        int height = rgb.length;
        int width = rgb.length;
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
        switch (component){
            case "Y":
                return y;
            case "Cb":
                return cb;
            case "Cr":
                return cr;
        }
        return null;
    }

    public static Map<Integer, Integer> getFrequency(int[][] component) {
        Map<Integer, Integer> result = new TreeMap<>();
        for (int[] aComponent : component) {
            for (int j = 0; j < component[0].length; j++) {
                int value = aComponent[j];
                if (result.containsKey(value)) {
                    result.put(value, result.get(value) + 1);
                } else {
                    result.put(value, 1);
                }
            }
        }
        return result;
    }

    public static double getEnth(int[][] component, Map<Integer, Integer> frequency) {
        double result = 0;
        double temp;
        double size = component.length * component[0].length;
        for (int i = 0; i < 255; i++) {
            if (frequency.containsKey(i)) {
                temp = frequency.get(i) / size;
                result += temp * (Math.log10(temp) / Math.log10(2));
            }
        }
        return -(result);
    }

    public static int[][] decimationAv(int[][] a) {
        int[][] result = new int[a.length / 2][a[0].length / 2];
        for (int i = 0; i < a.length; i++) {
            for(int j = 0; j < a[0].length; j++) {
                if(i % 2 != 0 && j % 2 != 0) {
                    result[i / 2][j / 2] = (a[i][j] + a[i - 1][j] + a[i - 1][j - 1] + a[i][j - 1]) / 4;
                }
            }
        }
        return result;
    }

    static int[][] getSampleOne(int[][] component, int y, int x) {
        int h = component.length - y;
        int w = component[0].length - x;
        int[][] result = new int[h][w];
        for(int i = 0; i < h; i++) {
            System.arraycopy(component[i],0, result[i],0, w);
        }
        return result;
    }

    static int[][] getSampleTwo(int[][] component, int y, int x) {
        int h = component.length - y;
        int w = component[0].length - x;
        int[][] result = new int[h][w];
        for(int i = 0; i < h; i++) {
            System.arraycopy(component[i + y], x, result[i], 0, w);
        }
        return result;
    }

    public static void autoCorrelation(int[][] component, String componentName) {
        XYChart chart = new XYChartBuilder().width(800).height(600).build();
        LinkedList<Double> clist = new LinkedList<>();
        LinkedList<Integer> xlist = new LinkedList<>();
        int[] y = {0, 5, -5, 10, -10};
        for (int i = 0; i < y.length; i++) {
            int value = y[i];
            if (y[i] < 0) {
                value *= -1;
            }
            for (int x = 0; x < component[0].length / 4; x += 4) {
                int[][] first = getSampleOne(component, value, x);
                int[][] second = getSampleTwo(component, value, x);
                clist.add(correlation(first, second));
                xlist.add(x);
            }
            chart.addSeries("AutoCorrelation"
                    + componentName + y[i], clist, xlist);
            clist.clear();
            xlist.clear();
        }
        try {
            BitmapEncoder.saveBitmap(chart, "AutoCorrelation"
                    + componentName, BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
