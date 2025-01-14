import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class DrawingApp extends JFrame {

    private DrawingPanel drawingPanel;

    public DrawingApp() {
        setTitle("ЛР4");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        drawingPanel = new DrawingPanel();
        add(drawingPanel, BorderLayout.CENTER);
        createMenu();
        setVisible(true);
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu colorMenu = new JMenu("Цвет");
        JMenuItem redColor = new JMenuItem("Красный");
        JMenuItem greenColor = new JMenuItem("Зеленый");
        JMenuItem blueColor = new JMenuItem("Синий");
        JMenuItem yellowColor = new JMenuItem("Желтый");

        redColor.addActionListener(e -> drawingPanel.setColor(Color.RED));
        greenColor.addActionListener(e -> drawingPanel.setColor(Color.GREEN));
        blueColor.addActionListener(e -> drawingPanel.setColor(Color.BLUE));
        yellowColor.addActionListener(e -> drawingPanel.setColor(Color.YELLOW));

        colorMenu.add(redColor);
        colorMenu.add(greenColor);
        colorMenu.add(blueColor);
        colorMenu.add(yellowColor);

        JMenu thicknessMenu = new JMenu("Толщина");
        JMenuItem thinLine = new JMenuItem("3px");
        JMenuItem mediumLine = new JMenuItem("5px");
        JMenuItem thickLine = new JMenuItem("7px");
        JMenuItem thickerLine = new JMenuItem("11px");

        thinLine.addActionListener(e -> drawingPanel.setThickness(3));
        mediumLine.addActionListener(e -> drawingPanel.setThickness(5));
        thickLine.addActionListener(e -> drawingPanel.setThickness(7));
        thickerLine.addActionListener(e -> drawingPanel.setThickness(11));

        thicknessMenu.add(thinLine);
        thicknessMenu.add(mediumLine);
        thicknessMenu.add(thickLine);
        thicknessMenu.add(thickerLine);

        JMenuItem eraser = new JMenuItem("Ластик");
        eraser.addActionListener(e -> {
            drawingPanel.setColor(Color.WHITE);
            drawingPanel.setThickness(5);
        });

        JMenuItem addText = new JMenuItem("T");
        addText.addActionListener(e -> drawingPanel.enableTextMode());

        menuBar.add(colorMenu);
        menuBar.add(thicknessMenu);
        menuBar.add(eraser);
        menuBar.add(addText);

        setJMenuBar(menuBar);
    }

    public static void main(String[] args) {
        new DrawingApp();
    }

    private class DrawingPanel extends JPanel {
        private Color currentColor = Color.BLACK;
        private int currentThickness = 3;
        private List<Line> lines = new ArrayList<>();
        private boolean textMode = false;
        private String textToDraw = "";

        public DrawingPanel() {
            setBackground(Color.WHITE);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (textMode) {
                        textToDraw = JOptionPane.showInputDialog("текст");
                        if (textToDraw != null && !textToDraw.isEmpty()) {
                            lines.add(new Line(currentColor, currentThickness, e.getPoint(), textToDraw));
                        }
                        textMode = false;
                    } else {
                        lines.add(new Line(currentColor, currentThickness, e.getPoint()));
                    }
                    repaint();
                }
            });

            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (!textMode) {
                        Line lastLine = lines.get(lines.size() - 1);
                        lastLine.addPoint(e.getPoint());
                        repaint();
                    }
                }
            });
        }

        public void setColor(Color color) {
            this.currentColor = color;
        }

        public void setThickness(int thickness) {
            this.currentThickness = thickness;
        }

        public void enableTextMode() {
            this.textMode = true;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            for (Line line : lines) {
                g2d.setColor(line.getColor());
                if (line.getText() != null) {
                    g2d.setFont(new Font("Arial", Font.PLAIN, 16));
                    g2d.drawString(line.getText(), line.getPoints().get(0).x, line.getPoints().get(0).y);
                } else {
                    g2d.setStroke(new BasicStroke(line.getThickness()));
                    List<Point> points = line.getPoints();
                    for (int i = 1; i < points.size(); i++) {
                        Point p1 = points.get(i - 1);
                        Point p2 = points.get(i);
                        g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
                    }
                }
            }
        }
    }

    private class Line {
        private Color color;
        private int thickness;
        private List<Point> points;
        private String text;

        public Line(Color color, int thickness, Point startPoint) {
            this.color = color;
            this.thickness = thickness;
            this.points = new ArrayList<>();
            this.points.add(startPoint);
            this.text = null;
        }

        public Line(Color color, int thickness, Point startPoint, String text) {
            this.color = color;
            this.thickness = thickness;
            this.points = new ArrayList<>();
            this.points.add(startPoint);
            this.text = text;
        }

        public Color getColor() {
            return color;
        }

        public int getThickness() {
            return thickness;
        }

        public List<Point> getPoints() {
            return points;
        }

        public String getText() {
            return text;
        }

        public void addPoint(Point point) {
            points.add(point);
        }
    }
}
