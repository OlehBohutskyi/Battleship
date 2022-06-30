package battleship;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args)  {
        int[][] fieldFirstPlayer = new int[10][10];
        int[][] fieldSecondPlayer = new int[10][10];
        int counterFirst = 17;
        int counterSecond = 17;

        Scanner scanner = new Scanner(System.in);

        System.out.println("Player 1, place your ships on the game field");
        placeYourShips(fieldFirstPlayer);

        System.out.println("Press Enter and pass the move to another player");
        while(!(scanner.nextLine()).isEmpty()) {}
        clearConsole();

        System.out.println("Player 2, place your ships to the game field");
        placeYourShips(fieldSecondPlayer);

        System.out.println("Press Enter and pass the move to another player");
        while(!(scanner.nextLine()).isEmpty()) {}
        clearConsole();

        while (true) {
            counterSecond = shot(fieldSecondPlayer, fieldFirstPlayer, counterSecond, 1);

            if (counterSecond == 0) {
                break;
            }

            System.out.println("Press Enter and pass the move to another player");
            while(!(scanner.nextLine()).isEmpty()) {}
            clearConsole();

            counterFirst = shot(fieldFirstPlayer, fieldSecondPlayer, counterFirst, 2);

            if (counterFirst == 0) {
                break;
            }

            System.out.println("Press Enter and pass the move to another player");
            while(!(scanner.nextLine()).isEmpty()) {}
            clearConsole();


        }

//        if (counterFirst == 0) {
//            System.out.println("Player 2 won!");
//        }
//        if (counterSecond == 0){
//            System.out.println("Player 1 won!");
//        }
        System.out.println("You sank the last ship. You won. Congratulations!");
    }

    public static void clearConsole()
    {
        try
        {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows"))
            {
                Runtime.getRuntime().exec("cls");
            }
            else
            {
                Runtime.getRuntime().exec("clear");
            }
        }
        catch (final Exception e)
        {
            //  Handle any exceptions.
        }
    }

    public static int shot(int[][] fieldFirst, int[][] fieldSecond, int counter, int player) {
        Scanner scanner = new Scanner(System.in);
        printField(fieldFirst, true);
        System.out.println("---------------------");
        printField(fieldSecond, false);
        System.out.printf("%nPlayer %d, it's your turn:%n", player);


        int[] coord = readCoords(scanner);
        while (coord[0] == -1 || coord[1] == -1) {
            System.out.println("\nError! You entered the wrong coordinates! Try again:\n");
            coord = readCoords(scanner);
        }
        if (fieldFirst[coord[0]][coord[1]] != 0) {
            if(fieldFirst[coord[0]][coord[1]] == 1) counter--;
            fieldFirst[coord[0]][coord[1]] = 2;
            if (checkIfShipSank(fieldFirst, coord)) {
                System.out.println("You sank a ship!\n");
            } else {
                System.out.println("You hit a ship!\n");
            }

        } else if (fieldFirst[coord[0]][coord[1]] != 1) {
            fieldFirst[coord[0]][coord[1]] = 3;
            System.out.println("You missed!\n");
        }
        return counter;
    }

    public static boolean checkIfShipSank(int[][] field, int[] coords) {
        final int x = coords[0];
        final int y = coords[1];

        int x1Ship = x;
        int y1Ship = y;
        int x2Ship = x;
        int y2Ship = y;

        while (x1Ship - 1 >= 0 && (field[x1Ship - 1][y1Ship] == 1 || field[x1Ship - 1][y1Ship] == 2)) {
            x1Ship--;
        }

        while (x2Ship + 1 <= 9 && (field[x2Ship + 1][y2Ship] == 1 || field[x2Ship + 1][y2Ship] == 2)) {
            x2Ship++;
        }

        while (y1Ship - 1 >= 0 && (field[x1Ship][y1Ship - 1] == 1 || field[x1Ship][y1Ship - 1] == 2)) {
            y1Ship--;
        }

        while (y2Ship + 1 <= 9 && (field[x2Ship][y2Ship + 1] == 1 || field[x2Ship][y2Ship + 1] == 2)) {
            y2Ship++;
        }

        if (x1Ship == x2Ship) {
            for (int i = y1Ship; i <= y2Ship; i++) {
                if (field[x1Ship][i] == 1) return false;
            }
        } else {
            for (int i = x1Ship; i <= x2Ship; i++) {
                if (field[i][y1Ship] == 1) return false;
            }
        }

        return true;
    }

    public static void placeYourShips(int [][] field) {

        String[] ships = new String[] {"Aircraft Carrier", "Battleship", "Submarine", "Cruiser", "Destroyer"};
        int[] shipsLength = new int[] {5, 4, 3, 3, 2};
        int[] buf;
        boolean checkPrintField = true;

        for (int i = 0; i < ships.length; i++) {
            if (checkPrintField) {
                System.out.println();
                printField(field, false);
                System.out.printf("Enter the coordinates of the %s (%d cells):%n%n", ships[i], shipsLength[i]);
            }
            checkPrintField = true;

            Scanner scanner = new Scanner(System.in);
            int[] coords1 = readCoords(scanner);
            int[] coords2 = readCoords(scanner);
            if (coords1[0] > coords2[0] || coords1[1] > coords2[1]) {
                buf = coords1;
                coords1 = coords2;
                coords2 = buf;
            }


            if (!checkCoords(coords1, coords2)){
                System.out.println("\nError! Wrong ship location! Try again:\n");
                checkPrintField = false;
                i--;
                continue;
            }
            if (!checkPlaceIsFree(field, coords1, coords2)){
                System.out.println("\nError! You placed it too close to another one. Try again:\n");
                checkPrintField = false;
                i--;
                continue;
            }

            if (!checkShipLength(coords1, coords2, shipsLength[i])) {
                System.out.printf("%nError! Wrong length of the %s! Try again:%n%n", ships[i]);
                checkPrintField = false;
                i--;
                continue;
            }
            placeShip(field, coords1, coords2);
        }
        System.out.println();
        printField(field, false);
    }

    public static void printField(int[][] field, boolean hidden) {
        final String[] numberCoords = {" ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        final String[] letterCoords = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        for (int i = 0; i < numberCoords.length; i++) {
            if (i != numberCoords.length - 1) {
                System.out.print(numberCoords[i] + " ");
            } else {
                System.out.print(numberCoords[i]);
            }
        }
        System.out.println();
        for (int i = 0; i < letterCoords.length; i++){
            System.out.print(letterCoords[i] + " ");
            for (int j = 0; j < letterCoords.length; j++){
                switch (field[i][j]) {
                    case 0:
                        if (j != letterCoords.length - 1) {
                            System.out.print("~ ");
                        } else {
                            System.out.print("~");
                        }
                        break;
                    case 1:
                        if (!hidden) {
                            if (j != letterCoords.length - 1) {
                                System.out.print("O ");
                            } else {
                                System.out.print("O");
                            }
                            break;
                        } else {
                            if (j != letterCoords.length - 1) {
                                System.out.print("~ ");
                            } else {
                                System.out.print("~");
                            }
                        }
                        break;
                    case 2:
                        if (j != letterCoords.length - 1) {
                            System.out.print("X ");
                        } else {
                            System.out.print("X");
                        }
                        break;
                    case 3:
                        if (j != letterCoords.length - 1) {
                            System.out.print("M ");
                        } else {
                            System.out.print("M");
                        }
                        break;
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public static int[] readCoords(Scanner scanner) {
        String coords = scanner.next();
        char letter = coords.charAt(0);
        int digit2 = Integer.parseInt(coords.substring(1));
        int digit1 = letterToCoord(letter);
        digit2--;

        if (digit1 < 0 || digit1 > 9 || digit2 < 0 || digit2 > 9) {
            return new int[] {-1, -1};
        }

        return new int[] {digit1, digit2};
    }

    public static int letterToCoord(char letter) {
        final int offset = 65;
        return letter - offset;
    }

    public static boolean checkPlaceIsFree(int[][] field, int[] coord1, int[] coord2) {
        int[] check1 = new int[2];
        int[] check2 = new int[2];

        check1[0] = coord1[0] - 1 <= 0 ? coord1[0] : coord1[0] - 1;
        check1[1] = coord1[1] - 1 <= 0 ? coord1[1] : coord1[1] - 1;
        check2[0] = coord2[0] + 1 >= 9 ? coord2[0] : coord2[0] + 1;
        check2[1] = coord2[1] + 1 >= 9 ? coord2[1] : coord2[1] + 1;


        for (int i = check1[0]; i <= check2[0]; i++) {
            for (int j = check1[1]; j <= check2[1]; j++) {
                if (field[i][j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkShipLength(int[] coord1, int[] coord2, int length) {
        return coord2[0] - coord1[0] + 1 == length || coord2[1] - coord1[1] + 1 == length;
    }

    public static boolean checkCoords(int[] coord1, int[] coord2) {
        if (coord1[0] == coord2[0]) {
            return true;
        } else return coord1[1] == coord2[1];
    }

    public static void placeShip(int[][] field, int[] coords1, int[] coords2) {
        for (int i = coords1[0]; i <= coords2[0]; i++) {
            for (int j = coords1[1]; j <= coords2[1]; j++) {
                field[i][j] = 1;
            }
        }
    }


}
