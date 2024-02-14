
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class FrameCard extends JFrame {
    private CardLayout cards;
    private Menu menu;
    private Game game;
    private Ranking ranking;
    private JPanel cPane;
    private int current = 0; //control which card is active

    public FrameCard(String title) throws HeadlessException, IOException, UnsupportedAudioFileException, LineUnavailableException { //constructor
        super(title);
        this.cPane = new JPanel();
        this.ranking = new Ranking(this);
        this.game = new Game(ranking, this);
        this.menu = new Menu(this,game, ranking);
        cards = new CardLayout();
        cPane.setLayout(cards);
        setSize(800, 600); //size
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().add(cPane, BorderLayout.NORTH);
        add(cPane);
    }
    public void setting() throws LineUnavailableException, IOException { //adding cards
        cPane.add(menu, "menu");
        cPane.add(ranking, "ranking");
        cPane.add(game, "game");
        cards.first(cPane);
        menu.framing(); //start menu
    }


    public void setMenu(){
        cPane.add(menu, "menu");
    }

    public void playing() { // to run game and switch card to view game
        current = 2;
        cPane.remove(menu); // swing problem here
        cards.show(cPane, "game");
        game.run();
    }

    public void rank() { //to view rank
        current = 1;
        cards.show(cPane, "ranking");
        ranking.viewRank();
    }

    public void returnState(){ //to view menu
        current = 0;
        cards.show(cPane, "menu");
    }
}
