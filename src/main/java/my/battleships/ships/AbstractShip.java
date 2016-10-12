package my.battleships.ships;


import my.battleships.Exeptions.ShipSetupException;
import my.battleships.Exeptions.WrongCoordinatesException;
import my.battleships.GameField.Deck;
import my.battleships.GameField.GameField;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractShip {
    private final ShipTypes Type;
    private final List<Deck> ShipDecks;
    protected boolean alive;

    AbstractShip(ShipTypes type, GameField.Coordinates coordinates, boolean isHorizontal, boolean hidden) throws ShipSetupException {
        Type = type;
        ShipDecks = new LinkedList<Deck>();
        for (int i = 0; i < type.getDecks(); i++) {
            ShipDecks.add(new Deck(this, type, isHorizontal ? coordinates.moveX(i) : coordinates.moveY(i), hidden));
        }
    }

    public ShipTypes getType() {
        return Type;
    }

    public LinkedList<Deck> getShipDecks() {
        return (LinkedList<Deck>) ShipDecks;
    }

    public boolean isShipAlive(){
        return alive;
    }

    abstract public void hit();
}
