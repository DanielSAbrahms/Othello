import javafx.scene.control.Button;

public class OthelloBoard {
    private static String LETTERS = "ABCDEFGH";
    private static char BLACK = 'b';
    private static char WHITE = 'w';
    private static char EMPTY = '-';
    private static char INVALID = '\0';
    private Cell[][] board;
    private int numOccupied;
    private int numOccupiedBlack;
    private int numOccupiedWhite;

    public OthelloBoard() {
        this.board = new Cell[8][8];
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                board[i][j] = (new Cell(new Coordinates(i,j), '-', false));
            }
        }
        this.numOccupied = 0;
    }

    public OthelloBoard(Cell[][] cellArr){
        this.board = cellArr;
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
