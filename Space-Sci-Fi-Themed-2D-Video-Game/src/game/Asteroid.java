package game7;

import utilities.SoundManager;
import utilities.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import static game7.Constants.FRAME_HEIGHT;
import static game7.Constants.FRAME_WIDTH;

import java.util.List;
import java.util.Random;

public class Asteroid extends GameObject {
    //public Sprite sprite;
    public double rotationPerFrame;
    public Vector2D direction;

    public Polygon polygon;

    // public static final int RADIUS = 10;
    public static final double MAX_SPEED = 100;
    public static final int MIN_POINTS = 20, MAX_POINTS = 40;
    public static final int MIN_RADIUS = 10, MAX_RADIUS = 20;
     public boolean isLarge = true;
    public List<Asteroid> spawnedAsteroids = new ArrayList<Asteroid>();

    public Asteroid(Game g, Vector2D pos, double vx, double vy, boolean isLarge) {
        super(g, pos, new Vector2D(vx, vy), 0);
        this.isLarge = isLarge;
        double dir = Math.random() * 2 * Math.PI;
        direction = new Vector2D(Math.cos(dir), Math.sin(dir));
        position = new Vector2D(pos);
        //sprite = new Sprite(spr.image, position, direction, spr.width, spr.height);
        //if (!isLarge) {
        //    sprite.height *= 2/3.0;
        //    sprite.width *= 2/3.0;
        //}
        //radius = sprite.getRadius();
        int nPoints = MIN_POINTS + (int)(Math.random()*(MAX_POINTS-MIN_POINTS));
        int[] px = new int[nPoints];
        int[] py = new int[nPoints];
        for (int i=0; i<nPoints; i++){
            double theta = Math.PI * 2 * (i + Math.random())/ nPoints;
            double rad = MIN_RADIUS + Math.random() * (MAX_RADIUS - MIN_RADIUS);
            if (!isLarge) rad *=2.0/3;
            Vector2D point = Vector2D.polar(theta, rad);
            px[i] = (int)(point.x); py[i] = (int)(point.y);
            // System.out.println(point.x+ " " +point.y);
        }
        polygon = new Polygon(px, py, nPoints);
        Rectangle bounds = polygon.getBounds();
        radius = (bounds.getWidth() + bounds.getHeight())/2;
        if (radius < 1) {
            System.out.println("Radius: "+ radius);
            for (int i = 0; i<nPoints; i++)
                System.out.println(px[i] + " " + py[i]);
        }rotationPerFrame = Math.random()  * 0.1;


    }

    public void draw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(position.x, position.y);
        double rot = direction.angle() + Math.PI / 2;
        g.rotate(rot);
        g.setColor(Color.GRAY);
        g.fill(polygon);
        g.setColor(Color.BLACK);
        g.draw(polygon);
        g.setTransform(at);

        // sprite.draw(g);
        //g.setColor(Color.RED);
        //g.fillOval((int) (position.x - radius), (int) (position.y - radius), (int) (2 * radius), (int) (2 * radius));
    }

    public Asteroid(Game g) {
        super(g, new Vector2D(Math.random() * FRAME_WIDTH, Math.random() + FRAME_HEIGHT), new Vector2D(0, 0), 0);
        double vx = Math.random() * MAX_SPEED;
        if (Math.random() < 0.5) vx *= -1;
        double vy = Math.random() * MAX_SPEED;
        if (Math.random() < 0.5) vy *= -1;
        velocity.set(new Vector2D(vx, vy));
        //double width = Math.min(Math.max(20+new Random().nextGaussian()*30, 30), 50);
        //Image im = Sprite.ASTEROID1;
        //double height = width * im.getHeight(null)/im.getWidth(null);
        double dir = Math.random() * 2 * Math.PI;
        direction = new Vector2D(Math.cos(dir), Math.sin(dir));
        //sprite = new Sprite(im, position, direction, width, height);
        //radius = sprite.getRadius();
        //rotationPerFrame = Math.random()  * 0.1;
        int nPoints = MIN_POINTS + (int)(Math.random()*(MAX_POINTS-MIN_POINTS));
        int[] px = new int[nPoints];
        int[] py = new int[nPoints];
        for (int i=0; i<nPoints; i++){
            double theta = Math.PI * 2 * (i + Math.random())/ nPoints;
            double rad = MIN_RADIUS + Math.random() * (MAX_RADIUS - MIN_RADIUS);
            if (!isLarge) rad *=2.0/3;
            Vector2D point = Vector2D.polar(theta, rad);
            px[i] = (int)(point.x); py[i] = (int)(point.y);
            // System.out.println(point.x+ " " +point.y);
        }
        polygon = new Polygon(px, py, nPoints);
        Rectangle bounds = polygon.getBounds();
        radius = (bounds.getWidth() + bounds.getHeight())/2;
        if (radius < 1) {
            System.out.println("Radius: "+ radius);
            for (int i = 0; i<nPoints; i++)
                System.out.println(px[i] + " " + py[i]);
        }
        rotationPerFrame = Math.random()  * 0.1;
    }

    private void spawn() {
        for (int i = 0; i < 2; i++) {
            double vx = Math.random() * MAX_SPEED;
            if (Math.random() < 0.5) vx *= -1;
            double vy = Math.random() * MAX_SPEED;
            if (Math.random() < 0.5) vy *= -1;
            // spawnedAsteroids.add(new Asteroid(new Vector2D(position.x, position.y), vx, vy, false, sprite));
            spawnedAsteroids.add(new Asteroid(game, new Vector2D(position.x, position.y), vx, vy, false));
        }

    }

    @Override
    public void hit() {
        super.hit();

        if (isLarge) {
            SoundManager.play(SoundManager.bangMedium);
            spawn();
        } else {
            SoundManager.play(SoundManager.bangSmall);
        }

        if (!isLarge)
            game.explosion(this);
    }

    @Override
    public boolean canCollide(GameObject other) {
        return !(other instanceof Saucer);
    }
    public String toString() {
        return "Asteroid: " + super.toString();
    }

    public void update() {
        super.update();
        direction.rotate(rotationPerFrame);

    }
}
