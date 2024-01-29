import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BreakoutGame extends JPanel implements KeyListener, ActionListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int PADDLE_WIDTH = 100;
    private static final int PADDLE_HEIGHT = 10;
    private static final int BALL_DIAMETER = 20;
    private static final int PADDLE_Y_POS = HEIGHT - 50;
    private static final int BALL_X_START = WIDTH / 2;
    private static final int BALL_Y_START = HEIGHT / 2;
    private static final int BALL_X_SPEED = 3;
    private static final int BALL_Y_SPEED = 3;
    private static final int BRICK_ROWS = 5;
    private static final int BRICK_COLUMNS = 10;
    private static final int BRICK_WIDTH = 75;
    private static final int BRICK_HEIGHT = 20;
    private static final int BRICK_START_Y = 50;
    private static final int DELAY = 8;

    private boolean gameRunning = true;
    private int paddleX = WIDTH / 2 - PADDLE_WIDTH / 2;
    private int ballX = BALL_X_START;
    private int ballY = BALL_Y_START;
    private int ballXDir = BALL_X_SPEED;
    private int ballYDir = BALL_Y_SPEED;
    private Timer timer;
    private int[][] bricks;

    public BreakoutGame() {
        addKeyListener(this);
        setFocusable(true);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        bricks = new int[BRICK_ROWS][BRICK_COLUMNS];
        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICK_COLUMNS; j++) {
                bricks[i][j] = 1;
            }
        }
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Drawing bricks
        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICK_COLUMNS; j++) {
                if (bricks[i][j] == 1) {
                    g.setColor(Color.WHITE);
                    g.fillRect(j * BRICK_WIDTH + 50, i * BRICK_HEIGHT + BRICK_START_Y, BRICK_WIDTH, BRICK_HEIGHT);
                    g.setColor(Color.BLACK);
                    g.drawRect(j * BRICK_WIDTH + 50, i * BRICK_HEIGHT + BRICK_START_Y, BRICK_WIDTH, BRICK_HEIGHT);
                }
            }
        }

        // Drawing paddle
        g.setColor(Color.GREEN);
        g.fillRect(paddleX, PADDLE_Y_POS, PADDLE_WIDTH, PADDLE_HEIGHT);

        // Drawing ball
        g.setColor(Color.YELLOW);
        g.fillOval(ballX, ballY, BALL_DIAMETER, BALL_DIAMETER);

        if (bricksBroken()) {
            gameRunning = false;
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("You Won!", WIDTH / 2 - 60, HEIGHT / 2);
            timer.stop();
        }

        if (ballY >= HEIGHT - BALL_DIAMETER) {
            gameRunning = false;
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over!", WIDTH / 2 - 80, HEIGHT / 2);
            g.drawString("Press Enter to Restart", WIDTH / 2 - 120, HEIGHT / 2 + 50);
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameRunning) {
            ballX += ballXDir;
            ballY += ballYDir;

            if (ballX <= 0 || ballX >= WIDTH - BALL_DIAMETER) {
                ballXDir = -ballXDir;
            }
            if (ballY <= 0) {
                ballYDir = -ballYDir;
            }
            if (ballY + BALL_DIAMETER >= PADDLE_Y_POS && ballX + BALL_DIAMETER >= paddleX && ballX <= paddleX + PADDLE_WIDTH) {
                ballYDir = -ballYDir;
            }
            for (int i = 0; i < BRICK_ROWS; i++) {
                for (int j = 0; j < BRICK_COLUMNS; j++) {
                    if (bricks[i][j] == 1) {
                        Rectangle brickRect = new Rectangle(j * BRICK_WIDTH + 50, i * BRICK_HEIGHT + BRICK_START_Y, BRICK_WIDTH, BRICK_HEIGHT);
                        Rectangle ballRect = new Rectangle(ballX, ballY, BALL_DIAMETER, BALL_DIAMETER);
                        if (ballRect.intersects(brickRect)) {
                            bricks[i][j] = 0;
                            ballYDir = -ballYDir;
                        }
                    }
                }
            }
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (paddleX >= WIDTH - PADDLE_WIDTH) {
                paddleX = WIDTH - PADDLE_WIDTH;
            } else {
                paddleX += 20;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (paddleX <= 0) {
                paddleX = 0;
            } else {
                paddleX -= 20;
            }
        }
        if (!gameRunning && e.getKeyCode() == KeyEvent.VK_ENTER) {
            resetGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    private boolean bricksBroken() {
        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICK_COLUMNS; j++) {
                if (bricks[i][j] == 1) {
                    return false;
                }
            }
        }
        return true;
    }

    private void resetGame() {
        paddleX = WIDTH / 2 - PADDLE_WIDTH / 2;
        ballX = BALL_X_START;
        ballY = BALL_Y_START;
        ballXDir = BALL_X_SPEED;
        ballYDir = BALL_Y_SPEED;
        gameRunning = true;
        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICK_COLUMNS; j++) {
                bricks[i][j] = 1;
            }
        }
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            BreakoutGame game = new BreakoutGame();
            frame.add(game);
            frame.setTitle("Breakout Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setResizable(false);
            frame.setVisible(true);
        });
    }
}
