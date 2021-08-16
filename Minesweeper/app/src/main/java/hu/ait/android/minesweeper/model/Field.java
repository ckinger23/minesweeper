package hu.ait.android.minesweeper.model;

public class Field {

    private boolean isMine = false;
    private boolean wasTouched = false;
    private boolean isFlagged = false;


    public String surroundingTotal;


    public Field() {
    }

    public Field(boolean isMine, boolean wasTouched, boolean isFlagged) {
        this.isMine = isMine;
        this.wasTouched = wasTouched;
        this.isFlagged = isFlagged;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public boolean isWasTouched() {
        return wasTouched;
    }

    public void setWasTouched(boolean wasTouched) {
        this.wasTouched = wasTouched;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }

    public String getSurroundingTotal() {
        return surroundingTotal;
    }

    public void setSurroundingTotal(String surroundingTotal) {
        this.surroundingTotal = surroundingTotal;
    }
}
