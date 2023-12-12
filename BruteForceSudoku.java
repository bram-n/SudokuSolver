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
}




