package sample;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class Controller {

    public Controller() {
        @FXML private Text actiontarget;

        @FXML protected void handleSubmitButtonAction(ActionEvent event) {
            actiontarget.setText("Sign in button pressed");
        }
    }

}
