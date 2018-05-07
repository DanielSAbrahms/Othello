import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.Comparator;

public class OthelloBoard {
    private static String LETTERS = "ABCDEFGH";
    private static char BLACK = 'b';
    private static char WHITE = 'w';
    private static char EMPTY = '-';
    private static char INVALID = '\0';
    private static int DEPTH_BOUND = 5;
    private static int DEPTH_BOUND_OTHER = 5;
    private Cell[][] board;
    private int numOccupied;
    private int numOccupiedBlack;
    private int numOccupiedWhite;
    private final int heur_win_bonus = 10000;
    private final int heur_corner_bonus = 400;
    private final int heur_corner_takeover_bonus = 200;
    private final int heur_edge_bonus = 20;
    private final int heur_late_game_bonus = 20;

    public OthelloBoard() {
        this.board = new Cell[8][8];
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                board[i][j] = (new Cell(new Coordinates(i,j), '-', false));
            }
        }
        this.numOccupied = 0;
        countEmUp();
    }

    public OthelloBoard(Cell[][] cellArr){
        this.board = cellArr;
        countEmUp();
    }


    public Cell[][] getBoard(){
        return this.board;
    }


    public boolean isIndexOccupied(int row, int col){
        return board[row][col].isOccupied();
    }



    // This is the beefy method where we flip all the tiles that are surrounded, on two sides, by
    // a piece belonging to the opponent
    public void makeMove(Button[][] buttonArr, char player, char opponent, int row, int col){
        // Making the actual player's move
        Cell tmp = board[row][col];
        tmp.setSymbol(player);
        tmp.setOccupied(true);
        numOccupied++;


        int i = row, j = col;

        // Lots of tedious code to change the appropriate tiles
        if(i-1>=0 && j-1>=0 && board[i-1][j-1].getSymbol() == opponent){
            i = i-1; j = j-1;
            while(i>0 && j>0 && board[i][j].getSymbol() == opponent) {
                i--;
                j--;
            }
            if(i>=0 && j>=0 && board[i][j].getSymbol() == player) {
                while(i!=row-1 && j!=col-1) {
                    board[++i][++j].setSymbol(player);
                    if(player == 'b') buttonArr[i][j].setStyle("-fx-background-color: black");
                    else buttonArr[i][j].setStyle("-fx-background-color: white");
                }
            }
        }

        i=row;j=col;
        if(i-1>=0 && board[i-1][j].getSymbol() == opponent){
            i = i-1;
            while(i>0 && board[i][j].getSymbol() == opponent)
                i--;
            if(i>=0 && board[i][j].getSymbol() == player) {
                while(i!=row-1) {
                    board[++i][j].setSymbol(player);
                    if (player == 'b') buttonArr[i][j].setStyle("-fx-background-color: black");
                    else buttonArr[i][j].setStyle("-fx-background-color: white");
                }
            }
        }

        i=row;
        if(i-1>=0 && j+1<=7 && board[i-1][j+1].getSymbol() == opponent){
            i = i-1; j = j+1;
            while(i>0 && j<7 && board[i][j].getSymbol() == opponent){
                i--;j++;
            }
            if(i>=0 && j<=7 && board[i][j].getSymbol() == player) {
                while(i!=row-1 && j!=col+1){
                    board[++i][--j].setSymbol(player);
                    if(player == 'b') buttonArr[i][j].setStyle("-fx-background-color: black");
                    else buttonArr[i][j].setStyle("-fx-background-color: white");
                }
            }
        }

        i=row;j=col;
        if(j-1>=0 && board[i][j-1].getSymbol() == opponent){
            j = j-1;
            while(j>0 && board[i][j].getSymbol() == opponent)
                j--;
            if(j>=0 && board[i][j].getSymbol() == player) {
                while(j!=col-1){
                    board[i][++j].setSymbol(player);
                    if(player == 'b') buttonArr[i][j].setStyle("-fx-background-color: black");
                    else buttonArr[i][j].setStyle("-fx-background-color: white");
                }
            }
        }

        j=col;
        if(j+1<=7 && board[i][j+1].getSymbol() == opponent){
            j=j+1;
            while(j<7 && board[i][j].getSymbol() == opponent)
                j++;
            if(j<=7 && board[i][j].getSymbol() == player) {
                while(j!=col+1){
                    board[i][--j].setSymbol(player);
                    if(player == 'b') buttonArr[i][j].setStyle("-fx-background-color: black");
                    else buttonArr[i][j].setStyle("-fx-background-color: white");
                }
            }
        }

        j=col;
        if(i+1<=7 && j-1>=0 && board[i+1][j-1].getSymbol() == opponent){
            i=i+1;j=j-1;
            while(i<7 && j>0 && board[i][j].getSymbol() == opponent){
                i++;
                j--;
            }
            if(i<=7 && j>=0 && board[i][j].getSymbol() == player) {
                while(i!=row+1 && j!=col-1){
                    board[--i][++j].setSymbol(player);
                    if(player == 'b') buttonArr[i][j].setStyle("-fx-background-color: black");
                    else buttonArr[i][j].setStyle("-fx-background-color: white");
                }
            }
        }

        i=row;
        j=col;
        if(i+1 <= 7 && board[i+1][j].getSymbol() == opponent){
            i=i+1;
            while(i<7 && board[i][j].getSymbol() == opponent)
                i++;
            if(i<=7 && board[i][j].getSymbol() == player) {
                while(i!=row+1){
                    board[--i][j].setSymbol(player);
                    if(player == 'b') buttonArr[i][j].setStyle("-fx-background-color: black");
                    else buttonArr[i][j].setStyle("-fx-background-color: white");
                }
            }
        }

        i=row;
        if(i+1 <= 7 && j+1 <=7 && board[i+1][j+1].getSymbol() == opponent){
            i=i+1;j=j+1;
            while(i<7 && j<7 && board[i][j].getSymbol() == opponent){
                i++;
                j++;
            }
            if(i<=7 && j<=7 && board[i][j].getSymbol() == player) {
                while (i != row + 1 && j != col + 1){
                    board[--i][--j].setSymbol(player);
                    if(player == 'b') buttonArr[i][j].setStyle("-fx-background-color: black");
                    else buttonArr[i][j].setStyle("-fx-background-color: white");
                }
            }
        }
        countEmUp();
    }

    public OthelloBoard fakeMove(char player, char opponent, int row, int col){
        
        OthelloBoard newBoard = this;
        
        // Faking the actual player's move
        Cell tmp = newBoard.getBoard()[row][col];
        tmp.setSymbol(player);
        tmp.setOccupied(true);
        newBoard.numOccupied++;


        int i = row, j = col;

        // Lots of tedious code to change the appropriate tiles
        if(i-1>=0 && j-1>=0 && newBoard.getBoard()[i-1][j-1].getSymbol() == opponent){
            i = i-1; j = j-1;
            while(i>0 && j>0 && newBoard.getBoard()[i][j].getSymbol() == opponent) {
                i--;
                j--;
            }
            if(i>=0 && j>=0 && newBoard.getBoard()[i][j].getSymbol() == player) {
                while(i!=row-1 && j!=col-1) {
                    newBoard.getBoard()[++i][++j].setSymbol(player);
                }
            }
        }

        i=row;j=col;
        if(i-1>=0 && newBoard.getBoard()[i-1][j].getSymbol() == opponent){
            i = i-1;
            while(i>0 && newBoard.getBoard()[i][j].getSymbol() == opponent)
                i--;
            if(i>=0 && newBoard.getBoard()[i][j].getSymbol() == player) {
                while(i!=row-1) {
                    newBoard.getBoard()[++i][j].setSymbol(player);
                }
            }
        }

        i=row;
        if(i-1>=0 && j+1<=7 && newBoard.getBoard()[i-1][j+1].getSymbol() == opponent){
            i = i-1; j = j+1;
            while(i>0 && j<7 && newBoard.getBoard()[i][j].getSymbol() == opponent){
                i--;j++;
            }
            if(i>=0 && j<=7 && newBoard.getBoard()[i][j].getSymbol() == player) {
                while(i!=row-1 && j!=col+1){
                    newBoard.getBoard()[++i][--j].setSymbol(player);
                }
            }
        }

        i=row;j=col;
        if(j-1>=0 && newBoard.getBoard()[i][j-1].getSymbol() == opponent){
            j = j-1;
            while(j>0 && newBoard.getBoard()[i][j].getSymbol() == opponent)
                j--;
            if(j>=0 && newBoard.getBoard()[i][j].getSymbol() == player) {
                while(j!=col-1){
                    newBoard.getBoard()[i][++j].setSymbol(player);
                }
            }
        }

        j=col;
        if(j+1<=7 && newBoard.getBoard()[i][j+1].getSymbol() == opponent){
            j=j+1;
            while(j<7 && newBoard.getBoard()[i][j].getSymbol() == opponent)
                j++;
            if(j<=7 && newBoard.getBoard()[i][j].getSymbol() == player) {
                while(j!=col+1){
                    newBoard.getBoard()[i][--j].setSymbol(player);
                }
            }
        }

        j=col;
        if(i+1<=7 && j-1>=0 && newBoard.getBoard()[i+1][j-1].getSymbol() == opponent){
            i=i+1;j=j-1;
            while(i<7 && j>0 && newBoard.getBoard()[i][j].getSymbol() == opponent){
                i++;
                j--;
            }
            if(i<=7 && j>=0 && newBoard.getBoard()[i][j].getSymbol() == player) {
                while(i!=row+1 && j!=col-1){
                    newBoard.getBoard()[--i][++j].setSymbol(player);
                }
            }
        }

        i=row;
        j=col;
        if(i+1 <= 7 && newBoard.getBoard()[i+1][j].getSymbol() == opponent){
            i=i+1;
            while(i<7 && newBoard.getBoard()[i][j].getSymbol() == opponent)
                i++;
            if(i<=7 && newBoard.getBoard()[i][j].getSymbol() == player) {
                while(i!=row+1){
                    newBoard.getBoard()[--i][j].setSymbol(player);
                }
            }
        }

        i=row;
        if(i+1 <= 7 && j+1 <=7 && newBoard.getBoard()[i+1][j+1].getSymbol() == opponent){
            i=i+1;j=j+1;
            while(i<7 && j<7 && newBoard.getBoard()[i][j].getSymbol() == opponent){
                i++;
                j++;
            }
            if(i<=7 && j<=7 && newBoard.getBoard()[i][j].getSymbol() == player) {
                while (i != row + 1 && j != col + 1){
                    newBoard.getBoard()[--i][--j].setSymbol(player);
                }
            }
        }
        newBoard.countEmUp();
        return newBoard;
    }

    public void highlightAppropriate(Button[][] buttonArr, char player, char opponent, int row, int col){
        // Making the actual player's move
        Cell tmp = board[row][col];
        tmp.setSymbol(player);
        tmp.setOccupied(true);
        numOccupied++;


        int i = row, j = col;

        // Lots of tedious code to change the appropriate tiles
        if(i-1>=0 && j-1>=0 && board[i-1][j-1].getSymbol() == opponent){
            i = i-1; j = j-1;
            while(i>0 && j>0 && board[i][j].getSymbol() == opponent) {
                i--;
                j--;
            }
            if(i>=0 && j>=0 && board[i][j].getSymbol() == player) {
                while(i!=row-1 && j!=col-1){
                    buttonArr[++i][++j].setStyle("-fx-background-color: yellow");
                }
            }
        }

        i=row;j=col;
        if(i-1>=0 && board[i-1][j].getSymbol() == opponent){
            i = i-1;
            while(i>0 && board[i][j].getSymbol() == opponent)
                i--;
            if(i>=0 && board[i][j].getSymbol() == player) {
                while(i!=row-1)
                    buttonArr[++i][j].setStyle("-fx-background-color: yellow");
            }
        }

        i=row;
        if(i-1>=0 && j+1<=7 && board[i-1][j+1].getSymbol() == opponent){
            i = i-1; j = j+1;
            while(i>0 && j<7 && board[i][j].getSymbol() == opponent){
                i--;j++;
            }
            if(i>=0 && j<=7 && board[i][j].getSymbol() == player) {
                while(i!=row-1 && j!=col+1)
                    buttonArr[++i][--j].setStyle("-fx-background-color: yellow");
            }
        }

        i=row;j=col;
        if(j-1>=0 && board[i][j-1].getSymbol() == opponent){
            j = j-1;
            while(j>0 && board[i][j].getSymbol() == opponent)
                j--;
            if(j>=0 && board[i][j].getSymbol() == player) {
                while(j!=col-1)
                    buttonArr[i][++j].setStyle("-fx-background-color: yellow");
            }
        }

        j=col;
        if(j+1<=7 && board[i][j+1].getSymbol() == opponent){
            j=j+1;
            while(j<7 && board[i][j].getSymbol() == opponent)
                j++;
            if(j<=7 && board[i][j].getSymbol() == player) {
                while(j!=col+1)
                    buttonArr[i][--j].setStyle("-fx-background-color: yellow");
            }
        }

        j=col;
        if(i+1<=7 && j-1>=0 && board[i+1][j-1].getSymbol() == opponent){
            i=i+1;j=j-1;
            while(i<7 && j>0 && board[i][j].getSymbol() == opponent){
                i++;
                j--;
            }
            if(i<=7 && j>=0 && board[i][j].getSymbol() == player) {
                while(i!=row+1 && j!=col-1)
                    buttonArr[--i][++j].setStyle("-fx-background-color: yellow");
            }
        }

        i=row;
        j=col;
        if(i+1 <= 7 && board[i+1][j].getSymbol() == opponent){
            i=i+1;
            while(i<7 && board[i][j].getSymbol() == opponent)
                i++;
            if(i<=7 && board[i][j].getSymbol() == player) {
                while(i!=row+1)
                    buttonArr[--i][j].setStyle("-fx-background-color: yellow");
            }
        }

        i=row;
        if(i+1 <= 7 && j+1 <=7 && board[i+1][j+1].getSymbol() == opponent){
            i=i+1;j=j+1;
            while(i<7 && j<7 && board[i][j].getSymbol() == opponent){
                i++;
                j++;
            }
            if(i<=7 && j<=7 && board[i][j].getSymbol() == player) {
                while (i != row + 1 && j != col + 1)
                    buttonArr[--i][--j].setStyle("-fx-background-color: yellow");
            }
        }

        if(player == 'b') buttonArr[row][col].setStyle("-fx-background-color: black");
        else buttonArr[row][col].setStyle("-fx-background-color: white");
    }

    public int countPotential(char player, char opponent, int row, int col){
        // Making the actual player's move
        Cell tmp = board[row][col];
        tmp.setSymbol(player);
        tmp.setOccupied(true);
        numOccupied++;

        int differential = 0;


        int i = row, j = col;

        // Lots of tedious code to change the appropriate tiles
        if(i-1>=0 && j-1>=0 && board[i-1][j-1].getSymbol() == opponent){
            i = i-1; j = j-1;
            while(i>0 && j>0 && board[i][j].getSymbol() == opponent) {
                i--;
                j--;
            }
            if(i>=0 && j>=0 && board[i][j].getSymbol() == player) {
                while(i!=row-1 && j!=col-1){
                    ++i;++j;
                    differential++;
                }
            }
        }

        i=row;j=col;
        if(i-1>=0 && board[i-1][j].getSymbol() == opponent){
            i = i-1;
            while(i>0 && board[i][j].getSymbol() == opponent)
                i--;
            if(i>=0 && board[i][j].getSymbol() == player) {
                while(i!=row-1)
                    ++i;
                differential++;
            }
        }

        i=row;
        if(i-1>=0 && j+1<=7 && board[i-1][j+1].getSymbol() == opponent){
            i = i-1; j = j+1;
            while(i>0 && j<7 && board[i][j].getSymbol() == opponent){
                i--;j++;
            }
            if(i>=0 && j<=7 && board[i][j].getSymbol() == player) {
                while(i!=row-1 && j!=col+1)
                    ++i;--j;
                    differential++;
            }
        }

        i=row;j=col;
        if(j-1>=0 && board[i][j-1].getSymbol() == opponent){
            j = j-1;
            while(j>0 && board[i][j].getSymbol() == opponent)
                j--;
            if(j>=0 && board[i][j].getSymbol() == player) {
                while(j!=col-1)
                    ++j;
                differential++;
            }
        }

        j=col;
        if(j+1<=7 && board[i][j+1].getSymbol() == opponent){
            j=j+1;
            while(j<7 && board[i][j].getSymbol() == opponent)
                j++;
            if(j<=7 && board[i][j].getSymbol() == player) {
                while(j!=col+1)
                    --j;
                differential++;
            }
        }

        j=col;
        if(i+1<=7 && j-1>=0 && board[i+1][j-1].getSymbol() == opponent){
            i=i+1;j=j-1;
            while(i<7 && j>0 && board[i][j].getSymbol() == opponent){
                i++;
                j--;
            }
            if(i<=7 && j>=0 && board[i][j].getSymbol() == player) {
                while(i!=row+1 && j!=col-1)
                    --i;++j;
                    differential++;
            }
        }

        i=row;
        j=col;
        if(i+1 <= 7 && board[i+1][j].getSymbol() == opponent){
            i=i+1;
            while(i<7 && board[i][j].getSymbol() == opponent)
                i++;
            if(i<=7 && board[i][j].getSymbol() == player) {
                while(i!=row+1)
                    --i;
                    differential++;
            }
        }

        i=row;
        if(i+1 <= 7 && j+1 <=7 && board[i+1][j+1].getSymbol() == opponent){
            i=i+1;j=j+1;
            while(i<7 && j<7 && board[i][j].getSymbol() == opponent){
                i++;
                j++;
            }
            if(i<=7 && j<=7 && board[i][j].getSymbol() == player) {
                while (i != row + 1 && j != col + 1)
                    --i;--j;
                    differential++;
            }
        }

        return differential;
    }

    protected ArrayList<Coordinates> findPlaceableLocations(char player, char opponent){
        ArrayList<Coordinates> placeablePositions = new ArrayList<Coordinates>();
        for(int i=0;i<8;++i){
            for(int j=0;j<8;++j){
                if(board[i][j].getSymbol() == opponent){
                    int I = i, J = j;
                    if(i-1>=0 && j-1>=0 && board[i-1][j-1].getSymbol() == '-'){
                        i = i+1; j = j+1;
                        while(i<7 && j<7 && board[i][j].getSymbol() == opponent){i++;j++;}
                        if(i<=7 && j<=7 && board[i][j].getSymbol() == player) placeablePositions.add(new Coordinates(I-1, J-1));
                    }
                    i=I;j=J;
                    if(i-1>=0 && board[i-1][j].getSymbol() == '-'){
                        i = i+1;
                        while(i<7 && board[i][j].getSymbol() == opponent) i++;
                        if(i<=7 && board[i][j].getSymbol() == player) placeablePositions.add(new Coordinates(I-1, J));
                    }
                    i=I;
                    if(i-1>=0 && j+1<=7 && board[i-1][j+1].getSymbol() == '-'){
                        i = i+1; j = j-1;
                        while(i<7 && j>0 && board[i][j].getSymbol() == opponent){i++;j--;}
                        if(i<=7 && j>=0 && board[i][j].getSymbol() == player) placeablePositions.add(new Coordinates(I-1, J+1));
                    }
                    i=I;j=J;
                    if(j-1>=0 && board[i][j-1].getSymbol() == '-'){
                        j = j+1;
                        while(j<7 && board[i][j].getSymbol() == opponent)j++;
                        if(j<=7 && board[i][j].getSymbol() == player) placeablePositions.add(new Coordinates(I, J-1));
                    }
                    j=J;
                    if(j+1<=7 && board[i][j+1].getSymbol() == '-'){
                        j=j-1;
                        while(j>0 && board[i][j].getSymbol() == opponent)j--;
                        if(j>=0 && board[i][j].getSymbol() == player) placeablePositions.add(new Coordinates(I, J+1));
                    }
                    j=J;
                    if(i+1<=7 && j-1>=0 && board[i+1][j-1].getSymbol() == '-'){
                        i=i-1;j=j+1;
                        while(i>0 && j<7 && board[i][j].getSymbol() == opponent){i--;j++;}
                        if(i>=0 && j<=7 && board[i][j].getSymbol() == player) placeablePositions.add(new Coordinates(I+1, J-1));
                    }
                    i=I;j=J;
                    if(i+1 <= 7 && board[i+1][j].getSymbol() == '-'){
                        i=i-1;
                        while(i>0 && board[i][j].getSymbol() == opponent) i--;
                        if(i>=0 && board[i][j].getSymbol() == player) placeablePositions.add(new Coordinates(I+1, J));
                    }
                    i=I;
                    if(i+1 <= 7 && j+1 <=7 && board[i+1][j+1].getSymbol() == '-'){
                        i=i-1;j=j-1;
                        while(i>0 && j>0 && board[i][j].getSymbol() == opponent){i--;j--;}
                        if(i>=0 && j>=0 && board[i][j].getSymbol() == player)placeablePositions.add(new Coordinates(I+1, J+1));
                    }
                    i=I;j=J;
                }
            }
        }
        return placeablePositions;
    }

    private void makeMoveText (char player, char opponent, int row, int col){
        // Making the actual player's move
        Cell tmp = board[row][col];
        tmp.setSymbol(player);
        tmp.setOccupied(true);
        numOccupied++;


        int i = row, j = col;

        // Lots of tedious code to change the appropriate tiles
        if(i-1>=0 && j-1>=0 && board[i-1][j-1].getSymbol() == opponent){
            i = i-1; j = j-1;
            while(i>0 && j>0 && board[i][j].getSymbol() == opponent) {
                i--;
                j--;
            }
            if(i>=0 && j>=0 && board[i][j].getSymbol() == player) {
                while(i!=row-1 && j!=col-1) {
                    board[++i][++j].setSymbol(player);
                }
            }
        }

        i=row;j=col;
        if(i-1>=0 && board[i-1][j].getSymbol() == opponent){
            i = i-1;
            while(i>0 && board[i][j].getSymbol() == opponent)
                i--;
            if(i>=0 && board[i][j].getSymbol() == player) {
                while(i!=row-1) {
                    board[++i][j].setSymbol(player);
                }
            }
        }

        i=row;
        if(i-1>=0 && j+1<=7 && board[i-1][j+1].getSymbol() == opponent){
            i = i-1; j = j+1;
            while(i>0 && j<7 && board[i][j].getSymbol() == opponent){
                i--;j++;
            }
            if(i>=0 && j<=7 && board[i][j].getSymbol() == player) {
                while(i!=row-1 && j!=col+1){
                    board[++i][--j].setSymbol(player);
                }
            }
        }

        i=row;j=col;
        if(j-1>=0 && board[i][j-1].getSymbol() == opponent){
            j = j-1;
            while(j>0 && board[i][j].getSymbol() == opponent)
                j--;
            if(j>=0 && board[i][j].getSymbol() == player) {
                while(j!=col-1){
                    board[i][++j].setSymbol(player);
                }
            }
        }

        j=col;
        if(j+1<=7 && board[i][j+1].getSymbol() == opponent){
            j=j+1;
            while(j<7 && board[i][j].getSymbol() == opponent)
                j++;
            if(j<=7 && board[i][j].getSymbol() == player) {
                while(j!=col+1){
                    board[i][--j].setSymbol(player);
                }
            }
        }

        j=col;
        if(i+1<=7 && j-1>=0 && board[i+1][j-1].getSymbol() == opponent){
            i=i+1;j=j-1;
            while(i<7 && j>0 && board[i][j].getSymbol() == opponent){
                i++;
                j--;
            }
            if(i<=7 && j>=0 && board[i][j].getSymbol() == player) {
                while(i!=row+1 && j!=col-1){
                    board[--i][++j].setSymbol(player);
                }
            }
        }

        i=row;
        j=col;
        if(i+1 <= 7 && board[i+1][j].getSymbol() == opponent){
            i=i+1;
            while(i<7 && board[i][j].getSymbol() == opponent)
                i++;
            if(i<=7 && board[i][j].getSymbol() == player) {
                while(i!=row+1){
                    board[--i][j].setSymbol(player);
                }
            }
        }

        i=row;
        if(i+1 <= 7 && j+1 <=7 && board[i+1][j+1].getSymbol() == opponent){
            i=i+1;j=j+1;
            while(i<7 && j<7 && board[i][j].getSymbol() == opponent){
                i++;
                j++;
            }
            if(i<=7 && j<=7 && board[i][j].getSymbol() == player) {
                while (i != row + 1 && j != col + 1){
                    board[--i][--j].setSymbol(player);
                }
            }
        }
        countEmUp();
    }


    public Coordinates AI(char player, char opponent){
        System.out.println("CALLED AI!");
        ArrayList<Coordinates> placeableLocations = findPlaceableLocations(player, opponent);
        ArrayList<CellValContainer> moves = new ArrayList<>();
        for (Coordinates child: placeableLocations){
            moves.add(maxVal(this, child, player, opponent, -10000, 10000, 1));
        }
        if(moves.size() == 0) return null;
        Coordinates which = moves.stream().max(Comparator.comparing(CellValContainer::getValue)).get().getCoordinates();
        return which;
    }



    private CellValContainer maxVal(OthelloBoard currBoard, Coordinates node, char player, char opponent, int alpha, int beta, int depth){
        System.out.println("THAT WAS CURR BOARD");
        Cell[][] newBoard = new Cell[8][8];
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                Cell tmpCell = currBoard.getCellAtIndex(j,i);
                newBoard[j][i] = new Cell(tmpCell.getCoordinates(),
                        tmpCell.getSymbol(), tmpCell.isOccupied());
            }
        }
        OthelloBoard tmpBoard = new OthelloBoard(newBoard);
        if(depth >= (player == BLACK ? DEPTH_BOUND : DEPTH_BOUND_OTHER)){
            int numAheadBy = 0;
            if(player == BLACK){
                numAheadBy = tmpBoard.getNumOccupiedBlack() - tmpBoard.getNumOccupiedWhite();

            } else {
                numAheadBy = tmpBoard.getNumOccupiedWhite() - tmpBoard.getNumOccupiedBlack();
            }
            //return new CellValContainer(node, tmpBoard.countPotential(player, opponent, node.getCol(), node.getRow()));
            return new CellValContainer(node, tmpBoard.cell_heur_player(player, opponent, node.getCol(), node.getRow(), depth));
        }
        int v = -10000;
        tmpBoard.makeMoveText(player, opponent, node.getCol(), node.getRow());
        for(Coordinates child: tmpBoard.findPlaceableLocations(player, opponent)){
            int v1 = minVal(tmpBoard, child, opponent, player, alpha, beta, depth+1).getValue();
            if ((v == -1000) || (v1 > v)){
                v = v1;
            }
            if(beta != -1000){
                if(v1 >= beta){
                    return new CellValContainer(node, v);
                }
            }
            if((alpha == -1000) || (v1 > alpha)){
                alpha = v1;
            }

        }
        return new CellValContainer(node, v);
    }


    private CellValContainer minVal(OthelloBoard currBoard, Coordinates node, char player, char opponent, int alpha, int beta, int depth){
        System.out.println("THAT WAS CURR BOARD");
        Cell[][] newBoard = new Cell[8][8];
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                Cell tmpCell = currBoard.getCellAtIndex(j,i);
                newBoard[j][i] = new Cell(tmpCell.getCoordinates(),
                        tmpCell.getSymbol(), tmpCell.isOccupied());
            }
        }
        OthelloBoard tmpBoard = new OthelloBoard(newBoard);
        if(depth >= (player == BLACK ? DEPTH_BOUND : DEPTH_BOUND_OTHER)){
            int numAheadBy = 0;
            if(player == BLACK){
                numAheadBy = tmpBoard.getNumOccupiedBlack() - tmpBoard.getNumOccupiedWhite();

            } else {
                numAheadBy = tmpBoard.getNumOccupiedWhite() - tmpBoard.getNumOccupiedBlack();
            }
            //return new CellValContainer(node, tmpBoard.countPotential(player, opponent, node.getCol(), node.getRow()));
            return new CellValContainer(node, tmpBoard.cell_heur_player(player, opponent, node.getCol(), node.getRow(), depth));
        }
        int v = 10000;
        tmpBoard.makeMoveText(player, opponent, node.getCol(), node.getRow());
        for(Coordinates child: tmpBoard.findPlaceableLocations(player, opponent)){
            int v1 = maxVal(tmpBoard, child, opponent, player, alpha, beta, depth+1).getValue();
            if ((v == -1000) || (v1 < v)){
                v = v1;
            }
            if(beta != -1000){
                if(v1 <= alpha){
                    return new CellValContainer(node, v);
                }
            }
            if((beta == -1000) || (v1 < beta)){
                alpha = v1;
            }
        }
        return new CellValContainer(node, v);
    }


    private void countEmUp(){
        numOccupiedWhite = numOccupiedBlack = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(board[i][j].getSymbol() == BLACK) numOccupiedBlack++;
                if(board[i][j].getSymbol() == WHITE) numOccupiedWhite++;
            }
        }
    }

    public int getNumOccupiedBlack() {
        return numOccupiedBlack;
    }

    public int getNumOccupiedWhite() {
        return numOccupiedWhite;
    }

    public int getNumOccupied() {
        return numOccupied;
    }

    protected Cell getCellAtIndex(int row, int col){
        return board[row][col];
    }

    private int cell_heur_player ( char player, char opponent, int i, int j, int depth) {
        int cost = 0;
        OthelloBoard potentialBoard = fakeMove(player, opponent, i, j);
        if (potentialBoard.isInvalidMove(i, j, player, opponent)) return 0;
        if ((potentialBoard.isWinner(player, opponent))&&
                (potentialBoard.getNumOccupiedBlack() +
                        potentialBoard.getNumOccupiedWhite() > 55)) return heur_win_bonus;
        if (potentialBoard.getBoard()[i][j].getSymbol() == player) {
            if ((i == 0 || i == 7) && (j == 0 || j == 7)) cost += heur_corner_bonus;
        }
        if (potentialBoard.getBoard()[0][1].getSymbol() == player && isInvalidMove(0, 0, opponent, player)) {
            cost += heur_corner_takeover_bonus;
        }
        if (potentialBoard.getBoard()[1][0].getSymbol() == player && isInvalidMove(0, 0, opponent, player)) {
            cost += heur_corner_takeover_bonus;
        }
        if (potentialBoard.getBoard()[1][1].getSymbol() == player && isIndexOccupied(0, 0)) {
            cost -= heur_corner_takeover_bonus;
        }
        if (potentialBoard.getBoard()[6][7].getSymbol() == player && isInvalidMove(7, 7, opponent, player)) {
            cost += heur_corner_takeover_bonus;
        }
        if (potentialBoard.getBoard()[7][6].getSymbol() == player && isInvalidMove(7, 7, opponent, player)) {
            cost += heur_corner_takeover_bonus;
        }
        if (potentialBoard.getBoard()[6][6].getSymbol() == player && isIndexOccupied(7, 7)) {
            cost -= heur_corner_takeover_bonus;
        }
        if (potentialBoard.getBoard()[1][7].getSymbol() == player && isInvalidMove(0, 7, opponent, player)) {
            cost += heur_corner_takeover_bonus;
        }
        if (potentialBoard.getBoard()[0][6].getSymbol() == player && isInvalidMove(0, 7, opponent, player)) {
            cost += heur_corner_takeover_bonus;
        }
        if (potentialBoard.getBoard()[1][6].getSymbol() == player && isIndexOccupied(0, 7)) {
            cost -= heur_corner_takeover_bonus;
        }
        if (potentialBoard.getBoard()[7][1].getSymbol() == player && isInvalidMove(7, 0, opponent, player)) {
            cost += heur_corner_takeover_bonus;
        }
        if (potentialBoard.getBoard()[6][0].getSymbol() == player && isInvalidMove(7, 0, opponent, player)) {
            cost += heur_corner_takeover_bonus;
        }
        if (potentialBoard.getBoard()[6][1].getSymbol() == player && isIndexOccupied(7, 0)) {
            cost -= heur_corner_takeover_bonus;
        }
        cost += (potentialBoard.howManyEdges(player, opponent)*heur_edge_bonus);
        if (potentialBoard.getNumOccupied() > 50) {
            cost += (countPotential(player, opponent, i, j) -
                    countPotential(opponent, player, i, j))*heur_late_game_bonus;
        }


        return cost;

    }

    public boolean isInvalidMove(int row, int col, char player, char opponent) {
        if(isIndexOccupied(row, col)) return false;
        if(countPotential(player, opponent, row, col) == 0 ) return false;
        return true;
    }

    public int howManyEdges (char player, char opponent) {
        int num = 0;
        for (int i = 0; i < 8; i++) {
            if (board[i][0].getSymbol() == player) num++;
            if (board[i][7].getSymbol() == player) num++;
            if (board[0][i].getSymbol() == player) num++;
            if (board[7][i].getSymbol() == player) num++;
        }
        return num;
    }

    public boolean isWinner (char player, char opponent) {
        if ((findPlaceableLocations(player, opponent) == null)||(getNumOccupied() >= 64)) {
            int numOccupiedPlayer = 0;
            int numOccupiedOpponent = 0;
            for(int i = 0; i < 8; i++){
                for(int j = 0; j < 8; j++){
                    if(board[i][j].getSymbol() == player) numOccupiedPlayer++;
                    if(board[i][j].getSymbol() == opponent) numOccupiedOpponent++;
                }
            }
            if (numOccupiedPlayer > numOccupiedOpponent) return true;
        }
        return false;
    }



    @Override
    public String toString() {
        String toReturn = "";
        for(int i = 0; i < 8; i++){
            toReturn += i+1 + " ";
            for(int j = 0; j < 8; j++){
                toReturn += board[i][j].getSymbol() + " ";
            }
            toReturn += "\n";
        }
        // Adding an extra space so it all lines up
        toReturn += "  ";
        for(int k = 0; k < 8; k++){
            toReturn += LETTERS.charAt(k) + " ";
        }
        return toReturn;
    }
}
