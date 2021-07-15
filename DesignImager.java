import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Scanner;

import static java.awt.Color.*;

public class DesignImager {
    // Step 1: convert image to 32 pixels

    public static void main(String[] args) {
        // Add better file selection later, this is just for testing
        String imagePath = "/Users/ari/IdeaProjects/Pictures/tarantula.jpg";
        // remember trippy mode where you convert greyscale to color but randomly
        BufferedImage original;
        BufferedImage blockImage;
        try {
            original = ImageIO.read(new File(imagePath));
            blockImage = createImage(original);
            pixelate(original, blockImage);
            int scale = 20;

            Color[][] colors = transcribe(blockImage);
            createTable(colors, scale);

            // make it bigger and add quadrants
            blockImage = divide(makeBigger(blockImage, scale));

            String fileName = "tarantula.jpg";
            File img = new File("/Users/ari/Desktop/" + fileName);

            ImageIO.write(blockImage, "jpg", img);

            String[] string = new String[]{"/bin/bash", "-c", ("open /Users/ari/Desktop/" + fileName)};
            Process proc = new ProcessBuilder(string).start();

        } catch (Exception e) {
            System.err.println("Error: unable to read file (probably).\n\t" + e);
            System.exit(1);
       }
    }

    static BufferedImage createImage(BufferedImage original) throws IOException {
        int height = original.getHeight();
        int width = original.getWidth();
        BufferedImage pixelImage;
        if (height > width)
            // 32/height = x/width
            pixelImage = new BufferedImage((int)((32.0 / height) * width), 32, BufferedImage.TYPE_INT_RGB);
        else
            // 32/width = x/height
            pixelImage = new BufferedImage(32, (int)((32.0 / width) * height), BufferedImage.TYPE_INT_RGB);
        return pixelImage;
    }

    static void pixelate(BufferedImage original, BufferedImage pixels) {
        int width = pixels.getWidth();
        int height = pixels.getHeight();
        int horizInc = original.getWidth() / width;
        int vertInc = original.getHeight() / height;

        //System.out.println(original.getWidth() + " " + width + " " + horizInc + "\t" + original.getHeight() + " " + height + " " + vertInc);

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                int currentPixel = (r * width) + c; // starting with the 0th pixel
                //System.out.println("Current pixel: " + currentPixel);
                //System.out.println("Col " + c + "\tRow " + r);
                //System.out.println("Adding up pixels:");
                // for each pixel...
                // calculate the average of all the pixels in
                // the nth horizInc x vertInc block
                int redSum = 0;
                int greenSum = 0;
                int blueSum = 0;
                for (int subr = (r*vertInc); subr < (r * vertInc)+vertInc; subr++) {
                    for (int subc = (c*horizInc); subc < (c * horizInc) + horizInc; subc++) {
                        //System.out.print("( " + subc + ", " + subr + " )\t");
                        Color color = new Color(original.getRGB(subc, subr));
                        redSum += color.getRed();
                        blueSum += color.getBlue();
                        greenSum += color.getGreen();
                    }
                }
                redSum /= horizInc * vertInc;
                blueSum /= horizInc * vertInc;
                greenSum /= horizInc * vertInc;
                Color blurred = new Color(redSum, greenSum, blueSum);
                //System.out.println();
                pixels.setRGB(c, r, blurred.getRGB());
            }
        }
    }

    static BufferedImage makeBigger(BufferedImage pixels, int scale){
        int width  = pixels.getWidth();
        int height = pixels.getHeight();
        BufferedImage larger = new BufferedImage(width*scale, height*scale, BufferedImage.TYPE_INT_RGB);
        for(int r = 0; r < height*scale; r++){
            for(int c = 0; c < width*scale; c++){
                larger.setRGB(c, r, pixels.getRGB(c/scale, r/scale));
            }
        }
        return larger;
    }

    static BufferedImage divide(BufferedImage pixels){
        // make this recursive eventually so you
        // split the image into four and then depending on
        // the size of the larger dimension, it does
        // different shades of black or numbers them
        int width  = pixels.getWidth();
        int height = pixels.getHeight();
        int rowOffset = 0;
        int colOffset = 0;
        BufferedImage divided = new BufferedImage(width+3, height+3, BufferedImage.TYPE_INT_RGB);
        for(int r = 0; r <= height; r++){
            for(int c = 0; c <= width; c++){
                //System.out.println("Divide row " + r + " col " + c);
                int retrievalRow = r;
                int retrievalCol = c;
                if(r >= height/2)
                    retrievalRow--;
                if(c >= width/2)
                    retrievalCol--;

                if(r == height/2 || c == width/2)
                    divided.setRGB(c, r, 0);
                else{
                    divided.setRGB(c, r, pixels.getRGB(retrievalCol, retrievalRow));
                }
            }
        }
        return divided;
    }

    static Color[][] transcribe(BufferedImage image){
        // hue goes from 0 - 360, there are 30 hues
            // hues change every 12 values
        // sat goes 0 - 100, 15 sats
        // luminosity goes 0 - 100, 15 lums
        int height = image.getHeight();
        int width = image.getWidth();
        Color[][] colors = new Color[height][width];
        for(int r = 0; r < height; r++){
            for(int c = 0; c < width; c++){
                // creates new color from pixel we're at
                colors[r][c] = new Color(image.getRGB(c, r));
                // float[] rgbColor = RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);

                //int hueIndex = (int) Math.round(30 * rgbColor[0]);
                //int satIndex = (int) Math.round(15 * rgbColor[1]);
                //int lumIndex = (int) Math.round(15 * rgbColor[2]);
            }
        }
        return colors;
    }

    static void createTable(Color[][] colors, int scale){
        String name = "Cool Window";
        JFrame frame = new JFrame(name);
        JPanel grid = new JPanel(new GridLayout(colors.length,colors[0].length));
        JLabel  text = new JLabel();

        for (int r = 0; r < colors.length; r++) {
            for (int c = 0; c < colors[0].length; c++) {
                //String s = "( " + ( r + 1 ) + " , " + (c + 1) + " )\n";
                //s += "Hue: 30\nSat: 15\nLum: 12\n";
                JButton butt = new JButton(""); // hehe butt
                butt.setBackground(colors[r][c]);
                butt.setOpaque(true);

                butt.addMouseListener(new MouseAdapter() {
                    Color color = butt.getBackground();
                    float[] hsl = RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);

                    String s = "Hue: " + hsl[0] + "\nSat: " + hsl[1] + "\nLum: " + hsl[2];

                    Boolean mouseWasClicked = false;
                    public void mouseClicked(MouseEvent me) {
                        // hueValue, satValue, lumValue, hueIndex, satIndex, lumIndex
                        double[] acnhValues = convertToACNH(color);
                        String s = "Hue: " + acnhValues[3] + "\t";
                        s += "Sat: " + acnhValues[4] + "\t";
                        s += "Lum: " + acnhValues[5];
                        text.setForeground(Color.BLACK);
                        text.setHorizontalAlignment(SwingConstants.CENTER);
                        butt.add(text);

                        butt.setBorder(BorderFactory.createLineBorder(black, 2, false));
                        Color acnhColor = new Color(HSBtoRGB((float)acnhValues[0], (float)acnhValues[1], (float)acnhValues[2]));
                        butt.setForeground(acnhColor);
                        mouseWasClicked = true;

                    }
                    public void mouseExited(MouseEvent me){
                        if(mouseWasClicked)
                            butt.setBorder(BorderFactory.createLineBorder(black, 1, false));
                    }
                });

                grid.add(butt);

            }
        }
        grid.setPreferredSize(new Dimension(colors[0].length*scale, colors.length*scale));
        frame.setLayout(new FlowLayout());

        frame.add(grid);
        frame.add(text);
        frame.setSize(colors[0].length * (scale+1), (colors.length*(scale+1))+(scale*3));
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    static double[] convertToACNH(Color color){
        double [] hueValues = new double[30];
        for(int i = 0; i < 30; i++){
            hueValues[i] = i * (1.0/30.0);
        }
        double [] satLumValues = new double[12];
        for(int i = 0; i < 12; i++){
            satLumValues[i] = i * (1.0/12.0);
        }

        float[] hsl = RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        int hueIndex = getIndex(hueValues, hsl[0]);
        int lumIndex = getIndex(satLumValues, hsl[1]);
        int satIndex = getIndex(satLumValues, hsl[2]);

        if(hueIndex == -1 || lumIndex == -1 || satIndex == -1){
            return null;
        }
        else{
            double[] returning = {hueValues[hueIndex], satLumValues[satIndex], satLumValues[lumIndex], hueIndex, satIndex, lumIndex};
            return returning;
        }
    }

    static int getIndex(double[] values, float value){
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
}
