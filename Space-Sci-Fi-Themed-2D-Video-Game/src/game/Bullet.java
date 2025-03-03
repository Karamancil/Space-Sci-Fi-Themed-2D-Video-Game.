package game7;

import utilities.Vector2D;

import java.awt.*;


import static game7.Constants.DT;

public class Bullet extends GameObject {
    private double lifetime;
    public static final int RADIUS = 2;
    public static final int BULLET_LIFE = 1; // seconds
    public boolean firedByShip;

    public Bullet(Game g, Vector2D pos, Vector2D vel, boolean firedByShip) {
        super(g, pos, vel, 2);
        this.lifetime = BULLET_LIFE;
        this.firedByShip = firedByShip;
    }

    @Override
    public void update() {
        super.update();
        lifetime -= DT;
        if (lifetime <= 0) dead = true;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(firedByShip ? Color.WHITE : Color.BLUE);
        g.fillOval((int) position.x - RADIUS, (int) position.y - RADIUS, 2 * RADIUS, 2 * RADIUS);


    }

    public String toString() {
        return "Bullet; " + super.toString();
    }
}
