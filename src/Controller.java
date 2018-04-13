import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private static String LETTERS = "ABCDEFGH";
    private static char BLACK = 'b';
    private static char WHITE = 'w';
    private static char EMPTY = '-';
    private static char INVALID = '\0';
    private int WHICH_PLAYER = 0;
    private OthelloBoard board;
    private int blackScore;
    private int whiteScore;

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



    // This function will be called every time a game button is clicked
    // This functionality is assigned in the FXML file, under 'onAction'
    @FXML protected void gameButtonPress(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        String which = button.getId();
        System.out.println(which);
        int row = Character.getNumericValue(which.charAt(1))-1;
        int col = LETTERS.indexOf(which.charAt(0));
        System.out.println(row + "||" + col);
        if(WHICH_PLAYER == 0) {
            //board.highlightAppropriate(buttonArr, 'b', 'w', col, row);
            board.makeMove(buttonArr, 'b', 'w', col, row);
            button.setStyle("-fx-background-color: black");
        }
        else {
            //board.highlightAppropriate(buttonArr, 'w', 'b', col, row);
            board.makeMove(buttonArr, 'w', 'b', col, row);
            button.setStyle("-fx-background-color: white");
        }
        WHICH_PLAYER = (WHICH_PLAYER+1) % 2;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttonArr = new Button[8][8];
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                int accessPoint = i*8 + j;
                buttonArr[i][j] = buttonList.get(accessPoint);
                buttonArr[i][j].setStyle("-fx-background-color: green");
            }
        }
        initBoard(false);
    }

    private void initBoard(boolean reversed){
        this.board = new OthelloBoard();
        // implement way to check if we want to reverse these starting positions
        board.makeMove(buttonArr,BLACK, WHITE,3, 3);
        buttonArr[3][3].setStyle("-fx-background-color: black");
        board.makeMove(buttonArr, WHITE, BLACK,3,4);
        buttonArr[3][4].setStyle("-fx-background-color: white");
        board.makeMove(buttonArr, WHITE, BLACK,4, 3);
        buttonArr[4][3].setStyle("-fx-background-color: white");
        board.makeMove(buttonArr, BLACK, WHITE,4, 4);
        buttonArr[4][4].setStyle("-fx-background-color: black");
        System.out.println(board);
        this.blackScore = 2;
        this.whiteScore = 2;
    }


    // main driver method. Gonna take some overhauling to figure out how exactly
    // itll work
    private void playGame() throws Exception{
        int which = 0;
        char player, opponent;
        while(board.getNumOccupied() < 64){
            // Text input in place, easy to modify to GUI
            if(which%2 == 0){
                player = BLACK;
                opponent = WHITE;
            } else {
                player = WHITE;
                opponent = BLACK;
            }
            // System.out.print(player == BLACK ? "BLACK: " : "WHITE: ");
            // System.out.print("Enter row, followed comma, followed by column char: (ie: ROW,COL)");
            try {
                // Code to get input coordinates from input values (text version, keeping for legacy)
                //      int row = Character.getNumericValue(input.charAt(0)) - 1;
                //      int col = LETTERS.indexOf(String.valueOf(input.charAt(2)).toUpperCase());

                // checking if move made is valid lines of code, don't want depalma to break our code
                // leaving these lines here so that it'll be easier to adapt to GUI later, rather than rewrite
                //      if(row>7 || row < 0 || col > 7 || col < 0) throw new IllegalArgumentException("Out of bounds move");
                //      if(isInvalidMove(row,col)) throw new IllegalArgumentException("Move already played");
                //      placeMove(player, opponent, row, col);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            System.out.println(board);
            which++;
            System.out.println("BLACK: " + board.getNumOccupiedBlack() + " || WHITE: " + board.getNumOccupiedWhite());
        }
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
        //TODO
    }

    public void flipSetupButtonPress(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        //TODO
    }

    public void confirmMoveButtonPress(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        //TODO
    }
}
