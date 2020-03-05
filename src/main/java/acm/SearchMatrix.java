package acm;

/**
 * @program: linuxtest
 * @description:
 * @author: YeDongYu
 * @create: 2020-03-05 11:32
 */
public class SearchMatrix {

    /**
     * @param matrix: matrix, a list of lists of integers
     * @param target: An integer
     */
    public boolean searchMatrix(int[][] matrix, int target) {
        // write your code here
        if (null == matrix || matrix.length == 0 || matrix[0].length == 0) {
            return false;
        }
        int startRow = 0;
        int endRow = matrix.length-1;
        int mid;
        while (startRow + 1 < endRow){
            mid = (startRow + endRow)/2;
            if (matrix[mid][0] == target){
                return true;
            } else if(matrix[mid][0] > target){
                endRow = mid;
            } else {
                startRow = mid;
            }
        }
        int row;
        if (matrix[endRow][0] == target){
            return true;
        } else if (matrix[endRow][0]>target){
            row = startRow;
        } else {
            row = endRow;
        }
        int startCol = 0;
        int endCol = matrix[0].length-1;
        while (startCol + 1 <endCol){
            mid = (startCol + endCol)/2;
            if (matrix[row][mid] == target){
                return true;
            }else if(matrix[row][mid] > target){
                endCol = mid;
            } else {
                startCol = mid;
            }
        }
        if (matrix[row][startCol] == target || matrix[row][endCol] == target){
            return true;
        }
        return false;
    }
}
