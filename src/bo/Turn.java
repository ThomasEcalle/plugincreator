package bo;

/**
 * Created by Thomas Ecalle on 15/07/2017.
 */
public class Turn
{
    private String board;
    private int user_id;
    private int game_id;

    public String getBoard()
    {
        return board;
    }

    public void setBoard(String board)
    {
        this.board = board;
    }

    public int getUser_id()
    {
        return user_id;
    }

    public void setUser_id(int user_id)
    {
        this.user_id = user_id;
    }

    public int getGame_id()
    {
        return game_id;
    }

    public void setGame_id(int game_id)
    {
        this.game_id = game_id;
    }

    @Override
    public String toString()
    {
        return board + "\n\n\n";
    }
}
