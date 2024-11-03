import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JLabel;  // นำเข้า JLabel ในที่นี้
import javax.swing.Timer;

public class TypeGame extends JPanel implements KeyListener, ActionListener {

    JTextField currentString;
    JTextArea pointBox;
    ArrayList<String> bank;
    ArrayList<FallingWord> wordsOnBoard;
    private int points;
    private Timer time;
    private int currentTime;
    private int difficulty;

    SoundGame sound;
    BestScore bs;

    public TypeGame(String file) throws FileNotFoundException {
    	bs = new BestScore();
    	sound = new SoundGame();

        setSize(1000, 750);
        setLayout(null);
        bank = Dictionary.getWords(file);
        
        setBackground(Color.BLACK);
        currentString = new JTextField("");
        currentString.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                sendString();
            }

        });
        currentString.setSize(1000, 50);
        currentString.setLocation(0, 660);
        currentString.setBackground(Color.BLUE);
        currentString.setEditable(true);
        currentString.setForeground(Color.white);
        currentString.setFont(currentString.getFont().deriveFont(20f));

        pointBox = new JTextArea("0");
        pointBox.setEditable(false);
        pointBox.setSize(70, 50);
        pointBox.setBackground(Color.BLACK);
        pointBox.setForeground(Color.white);
        pointBox.setFont(pointBox.getFont().deriveFont(30f));
        int pointBoxX = (getWidth() - pointBox.getWidth()) / 2;
        pointBox.setLocation(pointBoxX, 30);

        add(pointBox);
        add(currentString);
        setVisible(true);
        time = new Timer(100, this);
        startNewGame();
    }

    public void startNewGame() {
        points = 0;
        currentTime = 0;
        wordsOnBoard = new ArrayList<FallingWord>();
        difficulty = 0;
        time.start();
        makeNewWord(currentTime);
        sound.GameSound(); // เรียกใช้งานเสียงเมื่อเริ่มเกมใหม่
    }
    
    private void restartGame() {
        int choice = JOptionPane.showConfirmDialog(this, "Do you want to restart the game?", "Restart Game",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            removeWordsOnBoard();
            startNewGame();

            points = 0;
            pointBox.setText("" + points);
            currentString.setText("");

            if (getParent() instanceof JDialog) {
                JDialog gameOverDialog = (JDialog) getParent();
                gameOverDialog.dispose();
            }
        }
    }

    private void endGame() {
        time.stop();

        int finalScore = points; // คะแนนสุดท้าย
        float wpm = calculateWPM(finalScore, currentTime); // คำนวณค่า WPM
        
        int bestScore = bs.getBestScore(); // Retrieve the best score
        if (finalScore > bestScore) {
            bs.saveBestScore(finalScore);
        }
        // Create a custom JDialog for the game over message
        JDialog gameOverDialog = new JDialog();
        gameOverDialog.setLayout(new GridLayout(0, 1));
        gameOverDialog.setSize(300, 200);

        // Add a label with the game over message
        JLabel messageLabel = new JLabel("Game Over!!", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gameOverDialog.add(messageLabel);

        // Add labels with the final score and best score
        JLabel finalScoreLabel = new JLabel("Your Score: " + finalScore, SwingConstants.CENTER);
        finalScoreLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        gameOverDialog.add(finalScoreLabel);
        
        JLabel bestScoreLabel = new JLabel("Best Score: " + bestScore, SwingConstants.CENTER);
        bestScoreLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        gameOverDialog.add(bestScoreLabel);
        
        JLabel wpmScoreLabel = new JLabel("WPM: " + wpm, SwingConstants.CENTER);
        wpmScoreLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        gameOverDialog.add(wpmScoreLabel);

        // Add buttons for restart and back to menu
        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameOverDialog.dispose(); // Close the game over dialog
                restartGame();
            }
        });

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameOverDialog.dispose(); // Close the game over dialog
                backToMenu();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(restartButton);
        buttonPanel.add(backButton);
        gameOverDialog.add(buttonPanel);

        // Center the dialog on the screen
        gameOverDialog.setLocationRelativeTo(this);

        sound.playGameOverSound(); // Play game over sound
        // Show the dialog
        gameOverDialog.setVisible(true);
    }
    
    public void sendString() {
        String entry = currentString.getText();
        currentString.setText("");
        if (wordIsOnBoard(entry)) {
            points = points + entry.length(); //การคิดคะแนน
            pointBox.setText("" + points);
            removeWord(entry);  // เพิ่มการลบคำที่ถูกพิมพ์ถูกต้อง
            updateUI();
        }
    }


    public boolean wordIsOnBoard(String entry) {
        java.util.Iterator<FallingWord> it = wordsOnBoard.iterator();
        while (it.hasNext()) {
            FallingWord current = it.next();
            if (current.equals(entry)) {
                return true;
            }
        }
        return false;
    }
    
   

    private void removeWord(String entry) {
        java.util.Iterator<FallingWord> it = wordsOnBoard.iterator();
        boolean found = false;
        while (it.hasNext() && !found) {
            FallingWord current = it.next();
            if (current.equals(entry)) {
            	sound.playRemoveWordSound(); // เล่นเสียงเมื่อคำถูกลบออก
                remove(current.label);
                it.remove();
                found = true;
            }
        }
    }


    @Override
    public void actionPerformed(ActionEvent arg0) {
        currentTime++;
        moveAllDown();
        if (collison()) {
            endGame();
        }
        adjustDifficulty();
    }

    private void adjustDifficulty() {
        int wordFrequency = 30; // เพิ่มความถี่ของคำที่ตกลงมา

        wordFrequency -= currentTime / 125; // ปรับค่าตามเวลาที่ผ่านไป
        if (wordFrequency < 4) {
            wordFrequency = 4;
        }
        
        if (currentTime % wordFrequency == 0) {
            // difficulty++;
            makeNewWord(2);  // เพิ่มจำนวนคำที่ตกลงมา 2 คำ
        }
    }

    private void makeNewWord(int numberOfWords) {
        for (int i = 0; i < numberOfWords; i++) {
            String randomWord = getRandomWord();
            FallingWord newWord = new FallingWord(randomWord, 3);
            wordsOnBoard.add(newWord);
        }
    }
    
    private float calculateWPM(int finalScore, int currentTime) {
        // ปรับเวลาให้เป็นนาที
        double minutes = currentTime / 60.0;

        // คำนวณ WPM
        float wpm = (float) (finalScore / minutes);
        return Math.round(wpm * 100) / 100.0f; // ปัดเศษทศนิยมให้เหลือ 2 ตำแหน่ง
    }



    
    // เมทอดสำหรับการกลับไปยังหน้าเมนู
    private void backToMenu() {
        // เรียกเมทอดเพื่อกลับไปยังหน้าเมนูหลัก
        MainMenu mainMenu = new MainMenu(); // สร้างหน้าเมนูใหม่
        mainMenu.setVisible(true); // แสดงหน้าเมนู
        this.getParent().setVisible(false); // ซ่อนหน้าเกมปัจจุบัน
    }

    private void removeWordsOnBoard() {
        for (FallingWord word : wordsOnBoard) {
            remove(word.label);
        }
        wordsOnBoard.clear();
        updateUI();
    }

    public boolean collison() {
        java.util.Iterator<FallingWord> it = wordsOnBoard.iterator();
        while (it.hasNext()) {
            FallingWord current = it.next();
            if (current.atBottom()) {
                return true;
            }
        }
        return false;
    }

    private void moveAllDown() {
        java.util.Iterator<FallingWord> it = wordsOnBoard.iterator();
        while (it.hasNext()) {
            FallingWord current = it.next();
            current.updateBox();
        }
        updateUI();
    }

    private String getRandomWord() {
        Random ran = new Random();
        int randomIndex = ran.nextInt(bank.size());
        return bank.get(randomIndex);
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
        ;
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        ;
    }

    @Override
    public void keyTyped(KeyEvent key) {

    }

    private class FallingWord {

        private String word;
        private JLabel label; 
        private int boxVel;
        private int xLoc;
        private int yLoc;

        public FallingWord(String word, int boxVel) {
            Random ran = new Random();
            xLoc = ran.nextInt(900);
            yLoc = 0;
            this.word = word;
            // เพิ่มความเร็วของคำที่ตกลงมา
            this.boxVel = boxVel + (difficulty / 1);
            createBox();
        }

        public boolean atBottom() { // จุดที่แพ้ 655
            return yLoc >= 655;
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof String) {
                String otherword = (String) other;
                return this.word.equals(otherword);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return word.hashCode();
        }

        public void updateBox() {
            yLoc = yLoc + boxVel;
            
            label.setLocation(xLoc, yLoc);
            if (yLoc > 500) {
                label.setOpaque(true);
                label.setForeground(Color.red);

            } else if (yLoc > 110) {
            }
        }

        public void createBox() {
            label = new JLabel(word);
            label.setLocation(xLoc, yLoc);

            // เพิ่มขนาดฟอนต์สำหรับ JLabel
            Font currentFont = label.getFont();
            Font largerFont = new Font(currentFont.getName(), currentFont.getStyle(), currentFont.getSize() + 4);
            label.setFont(largerFont);

            label.setSize(9 * word.length() + 10, 30);
            label.setBackground(Color.black);  
            label.setForeground(Color.white);
            label.setOpaque(true);  
            add(label);
        }

    }
}