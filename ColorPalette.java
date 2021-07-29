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
        while(colorPalette.size() > 16) {
            int index1 = 0;
            int index2 = 1;
            double minDistance = findDistance(colorPalette.get(index1), colorPalette.get(index2));

            for (int i = 0; i < colorPalette.size(); i++) {
                for (int j = i + 1; j < colorPalette.size(); j++) {
                    double distance = findDistance(colorPalette.get(i), colorPalette.get(j));
                    if (distance < minDistance && i != j) {
                        System.out.println("Changing minDistance to " + i + "(" + colorPalette.get(i) + ")" + "\t" + j + "(" + colorPalette.get(j) + ")" + distance);
                        minDistance = distance;
                        index1 = i;
                        index2 = j;
                    }
                }
            }
            System.out.println("\nCombine " + colorPalette.get(index1) + " and " + colorPalette.get(index2));
            System.out.println(colorPalette);
            combineColors(colorPalette.get(index1), colorPalette.get(index2));
            System.out.println(colorPalette);
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
        int hueIndex;
        int satIndex;
        int lumIndex;

        // hue
        if(a.getHueIndex() > 15 && b.getHueIndex() < 15){
            int aHue = (30-a.getHueIndex())*-1;
            hueIndex = (aHue + b.getHueIndex())/2;
        }
        else if(b.getHueIndex() > 15 && a.getHueIndex() < 15){
            int bHue = (30-b.getHueIndex())*-1;
            hueIndex = (bHue + a.getHueIndex())/2;
        }
        else{
            hueIndex = (a.getHueIndex() + b.getHueIndex())/2;
        }

        // saturation
        if(a.getSatIndex() > 6 && b.getSatIndex() < 6){
            int aSat = (12-a.getSatIndex())*-1;
            satIndex = (aSat + b.getSatIndex())/2;
        }
        else if(b.getSatIndex() > 6 && a.getSatIndex() < 6){
            int bSat = (12-b.getSatIndex())*-1;
            satIndex = (bSat + a.getSatIndex())/2;
        }
        else{
            satIndex = (a.getSatIndex() + b.getSatIndex())/2;
        }

        // luminescence
        if(a.getLumIndex() > 6 && b.getLumIndex() < 6){
            int aLum = (12-a.getLumIndex())*-1;
            lumIndex = (aLum + b.getLumIndex())/2;
        }
        else if(b.getLumIndex() > 6 && a.getLumIndex() < 6){
            int bLum = (12-b.getLumIndex())*-1;
            lumIndex = (bLum + a.getLumIndex())/2;
        }
        else{
            lumIndex = (a.getLumIndex() + b.getLumIndex())/2;
        }

        ColorObj color = new Col;loorObj(hueIndex, satIndex, lumIndex);

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

    public Color findRelative(ColorObj obj){
        double minDistance = obj.findDistanceTo(colorPalette.get(0));
        Color closestRelative = colorPalette.get(0).getColor();
        for(ColorObj color : this.colorPalette){
            double distance = obj.findDistanceTo(color);
            if(distance < minDistance){
                minDistance = distance;
                closestRelative = color.getColor();
            }
        }
        return closestRelative;
    }

    public double findDistance(Integer int1, Integer int2, int cap){
        int distToCap = cap-Math.max(int1, int2);
        int otherDistToCap = Math.min(int1, int2);
        double subtraction = Math.abs(int1-int2);
        double wrapping = distToCap + otherDistToCap;
        return Math.min(subtraction, wrapping);
    }

    public double findDistance(ColorObj color1, ColorObj color2){
        return Math.sqrt(Math.pow(color1.getHueIndex()-color2.getHueIndex(), 2) + Math.pow(color1.getSatIndex()-color2.getSatIndex(), 2) + Math.pow(color1.getLumIndex()-color2.getLumIndex(), 2));
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
