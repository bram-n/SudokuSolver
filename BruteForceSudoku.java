import java.io.File;
import java.util.Scanner;

public class BruteForceSudoku{
    private int [][] SudokuGrid;
    private static final int COLUMNS = 9;
    private static final int ROWS = 9;

    public int[] findClosestBox(int[][]grid){
        int[] result = new int[2];
        // Finds the closest box
        for(int i=0;i<COLUMNS;i++){
            for(int j=0;j<ROWS;j++){
                if(grid[i][j]== 0){
                   result[0]=i;
                   result[1]=j;
                   return result;
                }
            }
        }

        return null;
    }
    public boolean isValid(int[][]grid, int choice, int row, int col){
        int[]rowValues=grid[row];
        for (int item: rowValues){
            if (item==choice){
                return false;
            }
        }
        
        for (int[] currentRow : grid) {
            if (currentRow[col] == choice) {
                return false;
            }
        }
        //Start of the row and column in the subgrid
        int rowStart= row - row % 3;
        int colStart= col - col % 3;
        int rowEnd=rowStart+2;
        int colEnd = colStart+2;

        for(int smallGridRow = rowStart;smallGridRow<=rowEnd;smallGridRow++){
            for(int smallGridCol = colStart;smallGridCol<=colEnd;smallGridCol++){
            if (grid[smallGridRow][smallGridCol]==choice){
                return false;
            }
        }
        }

        return true;
    
    }

    public boolean solvePuzzle(int[][]grid){
        int[]closest=findClosestBox(grid);
        // See if puzzle is finished
        if (closest==null){
            return true;
        }
        int row = closest[0];
        int col = closest[1];
        

        for (int choice=1; choice<=9;choice++){
            if (isValid(grid, choice, row, col)){
                grid[row][col]=choice;
                if (solvePuzzle(grid)){
                return true;
                }
            }
            grid[row][col]=0;
            
        }
        return false;
    }

    public void parsePuzzles(){
        try{
            Scanner scan = new Scanner(new File("HardPuzzles.txt"));
            long max = 0;
            long avg = 0;
            long min = Long.MAX_VALUE;
            String maxPuzzle = "";
            while(scan.hasNextLine()){
                String newLine = scan.nextLine();
                String[] line = newLine.split("");
                int[][] grid = new int [9][9];
                for(int i = 0; i < 9; i ++){
                    for(int j = 0; j <9; j ++){
                        if(line[9*i + j].equals(".")){
                            grid[i][j] = 0;
                        }
                        else{
                            grid[i][j] = Integer.valueOf(line[9*i+j]);
                        }
                    }
                }
                long start = System.nanoTime();
                solvePuzzle(grid);
                long end = System.nanoTime();
                long timeElapsed = end-start;
                if(max <= timeElapsed){
                    max = timeElapsed;
                    maxPuzzle = newLine;
                }
                if(min >= timeElapsed){
                    min = timeElapsed;
                }
                avg += timeElapsed;
            }
            avg /= 95;
            System.out.println("Average solve time (nanoseconds): " + avg);
            System.out.println("Max solve time (nanoseconds): " + max);
            System.out.println("Min solve time (nanoseconds): "+min);
            System.out.println("Hardest puzzle: " + maxPuzzle);
        }
        catch(Exception e){
            System.out.println("file don't exist yo");
        }
    }

    public static void main(String[] args) {
        BruteForceSudoku bfs = new BruteForceSudoku();
        bfs.parsePuzzles();
    }
}




