import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Tiles {

    private GameState gameState;
    private static final Random random = new Random();
    private Tile[][] tiles;
    private int width, height, mines,flags,correctFlags;
    private double probability;
    private boolean populated;
    public Tiles(int x, int y, double probability) {
        this.width = x;
        this.height = y;
        this.probability = probability;
        this.tiles = new Tile[x][y];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                this.tiles[i][j] = new Tile();
            }
        }
    }

    public void populate(int initialX, int initialY) {
        int mines = (int)((width*height)*probability);
        do {
            int mineX = random.nextInt(width);
            int mineY = random.nextInt(height);
            Tile tile = tiles[mineX][mineY];
            if (tile.isMine() || (initialX == mineX && initialY == mineY))
                continue;
            this.mines++;
            tile.setMine(true);
            mines--;
        } while(mines > 0);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Tile tile = tiles[i][j];
                int total = 0;
                for (int k = i - 1; k < i + 2; ++k) {
                    for (int l = j - 1; l < j + 2; ++l) {
                        if (k >= 0 && l >= 0 && k < width && l < height) {
                            Tile target = tiles[k][l];
                            if (!target.isMine())
                                continue;
                            total++;
                        }
                    }
                }
                tile.setSurrounding(total);
            }
        }
        this.setPopulated(true);
    }

    public void guess(int x, int y) {
        Tile tile = tiles[x][y];
        tile.setState(TileState.FLAG);
        flags++;
        if (tile.isMine()) {
            correctFlags++;
        }
        if (correctFlags == mines && flags == mines) {
            conclude(true);
        }
    }

    public void clear(int x, int y) {
        for (int i = x - 1; i < x + 2; i++) {
            for (int j = y - 1; j < y + 2; j++) {
                if (i >= 0 && j >= 0 && i < width && j < height) {
                    Tile tile = tiles[i][j];
                    if (tile.getState() == TileState.REVEALED || tile.isMine()) continue;
                    tile.setState(TileState.REVEALED);
                    if (tile.getSurrounding() == 0) {
                        clear(i,j);
                    }
                }
            }
        }
    }

    public void select(int x, int y) {
        Tile tile = tiles[x][y];
        boolean isFlag = tile.getState() == TileState.FLAG;
        tile.setState(TileState.REVEALED);

        if (isFlag) {
            flags--;
            if (tile.isMine()) {
                correctFlags--;
            }
        }

        tile.setState(TileState.REVEALED);
        if (correctFlags == mines && flags == mines) {
            conclude(true);
        }
    }

    public boolean isPopulated() {
        return populated;
    }

    public void setPopulated(boolean populated) {
        this.populated = populated;
    }

    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void conclude(boolean win) {
        Frame frame = Frame.getFrames()[0];
        frame.repaint();
        JOptionPane.showMessageDialog(frame, win ? "You won" : "You died");
        this.gameState = GameState.FINISHED;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
