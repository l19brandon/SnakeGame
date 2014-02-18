/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package snakegame;

import java.awt.Point;
import java.util.ArrayList;
import static snakegame.Direction.LEFT;
import static snakegame.Direction.UP;

/**
 *
 * @author Leo
 */
public class Snake {

    private ArrayList<Point> body;
    private Direction direction = Direction.DOWN;
    private int growthCounter;

    {
        setBody(new ArrayList<Point>());
    }

//    void grow(int growth) {
//        this.growthCounter += growth;
//    }

    public void move() {
        //create a new location for the head, using the directon
        int x = 0;
        int y = 0;

        switch (getDirection()) {
            case UP:
                x = 0;
                y = -1;
                break;
            case DOWN:
                x = 0;
                y = 1;
                break;

            case RIGHT:
                x = 1;
                y = 0;
                break;

            case LEFT:
                x = - 1;
                y = 0;
                break;


        }


        getBody().add(0, new Point(getHead().x + x, getHead().y + y));
        
        
//        delete tail
//        if (growthCounter > 0) {
//            growthCounter--;
//        } else {
//            getBody().remove(getBody().size() - 1);
//        }




    }

    public Point getHead() {
        return getBody().get(0);
    }

    /**
     * @return the body
     */
    public ArrayList<Point> getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(ArrayList<Point> body) {
        this.body = body;
    }

    /**
     * @return the direction
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
