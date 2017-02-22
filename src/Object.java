import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Object extends JPanel {
    private float x;
    private float y;
    private int collisionAreaX;
    private int collisionAreaY;
    private String type;
    boolean passed;
    private BufferedImage imgObject;
    public Object(float in_x, float in_y, BufferedImage imgObject, int in_collisionAreaX, int in_collisionAreaY, String in_type){
        this.x = in_x;
        this.y = in_y;
        this.collisionAreaX = in_collisionAreaX;
        this.collisionAreaY = in_collisionAreaY;
        this.imgObject = imgObject;
        this.type = in_type;
        passed = false;
    }
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(imgObject, (int)x,(int)y,null);
        //g.drawRect((int)x,(int)y, imgObject.getWidth(), imgObject.getHeight());
    }
    public void update(float scroll){
        x-=scroll;
    }
    public float getXpos(){
        return this.x;
    }
    public float getYpos(){
        return this.y;
    }
    public int getCollisionAreaX() { return this.collisionAreaX; }
    public int getCollisionAreaY() { return this.collisionAreaY; }
    public String getType() { return this.type; }
    public boolean getPassed(){
        return passed;
    }
    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}
