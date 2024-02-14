

import javax.swing.*;

public class Main{



    public static void main(String[] args) {
        // write your code here
        FrameCard frame = null; // because of catching
        try {
            frame = new FrameCard("Gameo"); // create Frame
            frame.setting(); //setting of cards
            frame.setVisible(true); // set visibility
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, e, "Error", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }

    }


}
