package snakegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class telaJogo extends JPanel implements ActionListener {
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final int BLOCK_SIZE = 50;
    private static final int UNITS = WIDTH * HEIGHT / (BLOCK_SIZE * BLOCK_SIZE);
    private static final int INTERVAL = 70;
    private static final String newFont = "Arial";
    private final int[] X_axis = new int[UNITS];
    private final int[] Y_axis = new int[UNITS];
    private int bodySnake = 6;
    private int eatenBlocks;
    private int XBlock;
    private int YBlock;
    private char direction = 'D'; //W - cima, S - baixo, A - esquerda, D - direita
    private boolean running = false;
    Timer timer;
    Random random;

    telaJogo(){
        random = new Random();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.DARK_GRAY);
        setFocusable(true);
        addKeyListener(new keyReader());
        runGame();
    }

    private void runGame() {
        createBlock();
        running = true;
        timer = new Timer(INTERVAL, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        drawScreen(g);
    }

    private void drawScreen(Graphics g) {
        if(running){
            g.setColor(Color.red);
            g.fillOval(XBlock, YBlock, BLOCK_SIZE, BLOCK_SIZE);

            for (int i = 0; i < bodySnake; i++){
                if (i == 0){
                    g.setColor(Color.green);
                    g.fillRect(X_axis[0], Y_axis[0], BLOCK_SIZE, BLOCK_SIZE);
                } else{
                    g.setColor(new Color(0, 180, 0));
                    g.fillRect(X_axis[i], Y_axis[i], BLOCK_SIZE, BLOCK_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font(newFont, Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Pontos: " + eatenBlocks, (WIDTH - metrics.stringWidth("Pontuação: " + eatenBlocks)), HEIGHT / 16);
        } else {
            endGame(g);
        }
    }

    private void createBlock(){
        XBlock = random.nextInt(WIDTH / BLOCK_SIZE) * BLOCK_SIZE;
        YBlock = random.nextInt(HEIGHT / BLOCK_SIZE) * BLOCK_SIZE;
    }

    private void endGame(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font(newFont, Font.BOLD, 40));
        FontMetrics pontuationFont = getFontMetrics(g.getFont());
        g.drawString("Pontos: " + eatenBlocks, (WIDTH - pontuationFont.stringWidth("Pontos: " + eatenBlocks))/ 2, HEIGHT / 2);
        g.setColor(Color.red);
        g.setFont(new Font(newFont, Font.BOLD, 40));
        FontMetrics finalFont = getFontMetrics(g.getFont());
        g.drawString("Fim de jogo", (WIDTH - finalFont.stringWidth("Fim de jogo")) / 2, HEIGHT / 3);
    }

    public void actionPerformed(ActionEvent e) {
        if(running){
            walk();
            reachBlock();
            validateLimits();
        }
        repaint();
    }

    private void walk() {
        for (int i = bodySnake; i > 0; i--){
            X_axis[i] = X_axis[i-1];
            Y_axis[i] = Y_axis[i-1];
        }

        switch (direction) {
            case 'W' -> Y_axis[0] = Y_axis[0] - BLOCK_SIZE;
            case 'S' -> Y_axis[0] = Y_axis[0] + BLOCK_SIZE;
            case 'A' -> X_axis[0] = X_axis[0] - BLOCK_SIZE;
            case 'D' -> X_axis[0] = X_axis[0] + BLOCK_SIZE;
            default -> {
            }
        }
    }

    private void reachBlock() {
        if (X_axis[0] == XBlock && Y_axis[0] == YBlock){
            bodySnake++;
            eatenBlocks++;
            createBlock();
        }
    }

    private void validateLimits(){
        //cabeça bateu no corpo?
        for (int i = bodySnake; i > 0; i--){
            if (X_axis[0] == X_axis[i] && Y_axis[0] == Y_axis[i]){
                running = false;
                break;
            }
        }

        //cabeça tocou as bordas?
        if (X_axis[0] < 0 || X_axis[0] > WIDTH){
            running = false;
        }

        //cabeca tocou piso ou teto?
        if (Y_axis[0] < 0 || Y_axis[0] > HEIGHT){
            running = false;
        }

        if (!running){
            timer.stop();
        }
    }

    public class keyReader extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_A:
                    if (direction != 'D'){
                        direction = 'A';
                    }
                    break;
                case KeyEvent.VK_D:
                    if (direction != 'A'){
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_W:
                    if (direction != 'S'){
                        direction = 'W';
                    }
                    break;
                case KeyEvent.VK_S:
                    if (direction != 'W'){
                        direction = 'S';
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if (!running){
                        restart();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void restart() {
        running = true;
        eatenBlocks = 0;
        bodySnake = 6;
        for (int i = 0; i < bodySnake; i++) {
            X_axis[i] = 0;
            Y_axis[i] = 0;
        }
        createBlock();
        direction = 'D';
        timer.start();
        repaint();
    }
}
