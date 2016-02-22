import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//Lights Out with GUI
@SuppressWarnings("serial")
public class LightsOutGUI extends JFrame{
  //Constants for game board - DONE
  public static final int rows = 5;
  public static final int cols = 5;

  //Constants for graphic drawing dimensions - DONE
  public static final int cellSize = 60; //cell width, height
  public static final int canvasWidth = cellSize * cols;
  public static final int canvasHeight = cellSize * rows;
  public static final int gridWidth = 4; //gridline width
  public static final int gridWidthHalf = gridWidth/2;

  //Constants for drawing symbols - DONE
  public static final int cellPadding = 4;
  public static final int symbolSize = cellSize - (cellPadding*2);
  public static final int symbolStrokeWidth = 8; //pen stroke width

  //Enum for game states - DONE
  public enum GameState{
    Playing, Won;
  }

  //Enum for Symbol - DONE
  public enum Symbol{
    Black, White;
  }

  //private Symbol currentPlayer; - NOT NEEDED?
  private GameState currentState;

  private Symbol[][] board; //game board
  private DrawCanvas canvas; //draw canvas for game board
  private JLabel statusBar; //status bar

  //Game and GUI constructor - DONE
  public LightsOutGUI(){
    canvas = new DrawCanvas(); //construct new canvas
    canvas.setPreferredSize(new Dimension(canvasWidth, canvasHeight));

    //Canvas fires a MouseEvents upon mouselick
    canvas.addMouseListener(new MouseAdapter(){
      @Override
      public void mouseClicked(MouseEvent e){ //mouseclick handler
        int mouseX = e.getX();
        int mouseY = e.getY();

        //Get row and col clicked
        int rowSelected = mouseY / cellSize;
        int colSelected = mouseX / cellSize;

        if(currentState == GameState.Playing){
          if(rowSelected >= 0 && rowSelected < rows && colSelected >= 0
            && colSelected < cols){
              //make move
              int r = rowSelected;
              int c = colSelected;
                if(board[r][c] == Symbol.White){board[r][c] = Symbol.Black;}
                else{board[r][c] = Symbol.White;}
              if(r != 0){
                if(board[r-1][c] == Symbol.White){board[r-1][c] = Symbol.Black;}
                else{board[r-1][c] = Symbol.White;}
              }
              if(c != (cols-1)){
                if(board[r][c+1] == Symbol.White){board[r][c+1] = Symbol.Black;}
                else{board[r][c+1] = Symbol.White;}
              }
              if(r != (rows-1)){
                if(board[r+1][c] == Symbol.White){board[r+1][c] = Symbol.Black;}
                else{board[r+1][c] = Symbol.White;}
              }
              if(c != 0){
                if(board[r][c-1] == Symbol.White){board[r][c-1] = Symbol.Black;}
                else{board[r][c-1] = Symbol.White;}
              }
              updateGame(rowSelected, colSelected); //update state
            }
        }
        else{ //game over
          initGame(); //restart game
        }
        repaint();
      }
    });

    //Setup status bar for status message
    statusBar = new JLabel("  ");
    statusBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));

    Container cp = getContentPane();
    cp.setLayout(new BorderLayout());
    cp.add(canvas, BorderLayout.CENTER);
    cp.add(statusBar, BorderLayout.PAGE_END);

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack(); //pack all components in JFrame
    setTitle("Lights Out");
    setVisible(true); //show this JFrame

    board = new Symbol[rows][cols]; //allocate array
    initGame(); //initialise game board contents and game variables
  }

  //Initialise game board contents and status - DONE - MAKE GAME BOARD

  public void initGame(){
    int i = (int)Math.floor(Math.random()*2);
    switch(i){
      case 0 : board1(); break;
      case 1 : board2(); break;
      /*case 2 : board3(); break;
      case 3 : board4(); break;
      case 4 : board5(); break;*/
      default : board1();
    }
    currentState = GameState.Playing;
  }

  //Update currentState after player has made move - DONE
  public void updateGame(int rowSelected, int colSelected){
    if(hasWon(rowSelected, colSelected)){//check for win
      currentState = GameState.Won;
    }
    //Otherwise no change to state
  }

   //Return true if player has won - DONE
   public boolean hasWon(int rowSelected, int colSelected) {
     for (int row = 0; row < rows; ++row) {
        for (int col = 0; col < cols; ++col) {
           if (board[row][col] == Symbol.White) {
              return false; // light on found - player hasn't won - exit
           }
        }
     }
     return true; //else all lights off - player has won!
   }

   //Class used for custom graphics drawing - DONE
   class DrawCanvas extends JPanel{
     @Override
     public void paintComponent(Graphics g){
       super.paintComponent(g); //fill background
       setBackground(Color.WHITE); //set background colour

       //Draw gridlines
       g.setColor(Color.LIGHT_GRAY);
       for(int row = 1; row < rows; ++row){
         g.fillRoundRect(0, cellSize * row - gridWidthHalf,
          canvasWidth-1, gridWidth, gridWidth, gridWidth);
       }
       for(int col = 1; col < cols; ++col){
         g.fillRoundRect(cellSize * col - gridWidthHalf, 0,
          gridWidth, canvasHeight-1, gridWidth, gridWidth);
       }

       //Draw symbols in cells if not empty
       Graphics2D g2d = (Graphics2D)g;
       g2d.setStroke(new BasicStroke(symbolStrokeWidth, BasicStroke.CAP_ROUND,
               BasicStroke.JOIN_ROUND));
       for(int row = 0; row < rows; ++row){
         for(int col = 0; col < cols; ++col){
           int x1 = col * cellSize + cellPadding;
           int y1 = row * cellSize + cellPadding;
           if(board[row][col] == Symbol.White){
             g2d.setColor(Color.WHITE);
             g2d.fillRect(x1, y1, symbolSize, symbolSize);
           }
           else if(board[row][col] == Symbol.Black){
             g2d.setColor(Color.BLACK);
             g2d.fillRect(x1, y1, symbolSize, symbolSize);
           }
         }
       }

       //Print status bar message
       if(currentState == GameState.Playing){
         statusBar.setForeground(Color.BLACK);
         statusBar.setText("Keep playing! :)");
       }
       else if(currentState == GameState.Won){
         statusBar.setForeground(Color.RED);
         statusBar.setText("You've won! Click to play again :)");
       }
     }
   }

   //Main entry method - DONE
   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            new LightsOutGUI();
         }
      });
   }

   void board1(){
     board[0][0] = Symbol.White;
     board[0][1] = Symbol.White;
     board[0][2] = Symbol.White;
     board[0][3] = Symbol.White;
     board[0][4] = Symbol.Black;
     board[1][0] = Symbol.Black;
     board[1][1] = Symbol.Black;
     board[1][2] = Symbol.Black;
     board[1][3] = Symbol.Black;
     board[1][4] = Symbol.Black;
     board[2][0] = Symbol.Black;
     board[2][1] = Symbol.Black;
     board[2][2] = Symbol.Black;
     board[2][3] = Symbol.White;
     board[2][4] = Symbol.Black;
     board[3][0] = Symbol.White;
     board[3][1] = Symbol.Black;
     board[3][2] = Symbol.Black;
     board[3][3] = Symbol.Black;
     board[3][4] = Symbol.White;
     board[4][0] = Symbol.Black;
     board[4][1] = Symbol.White;
     board[4][2] = Symbol.Black;
     board[4][3] = Symbol.White;
     board[4][4] = Symbol.Black;
   }

   void board2(){
     board[0][0] = Symbol.White;
     board[0][1] = Symbol.White;
     board[0][2] = Symbol.White;
     board[0][3] = Symbol.White;
     board[0][4] = Symbol.White;
     board[1][0] = Symbol.Black;
     board[1][1] = Symbol.Black;
     board[1][2] = Symbol.Black;
     board[1][3] = Symbol.Black;
     board[1][4] = Symbol.Black;
     board[2][0] = Symbol.Black;
     board[2][1] = Symbol.Black;
     board[2][2] = Symbol.Black;
     board[2][3] = Symbol.White;
     board[2][4] = Symbol.Black;
     board[3][0] = Symbol.White;
     board[3][1] = Symbol.Black;
     board[3][2] = Symbol.Black;
     board[3][3] = Symbol.Black;
     board[3][4] = Symbol.White;
     board[4][0] = Symbol.Black;
     board[4][1] = Symbol.White;
     board[4][2] = Symbol.Black;
     board[4][3] = Symbol.White;
     board[4][4] = Symbol.Black;
   }
}

/* TO WRITE
- Different game boards - l90
*/
