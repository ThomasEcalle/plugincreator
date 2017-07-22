package bo;

/**
 * Created by Thomas Ecalle on 14/07/2017.
 */
public final class GameFromDB
{
    private int size;
    private String updated_at;
    private int nb_player;
    private int user_id;
    private String created_at;
    private int id;
    private String deleted_at;

    public String getDeleted_at()
    {
        return deleted_at;
    }

    public String getCreated_at()
    {
        return created_at;
    }

    public void setCreated_at(String created_at)
    {
        this.created_at = created_at;
    }

    public int getUser_id()
    {
        return user_id;
    }

    public void setUser_id(int user_id)
    {
        this.user_id = user_id;
    }

    public int getNb_player()
    {
        return nb_player;
    }

    public void setNb_player(int nb_player)
    {
        this.nb_player = nb_player;
    }

    public String getUpdated_at()
    {
        return updated_at;
    }

    public void setUpdated_at(String updated_at)
    {
        this.updated_at = updated_at;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public void setDeleted_at(String deleted_at)
    {
        this.deleted_at = deleted_at;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }
}

