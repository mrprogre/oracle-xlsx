package QueriesCreater;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class Gui extends JFrame {
    static ImageIcon logo = new ImageIcon(Toolkit.getDefaultToolkit().createImage(Gui.class.getResource("/logo.png")));
    static JTable table;
    static DefaultTableModel model;
    static JTable objectTable;
    static DefaultTableModel objectModel;
    static int guiWindowHeight = 620;
    static int guiWindowWidth = 370;
    static int guiWindowX = 650;
    static int guiWindowY = 190;

    public Gui() {
        this.setResizable(false);
        setIconImage(logo.getImage());
        this.setTitle("PL/SQL: excel export");
        this.setFont(new Font("Tahoma", Font.PLAIN, 14));
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setBounds(guiWindowX, guiWindowY, guiWindowWidth, guiWindowHeight);
        this.getContentPane().setBackground(new Color(0xFFF9A1));
        this.getContentPane().setLayout(null);

        // Object name table
        final JScrollPane objectNames = new JScrollPane();
        objectNames.setBounds(10, 25, 334, 201);
        getContentPane().add(objectNames);

        String[] objectColumns = new String[]{"Object", "Value"};
        objectModel = new DefaultTableModel(new Object[][] {
                {"Function name", "get_xls_function"},
                {"Procedure name", "get_xls_from_table"},
                {"Record type", "t_type_of_record"},
                {"Table as record", "t_type_of_record_tbl"},
                {"Table or view", "table_or_view"},
                {"Headers background", "FFCC66"},
                {"Headers font size", 13},
                {"Rows font size", 12},
                {"Rows height", 25},
                {"Rows bold", false},
                {"Rows italic", false},
                {"Headers font", "Times New Roman"},
                {"Rows font", "Times New Roman"},
                {"Horizontal alignment", "center"},
                {"Vertical alignment", "center"},
                {"Wrap text", true},
        },objectColumns) {
            final boolean[] columnEditables = new boolean[]{
                    false, true
            };
            public boolean isCellEditable(int row, int column) {
                return this.columnEditables[column];
            }
        };
        objectTable = new JTable(objectModel);
        objectTable.setDefaultRenderer(Object.class, new TableInfoRenderer());
        // cell border color
        objectTable.setGridColor(new Color(58, 79, 79));
        objectTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // table background color
        objectTable.setFillsViewportHeight(true);
        objectTable.setBackground(new Color(250, 252, 255));
        // headers settings
        JTableHeader objectHeader = objectTable.getTableHeader();
        objectHeader.setFont(new Font("Tahoma", Font.BOLD, 13));
        //cell alignment
        TableInfoRenderer objectRenderer = new TableInfoRenderer();
        objectRenderer.setHorizontalAlignment(JLabel.LEADING);
        objectTable.getColumnModel().getColumn(0).setCellRenderer(objectRenderer);
        objectTable.getColumnModel().getColumn(1).setCellRenderer(objectRenderer);
        objectTable.setRowHeight(20);
        objectTable.setColumnSelectionAllowed(true);
        objectTable.setCellSelectionEnabled(true);
        objectTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        objectTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        objectTable.getColumnModel().getColumn(0).setPreferredWidth(158);
        objectTable.getColumnModel().getColumn(1).setPreferredWidth(158);
        //colors
        objectTable.setSelectionBackground(new Color(254, 204, 204));
        objectNames.setViewportView(objectTable);

        // удаление содержимого ячейки кнопкой Delete
        objectTable.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==127){
                    objectTable.setValueAt("", objectTable.getSelectedRow(), objectTable.getSelectedColumn());
                }
            }
        });

        // Columns table
        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 255, 336, 283);
        this.getContentPane().add(scrollPane);
        String[] columns = new String[]{"Header", "Width", "Column", "Type"};
        model = new DefaultTableModel(new Object[69][], columns) {
            final boolean[] columnEditables = new boolean[]{
                    true, true, true, true
            };
            public boolean isCellEditable(int row, int column) {
                return this.columnEditables[column];
            }
        };
        table = new JTable(model);

        // cell border color
        table.setGridColor(new Color(58, 79, 79));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // table background color
        table.setFillsViewportHeight(true);
        table.setBackground(new Color(250, 252, 255));
        // headers settings
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Tahoma", Font.BOLD, 13));
        //cell alignment
        DefaultTableCellRenderer Renderer = new DefaultTableCellRenderer();
        Renderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(Renderer);
        table.getColumnModel().getColumn(1).setCellRenderer(Renderer);
        table.getColumnModel().getColumn(2).setCellRenderer(Renderer);
        table.getColumnModel().getColumn(3).setCellRenderer(Renderer);
        table.setRowHeight(20);
        table.setColumnSelectionAllowed(true);
        table.setCellSelectionEnabled(true);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.getColumnModel().getColumn(0).setPreferredWidth(90);
        table.getColumnModel().getColumn(1).setPreferredWidth(48);
        table.getColumnModel().getColumn(2).setPreferredWidth(90);
        table.getColumnModel().getColumn(3).setPreferredWidth(90);
        //colors
        table.setSelectionBackground(new Color(254, 204, 204));
        scrollPane.setViewportView(table);

        // удаление содержимого ячейки кнопкой Delete
        table.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==127){
                    table.setValueAt("", table.getSelectedRow(), table.getSelectedColumn());
                }
            }
        });

        // Create objects button
        JButton setColumnsBtn = new JButton("Create objects");
        setColumnsBtn.setBounds(224, 547, 120, 22);
        setColumnsBtn.setBackground(new Color(192, 225, 255));
        setColumnsBtn.setFont(new Font("Tahoma", Font.BOLD, 11));
        setColumnsBtn.setContentAreaFilled(true);
        setColumnsBtn.setBorderPainted(true);
        setColumnsBtn.setFocusable(false);
        getContentPane().add(setColumnsBtn);
        setColumnsBtn.addActionListener((e) -> getValues());

        // Clear table
        JButton clearTableBtn = new JButton("Clear");
        clearTableBtn.setBounds(10, 547, 120, 22);
        clearTableBtn.setBackground(new Color(251, 203, 203));
        clearTableBtn.setFont(new Font("Tahoma", Font.BOLD, 11));
        clearTableBtn.setContentAreaFilled(true);
        clearTableBtn.setBorderPainted(true);
        clearTableBtn.setFocusable(false);
        getContentPane().add(clearTableBtn);

        JLabel objectNameLbl = new JLabel("Parameters");
        objectNameLbl.setFont(new Font("Tahoma", Font.BOLD, 13));
        objectNameLbl.setBounds(10, 5, 334, 18);
        getContentPane().add(objectNameLbl);

        JLabel columnLbl = new JLabel("Columns");
        columnLbl.setFont(new Font("Tahoma", Font.BOLD, 13));
        columnLbl.setBounds(10, 235, 334, 18);
        getContentPane().add(columnLbl);

        clearTableBtn.addActionListener((e) -> {
            for (int i = 0; i < table.getRowCount(); i++)
                for(int j = 0; j < table.getColumnCount(); j++) {
                    table.setValueAt("", i, j);
                }
        });

        this.setVisible(true);
    }


    public static void getValues() {
        // определяем количество строк для создания массива (количество столбцов статическое - 4)
        int rowCount = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            if (table.getValueAt(i, 0) != null) {
                rowCount++;
            }
        }

        // параметры
        Object[][] objectParameters = new Object[16][2];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 2; j++) {
                if (objectTable.getValueAt(i, j) != null) {
                    objectParameters [i][j] = objectTable.getValueAt(i, j);
                }
            }
        }

        // столбцы
        Object[][] columns = new Object[rowCount][4];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < 4; j++) {
                if (table.getValueAt(i, j) != null) {
                    columns [i][j] = table.getValueAt(i, j);
                }
            }
        }

        // выгрузка запросов в текстовый файл
        if (columns.length > 0) {
            WriteToFile wr = new WriteToFile();
            wr.write(new GetSqlQuery().getQueries(objectParameters, columns));
            //System.out.println(sql.getQueries(columns));
        }
    }

    public static class TableInfoRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, false, row, column);

            if(column == 0) {
                if (isSelected) {
                    super.setForeground(new Color(99, 9, 9));
                } else {
                    super.setForeground(Color.BLACK);
                }
                c.setBackground(new Color(219, 234, 201));
                c.setHorizontalAlignment(LEFT);
                c.setFont(new Font("Tahoma", Font.BOLD,13));
            }
            else {
                c.setBackground(Color.WHITE);
                c.setHorizontalAlignment(LEFT);
                if (isSelected) {
                    super.setBackground(new Color(254, 204, 204));
                }
            }
            return c;
        }
    }
}
