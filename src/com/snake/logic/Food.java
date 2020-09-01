package com.snake.logic;

import java.util.ArrayList;
import java.util.Random;

public class Food {
    int x = 0, y = 0;

    final Map map;

    public Food(Map map) {
        this.map = map;
    }

    private final Random random = new Random();
    // add random food
    public void add() {
        var emptyNodes = new ArrayList<Point>();
        for (var x = 0; x < map.width; ++x) {
            for (var y = 0; y < map.height; ++y) {
                if (!map.collision(x, y)) {
                    emptyNodes.add(new Point(x, y));
                }
            }
        }
        if (!emptyNodes.isEmpty()) {
            var p = emptyNodes.get(random.nextInt(emptyNodes.size()));
            this.x = p.x;
            this.y = p.y;
        }
    }
}
