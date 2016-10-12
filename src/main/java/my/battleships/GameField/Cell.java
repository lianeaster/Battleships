package my.battleships.GameField;

import my.battleships.ConsoleHelper;

public class Cell {
    private CellForm form;
    protected boolean alive;

    public Cell() {
        form = CellForm.EMPTY;
        alive = true;
    }

    public CellForm getForm() {
        return form;
    }

    public boolean isAlive() {
        return alive;
    }

    public void print(){
        ConsoleHelper.writeMessage(form);
    }

    public boolean hit(){
        if(alive) {
            form = CellForm.MISSED;
            alive = false;
            return true;
        }
        else return false;
    }

}
