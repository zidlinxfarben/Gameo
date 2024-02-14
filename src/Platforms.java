
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
        this.y = this.r.nextInt(50, 570); // where can appear
        if(blank){ //is it space
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
        this.blank = blank;
    }

    public void paint(Graphics2D g){
        if(!blank) { //it isn't only space
            g.setColor(Color.gray);
            g.fillRect(x, y, width, height);
        }
    }

    public void move(){ //moving with it
        x = x - game.speed;
    }

    public Rectangle getBounds(){
        return new Rectangle(x,y,width,height);
    } //to get where it is

    public boolean fullVisible(){ // is it appeared on screen in full size
        if(x + width < 800 && !fullVisibility){
            this.fullVisibility = true;
            return true;
        }
        return false;
    }
    public boolean unVisible(){ // is it disappeared from screen? (to delete it)
        return x + width <= 0;
    }

    public int getSpeed(){
        return 0;
    }
}
