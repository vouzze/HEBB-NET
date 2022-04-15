package com.lpnu;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Objects;

public class NewJFrame extends javax.swing.JFrame {
    public static void main(String[] args) {
        new NewJFrame();
    }

    private static final int row = 5;
    private static final int column = 5;
    private static final int rowHeight = 20;
    private static final int columnWidth = 30;
    private static final int tableCount = 4;


    int[] y = new int[tableCount];
    int[][] x = new int[tableCount][row * column + 1];
    int[] xo = new int[row * column + 1];
    double[] weight = new double[row * column + 1];
    double[] s = new double[tableCount];
    JTable[] tables;
    JTextField[] fields;
    String[] letters = new String[]{"I", "T", "O", "P"};
    boolean emptyTable;


    private JTable jTable1;
    private JTable jTable2;
    private JTable jTable3;
    private JTable jTable4;
    private JTable jTable5;
    private JTextArea jTextArea1;
    private JButton jButton1;
    private JButton jButton2;
    private JPanel show;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JButton jButton4;
    private JButton jButton3;

    public NewJFrame() {

        jButton1.addActionListener(e -> {
            tables = new JTable[]{jTable1, jTable2, jTable3, jTable4};
            fields = new JTextField[]{textField1, textField2, textField3, textField4};
            if (!jButton2.isEnabled()) {
                for (int i = 0; i < row * column + 1; i++) {
                    weight[i] = 0;
                }
            }
            for (int i = 0; i < tableCount; i++) {
                s[i] = 0;
            }

            for (int i = 0; i < tableCount; i++) {
                teachLet(tables[i], i);
                if (emptyTable) {
                    jTextArea1.setText("Table(s) is(are) empty!");
                    break;
                }
            }

            if (!emptyTable) {
                for (int i = 0; i < tableCount; i++) {
                    for (int j = 0; j < row * column + 1; j++) {
                        s[i] += weight[j] * x[i][j];
                    }
                    System.out.println("\ns of " + i + " is " + s[i]);
                }

                letters = initLetters(fields);

                if (!jButton2.isEnabled()) {
                    jButton2.setEnabled(true);
                }

                jTextArea1.setText("Try to type a new letter and recognize it!");
            }
        });


        jButton2.addActionListener(e -> {
            jTextArea1.setText("");
            xo = xate(jTable5);
            double su = 0;
            for (int i = 0; i < row * column + 1; i++) {
                su += weight[i] * xo[i];
            }
            jTextArea1.append("sum = " + su);
            for (int i = 0; i < tableCount; i++) {
                if (Math.abs(s[i] - su) == 0) {
                    jTextArea1.append("\nYour letter is " + letters[i]);
                    break;
                }
                if (i == tableCount - 1) {
                    jTextArea1.append("\nYour letter is not found");
                }
            }
        });


        jButton3.addActionListener(e -> {
            if (jTable5.isEditing()) {
                jTable5.getCellEditor().cancelCellEditing();
            }
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    jTable5.setValueAt(false, i, j);
                }
            }
            jTextArea1.setText("Table is wiped!");
        });

        jButton4.addActionListener(e -> {
            if (jTable5.isEditing()) {
                jTable5.getCellEditor().cancelCellEditing();
            }
        });


        setTitle("HebbieLite");
        getContentPane().add(show, BorderLayout.CENTER);
        getContentPane().setForeground(Color.YELLOW);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public String[] initLetters(JTextField[] textFields) {
        String[] let = new String[textFields.length];
        for (int i = 0; i < textFields.length; i++) {
            let[i] = textFields[i].getText();
        }
        return let;
    }


    public int[] xate(JTable tbl) {
        emptyTable = false;
        int[] xj = new int[row * column + 1];
        int empty = 0;
        int m = 1;
        xj[0] = 1;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (tbl.getValueAt(i, j) != null) {
                    if (tbl.getValueAt(i, j).equals(true)) {
                        xj[m] = 1;
                    } else {
                        empty++;
                        xj[m] = 0;
                    }
                } else {
                    empty++;
                    xj[m] = 0;
                }
                m++;
            }
        }
        if (empty == row*column) {
            emptyTable = true;
        }
        return xj;
    }

    public int deltaW(int x, int y) {
        int d = -1;
        if (x * y == 1) {
            d = 1;
        } else if (x == 0) {
            d = 0;
        }
        return d;
    }

    public int sumY(double s) {
        if (s >= 0) {
            return 1;
        } else {
            return 0;
        }
    }


    public void teachLet(JTable table, int index) {
        x[index] = xate(table);
        int stop = 0;
        if (index % 2 == 1) {
            while (stop == 0) {
                y[index - 1] = 1;
                y[index] = 0;
                int y1;
                int y2;
                double s1 = 0;
                double s2 = 0;
                for (int i = 0; i < row * column + 1; i++) {
                    weight[i] += deltaW(x[index - 1][i], y[index - 1]);
                    weight[i] += deltaW(x[index][i], y[index]);

                    s1 += weight[i] * x[index - 1][i];
                    s2 += weight[i] * x[index][i];
                }

                y1 = sumY(s1);
                y2 = sumY(s2);

                if ((y[index - 1] == y1) && (y[index] == y2)) {
                    stop = 1;
                }
            }
        }
    }


    public void setIcons(JTable table, ImageIcon icon, ImageIcon selectedIcon) {
        for (int i = 0; i < column; i++) {
            JCheckBox cellRenderer = (JCheckBox) table.getCellRenderer(0, i);
            cellRenderer.setSelectedIcon(selectedIcon);
            cellRenderer.setIcon(icon);

            DefaultCellEditor cellEditor = (DefaultCellEditor) table.getCellEditor(0, i);
            JCheckBox editorComponent = (JCheckBox) cellEditor.getComponent();
            editorComponent.setSelectedIcon(selectedIcon);
            editorComponent.setIcon(icon);
        }
    }


    private void createUIComponents() {
        ImageIcon imi = new ImageIcon(Objects.requireNonNull(getClass().getResource("iconI.png")));
        ImageIcon ims = new ImageIcon(Objects.requireNonNull(getClass().getResource("iconS.png")));

        Object[][] tb1 = {{false, false, true, false, false},
                {false, false, true, false, false},
                {false, false, true, false, false},
                {false, false, true, false, false},
                {false, false, true, false, false}
        };
        DefaultTableModel model = new DefaultTableModel(tb1, new String[]{"t", "b", "3", "", ""}) {
            @Override
            public Class getColumnClass(int columnIndex) {
                return Boolean.class;
            }
        };
        jTable1 = new JTable(model);
        jTable1.setTableHeader(null);
        jTable1.setRowHeight(rowHeight);
        for (int i = 0; i < column; i++) {
            jTable1.getColumnModel().getColumn(i).setMaxWidth(columnWidth);
        }

        Color peachy = new Color(0x413438);
        Border border = new LineBorder(peachy, 2, true);
        jTable1.setBorder(border);
        setIcons(jTable1, imi, ims);

        textField1 = new JTextField("I");
        textField1.setBorder(border);

        Object[][] tb2 = {{true, true, true, true, true},
                {false, false, true, false, false},
                {false, false, true, false, false},
                {false, false, true, false, false},
                {false, false, true, false, false}
        };
        model = new DefaultTableModel(tb2, new String[]{"t", "b", "3", "", ""}) {
            @Override
            public Class getColumnClass(int columnIndex) {
                return Boolean.class;
            }
        };
        jTable2 = new JTable(model);
        jTable2.setTableHeader(null);
        jTable2.setRowHeight(rowHeight);
        for (int i = 0; i < column; i++) {
            jTable2.getColumnModel().getColumn(i).setMaxWidth(columnWidth);
        }

        jTable2.setBorder(border);
        setIcons(jTable2, imi, ims);

        textField2 = new JTextField("T");
        textField2.setBorder(border);


        Object[][] tb3 = {{true, true, true, true, true},
                {true, false, false, false, true},
                {true, false, false, false, true},
                {true, false, false, false, true},
                {true, true, true, true, true}
        };
        model = new DefaultTableModel(tb3, new String[]{"t", "b", "3", "", ""}) {
            @Override
            public Class getColumnClass(int columnIndex) {
                return Boolean.class;
            }
        };
        jTable3 = new JTable(model);
        jTable3.setTableHeader(null);
        jTable3.setRowHeight(rowHeight);
        for (int i = 0; i < column; i++) {
            jTable3.getColumnModel().getColumn(i).setMaxWidth(columnWidth);
        }

        jTable3.setBorder(border);
        setIcons(jTable3, imi, ims);

        textField3 = new JTextField("O");
        textField3.setBorder(border);

        Object[][] tb4 = {{true, false, false, false, true},
                {true, false, false, false, true},
                {true, false, false, false, true},
                {false, true, false, true, false},
                {false, false, true, false, false}
        };
        model = new DefaultTableModel(tb4, new String[]{"t", "b", "4", "", ""}) {
            @Override
            public Class getColumnClass(int columnIndex) {
                return Boolean.class;
            }
        };
        jTable4 = new JTable(model);
        jTable4.setTableHeader(null);
        jTable4.setRowHeight(rowHeight);
        for (int i = 0; i < column; i++) {
            jTable4.getColumnModel().getColumn(i).setMaxWidth(columnWidth);
        }

        jTable4.setBorder(border);
        setIcons(jTable4, imi, ims);

        textField4 = new JTextField("V");
        textField4.setBorder(border);

        Object[][] tb5 = {{true, true, true, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false}
        };
        model = new DefaultTableModel(tb5, new String[]{"t", "b", "5", "", ""}) {
            @Override
            public Class getColumnClass(int columnIndex) {
                return Boolean.class;
            }
        };
        jTable5 = new JTable(model);
        jTable5.setTableHeader(null);
        jTable5.setRowHeight(rowHeight);
        for (int i = 0; i < column; i++) {
            jTable5.getColumnModel().getColumn(i).setMaxWidth(columnWidth);
        }

        jTable5.setBorder(border);
        setIcons(jTable5, imi, ims);

        jTextArea1 = new JTextArea("Teach Hebbian Neural Network new letters!");
        jTextArea1.setBorder(border);

        jButton1 = new JButton();
        jButton2 = new JButton();
        jButton3 = new JButton();
        jButton4 = new JButton();

    }
}


