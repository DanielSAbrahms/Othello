import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    // Global static variables
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
    private String[] players;
    FXMLLoader loader;

    private boolean gameStarted;

    // global variables for the current player and opponent,
    // as well as ints for column and row of current move
    private char currPlayer, currOpponent;
    private int currCol, currRow;

    // Array of Buttons (this is our View game board)
    private Button[][] buttonArr;

    // All explicitly named FXML items used in the class
    @FXML private List<Button> buttonList;
    @FXML private Label ErrorLabel;
    @FXML private Button ConfirmMoveButton, DeclineMoveButton, RevertButton;
    @FXML private Button FlipSetupButton;
    @FXML private javafx.scene.control.ListView MoveHistoryTextView;
    @FXML private Label WhitePointsLabel, BlackPointsLabel;




    // This function will be called every time a game button is clicked
    // This functionality is assigned in the FXML file, under 'onAction'
    @FXML protected void gameButtonPress(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        String which = button.getId();
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

        // Gather the row and column indexes of the move
        // Lots of global variables here, but it's necessary since we need access in the
        // Confirm/Deny listeners
        currRow = Character.getNumericValue(which.charAt(1))-1;
        currCol = LETTERS.indexOf(which.charAt(0));
        if(isInvalidMove(currCol, currRow)){
            ErrorLabel.setText("Error: Invalid Move! Try Again");
            return;
        }
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
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        buttonArr = new Button[8][8];
        initBoard(false);
        this.gameStarted = false;
        this.players = new String[2];
        this.players[0] = "P";
        this.players[1] = "A";
        disableBoard(true);
        ErrorLabel.setText("P: PRESS CONFIRM FOR BLACK\n DECLINE FOR WHITE");
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
        // Always start with player black, and initial score of 2 per player
        WHICH_PLAYER = 0;
    }



    // BLOCK OF LISTENER METHODS FOR VARIOUS BUTTON LISTENERS (NOT GAME BOARD) //



    // newGame just starts everything over again
    public void newGameButtonPress(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        initBoard(false);
        FlipSetupButton.setDisable(false);
    }

    // on flipSetup press, initialize our game with board flipped
    public void flipSetupButtonPress(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        initBoard(true);
    }


    // On confirmMove button press, we make the appropriate move, disable the confirm/
    // deny move buttons, add the move to the history, and enable the board buttons again
    public void confirmMoveButtonPress(ActionEvent actionEvent) {
        if(gameStarted) {
            Button button = buttonArr[currCol][currRow];
            board.makeMove(buttonArr, currPlayer, currOpponent, currCol, currRow);
            if (currPlayer == BLACK) {
                Controller.this.blackScore++;
                button.setStyle("-fx-background-color: black");
            } else {
                Controller.this.whiteScore++;
                button.setStyle("-fx-background-color: white");
            }
            ObservableList<String> moveHistory = MoveHistoryTextView.getItems();
            moveHistory.add((WHICH_PLAYER == 0 ? "BLACK:" : "WHITE:") + LETTERS.charAt(currCol) +
                    String.valueOf(currRow + 1));
            WHICH_PLAYER = (WHICH_PLAYER + 1) % 2;
            ConfirmMoveButton.setDisable(true);
            DeclineMoveButton.setDisable(true);
            currRow = currCol = -1;
        } else {
            gameStarted = true;
        }
        disableBoard(false);
    }

    // Reverts game state to state prior to the last move, if a previous
    // state exists
    public void revertButtonPress(ActionEvent actionEvent) {
        if(previousBoard == null){
            ErrorLabel.setText("No previous state");
            return;
        }
        // Set the current board to the previous board, draw the previous board, and
        // Delete the last move from the move history
        Button button = (Button) actionEvent.getSource();
        this.board = this.previousBoard;
        this.previousBoard = null;
        drawGivenBoard(this.previousBoard);
        WHICH_PLAYER = (WHICH_PLAYER+1)%2;
        ObservableList<String> moveHistory = MoveHistoryTextView.getItems();
        moveHistory.remove(moveHistory.size()-1);
        System.out.println(this.board.getNumOccupiedBlack());
        WhitePointsLabel.setText("White: " + String.valueOf(board.getNumOccupiedWhite()));
        BlackPointsLabel.setText("Black: " + String.valueOf(board.getNumOccupiedBlack()));
    }

    // Listener for decline move button press
    public void declineMoveButtonPress(ActionEvent actionEvent) {
        if(gameStarted) {
            ConfirmMoveButton.setDisable(true);
            DeclineMoveButton.setDisable(true);
            currRow = currCol = -1;
            disableBoard(false);
            RevertButton.fire();
        } else {
            gameStarted = true;
            Collections.reverse(Arrays.asList(players));
            disableBoard(false);
        }
        WhitePointsLabel.setText("White: " + String.valueOf(board.getNumOccupiedWhite()));
        BlackPointsLabel.setText("Black: " + String.valueOf(board.getNumOccupiedBlack()));
    }



    // BLOCK OF HELPER FUNCTIONS FOR DRAWING/DISABLING BOARD BUTTONS //


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
        System.out.println(currPlayer);
        if(disabled)
            ErrorLabel.setText((WHICH_PLAYER == 0 ? this.players[1] : this.players[0]) + " Please Confirm Move");
        else
            ErrorLabel.setText((WHICH_PLAYER == 0 ? this.players[0] : this.players[1]) + " Make A Move");
        WhitePointsLabel.setText("White points: " + String.valueOf(this.board.getNumOccupiedWhite()));
        BlackPointsLabel.setText("Black points: " + String.valueOf(this.board.getNumOccupiedBlack()));
    }

    // Chill method to check if the move is invalid or valid
    public boolean isInvalidMove(int row, int col) {
        return board.isIndexOccupied(row, col);
    }
}

