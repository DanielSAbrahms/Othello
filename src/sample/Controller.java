package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;

public class Controller {

    @FXML private Button actiontarget;

    @FXML protected void gameButtonPressed(ActionEvent event) {
        actiontarget.setStyle("-fx-background-color: #000000");
    }

    public Controller() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            Pane p = fxmlLoader.load(getClass().getResource("sample.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Button a1 = (Button) fxmlLoader.getController();
    }
}
