import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//Tic-Tac-Toe with GUI
@SuppressWarnings("serial")
public class TicTacToeGUI extends JFrame{
  //Constants for game board
  public static final int rows = 3;
  public static final int cols = 3;

  //Constants for graphic drawing dimensions
  public static final int cellSize = 100; //cell width, height
  public static final int canvasWidth = cellSize * cols;
  public static final int canvasHeight = cellSize * rows;
  public static final int gridWidth = 8; //gridline width
  public static final int gridWidthHalf = gridWidth/2;

  //Constants for drawing symbols
  public static final int cellPadding = cellSize/6;
  public static final int symbolSize = cellSize - (cellPadding*2);
  public static final int symbolStrokeWidth = 8; //pen stroke width

  //Enum for game states
  public enum GameState{
    Playing, Draw, CrossWon, NoughtWon;
  }

  //Enum for Symbol
  public enum Symbol{
    Empty, Cross, Nought;
  }

  private Symbol currentPlayer;
  private GameState currentState;

  private Symbol[][] board; //game board
  private DrawCanvas canvas; //draw canvas for game board
  private JLabel statusBar; //status bar

  //Game and GUI constructor
  public TicTacToeGUI(){
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
            && colSelected < cols && board[rowSelected][colSelected] == Symbol.Empty){
              board[rowSelected][colSelected] = currentPlayer; //make move
              updateGame(currentPlayer, rowSelected, colSelected); //update state
              currentPlayer = (currentPlayer == Symbol.Cross) ? Symbol.Nought : Symbol.Cross; //switch player
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
    setTitle("Tic Tac Toe");
    setVisible(true); //show this JFrame

    board = new Symbol[rows][cols]; //allocate array
    initGame(); //initialise game board contents and game variables
  }

  //Initialise game board contents and status
  public void initGame(){
    for(int row = 0; row < rows; ++row){
      for(int col = 0; col < cols; ++col){
        board[row][col] = Symbol.Empty; //all cells empty
      }
    }
    currentState = GameState.Playing; //ready to play
    currentPlayer = Symbol.Cross; //cross plays first
  }

  //Update currentState after player has made move
  public void updateGame(Symbol theSymbol, int rowSelected, int colSelected){
    if(hasWon(theSymbol, rowSelected, colSelected)){//check for win
      currentState = (theSymbol == Symbol.Cross) ? GameState.CrossWon : GameState.NoughtWon;
    }
    else if (isDraw()){ //check for draw
      currentState = GameState.Draw;
    }
    //Otherwise no change to state
  }

  //Return true if game is a draw
  public boolean isDraw() {
      for (int row = 0; row < rows; ++row) {
         for (int col = 0; col < cols; ++col) {
            if (board[row][col] == Symbol.Empty) {
               return false; // empty cell found - not draw - exit
            }
         }
      }
      return true;  // no more empty cells - draw
   }

   //Return true if player with theSymbol has won
   public boolean hasWon(Symbol theSymbol, int rowSelected, int colSelected) {
      return (board[rowSelected][0] == theSymbol  // 3 in a row
            && board[rowSelected][1] == theSymbol
            && board[rowSelected][2] == theSymbol
       || board[0][colSelected] == theSymbol      // 3 in a col
            && board[1][colSelected] == theSymbol
            && board[2][colSelected] == theSymbol
       || rowSelected == colSelected            // 3 in a \ diagonal
            && board[0][0] == theSymbol
            && board[1][1] == theSymbol
            && board[2][2] == theSymbol
       || rowSelected + colSelected == 2  // 3 in a / diagonal
            && board[0][2] == theSymbol
            && board[1][1] == theSymbol
            && board[2][0] == theSymbol);
   }

   //Class used for custom graphics drawing
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
           if(board[row][col] == Symbol.Cross){
             g2d.setColor(Color.RED);
             int x2 = (col+1) * cellSize - cellPadding;
             int y2 = (row+1) * cellSize - cellPadding;
             g2d.drawLine(x1, y1, x2, y2);
             g2d.drawLine(x2, y1, x1, y2);
           }
           else if(board[row][col] == Symbol.Nought){
             g2d.setColor(Color.BLUE);
             g2d.drawOval(x1, y1, symbolSize, symbolSize);
           }
         }
       }

       //Print status bar message
       if(currentState == GameState.Playing){
         statusBar.setForeground(Color.BLACK);
         if(currentPlayer == Symbol.Cross){
           statusBar.setText("X's Turn");
         }
         else{
           statusBar.setText("O's Turn");
         }
       }
       else if(currentState == GameState.Draw){
         statusBar.setForeground(Color.RED);
         statusBar.setText("It's a Draw! Click to play again.");
       }
       else if(currentState == GameState.CrossWon){
         statusBar.setForeground(Color.RED);
         statusBar.setText("'X' Won! Click to play again.");
       }
       else if(currentState == GameState.NoughtWon){
         statusBar.setForeground(Color.RED);
         statusBar.setText("'O' Won! Click to play again.");
       }
     }
   }

   //Main entry method
   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            new TicTacToeGUI();
         }
      });
   }
}
