package my.battleships.GameField;

import my.battleships.ConsoleHelper;
import my.battleships.Exeptions.ShipSetupException;
import my.battleships.Exeptions.WrongCoordinatesException;
import my.battleships.Players.FiringResult;
import my.battleships.ships.*;


import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

public class GameField extends Observable{
    private final List<AbstractShip> ships;
    private final Cell[][] field;
    private boolean atLeastOneShipAlive;

    public GameField() {
        ships = new LinkedList<AbstractShip>();
        field = new Cell[10][10];
        for (int i = 0; i < 10 ; i++)
            for (int j = 0; j < 10 ; j++)
                field [i][j] = new Cell();
        atLeastOneShipAlive = true;
    }

    public void addShip(AbstractShip  shipForAdd) throws ShipSetupException {
        if (checkShipConfluence(shipForAdd) && checkDimensionBetweenTwoShips(shipForAdd)){
            ships.add(shipForAdd);
            for (Deck currentDeck : shipForAdd.getShipDecks())
                field[currentDeck.getCoordinates().x][currentDeck.getCoordinates().y] = currentDeck;
        }
        else throw new ShipSetupException();
    }

    //must be checked every deck in every ship witch setup in Game field with every deck of new ship
    //by the formula Euclidean distance
    private boolean checkDimensionBetweenTwoShips(AbstractShip checkedShip){
        for(AbstractShip currentShip : ships)
            for(Deck currentDeck : currentShip.getShipDecks())
                for (Deck checkedDeck : checkedShip.getShipDecks())
                    if(Math.sqrt( Math.pow(checkedDeck.getCoordinates().x - currentDeck.getCoordinates().x, 2) +
                            Math.pow(checkedDeck.getCoordinates().y - currentDeck.getCoordinates().y, 2) ) < 2) return false;
        return true;
    }


    public boolean isAtLeastOneShipAlive() {
        return atLeastOneShipAlive;
    }

    private boolean checkShipConfluence(AbstractShip checkedShip){
        for(Deck currentDeck : checkedShip.getShipDecks())
            if (field[currentDeck.getCoordinates().x][currentDeck.getCoordinates().y] instanceof Deck) return false;
        return true;

    }

    private void checkShipsLife (){
        atLeastOneShipAlive = false;
        for(AbstractShip currentShip : ships)
            atLeastOneShipAlive = atLeastOneShipAlive | currentShip.isShipAlive();
    }

    public FiringResult checkFireResult(Coordinates coordinates){
        return checkFireResult(coordinates.x, coordinates.y);
    }

    public FiringResult checkFireResult(int x, int y){
        if(!(field[x][y] instanceof Deck)) return FiringResult.MISS;
        else {
            if (((Deck)field[x][y]).getParentShip().getType() == ShipTypes.MINE) return FiringResult.MINE;
            else if(((Deck)field[x][y]).getParentShip().isShipAlive()) return FiringResult.HURT;
            else return FiringResult.DROWNED;
        }
    }

    public Cell getFieldCell(Coordinates coordinates) {
        return getFieldCell(coordinates.x, coordinates.y);
    }

    public Cell getFieldCell(int x, int y) {
        return field[y][x];
    }

    public List<AbstractShip> getShips() {
        return ships;
    }

    public FiringResult hit(Coordinates coordinates) throws WrongCoordinatesException {
        return hit(coordinates.x, coordinates.y);
    }

    public FiringResult hit(int x, int y) throws WrongCoordinatesException {
        boolean isHitWasSuccessful = field[x][y].hit();
        if (isHitWasSuccessful){
            checkShipsLife();
            return checkFireResult(x, y);
        }
        else throw new WrongCoordinatesException();
    }

    public int shipsCount(){
        return ships.size();
    }

    public void cheats (){
        for(AbstractShip ship : ships)
            for(Deck deck : ship.getShipDecks())
                if (deck.isAlive())deck.setHide(!deck.isHide());


    }

    public boolean isRightMineSacrifice (Coordinates coordinates) throws WrongCoordinatesException {
        if(field[coordinates.x][coordinates.y] instanceof Deck &&
                field[coordinates.x][coordinates.y].isAlive() &&
                ((Deck)field[coordinates.x][coordinates.y]).getParentShip().getType() != ShipTypes.MINE) return true;
        else throw new WrongCoordinatesException();
    }

    public static class Coordinates{
        private int x;
        private int y;

        public Coordinates(int x, int y){
            this.x = x;
            this.y = y;
        }

        //it need for setup new ships with horizontal and vertical direction
        public Coordinates moveX(int dx) throws ShipSetupException {
            int newX = x + dx;
            if(newX < 0 || newX > 9 ) throw new ShipSetupException();
            return new Coordinates(newX, y);
        }

        public Coordinates moveY(int dy) throws ShipSetupException {
            int newY = y + dy;
            if(newY < 0 || newY > 9) throw new ShipSetupException();
            return new Coordinates(x, newY);
        }

        public static Coordinates coordinatesParser(String stringCoordinates) throws WrongCoordinatesException {
            if (!stringCoordinates.matches(("(^[a-j]\\d$)|(^[a-j]\\d[v]$)"))) throw new WrongCoordinatesException();
            int x = (ConsoleHelper.letters.indexOf(stringCoordinates.charAt(0))-1)/2;
            int y = Character.getNumericValue(stringCoordinates.charAt(1));
            return new Coordinates(x, y);
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Coordinates)) return false;

            Coordinates that = (Coordinates) o;

            if (x != that.x) return false;
            return y == that.y;

        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }

        @Override
        public String toString() {
            return "" + ConsoleHelper.letters.charAt(x * 2 + 1) + y;
        }
    }
}
