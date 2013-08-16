/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compareimage;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;




import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;


public class CompareImage {
    public static void main(String [] args)
    {
        compareImage();
    }
      private static void drawRectangle(int[][] diff) {
        boolean[][] visited = new boolean[diff.length][diff[0].length];
        for (int i = 0; i < diff.length; i++) {
            for (int j = 0; j < diff[0].length; j++) {
                if (diff[i][j] == 1 && visited[i][j] == false) {
                    drawRectangle(diff, visited, i, j);
                }
            }
        }

    }

    private static void drawRectangle(int[][] diff, boolean[][] visited, int x, int y) {
        visited[x][y] = true;
        if (diff[x][y] == 0) {
            diff[x][y] = 2;
            return;
        }

        if (x + 1 < diff.length && visited[x + 1][y] == false) {
            drawRectangle(diff, visited, x + 1, y);
        }
        if (y + 1 < diff[0].length && visited[x][y + 1] == false) {
            drawRectangle(diff, visited, x, y + 1);
        }

        if (x - 1 >= 0 && visited[x - 1][y] == false) {
            drawRectangle(diff, visited, x - 1, y);
        }
        if (y - 1 >= 0 && visited[x][y - 1] == false) {
            drawRectangle(diff, visited, x, y - 1);
        }

    }

    private static void saveDiffImage(int[][] diff, int[][] oldRGB) {
        drawRectangle(diff);
        int[][] newRGB = new int[diff.length][diff[0].length];
        for(int i=0;i<newRGB.length;i++)
        {
            for(int j=0;j<newRGB[0].length;j++)
            {
                if(diff[i][j]==2)
                {
                    newRGB[i][j]=0xFF0000;
                }
                else
                {
                    newRGB[i][j]=oldRGB[i][j];
                }
            }
                    
        }
    BufferedImage img=new BufferedImage(diff.length,diff[0].length,BufferedImage.TYPE_INT_RGB);

    for (int i = 0; i < diff.length; i++) {
                for (int j = 0; j < diff[0].length; j++) {
                   img.setRGB(i, j, newRGB[i][j]);
                }
            }
        try {
            ImageIO.write(img, "jpg", new File("output.jpg"));
        } catch (IOException ex) {
            Logger.getLogger(CompareImageView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void compareImage() {
        File input1 = new File("image1.png");
        BufferedImage img1;
        File input2 = new File("image2.png");
        BufferedImage img2;
        try {
            img1 = ImageIO.read(input1);

            int w1 = img1.getWidth();
            int h1 = img1.getHeight();
            int rgb1[][] = new int[w1][h1];
            for (int i = 0; i < w1; i++) {
                for (int j = 0; j < h1; j++) {
                    rgb1[i][j] = img1.getRGB(i, j); // store value
                }
            }

            img2 = ImageIO.read(input2);
            int w2 = img2.getWidth();
            int h2 = img2.getHeight();
            int[][] rgb2 = new int[w2][h2];
            for (int i = 0; i < w2; i++) {
                for (int j = 0; j < h2; j++) {
                    rgb2[i][j] = img2.getRGB(i, j); // store value
                }
            }


            //difference
            int[][] diff = new int[w1][h1];

            for (int i = 0; i < w1 && i < w2; i++) {
                for (int j = 0; j < h1 && j < h2; j++) {

                   int difference = Math.abs(rgb1[i][j] - rgb2[i][j]);
                    float average = (rgb1[i][j] + rgb2[i][j]) / 2;
                    float percentage = 0;
                    
                    if (average != 0) {
                        percentage = difference / average;
                    }
                    //if(difference!=0)
                       //  Logger.global.info("hell" );
                    if (difference> 0) {
                      //   Logger.global.info("hell" );
                        diff[i][j] = 1;
                    }
                }
            }


            saveDiffImage(diff, rgb1);
          

        } catch (IOException ex) {
            
        }

    }
}
