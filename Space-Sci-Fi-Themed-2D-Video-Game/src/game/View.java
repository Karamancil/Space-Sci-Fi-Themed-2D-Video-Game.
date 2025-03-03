package game7;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

import static game7.Constants.FRAME_WIDTH;
import static game7.Constants.FRAME_HEIGHT;

public class View extends JComponent {
    public static final Color BG_COLOR = Color.BLACK;
    private Game game;
    Image im = Constants.MILKYWAY2;
    AffineTransform bgTransf;

    public View(Game game){
        this.game = game;
        double imWidth = im.getWidth(null);
        double imHeight = im.getHeight(null);
        double stretchx = imWidth > FRAME_WIDTH ? FRAME_WIDTH / imWidth : 1;
        double stretchy = imHeight > FRAME_HEIGHT ? FRAME_HEIGHT / imHeight : 1;
        bgTransf = new AffineTransform();
        bgTransf.scale(stretchx, stretchy);

    }
    public void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0;
        g.drawImage(im, bgTransf, null);
        //g.setColor(BG_COLOR);
        //g.fillRect(0,0,getWidth(), getHeight());
        synchronized (Game.class) {
            for (GameObject object : game.objects)
                object.draw(g);
            for (Particle p: game.particles)
                p.draw(g);

        }
        g.setColor(Color.YELLOW);g.setFont(new Font("dialog", Font.BOLD, 20));g.drawString("Level: "+Game.getLevel(), 20, FRAME_HEIGHT-20);
        g.drawString("Score: "+Game.getScore(), FRAME_WIDTH/3+20, FRAME_HEIGHT-20);
        g.drawString("Lives: "+Game.getLives(), 2*FRAME_WIDTH/3+20, FRAME_HEIGHT-20);
        if (Game.getLives()==0)
            g.drawString("GAME OVER Score "+Game.getScore(), FRAME_WIDTH/2-100, FRAME_HEIGHT/2-20);
    }

    public Dimension getPreferredSize(){
        return Constants.FRAME_SIZE;
    }
}
