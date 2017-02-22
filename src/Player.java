import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.security.spec.ECField;

public class Player extends JPanel{
    private double playerX;
    private double playerY;
    private double speed;
    private double maxSpeed;
    private double minSpeed;
    private double accel;
    private int imgWidth;
    private int imgHeight;
    private int fallWidth;
    private int fallHeight;
    private int standingOffset;
    private double playerVelocityY = 0;
    private double playerAccelerationY = General.getGravity();
    private boolean grounded; //false as soon as player isn't on the ground
    private boolean alive;
    private boolean accelerating;
    private int imgFrame;
    private int imgRow;
    private double nTime;
    private double interval;
    private double lTime;
    private BufferedImage imgPlayer;
    private BufferedImage imgFall;
    private boolean rising; //rise higher during jumps until false
    private int risingTimer; //determines how long you can rise for
    private int risingTimerLimit;
    private boolean standStill;
    private boolean falling;

    public Player() {
        maxSpeed = 0.2;
        minSpeed = -0.2;
        accel = 0.0005;
        risingTimerLimit = General.getRisingLimit();
        try{
            imgPlayer = General.getImage(1).getSubimage(0,0,1112, 189);
            imgFall = General.getImage(1).getSubimage(0,189,1500, 640);
        }catch(Exception e){
        }
        init();
    }

    public void init() {
        playerX = 400;
        imgWidth = 278/2;
        imgHeight = 378/2;
        fallWidth = 600/2;
        fallHeight = 440/2;
        standingOffset = 10;
        playerY = General.getGroundY() - imgHeight;
        speed = -0.1;
        grounded = true;
        accelerating = false;
        imgFrame = 0;
        imgRow = -1;
        interval = 150;
        lTime = 0;
        rising = false;
        risingTimer = 0;
        standStill = true;
        falling = false;
    }

    public void paint(Graphics g) {
        super.paint(g);
        if (imgRow == -1) {
            g.drawImage(imgPlayer.getSubimage(imgWidth * imgFrame, 0, imgWidth, imgHeight), (int) playerX, (int) playerY + standingOffset, null);
        }
        else {
            g.drawImage(imgFall.getSubimage(fallWidth * imgFrame, imgRow * (imgHeight+30), fallWidth, fallHeight), (int)playerX, (int)playerY + standingOffset, null);
        }
    }
    public void update(double in_nTime) {

        nTime = in_nTime;

        if (alive) {
            checkGrannyCollision();
            if (grounded) {

                if (speed > 0.1) {
                    interval = 75;
                } else if (speed > 0) {
                    interval = 100;
                } else if (speed > -0.1) {
                    interval = 125;
                } else if (speed > -0.2) {
                    interval = 150;
                }

                if (nTime - lTime > interval) {
                    imgFrame++;
                    lTime = nTime;
                    if (imgFrame >= 8 && falling == false) imgFrame = 0;
                }
            } else imgFrame = 4;
            velocity();
        }
        else {

            if (nTime - lTime > interval) {
                if (imgFrame < 2) {
                    imgFrame++;
                    lTime = nTime;
                    if (imgFrame == 2 && imgRow == 0) {
                        imgRow = 1;
                        imgFrame = 0;
                    }
                }
            }
        }

    }

    public void playerDeath() {
        imgRow = 0;
        imgFrame = 0;
        interval = 150;
        lTime = 0;
    }

    public void checkGrannyCollision(){
        if(playerX < 65) {
            alive = false;
            playerDeath();
        }
    }
    public float getXPos() {return (float)playerX;}
    public float getYPos() {return (float)playerY;}
    public boolean getAlive(){
        return alive;
    }
    public boolean getAccelerating(){
        return accelerating;
    }
    public double getSpeed() { return  speed; }
    public double getMaxSpeed() { return  maxSpeed; }
    public int getImageWidth(){
        return imgWidth;
    }
    public int getImageHeight(){
        return imgHeight;
    }
    public int getImageRow() { return imgRow; }
    public int getImageFrame() { return imgFrame; }
    public void setImageRow(int inRow) { imgRow = inRow; }
    public void setImageFrame(int inFrame) { imgFrame = inFrame; }
    public boolean getGrounded(){
        return grounded;
    }
    public void setAlive(boolean alive){
        this.alive = alive;
    }
    public void setAccelerating(boolean accelerating){
        this.accelerating = accelerating;
    }
    public void setSpeed(double inSpeed) { speed = inSpeed; }
    public void velocity(){
        if(!grounded){
            gravity();
            playerY += playerVelocityY;
        }
        checkRising();
        accelerate();
    }
    public void checkRising(){
        if(rising){
            playerVelocityY = -1 * General.getJumpVelocity();
            if(risingTimer < risingTimerLimit)risingTimer++;
            else{
                risingTimer = 0;
                rising = false;
            }
        }
    }
    public void accelerate(){
        if(accelerating) {
            if (speed < maxSpeed) {
                speed += accel;
            }
        }
        else {
            if (speed > minSpeed) {
                speed -= accel;
            }
        }

        if (playerX >= General.getWidth() - General.getWidth()/2) {
            if (speed < 0) {
                playerX += speed;
            }
        }
        else {
            playerX += speed;
        }
    }
    public void gravity() {
        playerVelocityY += playerAccelerationY;
            checkLanding();
    }
    public void jump(){
        if(grounded) {
            playerVelocityY = -1 * General.getJumpVelocity();
            grounded = false;
        }
        rising = true;
    }
    public void stopJump(){
        rising = false;
    }
    public void checkLanding(){
        if(playerY > General.getGroundY() - imgHeight){
            grounded = true;
            playerY = General.getGroundY() - imgHeight;
            playerVelocityY = 0;
            risingTimer = 0;
            rising = false;
        }
    }
}
