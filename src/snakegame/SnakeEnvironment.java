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
    private ArrayList<Point> deleteTail;
    private GameState gameState = GameState.PAUSED;
    private Image timePenaltyPic;
    private Image addTimePic;
    private Image speedBoostPic;
    private Image removeSquaresPic;
    private Image deleteTailPic;
    private Point inPortal = new Point(10, 10);
    private Point outPortal = new Point(50, 20);
    private boolean Portal1Disappear = false;
    private boolean Portal2Disappear = false;
    private boolean deleteTailOn = false;

    public Point randomPoint() {

        return new Point((int) (Math.random() * this.grid1.getColumns()), (int) (Math.random() * this.grid1.getRows()));

    }

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
        this.deleteTailPic = ResourceTools.loadImageFromResource("resources/Scissors.png");


//        this.grid2 = new Grid();
//        this.grid2.setColor(Color.black);
//        this.grid2.setRows(20);
//        this.grid2.setColumns(20);
//        this.grid2.setCellHeight(15);
//        this.grid2.setCellWidth(15);
//        this.grid2.setPosition(new Point(360, 150));


        this.grid1 = new Grid();
        this.grid1.setColor(Color.white);
        this.grid1.setColumns(59);
        this.grid1.setRows(32);
        this.grid1.setCellHeight(15);
        this.grid1.setCellWidth(15);
        this.grid1.setPosition(new Point(0, 80));



        this.addPoints = new ArrayList<Point>();
        this.addPoints.add(randomPoint());
        this.addPoints.add(randomPoint());

        this.timePenalty = new ArrayList<Point>();
        this.timePenalty.add(randomPoint());
        this.timePenalty.add(randomPoint());

        this.addTime = new ArrayList<Point>();
        this.addTime.add(randomPoint());
        this.addTime.add(randomPoint());


        this.speedBoost = new ArrayList<Point>();
        this.speedBoost.add(randomPoint());
        this.speedBoost.add(randomPoint());

        this.removeSquares = new ArrayList<Point>();
        this.removeSquares.add(randomPoint());
        this.removeSquares.add(randomPoint());

        this.deleteTail = new ArrayList<Point>();
        this.deleteTail.add(randomPoint());
        this.deleteTail.add(randomPoint());




        this.snake = new Snake();
        this.snake.getBody().add(randomPoint());

//        this.wall = new ArrayList<Point>();
//        this.wall.add(new Point(0, 0));


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

        for (int i = 0; i < this.deleteTail.size(); i++) {
            if (snake.getHead().equals(this.deleteTail.get(i))) {
                deleteTail.get(i).move(this.randomX(), this.randomY());
                System.out.println("don't delete");
                if (this.snake.getBody().size() > 20) {
                    
                     for (int j = 2; j < 20; j++) {
                         
                 this.snake.getBody().get(j).move(randomX(), randomY());  
                 
                }
                     
                }else  {
                    
                     for (int j = 2; j < this.snake.getBody().size() - 1; j++) {
                         
                         this.snake.getBody().get(j).move(randomX(), randomY());
                         
                }
                     
                }

            }
        }

        for (int i = 0; i < this.removeSquares.size(); i++) {
            if (snake.getHead().equals(this.removeSquares.get(i))) {
                removeSquares.get(i).move(this.randomX(), this.randomY());

                for (int j = 0; j < 20; j++) {
                    this.snake.getBody().add((this.snake.getBody().size()), randomPoint());
                }

                System.out.println("random squares");
            }
        }


        for (int i = 1; i < this.snake.getBody().size(); i++) {
            if (snake.getHead().equals(this.snake.getBody().get(i).getLocation())) {
//                this.snake.getHead().move(this.randomX(), this.randomY());
//                this.moveCounter = 40;
                gameState = GameState.ENDED;
            }
        }




        if (snake.getHead().equals(this.inPortal)) {
            snake.getHead().x = this.outPortal.x;
            snake.getHead().y = this.outPortal.y;
            this.Portal1Disappear = true;
            this.Portal2Disappear = true;


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




        if (this.grid1 != null) {
//            this.grid1.paintComponent(graphics);
//            this.grid2.paintComponent(graphics);

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

                if (this.deleteTail != null) {
                    for (int i = 0; i < this.deleteTail.size(); i++) {
                        graphics.drawImage(deleteTailPic, this.grid1.getCellPosition(this.deleteTail.get(i)).x, this.grid1.getCellPosition(this.deleteTail.get(i)).y, this.grid1.getCellSize().x, this.grid1.getCellSize().y, this);
                    }

                }


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

                if (Portal1Disappear == false) {
                    GraphicsPalette.enterPortal(graphics, this.grid1.getCellPosition(inPortal), this.grid1.getCellSize(), Color.yellow);
                }

                if (Portal2Disappear == false) {
                    GraphicsPalette.leavePortal(graphics, this.grid1.getCellPosition(outPortal), this.grid1.getCellSize(), Color.yellow);

                }


            }




        }
//GraphicsPalette.drawUnicorn(graphics, new Point(50,50), new Point(200,200), Color.BLACK, environment.Direction.NORTH);
    }
}
