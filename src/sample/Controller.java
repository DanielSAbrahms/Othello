package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.util.Scanner;

public class Controller {
    private static String LETTERS = "ABCDEFGH";
    private static char BLACK = 'b';
    private static char WHITE = 'w';
    private static char EMPTY = '-';
    private static char INVALID = '\0';
    private OthelloBoard board;
    private int blackScore;
    private int whiteScore;

    @FXML private Text actiontarget;

    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
        actiontarget.setText("Sign in button pressed");
    }

    private void initBoard(){
        this.board = new OthelloBoard();
        // implement way to check if we want to reverse these starting positions
        board.makeMove(BLACK, WHITE,3, 3);
        board.makeMove(WHITE, BLACK,3,4);
        board.makeMove(WHITE, BLACK,4, 3);
        board.makeMove(BLACK, WHITE,4, 4);
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
        board.makeMove(which, opponent, row, col);
    }

    // Chill method to check if the move is invalid or valid
    public boolean isInvalidMove(int row, int col) {
        return board.isIndexOccupied(row, col);
    }

}
