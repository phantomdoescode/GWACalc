import java.awt.*;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class GWACalc extends JFrame {
    private final JTextField subjectField = new JTextField();
    private final JSpinner unitsSpinner = new JSpinner(new SpinnerNumberModel(3.0, 0.0, 100.0, 0.5));
    private final JSpinner gradeSpinner = new JSpinner(new SpinnerNumberModel(1.75, 0.0, 5.0, 0.25));
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JLabel resultValue = new JLabel("0.0000");
    private final JLabel statusLabel = new JLabel("Add your subjects, units, and grades, then press Calculate GWA.");
    private final DecimalFormat fourDp = new DecimalFormat("0.0000");

    private boolean darkMode = false;

    private JPanel rootPanel;
    private JPanel headerPanel;
    private JPanel resultCard;
    private JPanel centerWrapper;
    private JPanel centerCard;
    private JPanel inputCard;
    private JPanel footerPanel;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JLabel computedLabel;
    private JLabel tableTitleLabel;
    private JLabel formTitleLabel;
    private JScrollPane scrollPane;

    private JCheckBoxMenuItem darkModeItem;

    public GWACalc() {
        super("GWACalc");
        configureNativeFeel();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(860, 560));
        setSize(960, 640);
        setLocationRelativeTo(null);

        String[] columns = {"Subject", "Units", "Grade", "Weighted Points"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        configureTable();

        setContentPane(createMainPanel());
        setJMenuBar(createMenuBar());
        applyTheme();
    }

    private void configureNativeFeel() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                System.setProperty("apple.laf.useScreenMenuBar", "true");
                System.setProperty("apple.awt.application.name", "GWACalc");
            }
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
    }

    private Container createMainPanel() {
        rootPanel = new JPanel(new BorderLayout(16, 16));
        rootPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        headerPanel = createHeader();
        centerWrapper = createCenterSection();
        footerPanel = createFooter();

        rootPanel.add(headerPanel, BorderLayout.NORTH);
        rootPanel.add(centerWrapper, BorderLayout.CENTER);
        rootPanel.add(footerPanel, BorderLayout.SOUTH);

        return rootPanel;
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout(12, 8));
        header.setOpaque(true);

        titleLabel = new JLabel("GWACalc");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));

        subtitleLabel = new JLabel("Cross-platform GWA calculator for Windows and macOS");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JPanel textWrap = new JPanel();
        textWrap.setLayout(new BoxLayout(textWrap, BoxLayout.Y_AXIS));
        textWrap.setOpaque(false);
        textWrap.add(titleLabel);
        textWrap.add(Box.createVerticalStrut(4));
        textWrap.add(subtitleLabel);

        resultCard = createResultCard();

        header.add(textWrap, BorderLayout.WEST);
        header.add(resultCard, BorderLayout.EAST);
        return header;
    }

    private JPanel createResultCard() {
        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(215, 218, 224)),
                new EmptyBorder(12, 18, 12, 18)
        ));
        card.setPreferredSize(new Dimension(220, 84));

        computedLabel = new JLabel("Computed GWA", SwingConstants.CENTER);
        computedLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        resultValue.setHorizontalAlignment(SwingConstants.CENTER);
        resultValue.setFont(new Font("SansSerif", Font.BOLD, 24));

        card.add(computedLabel);
        card.add(resultValue);
        return card;
    }

    private JPanel createCenterSection() {
        JPanel wrapper = new JPanel(new BorderLayout(16, 16));
        wrapper.setOpaque(false);

        inputCard = createInputCard();
        wrapper.add(inputCard, BorderLayout.WEST);

        centerCard = new JPanel(new BorderLayout(10, 10));
        centerCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 223, 230)),
                new EmptyBorder(14, 14, 14, 14)
        ));

        tableTitleLabel = new JLabel("Subjects Overview");
        tableTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        centerCard.add(tableTitleLabel, BorderLayout.NORTH);

        scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 223, 230)));
        centerCard.add(scrollPane, BorderLayout.CENTER);

        wrapper.add(centerCard, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createInputCard() {
        JPanel card = new JPanel(new BorderLayout(12, 12));
        card.setPreferredSize(new Dimension(280, 100));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 223, 230)),
                new EmptyBorder(16, 16, 16, 16)
        ));

        formTitleLabel = new JLabel("Add Subject Entry");
        formTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        card.add(formTitleLabel, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        addField(form, gbc, 0, "Subject Name", subjectField);
        addField(form, gbc, 1, "Units", unitsSpinner);
        addField(form, gbc, 2, "Grade", gradeSpinner);

        card.add(form, BorderLayout.CENTER);
        card.add(createButtons(), BorderLayout.SOUTH);
        return card;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row * 2;
        gbc.weightx = 1;
        JLabel fieldLabel = new JLabel(label);
        panel.add(fieldLabel, gbc);

        gbc.gridy = row * 2 + 1;
        panel.add(field, gbc);
    }

    private JPanel createButtons() {
        JPanel buttons = new JPanel(new GridLayout(2, 2, 8, 8));
        buttons.setOpaque(false);

        JButton addButton = new JButton("Add Entry");
        JButton removeButton = new JButton("Remove Selected");
        JButton calculateButton = new JButton("Calculate GWA");
        JButton clearButton = new JButton("Clear All");

        addButton.addActionListener(e -> addEntry());
        removeButton.addActionListener(e -> removeSelectedEntry());
        calculateButton.addActionListener(e -> calculateGwa());
        clearButton.addActionListener(e -> clearAll());

        buttons.add(addButton);
        buttons.add(removeButton);
        buttons.add(calculateButton);
        buttons.add(clearButton);
        return buttons;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.add(statusLabel, BorderLayout.WEST);
        return footer;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem calculateItem = new JMenuItem("Calculate GWA");
        JMenuItem clearItem = new JMenuItem("Clear All");
        JMenuItem exitItem = new JMenuItem("Exit");

        calculateItem.addActionListener(e -> calculateGwa());
        clearItem.addActionListener(e -> clearAll());
        exitItem.addActionListener(e -> dispose());

        fileMenu.add(calculateItem);
        fileMenu.add(clearItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu viewMenu = new JMenu("View");
        darkModeItem = new JCheckBoxMenuItem("Dark Mode");
        darkModeItem.addActionListener(e -> {
            darkMode = darkModeItem.isSelected();
            applyTheme();
        });
        viewMenu.add(darkModeItem);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About GWACalc");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(
                this,
                "GWACalc\nA cross-platform Java UI for computing General Weighted Average.\nBuilt to work smoothly on Windows and macOS.",
                "About GWACalc",
                JOptionPane.INFORMATION_MESSAGE
        ));
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);
        return menuBar;
    }

    private void configureTable() {
        table.setRowHeight(28);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void applyTheme() {
        Color bg;
        Color panelBg;
        Color cardBg;
        Color fg;
        Color subFg;
        Color fieldBg;
        Color fieldFg;
        Color border;
        Color buttonBg;
        Color buttonFg;
        Color buttonBorder;
        Color tableHeaderBg;
        Color tableHeaderFg;
        Color selectionBg;
        Color selectionFg;
        Color menuBg;

        if (darkMode) {
            bg = new Color(24, 26, 31);
            panelBg = new Color(24, 26, 31);
            cardBg = new Color(38, 41, 48);
            fg = new Color(240, 240, 240);
            subFg = new Color(180, 185, 195);
            fieldBg = new Color(50, 54, 63);
            fieldFg = new Color(245, 245, 245);
            border = new Color(72, 78, 90);

            buttonBg = new Color(58, 63, 74);
            buttonFg = new Color(245, 245, 245);
            buttonBorder = new Color(95, 102, 118);

            tableHeaderBg = new Color(55, 60, 70);
            tableHeaderFg = new Color(245, 245, 245);
            selectionBg = new Color(90, 120, 180);
            selectionFg = Color.WHITE;
            menuBg = new Color(38, 41, 48);

            applyDarkUIDefaults(buttonBg, buttonFg, buttonBorder, fieldBg, fieldFg, menuBg, fg);
        } else {
            bg = new Color(245, 247, 250);
            panelBg = new Color(245, 247, 250);
            cardBg = Color.WHITE;
            fg = new Color(20, 20, 20);
            subFg = new Color(90, 98, 108);
            fieldBg = Color.WHITE;
            fieldFg = new Color(20, 20, 20);
            border = new Color(220, 223, 230);

            buttonBg = new Color(245, 245, 245);
            buttonFg = new Color(20, 20, 20);
            buttonBorder = new Color(200, 205, 214);

            tableHeaderBg = new Color(235, 238, 243);
            tableHeaderFg = new Color(20, 20, 20);
            selectionBg = new Color(184, 207, 229);
            selectionFg = Color.BLACK;
            menuBg = new Color(245, 245, 245);

            restoreSystemUIDefaults();
        }

        SwingUtilities.updateComponentTreeUI(this);

        rootPanel.setBackground(bg);
        headerPanel.setBackground(panelBg);
        footerPanel.setBackground(panelBg);
        centerWrapper.setBackground(panelBg);

        resultCard.setBackground(cardBg);
        inputCard.setBackground(cardBg);
        centerCard.setBackground(cardBg);

        titleLabel.setForeground(fg);
        subtitleLabel.setForeground(subFg);
        computedLabel.setForeground(subFg);
        resultValue.setForeground(fg);
        tableTitleLabel.setForeground(fg);
        formTitleLabel.setForeground(fg);
        statusLabel.setForeground(subFg);

        resultCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(border, 1),
                new EmptyBorder(12, 18, 12, 18)
        ));
        inputCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(border, 1),
                new EmptyBorder(16, 16, 16, 16)
        ));
        centerCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(border, 1),
                new EmptyBorder(14, 14, 14, 14)
        ));
        scrollPane.setBorder(BorderFactory.createLineBorder(border, 1));

        subjectField.setBackground(fieldBg);
        subjectField.setForeground(fieldFg);
        subjectField.setCaretColor(fieldFg);
        subjectField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(border, 1),
                new EmptyBorder(6, 8, 6, 8)
        ));

        styleSpinner(unitsSpinner, fieldBg, fieldFg, border);
        styleSpinner(gradeSpinner, fieldBg, fieldFg, border);

        table.setBackground(fieldBg);
        table.setForeground(fieldFg);
        table.setGridColor(border);
        table.setSelectionBackground(selectionBg);
        table.setSelectionForeground(selectionFg);

        JTableHeader header = table.getTableHeader();
        header.setBackground(tableHeaderBg);
        header.setForeground(tableHeaderFg);
        header.setBorder(BorderFactory.createLineBorder(border, 1));

        updateComponentTreeColors(rootPanel, fg, cardBg, fieldBg, fieldFg, buttonBg, buttonFg, buttonBorder);

        JMenuBar menuBar = getJMenuBar();
        if (menuBar != null) {
            menuBar.setBackground(menuBg);
            menuBar.setForeground(fg);
            styleMenuElement(menuBar, menuBg, fg);
        }

        repaint();
        revalidate();
    }

    private void applyDarkUIDefaults(
            Color buttonBg,
            Color buttonFg,
            Color buttonBorder,
            Color fieldBg,
            Color fieldFg,
            Color menuBg,
            Color fg
    ) {
        UIManager.put("Button.background", buttonBg);
        UIManager.put("Button.foreground", buttonFg);
        UIManager.put("Button.select", buttonBg.darker());
        UIManager.put("Button.border", BorderFactory.createLineBorder(buttonBorder, 1));

        UIManager.put("ToggleButton.background", buttonBg);
        UIManager.put("ToggleButton.foreground", buttonFg);

        UIManager.put("ComboBox.background", fieldBg);
        UIManager.put("ComboBox.foreground", fieldFg);

        UIManager.put("TextField.background", fieldBg);
        UIManager.put("TextField.foreground", fieldFg);
        UIManager.put("TextField.caretForeground", fieldFg);

        UIManager.put("FormattedTextField.background", fieldBg);
        UIManager.put("FormattedTextField.foreground", fieldFg);
        UIManager.put("FormattedTextField.caretForeground", fieldFg);

        UIManager.put("Spinner.background", fieldBg);
        UIManager.put("Spinner.foreground", fieldFg);
        UIManager.put("Spinner.border", BorderFactory.createLineBorder(buttonBorder, 1));

        UIManager.put("Panel.background", menuBg);
        UIManager.put("MenuBar.background", menuBg);
        UIManager.put("MenuBar.foreground", fg);
        UIManager.put("Menu.background", menuBg);
        UIManager.put("Menu.foreground", fg);
        UIManager.put("MenuItem.background", menuBg);
        UIManager.put("MenuItem.foreground", fg);
        UIManager.put("CheckBoxMenuItem.background", menuBg);
        UIManager.put("CheckBoxMenuItem.foreground", fg);
        UIManager.put("PopupMenu.background", menuBg);

        UIManager.put("OptionPane.background", menuBg);
        UIManager.put("OptionPane.messageForeground", fg);
    }

    private void restoreSystemUIDefaults() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
    }

    private void styleSpinner(JSpinner spinner, Color bg, Color fg, Color border) {
        spinner.setBackground(bg);
        spinner.setForeground(fg);
        spinner.setOpaque(true);
        spinner.setBorder(BorderFactory.createLineBorder(border, 1));

        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JFormattedTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
            tf.setBackground(bg);
            tf.setForeground(fg);
            tf.setCaretColor(fg);
            tf.setOpaque(true);
            tf.setBorder(new EmptyBorder(4, 6, 4, 6));
        }

        for (Component c : spinner.getComponents()) {
            c.setBackground(bg);
            c.setForeground(fg);

            if (c instanceof JButton) {
                JButton btn = (JButton) c;
                btn.setBackground(bg);
                btn.setForeground(fg);
                btn.setOpaque(true);
                btn.setContentAreaFilled(true);
                btn.setFocusPainted(false);
                btn.setBorder(BorderFactory.createLineBorder(border, 1));
            }
        }
    }

    private void updateComponentTreeColors(
            Component comp,
            Color fg,
            Color cardBg,
            Color fieldBg,
            Color fieldFg,
            Color buttonBg,
            Color buttonFg,
            Color buttonBorder
    ) {
        if (comp instanceof JLabel) {
            String text = ((JLabel) comp).getText();
            if (text != null && (
                    text.equals("Subject Name") ||
                    text.equals("Units") ||
                    text.equals("Grade"))) {
                comp.setForeground(fg);
            }
        }

        if (comp instanceof JPanel &&
                comp != rootPanel &&
                comp != headerPanel &&
                comp != footerPanel &&
                comp != centerWrapper) {
            comp.setBackground(cardBg);
        }

        if (comp instanceof JButton) {
            JButton btn = (JButton) comp;
            btn.setBackground(buttonBg);
            btn.setForeground(buttonFg);
            btn.setFocusPainted(false);
            btn.setBorderPainted(true);
            btn.setOpaque(true);
            btn.setContentAreaFilled(true);
            btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(buttonBorder, 1),
                    new EmptyBorder(8, 12, 8, 12)
            ));
        }

        if (comp instanceof JTextField) {
            comp.setBackground(fieldBg);
            comp.setForeground(fieldFg);
        }

        if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                updateComponentTreeColors(child, fg, cardBg, fieldBg, fieldFg, buttonBg, buttonFg, buttonBorder);
            }
        }
    }

    private void styleMenuElement(MenuElement element, Color bg, Color fg) {
        Component comp = element.getComponent();
        comp.setBackground(bg);
        comp.setForeground(fg);

        if (comp instanceof JComponent) {
            ((JComponent) comp).setOpaque(true);
        }

        for (MenuElement child : element.getSubElements()) {
            styleMenuElement(child, bg, fg);
        }
    }

    private void addEntry() {
        String subject = subjectField.getText().trim();
        double units = ((Number) unitsSpinner.getValue()).doubleValue();
        double grade = ((Number) gradeSpinner.getValue()).doubleValue();

        if (subject.isEmpty()) {
            showWarning("Please enter a subject name.");
            return;
        }
        if (units <= 0) {
            showWarning("Units must be greater than 0.");
            return;
        }
        if (grade <= 0) {
            showWarning("Grade must be greater than 0.");
            return;
        }

        double weightedPoints = units * grade;
        tableModel.addRow(new Object[]{
                subject,
                twoDp(units),
                twoDp(grade),
                fourDp.format(weightedPoints)
        });

        subjectField.setText("");
        unitsSpinner.setValue(3.0);
        gradeSpinner.setValue(1.75);
        statusLabel.setText("Added subject: " + subject);
        subjectField.requestFocusInWindow();
    }

    private void removeSelectedEntry() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showWarning("Please select a row to remove.");
            return;
        }
        String subject = String.valueOf(tableModel.getValueAt(row, 0));
        tableModel.removeRow(row);
        statusLabel.setText("Removed subject: " + subject);
        calculateIfPossible();
    }

    private void calculateGwa() {
        if (tableModel.getRowCount() == 0) {
            showWarning("Please add at least one subject before calculating.");
            return;
        }

        double totalWeighted = 0.0;
        double totalUnits = 0.0;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            double units = Double.parseDouble(String.valueOf(tableModel.getValueAt(i, 1)));
            double grade = Double.parseDouble(String.valueOf(tableModel.getValueAt(i, 2)));
            totalWeighted += units * grade;
            totalUnits += units;
        }

        double gwa = totalWeighted / totalUnits;
        resultValue.setText(fourDp.format(gwa));
        statusLabel.setText("GWA computed successfully for " + tableModel.getRowCount() + " subject(s).");
    }

    private void calculateIfPossible() {
        if (tableModel.getRowCount() == 0) {
            resultValue.setText("0.0000");
        } else {
            calculateGwa();
        }
    }

    private void clearAll() {
        subjectField.setText("");
        unitsSpinner.setValue(3.0);
        gradeSpinner.setValue(1.75);
        tableModel.setRowCount(0);
        resultValue.setText("0.0000");
        statusLabel.setText("All entries cleared.");
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "GWACalc", JOptionPane.WARNING_MESSAGE);
        statusLabel.setText(message);
    }

    private String twoDp(double value) {
        return String.format("%.2f", value);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GWACalc().setVisible(true));
    }
}
