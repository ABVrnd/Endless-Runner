import javax.swing.*;
import java.awt.*;

public class UI extends JPanel {

    ResourceLoader resLoader;

    private int score;
    private boolean deadScreen;
    private boolean waitStart = false;
    private boolean showHighscore;
    private boolean scoresRead;
    private boolean scoresWritten;

    private int selectorX;
    private int selectorY;
    private int numLetters;
    private String name;
    private String enterInitials = "Enter Initials";
    private String initials = "";
    private String alphabet1 = "A     B     C     D     E     F     G";
    private String alphabet2 = "H      I      J     K     L     M     N";
    private String alphabet3 = "O     P     Q     R     S     T    U";
    private String alphabet4 = "V     W     X     Y     Z";

    public UI(ResourceLoader inResLoader){
        init();
        showHighscore = false;
        resLoader = inResLoader;
    }
    public void init(){
        score = 0;
        waitStart = true;
        deadScreen = false;
        scoresRead = false;
        scoresWritten = false;
        name = "";
        selectorX = General.getWidth()/2 - 200;
        selectorY = General.getHeight()/2 - 50;
        numLetters = 0;
    }
    public void score(){
        score++;
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setFont(new Font("Arial", 0, 50));
        g2d.setColor(Color.BLACK);
        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if(!waitStart)g2d.drawString(""+score, General.getWidth() - 75, 50);
        if(deadScreen){
            if (!scoresRead) {
                resLoader.readFile();
                scoresRead = true;
            }
            g2d.drawString("You Passed Out!", General.getWidth()/2-200, General.getHeight()/2 - 250);
            g2d.setFont(new Font(" Arial", 0, 30));
            g2d.drawString("Your  Score: " + score, General.getWidth()/2-100, General.getHeight()/2 -200);
            for (int i=resLoader.scores.size()-1; i>0; i--) {
                if (score < resLoader.scores.get(i)) {
                    scoresWritten = true;
                }
            }
            if (!scoresWritten ) {
                g2d.drawRect(selectorX - 20, selectorY - 45, 60, 60);
                g2d.drawString(enterInitials, General.getWidth()/2 - 100, General.getHeight()/2 - 150);
                g.drawString(initials, General.getWidth()/2 - 200, General.getHeight()/2 - 100);
                g.drawString(name, General.getWidth()/2 - 50, General.getHeight()/2 - 100);
                g.drawString(alphabet1, General.getWidth()/2 - 200, General.getHeight()/2 - 50);
                g.drawString(alphabet2, General.getWidth()/2 - 200, General.getHeight()/2);
                g.drawString(alphabet3, General.getWidth()/2 - 200, General.getHeight()/2 + 50);
                g.drawString(alphabet4, General.getWidth()/2 - 200, General.getHeight()/2 + 100);
                g.drawString("OK", General.getWidth()/2 + 90, General.getHeight()/2 + 100);
                g.drawString("<", General.getWidth()/2 + 160, General.getHeight()/2 + 100);
            }
            else {
                g.drawString("Press 'Q' to continue", General.getWidth()/2 - 150, General.getHeight()/2 - 100);
            }
            g2d.setFont(new Font("Arial", 0, 25));
        }
        if(waitStart){
            if(showHighscore){
                g2d.setFont(new Font("Arial", 0, 50));
                g2d.drawString("HIGHSCORES", General.getWidth() / 2 - 170, 50);
                g2d.setFont(new Font("Arial", 0, 25));
                if (!scoresRead) {
                    resLoader.readFile();
                    scoresRead = true;
                }
                if (resLoader.scores.size() > 0) {
                    for (int i = 0; i < resLoader.scores.size(); i++) {
                        g2d.drawString(resLoader.names.get(i) + "   " + resLoader.scores.get(i), General.getWidth() / 2 - 30, 75 + (30 * (i + 1)));
                    }
                }
                g2d.drawString("Press UP to Start", General.getWidth() / 2 - 100, 350);
            }
            else {
                g2d.setFont(new Font("Arial", 0, 50));
                g2d.drawString("Press UP to Start", General.getWidth() / 2 - 200, General.getHeight() / 2 - 100);
                g2d.setFont(new Font("Arial", 0, 25));
                g2d.drawString("Press Q for Highscores", General.getWidth() / 2 - 150, General.getHeight() / 2);
            }
        }
    }
    public boolean getDeadScreen() { return deadScreen;}
    public boolean getWaitStart(){
        return waitStart;
    }
    public boolean getShowHighscore(){
        return showHighscore;
    }
    public void setDeadScreen(boolean deadScreen){
        this.deadScreen = deadScreen;
    }

    public void setWaitStart(boolean waitStart) {
        this.waitStart = waitStart;
    }

    public void setShowHighscore(boolean showHighscore) {
        this.showHighscore = showHighscore;
    }

    public void setInitials(String inName) {
        name = inName;
    }

    public String getInitials() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public boolean  getScoresWritten() {
        return scoresWritten;
    }

    public void setNumLetters(int inNum) {
        numLetters = inNum;
    }

    public int getNumLetters() {
        return numLetters;
    }

    public void setSelectorX(int in_x) {
        selectorX = in_x;
    }

    public void setSelectorY(int in_y) {
        selectorY = in_y;
    }

    public int getSelectorX() {
        return selectorX;
    }

    public int getSelectorY() {
        return selectorY;
    }
}
