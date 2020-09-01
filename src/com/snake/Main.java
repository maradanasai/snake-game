package com.snake;

import com.snake.logic.Food;
import com.snake.logic.Map;
import com.snake.logic.Point;
import com.snake.logic.Snake;

import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Map map = new Map();
        map.init(14, 14);
        addWalls(map);
        map.generate_r(-1, -1, 0, 0);
        map.generateTourNumber();

        Food food = new Food(map);

        Snake snake = new Snake(map, food);
        snake.addHead(0, 0);
        snake.addHead(1, 0);
        snake.addHead(2, 0);
        food.add();

        while (snake.body.size() < map.size) {
            String dir = snake.move(snake.nextDirection());
            map.draw(snake);
        }
    }

    public static final Set<Point> walls = new HashSet<>();

    private static void addWalls(Map map) {
        for (int x = map.width - 1, y = 0; y < map.height; y++) {
            walls.add(new Point(x, y));
        }
    }
}
