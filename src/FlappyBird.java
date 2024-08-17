import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 360;
    int boardHeight = 640;

    // Images
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;

    // Bird
    int birdX = boardWidth / 8;
    int birdY = boardHeight / 2;
    int birdWidth = 34;
    int birdHeight = 24;

    // Pipes
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64; // scaled by 1/6
    int pipeHeight = 512;

    // game logic
    Bird bird;
    int speedX = -4; // moves screen to the left
    int baseSpeedY = -10; // positive down and negative up
    int speedY = 0; // starting velocity
    int gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameLoop;
    Timer placePipesTimer;

    boolean gameOver = false;
    double score = 0;

    FlappyBird() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);

        // load images
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        // bird
        bird = new Bird(birdImg, birdX, birdY, birdWidth, birdHeight);
        pipes = new ArrayList<Pipe>();

        // place pipes timer
        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });

        // place pipes
        placePipesTimer.start();

        // game timer
        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();
    }

    public void placePipes() {
        // (0-1) * pipeheight/2 -> (0 - 256)
        // 0 - pipeheight/4 - (0-256) = 0 - 128 - (0-256)
        int randomPipeY = (int) (pipeY - pipeHeight / 4 - Math.random() * (pipeHeight / 2));
        int openingSpace = boardHeight / 6;

        Pipe topPipe = new Pipe(topPipeImg, pipeX, randomPipeY, pipeWidth, pipeHeight);
        pipes.add(topPipe); // add to array

        Pipe bottomPipe = new Pipe(bottomPipeImg, pipeX, topPipe.y + pipeHeight + openingSpace, pipeWidth, pipeHeight);
        pipes.add(bottomPipe);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);

        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.BOLD, 32));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf((int) score), 10, 35);
        } else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    public void move() {
        // bird
        speedY += gravity;
        bird.y += speedY;
        bird.y = Math.max(bird.y, 0);

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += speedX;

            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                pipe.passed = true;
                score += 0.5; // two pipes, so we split points in half for each
            }

            if (collision(bird, pipe)) {
                gameOver = true;
            }

        }

        if (bird.y > boardHeight) {
            gameOver = true;
        }

    }

    public boolean collision(Bird b, Pipe p) {
        return b.x < p.x + p.width &&
                b.x + b.width > p.x &&
                b.y < p.y + p.height &&
                b.y + b.height > p.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            placePipesTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            speedY = baseSpeedY;

            if (gameOver) { // restarts the game
                bird.y = birdY;
                speedY = 0;
                pipes.clear();
                score = 0;
                gameOver = false;
                placePipesTimer.start();
                gameLoop.start();

            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
