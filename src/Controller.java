import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.swing.text.html.ListView;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;

public class Controller implements Initializable {
    private static String LETTERS = "ABCDEFGH";
    private static char BLACK = 'b';
    private static char WHITE = 'w';
    private static char EMPTY = '-';
    private static char INVALID = '\0';
    private int WHICH_PLAYER = 0;
    private OthelloBoard board;
    private OthelloBoard previousBoard;
    private int blackScore;
    private int whiteScore;
    FXMLLoader loader;
    private Semaphore semaphore = new Semaphore(0);
    private static Object monitor = new Object();

    @FXML private GridPane GameGrid;

    @FXML private List<Button> buttonList;

    private Button[][] buttonArr;

    // These are all the game buttons
    @FXML private Button
            A1, A2, A3, A4, A5, A6, A7, A8,
            B1, B2, B3, B4, B5, B6, B7, B8,
            C1, C2, C3, C4, C5, C6, C7, C8,
            D1, D2, D3, D4, D5, D6, D7, D8,
            E1, E2, E3, E4, E5, E6, E7, E8,
            F1, F2, F3, F4, F5, F6, F7, F8,
            G1, G2, G3, G4, G5, G6, G7, G8,
            H1, H2, H3, H4, H5, H6, H7, H8;

    @FXML private Label ErrorLabel;
    @FXML private Button ConfirmMoveButton, DeclineMoveButton, RevertButton;
    @FXML private Button FlipSetupButton;
    @FXML private javafx.scene.control.ListView MoveHistoryTextView;

    private char currPlayer, currOpponent;
    private int currCol, currRow;



    // This function will be called every time a game button is clicked
    // This functionality is assigned in the FXML file, under 'onAction'
    @FXML protected void gameButtonPress(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        String which = button.getId();
        System.out.println(which);
        FlipSetupButton.setDisable(true);

        // All this code is bc Java LOVES references, and we have to literally
        // copy the contents of each and every cell to a new cell, or else this
        // terrible language won't do the right thing
        Cell[][] newBoard = new Cell[8][8];
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                Cell tmpCell = board.getCellAtIndex(j,i);
                newBoard[j][i] = new Cell(tmpCell.getCoordinates(),
                        tmpCell.getSymbol(), tmpCell.isOccupied());
            }
        }
        this.previousBoard = new OthelloBoard(newBoard);

        currRow = Character.getNumericValue(which.charAt(1))-1;
        currCol = LETTERS.indexOf(which.charAt(0));
        if(isInvalidMove(currCol, currRow)){
            ErrorLabel.setText("Error: Invalid Move! Try Again");
            return;
        }
        ErrorLabel.setText("PLEASE CONFIRM MOVE");
        if(WHICH_PLAYER == 0) {
            currPlayer = BLACK;
            currOpponent = WHITE;
        }
        else {
            currPlayer = WHITE;
            currOpponent = BLACK;
        }
        board.highlightAppropriate(buttonArr, currPlayer, currOpponent, currCol, currRow);
        ConfirmMoveButton.setDisable(false);
        DeclineMoveButton.setDisable(false);
        disableBoard(true);
        // TODO: Display the scores to the screen
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        buttonArr = new Button[8][8];
        initBoard(false);
    }

    private void initBoard(boolean reversed){
        this.board = new OthelloBoard();
        // Setting all appropriate tiles to green, then coloring black/white
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                int accessPoint = i*8 + j;
                buttonArr[i][j] = buttonList.get(accessPoint);
                buttonArr[i][j].setStyle("-fx-background-color: green");
            }
        }
        // Placing the appropriate tiles to start the game
        if(!reversed) {
            board.makeMove(buttonArr, BLACK, WHITE, 3, 3);
            buttonArr[3][3].setStyle("-fx-background-color: black");
            board.makeMove(buttonArr, WHITE, BLACK, 3, 4);
            buttonArr[3][4].setStyle("-fx-background-color: white");
            board.makeMove(buttonArr, WHITE, BLACK, 4, 3);
            buttonArr[4][3].setStyle("-fx-background-color: white");
            board.makeMove(buttonArr, BLACK, WHITE, 4, 4);
            buttonArr[4][4].setStyle("-fx-background-color: black");
        } else if (reversed){
            board.makeMove(buttonArr, WHITE, BLACK, 3, 3);
            buttonArr[3][3].setStyle("-fx-background-color: white");
            board.makeMove(buttonArr, BLACK, WHITE, 3, 4);
            buttonArr[3][4].setStyle("-fx-background-color: black");
            board.makeMove(buttonArr, BLACK, WHITE, 4, 3);
            buttonArr[4][3].setStyle("-fx-background-color: black");
            board.makeMove(buttonArr, WHITE, BLACK, 4, 4);
            buttonArr[4][4].setStyle("-fx-background-color: white");
        }
        WHICH_PLAYER = 0;
        System.out.println(board);
        this.blackScore = 2;
        this.whiteScore = 2;
    }


    // Local method for passing move making/updating to the OthelloBoard class
    public void placeMove(char which, char opponent, int row, int col) {
        board.makeMove(buttonArr, which, opponent, row, col);
    }

    // Chill method to check if the move is invalid or valid
    public boolean isInvalidMove(int row, int col) {
        return board.isIndexOccupied(row, col);
    }


    public void newGameButtonPress(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        initBoard(false);
        FlipSetupButton.setDisable(false);
    }

    public void flipSetupButtonPress(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        initBoard(true);
    }


    public void confirmMoveButtonPress(ActionEvent actionEvent) {
        Button button = buttonArr[currCol][currRow];
        board.makeMove(buttonArr, currPlayer, currOpponent, currCol, currRow);
        if(currPlayer == BLACK){
            Controller.this.blackScore++;
            button.setStyle("-fx-background-color: black");
        } else {
            Controller.this.whiteScore++;
            button.setStyle("-fx-background-color: white");
        }
        ObservableList<String> moveHistory = MoveHistoryTextView.getItems();
        moveHistory.add((WHICH_PLAYER == 0 ? "BLACK:" : "WHITE:") + LETTERS.charAt(currCol) +
                String.valueOf(currRow+1));
        WHICH_PLAYER = (WHICH_PLAYER+1) % 2;
        ConfirmMoveButton.setDisable(true);
        DeclineMoveButton.setDisable(true);
        currRow = currCol = -1;
        disableBoard(false);
    }

    // Reverts game state to state prior to the last move, if a previous
    // state exists
    public void revertButtonPress(ActionEvent actionEvent) {
        if(previousBoard == null){
            ErrorLabel.setText("No previous state");
            return;
        }
        Button button = (Button) actionEvent.getSource();
        this.board = this.previousBoard;
        drawGivenBoard(this.previousBoard);
        ObservableList<String> moveHistory = MoveHistoryTextView.getItems();
        moveHistory.remove(moveHistory.size()-1);
    }


    // Listener for decline move button press
    public void declineMoveButtonPress(ActionEvent actionEvent) {
        ConfirmMoveButton.setDisable(true);
        DeclineMoveButton.setDisable(true);
        currRow = currCol = -1;
        disableBoard(false);
        RevertButton.fire();
    }

    // Method to color in game buttons appropriately given OthelloBoard
    private void drawGivenBoard(OthelloBoard board){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                char which = this.board.getCellAtIndex(i,j).getSymbol();
                if(which == BLACK){
                    buttonArr[i][j].setStyle("-fx-background-color: black");
                } else if (which == WHITE){
                    buttonArr[i][j].setStyle("-fx-background-color: white");
                } else {
                    buttonArr[i][j].setStyle("-fx-background-color: green");
                }
            }
        }
    }

    // Disable all buttons in the board so no one can break our code
    private void disableBoard(boolean disabled){
        for(Button[] arr: buttonArr){
            for(Button b: arr){
                b.setDisable(disabled);
            }
        }
        if(disabled) ErrorLabel.setText("Please Confirm Move");
        else ErrorLabel.setText((currPlayer == 0 ? "Black" : "White") + " Make A Move");
    }
}

