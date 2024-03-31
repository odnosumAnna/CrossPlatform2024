import java.awt.EventQueue;
import java.io.IOException;

public class MainProgram {

    public static void main(String[] args) {
        // Запуск графічного інтерфейсу
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    DerivativeApplicationGUI frame = new DerivativeApplicationGUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Виконання алгоритму з класу DerivativeApplication (необов'язково)
        try {
            DerivativeApplication.main(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
