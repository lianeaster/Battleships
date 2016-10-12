package my.battleships.Players;

import my.battleships.ConsoleHelper;
import my.battleships.Exeptions.*;
import my.battleships.GameField.GameField;
import my.battleships.ships.*;




public class HumanPlayer extends Player {
    public HumanPlayer(String name) {
        super(name);
    }

    @Override
    public void setupTheShips() {
        setupTheShips(this.gameField);
    }

    @Override
    public void setupTheShips(GameField gameField) {
        for (ShipTypes currentType : ShipTypes.values()) {
            int shipsCountBeforeAdding = gameField.shipsCount();
            while (gameField.shipsCount() < shipsCountBeforeAdding + currentType.getCount()){
                ConsoleHelper.messageForShipsSetup(currentType);
                try {
                    String buffer = ConsoleHelper.readString();
                    GameField.Coordinates coordinates = GameField.Coordinates.coordinatesParser(buffer);
                    switch (currentType){
                        case FOUR_DECKER:
                        case THREE_DECKER:
                        case TWO_DECKER:
                            gameField.addShip(new Ship(currentType, coordinates,
                                    buffer.length() == 2, false));
                            break;
                        case SINGLE_DECKER:
                            gameField.addShip(new Ship(currentType, coordinates, false, false));
                            break;
                        case MINE:
                            gameField.addShip(new Mine(currentType, coordinates, false, false));
                    }
                    reprintGameWithMessage(null);
                } catch (ShipSetupException e) {
                    reprintGameWithMessage("you can't setup near or ON existing ships or shipis out of border." +
                            "\nPlease, choose another place...");
                }catch (WrongCoordinatesException e){
                    reprintGameWithMessage("coordinates' format is wrong! \n" +
                            "You must use [a-j] letters, and format must be \"a8\", \"c7v\", etc.");
                }
            }
        }
    }

    @Override
    public FiringResult makeHit(GameField enemysGameField) {
        FiringResult result = null;
        do{
            try {
                ConsoleHelper.writeMessageNL("\nLet's SHOOT now!! Your target:");
                String buffer = ConsoleHelper.readString();
                if(buffer.equals("cheats")) {
                    enemysGameField.cheats();
                    reprintGameWithMessage(null);
                    continue;
                }
                GameField.Coordinates coordinates = GameField.Coordinates.coordinatesParser(buffer);
                result = makeHit(enemysGameField, coordinates);
                reprintGameWithMessage(coordinates + " - " + result);
                if(result == FiringResult.MINE) {
                    coordinates = mineStruck(gameField);
                    result = FiringResult.MINE_SACRIFICE;
                    reprintGameWithMessage(coordinates + " - " + result);
                }
            } catch (WrongCoordinatesException e) {
                reprintGameWithMessage("wrong coordinates. Try another..");
            }
        }while (result == null);
        return result;
    }

    @Override
    protected GameField.Coordinates chooseSacrificeAfterMineExplosion() {
        boolean isRightCoordinates = false;
        GameField.Coordinates coordinates = null;
        do{
            try {
                ConsoleHelper.writeMessageNL("Choose ship or ship's deck which struck the mine:");
                coordinates = GameField.Coordinates.coordinatesParser(ConsoleHelper.readString());
                isRightCoordinates = gameField.isRightMineSacrifice(coordinates);
            } catch (WrongCoordinatesException e) {
                super.setChanged();
                super.notifyObservers("wrong sacrifice coordinates. Try another..");
            }
        }while (!isRightCoordinates);
        return coordinates;
    }

}
