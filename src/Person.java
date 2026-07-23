

import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Person {
    public int x = 130;
    public int y = 280;
    private final int width = 10;
    private final int height = 20; // 1st position
    private double up = 0;
    private boolean down = true; //can fall?
    private final Game game;
    private int moving = 0;
    private boolean died = false;

    public Person(Game game) {
        this.game = game;
    }

    public boolean isDead() { //getter
        return died;
    }

    public void keyPressed(){
        /**
         * checking if the jumping key is pressed and if a person is not falling
         */
        if(!falling()) { //if not in air
            up = -2;
            down = false;
        }
    }

    public void keyReleased(){ //if jumping key is released
        down = true;
    } //now it can fall

    public void move() throws LineUnavailableException, IOException, InterruptedException {
        /**
         * moves a person and check if the person died
         */
        moving = 0;
        if(((y >= 540 || y<= -height)&&!died)||(shot()&&!died)){ // is it out of screen? is it shot?
            died = true; // to end game
            JOptionPane.showMessageDialog(game,
                    "Game Over  your score is " + game.score +  "!",
                    "Game over!",
                    JOptionPane.ERROR_MESSAGE);
            down = true;
            up = 0;
        }
        if(falling()){
            y = (int)(y + up); // falling down
            if(down){
                up += 0.035; // to fall slowly
            }
        }
        else if(up < 0){
            y = (int)(y + up); //when it is jumping
        }
        else{
            up = 0;
            y = y + moving;
        }
    }

    public Rectangle getBounds(){
        /**
         * get bounds
         */
        return new Rectangle(x,y,width,height);
    }

    private boolean falling(){
        /**
         * check if a person is falling
         */
        for(Platforms a : game.list){
            if(!a.isBlank()) {
                if (a.getBounds().intersects(this.getBounds())) {
                    moving = a.getSpeed();
                    return false;
                }
            }
        }
        return true;
    }
    private boolean shot(){
        /**
         * check if a person is shot
         */
        for(CannonBall a : game.balls){
            if(a.getBounds().intersects(this.getBounds())){
                return true;
            }
        }
        return false;
    }

    public void paint(Graphics2D g){
        /**
         * paint person
         */
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);
    }


}
