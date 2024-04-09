import java.util.Scanner;

/**
 * A Hanabi player for testing purposes, using the console to get commands
 */
public class ConsolePlayer extends Player {
    private static final Scanner scn = new Scanner(System.in);

    @Override
    public String ask(int yourHandSize, Hand partnerHand, Board boardState) {
        // Gets the next command from the user's console input
        return scn.nextLine();
    }
}
