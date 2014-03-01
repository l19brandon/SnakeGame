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
import timer.TimerEventManager;
import timer.TimerNotification;

/**
 *
 * @author Leo
 */
class SnakeEnvironment extends Environment implements TimerNotification {

    private Grid grid1;
    private Snake snake;
    private int score = 0;
    private final int clockDelay = 20;
    private int clockCounter = clockDelay;
    private int clockTimer = 15;
    private int moveDelay = 4;
    private int moveCounter = moveDelay;
    private int level2 = 150;
    private int level3 = 250;
    private int level4 = 350;
    private int level5 = 450;
    private int totalTime = 0;
    private int pointsRemaining = 0;
    private ArrayList<Point> addPoints;
    private ArrayList<Point> timePenalty;
    private ArrayList<Point> addTime;
    private ArrayList<Point> speedBoost;
    private ArrayList<Point> removeSquares;
    private ArrayList<Point> wall;
    private ArrayList<Point> deleteTail;
    private ArrayList<Point> doublePoints;
    private GameState gameState = GameState.START;
    private Image timePenaltyPic;
    private Image addTimePic;
    private Image speedBoostPic;
    private Image removeSquaresPic;
    private Image deleteTailPic;
    private Image doublePointsPic;
    private Point Portal1 = new Point(10, 10);
    private Point Portal2 = new Point(49, 20);
    private boolean Portal1Disappear = false;
    private boolean Portal2Disappear = false;
    private boolean deleteTailOn = false;
    private boolean doublePointsOn = false;
    private boolean levelChange = false;
    private boolean page2 = false;
    private TimerEventManager tem;
    private Level level = Level.ONE;

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
        tem = new TimerEventManager();

        this.setBackground(new Color(123, 196, 12, 226));

        this.timePenaltyPic = ResourceTools.loadImageFromResource("resources/mine.png");
        this.addTimePic = ResourceTools.loadImageFromResource("resources/clock.png");
        this.speedBoostPic = ResourceTools.loadImageFromResource("resources/lightning bolt.png");
        this.removeSquaresPic = ResourceTools.loadImageFromResource("resources/Bomb.png");
        this.deleteTailPic = ResourceTools.loadImageFromResource("resources/Scissors.png");
        this.doublePointsPic = ResourceTools.loadImageFromResource("resources/Star.png");

        this.grid1 = new Grid();
        this.grid1.setColor(Color.white);
        this.grid1.setColumns(58);
        this.grid1.setRows(31);
        this.grid1.setCellHeight(15);
        this.grid1.setCellWidth(15);
        this.grid1.setPosition(new Point(0, 80));

        this.addPoints = new ArrayList<Point>();
        for (int i = 0; i < 6; i++) {
            this.addPoints.add(randomPoint());
        }

        this.timePenalty = new ArrayList<Point>();
        for (int i = 0; i < 12; i++) {
            this.timePenalty.add(randomPoint());
        }

        this.addTime = new ArrayList<Point>();
        for (int i = 0; i < 12; i++) {
            this.addTime.add(randomPoint());
        }

        this.speedBoost = new ArrayList<Point>();
        for (int i = 0; i < 2; i++) {
            this.speedBoost.add(randomPoint());
        }

        this.removeSquares = new ArrayList<Point>();
        for (int i = 0; i < 10; i++) {
            this.removeSquares.add(randomPoint());
        }

        this.deleteTail = new ArrayList<Point>();
        for (int i = 0; i < 2; i++) {
            this.deleteTail.add(randomPoint());
        }

        this.doublePoints = new ArrayList<Point>();
        for (int i = 0; i < 2; i++) {
            this.doublePoints.add(randomPoint());
        }

        this.snake = new Snake();
        this.snake.getBody().add(new Point(3, 1));

        gameState = GameState.START;


        this.wall = new ArrayList<Point>();
        for (int i = 0; i < this.grid1.getColumns(); i++) {
            this.wall.add(new Point(i, 0));
        }
        for (int i = 0; i < this.grid1.getColumns(); i++) {
            this.wall.add(new Point(i, this.grid1.getRows()));
        }
        for (int i = 0; i < this.grid1.getRows(); i++) {
            this.wall.add(new Point(0, i));
        }
        for (int i = 0; i <= this.grid1.getRows(); i++) {
            this.wall.add(new Point(this.grid1.getColumns(), i));
        }
        for (int i = 3; i < this.grid1.getRows() - 2; i++) {
            this.wall.add(new Point(17, i));
        }
        for (int i = 3; i < this.grid1.getRows() - 2; i++) {
            this.wall.add(new Point(40, i));
        }
    }

    @Override
    public void timerTaskHandler() {
        if (this.gameState == GameState.RUNNING) {
            if (snake != null) {
                if (clockCounter == 0) {
                    this.clockTimer--;
                    this.clockCounter = clockDelay;
                    this.totalTime++;
                    if (this.clockTimer <= 0) {
                        this.clockTimer = 0;
                        this.gameState = GameState.ENDED;
                    } else if (this.score >= 500) {
                        gameState = GameState.WIN;
                    }
                } else {
                    this.clockCounter--;
                }

                if (moveCounter <= 0) {
                    snake.move();
                    moveCounter = moveDelay;
                    checkSnakeIntersection();
                    levelChecker();
                    System.out.println("speed = " + this.moveDelay);
                    if (this.doublePointsOn) {
                        score += 2;
                    } else {
                        score++;
                    }
                } else {
                    moveCounter--;
                }
            }
        }
        
        
         if (gameState == GameState.RESTART) {
             
 
            this.addPoints = new ArrayList<Point>();
            for (int i = 0; i < 6; i++) {
                this.addPoints.add(randomPoint());
            }

            this.timePenalty = new ArrayList<Point>();
            for (int i = 0; i < 12; i++) {
                this.timePenalty.add(randomPoint());
            }

            this.addTime = new ArrayList<Point>();
            for (int i = 0; i < 12; i++) {
                this.addTime.add(randomPoint());
            }

            this.speedBoost = new ArrayList<Point>();
            for (int i = 0; i < 2; i++) {
                this.speedBoost.add(randomPoint());
            }

            this.removeSquares = new ArrayList<Point>();
            for (int i = 0; i < 10; i++) {
                this.removeSquares.add(randomPoint());
            }

            this.deleteTail = new ArrayList<Point>();
            for (int i = 0; i < 2; i++) {
                this.deleteTail.add(randomPoint());
            }

            this.doublePoints = new ArrayList<Point>();
            for (int i = 0; i < 2; i++) {
                this.doublePoints.add(randomPoint());
            }

            this.snake = new Snake();
            this.snake.getBody().add(new Point(3, 1));
            
            gameState = GameState.START;
        }
    }

    private void levelChecker() {
        if (level == Level.ONE && this.score > level2) {
            this.levelChange = true;
        } else if (level == Level.TWO && this.score > level3) {
            this.levelChange = true;
        } else if (level == Level.THREE && this.score > level4) {
            this.levelChange = true;
        } else if (level == Level.FOUR && this.score > level5) {
            this.levelChange = true;
        }

        if (this.levelChange) {
            if (level == Level.ONE) {
                System.out.println("level 1");
            }
            if (level == Level.ONE) {

                this.moveDelay -= 1;
                level = Level.TWO;
                this.levelChange = false;
                System.out.println("level 2");
            } else if (level == Level.TWO) {
                this.moveDelay -= 1;
                level = Level.THREE;
                System.out.println("level 3");
                this.levelChange = false;
            } else if (level == Level.THREE) {
                this.moveDelay -= 1;
                level = Level.FOUR;
                System.out.println("level 4");
                this.levelChange = false;
            } else if (level == Level.FOUR) {
                if (this.moveDelay >= 1) {
                    this.moveDelay -= 1;
                    level = Level.FIVE;
                    this.levelChange = false;
                    System.out.println(this.moveDelay);
                    System.out.println("max Level");
                }
            }
        }

        if (this.score <= level2) {
            this.pointsRemaining = (this.level2 - this.score);
        } else if (this.score <= level3) {
            this.pointsRemaining = (this.level3 - this.score);
        } else if (this.score <= level4) {
            this.pointsRemaining = (this.level4 - this.score);
        } else if (this.score <= level5) {
            this.pointsRemaining = (this.level5 - this.score);
        } else {
        }


    }

    private void checkSnakeIntersection() {
        if (snake.getHead().equals(this.Portal2)) {
            snake.getHead().x = this.Portal1.x;
            snake.getHead().y = this.Portal1.y;

            this.Portal1Disappear = true;
            this.Portal2Disappear = true;

            System.out.println("portal 1");

            Portal1 = new Point(0, 0);
            Portal2 = new Point(0, 0);
        }

        if (snake.getHead().equals(this.Portal1)) {

            snake.getHead().x = this.Portal2.x;
            snake.getHead().y = this.Portal2.y;

            this.Portal1Disappear = true;
            this.Portal2Disappear = true;

            System.out.println("portal 2");

            Portal1 = new Point(0, 0);
            Portal2 = new Point(0, 0);
        }



        for (int i = 0; i < this.addPoints.size(); i++) {
            if (snake.getHead().equals(this.addPoints.get(i))) {
                System.out.println("apples");
                this.score += 25;
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
                if (this.moveDelay == 4) {
                    this.moveDelay -= 4;
                    createTimerEvent("Speed Boost End 4", 3000);
                } else if (this.moveDelay == 3) {
                    this.moveDelay -= 0;
                    createTimerEvent("Speed Boost End 3", 3000);
                } else if (this.moveDelay == 2) {
                    this.moveDelay -= 2;
                    createTimerEvent("Speed Boost End 2", 3000);
                } else if (this.moveDelay == 1) {
                    this.moveDelay -= 1;
                    createTimerEvent("Speed Boost End 1", 3000);
                } else if (this.moveDelay == 0) {
                }
            }
        }

        for (int i = 0; i < this.deleteTail.size(); i++) {
            if (snake.getHead().equals(this.deleteTail.get(i))) {
                deleteTail.get(i).move(this.randomX(), this.randomY());
                System.out.println("don't delete");
                createTimerEvent("Delete Tail End", 5000);
                this.deleteTailOn = true;
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
                gameState = GameState.ENDED;
            }
        }

        if (this.deleteTailOn == true) {
            this.snake.getBody().get(1).move(this.snake.getBody().get(2).x, this.snake.getBody().get(2).y);
        }

        for (int i = 0; i < this.doublePoints.size(); i++) {
            if (snake.getHead().equals(this.doublePoints.get(i))) {
                doublePoints.get(i).move(this.randomX(), this.randomY());
                System.out.println("double points");
                this.doublePointsOn = true;
                createTimerEvent("Double Points End", 5000);
            }
        }

        for (int i = 0; i < this.wall.size(); i++) {
            if (snake.getHead().equals(this.wall.get(i))) {
                gameState = GameState.ENDED;
            }
        }


    }

    @Override
    public void keyPressedHandler(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (this.gameState == GameState.START) {
                this.gameState = GameState.RUNNING;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            this.moveCounter = 0;
            if (this.gameState == GameState.RUNNING) {
                this.gameState = GameState.PAUSED;
            } else if (this.gameState == GameState.PAUSED) {
                this.gameState = GameState.RUNNING;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_I) {
            if (this.gameState == GameState.START) {
                this.gameState = GameState.INSTRUCTIONS;
            } else if (this.gameState == GameState.INSTRUCTIONS) {
                this.gameState = GameState.START;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_3) {
            gameState = GameState.ENDED;
        }

        if (e.getKeyCode() == KeyEvent.VK_1) {
            gameState = GameState.WIN;
        } else if (e.getKeyCode() == KeyEvent.VK_2) {
            gameState = GameState.RESTART;
        }

        if (gameState == GameState.INSTRUCTIONS) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                if (this.page2) {
                    this.page2 = false;
                } else {
                    this.page2 = true;
                }
            }
        }

        if (gameState == GameState.RUNNING) {
            if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
                if (snake.getDirection() != Direction.DOWN) {
                    snake.setDirection(Direction.UP);
                }
            } else if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
                if (snake.getDirection() != Direction.UP) {
                    snake.setDirection(Direction.DOWN);
                }
            } else if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                if (snake.getDirection() != Direction.LEFT) {
                    snake.setDirection(Direction.RIGHT);
                }
            } else if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
                if (snake.getDirection() != Direction.RIGHT) {
                    snake.setDirection(Direction.LEFT);
                }
                snake.setDirection(Direction.LEFT);
            }
        } else if (gameState == GameState.ENDED || gameState == GameState.WIN) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
               gameState = GameState.RESTART; 
            }
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
        Point wallLocation;
        int paintLevel = 0;
        graphics.setColor(Color.red);

        if (level == level.ONE) {
            paintLevel = 1;
        } else if (level == level.TWO) {
            paintLevel = 2;
        } else if (level == level.THREE) {
            paintLevel = 3;
        } else if (level == level.FOUR) {
            paintLevel = 4;
        } else if (level == level.FIVE) {
            paintLevel = 5;
        }

        if (this.grid1 != null) {
//            this.grid1.paintComponent(graphics);

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
                if (this.moveDelay >= 1) {
                    for (int i = 0; i < this.speedBoost.size(); i++) {
                        graphics.drawImage(speedBoostPic, this.grid1.getCellPosition(this.speedBoost.get(i)).x, this.grid1.getCellPosition(this.speedBoost.get(i)).y, this.grid1.getCellSize().x, this.grid1.getCellSize().y, this);
                    }
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

                if (this.doublePoints != null) {
                    for (int i = 0; i < this.doublePoints.size(); i++) {
                        graphics.drawImage(doublePointsPic, this.grid1.getCellPosition(this.doublePoints.get(i)).x, this.grid1.getCellPosition(this.doublePoints.get(i)).y, this.grid1.getCellSize().x, this.grid1.getCellSize().y, this);
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

                if (Portal1Disappear == false) {
                    GraphicsPalette.enterPortal(graphics, this.grid1.getCellPosition(Portal1), this.grid1.getCellSize(), Color.yellow);
                }

                if (Portal2Disappear == false) {
                    GraphicsPalette.enterPortal(graphics, this.grid1.getCellPosition(Portal2), this.grid1.getCellSize(), Color.yellow);
                }
            }

            if (this.wall != null) {
                for (int i = 0; i < wall.size(); i++) {
                    wallLocation = grid1.getCellPosition(this.wall.get(i));
                    graphics.setColor(Color.RED);
                    graphics.fillRect(wallLocation.x, wallLocation.y, grid1.getCellWidth(), grid1.getCellHeight());
                }
            }

            if (gameState == GameState.ENDED) {
                graphics.setColor(new Color(250, 50, 50, 100));
                graphics.fillRect(125, 200, 575, 180);
                graphics.setColor(Color.GREEN);
                graphics.setFont(new Font("Calibri", Font.BOLD, 100));
                graphics.drawString("GAME OVER", 150, 300);
                graphics.setColor(Color.WHITE);
                graphics.setFont(new Font("Calibri", Font.BOLD, 40));
                graphics.drawString("Score: " + this.score, 340, 330);
                graphics.drawString("Level: " + paintLevel, 340, 365);
                graphics.setColor(Color.black);
                graphics.fillRect(279, 381, 311, 23);
                graphics.setColor(Color.white);
                graphics.setFont(new Font("OCRAStd", Font.BOLD, 20));
                graphics.drawString("Press Space to Restart", 280, 400);
            } else if (gameState == GameState.RUNNING) {
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
                graphics.setColor(Color.WHITE);
                graphics.setFont(new Font("Calibri", Font.BOLD, 30));
                graphics.drawString("Level: " + paintLevel, 570, 40);
                graphics.drawString("Points To Next Level: " + this.pointsRemaining, 570, 68);
            } else if (this.gameState == GameState.PAUSED) {
                graphics.setColor(new Color(250, 50, 50, 100));
                graphics.fillRect(125, 200, 575, 180);
                graphics.setColor(Color.BLACK);
                graphics.setFont(new Font("Calibri", Font.BOLD, 130));
                graphics.drawString("PAUSED", 200, 300);
                graphics.setColor(Color.WHITE);
                graphics.setFont(new Font("Calibri", Font.BOLD, 40));
                graphics.drawString("Press Space To Continue", 220, 355);
            } else if (this.gameState == GameState.START) {
                this.page2 = false;
                graphics.setColor(new Color(255, 255, 0, 200));
                graphics.fillRect(100, 100, 670, 320);
                graphics.setFont(new Font("Stencil Std", Font.BOLD, 150));
                graphics.setColor(Color.WHITE);
                graphics.drawString("Void", 240, 250);
                graphics.setFont(new Font("OCRAStd", Font.BOLD, 20));
                graphics.setColor(Color.white);
                graphics.fillRect(110, 330, 347, 23);
                graphics.fillRect(465, 330, 290, 23);
                graphics.setColor(Color.BLACK);
                graphics.drawString("Press i for Instructions", 120, 350);
                graphics.drawString("Press ENTER to start", 470, 350);
            } else if (this.gameState == GameState.INSTRUCTIONS) {
                graphics.setColor(new Color(255, 255, 0, 200));
                graphics.fillRect(100, 100, 670, 320);
                graphics.setColor(Color.BLACK);
                graphics.setFont(new Font("OCRAStd", Font.BOLD, 30));
                graphics.drawString("Instructions", 300, 140);
                graphics.setColor(Color.black);
                graphics.fillRect(315, 381, 248, 23);
                graphics.setColor(Color.white);
                graphics.setFont(new Font("OCRAStd", Font.BOLD, 20));
                graphics.drawString("Press i to Return", 320, 400);
                graphics.setColor(Color.blue);
                graphics.setFont(new Font("OCRAStd", Font.BOLD, 14));
                if (this.page2 == false) {
                    graphics.drawString("Page 1", 110, 130);
                    graphics.drawString("You are the white square in the upper right corner of your screen.", 100, 170);
                    graphics.drawString("When you begin playing, you will begin moving.", 100, 190);
                    graphics.drawString("You will use your arrow keys to control its direction.", 100, 210);
                    graphics.drawString("Once you have moved over a square, it will fall into 'the void'.", 100, 230);
                    graphics.drawString("If you attempt to cross over a square in the void, you will lose.", 100, 250);
                    graphics.drawString("If you run into a red wall or run out of time, you will lose.", 100, 270);
                    graphics.drawString("As you move around the playing field, you will accumulate points.", 100, 290);
                    graphics.drawString("More  points means a higher level which means a faster speed", 100, 310);
                    graphics.drawString("Your goal is to collect 500 points in the quickest time possible.", 100, 330);
                } else {
                    graphics.drawString("Page 2", 110, 130);
                    graphics.drawString("Available Powerups:", 100, 170);
                    graphics.drawImage(doublePointsPic, 101, 176, new Point(this.grid1.getCellSize()).x, new Point(this.grid1.getCellSize()).y, this);
                    graphics.drawString("Star: temporary double points", 120, 190);
                    GraphicsPalette.drawApple(graphics, new Point(100, 195), new Point(this.grid1.getCellSize()));
                    graphics.setColor(Color.blue);
                    graphics.drawString("Apple: score boost", 120, 210);
                    graphics.drawImage(timePenaltyPic, 101, 215, new Point(this.grid1.getCellSize()).x, new Point(this.grid1.getCellSize()).y, this);
                    graphics.drawString("Mine: breifly stop your movement", 120, 230);
                    graphics.drawImage(addTimePic, 101, 236, new Point(this.grid1.getCellSize()).x, new Point(this.grid1.getCellSize()).y, this);
                    graphics.drawString("Clock: extra time", 120, 250);
                    graphics.drawImage(speedBoostPic, 101, 256, new Point(this.grid1.getCellSize()).x, new Point(this.grid1.getCellSize()).y, this);
                    graphics.drawString("Lightning: temporary speed boost", 120, 270);
                    graphics.drawImage(deleteTailPic, 101, 276, new Point(this.grid1.getCellSize()).x, new Point(this.grid1.getCellSize()).y, this);
                    graphics.drawString("Scissors: temporarily stop falling into the void", 120, 290);
                    graphics.drawImage(removeSquaresPic, 101, 296, new Point(this.grid1.getCellSize()).x, new Point(this.grid1.getCellSize()).y, this);
                    graphics.drawString("Bomb: random squares will drop into the void", 120, 310);
                    GraphicsPalette.enterPortal(graphics, new Point(100, 316), this.grid1.getCellSize(), Color.yellow);
                    graphics.setColor(Color.blue);
                    graphics.drawString("Portal: transports you to the location of the other portal", 120, 330);
                }
                graphics.setColor(Color.BLACK);
                graphics.setFont(new Font("OCRAStd", Font.BOLD, 17));
                graphics.drawString("Press SPACE to Change Page", 280, 360);
            } else if (gameState == GameState.WIN) {
                graphics.setColor(new Color(169, 252, 53, 200));
                graphics.fillRect(100, 100, 670, 320);
                graphics.setColor(Color.BLACK);
                graphics.setFont(new Font("OCRAStd", Font.BOLD, 100));
                graphics.drawString("You Win!", 150, 270);
                graphics.setColor(Color.WHITE);
                graphics.setFont(new Font("OCRAStd", Font.BOLD, 60));
                graphics.drawString("Time: " + this.totalTime, 280, 355);
                graphics.setColor(Color.black);
                graphics.fillRect(279, 381, 311, 23);
                graphics.setColor(Color.white);
                graphics.setFont(new Font("OCRAStd", Font.BOLD, 20));
                graphics.drawString("Press Space to Restart", 280, 400);
            }
        }
    }  //<editor-fold defaultstate="collapsed" desc="TimerNotification Interface">

    @Override
    public void TimerEvent(String eventType) {
        System.out.println("Timer Event: " + eventType);
        if (eventType == "Speed Boost End 4") {
            this.moveDelay += 4;
            System.out.println("speed boost end");
        } else if (eventType == "Speed Boost End 3") {
            this.moveDelay += 3;
            System.out.println("speed boost end");
        } else if (eventType == "Speed Boost End 2") {
            this.moveDelay += 2;
            System.out.println("speed boost end");
        } else if (eventType == "Speed Boost End 1") {
            this.moveDelay += 1;
            System.out.println("speed boost end");
        } else if (eventType != "Delete Tail End") {
        } else {
            this.deleteTailOn = false;
            System.out.println("delete tail end");
        }

        if (eventType == "Double Points End") {
            this.doublePointsOn = false;
        }
    }

    private void createTimerEvent(String eventType, long milliseconds) {
        tem.registerTimerEvent(this, eventType, milliseconds);
    }
    //</editor-fold>
}
