

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.List;

public class Ranking extends JPanel{
    private final FrameCard frame;
    private File readFile;
    public File writeFile;
    private Scanner reader;
    private final FileWriter writer;
    public String name;
    public Button end =  new Button("Exit");
    public static List<Entry> scores = new ArrayList<>();
    private static final int MAX_ENTRIES = 5;


    public Ranking(FrameCard frame) throws Error, IOException { //constructor
        this.frame = frame;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        tryReadFile("ranking.txt");
        this.writeFile = new File("new.txt");
        writeFile.createNewFile();
        this.writer = new FileWriter(writeFile);
        String a;
        String c;
        int b;
        scores.clear();
        while (reader.hasNext()){
            a = reader.nextLine();
            if(reader.hasNextLine()){
                c = reader.nextLine().trim();
                a = a.trim();
                if(a.isEmpty() || c.isEmpty()) throw new IOException("File corrupted");
                try { b = Integer.parseInt(c); }
                catch (NumberFormatException e) { throw new IOException("File corrupted"); }
                scores.add(new Entry(a, b));
            } else {
                throw new IOException("File corrupted");
            }
        }
        setBackground(Color.white);
    }

    private void tryReadFile(String filename) throws IOException {
        /**
         * try to read a file
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


    private static void sortDescending(){
        scores.sort((x, y) -> Integer.compare(y.score, x.score));
    }

    public void viewRank(){
        /**
         * view rank via jframe
         */
        int rank = 1;
        int y = 30;
        Font font = new Font("Serif", 0, 42);
        for(Entry e : scores){
            JLabel label = new JLabel(rank + ". " + e.name + "   " + e.score, SwingConstants.CENTER);
            label.setBounds(370, y, 60, 30);
            label.setFont(font);
            add(label);
            y = y + 50;
            rank++;
        }
        // exit button
        this.end.addActionListener(_ -> { //exiting program
            exiting();
        });
        this.end.setFont(font);
        this.end.setBounds(370, 280, 60, 30);
        this.end.setBackground(Color.CYAN);
        this.add(end);
        repaint();
    }


    public void score(int scored){
        /**
         * where is the new score on the leaderboard?
         * @param score new score
         */
        sortDescending();
        boolean isHighScore = scores.size() < MAX_ENTRIES
                || scored > scores.get(scores.size() - 1).score;
        if(isHighScore) {
            newScore(scored);
        }
    }

    private void newScore(int score) {
        /**
         * create as new score
         * @param score new score
         */
        String name = JOptionPane.showInputDialog(frame, "Winner name");
        if(name == null) name = "Anon";
        name = name.trim();
        if(name.isEmpty()) name = "Anon";
        int nl = name.indexOf('\n');
        if(nl != -1){
            name = name.substring(0, nl);
        }
        name = name.replaceAll("\\s","_");
        scores.add(new Entry(name, score));
        sortDescending();
        while(scores.size() > MAX_ENTRIES) {
            scores.remove(scores.size() - 1);        // drop the lowest
        }
    }
    public void ending() throws IOException { // save score
        sortDescending();
        for(Entry e : scores){
            writer.write(e.name + "\n");
            writer.write(e.score + "\n");
        }
        reader.close();
        writer.close();
        Path target = readFile.toPath(); // ranking.txt
        try {
            Files.move(writeFile.toPath(), target,
                    StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(writeFile.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
        }
    }
    private void exiting(){
        removeAll();
        frame.returnState();
    }
    public static class Entry {
        /**
         * entry in the ranking
         */
        public final String name;
        public final int score;
        public Entry(String name, int score){ this.name = name; this.score = score; }
    }
}
