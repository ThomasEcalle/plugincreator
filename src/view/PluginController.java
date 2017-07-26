package view;

import WS.GetAPICall;
import bo.GameFromDB;
import bo.Turn;
import bo.User;
import com.google.gson.Gson;
import com.parasites.ParasitesPlugin;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import org.json.JSONArray;
import utils.AnimatedZoomOperator;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Thomas Ecalle on 09/07/2017.
 */
public class PluginController implements ParasitesPlugin, Initializable
{
    @FXML
    private GridPane gridPane;

    @FXML
    private ListView<User> listView;

    @FXML
    private TableView tableView;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button next;

    @FXML
    private Button previous;

    @FXML
    private TableColumn<GameFromDB, String> dateColumn;

    @FXML
    private Label label;

    private User user;

    private static String token;

    private ArrayList<Turn> actualTurns;
    private ArrayList<User> actualUsers;

    private ObservableList<User> dataUser;

    private int turnsCounter;

    private GraphicBoard graphicBoard;


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        next.setVisible(false);
        previous.setVisible(false);


        dataUser = FXCollections.observableArrayList();

        actualTurns = new ArrayList<>();
        actualUsers = new ArrayList<>();
        dateColumn.setCellValueFactory(new PropertyValueFactory<GameFromDB, String>("created_at"));

        tableView.getItems().addAll(getGamesDate());

        listView.setMouseTransparent(true);
        listView.setFocusTraversable(false);

        //        listView.setCellFactory(new Callback<ListView<User>, ListCell<User>>()
        //        {
        //
        //            @Override
        //            public ListCell<User> call(ListView<User> p)
        //            {
        //
        //                ListCell<User> cell = new ListCell<User>()
        //                {
        //
        //                    @Override
        //                    protected void updateItem(User user, boolean bln)
        //                    {
        //
        //                        final Label label;
        //                        super.updateItem(user, bln);
        //                        if (user != null)
        //                        {
        //                            label = new Label(user.getPseudo());
        //                        } else
        //                        {
        //                            label = new Label("");
        //                        }
        //
        //                        listView.getItems().remove(this);
        //                        setGraphic(label);
        //
        //
        //                    }
        //
        //                };
        //
        //                return cell;
        //            }
        //        });


        tableView.setRowFactory(tv ->
        {
            TableRow<GameFromDB> row = new TableRow<>();
            row.setOnMouseClicked(event ->
            {
                if (!row.isEmpty())
                {
                    turnsCounter = 0;

                    final GameFromDB game = (GameFromDB) tableView.getItems().get(row.getIndex());

                    actualTurns.clear();
                    actualTurns.addAll(getAllTurns(game.getId()));

                    if (!actualTurns.isEmpty())
                    {
                        next.setVisible(true);
                        previous.setVisible(true);

                        final Turn turn = actualTurns.get(turnsCounter);


                        refreshListView(getAllPlayersInGame(game.getId()), listView);


                        final int index = listView.getItems().indexOf(getTurnUser(turn.getUser_id()));

                        if (index != -1)
                        {
                            listView.getSelectionModel().select(index);
                        }


                        graphicBoard = new GraphicBoard(turn.getBoard(), game.getSize(), borderPane.getWidth());

                        graphicBoard.setAlignment(Pos.CENTER);

                        borderPane.setCenter(graphicBoard);

                        final AnimatedZoomOperator zoomOperator = new AnimatedZoomOperator();

                        graphicBoard.setOnScroll((event2) ->
                        {
                            double zoomFactor = 1.5;
                            if (event2.getDeltaY() <= 0)
                            {
                                // zoom out
                                zoomFactor = 1 / zoomFactor;
                            }
                            zoomOperator.zoom(graphicBoard, zoomFactor, event2.getSceneX(), event2.getSceneY());

                        });

                        handleButtonsAvailability();
                    }
                }
            });
            return row;
        });


    }

    private User getTurnUser(final int user_id)
    {
        for (User actualUser : actualUsers)
        {
            if (actualUser.getId() == user_id)
            {
                return actualUser;
            }
        }
        return null;
    }


    @Override
    public String getName()
    {
        return "historique";
    }

    @Override
    public void setUserToken(int id, String token)
    {
        PluginController.token = token;
        final Gson gson = new Gson();

        final String urlToGetUser = "http://10.33.1.74:5678/users/" + id + "?token=" + token;
        try
        {
            final String response = GetAPICall.getResponse(urlToGetUser);
            user = gson.fromJson(response, User.class);

        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public Node getContent()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("historic.fxml"));
        Node root = null;
        try
        {
            root = fxmlLoader.load();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return root;

    }

    @FXML
    private void nextTurn()
    {
        turnsCounter++;
        if (turnsCounter < actualTurns.size())
        {
            draw();
        }

        handleButtonsAvailability();
    }

    @FXML
    private void previousTurn()
    {
        turnsCounter--;
        if (turnsCounter >= 0)
        {
            draw();
        }

        handleButtonsAvailability();
    }

    private void draw()
    {
        final Turn turn = actualTurns.get(turnsCounter);
        graphicBoard.setBoard(turn.getBoard());
        graphicBoard.drawBoard();

        final int index = listView.getItems().indexOf(getTurnUser(turn.getUser_id()));
        if (index != -1)
        {
            listView.getSelectionModel().select(index);
        }
    }

    private List<GameFromDB> getGamesDate()
    {

        final String urlToGetGames = "http://10.33.1.74:5678/games/?token=" + PluginController.token;


        final List<GameFromDB> games = new ArrayList<>();


        String gamesString = null;

        try
        {
            gamesString = GetAPICall.getResponse(urlToGetGames);

            final Gson gson = new Gson();

            final JSONArray arr = new JSONArray(gamesString);
            for (int i = 0; i < arr.length(); i++)
            {

                final GameFromDB gameFromDB = gson.fromJson(arr.get(i).toString(), GameFromDB.class);

                games.add(gameFromDB);
            }

        } catch (Exception e)
        {
            System.out.println(e);
        }

        return games;
    }

    private List<Turn> getAllTurns(final int gameID)
    {
        final String urlToGetTurns = "http://10.33.1.74:5678/turns/" + gameID + "?token=" + token;


        final List<Turn> turns = new ArrayList<>();


        String turnsString = null;

        try
        {
            turnsString = GetAPICall.getResponse(urlToGetTurns);


            final Gson gson = new Gson();

            final JSONArray arr = new JSONArray(turnsString);
            for (int i = 0; i < arr.length(); i++)
            {

                final Turn turn = gson.fromJson(arr.get(i).toString(), Turn.class);

                turns.add(turn);
            }

        } catch (Exception e)
        {
            System.out.println(e);
        }

        return turns;
    }

    private List<User> getAllPlayersInGame(final int gameID)
    {
        final String urlToGetTurns = "http://10.33.1.74:5678/games/getPlayers/" + gameID + "?token=" + token;


        actualUsers.clear();


        String turnsString = null;

        try
        {
            turnsString = GetAPICall.getResponse(urlToGetTurns);


            final Gson gson = new Gson();

            final JSONArray arr = new JSONArray(turnsString);
            for (int i = 0; i < arr.length(); i++)
            {

                final User user = gson.fromJson(arr.get(i).toString(), User.class);

                actualUsers.add(user);
            }

        } catch (Exception e)
        {
            System.out.println(e);
        }

        return actualUsers;
    }

    private void handleButtonsAvailability()
    {
        if (turnsCounter == (actualTurns.size() - 1))
        {
            next.setDisable(true);
        } else
        {
            next.setDisable(false);
        }

        if (turnsCounter == 0)
        {
            previous.setDisable(true);
        } else
        {
            previous.setDisable(false);
        }

    }

    private void refreshListView(final List<?> list, final ListView listView)
    {
        Platform.runLater(() ->
        {
            dataUser.clear();
            dataUser.addAll((Collection<? extends User>) list);
            listView.setItems(dataUser);
        });
    }
}
