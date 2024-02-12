import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Scalable extends BufferedImage {
    public Scalable (int width,int height,int imageType) {
        super(width,height,imageType);
    }

    public BufferedImage scale (double scale) {
        int width = (int)(getWidth() * scale);
        int height = (int)(getHeight() * scale);
        BufferedImage scaled = new BufferedImage(width, height, TYPE_INT_ARGB);
        AffineTransform scaledInstance = AffineTransform.getScaleInstance(scale, scale);
        AffineTransformOp affineTransformOp = new AffineTransformOp(scaledInstance,1);
        Graphics2D graphics2D = (Graphics2D) scaled.getGraphics();
        graphics2D.drawImage(this, affineTransformOp,0,0);
        graphics2D.dispose();
        return scaled;
    }
}

