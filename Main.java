import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Visszaszámlálás");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JLabel label = new JLabel("Add meg a jövőbeli időpontot (yyyy-MM-dd HH:mm):");
        JTextField dateField = new JTextField(16);
        JButton startButton = new JButton("Indítás");
        JLabel countdownLabel = new JLabel("Hátralévő idő: ");
        JLabel timeDisplay = new JLabel("00:00:00");

        panel.add(label);
        panel.add(dateField);
        panel.add(startButton);
        panel.add(countdownLabel);
        panel.add(timeDisplay);

        frame.add(panel);
        frame.setVisible(true);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputDate = dateField.getText();

                if (isValidDate(inputDate)) {
                    LocalDateTime targetTime = LocalDateTime.parse(inputDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

                    if (targetTime.isAfter(LocalDateTime.now())) {
                        startCountdown(targetTime, timeDisplay);
                    } else {
                        JOptionPane.showMessageDialog(frame, "A megadott időpont nem lehet korábbi!");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Érvénytelen dátumformátum! (yyyy-MM-dd HH:mm)");
                }
            }
        });
    }

    private static boolean isValidDate(String date) {
        try {
            LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private static void startCountdown(LocalDateTime targetTime, JLabel timeDisplay) {
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDateTime now = LocalDateTime.now();
                if (now.isBefore(targetTime)) {
                    Duration duration = Duration.between(now, targetTime);
                    long hours = duration.toHours();
                    long minutes = duration.toMinutes() % 60;
                    long seconds = duration.getSeconds() % 60;
                    timeDisplay.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
                } else {
                    ((Timer) e.getSource()).stop();
                    JOptionPane.showMessageDialog(null, "Az idő lejárt!");
                }
            }
        });
        timer.start();
    }
}
