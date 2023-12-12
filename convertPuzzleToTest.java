import java.util.Scanner;
import java.io.File;

public class convertPuzzleToTest {
    public int[][][] parseFile(File fname){
        int [][][] listOfGrids = new int [95][9][9];
        try{
            Scanner scan = new Scanner(fname);
            // int lineCount = 1;
            
            for (int row = 0; scan.hasNextLine() && row < 9; row++) {
                char[] chars = scan.nextLine().toCharArray();
                for (int i = 0; i < 9 && i < chars.length; i++) {
                    for (int j = 0; j<95; j++){
                        listOfGrids [j][row][i] = chars[i];
                        System.out.println(listOfGrids[j][row][i]);
                // Grid[row][i] = chars[i];
                }
            }
        }
    }    
         catch(Exception e){
            System.out.println("file don't exist yo");
        }
        return listOfGrids;
    }

    public static void main(String[] args) {
        convertPuzzleToTest su = new convertPuzzleToTest();
        System.out.println((su.parseFile(new File("HardPuzzles.txt"))));
    }
    
}
