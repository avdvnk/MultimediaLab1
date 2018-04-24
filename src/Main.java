import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import points.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        BMP image = new BMP("lena.bmp");
        int[][] rgb = image.getData();
        int[][] red = Utils.selectComponent(rgb, "R");
        int[][] green = Utils.selectComponent(rgb, "G");
        int[][] blue = Utils.selectComponent(rgb, "B");
        System.out.println("Correlation:");
        System.out.println("RG: " + Utils.correlation(red, green));
        System.out.println("RB: " + Utils.correlation(red, blue));
        System.out.println("GB: " + Utils.correlation(green, blue) + "\n");

        image.RGBtoYCbCr(rgb);
        int[][] newRGB = Utils.YCbCrtoRGB(image.getY(), image.getCb(), image.getCr());
        System.out.println("PSNR:");
        System.out.println("Red: " + Utils.psnr(red,
                Utils.selectComponent(newRGB, "R")));
        System.out.println("Green: " + Utils.psnr(green,
                Utils.selectComponent(newRGB, "G")));
        System.out.println("Blue: " + Utils.psnr(blue,
                Utils.selectComponent(newRGB, "B")) + "\n");

        int[][] Y = image.getY();
        int[][] Cb = image.getCb();
        int[][] Cr = image.getCr();

        System.out.println("Correlation YCbCr: ");
        System.out.println("YCb: " + Utils.correlation(Y, Cb));
        System.out.println("YCr: " + Utils.correlation(Y, Cr));
        System.out.println("CbCr: " + Utils.correlation(Cb, Cr) + "\n");
        int[][] Cb2 = Utils.decimation(Cb, 2);
        int[][] recoverCb = Utils.recover(Cb2, 2);
        int[][] Cr2 = Utils.decimation(Cr, 2);
        int[][] recoverCr = Utils.recover(Cr2, 2);
        int[][] recoverRGB = Utils.YCbCrtoRGB(Y, recoverCb, recoverCr);

        System.out.println("PSNR after decimation x2:");
        System.out.println("Red: " + Utils.psnr(red,
                Utils.selectComponent(recoverRGB, "R")));
        System.out.println("Green: " + Utils.psnr(green,
                Utils.selectComponent(recoverRGB, "G")));
        System.out.println("Blue: " + Utils.psnr(blue,
                Utils.selectComponent(recoverRGB, "B")));
        System.out.println("Cb: " + Utils.psnr(Cb, recoverCb));
        System.out.println("Cr: " + Utils.psnr(Cr, recoverCr) + "\n");

        Cb2 = Utils.decimationAv(Cb);
        recoverCb = Utils.recover(Cb2, 2);
        Cr2 = Utils.decimationAv(Cr);
        recoverCr = Utils.recover(Cr2, 2);
        recoverRGB = Utils.YCbCrtoRGB(Y, recoverCb, recoverCr);
        System.out.println("Decimation Av x2: ");
        System.out.println("Red: " + Utils.psnr(red,
                Utils.selectComponent(recoverRGB, "R")));
        System.out.println("Green: " + Utils.psnr(green,
                Utils.selectComponent(recoverRGB, "G")));
        System.out.println("Blue: " + Utils.psnr(blue,
                Utils.selectComponent(recoverRGB, "B")));
        System.out.println("Cb: " + Utils.psnr(Cb, recoverCb));
        System.out.println("Cr: " + Utils.psnr(Cr, recoverCr) + "\n");


        int[][] Cb4 = Utils.decimation(Cb, 4);
        recoverCb = Utils.recover(Cb4, 4);
        int[][] Cr4 = Utils.decimation(Cr, 4);
        recoverCr = Utils.recover(Cr4, 4);
        recoverRGB = Utils.YCbCrtoRGB(Y, recoverCb, recoverCr);
        System.out.println("PSNR after decimation x4:");
        System.out.println("Red: " + Utils.psnr(red,
                Utils.selectComponent(recoverRGB, "R")));
        System.out.println("Green: " + Utils.psnr(green,
                Utils.selectComponent(recoverRGB, "G")));
        System.out.println("Blue: " + Utils.psnr(blue,
                Utils.selectComponent(recoverRGB, "B")));
        System.out.println("Cb: " + Utils.psnr(Cb, recoverCb));
        System.out.println("Cr: " + Utils.psnr(Cr, recoverCr) + "\n");

        Cb4 = Utils.decimationAv(Cb2);
        recoverCb = Utils.recover(Cb4, 4);
        Cr4 = Utils.decimationAv(Cr2);
        recoverCr = Utils.recover(Cr4, 4);
        recoverRGB = Utils.YCbCrtoRGB(Y, recoverCb, recoverCr);
        System.out.println("Decimation Av x4: ");
        System.out.println("Red: " + Utils.psnr(red,
                Utils.selectComponent(recoverRGB, "R")));
        System.out.println("Green: " + Utils.psnr(green,
                Utils.selectComponent(recoverRGB, "G")));
        System.out.println("Blue: " + Utils.psnr(blue,
                Utils.selectComponent(recoverRGB, "B")));
        System.out.println("Cb: " + Utils.psnr(Cb, recoverCb));
        System.out.println("Cr: " + Utils.psnr(Cr, recoverCr) + "\n");

        Map<Integer, Integer> freq = Utils.getFrequency(red);
        CategoryChart chart = new CategoryChartBuilder().width(800).height(600).build();
        chart.addSeries("freqRed", new ArrayList<>(freq.keySet()),
                new ArrayList<>(freq.values()));
        try {
            BitmapEncoder.saveBitmap(chart, "freqRed",
                    BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Enth red: " + Utils.getEnth(red, freq));

        freq = Utils.getFrequency(green);
        chart = new CategoryChartBuilder().width(800).height(600).build();
        chart.addSeries("freqGreen", new ArrayList<>(freq.keySet()),
                new ArrayList<>(freq.values()));
        try {
            BitmapEncoder.saveBitmap(chart, "freqGreen",
                    BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Enth green: " + Utils.getEnth(green, freq));

        freq = Utils.getFrequency(blue);
        chart = new CategoryChartBuilder().width(800).height(600).build();
        chart.addSeries("freqBlue", new ArrayList<>(freq.keySet()),
                new ArrayList<>(freq.values()));
        try {
            BitmapEncoder.saveBitmap(chart, "freqBlue",
                    BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Enth blue: " + Utils.getEnth(blue, freq));

        freq = Utils.getFrequency(Y);
        chart = new CategoryChartBuilder().width(800).height(600).build();
        chart.addSeries("freqY", new ArrayList<>(freq.keySet()),
                new ArrayList<>(freq.values()));
        try {
            BitmapEncoder.saveBitmap(chart, "freqY",
                    BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Enth Y: " + Utils.getEnth(Y, freq));

        freq = Utils.getFrequency(Cb);
        chart = new CategoryChartBuilder().width(800).height(600).build();
        chart.addSeries("freqCb", new ArrayList<>(freq.keySet()),
                new ArrayList<>(freq.values()));
        try {
            BitmapEncoder.saveBitmap(chart, "freqCb",
                    BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Enth Cb: " + Utils.getEnth(Cb, freq));

        freq = Utils.getFrequency(Cr);
        chart = new CategoryChartBuilder().width(800).height(600).build();
        chart.addSeries("freqCr", new ArrayList<>(freq.keySet()),
                new ArrayList<>(freq.values()));
        try {
            BitmapEncoder.saveBitmap(chart, "freqCr",
                    BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Enth Cr: " + Utils.getEnth(Cr, freq));

        Utils.autoCorrelation(red, "red");
        Utils.autoCorrelation(green, "green");
        Utils.autoCorrelation(blue, "blue");
        Utils.autoCorrelation(Y, "Y");
        Utils.autoCorrelation(Cb, "Cb");
        Utils.autoCorrelation(Cr, "Cr");
    }
}
