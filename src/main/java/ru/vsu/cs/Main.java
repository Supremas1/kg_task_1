package ru.vsu.cs;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.util.Random;


class Window extends JFrame {
    private Random random = new Random();
    class BackgroundPanel extends JPanel {
        private Image plain; // Добавляем поле для хранения изображения фона

        public BackgroundPanel() {
            // Загружаем изображение фона
            plain = new ImageIcon("src\\main\\java\\ru\\vsu\\cs\\plain.png").getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            GradientPaint gradient = new GradientPaint(
                    950, 0, new Color(0x480148),
                    950, getHeight() - 300, new Color(0x8A0135)
            );
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(gradient);
            g2.fillRect(0, 0, getWidth(), getHeight());
            drawStars(g2);
            GradientPaint gradient2 = new GradientPaint(
                    1450, 200, new Color(0x7E7D7D),
                    1600, 228, new Color(0x04D4D4D, true)
            );
            g2.setPaint(gradient2);

            // Рисуем самолёт
            g2.drawLine(1450, 200, 1600, 215);
            g2.drawLine(1440, 213, 1590, 228);
            if (plain != null) {
                g2.drawImage(plain, 1400, 150, this);
            }
        }
    }
    public Window(int size, int height) {
        setSize(size, height);
        setVisible(true);
        setTitle("Picture");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Создаем JLayeredPane для управления слоями
        JLayeredPane layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setBounds(0, 0, size, height);
        layeredPane.add(backgroundPanel, JLayeredPane.FRAME_CONTENT_LAYER);

        //панель солнца
        JPanel sunPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setOpaque(false); // Установите прозрачный фон

                int sunDiameter = Math.min(getWidth(), getHeight()) / 2;
                int sunX = (getWidth() - sunDiameter) / 2;
                int sunY = (getHeight() - sunDiameter) / 2 - 130;
                Ellipse2D.Double sun = new Ellipse2D.Double(sunX, sunY, sunDiameter, sunDiameter);
                GradientPaint gradient = new GradientPaint(
                        950, sunY, new Color(0xF6F500), // Начальный цвет (левый верхний угол)
                        950, getHeight() - 430, new Color(0xBB007A) // Конечный цвет (правый нижний угол)
                );

                // Ограничиваем область, в которой рисуем полосы (внутри солнца)
                ((Graphics2D) g).clip(sun);
                ((Graphics2D) g).setPaint(gradient); // Цвет круга
                g.fillOval(sunX, sunY, sunDiameter, sunDiameter);

                // Рисуем статические полосы на солнце
                GradientPaint gradient1 = new GradientPaint(
                        950, sunY, new Color(0xD7004B), // Начальный цвет (левый верхний угол)
                        950, getHeight() - 430, new Color(0x00CBAC)); // Конечный цвет (правый нижний угол)
                ((Graphics2D) g).setPaint(gradient1); // Цвет полос
                int stripeHeight = 15; // Высота полос
                int stripeWidth = sunDiameter + 10; // Ширина полос
                int numStripes = 30; // Количество полос
                int gap = 25; // Промежуток между полосами
                for (int i = 0; i < numStripes; i++) {
                    int stripeX = (sunX + sunDiameter / 4) - 140;
                    int stripeY = (sunY + sunDiameter / 4 + i * (stripeHeight + gap)) - 110;
                    g.fillRect(stripeX, stripeY, stripeWidth, stripeHeight);
                }
            }
            @Override
            public boolean isOpaque() {
                return false; // Устанавливаем непрозрачность в false
            }
        };

        sunPanel.setBounds(0, 0, size, height);
        layeredPane.add(sunPanel, JLayeredPane.DEFAULT_LAYER);

        //панель гор
        JPanel mountainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawAndFillMountain((Graphics2D) g);
            }
            @Override
            public boolean isOpaque() {
                return false; // Устанавливаем непрозрачность в false
            }
        };
        mountainPanel.setBounds(0, 0, size, height);
        layeredPane.add(mountainPanel, JLayeredPane.PALETTE_LAYER);

        //панель поля квадратов
        JPanel squaresPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawAndFillSquares((Graphics2D) g);
                drawRoad((Graphics2D) g);
            }
            @Override
            public boolean isOpaque() {
                return false; // Устанавливаем непрозрачность в false
            }
        };
        squaresPanel.setBounds(0, 0, size, height);
        layeredPane.add(squaresPanel, JLayeredPane.MODAL_LAYER);
    }
    // Метод для рисования гор
    private void drawAndFillMountain(Graphics2D g) {
        int[] xPoints = {-150, 0, 50, 80, 130, 260, 340, 420, 530, 620, 680, 730, 770, 860, 900, 950, 1050, 1150, 1200,
                1250, 1300, 1340, 1440, 1520, 1600, 1670, 1730, 1790, 1850, 1920, 1930}; // X-координаты вершин гор

        int[] yPoints = {getHeight() - 300, getHeight() - 490, getHeight() - 500, getHeight() - 530, getHeight() - 545, 410, getHeight() - 570,
                getHeight() - 620, getHeight() - 670, getHeight() - 590, getHeight() - 500, getHeight() - 510, getHeight() - 540, getHeight() - 580,
                getHeight() - 620, getHeight() - 585, getHeight() - 545, getHeight() - 590, getHeight() - 650, getHeight() - 670, getHeight() - 680,
                getHeight() - 630, getHeight() - 560, getHeight() - 600, getHeight() - 690, getHeight() - 650, getHeight() - 620, getHeight() - 600,
                getHeight() - 540, getHeight() - 500, getHeight() - 300}; // Y-координаты вершин гор

        GeneralPath mountain = new GeneralPath(); // Общий путь для гор
        mountain.moveTo(xPoints[0], yPoints[0]);
        for (int i = 1; i < xPoints.length; i++) {
            mountain.lineTo(xPoints[i], yPoints[i]);
        }
        mountain.closePath();

        g.setPaint(new Color(0x8D003F)); // цвет для контура гор
        g.setStroke(new BasicStroke(2)); // Толщина контура
        g.draw(mountain); // Рисуем контур гор

        GradientPaint fillGradient = new GradientPaint(
                950, 100, new Color(0xFF25174F, true), // Начальный цвет
                950, getHeight() - 300, new Color(0x000000) // Конечный цвет
        );
        g.setPaint(fillGradient); // цвет для заливки гор
        g.fill(mountain); // Заливаем горы

        GradientPaint gradient = new GradientPaint(
                950, 100, new Color(0x8D003F), // Начальный цвет
                950, getHeight() - 300, new Color(0x2C0014) // Конечный цвет
        );
        g.setStroke(new BasicStroke(2));
        g.setPaint(gradient);
        g.drawLine(0, getHeight() - 490, 80, getHeight() - 395);
        g.drawLine(80, getHeight() - 395, 120, getHeight() - 200);
        g.drawLine(80, getHeight() - 530, 200, getHeight() - 430);
        g.drawLine(200, getHeight() - 430, 350, getHeight());
        g.drawLine(620, getHeight() - 590, 480, getHeight()-570);
        g.drawLine(480, getHeight()-570, 400, getHeight()-500);
        g.drawLine(340, getHeight() - 570, 423, getHeight()-470);
        g.drawLine(423, getHeight()-470, 650, getHeight());
        g.drawLine(860,getHeight() - 580, 970, getHeight() - 480);
        g.drawLine(970, getHeight() - 480, 1100, getHeight());
        g.drawLine(1150, getHeight() - 590, 1230, getHeight() - 550);
        g.drawLine(1230, getHeight() - 550, 1297, getHeight()-480);
        g.drawLine(680, getHeight() - 500, 433, getHeight()-450);
        g.drawLine(730, getHeight() - 510, 550, getHeight());
        g.drawLine(1050, getHeight() - 545, 1200, getHeight() - 400);
        g.drawLine(1200, getHeight() - 400, 1300,getHeight() - 200);
        g.drawLine(1440, getHeight() - 560, 1300, getHeight() - 480);
        g.drawLine(1300, getHeight() - 480, 1215,getHeight() - 374);
        g.drawLine(1520, getHeight() - 600, 1700, getHeight() - 450);
        g.drawLine(1700, getHeight() - 450, 1900, getHeight());
        g.drawLine(1730,getHeight() - 620, 1600, getHeight() - 533);
    }

    //метод рисования поля квадратов
    private void drawAndFillSquares(Graphics2D g) {
        int[] xPoints = {-5, 1925, 1925, -5};
        int[] yPoints = {getHeight() - 300, getHeight() - 300, getHeight(), getHeight()};
        GeneralPath squares = new GeneralPath(); // Общий путь для поля квадратов
        squares.moveTo(xPoints[0], yPoints[0]);
        for (int i = 1; i < xPoints.length; i++) {
            squares.lineTo(xPoints[i], yPoints[i]);
        }
        squares.closePath();

        GradientPaint gradient2 = new GradientPaint(
                972, getHeight(), new Color(0, 129, 93), // Начальный цвет
                972, 500, new Color(0, 47, 34) // Конечный цвет
        );
        g.setPaint(gradient2);; // цвет для контура квадратов
        g.setStroke(new BasicStroke(2)); // Толщина контура
        g.draw(squares); // Рисуем контур квадратов

        GradientPaint gradient = new GradientPaint(
                950, getHeight() - 300, new Color(0x000000), // Начальный цвет
                950, getHeight() + 1, new Color(0x140033) // Конечный цвет
        );
        Graphics2D g3 = (Graphics2D) g;
        g3.setPaint(gradient);
        g3.fill(squares);

        ((Graphics2D) g).clip(squares);
        // Рисуем горизонтальные полосы для квадратов
        g.setPaint(gradient2); // Цвет полос
        int stripeHeight1 = 2; // Высота полос
        int stripeWidth1 = 1920; // Ширина полос
        int numStripes1 = 30; // Количество полос
        for (int i = 0; i < numStripes1; i++) {
            int gap1 = 80 - i * 5;
            int stripeX = 0;
            int stripeY = 1080 - i * (stripeHeight1 + gap1);
            g.fillRect(stripeX, stripeY, stripeWidth1, stripeHeight1);
        }
        // Рисуем вертикальные полосы для квадратов
        g.setPaint(gradient2); // Цвет полос
        int stripeHeight2 = 1080; // Высота полос
        int stripeWidth2 = 2; // Ширина полос
        int numStripes2 = 500; // Количество полос
        int gap2 = 100;
        for (int i = 0; i < numStripes2; i++) {
            int stripeX = 1920 / 50 + i * (stripeWidth2 + gap2);
            int stripeY = 0;
            g.fillRect(stripeX, stripeY, stripeWidth2, stripeHeight2);
        }
    }
    // Метод для рисования звёзд на фоне
    private void drawStars(Graphics2D g) {
        g.setColor(Color.white); // Цвет звёзд
        int numStars = 300; // Количество звёзд

        for (int i = 0; i < numStars; i++) {
            int x = random.nextInt(getWidth()); // Случайная X-координата
            int y = random.nextInt(getHeight());// Случайная Y-координата
            int starSize = random.nextInt(3) + 1; // Случайный размер звезды (1, 2 или 3 пикселя)

            g.fillRect(x, y, starSize, starSize); // Рисуем звезду как квадрат
        }
    }
    private boolean starsDrawn = false;
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (!starsDrawn) {
            Graphics2D g2 = (Graphics2D) g;
            drawStars(g2);
            starsDrawn = true; // Устанавливаем флаг в true после первой отрисовки
        }
    }

    private void drawRoad(Graphics2D g) {
        int[] xPoints = {800, 925, 975, 1100};
        int[] yPoints = {getHeight(), getHeight() - 300, getHeight()-300, getHeight()};
        GeneralPath road = new GeneralPath(); // Общий путь для дороги
        road.moveTo(xPoints[0], yPoints[0]);
        for (int i = 1; i < xPoints.length; i++) {
            road.lineTo(xPoints[i], yPoints[i]);
        }
        road.closePath();

        GradientPaint gradient2 = new GradientPaint(
                972, getHeight(), new Color(0, 129, 93), // Начальный цвет
                972, 500, new Color(0, 47, 34) // Конечный цвет
        );
        g.setPaint(gradient2); // цвет для контура дорогм
        g.setStroke(new BasicStroke(5)); // Толщина контура
        g.draw(road); // Рисуем контур дороги

        GradientPaint gradient1 = new GradientPaint(
                972, getHeight(), new Color(0x111111), // Начальный цвет
                972, getHeight() -300, new Color(0x000000) // Конечный цвет
        );
        Graphics2D g3 = (Graphics2D) g;
        g3.setPaint(gradient1);
        g3.fill(road);

        g.setPaint(gradient2);
        g.fillRect(935, getHeight()-50, 25, 100);
        int[] xPoints1 = {937, 943, 952, 958};
        int[] yPoints1 = {getHeight()-80, getHeight()-135, getHeight()-135, getHeight()-80};
        int numPoints = 4;
        g.fillPolygon(xPoints1, yPoints1, numPoints);
        int[] xPoints2 = {944, 946, 949, 951};
        int[] yPoints2 = {getHeight()-160, getHeight()-200, getHeight()-200, getHeight()-160};
        g.fillPolygon(xPoints2, yPoints2, numPoints);
        g.fillRect(948, getHeight()-300, 1, 5);
        g.fillRect(948, getHeight()-297, 1, 8);
        g.fillRect(948, getHeight()-280, 1, 11);
        g.fillRect(948, getHeight()-255, 1, 14);
        g.fillRect(947, getHeight()-232, 2, 18);
    }
}

public class Main {
    public static void main(String[] args) {
        Window window = new Window(1920, 1080);
    }
}