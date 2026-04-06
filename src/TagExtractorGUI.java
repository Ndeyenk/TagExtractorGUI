import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class TagExtractorGUI extends JFrame {

    private JTextArea outputArea;
    private JButton loadTextBtn, loadStopBtn, saveBtn;

    private Map<String, Integer> wordMap = new TreeMap<>();
    private Set<String> stopWords = new TreeSet<>();

    public TagExtractorGUI() {

        setTitle("Tag Extractor");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        outputArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(outputArea);

        loadTextBtn = new JButton("Load Text File");
        loadStopBtn = new JButton("Load Stop Words");
        saveBtn = new JButton("Save Output");

        JPanel panel = new JPanel();
        panel.add(loadTextBtn);
        panel.add(loadStopBtn);
        panel.add(saveBtn);

        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadStopBtn.addActionListener(e -> loadStopWords());
        loadTextBtn.addActionListener(e -> processTextFile());
        saveBtn.addActionListener(e -> saveOutput());

        setVisible(true);
    }

    public static void main(String[] args) {
        new TagExtractorGUI();
    }

    private void loadStopWords() {
        JFileChooser chooser = new JFileChooser();

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (Scanner scanner = new Scanner(chooser.getSelectedFile())) {

                stopWords.clear();

                while (scanner.hasNextLine()) {
                    String word = scanner.nextLine().trim().toLowerCase();
                    if (!word.isEmpty()) {
                        stopWords.add(word);
                    }
                }

                JOptionPane.showMessageDialog(this, "Stop words loaded!");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void processTextFile() {
        JFileChooser chooser = new JFileChooser();

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (Scanner scanner = new Scanner(chooser.getSelectedFile())) {

                wordMap.clear();

                while (scanner.hasNext()) {
                    String word = scanner.next();

                    word = word.replaceAll("[^a-zA-Z]", "").toLowerCase();

                    if (word.isEmpty()) continue;

                    if (stopWords.contains(word)) continue;

                    wordMap.put(word, wordMap.getOrDefault(word, 0) + 1);
                }

                displayResults();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void displayResults() {
        outputArea.setText("");

        for (String word : wordMap.keySet()) {
            outputArea.append(word + " : " + wordMap.get(word) + "\n");
        }
    }

    private void saveOutput() {
        JFileChooser chooser = new JFileChooser();

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter writer = new PrintWriter(chooser.getSelectedFile())) {

                for (String word : wordMap.keySet()) {
                    writer.println(word + " : " + wordMap.get(word));
                }

                JOptionPane.showMessageDialog(this, "File saved!");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}