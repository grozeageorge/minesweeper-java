import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Flag extends Scalable {
    public Flag() {
        super(13,13, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = getGraphics();
        graphics.setColor (Theme.EIGHT);
        graphics.fillRect(0,0,getWidth(),getHeight());
        graphics.setColor(Theme.BLACK);
        graphics.drawLine(7,7,7,10);
        graphics.fillRect(4,9,5,3);
        graphics.fillRect(2,10,9,2);
        graphics.setColor(Theme.RED);
        graphics.drawLine(7,2,7,6);
        graphics.fillRect(5,2,2,5);
        graphics.fillRect(3,3,2,3);
        graphics.drawLine(2,4,2,4);
    }

}

