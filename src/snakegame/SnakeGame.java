/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package snakegame;

import environment.ApplicationStarter;

/**
 *
 * @author Leo
 */
public class SnakeGame {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         start();
    }

    private static void start() {
        ApplicationStarter.run("Snake Game", new SnakeEnvironment());
    }
}
