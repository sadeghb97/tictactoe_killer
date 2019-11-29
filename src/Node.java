import java.util.ArrayList;
import java.util.Random;

public class Node {
    public static int NOONE_SYMBOL = 0;
    public static int MY_SYMBOL = 1;
    public static int OPPONENT_SYMBOL = 2;

    boolean isLeaf;
    int fitness = -1;
    int[] cells = new int[9];
    boolean isMaxNode;
    int fullCellsNumber;
    ArrayList<Node> children;

    Node(boolean isMaxNode) throws Exception {
        this.isMaxNode = isMaxNode;
        for(int i=0; 9>i; i++) cells[i] = NOONE_SYMBOL;
        fullCellsNumber = 0;
        isLeaf = false;
        children = new ArrayList<>();
        extend();
    }

    Node(int[] parentCells, int parentFullCellsNumber, int index, int symbole) throws Exception {
        for(int i=0; 9>i; i++) cells[i] = parentCells[i];
        if(cells[index] != NOONE_SYMBOL) throw new Exception("WrongIndex");
        if(symbole != MY_SYMBOL && symbole != OPPONENT_SYMBOL) throw new Exception("WrongSymbole");
        cells[index] = symbole;
        fullCellsNumber = parentFullCellsNumber + 1;
        isMaxNode = symbole == OPPONENT_SYMBOL;
        children = new ArrayList<>();
        extend();
    }

    public void extend() throws Exception {
        if(isFinished()) return;
        for(int i=0; 9>i; i++) {
            if(cells[i] == NOONE_SYMBOL) {
                children.add(new Node(cells, fullCellsNumber, i,
                        isMaxNode ? MY_SYMBOL : OPPONENT_SYMBOL));
            }
        }
    }

    public int whoIsWinner(){
        if(cells[0] != NOONE_SYMBOL && cells[0] == cells[1] && cells[0] == cells[2]) return cells[0];
        if(cells[3] != NOONE_SYMBOL && cells[3] == cells[4] && cells[3] == cells[5]) return cells[3];
        if(cells[6] != NOONE_SYMBOL && cells[6] == cells[7] && cells[6] == cells[8]) return cells[6];
        if(cells[0] != NOONE_SYMBOL && cells[0] == cells[3] && cells[0] == cells[6]) return cells[0];
        if(cells[1] != NOONE_SYMBOL && cells[1] == cells[4] && cells[1] == cells[7]) return cells[1];
        if(cells[2] != NOONE_SYMBOL && cells[2] == cells[5] && cells[2] == cells[8]) return cells[2];
        if(cells[0] != NOONE_SYMBOL && cells[0] == cells[4] && cells[0] == cells[8]) return cells[0];
        if(cells[2] != NOONE_SYMBOL && cells[2] == cells[4] && cells[2] == cells[6]) return cells[2];

        return NOONE_SYMBOL;
    }

    public boolean isFinished(){
        int whoIsWinner = whoIsWinner();
        if(whoIsWinner == MY_SYMBOL){
            fitness = 1 + (9 - fullCellsNumber);
            isLeaf = true;
            return true;
        }
        else if(whoIsWinner == OPPONENT_SYMBOL){
            fitness = -1 - (9 - fullCellsNumber);
            isLeaf = true;
            return true;
        }

        if(fullCellsNumber >= 9){
            fitness = 0;
            isLeaf = true;
            return true;
        }
        return false;
    }

    public void print(){
        for(int i=0; 9>i; i++) {
            if(i % 3 == 0){
                for(int j=0; 3 * 4 + 1 > j; j++)
                    StylishPrinter.print("-", StylishPrinter.BOLD_CYAN);
                System.out.println();
            }
            if(i % 3 == 0) StylishPrinter.print("| ", StylishPrinter.BOLD_CYAN);
            if(cells[i] == MY_SYMBOL) StylishPrinter.print("✕", StylishPrinter.BOLD_WHITE);
            else if(cells[i] == OPPONENT_SYMBOL) StylishPrinter.print("○", StylishPrinter.BOLD_WHITE);
            else System.out.print(" ");
            StylishPrinter.print(" | ", StylishPrinter.BOLD_CYAN);
            if((i+1) % 3 == 0) System.out.println();
        }
        for(int j=0; 3 * 4 + 1 > j; j++) StylishPrinter.print("-", StylishPrinter.BOLD_CYAN);
        System.out.println();
    }

    public int evaluate(){
        if(!isLeaf) {
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;

            for (int i = 0; children.size() > i; i++) {
                Node cnode = children.get(i);
                int f = cnode.evaluate();
                if (f < min) min = f;
                if (f > max) max = f;
            }

            if(isMaxNode) fitness = max;
            else fitness = min;
        }
        return fitness;
    }

    public Node getNextMaxNode(){
        int max = Integer.MIN_VALUE;
        ArrayList list = null;
        for(int i=0; children.size()>i; i++){
            if(children.get(i).fitness > max){
                max = children.get(i).fitness;
                list = new ArrayList();
                list.add(i);
            }
            else if(children.get(i).fitness == max){
                list.add(i);
            }
        }

        Random random = new Random();
        int randIndex = random.nextInt(list.size());
        return children.get((int) list.get(randIndex));
    }

    public Node getNextMinMode(int index) throws Exception {
        if(cells[index] != NOONE_SYMBOL) return null;
        for(int i=0; children.size()>i; i++){
            if(children.get(i).cells[index] != NOONE_SYMBOL) return children.get(i);
        }
        throw new Exception("Error!");
    }
}