import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

public class db_gui {
    class ImagePanel extends JPanel {

        private Image backgroundImg;

        public ImagePanel(String imgPath) {
            this(new ImageIcon(imgPath).getImage());
        }

        public ImagePanel(Image img) {
            this.backgroundImg = img;
            Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
            setPreferredSize(size);
            setMinimumSize(size);
            setMaximumSize(size);
            setSize(size);
            setLayout(null);
        }

        public void paintComponent(Graphics g) {
            g.drawImage(backgroundImg, 0, 0, null);
        }
    }

    class CheckboxRenderer extends DefaultTableCellRenderer {
        private JCheckBox checkBox = new JCheckBox();

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value != null && value instanceof Boolean) {
                checkBox.setSelected((Boolean) value);
            } else {
                checkBox.setSelected(false);
            }
            return checkBox;
        }
    }

    class CheckboxEditor extends AbstractCellEditor implements TableCellEditor {
        private JCheckBox checkBox = new JCheckBox();

        public CheckboxEditor() {
            checkBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    stopCellEditing();
                }
            });
        }

        public Object getCellEditorValue() {
            return checkBox.isSelected();
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value != null && value instanceof Boolean) {
                checkBox.setSelected((Boolean) value);
            } else {
                checkBox.setSelected(false);
            }
            return checkBox;
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            return true;
        }
    }



    private static Connection conn;
    private JFrame mainFrame;
    private JTextField searchTextField;
    private JTextField fname_input;
    private JTextField minit_input;
    private JTextField lname_input;
    private JTextField ssn_input;
    private JTextField bdate_input;
    private JTextField add_input;
    private JTextField salary_input;
    private JTextField superSsn_input;
    private JTextField dno_input;
    private JTextField update_input_text;
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JTable table_1;
    private JPanel noresults;
    private JLabel selected_employees;
    private JLabel selected_employee_num;

    public static void connectDB() throws SQLException{
        try
        {
            String user = "root";
            String pwd = "root"; // 비밀번호 입력
            String dbname = "company"; // 스키마 이름 입력
            String url = "jdbc:mysql://localhost:3306/" + dbname + "?serverTimezone=UTC";
            conn = DriverManager.getConnection(url, user, pwd);
            System.out.println("정상적으로 연결되었습니다.");
        }
        catch (SQLException e1) {
            System.err.println("연결할 수 없습니다.");
            e1.printStackTrace();
            // SQL 예외가 발생했을 때 예외 처리
            String errorMessage = "SQL 오류 : " + e1.getMessage();
            JOptionPane.showMessageDialog(null, errorMessage, "SQL Errror", JOptionPane.ERROR_MESSAGE);
        }
    }
    public static ResultSet rs(Connection conn , String stmt) {
        try {
            Statement s = conn.createStatement();
            return s.executeQuery(stmt);
        } catch (SQLException e1) {
            e1.printStackTrace();
            return null;
        }
    }
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    db_gui window = new db_gui();
                    window.mainFrame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    String errorMessage = e.getMessage();
                    JOptionPane.showMessageDialog(null, errorMessage, "Errror", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public db_gui() {
        initialize();
    }

    private void initialize() {
        mainFrame = new JFrame("Information Retrieval System");
        mainFrame.getContentPane().setLocation(111, 0);
        mainFrame.setResizable(false);
        mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("images/kau-logo.png"));
        mainFrame.getContentPane().setBackground(new Color(255, 254, 254));
        mainFrame.setSize(984, 656);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.getContentPane().setLayout(null);

        JPanel searchConditionsPanel = new JPanel();
        searchConditionsPanel.setBounds(98, 36, 859, 76);
        searchConditionsPanel.setBackground(new Color(56, 53, 118));
        searchConditionsPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        mainFrame.getContentPane().add(searchConditionsPanel);
        searchConditionsPanel.setLayout(null);

        JLabel searchRangeLabel = new JLabel("Range");
        searchRangeLabel.setBackground(new Color(55, 52, 114));
        searchRangeLabel.setForeground(new Color(254, 255, 255));
        searchRangeLabel.setBounds(16, 17, 85, 16);
        searchConditionsPanel.add(searchRangeLabel);

        JLabel searchItemLabel = new JLabel("Item");
        searchItemLabel.setBackground(new Color(55, 52, 114));
        searchItemLabel.setForeground(new Color(254, 255, 255));
        searchItemLabel.setBounds(16, 48, 74, 16);
        searchConditionsPanel.add(searchItemLabel);

        JCheckBox nameCheckBox = new JCheckBox("Name");
        nameCheckBox.setBackground(new Color(55, 52, 114));
        nameCheckBox.setSelectedIcon(new ImageIcon("images/check_selected.png"));
        nameCheckBox.setPressedIcon(new ImageIcon("images/check_hovered.png"));
        nameCheckBox.setIcon(new ImageIcon("images/check_idle.png"));
        nameCheckBox.setFocusable(false);
        nameCheckBox.setFocusPainted(false);
        nameCheckBox.setForeground(new Color(254, 255, 255));
        nameCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
        nameCheckBox.setSelected(true);
        nameCheckBox.setFont(new Font("Kohinoor Telugu", Font.PLAIN, 12));
        nameCheckBox.setBounds(63, 45, 75, 23);
        searchConditionsPanel.add(nameCheckBox);

        JCheckBox ssnCheckBox = new JCheckBox("Ssn");
        ssnCheckBox.setBackground(new Color(55, 52, 114));
        ssnCheckBox.setSelectedIcon(new ImageIcon("images/check_selected.png"));
        ssnCheckBox.setPressedIcon(new ImageIcon("images/check_hovered.png"));
        ssnCheckBox.setIcon(new ImageIcon("images/check_idle.png"));
        ssnCheckBox.setFocusable(false);
        ssnCheckBox.setFocusPainted(false);
        ssnCheckBox.setForeground(new Color(254, 255, 255));
        ssnCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
        ssnCheckBox.setSelected(true);
        ssnCheckBox.setFont(new Font("Kohinoor Telugu", Font.PLAIN, 12));
        ssnCheckBox.setBounds(135, 45, 61, 23);
        searchConditionsPanel.add(ssnCheckBox);

        JCheckBox bdateCheckBox = new JCheckBox("Bdate");
        bdateCheckBox.setBackground(new Color(55, 52, 114));
        bdateCheckBox.setSelectedIcon(new ImageIcon("images/check_selected.png"));
        bdateCheckBox.setPressedIcon(new ImageIcon("images/check_hovered.png"));
        bdateCheckBox.setIcon(new ImageIcon("images/check_idle.png"));
        bdateCheckBox.setFocusable(false);
        bdateCheckBox.setFocusPainted(false);
        bdateCheckBox.setForeground(new Color(254, 255, 255));
        bdateCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
        bdateCheckBox.setSelected(true);
        bdateCheckBox.setFont(new Font("Kohinoor Telugu", Font.PLAIN, 12));
        bdateCheckBox.setBounds(194, 45, 85, 23);
        searchConditionsPanel.add(bdateCheckBox);

        JCheckBox addressCheckBox = new JCheckBox("Address");
        addressCheckBox.setBackground(new Color(55, 52, 114));
        addressCheckBox.setSelectedIcon(new ImageIcon("images/check_selected.png"));
        addressCheckBox.setPressedIcon(new ImageIcon("images/check_hovered.png"));
        addressCheckBox.setIcon(new ImageIcon("images/check_idle.png"));
        addressCheckBox.setFocusable(false);
        addressCheckBox.setFocusPainted(false);
        addressCheckBox.setForeground(new Color(254, 255, 255));
        addressCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
        addressCheckBox.setSelected(true);
        addressCheckBox.setFont(new Font("Kohinoor Telugu", Font.PLAIN, 12));
        addressCheckBox.setBounds(276, 45, 83, 23);
        searchConditionsPanel.add(addressCheckBox);

        JCheckBox sexCheckBox = new JCheckBox("Sex");
        sexCheckBox.setBackground(new Color(55, 52, 114));
        sexCheckBox.setSelectedIcon(new ImageIcon("images/check_selected.png"));
        sexCheckBox.setPressedIcon(new ImageIcon("images/check_hovered.png"));
        sexCheckBox.setIcon(new ImageIcon("images/check_idle.png"));
        sexCheckBox.setFocusable(false);
        sexCheckBox.setFocusPainted(false);
        sexCheckBox.setForeground(new Color(254, 255, 255));
        sexCheckBox.setSelected(true);
        sexCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
        sexCheckBox.setFont(new Font("Kohinoor Telugu", Font.PLAIN, 12));
        sexCheckBox.setBounds(356, 45, 74, 23);
        searchConditionsPanel.add(sexCheckBox);

        JCheckBox salaryCheckBox = new JCheckBox("Salary");
        salaryCheckBox.setBackground(new Color(55, 52, 114));
        salaryCheckBox.setSelectedIcon(new ImageIcon("images/check_selected.png"));
        salaryCheckBox.setPressedIcon(new ImageIcon("images/check_hovered.png"));
        salaryCheckBox.setIcon(new ImageIcon("images/check_idle.png"));
        salaryCheckBox.setFocusable(false);
        salaryCheckBox.setFocusPainted(false);
        salaryCheckBox.setForeground(new Color(254, 255, 255));
        salaryCheckBox.setSelected(true);
        salaryCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
        salaryCheckBox.setFont(new Font("Kohinoor Telugu", Font.PLAIN, 12));
        salaryCheckBox.setBounds(428, 45, 75, 23);
        searchConditionsPanel.add(salaryCheckBox);

        JCheckBox supervisorCheckBox = new JCheckBox("Supervisor");
        supervisorCheckBox.setBackground(new Color(55, 52, 114));
        supervisorCheckBox.setSelectedIcon(new ImageIcon("images/check_selected.png"));
        supervisorCheckBox.setPressedIcon(new ImageIcon("images/check_hovered.png"));
        supervisorCheckBox.setIcon(new ImageIcon("images/check_idle.png"));
        supervisorCheckBox.setFocusable(false);
        supervisorCheckBox.setFocusPainted(false);
        supervisorCheckBox.setForeground(new Color(254, 255, 255));
        supervisorCheckBox.setSelected(true);
        supervisorCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
        supervisorCheckBox.setFont(new Font("Kohinoor Telugu", Font.PLAIN, 12));
        supervisorCheckBox.setBounds(504, 45, 98, 23);
        searchConditionsPanel.add(supervisorCheckBox);

        JCheckBox departmentCheckBox = new JCheckBox("Department");
        departmentCheckBox.setBackground(new Color(55, 52, 114));
        departmentCheckBox.setSelectedIcon(new ImageIcon("images/check_selected.png"));
        departmentCheckBox.setPressedIcon(new ImageIcon("images/check_hovered.png"));
        departmentCheckBox.setIcon(new ImageIcon("images/check_idle.png"));
        departmentCheckBox.setFocusable(false);
        departmentCheckBox.setFocusPainted(false);
        departmentCheckBox.setForeground(new Color(254, 255, 255));
        departmentCheckBox.setSelected(true);
        departmentCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
        departmentCheckBox.setFont(new Font("Kohinoor Telugu", Font.PLAIN, 12));
        departmentCheckBox.setBounds(596, 45, 112, 23);
        searchConditionsPanel.add(departmentCheckBox);

        JComboBox<String> searchItemComboBox = new JComboBox<String>();
        searchItemComboBox.setFocusTraversalPolicyProvider(true);
        searchItemComboBox.setFocusTraversalKeysEnabled(false);
        searchItemComboBox.setModel(new DefaultComboBoxModel(new String[] {"All", "Name", "Ssn", "Bdate", "Address", "Sex", "Salary", "Supervisor (Ssn)", "Department"}));
        searchItemComboBox.setBounds(63, 13, 133, 27);
        searchConditionsPanel.add(searchItemComboBox);


        JComboBox<String> searchComboBox = new JComboBox<String>();
        searchComboBox.setBounds(201, 13, 165, 27);
        searchConditionsPanel.add(searchComboBox);


        JButton searchButton = new JButton("Search");
        searchButton.setSelectedIcon(new ImageIcon("images/search_selected.png"));
        searchButton.setRolloverIcon(new ImageIcon("images/search_selected.png"));
        searchButton.setRolloverSelectedIcon(new ImageIcon("images/search_selected.png"));
        searchButton.setRequestFocusEnabled(false);
        searchButton.setFocusable(false);
        searchButton.setFocusPainted(false);
        searchButton.setFocusTraversalKeysEnabled(false);
        searchButton.setDefaultCapable(false);
        searchButton.setContentAreaFilled(false);
        searchButton.setBorderPainted(false);
        searchButton.setBackground(new Color(55, 53, 113));
        searchButton.setIcon(new ImageIcon("images/search_idle.png"));
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (e.getSource() == searchButton) {
                    String stmt = "SELECT ";

                    if (nameCheckBox.isSelected()) {
                        stmt += "concat(e.fname, ' ', e.minit, ' ', e.lname, ' ') as Name";
                    }

                    if (ssnCheckBox.isSelected()) {
                        if (!stmt.equals("SELECT ")) {
                            stmt += ", ";
                        }
                        stmt += "e.ssn";
                    }

                    if (bdateCheckBox.isSelected()) {
                        if (!stmt.equals("SELECT ")) {
                            stmt += ", ";
                        }
                        stmt += "e.bdate";
                    }

                    if (addressCheckBox.isSelected()) {
                        if (!stmt.equals("SELECT ")) {
                            stmt += ", ";
                        }
                        stmt += "e.address";
                    }

                    if (sexCheckBox.isSelected()) {
                        if (!stmt.equals("SELECT ")) {
                            stmt += ", ";
                        }
                        stmt += "e.sex";
                    }

                    if (salaryCheckBox.isSelected()) {
                        if (!stmt.equals("SELECT ")) {
                            stmt += ", ";
                        }
                        stmt += "e.salary";
                    }

                    if (supervisorCheckBox.isSelected()) {
                        if (!stmt.equals("SELECT ")) {
                            stmt += ", ";
                        }
                        stmt += "concat(s.fname, ' ', s.minit, ' ', s.lname, ' ') as Supervisor";
                    }

                    if (departmentCheckBox.isSelected()) {
                        if (!stmt.equals("SELECT ")) {
                            stmt += ", ";
                        }
                        stmt += "dname";
                    }

                    stmt += " FROM employee e LEFT OUTER JOIN employee s ON e.super_ssn = s.ssn, department WHERE e.dno = dnumber";

                    if (searchItemComboBox.getSelectedItem().toString().equals("Name")) {
                        String fname = searchTextField.getText();
                        String[] nameParts = fname.split(" ");

                        if (nameParts.length == 1) {
                            // 단일 이름으로 검색
                            stmt += " and (e.fname = '" + nameParts[0] + "' OR e.lname = '" + nameParts[0] + "' OR e.minit = '" + nameParts[0] + "')";
                        } else if (nameParts.length == 2) {
                            // 이름과 성으로 검색
                            stmt += " and (e.fname = '" + nameParts[0] + "' AND e.minit = '" + nameParts[1] + "')";
                        } else if (nameParts.length == 3) {
                            // 이름, 중간 이름, 성으로 검색
                            stmt += " and (e.fname = '" + nameParts[0] + "' AND e.minit = '" + nameParts[1] + "' AND e.lname = '" + nameParts[2] + "')";
                        }
                    }

                    if (searchItemComboBox.getSelectedItem().toString() == ("Ssn")) {
                        String fssn = searchTextField.getText();
                        stmt += " and e.ssn = '" + fssn + "'";
                    }
                    if (searchItemComboBox.getSelectedItem().toString() == ("Bdate")) {
                        String fbdate = searchTextField.getText();
                        //비워져있으면 NoResult
                        if (fbdate.isEmpty()) {
                            fbdate = "error";
                        }
                        stmt += " and e.bdate like '%" + fbdate + "%'";
                    }
                    if (searchItemComboBox.getSelectedItem().toString() == ("Address")) {
                        String faddress = searchTextField.getText();
                        //비워져있으면 NoResult
                        if (faddress.isEmpty()) {
                            faddress = "error";
                        }
                        stmt += " and e.address like '%" + faddress + "%'";
                    }
                    if (searchItemComboBox.getSelectedItem().toString() == ("Sex")) {
                        if(searchComboBox.getSelectedItem().toString() == ("M")) {
                            String fsex = "M";
                            stmt += " and e.sex = '"+ fsex +"'";
                        }
                        else if(searchComboBox.getSelectedItem().toString() == ("F")) {
                            String fsex = "F";
                            stmt += " and e.sex = '"+ fsex +"'";
                        }
                    }
                    if (searchItemComboBox.getSelectedItem().toString() == ("Salary")) {
                        String fsalary = searchTextField.getText();
                        //숫자로 이뤄져있는지 판단
                        if (!fsalary.matches("^\\d+$")) {
                            fsalary = "99999999999999";
                        }
                        stmt += " and e.salary >= '" + fsalary + "'";
                    }
                    if (searchItemComboBox.getSelectedItem().toString() == ("Supervisor")) {
                        String fsupervisor_ssn = searchTextField.getText();
                        stmt += " and e.super_ssn = '" + fsupervisor_ssn + "'";
                    }
                    if (searchItemComboBox.getSelectedItem().toString() == ("Department")) {
                        if(searchComboBox.getSelectedItem().toString() == ("Research")) {
                            String fdepartment = "Research";
                            stmt += " and dname = '"+ fdepartment +"'";
                        }
                        else if(searchComboBox.getSelectedItem().toString() == ("Administration")) {
                            String fdepartment = "Administration";
                            stmt += " and dname = '"+ fdepartment +"'";
                        }
                        else if(searchComboBox.getSelectedItem().toString() == ("Headquarters")) {
                            String fdepartment = "Headquarters";
                            stmt += " and dname = '"+ fdepartment +"'";
                        }
                    }

                    try {
                        connectDB();
                        ResultSet rs = rs(conn,stmt);
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int cols = rsmd.getColumnCount();

                        noresults.setVisible(false);
                        tableModel.setRowCount(0);
                        tableModel.setColumnCount(0);

                        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                            tableModel.addColumn(rsmd.getColumnName(i));

                        }

                        while (rs.next()) {
                            String[] rowData = new String[cols];
                            for (int i = 1; i <= cols; i++) {
                                Object columnValue = rs.getObject(i);
                                rowData[i - 1] = columnValue != null ? columnValue.toString() : "NULL";
                                if (columnValue != null) {
                                    System.out.printf(rsmd.getColumnName(i)+":"+"%-20s\t", columnValue.toString());

                                } else {
                                    System.out.printf("%-20s\t", "NULL");
                                }
                            }
                            System.out.println();
                            tableModel.addRow(rowData);
                        }
                        tableModel.addColumn("check");
                        tableModel.fireTableDataChanged();
                        tableModel.fireTableStructureChanged();
                        TableColumn checkboxColumn = employeeTable.getColumnModel().getColumn(cols); // 0번째 열 (왼쪽 열)에 체크박스 추가
                        checkboxColumn.setCellRenderer(new CheckboxRenderer());
                        checkboxColumn.setCellEditor(new CheckboxEditor());

                        if (tableModel.getRowCount() <= 0 && noresults.isVisible() == false) {
                            noresults.setVisible(true);
                        } else {
                            noresults.setVisible(false);
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }

                    System.out.println(stmt);
                }
            }
        });
        searchButton.setBounds(720, 17, 118, 45);
        searchConditionsPanel.add(searchButton);

        table_1 = new JTable();
        table_1.setBounds(0, 0, 1, 1);
        searchConditionsPanel.add(table_1);

        searchTextField = new JTextField();
        searchTextField.setBounds(203, 12, 163, 26);
        searchConditionsPanel.add(searchTextField);
        searchTextField.setColumns(10);
        JPanel sidebarPanel = new ImagePanel("images/sidebar.png");
        sidebarPanel.setBounds(6, 6, 80, 616);
        mainFrame.getContentPane().add(sidebarPanel);
        sidebarPanel.setLayout(null);

        JRadioButton homeRadioButton = new JRadioButton("");
        homeRadioButton.setBackground(new Color(53, 48, 112));
        homeRadioButton.setRequestFocusEnabled(false);
        homeRadioButton.setVerifyInputWhenFocusTarget(false);
        homeRadioButton.setFocusable(false);
        homeRadioButton.setFocusPainted(false);
        homeRadioButton.setSelectedIcon(new ImageIcon("images/icon1_selected.png"));
        homeRadioButton.setIcon(new ImageIcon("images/icon1_idle.png"));
        homeRadioButton.setSelected(true);
        homeRadioButton.setBounds(6, 34, 62, 40);
        sidebarPanel.add(homeRadioButton);

        JRadioButton searchRadioButton = new JRadioButton("");
        searchRadioButton.setBackground(new Color(49, 43, 103));
        searchRadioButton.setFocusable(false);
        searchRadioButton.setFocusPainted(false);
        searchRadioButton.setIcon(new ImageIcon("images/icon2_idle.png"));
        searchRadioButton.setSelectedIcon(new ImageIcon("images/icon2_selected.png"));
        searchRadioButton.setBounds(10, 86, 61, 55);
        sidebarPanel.add(searchRadioButton);

        JRadioButton addRadioButton = new JRadioButton("");
        addRadioButton.setBackground(new Color(45, 40, 93));
        addRadioButton.setFocusPainted(false);
        addRadioButton.setFocusable(false);
        addRadioButton.setVerifyInputWhenFocusTarget(false);
        addRadioButton.setRequestFocusEnabled(false);
        addRadioButton.setIcon(new ImageIcon("images/icon3_idle.png"));
        addRadioButton.setSelectedIcon(new ImageIcon("images/icon3_selected.png"));
        addRadioButton.setBounds(10, 144, 61, 62);
        sidebarPanel.add(addRadioButton);

        JRadioButton deleteRadioButton = new JRadioButton("");
        deleteRadioButton.setBackground(new Color(38, 33, 80));
        deleteRadioButton.setVerifyInputWhenFocusTarget(false);
        deleteRadioButton.setRequestFocusEnabled(false);
        deleteRadioButton.setFocusable(false);
        deleteRadioButton.setFocusPainted(false);
        deleteRadioButton.setSelectedIcon(new ImageIcon("images/icon4_selected.png"));
        deleteRadioButton.setIcon(new ImageIcon("images/icon4_idle.png"));
        deleteRadioButton.setBounds(10, 212, 61, 47);
        sidebarPanel.add(deleteRadioButton);

        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(deleteRadioButton);
        radioGroup.add(addRadioButton);
        radioGroup.add(searchRadioButton);
        radioGroup.add(homeRadioButton);

        noresults = new ImagePanel("images/noresults.png");
        noresults.setBounds(226, 30, 425, 299);
        noresults.setVisible(true);

        JPanel searchScreenPanel = new JPanel();
        searchScreenPanel.setBorder(null);
        searchScreenPanel.setBounds(101, 119, 860, 472);
        searchScreenPanel.add(noresults);

        tableModel = new DefaultTableModel();
        mainFrame.getContentPane().add(searchScreenPanel);
        searchScreenPanel.setBackground(new Color(255, 254, 254));
        searchScreenPanel.setLayout(null);
        searchScreenPanel.setLayout(null);

        employeeTable = new JTable(tableModel);
        employeeTable.setUpdateSelectionOnSort(false);
        employeeTable.setFocusTraversalKeysEnabled(false);
        employeeTable.setFocusable(false);
        employeeTable.setRowSelectionAllowed(false);
        employeeTable.setRequestFocusEnabled(false);

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setForeground(new Color(0, 0, 0));
        scrollPane.setFocusTraversalKeysEnabled(false);
        scrollPane.setEnabled(false);
        scrollPane.setFocusable(false);
        Dimension tableSize = employeeTable.getPreferredSize();
        scrollPane.setPreferredSize(new Dimension(tableSize.width, tableSize.height));
        scrollPane.setBounds(0, 0, 860, 330);
        scrollPane.setBorder(null);
        searchScreenPanel.add(scrollPane);
        employeeTable.setSurrendersFocusOnKeystroke(true);
        employeeTable.setGridColor(new Color(55, 52, 114));
        employeeTable.setBounds(0, 0, 0, 326);
        employeeTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        employeeTable.getTableHeader().setReorderingAllowed(false);
        employeeTable.setBorder(null);
        employeeTable.setBackground(new Color(254, 255, 255));
        employeeTable.setFillsViewportHeight(true);
        employeeTable.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    try {
                        int checkIndex = employeeTable.getColumnModel().getColumnIndex("check");
                        if (checkIndex >= 0) {
                            // 체크박스 열에서 변경 감지
                            String selectedNames = "";
                            int selectednb = 0;
                            for (int i = 0; i < employeeTable.getRowCount(); i++) {
                                Boolean isChecked = (Boolean) employeeTable.getValueAt(i, checkIndex);
                                if (isChecked == null) continue;
                                if (isChecked) {
                                    selectednb++;
                                    String value = (String) employeeTable.getValueAt(i, 0); // 첫 번째 열의 데이터 가져오기
                                    selectedNames += (" " + value);
                                }
                            }
                            if (selectednb >= 0) {
                                selected_employees.setText("선택한 직원 : " + selectedNames);
                                selected_employee_num.setText("인원 수 : " + Integer.toString(selectednb));
                            }
                        }
                    } catch (java.lang.IllegalArgumentException ell) {

                    }
                }
            }
        });

        JPanel update_delete_panel = new JPanel();
        update_delete_panel.setBounds(0, 351, 860, 115);
        searchScreenPanel.add(update_delete_panel);
        update_delete_panel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        update_delete_panel.setLayout(null);

        JPanel selected_info_panel = new JPanel();
        selected_info_panel.setBounds(6, 6, 394, 103);
        update_delete_panel.add(selected_info_panel);
        selected_info_panel.setLayout(null);

        selected_employees = new JLabel("선택한 직원 : ");
        selected_employees.setBounds(17, 6, 371, 44);
        selected_info_panel.add(selected_employees);
        selected_employees.setFont(new Font("Lucida Grande", Font.PLAIN, 16));

        selected_employee_num = new JLabel("인원 수 : ");
        selected_employee_num.setBounds(18, 53, 173, 19);
        selected_info_panel.add(selected_employee_num);
        selected_employee_num.setFont(new Font("Lucida Grande", Font.PLAIN, 15));

        JPanel update_panel = new JPanel();
        update_panel.setBounds(412, 6, 278, 103);
        update_delete_panel.add(update_panel);
        update_panel.setLayout(null);

        JLabel updateLabel = new JLabel("수정 : ");
        updateLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        updateLabel.setBounds(20, 19, 49, 16);
        update_panel.add(updateLabel);

        JComboBox updatecomboBox = new JComboBox();
        updatecomboBox.setModel(new DefaultComboBoxModel(new String[] {"Name", "Ssn", "Bdate", "Address", "Sex", "Salary", "Supervisor (Ssn)", "Department"}));
        updatecomboBox.setBounds(62, 14, 162, 27);
        update_panel.add(updatecomboBox);

        update_input_text = new JTextField();
        update_input_text.setBounds(20, 48, 244, 34);
        update_panel.add(update_input_text);
        update_input_text.setColumns(10);

        JComboBox update_input_combo = new JComboBox();
        update_input_combo.setBounds(20, 53, 244, 27);
        update_panel.add(update_input_combo);

        JPanel btn_panel = new JPanel();
        btn_panel.setBounds(700, 22, 151, 74);
        update_delete_panel.add(btn_panel);
        update_delete_panel.setVisible(false);
        btn_panel.setLayout(null);

        JButton updateBtn = new JButton("");
        updateBtn.setBounds(6, 5, 61, 63);
        btn_panel.add(updateBtn);
        updateBtn.setSelectedIcon(new ImageIcon("images/delete_hovered.png"));
        updateBtn.setRolloverSelectedIcon(new ImageIcon("images/update_hovered.png"));
        updateBtn.setRolloverIcon(new ImageIcon("images/update_hovered.png"));
        updateBtn.setIcon(new ImageIcon("images/update_idle.png"));
        updateBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == updateBtn) {
                    String ColumnName1 = employeeTable.getColumnName(0);
                    String ColumnName2 = employeeTable.getColumnName(1);
                    if ("Ssn".equals(ColumnName1) || "Ssn".equals(ColumnName2)) {
                        try {
                            int rowCount = employeeTable.getRowCount();
                            int colCount = employeeTable.getColumnCount();
                            int checkboxColumn = colCount - 1;
                            int ColumnName = 0, ColumnSsn = 0, ColumnBdate = 0, ColumnAddress = 0, ColumnSex = 0, ColumnSalary = 0, ColumnDepartment = 0;

                            for (int i = 0; i < colCount; i++) {
                                if ("Name".equals(employeeTable.getColumnName(i))) {
                                    ColumnName = i;
                                } else if ("Ssn".equals(employeeTable.getColumnName(i))) {
                                    ColumnSsn = i;
                                } else if ("Bdate".equals(employeeTable.getColumnName(i))) {
                                    ColumnBdate = i;
                                } else if ("Address".equals(employeeTable.getColumnName(i))) {
                                    ColumnAddress = i;
                                } else if ("Sex".equals(employeeTable.getColumnName(i))) {
                                    ColumnSex = i;
                                } else if ("Salary".equals(employeeTable.getColumnName(i))) {
                                    ColumnSalary = i;
                                } else if ("Dname".equals(employeeTable.getColumnName(i))) {
                                    ColumnDepartment = i;
                                }
                            }

                            // 콤보박스와 업데이트 값 가져오기
                            String selectedUpdateColumn = (String) updatecomboBox.getSelectedItem();
                            String selectedUpdateInput = (String) update_input_combo.getSelectedItem();
                            String updatedValue = update_input_text.getText();

                            if ("Name".equals(selectedUpdateColumn)) {
                                for (int i = 0; i < rowCount; i++) {
                                    if (Boolean.TRUE.equals(employeeTable.getValueAt(i, checkboxColumn))) {
                                        // Ssn 값을 문자열로 가져옴
                                        String ssn = (String) employeeTable.getValueAt(i, ColumnSsn);

                                        // Name을 Fname, Minit, Lname으로 분리
                                        String[] nameParts = updatedValue.split(" ");

                                        if (nameParts.length >= 3) {
                                            String fname = nameParts[0];
                                            String minit = nameParts[1];
                                            String lname = nameParts[2];
                                            employeeTable.setValueAt(updatedValue, i, ColumnName);
                                            // 데이터베이스 업데이트
                                            String updateSQL = "UPDATE EMPLOYEE SET Fname = ?, Minit = ?, Lname = ? WHERE Ssn = ?";
                                            PreparedStatement p = conn.prepareStatement(updateSQL);
                                            p.setString(1, fname);
                                            p.setString(2, minit);
                                            p.setString(3, lname);
                                            p.setString(4, ssn);
                                            p.executeUpdate();
                                            JOptionPane.showMessageDialog(null, "성공적으로 값이 업데이트 되었습니다", "message", JOptionPane.INFORMATION_MESSAGE);

                                            System.out.println("db수정완료");
                                        }
                                        else {
                                            System.out.println("Fname, Minit , Lname 을 모두 입력해주세요.");
                                            JOptionPane.showMessageDialog(null, "Fname, Minit , Lname 을 모두 입력해주세요.", "Unspecified Behavior", JOptionPane.ERROR_MESSAGE);
                                        }
                                    }
                                }
                                searchButton.doClick();
                            } else if ("Ssn".equals(selectedUpdateColumn)) {

                                for (int i = 0; i < rowCount; i++) {
                                    if (Boolean.TRUE.equals(employeeTable.getValueAt(i, checkboxColumn))) {
                                        // Ssn 값을 문자열로 가져옴
                                        String ssn = (String) employeeTable.getValueAt(i, ColumnSsn);


                                        try {
                                            // 데이터베이스 업데이트
                                            String updateSQL = "UPDATE EMPLOYEE SET Ssn = ? WHERE Ssn = ?";
                                            PreparedStatement p = conn.prepareStatement(updateSQL);
                                            p.setString(1, updatedValue);
                                            p.setString(2, ssn);
                                            p.executeUpdate();
                                            JOptionPane.showMessageDialog(null, "성공적으로 값이 업데이트 되었습니다", "message", JOptionPane.INFORMATION_MESSAGE);

                                            System.out.println("db수정완료");
                                            employeeTable.setValueAt(updatedValue, i, ColumnSsn);
                                        }catch (SQLException e1) {
                                            e1.printStackTrace();
                                            String errorMessage = "SQL 오류 : " + e1.getMessage();
                                            JOptionPane.showMessageDialog(null, errorMessage, "SQL Errror", JOptionPane.ERROR_MESSAGE);
                                        }
                                    }

                                }
                                searchButton.doClick();
                            } else if ("Bdate".equals(selectedUpdateColumn)) {
                                for (int i = 0; i < rowCount; i++) {
                                    if (Boolean.TRUE.equals(employeeTable.getValueAt(i, checkboxColumn))) {
                                        // Ssn 값을 문자열로 가져옴
                                        String ssn = (String) employeeTable.getValueAt(i, ColumnSsn);

                                        // Bdate 값을 업데이트
                                        employeeTable.setValueAt(updatedValue, i, ColumnBdate);

                                        // 데이터베이스 업데이트
                                        String updateSQL = "UPDATE EMPLOYEE SET Bdate = ? WHERE Ssn = ?";
                                        PreparedStatement p = conn.prepareStatement(updateSQL);
                                        p.setString(1, updatedValue);
                                        p.setString(2, ssn);
                                        p.executeUpdate();
                                        JOptionPane.showMessageDialog(null, "성공적으로 값이 업데이트 되었습니다", "message", JOptionPane.INFORMATION_MESSAGE);

                                        System.out.println("db수정완료");
                                    }
                                }
                                searchButton.doClick();
                            } else if ("Address".equals(selectedUpdateColumn)) {
                                for (int i = 0; i < rowCount; i++) {
                                    if (Boolean.TRUE.equals(employeeTable.getValueAt(i, checkboxColumn))) {
                                        // Ssn 값을 문자열로 가져옴
                                        String ssn = (String) employeeTable.getValueAt(i, ColumnSsn);

                                        // Address 값을 업데이트
                                        employeeTable.setValueAt(updatedValue, i, ColumnAddress);

                                        // 데이터베이스 업데이트
                                        String updateSQL = "UPDATE EMPLOYEE SET Address = ? WHERE Ssn = ?";
                                        PreparedStatement p = conn.prepareStatement(updateSQL);
                                        p.setString(1, updatedValue);
                                        p.setString(2, ssn);
                                        p.executeUpdate();
                                        JOptionPane.showMessageDialog(null, "성공적으로 값이 업데이트 되었습니다", "message", JOptionPane.INFORMATION_MESSAGE);
                                        System.out.println("db수정완료");
                                    }
                                }
                                searchButton.doClick();
                            } else if ("Sex".equals(selectedUpdateColumn)) {
                                for (int i = 0; i < rowCount; i++) {
                                    if (Boolean.TRUE.equals(employeeTable.getValueAt(i, checkboxColumn))) {
                                        // Ssn 값을 문자열로 가져옴
                                        String ssn = (String) employeeTable.getValueAt(i, ColumnSsn);

                                        String sex;

                                        switch (selectedUpdateInput) {
                                            case "F":
                                                sex = "F";
                                                break;
                                            case "M":
                                                sex = "M";
                                                break;
                                        }

                                        // Sex 값을 업데이트
                                        employeeTable.setValueAt(selectedUpdateInput, i, ColumnSex);

                                        // 데이터베이스 업데이트
                                        String updateSQL = "UPDATE EMPLOYEE SET Sex = ? WHERE Ssn = ?";
                                        PreparedStatement p = conn.prepareStatement(updateSQL);
                                        p.setString(1, selectedUpdateInput);
                                        p.setString(2, ssn);
                                        p.executeUpdate();
                                        JOptionPane.showMessageDialog(null, "성공적으로 값이 업데이트 되었습니다", "message", JOptionPane.INFORMATION_MESSAGE);

                                        System.out.println("db수정완료");
                                    }
                                }
                                searchButton.doClick();
                            } else if ("Salary".equals(selectedUpdateColumn)) {
                                for (int i = 0; i < rowCount; i++) {
                                    if (Boolean.TRUE.equals(employeeTable.getValueAt(i, checkboxColumn))) {
                                        // Ssn 값을 문자열로 가져옴
                                        String ssn = (String) employeeTable.getValueAt(i, ColumnSsn);

                                        // Salary 값을 업데이트
                                        employeeTable.setValueAt(Double.parseDouble(updatedValue), i, ColumnSalary);

                                        // 데이터베이스 업데이트
                                        String updateSQL = "UPDATE EMPLOYEE SET Salary = ? WHERE Ssn = ?";
                                        PreparedStatement p = conn.prepareStatement(updateSQL);
                                        p.setDouble(1, Double.parseDouble(updatedValue));
                                        p.setString(2, ssn);
                                        p.executeUpdate();
                                        JOptionPane.showMessageDialog(null, "성공적으로 값이 업데이트 되었습니다", "message", JOptionPane.INFORMATION_MESSAGE);
                                        System.out.println("db수정완료");
                                    }

                                }
                                searchButton.doClick();
                            } else if ("Supervisor (Ssn)".equals(selectedUpdateColumn)) {
                                for (int i = 0; i < rowCount; i++) {
                                    if (Boolean.TRUE.equals(employeeTable.getValueAt(i, checkboxColumn))) {
                                        // Ssn 값을 문자열로 가져옴
                                        String ssn = (String) employeeTable.getValueAt(i, ColumnSsn);

                                        // 데이터베이스에서 Supervisor의 Ssn을 찾음
                                        String query = "SELECT Ssn FROM EMPLOYEE WHERE Ssn = ?";
                                        PreparedStatement queryStatement = conn.prepareStatement(query);
                                        queryStatement.setString(1, updatedValue);
                                        ResultSet resultSet = queryStatement.executeQuery();

                                        if (resultSet.next()) {
                                            // 데이터베이스 업데이트
                                            String updateSQL = "UPDATE EMPLOYEE SET Super_ssn = ? WHERE Ssn = ?";
                                            PreparedStatement updateStatement = conn.prepareStatement(updateSQL);
                                            updateStatement.setString(1, updatedValue); // 새로운 Supervisor의 Ssn으로 업데이트
                                            updateStatement.setString(2, ssn); // 선택한 직원의 Ssn
                                            updateStatement.executeUpdate();
                                            JOptionPane.showMessageDialog(null, "성공적으로 값이 업데이트 되었습니다", "message", JOptionPane.INFORMATION_MESSAGE);
                                            searchButton.doClick();
                                            System.out.println("db수정완료");
                                        } else {
                                            JOptionPane.showMessageDialog(null, "입력된 Ssn을 가진 직원을 찾을 수 없습니다. 변경할 수 없습니다.", "Unspecified Behavior", JOptionPane.ERROR_MESSAGE);
                                            System.out.println("입력된 Ssn을 가진 직원을 찾을 수 없습니다. 변경할 수 없습니다.");
                                        }
                                    }
                                }
                            } else if ("Department".equals(selectedUpdateColumn)) {
                                for (int i = 0; i < rowCount; i++) {
                                    if (Boolean.TRUE.equals(employeeTable.getValueAt(i, checkboxColumn))) {
                                        // Ssn 값을 문자열로 가져옴
                                        String ssn = (String) employeeTable.getValueAt(i, ColumnSsn);

                                        String dno = null; // 기본값 설정

                                        switch (selectedUpdateInput) {
                                            case "Research":
                                                dno = "5";
                                                break;
                                            case "Administration":
                                                dno = "4";
                                                break;
                                            case "Headquarters":
                                                dno = "1";
                                                break;
                                            default:
                                                System.out.println("유효하지 않은 Dname 값입니다.");
                                                JOptionPane.showMessageDialog(null, "유효하지 않은 Dname 값입니다.", "Unspecified Behavior", JOptionPane.ERROR_MESSAGE);
                                        }

                                        if (dno != null) {
                                            // 데이터베이스 업데이트
                                            String updateSQL = "UPDATE EMPLOYEE SET Dno = ? WHERE Ssn = ?";
                                            PreparedStatement p = conn.prepareStatement(updateSQL);

                                            if (dno != null) {
                                                p.setString(1, dno);
                                            } else {
                                                p.setNull(1, Types.VARCHAR); // null 처리
                                            }

                                            p.setString(2, ssn);
                                            p.executeUpdate();

                                            employeeTable.setValueAt(selectedUpdateInput, i, ColumnDepartment);
                                            JOptionPane.showMessageDialog(null, "성공적으로 값이 업데이트 되었습니다", "message", JOptionPane.INFORMATION_MESSAGE);
                                            //searchButton.doClick();
                                            System.out.println("db수정완료");
                                        }
                                    }
                                }
                                searchButton.doClick();
                            } else {
                                System.out.println("업데이트 컬럼과 값을 선택해 주세요.");
                                JOptionPane.showMessageDialog(null, "업데이트 컬럼과 값을 선택해 주세요.", "Unspecified Behavior", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                            String errorMessage = "SQL Error : 입력값을 다시 확인해주세요.\n" + e1.getMessage();
                            JOptionPane.showMessageDialog(null, errorMessage,"SQL Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }else {
                        JOptionPane.showMessageDialog(null, "Ssn을 선택해야만 Update가 가능합니다.", "Unspecified Behavior", JOptionPane.ERROR_MESSAGE);
                        System.out.println("Ssn을 선택해야만 Update가 가능합니다.");
                    }
                }
            }
        });


        updateBtn.setToolTipText("update");
        updateBtn.setRequestFocusEnabled(false);
        updateBtn.setFocusable(false);
        updateBtn.setFocusTraversalKeysEnabled(false);
        updateBtn.setFocusPainted(false);
        updateBtn.setDefaultCapable(false);
        updateBtn.setContentAreaFilled(false);
        updateBtn.setBorderPainted(false);

        JButton deleteBtn = new JButton("");
        deleteBtn.setBounds(79, 6, 66, 62);
        btn_panel.add(deleteBtn);
        deleteBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Vector<String> delete_ssn = new Vector<String>();

                if (e.getSource() == deleteBtn) {

                    try {
                        String ColumnName1 = employeeTable.getColumnName(0);
                        String ColumnName2 = employeeTable.getColumnName(1);
                        if ("Ssn".equals(ColumnName1) || "Ssn".equals(ColumnName2)) {

                            int rowCount = employeeTable.getRowCount();
                            int colCount = employeeTable.getColumnCount();
                            System.out.println(rowCount);
                            System.out.println(colCount);
                            for (int i = 0; i < rowCount ; i++) {
                                if(Boolean.TRUE.equals(employeeTable.getValueAt(i, colCount-1))) {
                                    if("Ssn".equals(ColumnName2)) {
                                        delete_ssn.add((String) employeeTable.getValueAt(i, 1)); // Ssn 열
                                        System.out.println(employeeTable.getValueAt(i, colCount-1));
                                    }else {
                                        delete_ssn.add((String) employeeTable.getValueAt(i, 0)); // Ssn 열
                                    }
                                }
                            }
                            System.out.println(delete_ssn);
                            for (int i = 0; i< delete_ssn.size(); i++) {
                                for(int k = 0; k< employeeTable.getRowCount(); k ++) {
                                    if(employeeTable.getValueAt(k, colCount-1 )== Boolean.TRUE)
                                        tableModel.removeRow(k);
                                }
                            }

                            // 데이터베이스에서도 삭제
                            for (String ssn : delete_ssn) {
                                String deleteStmt = "DELETE FROM EMPLOYEE WHERE Ssn=?";

                                PreparedStatement p = conn.prepareStatement(deleteStmt);
                                p.clearParameters();
                                p.setString(1, ssn);
                                p.executeUpdate();
                            }
                            searchButton.doClick();
                        }else {
                            System.out.println("삭제가 불가능 합니다. Ssn을 반드시 선택해 주세요");
                            JOptionPane.showMessageDialog(null, "삭제가 불가능 합니다. Ssn을 반드시 선택해 주세요.", "Unspecified Behavior", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                        String errorMessage = "SQL 오류 : 입력값을 다시 확인해주세요.\n" + e1.getMessage();
                        JOptionPane.showMessageDialog(null, errorMessage,"SQL Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
        });
        deleteBtn.setDefaultCapable(false);
        deleteBtn.setFocusable(false);
        deleteBtn.setFocusTraversalKeysEnabled(false);
        deleteBtn.setFocusPainted(false);
        deleteBtn.setContentAreaFilled(false);
        deleteBtn.setBorderPainted(false);
        deleteBtn.setSelectedIcon(new ImageIcon("images/delet_hovered.png"));
        deleteBtn.setRolloverIcon(new ImageIcon("images/delet_hovered.png"));
        deleteBtn.setRequestFocusEnabled(false);
        deleteBtn.setIcon(new ImageIcon("images/delete_idle.png"));
        deleteBtn.setToolTipText("delete");


        updatecomboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
                String selected = updatecomboBox.getSelectedItem().toString();
                if (selected.equals("Department") || selected.equals("Sex")) {
                    update_input_text.setVisible(false);
                    update_input_combo.setVisible(true);
                    if (selected.equals("Sex")) {
                        update_input_combo.setModel(new DefaultComboBoxModel<String>(new String[]{"M", "F"}));
                    }
                    else if (selected.equals("Department")) {
                        String[] departmentOptions = {"Research", "Administration", "Headquarters"};
                        update_input_combo.setModel(new DefaultComboBoxModel<String>(departmentOptions));
                    }
                } else {
                    update_input_text.setVisible(true);
                    update_input_combo.setVisible(false);
                }
            }
        });
        searchScreenPanel.setVisible(false);
        update_delete_panel.setVisible(false);



        JPanel addScreenPanel = new JPanel();
        addScreenPanel.setBounds(100, 120, 860, 348);
        addScreenPanel.setEnabled(false);
        addScreenPanel.setBorder(null);
        addScreenPanel.setVerifyInputWhenFocusTarget(false);
        addScreenPanel.setRequestFocusEnabled(false);
        addScreenPanel.setOpaque(false);
        addScreenPanel.setFocusable(false);
        addScreenPanel.setFocusTraversalKeysEnabled(false);
        addScreenPanel.setDoubleBuffered(false);
        mainFrame.getContentPane().add(addScreenPanel);
        addScreenPanel.setBackground(new Color(55, 52, 114));
        addScreenPanel.setLayout(null);

        JLabel titleLabel = new JLabel("새로운 직원 정보 추가");
        titleLabel.setBounds(25, 17, 260, 45);
        addScreenPanel.add(titleLabel);
        titleLabel.setFont(new Font("Apple SD Gothic Neo", Font.BOLD, 24));

        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setBounds(25, 87, 90, 16);
        addScreenPanel.add(firstNameLabel);

        JLabel midInitLabel = new JLabel("Middle Initial:");
        midInitLabel.setBounds(26, 127, 90, 16);
        addScreenPanel.add(midInitLabel);

        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setBounds(26, 170, 90, 16);
        addScreenPanel.add(lastNameLabel);

        JLabel ssnLabel = new JLabel("Ssn:");
        ssnLabel.setBounds(26, 211, 90, 16);
        addScreenPanel.add(ssnLabel);

        JLabel bdateLabel = new JLabel("Birthdate:");
        bdateLabel.setBounds(26, 257, 90, 16);
        addScreenPanel.add(bdateLabel);

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setBounds(422, 90, 90, 16);
        addScreenPanel.add(addressLabel);

        JLabel sexLabel = new JLabel("Sex:");
        sexLabel.setBounds(422, 127, 90, 16);
        addScreenPanel.add(sexLabel);

        JLabel salaryLabel = new JLabel("Salary:");
        salaryLabel.setBounds(422, 173, 90, 16);
        addScreenPanel.add(salaryLabel);

        JLabel superSsnLabel = new JLabel("Super_ssn:");
        superSsnLabel.setBounds(422, 215, 90, 16);
        addScreenPanel.add(superSsnLabel);

        JLabel dnoLabel = new JLabel("Dno:");
        dnoLabel.setBounds(422, 261, 90, 16);
        addScreenPanel.add(dnoLabel);

        fname_input = new JTextField();
        fname_input.setBounds(133, 80, 251, 26);
        fname_input.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        addScreenPanel.add(fname_input);
        fname_input.setColumns(10);

        minit_input = new JTextField();
        minit_input.setBounds(133, 119, 251, 26);
        minit_input.setColumns(10);
        minit_input.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        addScreenPanel.add(minit_input);

        lname_input = new JTextField();
        lname_input.setBounds(133, 163, 251, 26);
        lname_input.setColumns(10);
        lname_input.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        addScreenPanel.add(lname_input);

        ssn_input = new JTextField();
        ssn_input.setBounds(133, 205, 251, 26);
        ssn_input.setColumns(10);
        ssn_input.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        addScreenPanel.add(ssn_input);

        bdate_input = new JTextField();
        bdate_input.setBounds(133, 251, 251, 26);
        bdate_input.setColumns(10);
        bdate_input.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        addScreenPanel.add(bdate_input);

        add_input = new JTextField();
        add_input.setBounds(529, 80, 251, 26);
        add_input.setColumns(10);
        add_input.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        addScreenPanel.add(add_input);

        salary_input = new JTextField();
        salary_input.setBounds(529, 163, 251, 26);
        salary_input.setColumns(10);
        salary_input.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        addScreenPanel.add(salary_input);

        superSsn_input = new JTextField();
        superSsn_input.setBounds(529, 205, 251, 26);
        superSsn_input.setColumns(10);
        superSsn_input.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        addScreenPanel.add(superSsn_input);

        dno_input = new JTextField();
        dno_input.setBounds(529, 251, 251, 26);
        dno_input.setColumns(10);
        dno_input.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        addScreenPanel.add(dno_input);

        JComboBox sex_input = new JComboBox();
        sex_input.setBounds(525, 120, 266, 29);
        sex_input.setVerifyInputWhenFocusTarget(false);
        sex_input.setModel(new DefaultComboBoxModel(new String[] {"female", "male"}));
        addScreenPanel.add(sex_input);

        JButton confirmBtn = new JButton("");
        confirmBtn.setBounds(659, 298, 120, 37);
        confirmBtn.setRequestFocusEnabled(false);
        confirmBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input_fname = fname_input.getText() , input_minit = minit_input.getText() , input_lname = lname_input.getText() ,
                        input_ssn = ssn_input.getText(), input_bdate = bdate_input.getText(), input_address = add_input.getText() ,
                        input_salary = salary_input.getText(), input_superssn = superSsn_input.getText(), input_dno = dno_input.getText(),
                        input_sex = sex_input.getSelectedItem().toString().equals("female") ? "F" : "M";
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");

                String stmt1 = "Insert into employee values (?,?,?,?,?,?,?,?,?,?,?,?)";
                try {
                    connectDB();
                    PreparedStatement p = conn.prepareStatement(stmt1);
                    p.clearParameters();
                    p.setString(1, input_fname);
                    p.setString(2, input_minit);
                    p.setString(3, input_lname);
                    p.setString(4, input_ssn);
                    p.setString(5, input_bdate);
                    p.setString(6, input_address);
                    p.setString(7, input_sex);
                    p.setString(8, input_salary);
                    p.setString(9, input_superssn);
                    p.setString(10, input_dno);
                    p.setString(11, sdf.format(timestamp));
                    p.setString(12, sdf.format(timestamp));
                    p.executeUpdate();
                    // clear inputs
                    fname_input.setText("");
                    minit_input.setText("");
                    lname_input.setText("");
                    ssn_input.setText("");
                    bdate_input.setText("");
                    add_input.setText("");
                    salary_input.setText("");
                    superSsn_input.setText("");
                    dno_input.setText("");
                    sex_input.setSelectedIndex(-1);
                    p.close();
                    JOptionPane.showMessageDialog(null, "성공적으로 데이터가 추가되었습니다!","message", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    String errorMessage = "SQL 오류 : 입력값을 다시 확인해주세요.\n" + ex.getMessage();
                    JOptionPane.showMessageDialog(null, errorMessage,"SQL Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        confirmBtn.setSelectedIcon(new ImageIcon("images/confirmAdd_hovered.png"));
        confirmBtn.setRolloverSelectedIcon(new ImageIcon("images/confirmAdd_hovered.png"));
        confirmBtn.setRolloverIcon(new ImageIcon("images/confirmAdd_hovered.png"));
        confirmBtn.setBorderPainted(false);
        confirmBtn.setContentAreaFilled(false);
        confirmBtn.setDefaultCapable(false);
        confirmBtn.setFocusable(false);
        confirmBtn.setFocusTraversalKeysEnabled(false);
        confirmBtn.setFocusPainted(false);
        confirmBtn.setIcon(new ImageIcon("images/confirmAdd_idle.png"));
        addScreenPanel.add(confirmBtn);

        JPanel homeScreenPanel = new ImagePanel("images/HomeScreen.png");
        homeScreenPanel.setBackground(new Color(255, 254, 254));
        homeScreenPanel.setBounds(98, 6, 902, 616);
        mainFrame.getContentPane().add(homeScreenPanel);
        homeScreenPanel.setLayout(null);
        homeScreenPanel.setVisible(false);
        searchTextField.setVisible(false);
        searchComboBox.setVisible(false);
        searchItemComboBox.setVisible(true);

        homeRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                homeScreenPanel.setVisible(true);
                searchScreenPanel.setVisible(false);
                addScreenPanel.setVisible(false);
                update_delete_panel.setVisible(false);
                searchConditionsPanel.setVisible(false);
            }
        });
        searchRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                homeScreenPanel.setVisible(false);
                searchScreenPanel.setVisible(true);
                addScreenPanel.setVisible(false);
                update_delete_panel.setVisible(false);
                searchConditionsPanel.setVisible(true);
            }
        });
        addRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                homeScreenPanel.setVisible(false);
                searchScreenPanel.setVisible(false);
                addScreenPanel.setVisible(true);
                update_delete_panel.setVisible(false);
                searchConditionsPanel.setVisible(false);
            }
        });
        deleteRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                homeScreenPanel.setVisible(false);
                searchScreenPanel.setVisible(true);
                addScreenPanel.setVisible(false);
                update_delete_panel.setVisible(true);
                searchConditionsPanel.setVisible(true);
            }
        });

        searchItemComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
                String selected = searchItemComboBox.getSelectedItem().toString();
                if (selected.equals("All")) {
                    searchTextField.setVisible(false);
                    searchComboBox.setVisible(false);
                }  else if (selected.equals("Department") || selected.equals("Sex")) {
                    searchTextField.setVisible(false);
                    searchComboBox.setVisible(true);
                    if (selected.equals("Sex")) {
                        searchComboBox.setModel(new DefaultComboBoxModel<String>(new String[]{"M", "F"}));
                    }
                    else if (selected.equals("Department")) {
                        String[] departmentOptions = {"Research", "Administration", "Headquarters"};
                        searchComboBox.setModel(new DefaultComboBoxModel<String>(departmentOptions));
                    }
                } else {
                    searchTextField.setVisible(true);
                    searchComboBox.setVisible(false);
                }
            }
        });
        homeScreenPanel.setVisible(true);
        addScreenPanel.setVisible(false);
        searchConditionsPanel.setVisible(false);
        noresults.setVisible(false);
    }
}