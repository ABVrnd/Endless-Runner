import javax.swing.*;

public class Main {

    JFrame frame = new JFrame();

    public static void main(String[] args) {
        new Main().init();
    }

    private void init() {

        frame.setSize(General.getWidth(), General.getHeight());
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        run();
    }

    private void run() {

        Main main = new Main();
        ResourceLoader resLoader = new ResourceLoader();
        Game game = new Game(frame, main, resLoader);
        frame.add(game);
        game.setVisible(true);
        frame.revalidate();

        while (game.isEnabled()) {
            game.repaint();
            game.update();
        }
    }
}
