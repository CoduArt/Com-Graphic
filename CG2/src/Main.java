import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

class Direction {
    int[][] direction = new int[][] {{-1,0}, {-1,1}, {0,1}, {1,1}};;

    public void down() {
        direction = new int[][] {{-1,0}, {-1,1}, {0,1}, {1,1}};
    }

    public void left() {
        direction = new int[][] {{-1,1}, {-1,0}, {-1,-1}, {0,-1}};
    }

    public void up() {
        direction = new int[][] {{-1,-1}, {0,-1}, {1,-1}, {1,0}};
    }

    public void right() {
        direction = new int[][] {{1,-1}, {1,0}, {1,1}, {0,1}};
    }

    public int getX(int i) {
        return direction[i][0];
    }

    public int getY(int i) {
        return direction[i][1];
    }
}

class Point {
    private int x;
    private int y;
    private int[] rgb = new int[3];

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(int x, int y, int r, int g, int b) {
        this.x = x;
        this.y = y;
        this.rgb[0] = r;
        this.rgb[1] = g;
        this.rgb[2] = b;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRed() {
        return rgb[0];
    }

    public int getGreen() {
        return rgb[1];
    }

    public int getBlue() {
        return rgb[2];
    }
}

class Viewport extends JPanel {
    private final int VIEWPORT_WIDTH = 640;
    private final int VIEWPORT_HEIGHT = 480;

    private final int ELLIPSE_X = 350;
    private final int ELLIPSE_Y = 250;
    private final int ELLIPSE_B = 100;
    private final int ELLIPSE_A = 200;

    private ArrayList<Point> coloredPointList = new ArrayList<>();
    private final Color START_RGB = new Color(0xF80A0A);
    private final Color END_RGB = new Color(0xD7A606);

    Direction direction = new Direction();
    Point curPoint = new Point(ELLIPSE_X + ELLIPSE_A, ELLIPSE_Y);
    ArrayList<Point> pointList = new ArrayList<>();
    boolean fin = true;

    public Viewport() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(VIEWPORT_WIDTH, VIEWPORT_HEIGHT));
        makePointList();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Point point: coloredPointList) {
            g.setColor(new Color(point.getRed(), point.getGreen(), point.getBlue()));
            g.drawRect(point.getX(), point.getY(), 1, 1);
        }

        g.setColor(Color.BLACK);

        for (Point point: pointList) {
            g.drawRect(point.getX(), point.getY(), 1 , 1);
        }
    }

    private void makePointList() {
        while (fin) {
            pointList.add(curPoint);
            logicEnd();
        }
    }

    private void logicEnd() {
        makeNewCurPoint();
        if (curPoint.getX() == ELLIPSE_X + ELLIPSE_A && curPoint.getY() == ELLIPSE_Y) {
            fin = false;
        }
        defineDirection();
    }

    private void defineDirection() {
        if (curPoint.getX() > ELLIPSE_X) {
            if (curPoint.getY() > ELLIPSE_Y) {
                direction.down();
            } else {
                direction.right();
            }
        } else {
            if (curPoint.getY() > ELLIPSE_Y) {
                direction.left();
            } else {
                direction.up();
            }
        }
    }

    private void makeNewCurPoint() {
        double func;
        Point newCur = null;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < 4; i++) {
            func = Math.abs((Math.pow(curPoint.getX() - ELLIPSE_X + direction.getX(i), 2) / Math.pow(ELLIPSE_A, 2)) +
                                (Math.pow(curPoint.getY() - ELLIPSE_Y + direction.getY(i), 2) / Math.pow(ELLIPSE_B, 2)) - 1);

            if (func < min) {
                min = Math.min(min, func);
                newCur = new Point(curPoint.getX() + direction.getX(i), curPoint.getY() + direction.getY(i));
            }


        }
        curPoint = newCur;
    }

    public void colorInterpolation(int mouseX, int mouseY) {
        double longDistance = findLongDistance(mouseX, mouseY);
        makeColorList(longDistance, mouseX, mouseY);
    }

    public boolean inEllipse(int x, int y) {
        return (Math.pow(x - ELLIPSE_X, 2) / Math.pow(ELLIPSE_A, 2)) + (Math.pow(y - ELLIPSE_Y, 2) / Math.pow(ELLIPSE_B, 2)) < 1;
    }

    private void makeColorList(final double LONG_DISTANCE, int centerX, int centerY) {
        double alpha;
        double distance;
        for (int y = ELLIPSE_Y - ELLIPSE_B; y < ELLIPSE_Y + ELLIPSE_B; y++) {
            for (int x = ELLIPSE_X - ELLIPSE_A; x < ELLIPSE_X + ELLIPSE_A; x++) {
                if (inEllipse(x, y)) {
                    distance = distanceBetweenPoints(x, y, centerX, centerY);
                    alpha = distance / LONG_DISTANCE;
                    if (alpha > 1) {
                        throw new RuntimeException("alpha");
                    }
                    int r = (int) (alpha * START_RGB.getRed() + (1 - alpha) * END_RGB.getRed());
                    int g = (int) (alpha * START_RGB.getGreen() + (1 - alpha) * END_RGB.getGreen());
                    int b = (int) (alpha * START_RGB.getBlue() + (1 - alpha) * END_RGB.getBlue());
                    coloredPointList.add(new Point(x, y, r, g, b));
                }
            }
        }
    }

    private double distanceBetweenPoints(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private double findLongDistance(int x, int y) {
        double maxDistance = Integer.MIN_VALUE;
        for (Point point: pointList) {
            maxDistance = Math.max(maxDistance, distanceBetweenPoints(x, y, point.getX(), point.getY()));
            }
        return maxDistance;
    }
}

public class Main extends JFrame {
    private Timer timer;
    private Viewport viewport = new Viewport();

    public Main() {
        super("Hello, graphics!");

        // Запрещаем масштабирование
        setResizable(false);
        // Останавливаем процесс приложения при закрытии окна
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Добавляем рисуемую область на окно
        getContentPane().add(viewport);
        // Перемасштабируем все элементы, чтобы их размер
        // соответствовал заданному
        pack();

        // Показываем окно на экране
        setVisible(true);

        timer = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewport.repaint();
            }
        });

        timer.start();
        viewport.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (viewport.inEllipse(e.getX(), e.getY())) {
                    viewport.colorInterpolation(e.getX(), e.getY());
                }

            }
        });

    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}