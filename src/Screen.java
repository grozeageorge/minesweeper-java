import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class Screen extends JPanel implements MouseListener, MouseMotionListener {

    private final Font font;
    private final BufferedImage mine,flag;
    private Tiles tiles;
    private final int BASE_SIZE = 13, scale;

    private int x,y,fieldX,fieldY;

    public Screen (int x, int y, int scale) {
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.tiles = new Tiles(x,y,0.1);
        Dimension dimension = new Dimension(
                x * BASE_SIZE * scale,
                y * BASE_SIZE * scale
        );
        this.font = new Font(Font.DIALOG, Font.BOLD, scale * 13);
        this.setPreferredSize(dimension);
        this.mine = new Mine().scale(scale);
        this.flag = new Flag().scale(scale);
        this.addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setFont(font);
        int scale = BASE_SIZE * this.scale;
        for (int x = 0; x < tiles.getWidth(); x++) {
            for (int y = 0; y < tiles.getHeight(); y++) {
                int positionX = x * scale;
                int positionY = y * scale;
                Tile tile = tiles.getTile(x, y);

                //draw tile
                g.setColor(Theme.TILE);
                g.fillRect(positionX, positionY, scale, scale);
                //draw tile white shadow
                g.setColor(Theme.WHITE);
                g.drawLine(positionX, positionY, positionX + scale, positionY);
                g.drawLine(positionX, positionY, positionX, positionY + scale);
                //draw tile shadow
                g.setColor(Theme.TILE_SHADOW);
                g.drawLine(positionX, positionY + scale - 1, positionX + scale, positionY + scale - 1);
                g.drawLine(positionX + scale - 1, positionY, positionX + scale - 1, positionY + scale);

                if (tile.getState() == TileState.FLAG) {
                    g.drawImage(flag, positionX + 1, positionY + 1, null);
                } else if (tile.getState() == TileState.MINE) {
                    g.drawImage(mine, positionX + 1, positionY + 1, null);
                } else if (tile.getState() == TileState.REVEALED) {
                    g.setColor(Theme.TILE_SHADOW);
                    g.fillRect(positionX + 1, positionY + 1, scale - 2, scale - 2);
                    if (tile.getSurrounding() > 0) {
                        FontMetrics metrics = graphics2D.getFontMetrics();
                        String value = String.valueOf(tile.getSurrounding());
                        int width = metrics.stringWidth(value);
                        graphics2D.setColor(Theme.COLOR_INDEX[tile.getSurrounding()]);
                        graphics2D.drawString(
                                value,
                                positionX + ((scale >> 1) - (width >> 1)),
                                positionY + ((scale >> 1) + (metrics.getAscent() >> 1)) - 4
                        );
                    }
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.fieldX = e.getX() / scale / BASE_SIZE;
        this.fieldY = e.getY() / scale / BASE_SIZE;
        if (!tiles.isPopulated())
            tiles.populate(fieldX,fieldY);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Tile tile = tiles.getTile(fieldX,fieldY);

        if (tile.getState() == TileState.REVEALED) return;
        switch (e.getButton()) {
            case 1:
                if (tile.isMine()) {
                    tile.setState(TileState.MINE);
                    tiles.conclude(false);

                } else if (tile.getSurrounding() == 0) {
                        tiles.clear(fieldX, fieldY);
                } else {
                    tiles.select(fieldX,fieldY);
                }
                break;
            case 3:
                tiles.guess(fieldX,fieldY);
                break;
        }
        repaint();
        if (tiles.getGameState() == GameState.FINISHED) {
            reset();
        }
    }

    private void reset() {
        this.tiles = new Tiles(x,y,0.1);
        this.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
