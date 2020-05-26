package titan;


import titan.constants.Titan;
import titan.syntax.Keywords;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("views/main_view.fxml"));
        Scene scene = new Scene(root, Titan.APP_WIDTH, Titan.APP_HEIGHT);
        scene.getStylesheets().add("titan/styles/editor_style.css");
        scene.getStylesheets().add("titan/styles/tab_pane_style.css");
        scene.getStylesheets().add("titan/styles/menu_style.css");
        scene.getStylesheets().add("titan/styles/result_area_style.css");
        scene.getStylesheets().add("titan/styles/list_style.css");
        scene.getStylesheets().add("titan/styles/toolbar_style.css");

        primaryStage.getIcons().add(Titan.APP_ICON);
        primaryStage.setTitle(Titan.APP_NAME);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.show();

        Keywords.onKeywordsBind();
        mainStage = primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
