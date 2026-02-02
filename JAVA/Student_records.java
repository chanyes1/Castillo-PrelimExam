import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class StudentRecordsApp extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtId;
    private JTextField txtFirstName;
    private JTextField txtLastName;

    private static final String FILE_NAME = "class_records.csv";

    public StudentRecordsApp() {
        setTitle("Student Records");
        setSize(900, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // UPDATED columns
        model = new DefaultTableModel(
                new String[]{
                        "Student ID",
                        "First Name",
                        "Last Name",
                        "Lab Work 1",
                        "Lab Work 2",
                        "Lab Work 3",
                        "Prelim Exam",
                        "Attendance Grade"
                }, 0);

        table = new JTable(model);
        loadFromFile();

        JScrollPane scrollPane = new JScrollPane(table);

        txtId = new JTextField();
        txtFirstName = new JTextField();
        txtLastName = new JTextField();

        JButton btnAdd = new JButton("Add");
        JButton btnDelete = new JButton("Delete");

        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        inputPanel.add(new JLabel("Student ID"));
        inputPanel.add(new JLabel("First Name"));
        inputPanel.add(new JLabel("Last Name"));
        inputPanel.add(new JLabel(""));

        inputPanel.add(txtId);
        inputPanel.add(txtFirstName);
        inputPanel.add(txtLastName);
        inputPanel.add(btnAdd);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnDelete);

        btnAdd.addActionListener(e -> addRecord());
        btnDelete.addActionListener(e -> deleteRecord());

        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // READ
    private void loadFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {

                // Skip header
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] data = line.split(",");

                model.addRow(new Object[]{
                        formatStudentId(data[0]),
                        data[1],
                        data[2],
                        data[3],
                        data[4],
                        data[5],
                        data[6], // Prelim Exam
                        data[7]  // Attendance Grade
                });
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error reading file: " + e.getMessage(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // CREATE
    private void addRecord() {
        String rawId = txtId.getText();
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();

        if (!rawId.matches("\\d{9}")) {
            JOptionPane.showMessageDialog(this,
                    "Student ID must be exactly 9 digits.");
            return;
        }

        model.addRow(new Object[]{
                formatStudentId(rawId),
                firstName,
                lastName,
                "", "", "", "", "" // new columns included
        });

        txtId.setText("");
        txtFirstName.setText("");
        txtLastName.setText("");
    }

    // DELETE
    private void deleteRecord() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to delete.");
            return;
        }
        model.removeRow(row);
    }

    // ID formatter
    private String formatStudentId(String id) {
        return id.substring(0, 2) + "-" +
               id.substring(2, 6) + "-" +
               id.substring(6);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new StudentRecordsApp().setVisible(true));
    }
}
