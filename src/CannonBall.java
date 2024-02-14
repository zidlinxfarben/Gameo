

import java.awt.*;
import java.awt.geom.*;
import java.util.Random;

public class CannonBall{
    public int x = 800;
    public int y;
    protected final int width = 20;
    protected final int height = 20;
    private final Game game;
    private final Random r;
    private boolean noBlank = true;
    private final int speedX;
    private final int speedY;
    private boolean unvisibility = false;

    public CannonBall(Game game, Random r) {
        this.game = game;
        this.r = r;
        this.y = r.nextInt(560);
        this.speedX = r.nextInt(1, 4);
        this.speedY = r.nextInt(-3, 3);
    }

    public boolean isNoBlank() {
        return noBlank;
    }

    public void setNoBlank(boolean noBlank) {
        this.noBlank = noBlank;
    }

    public void move(){
        x = x - speedX - game.speed;
        y = y - speedY;
    }

    public void paint(Graphics2D g){
        if(noBlank){
            g.setColor(Color.green);
            g.fillOval(x, y, width, height);
        }
    }

    public Ellipse2D getBounds(){
        return new Ellipse2D.Double(x,y, width, height);
    }

    public boolean outOfBonds(){
        if(x<-20){
            return true;
        }
        return y<-20 || y>650;
    }
}
