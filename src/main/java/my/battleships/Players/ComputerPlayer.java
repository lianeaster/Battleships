package my.battleships.Players;

import my.battleships.Exeptions.WrongCoordinatesException;
import my.battleships.GameField.Deck;
import my.battleships.GameField.GameField;
import my.battleships.ships.*;

import java.util.*;

public class ComputerPlayer extends Player {
    private Random rand;
    private final Map<GameField.Coordinates, FiringResult> shootBase;
    private boolean finishingMode;
    private int x =0, y =0;

    public ComputerPlayer(String name) {
        super(name);
        this.name = name;
        rand = new Random(System.currentTimeMillis());
        shootBase = new LinkedHashMap<>();
        finishingMode = false;
    }


    @Override
    public void setupTheShips() {
        setupTheShips(gameField, true);
    }

    @Override
    public void setupTheShips(GameField gameField){
        setupTheShips(gameField, false);
    }

    private void setupTheShips(GameField gameField, boolean hide) {
        for (ShipTypes currentType : ShipTypes.values()) {
            int shipsCountBeforeAdding = gameField.shipsCount();
            while (gameField.shipsCount() < shipsCountBeforeAdding + currentType.getCount()){
                try {
                    GameField.Coordinates coordinates = new GameField.Coordinates(rand.nextInt(10), rand.nextInt(10));
                    switch (currentType){
                        case FOUR_DECKER:
                        case THREE_DECKER:
                        case TWO_DECKER:
                            gameField.addShip(new Ship(currentType, coordinates, rand.nextBoolean(), hide));
                            break;
                        case SINGLE_DECKER:
                            gameField.addShip(new Ship(currentType, coordinates, false, hide));
                            break;
                        case MINE:
                            gameField.addShip(new Mine(currentType, coordinates, false, hide));
                    }
                } catch (Exception ignored) {
                    continue;
                }
            }
        }
        if (!hide) {
            reprintGameWithMessage(null);
        }
    }

    @Override
    public FiringResult makeHit(GameField enemysGameField) {
        FiringResult firingResult = null;
        do{
            try {
                GameField.Coordinates coordinates = chooseCoordinatesForHit();
                firingResult = makeHit(enemysGameField, coordinates);
                reprintGameWithMessage(coordinates + " - " + firingResult);
                shootBase.put(coordinates, firingResult);
                if (firingResult == FiringResult.MINE) {
                    coordinates = mineStruck(gameField);
                    firingResult = FiringResult.MINE_SACRIFICE;
                    reprintGameWithMessage(coordinates + " - " + firingResult);
                    continue;
                }
                if(firingResult == FiringResult.HURT) finishingMode = true;
                else if (firingResult == FiringResult.DROWNED) finishingMode = false;
            }catch (WrongCoordinatesException ignored){}
        }while (firingResult == null);
        return firingResult;
    }

    private GameField.Coordinates chooseCoordinatesForHit() {
        return new GameField.Coordinates(rand.nextInt(10), rand.nextInt(10));
//        if(!finishingMode)return new GameField.Coordinates(rand.nextInt(10), rand.nextInt(10));
//        else{
//            GameField.Coordinates coordinates = null;
//            List<GameField.Coordinates> keyList = new ArrayList<>(shootBase.keySet());
//            for (int i = keyList.size()-1 ; i>=0; i--) {
//
//            }
//            return coordinates;
//        }
    }

    protected GameField.Coordinates chooseSacrificeAfterMineExplosion() {
        for(AbstractShip currentShip : gameField.getShips()){
            if(currentShip.isShipAlive())
                for(Deck currentDeck : currentShip.getShipDecks())
                    if(currentDeck.isAlive()) return currentDeck.getCoordinates();
        }
        return null;
    }


}