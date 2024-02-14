

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private final Game game;
    private final Ranking rank;
    private Long currentFrame;
    private File musicFile = null;



    public Menu(FrameCard frame, Game game, Ranking rank) throws UnsupportedAudioFileException, IOException, LineUnavailableException { //construct Class
        this.frame = frame; // getting frame
        this.game = game;// center locations
        this.rank = rank;
        Font font = new Font("Serif", 0, 42);
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // add func to run game
                try {
                    performing(); //plays the game
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame,ex, "error", JOptionPane.INFORMATION_MESSAGE);
                    throw new RuntimeException(ex);
                }
            }
        });
        this.start.setFont(font);
        this.start.setBounds(370, 105, 60, 30);
        this.start.setBackground(Color.CYAN);
        this.add(this.start);
        this.score.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    scoring(); //view score
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame,ex, "error", JOptionPane.INFORMATION_MESSAGE);
                    throw new RuntimeException(ex);
                }
            }
        });
        this.score.setFont(font);
        this.score.setBounds(370, 225, 60, 30);
        this.score.setBackground(Color.CYAN);
        this.add(this.score);
        this.end.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { //exiting program
                music.close();
                try {
                    audioStreamMusic.close();
                    Menu.this.rank.ending(); //save score
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, ex, "exit error", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
                System.exit(0);
            }
        });
        this.end.setFont(font);
        this.end.setBounds(370, 345, 60, 30);
        this.end.setBackground(Color.CYAN);
        this.add(this.end);
        try {
            this.musicFile = new File("track_2.wav").getAbsoluteFile();
            this.audioStreamMusic = AudioSystem.getAudioInputStream(musicFile); // don't work with only "track_2.wav"
            DataLine.Info info = new DataLine.Info(Clip.class, audioStreamMusic.getFormat());
            this.music = (Clip) AudioSystem.getLine(info);
            this.music.loop(Clip.LOOP_CONTINUOUSLY); //music for menu
        }catch (FileNotFoundException ignored){

        }
    }

    private void performing() throws InterruptedException, UnsupportedAudioFileException, LineUnavailableException, IOException {
        frame.playing(); //start game
    }

    private void scoring() throws Exception {
        frame.rank(); //view rank
    }

    public void framing() throws LineUnavailableException, IOException { //starts music
        try {
            music.open(audioStreamMusic);
            music.start();
        }catch(FileNotFoundException ignored){

        }
    }

}
