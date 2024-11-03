import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// สร้างคลาส MainMenuButton ให้เป็นซับคลาสของ MainMenu
public class MainMenuButton extends JButton {
    private Color backgroundColor;
    private Color hoverColor;

    // สร้างคอนสตรักเตอร์สำหรับ MainMenuButton
    public MainMenuButton(String text, Color backgroundColor, Color hoverColor) {
        super(text); // เรียกใช้งานคอนสตรักเตอร์ของ JButton เพื่อกำหนดข้อความบนปุ่ม

        this.backgroundColor = backgroundColor;
        this.hoverColor = hoverColor;

        // กำหนดสีพื้นหลังและสีพื้นหลังเมื่อเม้าส์เข้าไป
        setBackground(this.backgroundColor);

        // กำหนดสีของตัวอักษร
        setForeground(Color.BLACK);

        // กำหนดแบบตัวอักษร
        setFont(new Font("Arial", Font.BOLD, 28));

        // ไม่แสดงเส้นขอบ
        setBorderPainted(false);

        // ไม่แสดงเส้นขอบเมื่อไฮไลต์
        setContentAreaFilled(true);

        // ไม่มีเส้นกรอบ
        setFocusPainted(true);

        // เพิ่มเอฟเฟ็กต์เมื่อเมาส์เข้าไป
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(backgroundColor);
            }
        });
    }
}
