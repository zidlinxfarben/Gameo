

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
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
    private boolean close = false;

    public Person(Game game) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.game = game;
    }

    public boolean isClose() { //getter
        return close;
    }

    public void keyPressed(){ //if jumping key is click
        if(!falling()) { //if isn't in air
            up = -2;
            down = false;
        }
    }

    public void keyReleased(){ //if jumping key is realased
        down = true;
    } //now it can fall

    public void move() throws LineUnavailableException, IOException, InterruptedException { //moving
        moving = 0;
        if(((y >= 540 || y<= -height)&&!close)||(shoted()&&!close)){ // is it out of screen
            close = true; // to end game
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
        return new Rectangle(x,y,width,height);
    }

    private boolean falling(){ //if it isn't standing on platform
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
    private boolean shoted(){
        for(CannonBall a : game.balls){
            if(a.getBounds().intersects(this.getBounds())){
                return true;
            }
        }
        return false;
    }

    public void paint(Graphics2D g){ //animating
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);
    }


}
