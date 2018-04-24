package points;

public class YCbCr {
    private double Y;
    private double Cb;
    private double Cr;
    public YCbCr (double y, double cb, double cr){
        Y = y;
        Cb = cb;
        Cr = cr;
    }
    public double getY(){
        return Y;
    }
    public double getCb(){
        return Cb;
    }
    public double getCr(){
        return Cr;
    }
    public void setY(double value){
        Y = value;
    }
    public void setCb(double value){
        Cb = value;
    }
    public void setCr(double value){
        Cr = value;
    }
}
