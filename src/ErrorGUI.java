import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;

public class ErrorGUI {
    private static Stage window;
    private static Scene error;

    public static void display(String errorMessage){
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        Label errorLabel = new Label(errorMessage);
        errorLabel.setWrapText(true);
        errorLabel.setAlignment(Pos.CENTER);
        errorLabel.setTextAlignment(TextAlignment.CENTER);

        Button ok = new Button("OK");
        ok.setOnAction(e -> window.close());

        VBox vert = new VBox(20);
        vert.setAlignment(Pos.CENTER);
        vert.getChildren().addAll(errorLabel, ok);

        error = new Scene(vert, 300, 100);
        window.setTitle("ERROR");
        window.setMinHeight(100);
        window.setMinWidth(200);
        window.setScene(error);
        window.showAndWait();
    }
}
