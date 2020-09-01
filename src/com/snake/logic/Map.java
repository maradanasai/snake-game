package com.snake.logic;

import java.util.Random;

public class Map {

    public int width, height, size;

    int[] tour, visited, goRight, goDown, snake;

    public void init(int width, int height) {
        this.width = width;
        this.height = height;
        this.size = width * height;
        ///this.scale = Math.min(canvasWidth, canvasHeight) / Math.max(this.width, this.height);
        // Hamiltonian Cycle

        // flags
        this.tour = this.array2D(width, height);
        this.visited = this.array2D(width, height);
        this.goRight = this.array2D(width, height);
        this.goDown = this.array2D(width, height);
        this.snake = this.array2D(width, height);
    }

    public int tour(int x, int y) {
        return array2D0(tour, x, y, width);
    }

    public void setTour(int x, int y, int value) {
        var i = x + width * y;
        if (tour[i] == 0) tour[i] = value;
    }

    public boolean isVisited(int x, int y) {
        return array2D0(visited, x, y, width / 2) != 0;
    }

    public void setVisited(int x, int y, int value) {
        array2D2(visited, x, y, value, width / 2);
    }

    public boolean canGoRight(int x, int y) {
        return array2D0(goRight, x, y, width / 2) != 0;
    }

    public void setGoRight(int x, int y, int value) {
        array2D2(goRight, x, y, value, width / 2);
    }

    public boolean canGoDown(int x, int y) {
        return array2D0(goDown, x, y, width / 2) != 0;
    }

    public void setGoDown(int x, int y, int value) {
        array2D2(goDown, x, y, value, width / 2);
    }

    public boolean isSnake(int x, int y) {
        return array2D0(snake, x, y, width) != 0;
    }

    public void setSnake(int x, int y, int value) {
        array2D2(snake, x, y, value, width);
    }

    public boolean canGoLeft(int x, int y) {
        if (x == 0) return false;
        return this.canGoRight(x - 1, y); // doubt
    }

    public boolean canGoUp(int x, int y) {
        if (y == 0) return false;
        return this.canGoDown(x, y - 1);
    }

    // directions
    public static final int Left= 1;
    public static final int Up= 2;
    public static final int Right= 3;
    public static final int Down= 4;

    // flat 2D array
    private int[] array2D(int width, int height) {
        return new int[width * height];
    }

    private int array2D0(int[] data, int x, int y, int width) {
        return data[x + width * y];
    }

    private void array2D2(int[] data, int x, int y, int value, int width) {
        data[x + width * y] = value;
    }

    // test snake collision
    public boolean collision(int x, int y) {
        if (x < 0 || x >= this.width) return true;
        if (y < 0 || y >= this.height) return true;
        return this.isSnake(x, y);
    }

    // path distance
    public int distance(int a, int b) {
        if (a < b) return b - a - 1;else return b - a - 1 + this.size;
    }

    private final Random random = new Random();

    // Hamiltonian Cycle
    public void generate_r(int fromx, int fromy, int x, int y) {
        if (x < 0 || y < 0 || x >= this.width / 2 || y >= this.height / 2) return;
        if (this.isVisited(x, y)) return;
        this.setVisited(x, y, 1);
        if (fromx != -1) {
            if (fromx < x) 
                this.setGoRight(fromx, fromy, 1);
            else if (fromx > x) 
                this.setGoRight(x, y, 1);
            else if (fromy < y) 
                this.setGoDown(fromx, fromy, 1);
            else if (fromy > y) 
                this.setGoDown(x, y, 1);
        }
        for (var i = 0; i < 2; i++) {
            int r = random.ints(1, 0, 4).findFirst().orElse(0);
            switch (r) {
                case 0:
                    this.generate_r(x, y, x - 1, y);
                    break;
                case 1:
                    this.generate_r(x, y, x + 1, y);
                    break;
                case 2:
                    this.generate_r(x, y, x, y - 1);
                    break;
                case 3:
                    this.generate_r(x, y, x, y + 1);
                    break;
            }
        }
        this.generate_r(x, y, x - 1, y);
        this.generate_r(x, y, x + 1, y);
        this.generate_r(x, y, x, y + 1);
        this.generate_r(x, y, x, y - 1);
    }

    // find next direction in cycle
    public int findNextDir(int x, int y, int dir) {
        if (dir == Right) {
            if (this.canGoUp(x, y)) return Up;
            if (this.canGoRight(x, y)) return Right;
            if (this.canGoDown(x, y)) return Down;
            return Left;
        } else if (dir == Down) {
            if (this.canGoRight(x, y)) return Right;
            if (this.canGoDown(x, y)) return Down;
            if (this.canGoLeft(x, y)) return Left;
            return Up;
        } else if (dir == Left) {
            if (this.canGoDown(x, y)) return Down;
            if (this.canGoLeft(x, y)) return Left;
            if (this.canGoUp(x, y)) return Up;
            return Right;
        } else if (dir == Up) {
            if (this.canGoLeft(x, y)) return Left;
            if (this.canGoUp(x, y)) return Up;
            if (this.canGoRight(x, y)) return Right;
            return Down;
        }
        return -1; //Unreachable
    }

    // generate Hamiltonian Cycle
    public void generateTourNumber() {
        var x = 0;
        var y = 0;
        var dir = this.canGoDown(x, y) ? Up : Left;
        var number = 0;
        do {
            var nextDir = this.findNextDir(x, y, dir);
            switch (dir) {
                case Right:
                    this.setTour(x * 2, y * 2, number++);
                    if (nextDir == dir || nextDir == Down || nextDir == Left) this.setTour(x * 2 + 1, y * 2, number++);
                    if (nextDir == Down || nextDir == Left) this.setTour(x * 2 + 1, y * 2 + 1, number++);
                    if (nextDir == Left) this.setTour(x * 2, y * 2 + 1, number++);
                    break;
                case Down:
                    this.setTour(x * 2 + 1, y * 2, number++);
                    if (nextDir == dir || nextDir == Left || nextDir == Up) this.setTour(x * 2 + 1, y * 2 + 1, number++);
                    if (nextDir == Left || nextDir == Up) this.setTour(x * 2, y * 2 + 1, number++);
                    if (nextDir == Up) this.setTour(x * 2, y * 2, number++);
                    break;
                case Left:
                    this.setTour(x * 2 + 1, y * 2 + 1, number++);
                    if (nextDir == dir || nextDir == Up || nextDir == Right) this.setTour(x * 2, y * 2 + 1, number++);
                    if (nextDir == Up || nextDir == Right) this.setTour(x * 2, y * 2, number++);
                    if (nextDir == Right) this.setTour(x * 2 + 1, y * 2, number++);
                    break;
                case Up:
                    this.setTour(x * 2, y * 2 + 1, number++);
                    if (nextDir == dir || nextDir == Right || nextDir == Down) this.setTour(x * 2, y * 2, number++);
                    if (nextDir == Right || nextDir == Down) this.setTour(x * 2 + 1, y * 2, number++);
                    if (nextDir == Down) this.setTour(x * 2 + 1, y * 2 + 1, number++);
                    break;
            }
            dir = nextDir;
            switch (nextDir) {
                case Right:
                    ++x;
                    break;
                case Left:
                    --x;
                    break;
                case Down:
                    ++y;
                    break;
                case Up:
                    --y;
                    break;
            }
        } while (number != this.size);
    }

    // get next node
    public Point getNext(int x, int y, int dir) {
        Point p;
        switch (dir) {
            case Left: p = new Point(x - 1, y); break;
            case Up: p = new Point(x, y - 1); break;
            case Right: p = new Point(x + 1, y); break;
            case Down: p = new Point(x, y + 1); break;
            default: p = new Point(x, y);
        }
        return p;
    }

    // draw map
    public void draw(Snake snake) {
        int[][] grid = new int[height][width];
        // snake.body.forEach(p -> grid[p.y][p.x] = 1); // body
        for (Point p : snake.body) {
            grid[p.y][p.x] = 1;
        }
        grid[snake.food.y][snake.food.x] = 2; // food

        StringBuilder sb = new StringBuilder();
        for (int[] arr : grid) {
            for (int a : arr) {
                sb.append(a);
            }
            sb.append("\n");
        }
        sb.append("\n");

        System.out.println(sb);
    }

    public void debug_print_maze_path() {
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x)
                System.out.printf("%4d ", tour(x,y));
            System.out.println();
        }
    }
}
