/*
* CLASS: CPSC427-01
* Authors: Jason Conci, Daniel Abrahms, Tyler Tiedt
* ID's:    -jconci      -dabrahms       -ttiedt
* Assignment: Project 8 - An Interface to Othello
*
* INSTRUCTIONS FOR USE:
*   1. Run Main.java, window should open.
*   2. Upon opening, look in the bottom left corner to see prompt for P
*       - P hits the CONFIRM button to choose black
*       - P hits the DENY button to choose white
*   3. Upon choosing black or white, play starts. Current player has 10 seconds t0 move.
*       - Upon moving, the opposing player is prompted to either confirm or deny the player's made move
*       - Confirming means that it is a legal move, deny means that the move is illegal.
*           > Upon confirming, it is the opposing player's turn
*           > Upon denying, it is the current player's turn again, to make a different move
*             Timer starts over when the move is denied.
*       - Play will go on until there are no more moves that can be made, or when the quit button is
*         pressed.
*    4. Further instructions for use
*       - 'Revert' button returns state of board to the state prior to the last move.
*          'revert' only stores the LAST state. Pressing more than once will result in an error,
*          since the state before the previous state is not stored
*       - "Decline" button is reserved for when the current player has no available moves.
*          Pressing this button forfeits the turn, and it becomes the opposing player's turn.
*
* Sources:
*     [1] Othello, haly. Used in OthelloBoard.java, highlighting/making move function
*           https://github.com/haly/Othello/blob/master/Othello.java
*     [2] Timeline, javafx. Used in implementing timer in Controller class (disableBoard)
*           https://docs.oracle.com/javase/8/javafx/api/javafx/animation/Timeline.html
*
 */



import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Jarvis");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();
        Controller controller = new Controller();
    }




    public static void main(String[] args) {
        launch(args);
    }
}
