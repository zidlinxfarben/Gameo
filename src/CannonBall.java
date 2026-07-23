

import java.awt.*;
import java.awt.geom.*;
import java.util.Random;

public class CannonBall{
    public int x = 800;
    public int y;
    protected final int width = 20;
    protected final int height = 20;
    private final Game game;
    private boolean noBlank = true;
    private final int speedX;
    private final int speedY;

    public CannonBall(Game game, Random r) {
        this.game = game;
        this.y = r.nextInt(560);
        this.speedX = r.nextInt(1, 4);
        this.speedY = r.nextInt(-3, 3);
    }

    public void setNoBlank(boolean noBlank) {
        /**
         * set noBlank
         */
        this.noBlank = noBlank;
    }

    public void move(){
        /**
         * move ball
         */
        x = x - speedX - game.speed;
        y = y - speedY;
    }

    public void paint(Graphics2D g){
        /**
         * paint ball
         */
        if(noBlank){
            g.setColor(Color.green);
            g.fillOval(x, y, width, height);
        }
    }

    public Ellipse2D getBounds(){
        /**
         * get bounds
         */
        return new Ellipse2D.Double(x,y, width, height);
    }

    public boolean outOfBonds(){
        /**
         * check if a ball is out of bounds
         */
        if(x<-20){
            return true;
        }
        return y<-20 || y>650;
    }
}
