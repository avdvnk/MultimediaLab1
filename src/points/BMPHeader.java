package points;

public class BMPHeader {
    short bitmapFileType;
    long bitmapFileSize;
    short bitmapFileReserved1;
    short bitmapFileReserved2;
    int bitmapFileOffsetBits;

    int size;
    int width;
    int height;
    short planes;
    short bitCount;
    int compression;
    int imageSize;
    int xPixelsPerMeter;
    int yPixelsPerMeter;
    int colorsUsed;
    int colorsImportant;

    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }
}