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

    public void optimize(){
        //(from, to)
        int startingSize = colorPalette.size();

        int range = 1;
        while(colorPalette.size() > 16) {
            //System.out.println();
            //System.out.println(colorPalette.size());
            //System.out.println();
            double minDistance = -1;
            ColorObj from = colorPalette.get(0);
            ColorObj to = colorPalette.get(0);
            int size = colorPalette.size();
            for (int i = 0; i < size; i++) {
                for (int j = i; j < size - i; j++) {
                    //return
                    double distance = colorPalette.get(i).findDistanceTo(colorPalette.get(j));
                    if (distance > minDistance) {
                        minDistance = distance;
                        from = colorPalette.get(i);
                        to = colorPalette.get(j);
                        //System.out.println("Mindistance: " + minDistance + "\t" + from + "\t" + to);
                    }
                }
            }
            combineColors(from, to);
        }
        System.out.println("ColorPalette: ");
        System.out.println(colorPalette);

    }

    public void combineColors(ColorObj a, ColorObj b){
        System.out.println("Combining colors!");
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
        System.out.println("Palette from combining:");
        System.out.println(colorPalette);
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
}
