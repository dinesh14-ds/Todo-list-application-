package src;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static src.ModernUIComponents.*;

public class TodoApp extends JFrame {
    private List<Task> tasks;
    private List<Task> filteredTasks;

    // UI Components
    private JPanel taskListPanel;
    private JScrollPane scrollPane;
    private CustomProgressBar progressBar;
    private JLabel statsLabel;
    private JLabel completionPercentageLabel;
    
    // Inputs
    private ModernTextField titleField;
    private ModernTextField descField;
    private ModernTextField categoryField;
    private ModernTextField dateField;
    private String selectedPriority = "Medium";
    private ModernButton btnLowPriority;
    private ModernButton btnMedPriority;
    private ModernButton btnHighPriority;
    
    // Filter controls
    private ModernTextField searchField;
    private JComboBox<String> categoryFilterComboBox;
    private String statusFilter = "All"; // "All", "Pending", "Completed"
    private ModernButton btnFilterAll;
    private ModernButton btnFilterPending;
    private ModernButton btnFilterCompleted;

    public TodoApp() {
        // Load initial tasks
        tasks = StorageManager.loadTasks();
        filteredTasks = new ArrayList<>(tasks);

        // Window Settings
        setTitle("Ds task - Personal Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null); // Center on screen
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout());

        // Header Panel (Top Title and Stats Dashboard)
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Main Content Split Layout
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setOpaque(false);
        mainContentPanel.setBorder(new EmptyBorder(10, 25, 25, 25));

        // Left Sidebar: Form to Add Task
        JPanel leftPanel = createAddTaskFormPanel();
        mainContentPanel.add(leftPanel, BorderLayout.WEST);

        // Center Panel: Task List, Search & Filter Controls
        JPanel centerPanel = createTaskListAndFilterPanel();
        mainContentPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainContentPanel, BorderLayout.CENTER);

        // Populate initial UI lists
        refreshUI();
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(25, 25, 10, 25));

        // Left Side: Title and Subtitle
        JPanel titleGroup = new JPanel();
        titleGroup.setLayout(new BoxLayout(titleGroup, BoxLayout.Y_AXIS));
        titleGroup.setOpaque(false);

        JLabel titleLabel = new JLabel("Ds task");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("Manage your productivity with absolute simplicity");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(TEXT_MUTED);

        titleGroup.add(titleLabel);
        titleGroup.add(Box.createRigidArea(new Dimension(0, 4)));
        titleGroup.add(subtitleLabel);

        header.add(titleGroup, BorderLayout.WEST);

        // Right Side: Dashboard stats card
        RoundedPanel statsCard = new RoundedPanel(16, BG_CARD);
        statsCard.setLayout(new BorderLayout());
        statsCard.setBorder(new EmptyBorder(15, 20, 15, 20));
        statsCard.setPreferredSize(new Dimension(320, 75));

        JPanel statsTextPanel = new JPanel(new BorderLayout());
        statsTextPanel.setOpaque(false);

        statsLabel = new JLabel("0 of 0 tasks completed");
        statsLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statsLabel.setForeground(TEXT_MUTED);
        statsTextPanel.add(statsLabel, BorderLayout.WEST);

        completionPercentageLabel = new JLabel("0%");
        completionPercentageLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        completionPercentageLabel.setForeground(ACCENT_PRIMARY);
        statsTextPanel.add(completionPercentageLabel, BorderLayout.EAST);

        statsCard.add(statsTextPanel, BorderLayout.NORTH);

        progressBar = new CustomProgressBar();
        progressBar.setPreferredSize(new Dimension(280, 8));
        statsCard.add(progressBar, BorderLayout.SOUTH);

        header.add(statsCard, BorderLayout.EAST);
        return header;
    }

    private JPanel createAddTaskFormPanel() {
        RoundedPanel formPanel = new RoundedPanel(16, BG_CARD);
        formPanel.setPreferredSize(new Dimension(300, 500));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        JLabel formTitle = new JLabel("CREATE NEW TASK");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formTitle.setForeground(TEXT_PRIMARY);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(formTitle);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Title Input
        JLabel titleLbl = new JLabel("Title *");
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        titleLbl.setForeground(TEXT_MUTED);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(titleLbl);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        titleField = new ModernTextField("What needs to be done?", 20);
        titleField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(titleField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Description Input
        JLabel descLbl = new JLabel("Description (Optional)");
        descLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        descLbl.setForeground(TEXT_MUTED);
        descLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(descLbl);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        descField = new ModernTextField("Add details...", 20);
        descField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(descField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Category Input
        JLabel catLbl = new JLabel("Category (Optional)");
        catLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        catLbl.setForeground(TEXT_MUTED);
        catLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(catLbl);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        categoryField = new ModernTextField("e.g. Work, Personal, Fitness", 20);
        categoryField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(categoryField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Due Date Input
        JLabel dateLbl = new JLabel("Due Date (Optional)");
        dateLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        dateLbl.setForeground(TEXT_MUTED);
        dateLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(dateLbl);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        dateField = new ModernTextField("e.g. Tomorrow, Jun 15, YYYY-MM-DD", 20);
        dateField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(dateField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Priority Selection
        JLabel prioLbl = new JLabel("Priority");
        prioLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        prioLbl.setForeground(TEXT_MUTED);
        prioLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(prioLbl);
        formPanel.add(Box.createRigidArea(new Dimension(0, 6)));

        JPanel prioButtonPanel = new JPanel(new GridLayout(1, 3, 6, 0));
        prioButtonPanel.setOpaque(false);
        prioButtonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        prioButtonPanel.setMaximumSize(new Dimension(300, 32));

        btnLowPriority = new ModernButton("Low", BG_BUTTON_DEFAULT, BG_BUTTON_HOVER, BG_BUTTON_ACTIVE);
        btnMedPriority = new ModernButton("Medium", PRIORITY_MEDIUM_BG, PRIORITY_MEDIUM_BG, PRIORITY_MEDIUM_BG);
        btnMedPriority.setForeground(PRIORITY_MEDIUM_TXT);
        btnHighPriority = new ModernButton("High", BG_BUTTON_DEFAULT, BG_BUTTON_HOVER, BG_BUTTON_ACTIVE);

        btnLowPriority.addActionListener(e -> selectPriority("Low"));
        btnMedPriority.addActionListener(e -> selectPriority("Medium"));
        btnHighPriority.addActionListener(e -> selectPriority("High"));

        prioButtonPanel.add(btnLowPriority);
        prioButtonPanel.add(btnMedPriority);
        prioButtonPanel.add(btnHighPriority);
        formPanel.add(prioButtonPanel);

        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Add Button
        ModernButton btnAddTask = new ModernButton("Create Task", ACCENT_PRIMARY, ACCENT_PRIMARY_HOVER, ACCENT_PRIMARY_ACTIVE);
        btnAddTask.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnAddTask.setMaximumSize(new Dimension(300, 40));
        btnAddTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewTask();
            }
        });
        formPanel.add(btnAddTask);

        // Wrapper Panel to avoid shrinking
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(formPanel, BorderLayout.NORTH);
        wrapper.setBorder(new EmptyBorder(0, 0, 0, 20));
        return wrapper;
    }

    private void selectPriority(String priority) {
        selectedPriority = priority;
        
        // Reset colors
        btnLowPriority.setBackground(BG_BUTTON_DEFAULT);
        btnMedPriority.setBackground(BG_BUTTON_DEFAULT);
        btnHighPriority.setBackground(BG_BUTTON_DEFAULT);
        
        btnLowPriority.setForeground(TEXT_PRIMARY);
        btnMedPriority.setForeground(TEXT_PRIMARY);
        btnHighPriority.setForeground(TEXT_PRIMARY);

        // Highlight selected
        if (priority.equals("Low")) {
            btnLowPriority.setBackground(PRIORITY_LOW_BG);
            btnLowPriority.setForeground(PRIORITY_LOW_TXT);
        } else if (priority.equals("Medium")) {
            btnMedPriority.setBackground(PRIORITY_MEDIUM_BG);
            btnMedPriority.setForeground(PRIORITY_MEDIUM_TXT);
        } else if (priority.equals("High")) {
            btnHighPriority.setBackground(PRIORITY_HIGH_BG);
            btnHighPriority.setForeground(PRIORITY_HIGH_TXT);
        }
    }

    private JPanel createTaskListAndFilterPanel() {
        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);

        // Search and Filter Bar
        JPanel searchFilterBar = new JPanel(new BorderLayout(10, 0));
        searchFilterBar.setOpaque(false);
        searchFilterBar.setBorder(new EmptyBorder(0, 0, 15, 0));

        // Realtime Search Box
        searchField = new ModernTextField("Search tasks...", 20);
        searchField.setPreferredSize(new Dimension(220, 36));
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterTasks(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterTasks(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterTasks(); }
        });
        searchFilterBar.add(searchField, BorderLayout.CENTER);

        // Right side filters (Category + Status)
        JPanel filtersRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filtersRight.setOpaque(false);

        // Category Filter ComboBox
        categoryFilterComboBox = new JComboBox<>();
        categoryFilterComboBox.setPreferredSize(new Dimension(130, 36));
        categoryFilterComboBox.setBackground(BG_CARD);
        categoryFilterComboBox.setForeground(TEXT_PRIMARY);
        categoryFilterComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        categoryFilterComboBox.setBorder(BorderFactory.createLineBorder(new Color(0xcb, 0xd5, 0xe1), 1));
        categoryFilterComboBox.addActionListener(e -> filterTasks());
        filtersRight.add(categoryFilterComboBox);

        // Status Segmented Filters (All, Pending, Completed)
        JPanel statusPanel = new JPanel(new GridLayout(1, 3, 4, 0));
        statusPanel.setOpaque(false);
        statusPanel.setPreferredSize(new Dimension(220, 36));

        btnFilterAll = new ModernButton("All", ACCENT_PRIMARY, ACCENT_PRIMARY_HOVER, ACCENT_PRIMARY_ACTIVE);
        btnFilterAll.setForeground(Color.WHITE);
        btnFilterPending = new ModernButton("Pending", BG_BUTTON_DEFAULT, BG_BUTTON_HOVER, BG_BUTTON_ACTIVE);
        btnFilterCompleted = new ModernButton("Done", BG_BUTTON_DEFAULT, BG_BUTTON_HOVER, BG_BUTTON_ACTIVE);

        btnFilterAll.addActionListener(e -> setStatusFilter("All"));
        btnFilterPending.addActionListener(e -> setStatusFilter("Pending"));
        btnFilterCompleted.addActionListener(e -> setStatusFilter("Completed"));

        statusPanel.add(btnFilterAll);
        statusPanel.add(btnFilterPending);
        statusPanel.add(btnFilterCompleted);
        filtersRight.add(statusPanel);

        searchFilterBar.add(filtersRight, BorderLayout.EAST);
        container.add(searchFilterBar, BorderLayout.NORTH);

        // Task List Container
        taskListPanel = new JPanel();
        taskListPanel.setBackground(BG_DARK);
        taskListPanel.setLayout(new BoxLayout(taskListPanel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(taskListPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // Custom styled scrollbar
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected JButton createDecreaseButton(int orientation) { return createZeroButton(); }
            @Override
            protected JButton createIncreaseButton(int orientation) { return createZeroButton(); }
            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }
            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                g.setColor(BG_DARK);
                g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
            }
            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xcb, 0xd5, 0xe1));
                g2.fillRoundRect(thumbBounds.x + 3, thumbBounds.y + 2, thumbBounds.width - 6, thumbBounds.height - 4, 8, 8);
                g2.dispose();
            }
        });
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));

        container.add(scrollPane, BorderLayout.CENTER);
        return container;
    }

    private void setStatusFilter(String filter) {
        statusFilter = filter;
        
        btnFilterAll.setBackground(BG_BUTTON_DEFAULT);
        btnFilterPending.setBackground(BG_BUTTON_DEFAULT);
        btnFilterCompleted.setBackground(BG_BUTTON_DEFAULT);
        
        btnFilterAll.setForeground(TEXT_PRIMARY);
        btnFilterPending.setForeground(TEXT_PRIMARY);
        btnFilterCompleted.setForeground(TEXT_PRIMARY);

        if (filter.equals("All")) {
            btnFilterAll.setBackground(ACCENT_PRIMARY);
            btnFilterAll.setForeground(Color.WHITE);
        } else if (filter.equals("Pending")) {
            btnFilterPending.setBackground(ACCENT_PRIMARY);
            btnFilterPending.setForeground(Color.WHITE);
        } else if (filter.equals("Completed")) {
            btnFilterCompleted.setBackground(ACCENT_PRIMARY);
            btnFilterCompleted.setForeground(Color.WHITE);
        }

        filterTasks();
    }

    private void addNewTask() {
        String title = titleField.getRealText();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Task Title is required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String desc = descField.getRealText();
        String category = categoryField.getRealText();
        String date = dateField.getRealText();

        Task newTask = new Task(title, desc, category, selectedPriority, date);
        tasks.add(0, newTask); // Add new task to the top
        StorageManager.saveTasks(tasks);

        // Reset Inputs
        titleField.resetPlaceholder();
        descField.resetPlaceholder();
        categoryField.resetPlaceholder();
        dateField.resetPlaceholder();
        selectPriority("Medium");

        updateFiltersList();
        refreshUI();
    }

    private void deleteTask(Task task) {
        tasks.remove(task);
        StorageManager.saveTasks(tasks);
        updateFiltersList();
        refreshUI();
    }

    private void toggleTaskCompleted(Task task) {
        task.setCompleted(!task.isCompleted());
        StorageManager.saveTasks(tasks);
        refreshUI();
    }

    private void updateFiltersList() {
        // Collect current list of categories for filter combo box
        String currentSelection = (String) categoryFilterComboBox.getSelectedItem();
        
        categoryFilterComboBox.removeAllItems();
        categoryFilterComboBox.addItem("All Categories");
        
        Set<String> categories = tasks.stream()
                .map(Task::getCategory)
                .filter(cat -> cat != null && !cat.isEmpty())
                .collect(Collectors.toSet());
        
        for (String cat : categories) {
            categoryFilterComboBox.addItem(cat);
        }

        // Try to restore previous selection
        if (currentSelection != null) {
            categoryFilterComboBox.setSelectedItem(currentSelection);
        }
    }

    private void filterTasks() {
        String query = searchField.getRealText().toLowerCase().trim();
        String selectedCategory = (String) categoryFilterComboBox.getSelectedItem();

        filteredTasks = tasks.stream()
                .filter(t -> {
                    // Search Filter
                    if (query.isEmpty()) return true;
                    return t.getTitle().toLowerCase().contains(query) || 
                           t.getDescription().toLowerCase().contains(query);
                })
                .filter(t -> {
                    // Category Filter
                    if (selectedCategory == null || selectedCategory.equals("All Categories")) return true;
                    return t.getCategory().equals(selectedCategory);
                })
                .filter(t -> {
                    // Status Filter
                    if (statusFilter.equals("All")) return true;
                    if (statusFilter.equals("Pending")) return !t.isCompleted();
                    return t.isCompleted();
                })
                .collect(Collectors.toList());

        renderTaskList();
    }

    private void refreshUI() {
        // Update Stats Dashboard
        int total = tasks.size();
        long completed = tasks.stream().filter(Task::isCompleted).count();
        statsLabel.setText(completed + " of " + total + " tasks completed");

        double percentage = total > 0 ? (double) completed / total : 0.0;
        progressBar.setProgress(percentage);
        completionPercentageLabel.setText((int) (percentage * 100) + "%");

        // Sync and refresh filters
        updateFiltersList();
        filterTasks();
    }

    private void renderTaskList() {
        taskListPanel.removeAll();
        
        if (filteredTasks.isEmpty()) {
            // Display clean Empty State panel
            JPanel emptyState = new JPanel();
            emptyState.setLayout(new BoxLayout(emptyState, BoxLayout.Y_AXIS));
            emptyState.setOpaque(false);
            emptyState.setBorder(new EmptyBorder(80, 20, 20, 20));

            JLabel emptyTitle = new JLabel("All clear!");
            emptyTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
            emptyTitle.setForeground(TEXT_MUTED);
            emptyTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel emptySub = new JLabel("No tasks fit your active search or filters");
            emptySub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            emptySub.setForeground(new Color(0x6c, 0x70, 0x86));
            emptySub.setAlignmentX(Component.CENTER_ALIGNMENT);

            emptyState.add(emptyTitle);
            emptyState.add(Box.createRigidArea(new Dimension(0, 6)));
            emptyState.add(emptySub);
            
            taskListPanel.add(emptyState);
        } else {
            for (Task task : filteredTasks) {
                taskListPanel.add(createTaskPanel(task));
                taskListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        taskListPanel.revalidate();
        taskListPanel.repaint();
    }

    private JPanel createTaskPanel(Task task) {
        RoundedPanel panel = new RoundedPanel(12, BG_CARD);
        panel.setLayout(new BorderLayout(15, 0));
        panel.setBorder(new EmptyBorder(12, 16, 12, 16));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // Checkbox on Left
        ModernCheckbox checkbox = new ModernCheckbox(task.isCompleted(), () -> toggleTaskCompleted(task));
        JPanel leftAlign = new JPanel(new GridBagLayout());
        leftAlign.setOpaque(false);
        leftAlign.add(checkbox);
        panel.add(leftAlign, BorderLayout.WEST);

        // Task Content in Center
        JPanel centerContent = new JPanel();
        centerContent.setLayout(new BoxLayout(centerContent, BoxLayout.Y_AXIS));
        centerContent.setOpaque(false);

        JLabel titleLbl = new JLabel(task.getTitle());
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        if (task.isCompleted()) {
            titleLbl.setForeground(new Color(0x6c, 0x70, 0x86));
            // Apply simple strike-through formatting using HTML
            titleLbl.setText("<html><s>" + task.getTitle() + "</s></html>");
        } else {
            titleLbl.setForeground(TEXT_PRIMARY);
        }
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerContent.add(titleLbl);

        if (task.getDescription() != null && !task.getDescription().trim().isEmpty()) {
            centerContent.add(Box.createRigidArea(new Dimension(0, 3)));
            JLabel descLbl = new JLabel(task.getDescription());
            descLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            descLbl.setForeground(TEXT_MUTED);
            descLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            centerContent.add(descLbl);
        }

        panel.add(centerContent, BorderLayout.CENTER);

        // Tags & Actions on Right
        JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightActions.setOpaque(false);

        // Category Badge
        if (task.getCategory() != null && !task.getCategory().isEmpty() && !task.getCategory().equalsIgnoreCase("General")) {
            Badge catBadge = new Badge(task.getCategory().toUpperCase(), BG_BUTTON_DEFAULT);
            rightActions.add(catBadge);
        }

        // Priority Badge
        Color prioBgColor;
        Color prioTxtColor;
        switch (task.getPriority()) {
            case "High" -> {
                prioBgColor = PRIORITY_HIGH_BG;
                prioTxtColor = PRIORITY_HIGH_TXT;
            }
            case "Medium" -> {
                prioBgColor = PRIORITY_MEDIUM_BG;
                prioTxtColor = PRIORITY_MEDIUM_TXT;
            }
            default -> {
                prioBgColor = PRIORITY_LOW_BG;
                prioTxtColor = PRIORITY_LOW_TXT;
            }
        }
        Badge prioBadge = new Badge(task.getPriority().toUpperCase(), prioBgColor);
        prioBadge.setForeground(prioTxtColor);
        rightActions.add(prioBadge);

        // Due Date Badge
        if (task.getDueDate() != null && !task.getDueDate().isEmpty() && !task.getDueDate().equalsIgnoreCase("No Date")) {
            Badge dateBadge = new Badge(task.getDueDate(), BG_INPUT);
            dateBadge.setForeground(TEXT_MUTED);
            rightActions.add(dateBadge);
        }

        // Spacer
        rightActions.add(Box.createRigidArea(new Dimension(5, 0)));

        // Delete Button
        DeleteButton btnDelete = new DeleteButton();
        btnDelete.addActionListener(e -> deleteTask(task));
        rightActions.add(btnDelete);

        JPanel rightAlign = new JPanel(new GridBagLayout());
        rightAlign.setOpaque(false);
        rightAlign.add(rightActions);
        panel.add(rightAlign, BorderLayout.EAST);

        return panel;
    }

    // Custom Delete Button with rounded hover effect
    private static class DeleteButton extends JButton {
        private boolean hovered = false;

        public DeleteButton() {
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setPreferredSize(new Dimension(28, 28));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    hovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (hovered) {
                g2.setColor(new Color(0xf3, 0x8b, 0xa8, 0x30)); // Translucent red hover circle
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(PRIORITY_HIGH);
            } else {
                g2.setColor(TEXT_MUTED);
            }
            
            g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int pad = 9;
            g2.drawLine(pad, pad, getWidth() - pad, getHeight() - pad);
            g2.drawLine(getWidth() - pad, pad, pad, getHeight() - pad);
            
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        // Set Look and Feel to System standard before customizing
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Fallback
        }

        // Custom Swing component overrides
        UIManager.put("ComboBox.background", BG_CARD);
        UIManager.put("ComboBox.foreground", TEXT_PRIMARY);
        UIManager.put("ComboBox.selectionBackground", ACCENT_PRIMARY);
        UIManager.put("ComboBox.selectionForeground", Color.WHITE);
        UIManager.put("ComboBox.border", BorderFactory.createLineBorder(new Color(0xcb, 0xd5, 0xe1)));

        SwingUtilities.invokeLater(() -> {
            TodoApp app = new TodoApp();
            app.setVisible(true);
        });
    }
}
