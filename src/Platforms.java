
import java.awt.*;
import java.util.Random;

public class Platforms {
    public int x = 800;
    public int y;
    protected final int width;
    protected final int height = 10;
    protected boolean fullVisibility = false; // to adding new Platforms
    protected boolean blank; //for spaces
    private final Game game;
    private final Random r;

    public Platforms(boolean blank, Game game, Random r) { //space and platforms
        this.blank = blank;
        this.game = game;
        this.r = r;
        this.y = this.r.nextInt(50, 560); // where can appear
        if(blank){ //is it space?
            this.width = this.r.nextInt(30, 100);
        }
        else {
            this.width = this.r.nextInt(80,200); //for platforms
        }
    }

    public Platforms(int x, int y, int width, Game game, Random r) { // 1st platform only
        this.y = y;
        this.x = x;
        this.width = width;
        this.game = game;
        this.blank = false;
        this.r = r;
    }

    public boolean isBlank() {
        return blank;
    } // is it space?

    public void setBlank(boolean blank) {
        /**
         * set if the platform is space or not
         */
        this.blank = blank;
    }

    public void paint(Graphics2D g){
        /**
         * paint platform
         */
        if(!blank) { //if it isn't space
            g.setColor(Color.gray);
            g.fillRect(x, y, width, height);
        }
    }

    public void move(){
        /**
         * move platform to the left
         */
        x = x - game.speed;
    }

    public Rectangle getBounds(){
        /**
         * get bounds
         */
        return new Rectangle(x,y,width,height);
    }

    public boolean fullVisible(){
        /**
         * sets fullVisibility to true if the platform is fully visible
         */
        if(x + width < 800 && !fullVisibility){
            this.fullVisibility = true;
            return true;
        }
        return false;
    }
    public boolean notVisible(){
        /**
         *  Dit it get out of the screen? 1st platform only
         */
        return x + width <= 0;
    }

    public int getSpeed(){
        /**
         * compatibility function
         */
        return 0;
    }
}
