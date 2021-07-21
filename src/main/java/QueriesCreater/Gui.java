package QueriesCreater;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

public class Gui extends JFrame {
    static JTable columnsTable;
    static DefaultTableModel model;
    static JTable objectTable;
    ImageIcon logo = new ImageIcon(Toolkit.getDefaultToolkit().createImage(Gui.class.getResource("/logo.png")));
    DefaultTableModel objectModel;
    JComboBox<String> typesCombobox = new JComboBox<>();
    final String [] plsqlTypes = {"NUMBER","VARCHAR2","DATE","CHAR","NCHAR","NVARCHAR2","LONG","RAW","LONG_RAW",
                                  "NUMERIC","DEC","DECIMAL","PLS_INTEGER","BFILE","BLOB","CLOB","NCLOB",
                                  "BOOLEAN","ROWID"};

    public Gui() {
        this.setResizable(false);
        setIconImage(logo.getImage());
        this.setTitle("PL/SQL: excel export");
        this.setFont(new Font("Tahoma", Font.PLAIN, 14));
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setBounds(650, 190, 370, 620);
        this.getContentPane().setBackground(new Color(0xFFF9A1));
        this.getContentPane().setLayout(null);

        // заполняем комбобокс типами данных PL/SQL
        for (String type : plsqlTypes) {
            typesCombobox.addItem(type);
        }

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
        columnsTable = new JTable(model);

        // cell border color
        columnsTable.setGridColor(new Color(58, 79, 79));
        columnsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // table background color
        columnsTable.setFillsViewportHeight(true);
        columnsTable.setBackground(new Color(250, 252, 255));
        // headers settings
        JTableHeader header = columnsTable.getTableHeader();
        header.setFont(new Font("Tahoma", Font.BOLD, 13));
        //cell alignment
        DefaultTableCellRenderer Renderer = new DefaultTableCellRenderer();
        Renderer.setHorizontalAlignment(JLabel.CENTER);
        columnsTable.getColumnModel().getColumn(0).setCellRenderer(Renderer);
        columnsTable.getColumnModel().getColumn(1).setCellRenderer(Renderer);
        columnsTable.getColumnModel().getColumn(2).setCellRenderer(Renderer);
        columnsTable.getColumnModel().getColumn(3).setCellRenderer(Renderer);
        // Типы данных - комбобокс
        TableColumn testColumn = columnsTable.getColumnModel().getColumn(3);
        testColumn.setCellEditor(new DefaultCellEditor(typesCombobox));
        columnsTable.setRowHeight(20);
        columnsTable.setColumnSelectionAllowed(true);
        columnsTable.setCellSelectionEnabled(true);
        columnsTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        columnsTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        columnsTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        columnsTable.getColumnModel().getColumn(1).setPreferredWidth(48);
        columnsTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        columnsTable.getColumnModel().getColumn(3).setPreferredWidth(110);
        //colors
        columnsTable.setSelectionBackground(new Color(254, 204, 204));
        scrollPane.setViewportView(columnsTable);

        // удаление содержимого ячейки кнопкой Delete
        columnsTable.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==127){
                    columnsTable.setValueAt("", columnsTable.getSelectedRow(), columnsTable.getSelectedColumn());
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
            for (int i = 0; i < columnsTable.getRowCount(); i++)
                for(int j = 0; j < columnsTable.getColumnCount(); j++) {
                    columnsTable.setValueAt("", i, j);
                }
        });

        this.setVisible(true);
    }


    public static void getValues() {
        // определяем количество строк для создания массива (количество столбцов статическое - 4)
        int rowCount = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            if (columnsTable.getValueAt(i, 0) != null) {
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
                if (columnsTable.getValueAt(i, j) != null) {
                    columns [i][j] = columnsTable.getValueAt(i, j);
                }
            }
        }

        // выгрузка запросов в текстовый файл
        if (columns.length > 0) {
            write(new GetSqlQuery().getQueries(objectParameters, columns));
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

    static void write(String pSql) {
        //Save file to
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.txt", "*.txt", "*.TXT", "*.*");
        JFileChooser save_to = new JFileChooser();
        save_to.setFileFilter(filter);
        save_to.setCurrentDirectory(new File
                (System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop"));
        int ret = save_to.showDialog(null, "Save");
        if (ret == JFileChooser.APPROVE_OPTION) {
            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(save_to.getSelectedFile() + ".txt"), StandardCharsets.UTF_8)) {
                writer.write(pSql);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
