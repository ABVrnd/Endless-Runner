import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Game extends JPanel implements KeyListener {

    Main main;
    ResourceLoader resLoader;
    Random rn = new Random();
    Player player = new Player();
    ArrayList<Object> objects = new ArrayList<>();
    UI ui;
    private int groundY = General.getGroundY();
    private double nTime = 0;
    private double interval = 0.5;
    private int frameInterval;
    private double lTime = 0;

    private double brickInterval;
    private double lBrickTime = 0;

    private double cloudInterval;
    private double lCloudTime;

    private BufferedImage imgBackground;
    private BufferedImage imgGround1;
    private BufferedImage imgGround2;
    private BufferedImage imgGround3;
    private BufferedImage imgObject;
    private BufferedImage imgCloud1;
    private BufferedImage imgCloud2;
    private BufferedImage imgCloud3;
    private BufferedImage imgGranny;

    private float cloud1x;
    private int cloud1y;
    private float cloud2x;
    private int cloud2y;
    private float cloud3x;
    private int cloud3y;

    private float ground1x = 0;
    private float ground2x = 462;
    private float ground3x = 924;
    private float scroll = 0.2f;
    private int frameGranny;
    private int difficulty = 0;

    public Game(JFrame inFrame, Main inMain, ResourceLoader inResloader) {

        inFrame.addKeyListener(this);
        main = inMain;
        resLoader = inResloader;

        ui = new UI(resLoader);

        imgBackground = resLoader.loadImage("background.png");
        imgGranny = General.getImage(1).getSubimage(0, 625, 928, 145);
        imgGround1 = General.getImage(2).getSubimage(0, 148, 462, 177);
        imgGround2 = General.getImage(2).getSubimage(0, 148, 462, 177);
        imgGround3 = General.getImage(2).getSubimage(0, 148, 462, 177);
        imgObject = General.getImage(2).getSubimage(0, 0,1500, 148);
        imgCloud1 = General.getImage(2).getSubimage(1050, 0, 130, 130);
        imgCloud2 = General.getImage(2).getSubimage(16, 360, 163, 108);
        imgCloud3 = General.getImage(2).getSubimage(5, 540, 193, 125);

        cloud1x = -31;
        cloud2x = -163;
        cloud3x = -193;
    }

    public void paint(Graphics g) {

        g.drawImage(imgBackground, 0, 0, null);
        g.drawImage(imgGround1, (int)ground1x, groundY - 7, null);
        g.drawImage(imgGround2, (int)ground2x, groundY - 7, null);
        g.drawImage(imgGround3, (int)ground3x, groundY - 7, null);
        g.drawImage(imgGranny.getSubimage(116*frameGranny, 0, 116, 145), 0, groundY - 128, null);
        if (cloud1x > -130) {
            g.drawImage(imgCloud1, (int)cloud1x, cloud1y, null);
        }

        if (cloud2x > -163) {
            g.drawImage(imgCloud2, (int)cloud2x, cloud2y, null);
        }

        if (cloud3x > -193) {
            g.drawImage(imgCloud3, (int)cloud3x, cloud3y, null);
        }

        for (int i=0; i<objects.size(); i++) {
            objects.get(i).paint(g);
        }
        player.paint(g);
        ui.paint(g);
        //g.drawRect((int)player.getXPos(), (int)player.getYPos(), player.getImageWidth(), player.getImageHeight());
    }

    public void update() {

        if (player.getAlive() || (player.getImageRow() != 1 && player.getImageFrame() != 2)) {
            nTime = System.currentTimeMillis();
        }

        if (player.getAlive()) {


            if (nTime - lBrickTime >= brickInterval) {
                spawnObject();
                brickInterval = rn.nextInt(5000 - 2500) + 2500;
                lBrickTime = nTime;
            }

            if (nTime - lCloudTime >= cloudInterval) {
                spawnCloud();
                cloudInterval = rn.nextInt(2600 - 1300) + 1300;
                lCloudTime = nTime;
            }

            if (nTime - lTime >= interval) {

                if (objects.size() > 0) {

                    for (int i = 0; i < objects.size(); i ++) {
                        if (objects.get(i).getXpos() > -150) {
                            objects.get(i).update(scroll);
                            if(!objects.get(i).getPassed()&& objects.get(i).getXpos()+75 < player.getXPos()){
                                objects.get(i).setPassed(true);
                                ui.score();
                            }
                        } else {
                            objects.remove(i);
                        }
                    }
                }

                if (ground1x <= -462) {
                    ground1x = 924;
                    if(difficulty == 1)imgGround1 = General.getImage(2).getSubimage(466, 148, 462, 177);
                    if(difficulty == 2)imgGround1 = General.getImage(2).getSubimage(932, 148, 462, 177);
                }
                else if (ground2x <= -462) {
                    ground2x = 924;
                    if(difficulty == 1)imgGround2 = General.getImage(2).getSubimage(466, 148, 462, 177);
                    if(difficulty == 2)imgGround2 = General.getImage(2).getSubimage(932, 148, 462, 177);
                }
                else if (ground3x <= -462) {
                    ground3x = 924;
                    if(difficulty == 1)imgGround3 = General.getImage(2).getSubimage(466, 148, 462, 177);
                    if(difficulty == 2)imgGround3 = General.getImage(2).getSubimage(932, 148, 462, 177);
                }
                checkDifficulty();
                player.update(nTime);
                animateGranny();
                ground1x -= scroll;
                ground2x -= scroll;
                ground3x -= scroll;

                if (cloud1x > -130) {
                    cloud1x -=scroll;
                }

                if (cloud2x > -163) {
                    cloud2x -=scroll;
                }

                if (cloud3x > -193) {
                    cloud3x -=scroll;
                }

                lTime = nTime;
            }
            if (objects.size() > 0) {
                for (int i = 0; i < objects.size(); i ++) {
                    if (onCollision(player.getXPos(), player.getYPos(), objects.get(i).getXpos(), objects.get(i).getYpos(), objects.get(i).getCollisionAreaX(), objects.get(i).getCollisionAreaY())) {
                        if (objects.get(i).getType() == "mud") {
                            player.setAccelerating(false);
                        }
                        else {
                            player.playerDeath();
                            player.setAlive(false);
                        }
                    }
                }
            }
        }
        else if(ui.getWaitStart());
        else {
            ui.setDeadScreen(true);

            if ((player.getImageRow() != 1) && (player.getImageFrame() != 2)) {

                player.update(nTime);
            }
        }
    }

//    private void spawnBrick() {
//
//        numBricks = rn.nextInt(4)+1;
//        brickY = 465;
//
//        for (int i=0; i<numBricks; i++) {
//            bricks.add((float)General.getWidth());
//            bricks.add((float)brickY);
//            brickY -= 32;
//        }
//    }
    private void animateGranny(){
        frameInterval++;
        if(frameInterval > General.getGrannyAnimationSpeed()) {
            if (frameGranny == 6) frameGranny = 0;
            else frameGranny++;

            frameInterval = 0;
        }
    }
    private void checkDifficulty(){
        if(difficulty == 0 && ui.getScore() >= 2)difficulty++;
        if(difficulty == 1 && ui.getScore() >= 5)difficulty++;
    }
    private void spawnCloud() {

        int type = rn.nextInt(3);

        if (type == 0) {
            if (cloud1x <= -130) {
                cloud1y = rn.nextInt(100);
                cloud1x = (float) General.getWidth();
            }
        }
        else if (type == 1) {
            if (cloud2x <= -163) {
                cloud2y = rn.nextInt(100);
                cloud2x = (float) General.getWidth();
            }
        }
        else if (type == 2) {
            if (cloud3x <= -193) {
                cloud3y = rn.nextInt(101);
                cloud3x = (float) General.getWidth();
            }
        }
    }

    public void restart(){
        nTime = 0;
        lTime = 0;
        lBrickTime = 0;
        lCloudTime = 0;
        player.init();
        ui.init();
        objects.clear();
        cloud1x = -31;
        cloud2x = -163;
        cloud3x = -193;
        frameGranny = 0;
        frameInterval = 0;
        imgGround1 = General.getImage(2).getSubimage(0, 148, 462, 177);
        imgGround2 = General.getImage(2).getSubimage(0, 148, 462, 177);
        imgGround3 = General.getImage(2).getSubimage(0, 148, 462, 177);
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP) {
            if (player.getGrounded()) {
                if(ui.getWaitStart()){
                    player.setAlive(true);
                    ui.setWaitStart(false);
                    ui.setDeadScreen(false);
                    ui.setShowHighscore(false);
                }
                else player.jump();
            }
            else if (ui.getDeadScreen() && !ui.getScoresWritten()) {
                if (ui.getSelectorY() <= General.getHeight()/2 - 50) {
                    ui.setSelectorY(General.getHeight()/2 + 100);
                }
                else {
                    ui.setSelectorY(ui.getSelectorY() - 50);
                }
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (ui.getDeadScreen() && !ui.getScoresWritten()) {
                if (ui.getSelectorX() >= (General.getWidth()/2 - 200) + 360) {
                    ui.setSelectorX(General.getWidth()/2 - 200);
                }
                else {
                    ui.setSelectorX(ui.getSelectorX() + 60);
                }
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (player.getAlive()) {
                if (!player.getAccelerating()) player.setAccelerating(true);
            }
            else if (ui.getDeadScreen() && !ui.getScoresWritten()) {

                if (ui.getSelectorY() == General.getHeight()/2 - 50) {
                    if (ui.getSelectorX() == General.getWidth()/2 - 200) {
                        if (ui.getNumLetters() < 3) {
                            ui.setInitials(ui.getInitials() + "A");
                            ui.setNumLetters(ui.getNumLetters() + 1);
                        }
                    }
                    else if (ui.getSelectorX() == General.getWidth()/2 - 140) {
                        if (ui.getNumLetters() < 3) {
                            ui.setInitials(ui.getInitials() + "B");
                            ui.setNumLetters(ui.getNumLetters() + 1);
                        }
                    }
                    else if (ui.getSelectorX() == General.getWidth()/2 - 80) {
                        if (ui.getNumLetters() < 3) {
                            ui.setInitials(ui.getInitials() + "C");
                            ui.setNumLetters(ui.getNumLetters() + 1);
                        }
                    }
                    else if (ui.getSelectorX() == General.getWidth()/2 - 20) {
                        if (ui.getNumLetters() < 3) {
                            ui.setInitials(ui.getInitials() + "D");
                            ui.setNumLetters(ui.getNumLetters() + 1);
                        }
                    }
                    else if (ui.getSelectorX() == General.getWidth()/2 + 40) {
                        if (ui.getNumLetters() < 3) {
                            ui.setInitials(ui.getInitials() + "E");
                            ui.setNumLetters(ui.getNumLetters() + 1);
                        }
                    }
                    else if (ui.getSelectorX() == General.getWidth()/2 + 100) {
                        if (ui.getNumLetters() < 3) {
                            ui.setInitials(ui.getInitials() + "F");
                            ui.setNumLetters(ui.getNumLetters() + 1);
                        }
                    }
                    else if (ui.getSelectorX() == General.getWidth()/2 + 160) {
                        if (ui.getNumLetters() < 3) {
                            ui.setInitials(ui.getInitials() + "G");
                            ui.setNumLetters(ui.getNumLetters() + 1);
                        }
                    }
                }
                else if (ui.getSelectorY() == General.getHeight()/2) {
                    if (ui.getSelectorX() == General.getWidth()/2 - 200) {
                        if (ui.getNumLetters() < 3) {
                            ui.setInitials(ui.getInitials() + "H");
                            ui.setNumLetters(ui.getNumLetters() + 1);
                        }
                    }
                    else if (ui.getSelectorX() == General.getWidth()/2 - 140) {
                        if (ui.getNumLetters() < 3) {
                            ui.setInitials(ui.getInitials() + "I");
                            ui.setNumLetters(ui.getNumLetters() + 1);
                        }
                    }
                    else if (ui.getSelectorX() == General.getWidth()/2 - 80) {
                        if (ui.getNumLetters() < 3) {
                            ui.setInitials(ui.getInitials() + "J");
                            ui.setNumLetters(ui.getNumLetters() + 1);
                        }
                    }
                    else if (ui.getSelectorX() == General.getWidth()/2 - 20) {
                        if (ui.getNumLetters() < 3) {
                            ui.setInitials(ui.getInitials() + "K");
                            ui.setNumLetters(ui.getNumLetters() + 1);
                        }
                    }
                    else if (ui.getSelectorX() == General.getWidth()/2 + 40) {
                        if (ui.getNumLetters() < 3) {
                            ui.setInitials(ui.getInitials() + "L");
                            ui.setNumLetters(ui.getNumLetters() + 1);
                        }
                    }
                    else if (ui.getSelectorX() == General.getWidth()/2 + 100) {
                        if (ui.getNumLetters() < 3) {
                            ui.setInitials(ui.getInitials() + "M");
                            ui.setNumLetters(ui.getNumLetters() + 1);
                        }
                    }
                    else if (ui.getSelectorX() == General.getWidth()/2 + 160) {
                        if (ui.getNumLetters() < 3) {
                            ui.setInitials(ui.getInitials() + "N");
                            ui.setNumLetters(ui.getNumLetters() + 1);
                        }
                    }
                }
                else if (ui.getSelectorY() == General.getHeight()/2 + 50) {
                    if (ui.getSelectorX() == General.getWidth()/2 - 200) {
                        if (ui.getNumLetters() < 3) {
                            ui.setInitials(ui.getInitials() + "O");
                            ui.setNumLetters(ui.getNumLetters() + 1);
                        }
                    }
                    else if (ui.getSelectorX() == General.getWidth()/2 - 140) {
                        if (ui.getNumLetters() < 3) {
                            ui.setInitials(ui.getInitials() + "P");
                            ui.setNumLetters(ui.getNumLetters() + 1);
                        }
                    }
                    else if (ui.getSelectorX() == General.getWidth()/2 - 80) {
                        if (ui.getNumLetters() < 3) {
                            ui.setInitials(ui.getInitials() + "Q");
                            ui.setNumLetters(ui.getNumLetters() + 1);
                        }
                    }
                    else if (ui.getSelectorX() == General.getWidth()/2 - 20) {
                        if (ui.getNumLetters() < 3) {
                            ui.setInitials(ui.getInitials() + "R");
                            ui.setNumLetters(ui.getNumLetters() + 1);
                        }
                    }
                    else if (ui.getSelectorX() == General.getWidth()/2 + 40) {
                        if (ui.getNumLetters() < 3) {
                            ui.setInitials(ui.getInitials() + "S");
                            ui.setNumLetters(ui.getNumLetters() + 1);
                        }
                    }
                    else if (ui.getSelectorX() == General.getWidth()/2 + 100) {
                        if (ui.getNumLetters() < 3) {
                            ui.setInitials(ui.getInitials() + "T");
                            ui.setNumLetters(ui.getNumLetters() + 1);
                        }
                    }
                    else if (ui.getSelectorX() == General.getWidth()/2 + 160) {
                        if (ui.getNumLetters() < 3) {
                            ui.setInitials(ui.getInitials() + "U");
                            ui.setNumLetters(ui.getNumLetters() + 1);
                        }
                    }
                }
                else if (ui.getSelectorY() == General.getHeight()/2 + 100) {
                    if (ui.getSelectorX() == General.getWidth()/2 - 200) {
                        if (ui.getNumLetters() < 3) {
                            ui.setInitials(ui.getInitials() + "V");
                            ui.setNumLetters(ui.getNumLetters() + 1);
                        }
                    }
                    else if (ui.getSelectorX() == General.getWidth()/2 - 140) {
                        if (ui.getNumLetters() < 3) {
                            ui.setInitials(ui.getInitials() + "W");
                            ui.setNumLetters(ui.getNumLetters() + 1);
                        }
                    }
                    else if (ui.getSelectorX() == General.getWidth()/2 - 80) {
                        if (ui.getNumLetters() < 3) {
                            ui.setInitials(ui.getInitials() + "X");
                            ui.setNumLetters(ui.getNumLetters() + 1);
                        }
                    }
                    else if (ui.getSelectorX() == General.getWidth()/2 - 20) {
                        if (ui.getNumLetters() < 3) {
                            ui.setInitials(ui.getInitials() + "Y");
                            ui.setNumLetters(ui.getNumLetters() + 1);
                        }
                    }
                    else if (ui.getSelectorX() == General.getWidth()/2 + 40) {
                        if (ui.getNumLetters() < 3) {
                            ui.setInitials(ui.getInitials() + "Z");
                            ui.setNumLetters(ui.getNumLetters() + 1);
                        }
                    }
                    else if (ui.getSelectorX() == General.getWidth()/2 + 100) {
                        resLoader.writeFile(ui.getInitials(), ui.getScore());
                        restart();
                    }
                    else if (ui.getSelectorX() == General.getWidth()/2 + 160) {
                        if (ui.getNumLetters() > 0) {
                            ui.setNumLetters(ui.getNumLetters() - 1);
                            ui.setInitials(ui.getInitials().substring(0,ui.getNumLetters()));
                        }
                    }
                }
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            if(player.getAlive()) {
                player.playerDeath();
                player.setAlive(false);
            }
            else {
                if(ui.getWaitStart()){
                    if(!ui.getShowHighscore())ui.setShowHighscore(true);
                    else ui.setShowHighscore(false);
                }
                else restart();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            player.setAccelerating(false);
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            player.stopJump();
        }
    }

    private boolean onCollision(float x1, float y1, float x2, float y2, float bubbleWidth, float bubbleHeight) {

        if (x1 >= x2 - bubbleWidth && x1 <= x2 + bubbleWidth && y1 >= y2 - bubbleHeight && y1 <= y2 + bubbleHeight) {
            return true;
        }
        else {
            return false;
        }
    }
    public void spawnObject(){
        int i = 0;
        if(ui.getScore() < 5)i = rn.nextInt(6)+1;
        else if(ui.getScore() < 10)i = rn.nextInt(10)+1;
        else i = rn.nextInt(10)+6;

        if(i==1)objects.add(new Object(General.getWidth(), 390, imgObject.getSubimage(0, 0,150,148), 58, 95, "hydrant")); //hydrant
        if(i==2)objects.add(new Object(General.getWidth(), 390, imgObject.getSubimage(150, 0,150,148), 58, 95, "cone")); //cone
        if(i==3)objects.add(new Object(General.getWidth(), 414, imgObject.getSubimage(300, 0,150,148), 58, 106, "hole")); //hole
        if(i==4)objects.add(new Object(General.getWidth(), 390, imgObject.getSubimage(450, 0,150,148), 58, 95, "bush")); //bush
        if(i==5)objects.add(new Object(General.getWidth(), 380, imgObject.getSubimage(750, 0,150,148), 58, 120, "bin")); //bin
        if(i==6)objects.add(new Object(General.getWidth(), 405, imgObject.getSubimage(900, 0,150,148), 58, 97, "mud")); //mud
        if(i==7){ //hydrant/bush
            objects.add(new Object(General.getWidth(), 390, imgObject.getSubimage(0, 0,150,148), 58, 95, "hydrant"));
            objects.add(new Object(General.getWidth() + 70, 390,  imgObject.getSubimage(450, 0,150,148), 58, 95, "bush"));
        }
        if(i==8){ //cone/cone
            objects.add(new Object(General.getWidth(), 390, imgObject.getSubimage(150, 0,150,148), 58, 95, "cone"));
            objects.add(new Object(General.getWidth() + 70, 390, imgObject.getSubimage(150, 0,150,148), 58, 95, "cone"));
        }
        if(i==9){ //hydrant/bush
            objects.add(new Object(General.getWidth(), 390, imgObject.getSubimage(0, 0,150,148), 58, 95, "hydrant"));
            objects.add(new Object(General.getWidth() + 70, 390, imgObject.getSubimage(450, 0,150,148), 58, 95, "bush"));
        }
        if(i==10){ //cone/cone
            objects.add(new Object(General.getWidth(), 390, imgObject.getSubimage(150, 0,150,148), 58, 95, "cone"));
            objects.add(new Object(General.getWidth() + 70, 390, imgObject.getSubimage(150, 0,150,148), 58, 95, "cone"));
        }


    }
}
