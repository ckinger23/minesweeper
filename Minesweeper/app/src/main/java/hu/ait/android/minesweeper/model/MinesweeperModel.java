package hu.ait.android.minesweeper.model;

import java.util.Random;

public class MinesweeperModel {

    public static int numRowCols = 7; //able to manipulate size of board
    public static int numBombs = 8; //able to manipulate amount of bombs
    private static int numFlags = numBombs; //numFlags always equal to numBombs

    private Field[][] fields = new Field[numRowCols][numRowCols]; //matrix of specific size

    private boolean flagMode = false;
    private static MinesweeperModel minesweeperModel = null;
    private Random random = new Random();


    public static MinesweeperModel getInstance() {
        if (minesweeperModel == null) {
            minesweeperModel = new MinesweeperModel();
        }
        return minesweeperModel;
    }


    public static int getNumFlags() {
        return numFlags;
    }


    public void setNumFlags(int numFlags) {
        this.numFlags = numFlags;
    }


    private MinesweeperModel() { //fill specific-sized board with new Field()
        for (int i = 0; i < numRowCols; i++) {
            for (int j = 0; j < numRowCols; j++) {
                fields[i][j] = new Field();
            }
        }
        setupBoard();
    }

    private void setupBoard() {
        setBombs();
        calculateBoard();
    }


    public void setBombs() {
        int i = 0;
        while (i < numBombs) {
            int bombX = random.nextInt(numRowCols);
            int bombY = random.nextInt(numRowCols);
            if (fields[bombX][bombY].isMine() == false) {
                fields[bombX][bombY].setMine(true);
                i++;
            }
        }
    }


    public void calculateBoard() {

        for (int i = 0; i < numRowCols; i++) {
            for (int j = 0; j < numRowCols; j++) {
                if (fields[i][j].isMine() == false) {
                    Integer total = (sumSurrounding(i, j));
                    fields[i][j].setSurroundingTotal(Integer.toString(total));//String of # bombs in vicinity
                }
            }
        }
    }


    public int sumSurrounding(int i, int j) {
        int first = calculateSquare(i - 1, j - 1);
        int second = calculateSquare(i - 1, j);
        int third = calculateSquare(i - 1, j + 1);
        int fourth = calculateSquare(i, j - 1);
        int fifth = calculateSquare(i, j + 1);
        int sixth = calculateSquare(i + 1, j - 1);
        int seventh = calculateSquare(i + 1, j);
        int eighth = calculateSquare(i + 1, j + 1);
        int total = first + second + third + fourth + fifth + sixth + seventh + eighth;
        return total;
        //take the 8 surrounding squares and add up all of the bombs
    }


    public int calculateSquare(int x, int y) {
        if (x >= 0 && x < numRowCols && y >= 0 && y < numRowCols && fields[x][y].isMine()) {  //each square in vicinity containing bomb returns a 1
            return 1;
        }
        return 0;
    }


    public boolean hasPlayerWon() {
        for (int i = 0; i < numRowCols; i++) {
            for (int j = 0; j < numRowCols; j++) {
                if (fields[i][j].isMine()) {
                    if (fields[i][j].isFlagged() == false) {  //if there is a mine that is not flagged == not Won
                        return false;
                    }
                }
            }
        }
        return true;
    }


    public Field getFieldsContent(int x, int y) {
        return fields[x][y];
    }


    public boolean isFieldMine(int x, int y) {
        return fields[x][y].isMine();
    }


    public boolean isFieldFlagged(int x, int y) {
        return fields[x][y].isFlagged();
    }


    public boolean isWasTouched(int x, int y) {
        return fields[x][y].isWasTouched();
    }


    public void resetFields() {
        for (int i = 0; i < numRowCols; i++) {
            for (int j = 0; j < numRowCols; j++) {
                fields[i][j] = new Field();
            }
        }
        setupBoard();
    }

    public boolean isFlagMode() {
        return flagMode;
    }

    public void setFlagMode(boolean flagMode) {
        this.flagMode = flagMode;
    }
}













