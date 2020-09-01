package com.snake.logic;

import java.util.LinkedList;

public class Snake {
    public LinkedList<Point> body = new LinkedList<>();
    Point head = new Point(0, 0);

    final Map map;
    final Food food;

    public Snake(Map map, Food food) {
        this.map = map;
        this.food = food;
    }

    public void removeTail() {
        var p = this.body.removeFirst();
        map.setSnake(p.x, p.y, 0);
    }

    public void addHead(int x, int y) {
        this.head.x = x;
        this.head.y = y;
        this.body.addLast(new Point(x, y));
        map.setSnake(x, y, 1);
    }

    public String move(int dir) {
        var next = map.getNext(this.head.x, this.head.y, dir);
        this.addHead(next.x, next.y);
        if (next.x == food.x && next.y == food.y) {
            food.add();
        } else {
            this.removeTail();
        }

        if (dir == 1) return "Left";
        if (dir == 2) return "Up";
        if (dir == 3) return "Right";
        if (dir == 4) return "Down";
        else return "None";
    }

    // snake IA
    public int nextDirection() {
        int x = this.head.x;
        int y = this.head.y;
        int pathNumber = map.tour(x, y);
        int distanceToFood = map.distance(pathNumber, map.tour(food.x, food.y));
        int distanceToTail = map.distance(pathNumber, map.tour(this.body.getFirst().x, this.body.getFirst().y)); // doubt
        int cuttingAmountAvailable = distanceToTail - 4;
        int numEmptySquaresOnBoard = map.size - body.size() - 1;
        if (distanceToFood < distanceToTail) cuttingAmountAvailable -= 1;
        if (distanceToFood < cuttingAmountAvailable) cuttingAmountAvailable = distanceToFood;
        if (cuttingAmountAvailable < 0) cuttingAmountAvailable = 0;
        boolean canGoRight = !map.collision(x + 1, y);
        boolean canGoLeft = !map.collision(x - 1, y);
        boolean canGoDown = !map.collision(x, y + 1);
        boolean canGoUp = !map.collision(x, y - 1);
        int bestDir = -1;
        int bestDist = -1;
        int dist = 0;
        if (canGoRight) {
            dist = map.distance(pathNumber, map.tour(x + 1, y));
            if (dist <= cuttingAmountAvailable && dist > bestDist) {
                bestDir = Map.Right;
                bestDist = dist;
            }
        }
        if (canGoLeft) {
            dist = map.distance(pathNumber, map.tour(x - 1, y));
            if (dist <= cuttingAmountAvailable && dist > bestDist) {
                bestDir = Map.Left;
                bestDist = dist;
            }
        }
        if (canGoDown) {
            dist = map.distance(pathNumber, map.tour(x, y + 1));
            if (dist <= cuttingAmountAvailable && dist > bestDist) {
                bestDir = Map.Down;
                bestDist = dist;
            }
        }
        if (canGoUp) {
            dist = map.distance(pathNumber, map.tour(x, y - 1));
            if (dist <= cuttingAmountAvailable && dist > bestDist) {
                bestDir = Map.Up;
                bestDist = dist;
            }
        }
        if (bestDist >= 0) return bestDir;
        if (canGoUp) return Map.Up;
        if (canGoLeft) return Map.Left;
        if (canGoDown) return Map.Down;
        if (canGoRight) return Map.Right;
        return Map.Right;
    }

}
