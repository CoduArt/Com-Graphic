import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

// Требования к лабораторной работе
//  - Не использовать магические числа
//        drawRect(123, 123, 12, 15); - плохо
//        drawRect(HOUSE_POS_X, HOUSE_POS_Y, HOUSE_WIDTH, HOUSE_HEIGHT); - хорошо
//  - Выносить рисовку отдельных объектов в отдельные методы
//      - Рисовать всё внутри paintComponent - плохо.
//      - Желательно создавать отдельные методы для рисования.
//          public void drawHouse(Graphics g,
//              int posX, int posY, int width, int height, Color color);
//   - Для получения максимального балла по задаче нужна анимация.

class BushAndMark {
    private int BAMPosX;
    private int BAMPosY;
    private int BAMHeight;
    private int BAMWidth;

    public BushAndMark(int BAMPosX, int BAMPosY, int BAMHeight, int BAMWidth) {
        this.BAMPosX = BAMPosX;
        this.BAMPosY = BAMPosY;
        this.BAMHeight = BAMHeight;
        this.BAMWidth = BAMWidth;
    }

    public int getBAMPosX() {
        return BAMPosX;
    }

    public int getBAMPosY() {
        return BAMPosY;
    }

    public int getBAMHeight() {
        return BAMHeight;
    }

    public int getBAMWidth() {
        return BAMWidth;
    }

    public void setBAMPosX(int BAMPosX) {
        this.BAMPosX = BAMPosX;
    }

    public void setBAMPosY(int BAMPosY) {
        this.BAMPosY = BAMPosY;
    }
}

class Viewport extends JPanel {
    private final int VIEWPORT_WIDTH = 640;
    private final int VIEWPORT_HEIGHT = 480;
    private final int SPEED = 200;

    private Random random = new Random();

    private ArrayList<BushAndMark> bushList = new ArrayList<>();
    private ArrayList<BushAndMark> markList = new ArrayList<>();

    private final int ROAD_X = 0;
    private final int ROAD_Y = 300;
    private final int ROAD_WIDTH = 640;
    private final int ROAD_HEIGHT = VIEWPORT_HEIGHT - ROAD_Y;
    private final int INTEND = 50;

    int moving = -1;
    int carCorpusX = ROAD_X + 70;
    int carCorpusY = ROAD_Y + 100;
    int wheelX = carCorpusX;
    int wheelY = carCorpusY;


    public Viewport() {
        setBackground(new Color(143, 216, 239));
        setPreferredSize(new Dimension(VIEWPORT_WIDTH, VIEWPORT_HEIGHT));

        final int BUSH_COUNT = 20;
        final int BUSH_RADIUS = INTEND / 2;
        final int BUSH_START = -INTEND;
        final int BUSH_END = VIEWPORT_WIDTH + INTEND;

        final int BUSH_RANGE = (BUSH_END - BUSH_START) / BUSH_COUNT;

        for (int count = 1; count <= BUSH_COUNT; count++) {

            bushList.add(new BushAndMark(BUSH_RANGE * count + BUSH_START - BUSH_RADIUS + random.nextInt(7),
                    ROAD_Y - BUSH_RADIUS + random.nextInt(7), BUSH_RADIUS * 2, BUSH_RADIUS * 2));
        }

        final int MARK_START = -INTEND;
        final int MARK_END = VIEWPORT_WIDTH + INTEND;

        final int MARK_COUNT = 10;
        final int MARK_INDENT = 40;
        final int MARK_RANGE = (MARK_END - MARK_START) / 10;
        final int MARK_LENGTH = (MARK_END - MARK_START - MARK_INDENT * (MARK_COUNT - 1)) / (MARK_COUNT - 2);
        final int MARK_HEIGHT = 5;


        for (int count = 1; count <= MARK_COUNT; count++) {
            markList.add(new BushAndMark(count * MARK_RANGE + MARK_START - MARK_LENGTH / 2, ROAD_Y + ROAD_HEIGHT / 2 + 10, MARK_HEIGHT, MARK_LENGTH));
        }

    }

    public void update(float delta) {
        for (BushAndMark bush: bushList) {
            bush.setBAMPosX((int) (bush.getBAMPosX() - SPEED * delta));
            if (bush.getBAMPosX() + bush.getBAMWidth() / 2 < -INTEND) {
                bush.setBAMPosX(VIEWPORT_WIDTH);
            }
        }
        for (BushAndMark mark: markList) {
            mark.setBAMPosX((int) (mark.getBAMPosX() - SPEED * delta));
            if (mark.getBAMPosX() + mark.getBAMWidth() / 2 < - INTEND) {
                mark.setBAMPosX(VIEWPORT_WIDTH);
            }
        }
        if (carCorpusY < ROAD_Y + 95 || carCorpusY > ROAD_Y + 100) {
            moving *= -1;
        }
        carCorpusY += (int)(SPEED * delta * 0.2 * moving);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        final int ROAD_MARK_WIDTH = ROAD_WIDTH;
        final int ROAD_MARK_HEIGHT = 5;
        final int ROAD_MARK_X = ROAD_X;
        final int ROAD_MARK_Y = ROAD_Y + 10;

        final int CAR_CORPUS_HEIGHT = 40;
        final int CAR_CORPUS_WEIGHT = 160;
        final int WHEEL_RADIUS = 15;


        g.setColor(new Color(0x206522));

        for (BushAndMark bush: bushList) {
            g.fillOval(bush.getBAMPosX(), bush.getBAMPosY(), bush.getBAMWidth(), bush.getBAMHeight());
        }

        g.setColor(new Color(0x4A4A4A));
        g.fillRect(ROAD_X, ROAD_Y, ROAD_WIDTH, ROAD_HEIGHT);

        g.setColor(new Color(0xC4C4C4));
        g.fillRect(ROAD_MARK_X, ROAD_MARK_Y, ROAD_MARK_WIDTH, ROAD_MARK_HEIGHT);

        for (BushAndMark mark: markList) {
            g.fillOval(mark.getBAMPosX(), mark.getBAMPosY(), mark.getBAMWidth(), mark.getBAMHeight());
        }

        drawWheel(g, (int) (CAR_CORPUS_WEIGHT * 0.2), CAR_CORPUS_HEIGHT, WHEEL_RADIUS);
        drawWheel(g, (int) (CAR_CORPUS_WEIGHT * 0.8), CAR_CORPUS_HEIGHT, WHEEL_RADIUS);

        drawCar(g, CAR_CORPUS_WEIGHT, CAR_CORPUS_HEIGHT);
    }

    public void drawCar(Graphics g, int carCorpusWeight, int carCorpusHeight) {
        g.setColor(new Color(0xA41E1E));
        g.fillRect(carCorpusX, carCorpusY, carCorpusWeight, carCorpusHeight);
        g.fillRect((int) (carCorpusX + carCorpusWeight * 0.25), carCorpusY - carCorpusHeight, (int) (carCorpusWeight * 0.5), carCorpusHeight);
        g.setColor(new Color(0x231E1E));
        g.fillRect((int) (carCorpusX + carCorpusWeight * 0.5), carCorpusY - carCorpusHeight, (int) (carCorpusWeight * 0.25), carCorpusHeight);
    }

    public void drawWheel(Graphics g, int carCorpusWeight, int carCorpusHeight, int wheelRadius) {
        g.setColor(new Color(0x170F0F));
        g.fillOval( carCorpusX + carCorpusWeight - wheelRadius,
                wheelY + carCorpusHeight - wheelRadius, wheelRadius * 2, wheelRadius * 2);
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

        // Создаём таймер, который будет "тикать" раз в 33 миллисекунды.
        // В каждом тике вызываем обновление сцены (для анимации)
        // и перерисовку сцены.
        timer = new Timer(33, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewport.update(1.0f / 33.0f);
                viewport.repaint();
            }
        });

        // Показываем окно на экране
        setVisible(true);

        // Запускаем таймер
        timer.start();

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}