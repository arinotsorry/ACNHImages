import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static java.awt.Color.HSBtoRGB;
import static java.awt.Color.RGBtoHSB;

public class ColorObj {
    public static ArrayList<ColorObj> colorList = new ArrayList<>();

    private Color color;
    private int red;
    private int green;
    private int blue;
    private float[] hslInitial;
    private double[] hslRounded; // hueVal, satVal, lumVal, hueIndex, satIndex, lumIndex
    private static double[] hueValues = new double[30];
    private static double [] satLumValues = new double[12];
    public Boolean clicked = false;

    public ColorObj(Color color) {
        this.color = color;
        this.red = color.getRed();
        this.green = color.getGreen();
        this.blue = color.getBlue();
        this.hslInitial = RGBtoHSB(this.red, this.green, this.blue, null);
        populate();
        if(!colorList.contains(this))
            colorList.add(this);

        //round values
        roundValue();
        System.out.println(Arrays.toString(hueValues));
        System.out.println(Arrays.toString(satLumValues));
        System.out.println();
    }

    public ColorObj(int rgb) {
        this.color = new Color(rgb);
        this.red = color.getRed();
        this.green = color.getGreen();
        this.blue = color.getBlue();
        this.hslInitial = RGBtoHSB(this.red, this.green, this.blue, null);
        populate();
        if(!colorList.contains(this))
            colorList.add(this);

        //round values
        roundValue();
    }

    public ColorObj(int h, int s, int l){
        populate();
        this.color = new Color(HSBtoRGB((float)hueValues[h], (float)satLumValues[s], (float)satLumValues[l]));
        this.red = color.getRed();
        this.green = color.getGreen();
        this.blue = color.getBlue();
        this.hslInitial = RGBtoHSB(this.red, this.green, this.blue, null);
        this.hslRounded = new double[] {hueValues[h], satLumValues[s], satLumValues[l], h, s, l};
    }

    public static void populate(){
        //initialize values of increment arrays - which colors are actually available in Animal Crossing
        for(int i = 0; i < 30; i++){
            hueValues[i] = i * (1.0/30.0);
            if(i < 12)
                satLumValues[i] = i * (1.0/12.0);
        }
        System.out.println(Arrays.toString(hueValues));
        System.out.println(Arrays.toString(satLumValues));
    }

    public Color getColor() {
        return color;
    }

    public void roundValue(){
        int hueIndex = getIndex(hueValues, this.hslInitial[0]);
        int lumIndex = getIndex(satLumValues, this.hslInitial[1]);
        int satIndex = getIndex(satLumValues, this.hslInitial[2]);

        if(! (hueIndex == -1 || lumIndex == -1 || satIndex == -1)){
            // returning hueVal, satVal, lumVal, hueIndex, satIndex, lumIndex
            this.hslRounded = new double[] {hueValues[hueIndex], satLumValues[satIndex], satLumValues[lumIndex], hueIndex, satIndex, lumIndex};
        }
    }


    private static int getIndex(double[] values, float value){
        int length = values.length;
        double step = values[1] - values[0];
        for(int i = 0; i < length; i++){
            double difference = Math.abs(values[i]-value);
            if(i == length - 1)
                return i;
            else if(difference <= step){
                double differenceAbove = Math.abs(values[i+1] - value);
                return (difference < differenceAbove) ? i : i+1;
            }
        }
        return -1;
    }

    public int getRGB(){
        return color.getRGB();
    }

    public double getHue(){
        return hslRounded[0];
    }
    public double getSat(){
        return hslRounded[1];
    }
    public double getLum(){
        return hslRounded[2];
    }
    public int getHueIndex(){
        return (int)hslRounded[3];
    }public int getSatIndex(){
        return (int)hslRounded[4];
    }
    public int getLumIndex(){
        return (int)hslRounded[5];}

    @Override
    public String toString() {
        return "Hue: " + this.getHueIndex() + ",\tSat: " + this.getSatIndex() + ",\tLum: " + this.getLumIndex();
    }

    public double findDistanceTo(ColorObj obj){

        return Math.sqrt(
                Math.pow(Math.min((obj.getHue()-this.getHue()), Math.min((1-obj.getHue())-this.getHue(), obj.getHue()-(1-this.getHue()))), 2) +
                Math.pow(Math.min((obj.getSat()-this.getSat()), Math.min((1-obj.getSat())-this.getSat(), obj.getSat()-(1-this.getSat()))), 2) +
                Math.pow(Math.min((obj.getLum()-this.getLum()), Math.min((1-obj.getLum())-this.getLum(), obj.getLum()-(1-this.getLum()))), 2));
        //return Math.sqrt(
          //      Math.pow(Math.min((obj.getHue()-this.getHue()), (obj.getHue()-this.getHue()-1)), 2) +
            //    Math.pow(Math.min((obj.getSat()-this.getSat()), (obj.getSat()-this.getSat()-1)), 2) +
              //  Math.pow(Math.min((obj.getLum()-this.getLum()), (obj.getLum()-this.getLum()-1)), 2));
    }
}
