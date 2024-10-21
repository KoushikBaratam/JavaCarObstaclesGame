import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class CarGame extends JPanel implements ActionListener {
    private Timer timer;
    private Car car;
    private ArrayList<Obstacle> obstacles;
    private boolean gameOver;
    private final int ROAD_WIDTH = 400;
    private final int ROAD_HEIGHT = 600;
    private final int OBSTACLE_SPEED = 5;
    private int speedCounter;
    
    public CarGame() {
        setFocusable(true);
        setPreferredSize(new Dimension(ROAD_WIDTH, ROAD_HEIGHT));
        setBackground(Color.GRAY);
        car = new Car(ROAD_WIDTH / 2 - 20, ROAD_HEIGHT - 100);
        obstacles = new ArrayList<>();
        timer = new Timer(20, this);
        timer.start();
        gameOver = false;
        speedCounter = 0;
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                car.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                car.keyReleased(e);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!gameOver) {
            drawRoadLines(g);
            car.draw(g);
            for (Obstacle obstacle : obstacles) {
                obstacle.draw(g);
            }
            drawSpeedCounter(g);
        } else {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("Game Over!", ROAD_WIDTH / 2 - 100, ROAD_HEIGHT / 2);
        }
    }

    private void drawSpeedCounter(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Speed: " + car.getSpeed() + " km/h", 10, 20);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            car.move();
            for (Obstacle obstacle : obstacles) {
                obstacle.move();
                if (obstacle.getBounds().intersects(car.getBounds())) {
                    gameOver = true;
                    timer.stop();
                }
            }
            addObstacle();
            repaint();
        }
    }

    private void addObstacle() {
        if (obstacles.isEmpty() || obstacles.get(obstacles.size() - 1).getY() > 150) {
            Random rand = new Random();
            int x = rand.nextInt(ROAD_WIDTH - 50);
            obstacles.add(new Obstacle(x, -50));
        }
    }

    private void drawRoadLines(Graphics g) {
        g.setColor(Color.WHITE);
        for (int i = 0; i < ROAD_HEIGHT; i += 50) {
            g.fillRect(ROAD_WIDTH / 2 - 5, i, 10, 30);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Car Game");
        CarGame gamePanel = new CarGame();
        frame.add(gamePanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    class Car {
        private int x, y;
        private int dx;
        private final int WIDTH = 40;
        private final int HEIGHT = 80;
        private final int SPEED = 8;

        public Car(int x, int y) {
            this.x = x;
            this.y = y;
            this.dx = 0;
        }

        public void draw(Graphics g) {
            g.setColor(Color.RED);
            // Draw the main body of the car
            g.fillRoundRect(x, y, WIDTH, HEIGHT, 20, 20);
            g.setColor(Color.BLACK);
            // Draw wheels
            g.fillOval(x + 5, y + HEIGHT - 10, 10, 10);
            g.fillOval(x + WIDTH - 15, y + HEIGHT - 10, 10, 10);
            g.fillOval(x + 5, y, 10, 10);
            g.fillOval(x + WIDTH - 15, y, 10, 10);
            // Draw windows
            g.setColor(Color.CYAN);
            g.fillRect(x + 10, y + 20, WIDTH - 20, HEIGHT / 3);
        }

        public void move() {
            if (x + dx > 0 && x + dx < ROAD_WIDTH - WIDTH) {
                x += dx;
            }
        }

        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT) {
                dx = -SPEED;
            }
            if (key == KeyEvent.VK_RIGHT) {
                dx = SPEED;
            }
            if (key == KeyEvent.VK_DOWN) {
                dx = 0; // Brake by releasing horizontal movement
            }
        }

        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
                dx = 0;
            }
        }

        public int getSpeed() {
            return Math.abs(dx * 10); // Convert speed to km/h for display
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, WIDTH, HEIGHT);
        }
    }

    class Obstacle {
        private int x, y;
        private final int WIDTH = 50;
        private final int HEIGHT = 50;

        public Obstacle(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void draw(Graphics g) {
            g.setColor(new Color(34, 139, 34)); // Forest green color for tree-like obstacle
            g.fillRect(x, y, WIDTH, HEIGHT);
            g.setColor(new Color(139, 69, 19)); // Brown color for trunk
            g.fillRect(x + WIDTH / 4, y + HEIGHT - 10, WIDTH / 2, 10);
        }

        public void move() {
            y += OBSTACLE_SPEED;
        }

        public int getY() {
            return y;
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, WIDTH, HEIGHT);
        }
    }
}