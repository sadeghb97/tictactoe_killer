import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        while (mainMenu());
    }

    public static boolean mainMenu(){
        StylishPrinter.println("\nMenu:", StylishPrinter.BOLD_RED);
        System.out.println("1: Play TicTakToe (You are the first)");
        System.out.println("2: Play TicTakToe (CPU is the first)");
        System.out.println("3: Exit");
        System.out.print("\nEnter Your Choice: ");
        int choice = SBProScanner.inputInt(1, 3);

        if(choice==1) playTicTakToe(false);
        else if(choice == 2) playTicTakToe(true);
        else if(choice == 3) return false;
        return true;
    }

    public static void playTicTakToe(boolean isMaxNode){
        System.out.println();
        try {
            Node rootNode = new Node(isMaxNode);
            System.out.println("Game Started: ");
            rootNode.print();
            System.out.println();

            rootNode.evaluate();
            Node currentNode = rootNode;
            while(!currentNode.isLeaf){
                if(currentNode.isMaxNode) {
                    System.out.println("CPU Turn: ");
                    currentNode = currentNode.getNextMaxNode();
                }
                else {
                    System.out.print("Enter your move: ");
                    int choice = SBProScanner.inputInt(1, 9);
                    choice--;
                    System.out.println("\nYour Turn: ");
                    currentNode = currentNode.getNextMinMode(choice);
                }
                currentNode.print();
                System.out.println();
            }

            int whoIsWinner = currentNode.whoIsWinner();
            if(whoIsWinner == Node.MY_SYMBOLE){
                StylishPrinter.println("Sorry I win :)",
                        StylishPrinter.BOLD_WHITE, StylishPrinter.BG_GREEN);
            }
            else if(whoIsWinner == Node.OPPONENT_SYMBOLE){
                StylishPrinter.println("Ooops You are the winner :(",
                        StylishPrinter.BOLD_WHITE, StylishPrinter.BG_RED);
            }
            else {
                StylishPrinter.println("OK Its a draw :(",
                        StylishPrinter.BOLD_BLACK, StylishPrinter.BG_WHITE);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            StylishPrinter.print("Error: ", StylishPrinter.RED);
            System.out.println(e.getMessage());
        }
    }
}
