package my.battleships.ships;

import my.battleships.Exeptions.ShipSetupException;
import my.battleships.Exeptions.WrongCoordinatesException;
import my.battleships.GameField.GameField;

public class Mine extends AbstractShip{


    public Mine(ShipTypes type, GameField.Coordinates coordinates, boolean isHorizontal, boolean hidden) throws ShipSetupException {
        super(type, coordinates, isHorizontal, hidden);
    }

    @Override
    public void hit() {
    }

    @Override
    public boolean isShipAlive() {
        return false;
    }
}
