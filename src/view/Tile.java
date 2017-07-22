package view;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import utils.Utils;

/**
 * Created by Thomas Ecalle on 24/02/2017.
 */
public class Tile extends Rectangle
{
    private final int tileCoordonate;

    public Tile(double w, double h, int tileCoordonate)
    {
        super(w, h, Color.YELLOW);
        this.tileCoordonate = tileCoordonate;
    }


    public void setIconAndUser(final String[] board)
    {
        final String icon = board[tileCoordonate].split(",")[0];
        setIcon(icon);
    }

    private void setIcon(String icon)
    {
        String url = null;
        switch (icon)
        {
            case "W":
                url = "ic_warrior.png";
                break;
            case "D":
                url = "ic_defender.png";
                break;
            case "B":
                url = "ic_builder.png";
                break;
            case "C":
                url = "ic_colony.png";
                break;
            case "Q":
                url = "ic_queen.png";
                break;
        }

        if (url != null)
        {
            final Image image = new Image(Utils.getResourceUrl(url, getClass()));

            final ImagePattern imagePattern = new ImagePattern(image);

            setFill(imagePattern);
        }
    }
}
