/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package snakegame;

import environment.Environment;
import environment.GraphicsPalette;
import environment.Grid;
import image.ResourceTools;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 *
 * @author Leo
 */
class SnakeEnvironment extends Environment {

    private Grid grid;
    private int score = 0;
    private Snake snake;
    private int delay = 0;
    private int moveCounter = delay;
    
    private ArrayList<Point> apples;
    
    private ArrayList<Point> poison;

    public SnakeEnvironment() {
    }

    @Override
    public void initializeEnvironment() {
        this.setBackground(ResourceTools.loadImageFromResource("resources/ice.jpg"));


        this.grid = new Grid();
        this.grid.setColor(Color.white);
        this.grid.setColumns(70);
        this.grid.setRows(40);
        this.grid.setCellHeight(20);
        this.grid.setCellWidth(20);
        this.grid.setPosition(new Point(0, 0));
        
        this.apples = new ArrayList<Point>();
        this.apples.add(new Point(10,12));
        this.apples.add(new Point(20,15)); 
        
        this.poison = new ArrayList<Point>();
        this.poison.add(new Point(29,15));
        this.poison.add(new Point(22, 22));




        this.snake = new Snake();
        this.snake.getBody().add(new Point(5, 5));
        this.snake.getBody().add(new Point(5, 4));
        this.snake.getBody().add(new Point(5, 3));
        this.snake.getBody().add(new Point(5, 2));


    }

    @Override
    public void timerTaskHandler() {

        if (snake != null) {


            if (moveCounter <= 0) {
                snake.move();
                moveCounter = delay;
            } else {
                moveCounter--;
            }
        }




        if (this.snake.getHead().x >= (this.getWidth() / this.grid.getCellWidth())) {

            this.snake.getBody().add(0, new Point(0, this.snake.getHead().y));
            this.snake.getBody().remove(this.snake.getBody().size() - 1);

        } else if (this.snake.getHead().y >= (this.getHeight() / this.grid.getCellHeight())) {

            this.snake.getBody().add(0, new Point(this.snake.getHead().x, 0));
            this.snake.getBody().remove(this.snake.getBody().size() - 1);

        }
//            else if (this.snake.getHead().x <= 0) {
//            
//            this.snake.getBody().add(0, new Point(this.getWidth() - this.grid.getCellWidth(), this.snake.getHead().y));
//            this.snake.getBody().remove(this.snake.getBody().size() - 1);
//
//        }
//        System.out.println(this.getHeight());




    }

    @Override
    public void keyPressedHandler(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_M) {
            this.score += 1;
        } else if (e.getExtendedKeyCode() == KeyEvent.VK_SPACE) {
//            snake.move();
        } else if (e.getKeyCode() == KeyEvent.VK_W) {
            if (snake.getDirection() == Direction.DOWN) {
                return;
            }
            snake.setDirection(Direction.UP);
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            if (snake.getDirection() == Direction.UP) {
                return;
            }
            snake.setDirection(Direction.DOWN);
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            if (snake.getDirection() == Direction.LEFT) {
                return;
            }
            snake.setDirection(Direction.RIGHT);
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            if (snake.getDirection() == Direction.RIGHT) {
                return;
            }
            snake.setDirection(Direction.LEFT);
        }

    }

    @Override
    public void keyReleasedHandler(KeyEvent e) {
    }

    @Override
    public void environmentMouseClicked(MouseEvent e) {
    }

    @Override
    public void paintEnvironment(Graphics graphics) {
        if (this.grid != null) {
            this.grid.paintComponent(graphics);
            
            if(this.apples != null){
                for (int i = 0; i < this.apples.size(); i++) {
                    GraphicsPalette.drawApple(graphics, this.grid.getCellPosition(this.apples.get(i)), new Point(this.grid.getCellSize()));
                }
            }
            
            
            if(this.poison != null){
                for (int i = 0; i < this.poison.size(); i++) {
                    GraphicsPalette.drawPoisonBottle(graphics, this.grid.getCellPosition(this.poison.get(i)), new Point(this.grid.getCellSize()), Color.yellow);
                }
            }

            graphics.setColor(Color.black);
            graphics.setFont(new Font("Calibri", Font.BOLD, 60));
            graphics.drawString("Score:" + this.score, 50, 50);

            Point cellLocation;
            graphics.setColor(Color.red);

            if (snake != null) {
                for (int i = 0; i < snake.getBody().size(); i++) {

                    cellLocation = grid.getCellPosition(snake.getBody().get(i));
                    graphics.fillOval(cellLocation.x, cellLocation.y, grid.getCellWidth(), grid.getCellHeight());

                }
            }
        }
        
//GraphicsPalette.drawUnicorn(graphics, new Point(50,50), new Point(200,200), Color.BLACK, environment.Direction.NORTH);



    }
}
