package my.battleships;

import my.battleships.Players.ComputerPlayer;
import my.battleships.Players.Player;
import my.battleships.ships.ShipTypes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Observer;

public class ConsoleHelper implements Observer{
    public static final String letters = " a b c d e f g h i j";
    private static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    private Battleships game;

    ConsoleHelper(Battleships game) {
        this.game = game;
    }

    //write message with new line (NL)
    public static void writeMessageNL(Object message) {
        System.out.println(message);
    }
    //write message without new line
    public static void writeMessage(Object message) {
        System.out.print(message);
    }

    public static String readString() {
        String readString = null;
        try {
            readString = bufferedReader.readLine();
        } catch (IOException e) {
            writeMessageNL("FATAL ERROR " + Thread.currentThread().getName() + " : " + e.getMessage());
            e.printStackTrace();
        }
        return readString;
    }

    void printGame() {
        printGame(null);
    }
    void printGame(String message) {
        writeMessageNL("\n\n  " + letters + "           " + letters);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 20; j++) {
                writeMessage(' ');
                if(j == 10) writeMessage("         ");
                if(j==0 || j == 10) writeMessage(i + " ");
                if(j < 10) game.getHumanPlayer().getGameField().getFieldCell(i, j).print();
                else game.getComputerPlayer().getGameField().getFieldCell(i, j-10).print();
            }
            writeMessageNL("");
        }
        if (message != null) writeMessageNL("\n" + message);

    }

    public void update(Observable o, Object arg) {
        if (arg != null) printGame("\n"+((Player)o).getName() + ", " + arg);
        else printGame();
        if (o instanceof ComputerPlayer) pause();
    }

    static void pause(){
        ConsoleHelper.writeMessageNL("Enter - continue");
        ConsoleHelper.readString();

    }

    static boolean askPlayerAboutHisOwnShipInstallation(){
        writeMessageNL("Do you want to setup your ships by yourself? Y/y - for \"yes\"");
        if (readString().matches("[Yy].*")) return true;
        return false;
    }

    static void winMessageAndCloseTheProgram(String playersName){
        writeMessageNL("GAME OVER! " + playersName +  " WIN!");
        System.exit(0);
    }

    public static void messageForShipsSetup(ShipTypes shipType){
        writeMessageNL("\nOk, " + shipType.getName()+ " will be at... \n" +
                "(\"d3\" - on \"d3\" will be head, default direction is horizontal, \"d3v\" for vertical)");
    }
}
