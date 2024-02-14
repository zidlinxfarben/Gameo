

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ArrowAction extends AbstractAction {
    private String command;

    public ArrowAction(String command) { // to get command
        this.command = command;
    }

    @Override
    public void actionPerformed(ActionEvent e) { // is it allowed command?
        if (command.equalsIgnoreCase("UpArrow")) { // up key pressed
            Person.keyPressed();
        }else if(command.equalsIgnoreCase("UpArrowReleased")){ // up key pressed
            Person.keyReleased();
        }
    }
}

