

import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


public class Game extends JPanel{
    public Random r;
    public ArrayList<Platforms> list = new ArrayList<Platforms>();
    public ArrayList<CannonBall> balls = new ArrayList<CannonBall>();
    private boolean LastFullVisible = false;
    public boolean blanking = true; // create space or platform
    public Person player;
    public int score = 0; //score of player
    public int speed = 1; // speed of game
    private final Ranking rank;
    private int i = 0; // countdown to raise speed
    public Color background = new Color(208, 208, 208);
    public final FrameCard frame;
    private final Game game;
    public Timer timer;
    private final InputMap im;
    private final ActionMap am;



    public Game(Ranking rank, FrameCard frame){
        super();
        this.r = new Random(); //creating game with random seed
        this.rank = rank;
        this.frame = frame;
        this.im = this.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
        this.am = this.getActionMap();
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "UpArrow"); //creating key command
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "UpArrow");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), "UpArrowReleased");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "UpArrowReleased");
        am.put("UpArrow", new ArrowAction("UpArrow")); // what to send to ArrowAction
        am.put("UpArrowReleased", new ArrowAction("UpArrowReleased"));
        setBackground(background);
        setFocusable(true); // to always listen
        this.game = this;
        this.timer = new Timer(10, new ActionListener() { // special Thread to play game
            @Override
            public void actionPerformed(ActionEvent e) {
                if(player.isClose()){ // game ended?
                    rank.score(score); // is this score new highscore
                    score = 0; //reset
                    speed = 1; //reset
                    blanking = true;
                    for (Platforms a : list){ //reset
                        a.setBlank(true);
                    }
                    Iterator<CannonBall> ir = balls.iterator();
                    while(ir.hasNext()){
                        CannonBall c = ir.next();
                        c.setNoBlank(false);
                    }
                    frame.setMenu(); // to set back menu to frame
                    frame.returnState(); // to view menu
                    timer.stop(); // end Thread
                }
                if(i>= 1000){
                    speed++;
                    i = 0;
                }
                if(LastFullVisible){ //last space or platform fully visible
                    int a = r.nextInt(4);
                    Platforms b;
                    if(a>0){
                        b = new Platforms(blanking,game, r);
                    }
                    else{
                        if(score>750){
                            b = new MovingPlatforms(blanking,game, r);
                        }
                        else{
                            b = new Platforms(blanking,game, r);
                        }
                    }
                    list.add(b);
                    LastFullVisible = false;
                    blanking = !blanking;
                }
                if(score>1500){
                    if(r.nextInt(150)== 2){
                        CannonBall ball = new CannonBall(game, r);
                        balls.add(ball);
                    }
                }
                Iterator<Platforms> it = list.iterator(); //to delete platform when it isn't on screen
                while (it.hasNext()){
                    Platforms c = it.next();
                    LastFullVisible = c.fullVisible(); //only last get saved
                    if(c.unVisible()){
                        it.remove();
                    }
                }
                Iterator<CannonBall> in = balls.iterator();
                while(in.hasNext()){
                    CannonBall c = in.next();
                    if(c.outOfBonds()){
                        in.remove();
                    }
                }
                revalidate();
                repaint();
                move();
                i++;
                score++;
            }
        });
        this.timer.setRepeats(true); // to repeat infinitely
    }


    @Override
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D gr2d = (Graphics2D)g;
        gr2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gr2d.setColor(Color.BLUE);
        gr2d.drawString("score: " + score, 20, 20); // viewing score
        for(Platforms i : list){ //painting platforms
            i.paint(gr2d);
        }
        for(CannonBall ix : balls){
            ix.paint(gr2d);
        }
        player.paint(gr2d);
    }


    public void move(){
        for(Platforms i : list){ // moving with all platform
            i.move();
        }
        try {
            player.move(); // moving with player
        } catch (LineUnavailableException | InterruptedException | IOException e) {
            JOptionPane.showMessageDialog(frame,e, "error", JOptionPane.INFORMATION_MESSAGE);
            throw new RuntimeException(e);
        }
        for(CannonBall i : balls){
            i.move();
        }
    }



    public void run() { // to start game
        try {
            player = new Person(this); // creating new person
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,e, "error", JOptionPane.INFORMATION_MESSAGE);
            throw new RuntimeException(e);
        }
        Platforms a = new Platforms(0,300, 850, this, r); // creating 1st platform
        list.add(a); // adding a to annother platforms
        timer.start(); // start the game
    }

}
