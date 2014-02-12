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
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 *
 * @author Leo
 */
class SnakeEnvironment extends Environment {

    private Grid grid1;
    private Grid grid2;
    private int score = 0;
    private Snake snake;
    private int moveDelay = 4;
    private final int clockDelay = 20;
    private int clockCounter = clockDelay;
    private int clockTimer = 15;
    private int moveCounter = moveDelay;
    private ArrayList<Point> addPoints;
    private ArrayList<Point> timePenalty;
    private ArrayList<Point> addTime;
    private ArrayList<Point> speedBoost;
    private ArrayList<Point> removeSquares;
    private ArrayList<Point> wall;
    private GameState gameState = GameState.PAUSED;
    private Image timePenaltyPic;
    private Image addTimePic;
    private Image speedBoostPic;
    private Image removeSquaresPic;
    private Point inPortal = new Point(10,10);
    private Point outPortal = new Point(5,5);

    public int randomX() {
        return (int) (Math.random() * this.grid1.getColumns());
    }

    public int randomY() {
        return (int) (Math.random() * this.grid1.getRows());
    }

    public SnakeEnvironment() {
    }

    @Override
    public void initializeEnvironment() {
        this.setBackground(new Color(123, 196, 12, 226));

        this.timePenaltyPic = ResourceTools.loadImageFromResource("resources/mine.png");
        this.addTimePic = ResourceTools.loadImageFromResource("resources/clock.png");
        this.speedBoostPic = ResourceTools.loadImageFromResource("resources/lightning bolt.png");
        this.removeSquaresPic = ResourceTools.loadImageFromResource("resources/Bomb.png");


        this.grid2 = new Grid();
        this.grid2.setColor(Color.black);
        this.grid2.setRows(20);
        this.grid2.setColumns(20);
        this.grid2.setCellHeight(15);
        this.grid2.setCellWidth(15);
        this.grid2.setPosition(new Point(360, 150));


        this.grid1 = new Grid();
        this.grid1.setColor(Color.white);
        this.grid1.setColumns(20);
        this.grid1.setRows(20);
        this.grid1.setCellHeight(15);
        this.grid1.setCellWidth(15);
        this.grid1.setPosition(new Point(30, 150));



        this.addPoints = new ArrayList<Point>();
        this.addPoints.add(new Point(this.randomX(), this.randomY()));
        this.addPoints.add(new Point(this.randomX(), this.randomY()));

        this.timePenalty = new ArrayList<Point>();
        this.timePenalty.add(new Point(this.randomX(), this.randomY()));
        this.timePenalty.add(new Point(this.randomX(), this.randomY()));

        this.addTime = new ArrayList<Point>();
        this.addTime.add(new Point(this.randomX(), this.randomY()));
        this.addTime.add(new Point(this.randomX(), this.randomY()));


        this.speedBoost = new ArrayList<Point>();
        this.speedBoost.add(new Point(this.randomX(), this.randomY()));
        this.speedBoost.add(new Point(this.randomX(), this.randomY()));

        this.removeSquares = new ArrayList<Point>();
        this.removeSquares.add(new Point(this.randomX(), this.randomY()));
        this.removeSquares.add(new Point(this.randomX(), this.randomY()));




        this.snake = new Snake();
        this.snake.getBody().add(new Point(this.randomX(), this.randomY()));

//        System.out.println((((int) (Math.random() * this.grid.getColumns())) + " , " + ((int) (Math.random() * this.grid.getRows()))));

        this.wall = new ArrayList<Point>();
        this.wall.add(new Point(0, 0));


    }

    @Override
    public void timerTaskHandler() {

        if (this.gameState == GameState.RUNNING) {

            if (snake != null) {

                if (clockCounter == 0) {
                    this.clockTimer--;
                    this.clockCounter = clockDelay;


                    if (this.clockTimer <= 0) {
                        this.clockTimer = 0;
                        this.gameState = GameState.ENDED;
                    }

                } else {
                    this.clockCounter--;

                }


                if (moveCounter <= 0) {
                    snake.move();
                    moveCounter = moveDelay;
                    this.score++;
                    checkSnakeIntersection();
                } else {
                    moveCounter--;
                }


            }
        }





//        if (this.snake.getHead().x >= (this.getWidth() / this.grid.getCellWidth())) {
//
//            this.snake.getBody().add(0, new Point(0, this.snake.getHead().y));
//
//        } else if (this.snake.getHead().y >= (this.getHeight() / this.grid.getCellHeight())) {
//
//            this.snake.getBody().add(0, new Point(this.snake.getHead().x, 0));
//
//        }
//            else if (this.snake.getHead().x <= 0) {
//
//            this.snake.getBody().add(0, new Point(this.getWidth() - this.grid.getCellWidth(), this.snake.getHead().y));
//
//        }
//        System.out.println(this.getHeight());




    }

    private void checkSnakeIntersection() {

        for (int i = 0; i < this.addPoints.size(); i++) {
            if (snake.getHead().equals(this.addPoints.get(i))) {
                System.out.println("apples");
                this.score += 200;
//                this.snake.grow(2);
                addPoints.get(i).move(this.randomX(), this.randomY());
            }
        }

        for (int i = 0; i < this.timePenalty.size(); i++) {
            if (snake.getHead().equals(this.timePenalty.get(i))) {
                System.out.println("mine");
                timePenalty.get(i).move(this.randomX(), this.randomY());
                this.moveCounter = 40;

            }
        }

        for (int i = 0; i < this.addTime.size(); i++) {
            if (snake.getHead().equals(this.addTime.get(i))) {
                System.out.println("clock");
                addTime.get(i).move(this.randomX(), this.randomY());
                this.clockTimer += 10;

            }
        }

        for (int i = 0; i < this.speedBoost.size(); i++) {
            if (snake.getHead().equals(this.speedBoost.get(i))) {
                speedBoost.get(i).move(this.randomX(), this.randomY());
                System.out.println("speed boost");


            }
        }

        for (int i = 0; i < this.removeSquares.size(); i++) {
            if (snake.getHead().equals(this.removeSquares.get(i))) {
                removeSquares.get(i).move(this.randomX(), this.randomY());

                this.snake.getBody().add((this.snake.getBody().size()), new Point(this.randomX(), this.randomY()));
                this.snake.getBody().add((this.snake.getBody().size()), new Point(this.randomX(), this.randomY()));
                this.snake.getBody().add((this.snake.getBody().size()), new Point(this.randomX(), this.randomY()));
                this.snake.getBody().add((this.snake.getBody().size()), new Point(this.randomX(), this.randomY()));
                this.snake.getBody().add((this.snake.getBody().size()), new Point(this.randomX(), this.randomY()));
                this.snake.getBody().add((this.snake.getBody().size()), new Point(this.randomX(), this.randomY()));
                this.snake.getBody().add((this.snake.getBody().size()), new Point(this.randomX(), this.randomY()));
                this.snake.getBody().add((this.snake.getBody().size()), new Point(this.randomX(), this.randomY()));
                this.snake.getBody().add((this.snake.getBody().size()), new Point(this.randomX(), this.randomY()));
                this.snake.getBody().add((this.snake.getBody().size()), new Point(this.randomX(), this.randomY()));
                this.snake.getBody().add((this.snake.getBody().size()), new Point(this.randomX(), this.randomY()));
                this.snake.getBody().add((this.snake.getBody().size()), new Point(this.randomX(), this.randomY()));
                this.snake.getBody().add((this.snake.getBody().size()), new Point(this.randomX(), this.randomY()));
                this.snake.getBody().add((this.snake.getBody().size()), new Point(this.randomX(), this.randomY()));


                System.out.println("random squares");
            }
        }


        for (int i = 1; i < this.snake.getBody().size(); i++) {
            if (snake.getHead().equals(this.snake.getBody().get(i).getLocation())) {
                this.snake.getHead().move(this.randomX(), this.randomY());
                this.moveCounter = 40;
            }
        }






    }

    @Override
    public void keyPressedHandler(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            this.moveCounter = 0;

            if (this.gameState == GameState.RUNNING) {
                this.gameState = GameState.PAUSED;
            } else if (this.gameState == GameState.PAUSED) {
                this.gameState = GameState.RUNNING;
            }

        }
        if (e.getKeyCode() == KeyEvent.VK_W) {
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


        Point headLocation;
        graphics.setColor(Color.red);

        if (snake != null) {
            
            
            for (int i = 0; i < 1; i++) {
                headLocation = grid1.getCellPosition(snake.getBody().get(i));
                graphics.setColor(Color.WHITE);
                graphics.fillRect(headLocation.x, headLocation.y, grid1.getCellWidth(), grid1.getCellHeight());
            }

            for (int i = 1; i < snake.getBody().size(); i++) {
                headLocation = grid1.getCellPosition(snake.getBody().get(i));
                graphics.setColor(Color.BLACK);
                graphics.fillRect(headLocation.x, headLocation.y, grid1.getCellWidth(), grid1.getCellHeight());
            }
        }



        if (this.grid1 != null) {
            this.grid1.paintComponent(graphics);
            this.grid2.paintComponent(graphics);

            if (this.addPoints != null) {
                for (int i = 0; i < this.addPoints.size(); i++) {
                    GraphicsPalette.drawApple(graphics, this.grid1.getCellPosition(this.addPoints.get(i)), new Point(this.grid1.getCellSize()));
                }
            }


            if (this.timePenalty != null) {
                for (int i = 0; i < this.timePenalty.size(); i++) {
                    graphics.drawImage(timePenaltyPic, this.grid1.getCellPosition(this.timePenalty.get(i)).x, this.grid1.getCellPosition(this.timePenalty.get(i)).y, this.grid1.getCellSize().x, this.grid1.getCellSize().y, this);
                }
            }

            if (this.addTime != null) {
                for (int i = 0; i < this.addTime.size(); i++) {
                    graphics.drawImage(addTimePic, this.grid1.getCellPosition(this.addTime.get(i)).x, this.grid1.getCellPosition(this.addTime.get(i)).y, this.grid1.getCellSize().x, this.grid1.getCellSize().y, this);
                }
            }

            if (this.speedBoost != null) {
                for (int i = 0; i < this.speedBoost.size(); i++) {
                    graphics.drawImage(speedBoostPic, this.grid1.getCellPosition(this.speedBoost.get(i)).x, this.grid1.getCellPosition(this.speedBoost.get(i)).y, this.grid1.getCellSize().x, this.grid1.getCellSize().y, this);
                }
            }


            if (this.removeSquares != null) {
                for (int i = 0; i < this.removeSquares.size(); i++) {
                    graphics.drawImage(removeSquaresPic, this.grid1.getCellPosition(this.removeSquares.get(i)).x, this.grid1.getCellPosition(this.removeSquares.get(i)).y, this.grid1.getCellSize().x, this.grid1.getCellSize().y, this);
                }
            }

            if (gameState == GameState.ENDED) {
                graphics.setColor(new Color(250, 50, 50, 100));
                graphics.fillRect(125, 200, 575, 180);
                graphics.setColor(Color.red);
                graphics.setFont(new Font("Calibri", Font.BOLD, 100));
                graphics.drawString("GAME OVER", 150, 300);
                graphics.setColor(Color.WHITE);
                graphics.setFont(new Font("Calibri", Font.BOLD, 50));
                graphics.drawString("Score: " + this.score, 310, 350);
            } else {
                graphics.setColor(Color.WHITE);
                graphics.setFont(new Font("Calibri", Font.BOLD, 60));
                graphics.drawString("Score:" + this.score, 10, 60);
                if (this.clockTimer >= 10) {
                    graphics.setColor(Color.white);
                } else if (this.clockTimer >= 5) {
                    graphics.setColor(Color.YELLOW);
                } else if (this.clockTimer >= 1) {
                    graphics.setColor(Color.red);
                }
                graphics.drawString("Timer:" + this.clockTimer, 300, 60);
            }

            GraphicsPalette.enterPortal(graphics, this.grid1.getCellPosition(inPortal), this.grid1.getCellSize(), Color.yellow);
            GraphicsPalette.leavePortal(graphics, this.grid2.getCellPosition(outPortal), this.grid2.getCellSize(), Color.yellow);
            

        }




    }
//GraphicsPalette.drawUnicorn(graphics, new Point(50,50), new Point(200,200), Color.BLACK, environment.Direction.NORTH);
}
