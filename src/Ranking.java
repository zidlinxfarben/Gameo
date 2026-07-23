

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
        tryReadFile("ranking.txt");
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
                a = a.trim();
                c = c.trim();
                if(a.equals("") || c.equals("")){
                    throw new IOException("File corrupted");
                }
                try {
                    b = Integer.parseInt(c);
                } catch (NumberFormatException e) {
                    throw new IOException("File corrupted");
                }
                map.put(a,b);
            }
            else{
                throw new IOException("File corrupted");
            }
        }
        setBackground(Color.white);
    }

    private void tryReadFile(String filename) throws IOException {
        /**
         * try to read file
         * @param filename file name
         */
        this.readFile = new File(filename);
        try {
            this.reader = new Scanner(readFile);
        } catch (FileNotFoundException e) {
            try {
                readFile.createNewFile();
            } catch (IOException ex) {
                throw new RuntimeException("cannot create new file");
            }
            FileWriter rankingWriter = new FileWriter(readFile);
            String[] names = {"a", "b", "c", "d", "e"};
            int[] scores = {3000, 2500, 2000, 1500, 1000};
            for (int i = 0; i < names.length; i++) {
                rankingWriter.append(names[i] + "\n");
                rankingWriter.append(scores[i] + "\n");
            }
            rankingWriter.close();
            this.reader = new Scanner(readFile);
        }
    }

    private String findKey(int a){
        /**
         * find key by value
         * @param a value
         */
        for(Map.Entry u : map.entrySet()){
            if(Objects.equals(a, u.getValue())){
                return (String) u.getKey();
            }
        }
        return null;
    }

    private static Integer[] sortValues(){
        /**
         * sort values
         */
        Integer[] values = new Integer[5];
        int j = 0;
        if(map.size() > 5){
            throw new RuntimeException("Too many scores");
        }
        if(map.size() < 5){
            throw new RuntimeException("Not enough scores");
        }
        for(int i : map.values()){
            values[j] = i;
            j++;
        }
        Arrays.sort(values, Collections.reverseOrder());
        return values;
    }

    public void viewRank(){
        /**
         * view rank via jframe
         */
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

    private static void scoring(Integer[] value, int score, int place){
        /**
         * add new score
         * @param value sorted values
         * @param score new score
         * @param place place on the leaderboard
         */
        int helpingPoint;
        for(; place < value.length; place++){
            helpingPoint = value[place];
            value[place] = score;
            score = helpingPoint;
        }
    }

    public void score(int score){
        /**
         * where is the new score on the leaderboard?
         * @param score new score
         */
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
    private boolean inScores(String name, Integer[] values){
        /**
         * is there the same score under the same name?
         * @param name name
         */
        for(int i : values){
            if(map.get(name).equals(i)){
                return true;
            }
        }
        return false;
    }
    private String SameNaming(String name, int number){
        /**
         * if there is a same name, add number to the end of name
         * @param name name
         * @param number number
         */
        for(String nameKey : map.keySet()){
            if(nameKey.equals(name)){
                name = "" + name + number;
                number++;
                name = SameNaming(name, number);
            }
        }
        return name;
    }
    private void newScore(Integer[] values, int score) {
        /**
         * create as new score
         * @param values sorted values
         * @param score new score
         */
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
        name = name.trim();
        if(name.equals("")){
            name = "Anon";
        }
        if(name.indexOf('\n') != -1){
            name = name.substring(0, name.indexOf('\n'));
        }
        SameNaming(name, 1); // is there a same name? also adds another char
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
        Files.move(writeFile.toPath(), writeFile.toPath().resolveSibling("ranking.txt"), StandardCopyOption.REPLACE_EXISTING);
    }
    private void exiting(){
        removeAll();
        frame.returnState();
    }
}
