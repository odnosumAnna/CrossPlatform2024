import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;
import java.util.Arrays;

public class ClassAnalyzerGUI extends JFrame {
    private JTextField classField;
    private JButton analyzeButton;
    private JButton clearButton;
    private JButton exitButton;
    private JTextArea resultArea;

    public ClassAnalyzerGUI() {
        setTitle("Class Analyzer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel classLabel = new JLabel("Class Name:");
        classField = new JTextField(30);
        inputPanel.add(classLabel);
        inputPanel.add(classField);

        analyzeButton = new JButton("Analyze");
        analyzeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                analyzeClass();
            }
        });

        clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearResult();
            }
        });

        exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        buttonPanel.add(analyzeButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(exitButton);

        JPanel resultPanel = new JPanel(new BorderLayout());
        resultArea = new JTextArea(20, 50);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        resultPanel.add(scrollPane, BorderLayout.CENTER);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
        add(resultPanel, BorderLayout.CENTER);
    }

    private void analyzeClass() {
        String className = classField.getText();
        if (className.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a class name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Class<?> clazz = Class.forName(className);
            StringBuilder description = new StringBuilder();

            // Package
            Package pkg = clazz.getPackage();
            if (pkg != null) {
                description.append("package ").append(pkg.getName()).append(";\n");
            }

            // Class modifiers and name
            int modifiers = clazz.getModifiers();
            description.append(Modifier.toString(modifiers)).append(" class ").append(clazz.getSimpleName());

            // Superclass
            Class<?> superclass = clazz.getSuperclass();
            if (superclass != null && superclass != Object.class) {
                description.append(" extends ").append(superclass.getName());
            }

            // Implemented interfaces
            Class<?>[] interfaces = clazz.getInterfaces();
            if (interfaces.length > 0) {
                description.append(" implements ");
                description.append(Arrays.toString(interfaces).replaceAll("[\\[\\]]", ""));
            }

            description.append(" {\n");

            // Fields
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                description.append("\t").append(Modifier.toString(field.getModifiers())).append(" ")
                        .append(field.getType().getSimpleName()).append(" ").append(field.getName()).append(";\n");
            }

            // Constructors
            Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            for (Constructor<?> constructor : constructors) {
                description.append("\t").append(Modifier.toString(constructor.getModifiers())).append(" ")
                        .append(clazz.getSimpleName()).append("(");
                Class<?>[] paramTypes = constructor.getParameterTypes();
                for (int i = 0; i < paramTypes.length; i++) {
                    if (i > 0) description.append(", ");
                    description.append(paramTypes[i].getSimpleName()).append(" arg").append(i);
                }
                description.append(");\n");
            }

            // Methods
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                description.append("\t").append(Modifier.toString(method.getModifiers())).append(" ")
                        .append(method.getReturnType().getSimpleName()).append(" ")
                        .append(method.getName()).append("(");
                Class<?>[] paramTypes = method.getParameterTypes();
                for (int i = 0; i < paramTypes.length; i++) {
                    if (i > 0) description.append(", ");
                    description.append(paramTypes[i].getSimpleName()).append(" arg").append(i);
                }
                description.append(");\n");
            }

            description.append("}");

            resultArea.setText(description.toString());
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Class not found: " + className, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearResult() {
        resultArea.setText("");
        classField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ClassAnalyzerGUI gui = new ClassAnalyzerGUI();
                gui.setVisible(true);
            }
        });
    }
}
