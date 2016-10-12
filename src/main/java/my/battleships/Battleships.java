package my.battleships;

import my.battleships.Players.*;

public class Battleships {

    private final Player humanPlayer = new HumanPlayer("Player");
    private final Player computerPlayer = new ComputerPlayer("Computer");
    private final ConsoleHelper console = new ConsoleHelper(this);

    Player getHumanPlayer() {
        return humanPlayer;
    }

    Player getComputerPlayer() {
        return computerPlayer;
    }

    private void run() {
        console.printGame();
        humanPlayer.addObserver(console);
        computerPlayer.addObserver(console);

        if(ConsoleHelper.askPlayerAboutHisOwnShipInstallation())
            humanPlayer.setupTheShips();

        else
            computerPlayer.setupTheShips(humanPlayer.getGameField());


        computerPlayer.setupTheShips();

        FiringResult currentPlayersFiringResult = null;
        while (true) {
            do{
                currentPlayersFiringResult = humanPlayer.makeHit(computerPlayer.getGameField());
                gameOverCheck();
            } //if we hit the mark, we have one more turn
            while (currentPlayersFiringResult == FiringResult.HURT
                    || currentPlayersFiringResult == FiringResult.DROWNED);


            do{
                ConsoleHelper.pause();
                currentPlayersFiringResult = computerPlayer.makeHit(humanPlayer.getGameField());
                gameOverCheck();
            } while(currentPlayersFiringResult == FiringResult.HURT
                    || currentPlayersFiringResult == FiringResult.DROWNED);
        }
    }

    public static void main(String[] args) {
        Battleships game = new Battleships();
        game.run();
    }

    private void gameOverCheck(){
        if(!computerPlayer.getGameField().isAtLeastOneShipAlive())
            ConsoleHelper.winMessageAndCloseTheProgram(humanPlayer.getName());
        if(!humanPlayer.getGameField().isAtLeastOneShipAlive())
            ConsoleHelper.winMessageAndCloseTheProgram(computerPlayer.getName());
    }
}
