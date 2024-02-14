

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Ranking extends JPanel{
    private FrameCard frame;
    private File readFile;
    public File writeFile;
    private Scanner reader;
    private JLabel label;
    private FileWriter writer;
    public String name;
    public static HashMap<String, Integer> map = new HashMap<>(); //here score
    public Button end =  new Button("Exit");

    public Ranking(FrameCard frame) throws Error, IOException { //constructor
        this.frame = frame;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.readFile = new File("ranking.txt");
        this.reader = new Scanner(readFile);
        this.writeFile = new File("new.txt");
        if(writeFile.createNewFile()){
        }
        this.writer = new FileWriter(writeFile);
        String a;
        String c;
        int b;
        while (reader.hasNext()){
            a = reader.nextLine();
            if(reader.hasNextLine()){
                c=reader.nextLine();
                b = Integer.parseInt(c);
                map.put(a,b);
            }
            else{
                throw new IOException("File corrupted");
            }
        }
        setBackground(Color.white);
    }

    private String findKey(int a){ // to find nkey to value
        for(Map.Entry u : map.entrySet()){
            if(Objects.equals(a, u.getValue())){
                return (String) u.getKey();
            }
        }
        return null;
    }

    private static Integer[] sortValues(){ // sorting too view in order
        Integer[] values = new Integer[5];
        int j = 0;
        for(int i : map.values()){
            values[j] = i;
            j++;
        }
        Arrays.sort(values, Collections.reverseOrder());
        return values;
    }

    public void viewRank(){ //viewing on JFrame
        int rank = 1;
        int y = 30;
        Integer[] values = sortValues();
        Font font = new Font("Serif", 0, 42);
        for(int i : values){
            label = new JLabel(rank + ". " + findKey(i) + "   " + i, SwingConstants.CENTER);
            label.setBounds(370, y, 60, 30);
            label.setFont(font);
            add(label);
            y = y + 50;
            rank++;
        }
        this.end.addActionListener(new ActionListener() { // exit button
            @Override
            public void actionPerformed(ActionEvent e) { //exiting program
                exiting();
            }
        });
        this.end.setFont(font);
        this.end.setBounds(370, 280, 60, 30);
        this.end.setBackground(Color.CYAN);
        this.add(end);
        repaint();
    }

    private static void scoring(Integer[] value, int score, int place){ //add new score
        int helpingPoint;
        for(; place < value.length; place++){
            helpingPoint = value[place];
            value[place] = score;
            score = helpingPoint;
        }
    }

    public void score(int score){ // what is the place
        Integer[] values = sortValues();
        int newPlace = 0;
        boolean newHighScore = false;
        for(int i : values){
            if(score > i){ // if place was found
                newHighScore = true;
                scoring(values, score, newPlace); //add new score
                break;
            }
            newPlace++;
        }
        if(newHighScore){
            newScore(values, score);
        }
    }
    private boolean inScores(String a, Integer[] values){  //is this name in scores
        for(int i : values){
            if(map.get(a) == i){
                return true;
            }
        }
        return false;
    }
    private String SameNaming(String name, int number){ // to remove bug with same names
        for(String a : map.keySet()){
            if(a.equals(name)){
                name = "" + name + number;
                number++;
                name = SameNaming(name, number);
            }
        }
        return name;
    }
    private void newScore(Integer[] values, int score){ //to remove last place and add new name to list
        String b = null;
        for(String a : map.keySet()){
            if(!inScores(a, values)){
                b = a;
            }
        }
        if(b != null) {
            map.remove(b);
        }
        String name=JOptionPane.showInputDialog(frame,"Winner name"); //view dialog to write name
        if(name == null){
            name = "Anon"; // cannot have name which value is null
        }
        SameNaming(name, 1); // is there same name? also adds another char
        map.put(name, score);
    }
    public void ending() throws IOException { // save score
        Integer[] values = sortValues();
        String s;
        boolean a = false;
        for(int i : values){
            s = findKey(i);
            writer.write(s + "\n");
            writer.write(i + "\n");
        }
        reader.close();
        writer.close();
        readFile.delete();
        Files.move(writeFile.toPath(), writeFile.toPath().resolveSibling("ranking.txt"));
    }
    private void exiting(){
        removeAll();
        frame.returnState();
    }
}
