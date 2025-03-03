package game7;

import utilities.JEasyFrame;
import utilities.Vector2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static game7.Constants.*;

public class Game {
    public static final int N_INITIAL_ASTEROIDS = 5;
    public List<GameObject> objects;
    public List<Ship> ships;

    public List<Particle> particles;
    PlayerShip playerShip;
    Keys ctrl;
    Controller controller;

    private static int score = 0;
    private static int lives = 5;
    private static int level = 1;
    public static boolean gameOver = false;

    public Game() {
        objects = new ArrayList<GameObject>();
        ships = new ArrayList<Ship>();
        particles = new ArrayList<Particle>();
        for (int i = 0; i < N_INITIAL_ASTEROIDS; i++) {
            objects.add(new Asteroid(this));

        }

        ctrl = new Keys();  //always create this (even when not used) to avoid
        // having to comment out adding of action listener
        controller = ctrl;
        // alternate controller options to replace above line
        // controller = new RandomAction();
        // controller = new RotateNShoot();
        playerShip = new PlayerShip(this, controller);
        objects.add(playerShip);
        ships.add(playerShip);

        addSaucers();
    }

    public void newLevel() {
        level++;
        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }
        synchronized (Game.class) {
            objects.clear();
            ships.clear();
            particles.clear();
            for (int i = 0; i < N_INITIAL_ASTEROIDS + 2 * (level - 1); i++) {
                objects.add(new Asteroid(this));

            }
            playerShip = new PlayerShip(this, controller);
            objects.add(playerShip);
            ships.add(playerShip);
            addSaucers();
        }

    }

    public void newLife() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }
        synchronized (Game.class) {
            objects.clear();
            ships.clear();
            particles.clear();
            for (int i = 0; i < N_INITIAL_ASTEROIDS + 2 * (level - 1); i++) {
                objects.add(new Asteroid(this));

            }
            // playerShip = new Ship(ctrl);
            // playerShip = new Ship(new RotateNShoot());
            playerShip = new PlayerShip(this, controller);
            objects.add(playerShip);
            ships.add(playerShip);
            // saucer = new Saucer(new RandomAction());
            addSaucers();
        }
    }

    private void addSaucers() {
        for (int i = 0; i < 3; i++) {
            Controller ctrl = (i % 3 != 0 ? new RandomAction() : new AimNShoot(this));
            Color colorBody = (i % 3 != 0 ? Color.PINK : Color.GREEN);
            Random r = new Random();
            Vector2D s = new Vector2D(
                    r.nextInt(Constants.FRAME_WIDTH),
                    r.nextInt(Constants.FRAME_HEIGHT));
            Ship saucer = new Saucer(this, ctrl, colorBody, Color.white);
            if (i % 3 == 0) {
                ((AimNShoot) ctrl).setShip(saucer);
                // move the saucer if it is too close to the player ship;
                // otherwise it will shoot ship immediately before player
                // has time to flee
                Vector2D posDiff = new Vector2D(saucer.position).subtract(playerShip.position);
                if (posDiff.mag() < AimNShoot.SHOOTING_DISTANCE) {
                    saucer.position = new Vector2D(playerShip.position).addScaled(posDiff.normalise(), AimNShoot.SHOOTING_DISTANCE * 0.75);
                }
            }
            objects.add(saucer);
            ships.add(saucer);
        }
    }

    public static void main(String[] args) {
        Game game = new Game();
        View view = new View(game);
        new JEasyFrame(view, "Game with AI").addKeyListener(game.ctrl);
        while (!gameOver) {
            game.update();
            game.updateParticles();
            view.repaint();
            try {
                Thread.sleep(DELAY);
            } catch (Exception e) {
            }


        }
    }

    public void update() {
        for (int i = 0; i < objects.size(); i++) {
            GameObject o1 = objects.get(i);
            for (int j = i + 1; j < objects.size(); j++) {
                GameObject o2 = objects.get(j);
                o1.collisionHandling(o2);
            }
        }
        List<GameObject> alive = new ArrayList<>();
        boolean noAsteroids = true;
        boolean noShip = true;
        for (GameObject o : objects) {
            o.update();
            if (o instanceof Asteroid) {
                noAsteroids = false;
                Asteroid a = (Asteroid) o;
                if (!a.spawnedAsteroids.isEmpty()) {
                    alive.addAll(a.spawnedAsteroids);
                    a.spawnedAsteroids.clear();
                }
            } else if (o instanceof PlayerShip) noShip = false;
            if (!o.dead) alive.add(o);
            for (Ship s : ships)
                if (s.bullet != null) {
                    alive.add(s.bullet);
                    s.bullet = null;
                }

        }
        synchronized (Game.class) {
            objects.clear();
            objects.addAll(alive);
        }
        if (noAsteroids) {
            newLevel();
        } else if (noShip) {
            newLife();
        }
    }

    void updateParticles() {
        List<Particle> alive = new ArrayList<Particle>();
        for (Particle p:particles) {
            p.update();
            if (!p.dead)
                alive.add(p);

        }
        synchronized (Game.class) {
            particles.clear();
            particles.addAll(alive);
        }
    }

    public void explosion(GameObject object) {
        if (object instanceof Ship)
            for (int i = 0; i<100; i++) {
                Particle p = new Particle(this, object.position, object.velocity, ((Ship) object).color);
                particles.add(p);

            }
        else
            for (int i = 0; i<100; i++) {
                Particle p = new Particle(this, object.position, object.velocity);
                particles.add(p);

            }
    }

    public static void incScore(int inc) {
        int oldScore = score;
        score += inc;
        System.out.println("Score " + score);
        if (score / 5000 > oldScore / 5000) {
            System.out.println("Adding life");
            lives++;
        }
    }

    public static void loseLife() {
        lives--;
        if (lives == 0)
            gameOver = true;
    }

    public static int getScore() {
        return score;
    }

    public static int getLevel() {
        return level;
    }

    public static int getLives() {
        return lives;
    }


}

