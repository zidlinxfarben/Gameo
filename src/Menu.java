

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Menu extends JPanel{
    Button start = new Button("Start");
    Button score = new Button("Score");
    Button end = new Button("End");
    private final FrameCard frame;
    private  Clip music = null;
    private AudioInputStream audioStreamMusic;
    private final Ranking rank;


    public Menu(FrameCard frame, Ranking rank) { //construct Class
        this.frame = frame; // getting frame
        this.rank = rank;
        Font font = new Font("Serif", 0, 42);
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.start.addActionListener(_ -> {
            // add func to run game
            try {
                performing(); //plays the game
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame,ex, "error", JOptionPane.INFORMATION_MESSAGE);
                throw new RuntimeException(ex);
            }
        });
        this.start.setFont(font);
        this.start.setBounds(370, 105, 60, 30);
        this.start.setBackground(Color.CYAN);
        this.add(this.start);
        this.score.addActionListener(_ -> {
            try {
                scoring(); //view score
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame,ex, "error", JOptionPane.INFORMATION_MESSAGE);
                throw new RuntimeException(ex);
            }
        });
        this.score.setFont(font);
        this.score.setBounds(370, 225, 60, 30);
        this.score.setBackground(Color.CYAN);
        this.add(this.score);
        this.end.addActionListener(_ -> { //exiting program
            try {
                music.close();
            }catch (Exception ignored){

            }
            try {
                audioStreamMusic.close();
            } catch (Exception ignored) {}
            try {
                Menu.this.rank.ending();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            System.exit(0);
        });
        this.end.setFont(font);
        this.end.setBounds(370, 345, 60, 30);
        this.end.setBackground(Color.CYAN);
        this.add(this.end);
        try {
            File musicFile = new File("track_2.wav").getAbsoluteFile();
            this.audioStreamMusic = AudioSystem.getAudioInputStream(musicFile); // don't work with only "track_2.wav"
            DataLine.Info info = new DataLine.Info(Clip.class, audioStreamMusic.getFormat());
            this.music = (Clip) AudioSystem.getLine(info);
            this.music.loop(Clip.LOOP_CONTINUOUSLY); //music for menu
        }catch (FileNotFoundException ignored){

        }catch (IOException ignored){
            System.out.println("IOException");
        }catch (UnsupportedAudioFileException ignored){
            System.out.println("UnsupportedAudioFileException");
        }catch (LineUnavailableException ignored){
            System.out.println("LineUnavailableException");
        }
    }

    private void performing(){
        /**
         * start game
         */
        frame.playing();
    }

    private void scoring(){
        /**
         * view score
         */
        frame.rank();
    }

    public void start_music() throws LineUnavailableException{
        /**
         * start music
         */
        try {
            music.open(audioStreamMusic);
            music.start();
        } catch(NullPointerException | IOException ignored){

        }
    }

}
