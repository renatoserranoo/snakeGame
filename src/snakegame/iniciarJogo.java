package snakegame;

import javax.swing.*;

public class iniciarJogo extends JFrame {
    public static void main(String[] args) {
        new iniciarJogo();
    }

    iniciarJogo(){
        add(new telaJogo());
        setTitle("Snake Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }
}
