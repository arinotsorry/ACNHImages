import java.awt.*;
import java.util.ArrayList;

public class ColorPalette extends ArrayList{
    public ArrayList<ColorObj> colorPalette = new ArrayList<>();
    private int size = 16;

    public ColorPalette(ColorObj[][] colors){
        for (ColorObj[] colorRow : colors) {
            for (int c = 0; c < colors[0].length; c++) {
                if (!(this.has(colorRow[c]))) {
                    colorPalette.add(colorRow[c]);
                    System.out.println("Adding " + colorRow[c] + " to colorPalette (" + colorPalette.size() + ")");
                }
            }
        }
    }
    public ColorPalette(){}

    public void optimize(){
        ArrayList<Integer> array = new ArrayList<>();
        addNumbers(array);
        int startingSize = array.size();
        while(array.size() > 4) {
            int index1 = 0;
            int index2 = 1;
            double minDistance = findDistance(array.get(index1), array.get(index2), 30);

            for (int i = 0; i < array.size(); i++) {
                for (int j = i + 1; j < array.size(); j++) {
                    double distance = findDistance(array.get(i), array.get(j), 30);
                    if (distance < minDistance && i != j) {
                        System.out.println("Changing minDistance to " + i + "\t" + j + "\t" + distance);
                        minDistance = distance;
                        index1 = i;
                        index2 = j;
                    }
                }
            }
            System.out.println("\nCombine " + array.get(index1) + " and " + array.get(index2));
            System.out.println(array);
            array.add(findNewColor(array.get(index1), array.get(index2), 30));
            array.remove(Math.max(index1, index2));
            array.remove(Math.min(index1, index2));
            System.out.println(array);
        }

    }

    public void addNumbers(ArrayList<Integer> array){
        array.add(14);
        array.add(28);
        array.add(2);
        array.add(18);
        array.add(17);
        array.add(29);
        array.add(28);
        array.add(17);
        array.add(21);
        array.add(7);
        array.add(25);
        array.add(4);
        array.add(9);
        array.add(24);
        array.add(8);
        array.add(23);
        array.add(5);
        array.add(7);
        array.add(23);
        array.add(22);
        array.add(4);
        array.add(9);
        array.add(12);
        array.add(20);
    }

    public void combineColors(ColorObj a, ColorObj b){
        //System.out.println("Combining colors!");
        int hue = a.getHueIndex() + b.getHueIndex();
        int sat = a.getSatIndex() + b.getSatIndex();
        int lum = a.getLumIndex() + b.getLumIndex();
        hue /= 2;
        sat /= 2;
        lum /= 2;

        ColorObj color = new ColorObj(hue, sat, lum);

        colorPalette.remove(a);
        colorPalette.remove(b);
        colorPalette.add(color);
        //System.out.println("Palette from combining:");
        //System.out.println(colorPalette);
    }

    public boolean has(ColorObj o) {
        int length = this.colorPalette.size();
        for(ColorObj color : this.colorPalette){
            if((o).getHueIndex() == color.getHueIndex()){
                if((o).getSatIndex() == color.getSatIndex()){
                    if((o).getLumIndex() == color.getLumIndex()){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Double findDistance(Integer int1, Integer int2, int cap){
        int distToCap = cap-Math.max(int1, int2);
        int otherDistToCap = Math.min(int1, int2);
        //System.out.println("For " + int1 + " and " + int2 + ":");
        //System.out.println("Distance subtracting: " + Math.abs(int1-int2));
        //System.out.println("Distance wrapping: " + (distToCap+otherDistToCap));
        //System.out.println();
        double subtraction = Math.abs(int1-int2);
        double wrapping = distToCap + otherDistToCap;
        return Math.min(subtraction, wrapping);
    }

    public int findNewColor(Integer int1, Integer int2, int cap){
        int distToCap = cap-Math.max(int1, int2);
        int otherDistToCap = Math.min(int1, int2);
        double subtraction = Math.abs(int1-int2);
        double wrapping = distToCap + otherDistToCap;
        if(subtraction < wrapping){
            return (int1+int2)/2;
        }
        else{
            distToCap *= -1;
            int calculatedDistance = (distToCap+otherDistToCap)/2;
            // you get -1
            if(calculatedDistance < 0)
                return cap+calculatedDistance;
            // you get 32
            else if(calculatedDistance > cap-1)
                return calculatedDistance-cap;
            // you get 15
            return calculatedDistance;
        }
    }
}
