package view;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 * Created by Thomas Ecalle on 26/02/2017.
 */
public final class GraphicBoard extends GridPane
{
    private String[] board;
    private int size;
    private double borderPaneSize;

    public GraphicBoard(final String boardString, final int size, final double borderPaneSize)
    {
        this.board = convertBoardToArray(boardString);
        this.size = size;
        this.borderPaneSize = borderPaneSize;

        drawBoard();
    }

    public void setBoard(String boardString)
    {
        this.board = convertBoardToArray(boardString);
    }

    private String[] convertBoardToArray(final String string)
    {
        final String replace = string.replace("\n", "");
        final String replace2 = replace.replace("|", " ");
        return replace2.split(" ");
    }


    public void drawBoard()
    {
        getChildren().clear();

        int counter = 0;

        final double tileWidth = borderPaneSize / size;

        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {

                final Tile tile = new Tile(tileWidth, tileWidth, counter);

                tile.setIconAndUser(board);

                tile.setStroke(Color.BLACK);

                counter++;

                add(tile, j, i);
            }
        }

    }


}
