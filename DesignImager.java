//import javax.imageio.ImageIO;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//
//import static java.awt.Color.black;
//
//public class DesignImager {
//    // Step 1: convert image to 32 pixels
//    static public ArrayList<ColorObj> unsortedColors = new ArrayList<ColorObj>();
//    static public int[][] colorPalette = new int[16][3];
//    static final int SCALE = 20;
//
//
//
//    public static void main(String[] args) {
//        // Put absolute file path in config
//        String imagePath = args[0];
//        String[] splitPath = imagePath.split("/");
//        String fileName = splitPath[splitPath.length-1];
//
//        BufferedImage original;
//        BufferedImage blockImage;
//
//        try {
//            original = ImageIO.read(new File(imagePath));
//            blockImage = createImage(original);
//            pixelate(original, blockImage);
//            ColorObj[][] colors = transcribe(blockImage);
//            createTable(colors);
//
//            ColorPalette palette = new ColorPalette(colors);
//            palette.optimize();
//
//            displayPalette(colors, palette);
//            // make it bigger and add quadrants
//            blockImage = divide(makeBigger(blockImage));
//            File img = new File("/Users/ari/Desktop/" + fileName);
//            ImageIO.write(blockImage, "jpg", img);
//            String[] string = new String[]{"/bin/bash", "-c", ("open /Users/ari/Desktop/" + fileName)};
//            Process proc = new ProcessBuilder(string).start();
//
//        } catch (Exception e) {
//            System.err.println("Error: unable to read file (probably).\n\t" + e);
//            System.exit(1);
//       }
//    }
//
//    private static void displayPalette(ColorObj[][] colors, ColorPalette palette) {
//        String windowName = "Cool Window";
//        JFrame frame = new JFrame(windowName);
//        JPanel grid = new JPanel(new GridLayout(colors.length, colors[0].length));
//        JLabel text = new JLabel();
//
//        JPanel paletteGrid = new JPanel(new GridLayout(1, 16));
//        for(ColorObj c : palette.colorPalette){
//            JButton paletteButton = new JButton();
//            paletteButton.setBackground(c.getColor());
//            paletteButton.setForeground(c.getColor());
//            paletteButton.setOpaque(true);
//            paletteButton.addMouseListener(new MouseAdapter() {
//                public void mouseClicked(MouseEvent me){
//                    paletteButton.setBorder(BorderFactory.createLineBorder(black, 2, false));
//                }
//                public void mouseExited(MouseEvent me){
//                    paletteButton.setBorder(BorderFactory.createLineBorder(black, 0, false));
//                }
//            });
//
//            paletteGrid.add(paletteButton);
//        }
//
//        for (int r = 0; r < colors.length; r++) {
//            for (int c = 0; c < colors[0].length; c++) {
//                JButton box = new JButton("");
//                box.setBackground(palette.findRelative(colors[r][c]));
//                box.setOpaque(true);
//                grid.add(box);
//            }
//        }
//
//        paletteGrid.setPreferredSize(new Dimension(colors[0].length*SCALE, 30));
//        grid.setPreferredSize(new Dimension(colors[0].length*SCALE, colors.length*SCALE));
//        frame.setLayout(new FlowLayout());
//        frame.add(paletteGrid);
//        frame.add(grid);
//        frame.add(text);
//        frame.setSize(colors[0].length * (SCALE+1), (colors.length*(SCALE+1))+(SCALE*3));
//        frame.setVisible(true);
//        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//
//    }
//
//    static BufferedImage createImage(BufferedImage original) throws IOException {
//        int height = original.getHeight();
//        int width = original.getWidth();
//        BufferedImage pixelImage;
//        if (height > width)
//            // 32/height = x/width
//            pixelImage = new BufferedImage((int)((32.0 / height) * width), 32, BufferedImage.TYPE_INT_RGB);
//        else
//            // 32/width = x/height
//            pixelImage = new BufferedImage(32, (int)((32.0 / width) * height), BufferedImage.TYPE_INT_RGB);
//        return pixelImage;
//    }
//
//    static void pixelate(BufferedImage original, BufferedImage pixels) {
//        int width = pixels.getWidth();
//        int height = pixels.getHeight();
//        int horizInc = original.getWidth() / width;
//        int vertInc = original.getHeight() / height;
//
//        for (int r = 0; r < height; r++) {
//            for (int c = 0; c < width; c++) {
//                // for each pixel...
//                // calculate the average of all the pixels in
//                // the nth horizInc x vertInc block
//                int redSum = 0;
//                int greenSum = 0;
//                int blueSum = 0;
//                for (int subr = (r*vertInc); subr < (r * vertInc)+vertInc; subr++) {
//                    for (int subc = (c*horizInc); subc < (c * horizInc) + horizInc; subc++) {
//                        //System.out.print("( " + subc + ", " + subr + " )\t");
//                        Color color = new Color(original.getRGB(subc, subr));
//                        redSum += color.getRed();
//                        blueSum += color.getBlue();
//                        greenSum += color.getGreen();
//                    }
//                }
//                redSum /= horizInc * vertInc;
//                blueSum /= horizInc * vertInc;
//                greenSum /= horizInc * vertInc;
//                Color blurred = new Color(redSum, greenSum, blueSum);
//                pixels.setRGB(c, r, blurred.getRGB());
//            }
//        }
//    }
//
//    static BufferedImage makeBigger(BufferedImage pixels){
//        int width  = pixels.getWidth();
//        int height = pixels.getHeight();
//        BufferedImage larger = new BufferedImage(width*SCALE, height*SCALE, BufferedImage.TYPE_INT_RGB);
//        for(int r = 0; r < height*SCALE; r++){
//            for(int c = 0; c < width*SCALE; c++){
//                larger.setRGB(c, r, pixels.getRGB(c/SCALE, r/SCALE));
//            }
//        }
//        return larger;
//    }
//
//    static BufferedImage divide(BufferedImage pixels){
//        // make this recursive eventually so you
//        // split the image into four and then depending on
//        // the size of the larger dimension, it does
//        // different shades of black or numbers them
//        int width  = pixels.getWidth();
//        int height = pixels.getHeight();
//        BufferedImage divided = new BufferedImage(width+3, height+3, BufferedImage.TYPE_INT_RGB);
//        for(int r = 0; r <= height; r++){
//            for(int c = 0; c <= width; c++){
//                int retrievalRow = r;
//                int retrievalCol = c;
//                if(r >= height/2)
//                    retrievalRow--;
//                if(c >= width/2)
//                    retrievalCol--;
//
//                if(r == height/2 || c == width/2)
//                    divided.setRGB(c, r, 0);
//                else{
//                    divided.setRGB(c, r, pixels.getRGB(retrievalCol, retrievalRow));
//                }
//            }
//        }
//        return divided;
//    }
//
//    /**
//     * Create 2d array of ColorObjs from image file
//     * @param image
//     * @return array of ColorObjs which are basically Colors
//     */
//    static ColorObj[][] transcribe(BufferedImage image){
//        int height = image.getHeight();
//        int width = image.getWidth();
//        ColorObj[][] colors = new ColorObj[height][width];
//        for(int r = 0; r < height; r++){
//            for(int c = 0; c < width; c++){
//                // creates new color from pixel we're at
//                colors[r][c] = new ColorObj(image.getRGB(c, r));
//            }
//        }
//        return colors;
//    }
//
//    static void createTable(ColorObj[][] colors){
//        for (int r = 0; r < colors.length; r++) {
//            for (int c = 0; c < colors[0].length; c++) {
//                unsortedColors.add(colors[r][c]);
//            }
//        }
//    }
//
//}
