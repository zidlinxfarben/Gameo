

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class MovingPlatforms extends Platforms{
    private int speed;

    public MovingPlatforms(boolean blank, Game game, Random r) {
        super(blank, game, r);
        this.speed = r.nextInt(1, 5);
    }

    @Override
    public int getSpeed() {
        return speed;
    }


    @Override
    public void move(){
        if(y - speed < 35 && speed < 0){
            speed = -speed;
        }
        else if (y - speed > 560 && speed > 0){
            speed = -speed;
        }
        super.move();
        y = y + speed;
    }

    public void paint(Graphics2D g){
        if(!blank) { //it isn't only space
            g.setColor(Color.blue);
            g.fillRect(x, y, width, height);
        }
    }
}
