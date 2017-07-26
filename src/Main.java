import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Thomas Ecalle on 09/07/2017.
 */
public class Main extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
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

        final Scene scene = new Scene((Parent) root);

        primaryStage.setScene(scene);

        primaryStage.show();

    }
}
