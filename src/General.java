import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public abstract class General { //Game Constants and General functions put in Here, abstract
    private static int width = 800;
    private static int height = 600;
    private static int groundY = 497;
    private static double gravity = 0.002;
    private static double jumpVelocity = 0.5;
    private static int risingLimit = 300;
    private static double spriteSize = 0.5;
    private static BufferedImage imgPlayer = loadImage("ss_player.png");
    private static BufferedImage imgObjects = loadImage("ss_objects.png");
    private static int grannyAnimationSpeed = 60;

    public static int getGroundY(){
        return groundY;
    }
    public static double getGravity(){
        return gravity;
    }
    public static double getJumpVelocity(){return jumpVelocity;
    }
    public static int getRisingLimit(){
        return risingLimit;
    }
    public static BufferedImage getImage(int i){
        if(i==1)return imgPlayer;
        else if(i==2)return imgObjects;
        return imgPlayer;
    }
    protected static BufferedImage loadImage(String file) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream filePath = loader.getResourceAsStream("res/images/" + file);
        BufferedImage img = null;
        try {
            img = ImageIO.read(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        img = resizeImage(img);
        return img;
    }
    public static BufferedImage resizeImage(BufferedImage oldImg){
        BufferedImage resizedImg = new BufferedImage((int)(oldImg.getWidth()*spriteSize),(int)(oldImg.getHeight()*spriteSize), BufferedImage.TRANSLUCENT);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(oldImg, 0, 0,(int)(oldImg.getWidth()*spriteSize),(int)(oldImg.getHeight()*spriteSize), null);
        g2.dispose();
        return resizedImg;
    }
    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static int getGrannyAnimationSpeed() {
        return grannyAnimationSpeed;
    }
}
