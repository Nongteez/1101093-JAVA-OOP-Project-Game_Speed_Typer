import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class CustomInput extends JDialog {
    private JTextField textField;
    private JButton saveButton;
    private ArrayList<String> customWords;

    public CustomInput(JFrame parent) {
        super(parent, "Custom Input", true);
        setSize(300, 125);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        customWords = new ArrayList<>();

        textField = new JTextField(20);
        saveButton = new JButton("Save");

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Enter Custom Words: "));
        inputPanel.add(textField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);

        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCustomString(textField.getText());
                textField.setText(""); // Clear the text field after adding
                saveCustomWords(); // Save the custom words
            }
        });
    }

    private void addCustomString(String customString) {
        customWords.add(customString);
    }

    public void saveCustomWords() {
        // เรียงลำดับข้อมูลใน ArrayList
        Collections.sort(customWords);

        try (PrintWriter writer = new PrintWriter(new FileWriter("res/custom.txt"))) {
            // บันทึกข้อมูลที่เรียงลำดับแล้วลงในไฟล์
            for (String word : customWords) {
                writer.println(word);
            }
            JOptionPane.showMessageDialog(this, "Saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
}

