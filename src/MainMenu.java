import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

@SuppressWarnings("serial")
public class MainMenu extends JFrame {

    private JPanel cardPanel;
    private CardLayout cardLayout;

    public MainMenu() {
        setSize(1000, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("SpeedTyper");

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Main Menu Panel
        JPanel mainMenuPanel = createMainMenuPanel();
        cardPanel.add(mainMenuPanel, "MainMenu");       

        setContentPane(cardPanel);

        // Start with Main Menu
        cardLayout.show(cardPanel, "MainMenu");
    }

    private JPanel createMainMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(41, 128, 185));

        // Image Panel
        JPanel imagePanel = new JPanel();
        ImageIcon gameIcon = createResizedIcon("res/logo.png", 500, 500);
        JLabel imageLabel = new JLabel(gameIcon);
        imagePanel.add(imageLabel);
        imagePanel.setOpaque(false);
        panel.add(imagePanel, BorderLayout.CENTER);

        // Center Panel for Image
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(imagePanel, BorderLayout.CENTER);
        panel.add(centerPanel, BorderLayout.CENTER);
        
        // Mode Selection and Button Panel
        JPanel modeAndButtonPanel = new JPanel(new BorderLayout());

        // Mode Selection Panel
        JPanel modeSelectionPanel = new JPanel();
        modeSelectionPanel.setOpaque(false);

     // Add label for mode selection
        JLabel modeLabel = new JLabel("Select Mode: ");
        modeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        modeSelectionPanel.add(modeLabel);
        
        // Radio buttons for game modes
        JRadioButton English = new JRadioButton("English");
        JRadioButton Thai = new JRadioButton("Thai");
        JRadioButton Java = new JRadioButton("Java");
        JRadioButton Custom = new JRadioButton("Custom");
        
        // Set font size for radio buttons
        Font radioButtonFont = new Font("Arial", Font.PLAIN, 20);
        English.setFont(radioButtonFont);
        Thai.setFont(radioButtonFont);
        Java.setFont(radioButtonFont);
        Custom.setFont(radioButtonFont);

        // Button group to ensure exclusive selection
        ButtonGroup modeButtonGroup = new ButtonGroup();
        modeButtonGroup.add(English);
        modeButtonGroup.add(Thai);
        modeButtonGroup.add(Java);
        modeButtonGroup.add(Custom);

        // Add radio buttons to the panel
        modeSelectionPanel.add(English);
        modeSelectionPanel.add(Thai);
        modeSelectionPanel.add(Java);
        modeSelectionPanel.add(Custom);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setOpaque(false);
        Dimension buttonPanelSize = new Dimension(500, 150);
        buttonPanel.setPreferredSize(buttonPanelSize);

        // Create MainMenuButton instances
        MainMenuButton startButton = new MainMenuButton("Start", new Color(46, 204, 113), new Color(39, 174, 96));
        MainMenuButton exitButton = new MainMenuButton("Exit", new Color(231, 76, 60), new Color(192, 57, 43));
        MainMenuButton aboutButton = new MainMenuButton("About", new Color(52, 152, 219), new Color(41, 128, 185));
        MainMenuButton customButton = new MainMenuButton("Customize", new Color(255, 255, 153), new Color(255, 255, 153));


     // สร้างอินสแตนซ์ของ BestScore
        BestScore bs = new BestScore();
        
        // Add action listeners to buttons
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String selectedMode = getSelectedMode(English, Thai, Java, Custom);
                    
                    // Perform actions based on the selected mode
                    if ("English".equals(selectedMode)) {
                        // Handle Mode 1
                        TypeGame typeGame = new TypeGame("res/words.txt");
                        cardPanel.add(typeGame, "TypeGame");
                        
                    } else if ("Thai".equals(selectedMode)) {
                        // Handle Mode 2
                        TypeGame typeGame = new TypeGame("res/thai.txt");
                        cardPanel.add(typeGame, "TypeGame");

                    } else if ("Java".equals(selectedMode)) {
                        // Handle Mode 3
                        TypeGame typeGame = new TypeGame("res/java.txt");
                        cardPanel.add(typeGame, "TypeGame");

                    } else if ("Custom".equals(selectedMode)) {
                        // Handle Mode 3
                        TypeGame typeGame = new TypeGame("res/custom.txt");
                        cardPanel.add(typeGame, "TypeGame");
                    }

                    // Switch to the game panel
                    cardLayout.show(cardPanel, "TypeGame");
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                    System.out.println("Error");
                }            
            }

			private String getSelectedMode(JRadioButton... buttons) {
        for (JRadioButton button : buttons) {
            if (button.isSelected()) {
                return button.getText();
            }
        }
        return ""; // Default mode if none selected
    }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle Exit button action
                System.exit(0); // Terminate the program
            }
        });
        
        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle About button action
                JOptionPane.showMessageDialog(MainMenu.this, "SpeedTyper\nVersion 1.0\nThis game was developed for an educational project in object-oriented software development \nbased on"
                		+ " creative thinking or design thinking\n\nThis game is developed by [Group 2].", "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        customButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CustomInput dialog = new CustomInput(MainMenu.this);
                dialog.setVisible(true);
            }
        });



        // Add buttons to the button panel
        buttonPanel.add(startButton);
        buttonPanel.add(customButton);
        buttonPanel.add(aboutButton);
        buttonPanel.add(exitButton);

        // Add components to the main panel
        panel.add(centerPanel, BorderLayout.NORTH);
        panel.add(modeSelectionPanel, BorderLayout.CENTER); 
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private ImageIcon createResizedIcon(String path, int width, int height) {
        try {
            ImageIcon originalIcon = new ImageIcon(path);
            Image originalImage = originalIcon.getImage();
            Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(resizedImage);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MainMenu().setVisible(true);
        });
    }
}
