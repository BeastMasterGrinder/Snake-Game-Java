package nb.test.gamepackagev1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JPanel implements ActionListener {

    private int BOARD_WIDTH = 300;      //Board width
    private int BOARD_HEIGHT = 300;     //Board height
    private int Circle_Size = 10;       //diameter of the circle
    private int TOTAL_DOTS = 900;       //Max size of the snake
    private int RandomPosition = 29;
    private int DELAY = 140;        //Delay for the fps of the game
    private long startTime;     //For the printing of time
    private int x[] = new int[TOTAL_DOTS];
    private int y[] = new int[TOTAL_DOTS];
    private int body;
    private int food_x;     //X coordinate of the food
    private int food_y;     //Y coordinate of the food
    private boolean movingLeft = false;
    private boolean movingRight = true;
    private boolean movingUp = false;
    private boolean movingDown = false;
    private boolean inGame = true;
    private Timer timer;
    private Image dotImage;
    private Image appleImage;
    private Image headImage;
    private int Score;

    public Game() {
        initSnakeGame();
    }
    
    private void initSnakeGame() {
        addKeyListener(new Input_from_user());
        setBackground(Color.darkGray);
        setFocusable(true);

        startTime = System.currentTimeMillis();     //Recording the Starting time
        
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        loadImages();

        //Starting the game
        body = 3;

        int temp = 0;
        while (temp < body) {
            x[temp] = 50 - temp * 10;
            y[temp] = 50;
            temp++;
        }
        //Random position of the food
        int r = (int) (Math.random() * RandomPosition);
        food_x = ((r * Circle_Size));

        r = (int) (Math.random() * RandomPosition);
        food_y = ((r * Circle_Size));
        //Start the timer
        timer = new Timer(DELAY, this);
        timer.start();
    }

    

    private void loadImages() { //Loading the images
        ImageIcon dotIcon = new ImageIcon("src/dot.png");
        dotImage = dotIcon.getImage();

        ImageIcon appleIcon = new ImageIcon("src/apple.png");
        appleImage = appleIcon.getImage();

        ImageIcon headIcon = new ImageIcon("src/head.png");
        headImage = headIcon.getImage();
    }

    

    @Override
    public void paintComponent(Graphics obj) {
        super.paintComponent(obj);
        
        Draw_components(obj);
//        drawScore(obj);
        drawScore(obj);
        displayTimer(obj);
    }
    
    private void Draw_components(Graphics obj) {
        if (inGame) {
            obj.drawImage(appleImage, food_x, food_y, this);

            int z = 0;
            while (z < body) {
                if (z == 0) {
                    obj.drawImage(headImage, x[z], y[z], this);
                } else {
                    obj.drawImage(dotImage, x[z], y[z], this);
                }
                z++;
            }

            Toolkit.getDefaultToolkit().sync();
        } else {
            end_game_screen(obj);
        }
        
    }

    private void drawScore(Graphics obj){
        String scoreText = "Score: " + Integer.toString(Score);
        Font smallFont = new Font("Helvetica", Font.BOLD, 14);
        
        
        obj.setColor(Color.white);
        obj.setFont(smallFont);
        obj.drawString(scoreText, 10, 20);
    }
    private void displayTimer(Graphics obj) {
        //How much time has passed
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000; // Convert to seconds

        // Display the elapsed time
        obj.setColor(Color.white);
        obj.setFont(new Font("Helvetica", Font.BOLD, 14));
        obj.drawString("Time: " + elapsedTime + " secs", BOARD_WIDTH - 120, 20);
    }
    
    private void end_game_screen(Graphics obj) {
        String msg = "Game Over";
        String scoreText = "Score: " + Integer.toString(Score);
        Font smallFont = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metrics = getFontMetrics(smallFont);

        obj.setColor(Color.white);
        obj.setFont(smallFont);
        obj.drawString(msg, (BOARD_WIDTH - metrics.stringWidth(msg)) / 2, BOARD_HEIGHT / 2);
        obj.drawString(scoreText, (BOARD_WIDTH - metrics.stringWidth(scoreText)) / 2, BOARD_HEIGHT / 2 + 50);


    }

    private void move_snake() {
        int coordinate = body;
        while (coordinate > 0) {
            x[coordinate] = x[(coordinate - 1)];
            y[coordinate] = y[(coordinate - 1)];
            coordinate--;
        }
            if (movingLeft) {
            x[0] -= Circle_Size;
        }

        if (movingRight) {
            x[0] += Circle_Size;
        }

        if (movingUp) {
            y[0] -= Circle_Size;
        }

        if (movingDown) {
            y[0] += Circle_Size;
        }
    }

private void Collision_with_boundries() {
    int temp = body;
    while (temp > 0) {
        if ((temp > 4) && (x[0] == x[temp]) && (y[0] == y[temp])) {
            inGame = false;
        }
        temp--;
    }

    if (y[0] >= BOARD_HEIGHT) {
        inGame = false;
    }

    if (y[0] < 0) {
        inGame = false;
    }

    if (x[0] >= BOARD_WIDTH) {
        inGame = false;
    }

    if (x[0] < 0) {
        inGame = false;
    }
    
    if (!inGame) {
        timer.stop();
    }
}

@Override
public void actionPerformed(ActionEvent e) {
    if (inGame) {
        //checking Collision with food
        if ((x[0] == food_x) && (y[0] == food_y)) {
            body++;
            Score+=10;
            int r = (int) (Math.random() * RandomPosition);
            food_x = ((r * Circle_Size));

            r = (int) (Math.random() * RandomPosition);
            food_y = ((r * Circle_Size));
            //Decrease the delay after eating an apple The game speeds up
            int delay = DELAY - 10;
            timer.setDelay(delay);
        }
        Collision_with_boundries();
        move_snake();
    }
    repaint();
}

private class Input_from_user extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
            int input = e.getKeyCode();

            if ((input == KeyEvent.VK_LEFT) && (!movingRight)) {
                movingLeft = true;
                movingUp = false;
                movingDown = false;
            }
            else if ((input == KeyEvent.VK_RIGHT) && (!movingLeft)) {
                movingRight = true;
                movingUp = false;
                movingDown = false;
            }
            else if ((input == KeyEvent.VK_UP) && (!movingDown)) {
                movingUp = true;
                movingRight = false;
                movingLeft = false;
            }
            else if ((input == KeyEvent.VK_DOWN) && (!movingUp)) {
                movingDown = true;
                movingRight = false;
                movingLeft = false;
            }
        }
    }
}

