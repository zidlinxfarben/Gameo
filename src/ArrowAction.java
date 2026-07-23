

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ArrowAction extends AbstractAction {
    private final String command;
    private final Game game;

    public ArrowAction(String command, Game game) { // to get command
        this.command = command;
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent e) { // is it allowed command?
        Person person = game.player;
        if(person == null) return;
        if (command.equalsIgnoreCase("UpArrow")) { // up key pressed
            person.keyPressed();
        }else if(command.equalsIgnoreCase("UpArrowReleased")){ // up key pressed
            person.keyReleased();
        }
    }
}

