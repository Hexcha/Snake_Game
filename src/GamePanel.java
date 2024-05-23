import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH= 800;
    static final int SCREEN_HEIGHT= 800;
    static final int UNIT_SIZE= 40;
    static final int GAME_UNITS=(SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY= 150;
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int bodyParts= 6;
    int currentScore;
    static int highScore;
    int appleX,appleY;
    char direction = 'R';
    boolean running= false;
    Timer timer;
    Random random;
    JButton restartButton;

    GamePanel(){

        random= new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new Movement());
        startGame();

    }
    public void startGame(){

        //Making sure both body and head of the snake is painted middle of the screen
        for (int i = 0; i < bodyParts; i++) {

            x[i]=SCREEN_WIDTH/2;
            y[i]=SCREEN_HEIGHT/2;

        }

        createApple();
        running=true;
        timer= new Timer(DELAY,this);
        timer.start();

    }
    public void restartGame(){

        if(currentScore>highScore)
            highScore=currentScore;

        currentScore = 0;
        bodyParts = 6;
        direction = 'R';
        running = true;
        createApple();
        timer.start();
        Arrays.fill(x, 0); // Reset x coordinates
        Arrays.fill(y, 0); // Reset y coordinates
        x[0] = SCREEN_WIDTH / 2;
        y[0] = SCREEN_HEIGHT / 2;
        addKeyListener(new Movement());
        remove(restartButton);

        //Making sure both body and head of the snake is painted middle of the screen
        for (int i = 0; i < bodyParts; i++) {

            x[i]=SCREEN_WIDTH/2;
            y[i]=SCREEN_HEIGHT/2;

        }
        repaint();


    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);

    }
    public void  draw(Graphics g){
        if(running){
            /*I used this because I wanted to see the pixels I'm working with

            for (int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
                g.drawLine(i*UNIT_SIZE,0,i*UNIT_SIZE,SCREEN_HEIGHT);
                g.drawLine(0,i*UNIT_SIZE,SCREEN_WIDTH,i*UNIT_SIZE);
            }
            */

            //drawing the apple
            g.setColor(Color.red);
            g.fillOval(appleX,appleY,UNIT_SIZE,UNIT_SIZE);

            //drawing the snake
            for (int i = 0; i <bodyParts ; i++) {
                //the head
                if(i==0){

                    g.setColor(Color.green);
                    g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);

                }
                //the body
                else {

                    g.setColor(new Color(45,180,0));
                    g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);

                }

            }

            g.setColor(Color.yellow);
            g.setFont(new Font("Comic Sans",Font.BOLD,40));
            FontMetrics metrics= getFontMetrics(g.getFont());
            g.drawString("Score: "+ currentScore,(SCREEN_WIDTH-metrics.stringWidth("Score: "+ currentScore))/2,g.getFont().getSize());

            g.setColor(Color.yellow);
            g.setFont(new Font("Comic Sans",Font.BOLD,40));
            FontMetrics metrics3= getFontMetrics(g.getFont());
            g.drawString("High Score: "+ highScore,(SCREEN_WIDTH-metrics3.stringWidth("High Score: "+ highScore))/2,g.getFont().getSize()*2);

        }

        else {
            gameOver(g);
        }

    }
    public void createApple() {
        boolean appleOnSnake;
        do {
            appleOnSnake = false;
            appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
            appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
            for (int i = 0; i < bodyParts; i++) {
                if (appleX == x[i] && appleY == y[i]) {
                    appleOnSnake = true;
                    break;
                }
            }
        } while (appleOnSnake);
    }


    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }

        //First tried in the CheckCollisions() method but
        //it wasn't updating fast enough. Sometimes it
        //allowed the head to pass through body.
        if (x[0] < 0) {
            x[0] = SCREEN_WIDTH - UNIT_SIZE;
            checkCollisions();
        } else if (x[0] >= SCREEN_WIDTH) {
            x[0] = 0;
            checkCollisions();
        }

        if (y[0] < 0) {
            y[0] = SCREEN_HEIGHT - UNIT_SIZE;
            checkCollisions();
        } else if (y[0] >= SCREEN_HEIGHT) {
            y[0] = 0;
            checkCollisions();
        }
    }


    public void checkApple(){

        if((x[0]==appleX)&& (y[0]==appleY)){

            bodyParts++;
            currentScore++;
            createApple();
        }

    }
    public void checkCollisions() {

        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }

        if (!running) {
            timer.stop();
        }
    }


    public void gameOver(Graphics g){


        g.setColor(Color.yellow);
        g.setFont(new Font("Comic Sans",Font.BOLD,40));
        FontMetrics metrics1= getFontMetrics(g.getFont());
        g.drawString("Score: "+ currentScore,(SCREEN_WIDTH-metrics1.stringWidth("Score: "+ currentScore))/2,g.getFont().getSize());

        g.setColor(Color.yellow);
        g.setFont(new Font("Comic Sans",Font.BOLD,120));
        FontMetrics metrics2= getFontMetrics(g.getFont());
        g.drawString("Game Over",(SCREEN_WIDTH-metrics2.stringWidth("Game Over"))/2,SCREEN_HEIGHT/2);

        g.setColor(Color.yellow);
        g.setFont(new Font("Comic Sans",Font.BOLD,40));
        FontMetrics metrics3= getFontMetrics(g.getFont());
        g.drawString("High Score: "+ highScore,(SCREEN_WIDTH-metrics3.stringWidth("High Score: "+ highScore))/2,g.getFont().getSize()*2);

        restartButton= new JButton("Restart?");
        restartButton.setFont(new Font("Comic Sans",Font.BOLD,70));
        restartButton.setBackground(Color.black);
        restartButton.setForeground(Color.yellow);
        restartButton.setBorder(new EmptyBorder(0,0,0,0));
        restartButton.setBounds((SCREEN_WIDTH - 300) / 2, SCREEN_HEIGHT / 2 + 50, 300, 100);
        restartButton.addActionListener(this);
        add(restartButton);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(running){

            move();
            checkApple();
            checkCollisions();

        }
        repaint();
        if(e.getSource()==restartButton)
            restartGame();


    }
    public class Movement extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){

            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT,KeyEvent.VK_A:
                    if(direction!='R')
                        direction='L';
                    break;
                case KeyEvent.VK_RIGHT,KeyEvent.VK_D:
                    if(direction!='L')
                        direction='R';
                    break;
                case KeyEvent.VK_UP,KeyEvent.VK_W:
                    if(direction!='D')
                        direction='U';
                    break;
                case KeyEvent.VK_DOWN,KeyEvent.VK_S:
                    if(direction!='U')
                        direction='D';
                    break;
            }

        }


    }
}
