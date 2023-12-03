import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.List;
import java.io.File;
import java.lang.StringBuilder;
import java.util.Scanner;
import java.time.*;
import java.util.Random;

/**
 * @author Adam Schroeder, Owen Suelflow
 * Sudoku Algorithm
 * Capable of creating and solving Sudoku puzzles using algorithm developed by Peter Norvig
 * https://norvig.com/sudoku.html
 */
public class BasicSudoku{
    private double constraintThreeCounter = 0;
    private int randomAssignmentCounter = 0;
    private int constraintTwoCounter = 0;
    private int constraintOneCounter = 0;
    private static final String ROWS_STRING = "123456789";
    private static final String COLS_STRING = "123456789";
    private static final HashSet<String> DIGITS = makeDigits();
    private static final String[][] SQUARES = createSquares();
    private static final HashMap<Integer, HashSet<String>> BOXES = createBoxes();
    private static final HashMap<Integer, HashSet<String>> ROWS = createRows();
    private static final HashMap<Integer, HashSet<String>> COLS = createCols();
    private static final HashMap<String, HashSet<String>> PEERS = createPeers();

    /**
     * ex. SQUARES[1][5] = "15"
     * @return Double Array of Strings
     */
    public String[][] getSquareTags() {
        return SQUARES;
    }

    /**
     * Initializes the set of possible digits
     * @return Set of String digits 1-9
    */
    private static HashSet<String> makeDigits(){
        HashSet<String> d = new HashSet<>();
        d.add("1");
        d.add("2");
        d.add("3");
        d.add("4");
        d.add("5");
        d.add("6");
        d.add("7");
        d.add("8");
        d.add("9");
        return d;
    }

    /**
     * Initialize Squares array
     * @return Matrix (Double Array) of Squares represented with String tag "11" and "12" and so on
     */
    private static String[][] createSquares(){
        int R = -1;
        int C = -1;
        String[][] temp = new String[9][9];
        for(String r : ROWS_STRING.split("")){
            R ++;
            for(String c : COLS_STRING.split("")){
                C ++;
                temp[R][C] = r.concat(c);
            }
            C = -1;
        }
        return temp;
    }

    /**
     * Initialize boxes map, which maps each box number to a set of cells belonging to it
     * 1st box has cells 11, 12, 13, 21, 22, 23, 31, 32, 33
     * @return HashMap mapping integers to the correpsonding square's information (HashSet<String>) in that box
     */
    private static HashMap<Integer, HashSet<String>> createBoxes(){
        HashMap<Integer, HashSet<String>> map = new HashMap<>();
        for(int i = 1; i < 10; i++){
            map.put(i, new HashSet<>());
        }
        for(int r = 0; r < 9; r ++){
            for(int c = 0; c < 9; c ++){
                int box = calcBox(r+1,c+1);
                map.get(box).add(SQUARES[r][c]);
            }
        }
        return map;
    }

    /**
     * Calculates the box in which a cell with row # = row and col # = col belongs to
     * @param row int row number
     * @param col int column number
     * @return Integer indicating which box a square with row and col belongs to
     */
    private static Integer calcBox(int row, int col){
        if(row - 3 <= 0){
            if(col - 3 <= 0){
                return 1;
            }
            else if(col - 3 <= 3){
                return 2;
            }
            else{
                return 3;
            }
        }

        else if(row - 3 <= 3){
            if(col - 3 <= 0){
                return 4;
            }
            else if(col - 3 <= 3){
                return 5;
            }
            else{
                return 6;
            }
        }

        else{
            if(col - 3 <= 0){
                return 7;
            }
            else if(col - 3 <= 3){
                return 8;
            }
            else{
                return 9;
            }
        }
    }

    /** 
     * @return HashMap that that maps each square's String tag to a set of all the squares' String tags in its row, column, and box
     */
    private static HashMap<String, HashSet<String>> createPeers(){
        HashMap<String, HashSet<String>> map = new HashMap<>();
        for(int r = 0; r < 9; r ++){
            for(int c = 0; c < 9; c ++){
                String square = SQUARES[r][c];
                map.put(square, new HashSet<>());
                addBoxPeers(map, square, calcBox(r+1, c+1));
                addColPeers(map, square, c);
                addRowPeers(map, square, r);
            }
        }
        return map;
    }

    /**
     * Adds all squares in square's row, helper for createPeers()
     * @param map HashMap of square's peers
     * @param square String tag of current square
     * @param r int row number
     */
    private static void addRowPeers(HashMap<String, HashSet<String>> map, String square, int r){
        for(String s : SQUARES[r]){
            if(!s.equalsIgnoreCase(square)){
                map.get(square).add(s);
            }
        }
    }

     /**
     * Adds all squares in square's column, helper for createPeers()
     * @param map HashMap of square's peers
     * @param square String tag of current square
     * @param c int column number
     */
    private static void addColPeers(HashMap<String, HashSet<String>> map, String square, int c){
        for(int i = 0; i < 9; i ++){
            String s = SQUARES[i][c];
            if(!s.equalsIgnoreCase(square)){
                map.get(square).add(s);
            }
        }
    }

     /**
     * Adds all squares in square's box, helper for createPeers()
     * @param map HashMap of square's peers
     * @param square String tag of current square
     * @param b int box number
     */
    private static void addBoxPeers(HashMap<String, HashSet<String>> map, String square, int b){
        for(String s : BOXES.get(b)){
            if(!s.equalsIgnoreCase(square)){
                map.get(square).add(s);
            }
        }
    }

    /**
     * Initializes a map of candidates, which maps each square to a set of the possible digits that it can take within the rules of Sudoku
     * @return HashMap map of the candidates (String tag to HashSet of String tags)
     */
    private HashMap<String, HashSet<String>> initializeCandidates(){
        HashMap<String, HashSet<String>> candidates = new HashMap<>();
        for(int r = 0; r < 9; r ++){
            for(int c = 0; c < 9; c ++){
                HashSet<String> hs = new HashSet<>();
                for(String s : ROWS_STRING.split("")){
                    hs.add(s);
                }
                candidates.put(SQUARES[r][c], hs);
            }
        }
        return candidates;
    }

    /**
     * Maps each row to a set of the squares in that row
     * @return HashMap mapping int row number to String tags of squares in the row
     */
    private static HashMap<Integer, HashSet<String>> createRows(){
        HashMap<Integer, HashSet<String>> map = new HashMap<>();
        for(int i = 1; i < 10; i++){
            map.put(i, new HashSet<>());
        }
        for(int r = 0; r < 9; r ++){
            for(int c = 0; c < 9; c ++){
                map.get(r+1).add(SQUARES[r][c]);
            }
        }
        return map;
    }
    
    /**
     * Maps each row to a set of the squares in that column
     * @return HashMap mapping int column number to String tags of squares in the column
     */
    private static HashMap<Integer, HashSet<String>> createCols(){
        HashMap<Integer, HashSet<String>> map = new HashMap<>();
        for(int i = 1; i < 10; i++){
            map.put(i, new HashSet<>());
        }
        for(int r = 0; r < 9; r ++){
            for(int c = 0; c < 9; c ++){
                map.get(c+1).add(SQUARES[r][c]);
            }
        }
        return map;
    }

    /**
     * @param candidates HashMap<String, HashSet<String>> of candidates
     * @param s String tag of square
     * @return Returns a copy of square s candidates as HashSet<String>
     */
    private HashSet<String> copyCandidates(HashMap<String, HashSet<String>> candidates, String s){
        HashSet<String> cands = new HashSet<>();
        for(String d : candidates.get(s)){
            cands.add(d);
        }
        return cands;
    }

    /**
     * @param candidates HashMap<String, HashSet<String>> candidates map
     * @return Returns a copy of the candidates map as HashMap<String, HashSet<String>>
     */
    private HashMap<String, HashSet<String>> copyCandMap(HashMap<String, HashSet<String>> candidates){
        HashMap<String, HashSet<String>> map = new HashMap<>();
        for(int r = 0; r < 9; r ++){
            for(int c = 0; c < 9; c ++){
                map.put(SQUARES[r][c], copyCandidates(candidates, SQUARES[r][c]));
            }
        }
        return map;
    }

    /**
     * Assigns a value d to square s by eliminating every other digit in square s candidates.
     * @param candidates HashMap<string, HashSet<String>> candidates map
     * @param s String square s
     * @param d String digit d
     * @return candidates map as HashMap<String, HashSet<String>> if no contradictions, otherwise returns null
     */
    private HashMap<String, HashSet<String>> assignValue(HashMap<String, HashSet<String>> candidates, String s, String d){
        HashSet<String> otherCands = copyCandidates(candidates, s);
        otherCands.remove(d);
        for(String digit : otherCands){
            if(eliminateValue(candidates, s, digit) == null){
                return null;
            }
        }
        return candidates;
    }

    /**
     * Eliminates digit d from square s candidates. Calls both constraint methods.
     * @param candidates HahsMap<String, HashSet<String>> candidates map
     * @param s String square s
     * @param d String digit d
     * @return Returns null if a contradiction was found, otherwise returns candidates as HashMap<String, HashSet<String>>
     */
    private HashMap<String, HashSet<String>> eliminateValue(HashMap<String, HashSet<String>> candidates, String s, String d){
        HashSet<String> currentCands = candidates.get(s);
        if(!currentCands.contains(d)){
            return candidates;
        }
        currentCands.remove(d);
        if(currentCands.size() == 0){
            return null;
        }
        else if(currentCands.size() == 1){
            constraintOneCounter++;
            Iterator<String> iter = currentCands.iterator();
            String d2= iter.next();
            for(String sq : PEERS.get(s)){
                if(eliminateValue(candidates, sq, d2) == null){
                    return null;
                }
            }
            return candidates;
        }
        if(!constraintTwo(d, getRow(s), candidates, ROWS)){
            return null;
        }
        else if(!constraintTwo(d, getCol(s), candidates, COLS)){
            return null;
        }
        else if(!constraintTwo(d, calcBox(getRow(s), getCol(s)), candidates, BOXES)){
            return null;
        }
        if(!constraintThree(d, calcBox(getRow(s), getCol(s)), candidates, BOXES)){
            return null;
        }
        else{
            return candidates;
        }
    }

    /**
     * Takes in a grid, and assigns the corresponding given digits to the appropriate square
     * @param grid String grid of digits and '.'
     * @return candidates map as HashMap<String, HashSet<String>>
     */
    public HashMap<String, HashSet<String>> parseGrid(String grid){
        HashMap<String, HashSet<String>> candidates = initializeCandidates();
        String[] gridString = grid.split("");
        String s = "";
        for(int r = 0; r < 9; r ++){
            for(int c = 0; c < 9; c ++){
                s = SQUARES[r][c];
                String d = gridString[c + 9*r];
                if(DIGITS.contains(d)){
                    assignValue(candidates, s, d);
                }
            }
        }
        return candidates;
    }

    /**
     * @param candidates HashMap<String, HashSet<String>> candidates map
     * @return Returns a nicely formatted completed Sudoku puzzle as a String
     */
    private String display(HashMap<String, HashSet<String>> candidates){
        StringBuilder sb = new StringBuilder();
        for(int r = 0; r < 9; r ++){
            for(int c = 0; c < 9; c ++){
                sb.append('(');
                for(String d : candidates.get(SQUARES[r][c])){
                    sb.append(d);
                }
                sb.append(") ");
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    private int getRow(String s){
        return Character.getNumericValue(s.charAt(0));
    }

    private int getCol(String s){
        return Character.getNumericValue(s.charAt(1));
    }

    /**
     * Second constraint in the eliminate method:
     * If there is only one spot for a digit d to go in unit, then assign it there.
     * @param d String digit d
     * @param u int which number unit
     * @param candidates HashMap<String, HashSet<String>> candidates map
     * @param unit HashMap<Integer, HashSet<String>> row, column, or box
     * @return true if no contradiction found, otherwise false
     */
    private boolean constraintTwo(String d, int u, HashMap<String, HashSet<String>> candidates, HashMap<Integer, HashSet<String>> unit){
        int count =0;
        String s = "";
        for(String sq : unit.get(u)){
            if(candidates.get(sq).contains(d)){
                count++;
                s = sq;
            }
        }
        if(count == 0){
            return false;
        }
        else if(count == 1){
            constraintTwoCounter++;
            if(assignValue(candidates, s, d) == null){
                return false;
            }
        }
        return true;
    }

    /**
     * Third constraint in the eliminate method:
     * If the available spots for a digit d to go in a row/column all reside in the same box, 
     * then eliminate d from the rest of the row/column
     * @param d String digit d
     * @param u int which number unit
     * @param candidates HashMap<String, HashSet<String>> candidates map
     * @param unit HashMap<Integer, HashSet<String>> row, column, or box
     * @return true if no contradiction found, otherwise false
     */
    private boolean constraintThree(String d, int u, HashMap<String, HashSet<String>> candidates, HashMap<Integer, HashSet<String>> box){
        boolean rowBool = true;
        boolean colBool = true;
        int row = 0;
        int col = 0;
        int rcount = 0;
        int ccount = 0;
        for(String sq : box.get(u)){
            if(candidates.get(sq).contains(d)){
                if(rowBool){
                    if(rcount >0){
                        if(row != getRow(sq)){
                            rowBool = false;
                        }
                        rcount++;
                    }
                    row = getRow(sq);
                    rcount ++;
                }
                if(colBool){
                    if(ccount >0){
                        if(col != getCol(sq)){
                            colBool = false;
                        }
                        ccount++;
                    }
                    col = getCol(sq);
                    ccount ++;
                }
            }
        }
        if(rcount>1){
            rowBool=false;
        }
        if(ccount>1){
            colBool = false;
        }
        if(rowBool){
            constraintThreeCounter++;
            for(String s : ROWS.get(row)){
                if(calcBox(row, getCol(s))!=u){
                    if(eliminateValue(candidates, s, d) == null){
                        return false;
                    }
                }   
            }
        }
        if(colBool){
            constraintThreeCounter++;
            for(String s : COLS.get(col)){
                if(calcBox(getRow(s), col)!=u){
                    if(eliminateValue(candidates, s, d) == null){
                        return false;
                    }
                }   
            }
        }
        return true;
    }

    /**
     * Recursive searching method. Finds the square with the smallest number of possible digit, and then assigns it a random value, checking for contradictions along the way.
     * @param candidates HashMap<String, HashSet<String>> candidates map
     * @return candidates map as HashMap<String, HashSet<String>>
     */
    public HashMap<String, HashSet<String>> search(HashMap<String, HashSet<String>> candidates){
        boolean solved = true;
        String s = "";
        int size = 100;
        int curSize = 0;
        for(int r = 0; r < 9; r ++){
            for(int c = 0; c < 9; c ++){
                curSize = candidates.get(SQUARES[r][c]).size();
                if(curSize != 1){
                    solved = false;
                }
                if(curSize == 0){
                    return null;
                }
                if(curSize != 1 && curSize < size){
                    s = SQUARES[r][c];
                    size = curSize;
                    if(curSize==2){
                        break;
                    }
                }
            }
        }
        if(solved){
            return candidates;
        }
        HashSet<String> currCands = candidates.get(s);
        for(String d : currCands){
            randomAssignmentCounter++;
            HashMap<String, HashSet<String>> nextCands = assignValue(copyCandMap(candidates), s, d);
            if(nextCands != null){
                HashMap<String, HashSet<String>> result = search(nextCands);
                if(result != null){
                    return result;
                }
            }
        }
        return null;
    }

    /**
     * Solves the puzzle using the search method. Returns the String from display()
     * @param grid String initial puzzle
     */
    public String solve(String grid){
        return display(search(parseGrid(grid)));
    }

    /**
     * @param grid String initial puzzle
     * @return Rather than displaying as a neat finished puzzle, it returns a string of digits, i.e "123456789987654321... "
     */
    public String solveAsLine(String grid){
        HashMap<String, HashSet<String>> cands = search(parseGrid(grid));
        StringBuilder sb = new StringBuilder();
        for(int r = 0; r < 9; r ++){
            for(int c = 0; c <9; c ++){
                HashSet<String> curCands = cands.get(SQUARES[r][c]);
                Iterator<String> iter = curCands.iterator();
                sb.append(iter.next());
            }
        }
        return sb.toString();
    }

    /**
     * Parses a file of puzzles into a map of puzzle line to string, i.e 1 to "1259..."
     * @param fname File of puzzles
     * @return HashMap<Integer, String> map of puzzle number to String representation
     */
    public HashMap<Integer, String> parseFile(File fname){
        HashMap<Integer, String> puzzles =  new HashMap<>();
        try{
            Scanner scan = new Scanner(fname);
            int count = 1;
            while(scan.hasNextLine()){
                String newLine = scan.nextLine();
                puzzles.put(count, newLine);
                count ++;
            }
        } catch(Exception e){
            System.out.println("file don't exist yo");
        }
        return puzzles;
    }

    /**
     * @param puzzles HashMap<Integer, String> which maps puzzle number to string representation of puzzle - this is found with parseFile()
     * @return Formatted string display of solved puzzle along with time taken to solve
     */
    private void parsePuzzles(HashMap<Integer, String> puzzles){
        long max = 0;
        long avg = 0;
        long min = Long.MAX_VALUE;
        String maxPuzzle = "";
        for(int n = 1; n <= puzzles.size(); n ++){
            String grid = puzzles.get(n);
            Instant start = Instant.now();
            search(parseGrid(grid));
            Instant end = Instant.now();
            Duration timeElapsed = Duration.between(start, end);
            if(max <= timeElapsed.toNanos()){
                max = timeElapsed.toNanos();
                maxPuzzle = grid;
            }
            if(min >= timeElapsed.toNanos()){
                min = timeElapsed.toNanos();
            }
            avg += timeElapsed.toNanos();
        }
        avg /= puzzles.size();
        System.out.println("Average solve time (nanoseconds): " + avg);
        System.out.println("Max solve time (nanoseconds): " + max);
        System.out.println("Min solve time (nanoseconds): "+min);
        System.out.println("Hardest puzzle: " + maxPuzzle);
        System.out.println(constraintThreeCounter/puzzles.size());
    }

    /**
     * Creates a random puzzle with the givenDigits specified
     * @param givenDigits int number of digits that should be given
     * @return string representation of puzzle
     */
    public String createRandomPuzzle(int givenDigits){
        HashMap<String, HashSet<String>> candidates = initializeCandidates();
        ArrayList<String> shuffledSquares = shuffleSquares();
        for(String s : shuffledSquares){
            if(candidates.get(s).size() != 1){
                String d = getRandomDigit(candidates, s);
                if(assignValue(candidates, s, d) == null){
                    break;
                }
                ArrayList<String> goodSquares = new ArrayList<>();
                HashSet<String> uniqueDigits = new HashSet<>();
                for(int r = 0; r < 9; r ++){
                    for(int c = 0; c < 9; c ++){
                        HashSet<String> curCands = candidates.get(SQUARES[r][c]);
                        if(curCands.size() == 1){
                            Iterator<String> iter = curCands.iterator();
                            goodSquares.add(SQUARES[r][c]);
                            uniqueDigits.add(iter.next());
                        }
                    }
                }
                if(goodSquares.size() >= givenDigits && uniqueDigits.size() >= 8){
                    StringBuilder sb = new StringBuilder();
                    for(int r = 0; r < 9; r ++){
                        for(int c = 0; c < 9; c ++){
                            HashSet<String> curCands = candidates.get(SQUARES[r][c]);
                            if(curCands.size() == 1){
                                Iterator<String> iter = curCands.iterator();
                                sb.append(iter.next());
                            }
                            else{
                                sb.append(".");
                            }
                        }
                    }
                    return sb.toString();
                }
            }
        }
        return createRandomPuzzle(givenDigits);
    }   

    /**
     * @return Returns an ArrayList<String> of the squares in SQUARES shuffled in a random order
     */
    private ArrayList<String> shuffleSquares(){
        ArrayList<String> shuffledSquares = new ArrayList<>();
        for(int i = 0; i < 9; i ++){
            for(int j = 0; j < 9; j ++){
                shuffledSquares.add(SQUARES[i][j]);
            }
        }
        Collections.shuffle(shuffledSquares);
        return shuffledSquares;
    }

    /**
     * @param candidates HashMap<String, HashSet<String>> candidates map
     * @param s String square s
     * @return Returns a random digit (String) from square s candidates
     */
    private String getRandomDigit(HashMap<String, HashSet<String>> candidates, String s){
        HashSet<String> cands = candidates.get(s);
        String[] temp = cands.toArray(new String[cands.size()]);
        Random rand = new Random();
        return temp[rand.nextInt(cands.size()-1)];
    }
    
    /**
     * Similar to solveAsLine(), only doesn't solve the puzzle within the method
     * @param candidates HashMap<String, HashSet<String>> candidates map
     * @return String puzzles as a one line string
     */
    public String displayPuzzleString(HashMap<String, HashSet<String>> candidates){
        StringBuilder sb = new StringBuilder();
        HashSet<String> curCands = new HashSet<>();
        for(int r = 0; r < 9; r ++){
            for(int c = 0; c < 9; c ++){
                curCands = candidates.get(SQUARES[r][c]);
                Iterator<String> iter = curCands.iterator();
                sb.append(iter.next());
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        BasicSudoku su = new BasicSudoku();
        // Code to create 100,000 random puzzles and determine the average solve time
        // long max = 0;
        // long avg = 0;
        // long min = Long.MAX_VALUE;
        // String maxPuzzle = "";
        // for(int i = 0; i < 10000; i ++){
        //     String puzzle = su.createRandomPuzzle(17);
        //     Instant start = Instant.now();
        //     su.search(su.parseGrid(puzzle));
        //     Instant end = Instant.now();
        //     Duration timeElapsed = Duration.between(start, end);
        //     if(max <= timeElapsed.toNanos()){
        //         max = timeElapsed.toNanos();
        //         maxPuzzle = puzzle;
        //     }
        //     if(min >= timeElapsed.toNanos()){
        //         min = timeElapsed.toNanos();
        //     }
        //     avg += timeElapsed.toNanos();
        // }
        // avg = avg / 10000;
        // // avg/=1e9;
        // // max /= 1e9;
        // // min /= 1e9;
        // System.out.println("Average solve time (nanoseconds): " + avg);
        // System.out.println("Max solve time (nanoseconds): " + max);
        // System.out.println("Min solve time (nanoseconds): "+min);
        // System.out.println("Hardest puzzle: " + maxPuzzle);
        // System.out.println(su.constraintThreeCounter/10000);
        // su.parsePuzzles(su.parseFile(new File("HardPuzzles.txt")));
        System.out.println(su.solve(su.parseFile(new File("HardPuzzles.txt")).get(2)));
        System.out.println("Constraint One: "+ su.constraintOneCounter);
        System.out.println("Constraint Two: "+ su.constraintTwoCounter);
        System.out.println("Constraint Three: "+ su.constraintThreeCounter);
        System.out.println("Random Assignments: " + su.randomAssignmentCounter);
    }
}
