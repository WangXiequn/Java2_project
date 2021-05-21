package ch.makery.address;

import ch.makery.address.view.StartController;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {
    public Stage primaryStage;
    private BorderPane rootLayout;
    private BorderPane StartLayout;
    public BorderPane mainpane=new BorderPane();//outmost
    public static String PlayingMode;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AddressApp");
        Scene scene = new Scene(mainpane);
        scene.getStylesheets().add("/stylesheets/global.css");
        initstart();
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public void initstart() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Start.fxml"));
            StartLayout = (BorderPane) loader.load();
            StartController controller = loader.getController();
            controller.setMainApp(this);
            // Show the scene containing the root layout.
            mainpane.setCenter(StartLayout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
