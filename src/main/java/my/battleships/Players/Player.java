package my.battleships.Players;

import my.battleships.Exeptions.WrongCoordinatesException;
import my.battleships.GameField.GameField;

import java.util.Observable;

public abstract class Player extends Observable{

    protected String name;
    protected final GameField gameField;

    public Player(String name) {
        this.name = name;
        this.gameField = new GameField();
    }

    public String getName() {
        return name;
    }

    public GameField getGameField() {
        return gameField;
    }

    protected void reprintGameWithMessage(String message){
        super.setChanged();
        super.notifyObservers(message);
    }

    //setup ships on player's oun field
    public abstract void setupTheShips();

    //setup ships to another specified field
    public abstract void setupTheShips(GameField gameField);

    public abstract FiringResult makeHit(GameField enemysGameField);

    FiringResult makeHit(GameField enemysGameField, GameField.Coordinates coordinates) throws WrongCoordinatesException {
        return enemysGameField.hit(coordinates);
    }

    protected abstract GameField.Coordinates chooseSacrificeAfterMineExplosion ();

    GameField.Coordinates mineStruck(GameField gameField) throws WrongCoordinatesException {
        GameField.Coordinates coordinates = chooseSacrificeAfterMineExplosion();
        makeHit(gameField, coordinates);
        return coordinates;
    }


}
