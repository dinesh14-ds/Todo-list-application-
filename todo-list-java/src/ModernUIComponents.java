package src;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class ModernUIComponents {

    // Theme Colors
    public static final Color BG_DARK = new Color(0x1e, 0x1e, 0x2e);      // Deep Slate
    public static final Color BG_CARD = new Color(0x25, 0x25, 0x38);      // Dark Purple-Gray Card
    public static final Color BG_INPUT = new Color(0x11, 0x11, 0x1b);     // Very Dark Input
    public static final Color ACCENT_PRIMARY = new Color(0x89, 0xb4, 0xfa); // Indigo Blue
    public static final Color ACCENT_PRIMARY_HOVER = new Color(0xb4, 0xbe, 0xfe);
    public static final Color ACCENT_PRIMARY_ACTIVE = new Color(0x74, 0xc7, 0xec);
    
    public static final Color TEXT_PRIMARY = new Color(0xcd, 0xd6, 0xf4);   // White
    public static final Color TEXT_MUTED = new Color(0xa6, 0xad, 0xc8);     // Muted Gray
    
    // Priority Colors
    public static final Color PRIORITY_HIGH = new Color(0xf3, 0x8b, 0xa8);   // Neon Red
    public static final Color PRIORITY_MEDIUM = new Color(0xfa, 0xb3, 0x87); // Peach/Orange
    public static final Color PRIORITY_LOW = new Color(0xa6, 0xe3, 0xa1);    // Soft Green
    
    // Custom Rounded Panel
    public static class RoundedPanel extends JPanel {
        private int radius;
        private Color bgColor;

        public RoundedPanel(int radius, Color bgColor) {
            super();
            this.radius = radius;
            this.bgColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor != null ? bgColor : getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Custom Modern Flat Button
    public static class ModernButton extends JButton {
        private Color baseColor;
        private Color hoverColor;
        private Color activeColor;
        private int cornerRadius = 12;

        public ModernButton(String text, Color baseColor, Color hoverColor, Color activeColor) {
            super(text);
            this.baseColor = baseColor;
            this.hoverColor = hoverColor;
            this.activeColor = activeColor;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setForeground(TEXT_PRIMARY);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setBackground(baseColor);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    setBackground(hoverColor);
                    repaint();
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    setBackground(baseColor);
                    repaint();
                }

                @Override
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    setBackground(activeColor);
                    repaint();
                }

                @Override
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    if (getBounds().contains(evt.getPoint())) {
                        setBackground(hoverColor);
                    } else {
                        setBackground(baseColor);
                    }
                    repaint();
                }
            });
        }

        public void setCornerRadius(int radius) {
            this.cornerRadius = radius;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Custom Rounded TextField
    public static class ModernTextField extends JTextField implements FocusListener {
        private String placeholder;
        private boolean isShowingPlaceholder;
        private Color focusColor = ACCENT_PRIMARY;
        private Color borderColor = new Color(0x31, 0x32, 0x44);

        public ModernTextField(String placeholder, int columns) {
            super(columns);
            this.placeholder = placeholder;
            this.isShowingPlaceholder = true;
            
            setOpaque(false);
            setCaretColor(TEXT_PRIMARY);
            setForeground(TEXT_MUTED);
            setText(placeholder);
            setFont(new Font("Segoe UI", Font.PLAIN, 13));
            setBorder(new EmptyBorder(8, 12, 8, 12));
            setBackground(BG_INPUT);
            
            addFocusListener(this);
        }

        public String getRealText() {
            return isShowingPlaceholder ? "" : getText();
        }

        public void resetPlaceholder() {
            setText(placeholder);
            setForeground(TEXT_MUTED);
            isShowingPlaceholder = true;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Fill background
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            
            // Draw border
            g2.setColor(hasFocus() ? focusColor : borderColor);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 10, 10);
            
            g2.dispose();
            super.paintComponent(g);
        }

        @Override
        public void focusGained(FocusEvent e) {
            if (isShowingPlaceholder) {
                isShowingPlaceholder = false;
                setText("");
                setForeground(TEXT_PRIMARY);
            }
            repaint();
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (getText().isEmpty()) {
                isShowingPlaceholder = true;
                setText(placeholder);
                setForeground(TEXT_MUTED);
            }
            repaint();
        }
    }

    // Custom Badge for priority or category
    public static class Badge extends JLabel {
        private Color badgeColor;

        public Badge(String text, Color badgeColor) {
            super(text, SwingConstants.CENTER);
            this.badgeColor = badgeColor;
            setOpaque(false);
            setForeground(TEXT_PRIMARY);
            setFont(new Font("Segoe UI", Font.BOLD, 10));
            setBorder(new EmptyBorder(4, 10, 4, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(badgeColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Custom Checkbox that renders visually without native styling
    public static class ModernCheckbox extends JComponent {
        private boolean selected = false;
        private Runnable onClick;

        public ModernCheckbox(boolean initial, Runnable onClick) {
            this.selected = initial;
            this.onClick = onClick;
            setPreferredSize(new Dimension(22, 22));
            setMinimumSize(new Dimension(22, 22));
            setMaximumSize(new Dimension(22, 22));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    selected = !selected;
                    repaint();
                    if (onClick != null) {
                        onClick.run();
                    }
                }
            });
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int w = getWidth() - 4;
            int h = getHeight() - 4;
            int x = 2;
            int y = 2;
            
            if (selected) {
                // Draw completed checked box
                g2.setColor(ACCENT_PRIMARY);
                g2.fillRoundRect(x, y, w, h, 6, 6);
                
                // Draw checkmark
                g2.setColor(BG_DARK);
                g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(x + 5, y + 10, x + 9, y + 14);
                g2.drawLine(x + 9, y + 14, x + 15, y + 6);
            } else {
                // Draw empty border box
                g2.setColor(BG_INPUT);
                g2.fillRoundRect(x, y, w, h, 6, 6);
                
                g2.setColor(new Color(0x45, 0x47, 0x5a));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(x, y, w, h, 6, 6);
            }
            g2.dispose();
        }
    }

    // Custom Rounded Progress Bar
    public static class CustomProgressBar extends JComponent {
        private double progress = 0.0; // 0.0 to 1.0

        public void setProgress(double progress) {
            this.progress = Math.max(0.0, Math.min(1.0, progress));
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int w = getWidth();
            int h = getHeight();
            int arc = h;
            
            // Draw background
            g2.setColor(new Color(0x31, 0x32, 0x44));
            g2.fillRoundRect(0, 0, w, h, arc, arc);
            
            // Draw foreground fill
            int fillW = (int) (w * progress);
            if (fillW > 0) {
                // Gradient for visual interest
                GradientPaint gp = new GradientPaint(0, 0, ACCENT_PRIMARY, fillW, 0, ACCENT_PRIMARY_HOVER);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, fillW, h, arc, arc);
            }
            
            g2.dispose();
        }
    }
}
