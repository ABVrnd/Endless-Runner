import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

public class ResourceLoader {

    private static String path = System.getProperty("user.home") + File.separator + "Documents/EndlessRunner.txt";

    private static int maxScores = 6;
    public  ArrayList<String> names = new ArrayList<>();
    public  ArrayList<Integer> scores = new ArrayList<>();

    public void readFile() {

        names.clear();
        scores.clear();

        try {

            InputStream filePath = new FileInputStream(path);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(filePath))) {

                String line;

                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    names.add(parts[0]);
                    scores.add(Integer.valueOf(parts[1]));
                }

                reader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException f) {
            f.printStackTrace();
        }
    }

    public  void writeFile(String newName, int newScore) {

        readFile();
        names.add(newName);
        scores.add(newScore);

        for (int i = scores.size()-2; i >= 0; i--) {
            if (newScore > scores.get(i)) {
                Collections.swap(names, i, i+1);
                Collections.swap(scores, i, i+1);
            }
        }

        if (scores.size() > maxScores) {
            for (int i = scores.size()-1; i >= maxScores; i--) {
                names.remove(i);
                scores.remove(i);
            }
        }

        try {

            OutputStream file = new FileOutputStream(path);

            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(file))) {

                for (int i = 0; i < scores.size(); i++) {
                    writer.write(names.get(i) + ";" + scores.get(i));
                    writer.newLine();
                }
                writer.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch (FileNotFoundException f) {
            f.printStackTrace();
        }
    }

    public BufferedImage loadImage(String file) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream filePath = loader.getResourceAsStream("res/images/" + file);
        BufferedImage img = null;
        try {
            img = ImageIO.read(filePath);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    public void loadAudio(String file) {

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(loader.getResourceAsStream("res/sounds/" + file));
            clip.open(inputStream);
            if (file == "bgm.wav") {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            clip.start();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException exx) {
            exx.printStackTrace();
        }
    }
}
