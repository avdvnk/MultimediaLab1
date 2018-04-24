package points;

import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteOrder;

public class BMPImage {

    public static int[][] read(File input, BMPHeader header) throws IOException {
        ImageInputStream iis = new FileImageInputStream(input);
        iis.setByteOrder(ByteOrder.LITTLE_ENDIAN);
        header.bitmapFileType = iis.readShort();
        header.bitmapFileSize = iis.readUnsignedInt();
        header.bitmapFileReserved1 = iis.readShort();
        header.bitmapFileReserved2 = iis.readShort();
        header.bitmapFileOffsetBits = iis.readInt();

        header.size = iis.readInt();
        header.width = iis.readInt();
        header.height = iis.readInt();
        header.planes = iis.readShort();
        header.bitCount = iis.readShort();
        header.compression = iis.readInt();
        header.imageSize = iis.readInt();
        header.xPixelsPerMeter = iis.readInt();
        header.yPixelsPerMeter = iis.readInt();
        header.colorsUsed = iis.readInt();
        header.colorsImportant = iis.readInt();

        int[][] rgb = new int[header.height][header.width];

        int dataPerLine = header.width * 3;
        int bytesPerLine = dataPerLine;
        if (bytesPerLine % 4 != 0) {
            bytesPerLine = (bytesPerLine / 4 + 1) * 4;
        }
        int padBytesPerLine = bytesPerLine - dataPerLine;

        int a = 255;
        for (int i = header.height - 1; i >= 0; i--) {
            for (int j = 0; j < header.width; j++) {
                int b = iis.readUnsignedByte();
                int g = iis.readUnsignedByte();
                int r = iis.readUnsignedByte();
                rgb[i][j] = (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
            }
            iis.skipBytes(padBytesPerLine);
        }
        return rgb;
    }

    static void write(File output, BMPHeader header, int[][] rgb) throws IOException {
        ImageOutputStream ios = new FileImageOutputStream(output);
        ios.setByteOrder(ByteOrder.LITTLE_ENDIAN);
        ios.writeShort(header.bitmapFileType);
        ios.writeLong(header.bitmapFileSize);
        ios.writeInt(header.bitmapFileOffsetBits);

        ios.writeInt(header.size);
        ios.writeInt(header.width);
        ios.writeInt(header.height);
        ios.writeShort(header.planes);
        ios.writeShort(header.bitCount);
        ios.writeInt(header.compression);
        ios.writeInt(header.imageSize);
        ios.writeInt(header.xPixelsPerMeter);
        ios.writeInt(header.yPixelsPerMeter);
        ios.writeInt(header.colorsUsed);
        ios.writeInt(header.colorsImportant);

        int bytesPerLine = header.width * 3;
        if (bytesPerLine % 4 != 0) {
            bytesPerLine = (bytesPerLine / 4 + 1) * 4;
        }

        for (int i = header.height - 1; i >= 0; i--) {
            ios.write(toColorArray(rgb[i]));

            for (int k = header.width * 3; k < bytesPerLine; k++) {
                ios.writeByte(0);
            }
        }
        System.out.println("File " + output.getName() + " written");
    }
    private static byte[] toColorArray (int[] array){
        byte[] result = new byte[array.length * 3];
        int b, g, r;
        int j = 0;
        for (int anArray : array) {
            b = anArray & 0xFF;
            g = (anArray >> 8) & 0xFF;
            r = (anArray >> 16) & 0xFF;
            result[j++] = (byte)b;
            result[j++] = (byte)g;
            result[j++] = (byte)r;
        }
        return result;
    }
}