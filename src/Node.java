import java.util.ArrayList;

public class Node {
    public static int NOONE_SYMBOLE = 0;
    public static int MY_SYMBOLE = 1;
    public static int OPPONENT_SYMBOLE = 2;

    boolean isLeaf;
    int fitness = -1;
    int[] cells = new int[9];
    boolean isMaxNode;
    int fullCellsNumber;
    ArrayList<Node> children;

    Node(boolean isMaxNode) throws Exception {
        this.isMaxNode = isMaxNode;
        for(int i=0; 9>i; i++) cells[i] = NOONE_SYMBOLE;
        fullCellsNumber = 0;
        isLeaf = false;
        children = new ArrayList<>();
        extend();
    }

    Node(int[] parentCells, int parentFullCellsNumber, int index, int symbole) throws Exception {
        for(int i=0; 9>i; i++) cells[i] = parentCells[i];
        if(cells[index] != NOONE_SYMBOLE) throw new Exception("WrongIndex");
        if(symbole != MY_SYMBOLE && symbole != OPPONENT_SYMBOLE) throw new Exception("WrongSymbole");
        cells[index] = symbole;
        fullCellsNumber = parentFullCellsNumber + 1;
        isMaxNode = symbole == OPPONENT_SYMBOLE;
        children = new ArrayList<>();
        extend();
    }

    public void extend() throws Exception {
        if(isFinished()) return;
        for(int i=0; 9>i; i++) {
            if(cells[i] == NOONE_SYMBOLE) {
                children.add(new Node(cells, fullCellsNumber, i,
                        isMaxNode ? MY_SYMBOLE : OPPONENT_SYMBOLE));
            }
        }
    }

    public int whoIsWinner(){
        if(cells[0] != NOONE_SYMBOLE && cells[0] == cells[1] && cells[0] == cells[2]) return cells[0];
        if(cells[3] != NOONE_SYMBOLE && cells[3] == cells[4] && cells[3] == cells[5]) return cells[3];
        if(cells[6] != NOONE_SYMBOLE && cells[6] == cells[7] && cells[6] == cells[8]) return cells[6];
        if(cells[0] != NOONE_SYMBOLE && cells[0] == cells[3] && cells[0] == cells[6]) return cells[0];
        if(cells[1] != NOONE_SYMBOLE && cells[1] == cells[4] && cells[1] == cells[7]) return cells[1];
        if(cells[2] != NOONE_SYMBOLE && cells[2] == cells[5] && cells[2] == cells[8]) return cells[2];
        if(cells[0] != NOONE_SYMBOLE && cells[0] == cells[4] && cells[0] == cells[8]) return cells[0];
        if(cells[2] != NOONE_SYMBOLE && cells[2] == cells[4] && cells[2] == cells[6]) return cells[2];

        return NOONE_SYMBOLE;
    }

    public boolean isFinished(){
        int whoIsWinner = whoIsWinner();
        if(whoIsWinner == MY_SYMBOLE){
            fitness = 1 + (9 - fullCellsNumber);
            isLeaf = true;
            return true;
        }
        else if(whoIsWinner == OPPONENT_SYMBOLE){
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
            if(cells[i] == MY_SYMBOLE) StylishPrinter.print("✕", StylishPrinter.BOLD_WHITE);
            else if(cells[i] == OPPONENT_SYMBOLE) StylishPrinter.print("○", StylishPrinter.BOLD_WHITE);
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
        int index = -1;
        for(int i=0; children.size()>i; i++){
            if(children.get(i).fitness > max){
                max = children.get(i).fitness;
                index = i;
            }
        }

        return children.get(index);
    }

    public Node getNextMinMode(int index) throws Exception {
        for(int i=0; children.size()>i; i++){
            if(children.get(i).cells[index] != NOONE_SYMBOLE) return children.get(i);
        }
        throw new Exception("Error!");
    }
}