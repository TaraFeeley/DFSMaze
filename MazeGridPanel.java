//Tara Feeley
//CIS2168
//Lab_04

import java.awt.*;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;


public class MazeGridPanel extends JPanel{
    private int rows;
    private int cols;
    private Cell[][] maze;
    private static final int WAIT = 1; //animation delay in ms

    public void genDFSMaze() {
        boolean[][] visited = new boolean[rows][cols];
        Stack<Cell> stack  = new Stack<Cell>();
        Cell start = maze[0][0];
        visited[start.row][start.col]=true; //mark initial cell as visited
        stack.push(start); //push to stack
        Cell current;
        Cell chosen;
        Cell prev = null;
        Random rand = new Random();
        while(!stack.isEmpty()) { // while the stack isn't empty
            current = stack.pop(); // pop a cell from the stack and make it the current cell
            //get ArrayList of current's unvisited neighbors
            List<Cell> neighbors = unvisitedNeighborCells(current, visited);

            if (!neighbors.isEmpty()) { //check that neighbors has at least one element
                stack.push(current); //push the current cell to the stack

                // choose a random number between 0 and size of neighbors exclusive
                int randIndex = rand.nextInt(neighbors.size());
                //get the cell at the randIndex in neighbors ArrayList and make this the chosen cell
                chosen = neighbors.get(randIndex);

                //each condition checks if the cell above, right of, below, or left of respectively is in bounds
                //and if the chosen cell chosen is above, right of, below, or left of current respectively
                //if true, remove wall between chosen and current and mark chosen cell as visited
                if (current.row-1 >= 0 && chosen.equals(maze[current.row - 1][current.col])) {
                    current.northWall = false;
                    chosen.southWall = false;
                    visited[chosen.row][chosen.col] = true;

                } else if (current.row + 1 < rows && chosen.equals(maze[current.row + 1][current.col])) {
                    current.southWall = false;
                    chosen.northWall = false;
                    visited[chosen.row][chosen.col] = true;

                } else if (current.col - 1 >=0 && chosen.equals(maze[current.row][current.col - 1])) {
                    current.westWall = false;
                    chosen.eastWall = false;
                    visited[chosen.row][chosen.col] = true;

                } else if (current.col + 1 < cols && chosen.equals(maze[current.row][current.col + 1])) {
                    current.eastWall = false;
                    chosen.westWall = false;
                    visited[chosen.row][chosen.col] = true;
                }

                prev = current;//store the current cell as prev
                stack.push(chosen); //push chosen cell to the stack

            } else{ // the current cell has no unvisited neighbors so need to backtrack
                //mark the previous cell as unvisited to proceed with backtracking
                visited[prev.row][prev.col] = false;
            }

        }

    }
    //helper method that returns an ArrayList containing the unvisited cells which neighbor the current position
    public ArrayList<Cell> unvisitedNeighborCells(Cell current, boolean[][] visited){
        ArrayList<Cell> unvisited = new ArrayList<>();
        //each if condition checks whether the neighbor cell- above, below, right of, and left of respectively-
        //is in bounds and has not been visited
        //if true, add the cell to the ArrayList
        if(current.row-1>=0 && !visited[current.row-1][current.col]){
            unvisited.add(maze[current.row-1][current.col]);
        }
        if(current.row+1< rows &&!visited[current.row+1][current.col]){
            unvisited.add(maze[current.row+1][current.col]);
        }
        if(current.col-1>=0 && !visited[current.row][current.col-1]){
            unvisited.add(maze[current.row][current.col-1]);
        }
        if(current.col+1<cols &&!visited[current.row][current.col+1]){
            unvisited.add(maze[current.row][current.col+1]);
        }
        //return the ArrayList of unvisited neighboring cells, which will contain between 0 and 4 elements
        return unvisited;
    }

    public void solveMaze() throws InterruptedException {
        Stack<Cell> stack  = new Stack<Cell>();
        //mark the start and finish cells
        Cell start = maze[0][0];
        start.setBackground(Color.GREEN);
        Cell finish = maze[rows-1][cols-1];
        finish.setBackground(Color.RED);
        stack.push(start); //push initial cell to stack
        Cell position= start;

        while(!stack.isEmpty() && position!= finish){ //While the stack isn't empty and have not reached finish

            Thread.sleep(WAIT); //delays animation
            position = stack.peek(); //current position is the cell at the top of the stack

            //worked with Shavya: gave each direction a different color to better see the path being taken at each step

            //check if position is not at north wall and the cell above position hasn't been visited
            if(!position.northWall  && !visited(position.row -1, position.col)) {
                //push cell above to the stack, mark it as current position, and change background color to mark as visited
                position = stack.push(maze[position.row-1][position.col]);
                position.setBackground(Color.pink);
            }

            //check if position is not at south wall and the cell below position hasn't been visited
            else if(!position.southWall  && !visited(position.row +1, position.col)) {
                //push cell above to the stack, mark it as current position, and change background color to mark as visited
                position = stack.push(maze[position.row+1][position.col]);
                position.setBackground(Color.orange);
            }

            //check if position is not at east wall and the cell to right of position hasn't been visited
            else if(!position.eastWall  && !visited(position.row, position.col+1)) {
                //push cell to the right to the stack, mark it as current position, and change background color to mark as visited
                position = stack.push(maze[position.row][position.col+1]);
                position.setBackground(Color.yellow);
            }

            //check if position is not at west wall and the cell to the left of position hasn't been visited
            else if(!position.westWall  && !visited(position.row, position.col-1)) {
                //push cell above to the left to the stack, mark it as current position, and change background color to mark as visited
                position = stack.push(maze[position.row][position.col-1]);
                position.setBackground(Color.magenta);
            }
            //backtrack
            else{
                position.setBackground(Color.darkGray);
                stack.pop();
            }
        }
    }

    public boolean visited(int row, int col) {
        Cell c = maze[row][col];
        Color status = c.getBackground();
        if(status.equals(Color.WHITE)  || status.equals(Color.RED)  ) {
            return false;
        }
        return true;
    }

    public MazeGridPanel(int rows, int cols) throws InterruptedException {
        this.setPreferredSize(new Dimension(800,800));
        this.rows = rows;
        this.cols = cols;
        this.setLayout(new GridLayout(rows,cols));
        this.maze =  new Cell[rows][cols];
        for(int row = 0 ; row  < rows ; row++) {
            for(int col = 0; col < cols; col++) {

                maze[row][col] = new Cell(row,col);

                this.add(maze[row][col]);
            }
        }
        this.genDFSMaze();
//		this.solveMaze();
    }

}

