package my.battleships.GameField;


import my.battleships.ConsoleHelper;
import my.battleships.ships.AbstractShip;
import my.battleships.ships.ShipTypes;

public class Deck extends Cell {
    private final AbstractShip parentShip;
    private CellForm form;
    private boolean hide;
    private final GameField.Coordinates coordinates;

    public Deck(AbstractShip parentShip, ShipTypes type, GameField.Coordinates coordinates, boolean hide) {
        this.parentShip = parentShip;
        switch (type){
            case SINGLE_DECKER:
            case TWO_DECKER:
            case THREE_DECKER:
            case FOUR_DECKER: form = CellForm.DECK; break;
            case MINE: form = CellForm.MINE; break;
            default: throw new UnsupportedOperationException();
        }
        this.coordinates = coordinates;
        this.hide = hide;
    }

    public GameField.Coordinates getCoordinates(){
        return coordinates;
    }

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    public AbstractShip getParentShip() {
        return parentShip;
    }

    @Override
    public void print() {
        if (hide) super.print();
        else ConsoleHelper.writeMessage(form);
    }

    @Override
    public boolean hit(){
        if(alive){
            if (hide) hide = false;
            form = CellForm.DAMAGED;
            alive = false;
            getParentShip().hit();
            return true;
        }
        else return false;
    }
}
