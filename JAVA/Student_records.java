import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class StudentRecordsApp extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtGrade;

    private static final String FILE_NAME = "class_records.csv";

    public StudentRecordsApp() {
        setTitle("Student Records Application");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Table model with column names
        model = new DefaultTableModel(new String[]{"ID", "Name", "Grade"}, 0);
        table = new JTable(model);

        loadFromFile(); // READ on startup

        JScrollPane scrollPane = new JScrollPane(table);

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 5, 5));

        txtId = new JTextField();
        txtName = new JTextField();
        txtGrade = new JTextField();

        JButton btnAdd = new JButton("Add");
        JButton btnDelete = new JButton("Delete");

        inputPanel.add(new JLabel("ID"));
        inputPanel.add(new JLabel("Name"));
        inputPanel.add(new JLabel("Grade"));
        inputPanel.add(new JLabel(""));

        inputPanel.add(txtId);
        inputPanel.add(txtName);
        inputPanel.add(txtGrade);
        inputPanel.add(btnAdd);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnDelete);

        // Add actions
        btnAdd.addActionListener(e -> addRecord());
        btnDelete.addActionListener(e -> deleteRecord());

        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // READ (File I/O)
    private void loadFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                model.addRow(data);
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
        String id = txtId.getText();
        String name = txtName.getText();
        String grade = txtGrade.getText();

        if (id.isEmpty() || name.isEmpty() || grade.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        model.addRow(new String[]{id, name, grade});

        txtId.setText("");
        txtName.setText("");
        txtGrade.setText("");
    }

    // DELETE
    private void deleteRecord() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to delete.");
            return;
        }

        model.removeRow(selectedRow);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StudentRecordsApp().setVisible(true);
        });
    }
}
