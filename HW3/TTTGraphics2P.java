import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Tic-Tac-Toe: Two-player Graphics version with Simple-OO
 * https://www.ntu.edu.sg/home/ehchua/programming/java/JavaGame_TicTacToe.html
 */
@SuppressWarnings("serial")
public class TTTGraphics2P extends JFrame {
   // Named-constants for the game board
   public static final int ROWS = 3;  // ROWS by COLS cells
   public static final int COLS = 3;
 
   // Named-constants of the various dimensions used for graphics drawing
   public static final int CELL_SIZE = 100; // cell width and height (square)
   public static final int CANVAS_WIDTH = CELL_SIZE * COLS;  // the drawing canvas
   public static final int CANVAS_HEIGHT = CELL_SIZE * ROWS;
   public static final int GRID_WIDTH = 8;                   // Grid-line's width
   public static final int GRID_WIDHT_HALF = GRID_WIDTH / 2; // Grid-line's half-width
   
   // Symbols (cross/nought) are displayed inside a cell, with padding from border
   public static final int CELL_PADDING = CELL_SIZE / 6;
   public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2; // width/height
   public static final int SYMBOL_STROKE_WIDTH = 8; // pen's stroke width

   public final Seed EMPTY_SEED = new Empty();
   public final Seed CROSS_SEED = new Cross();
   public final Seed NOUGHT_SEED = new Nought();
   
   public final GameState PLAYING_STATE = new PlayingState();
   public final GameState WON_STATE = new WonState();
   public final GameState DRAW_STATE = new DrawState();
 
   // Use an abstract class (inner class) to represent the seeds and cell contents
   abstract class Seed {
      protected char _symbol;
      protected Color _color;
      protected int _x;
      protected int _y;

      public boolean equals(Seed other) {
          return (_symbol == other.symbol());
      }

      public char symbol() {
          return _symbol;
      }

       public void draw(Graphics2D g2d, int row, int col) {
           g2d.setColor(_color);
           _x = col * CELL_SIZE + CELL_PADDING;
           _y = row * CELL_SIZE + CELL_PADDING;
       }
   }

   class Empty extends Seed {
        public Empty() {
            _symbol = '\0';
            _color = Color.WHITE;
        }

        public void draw(Graphics2D g2d, int row, int col) {
            super.draw(g2d, row, col);
        }
    }

    class Cross extends Seed {
        public Cross() {
            _symbol = 'X';
            _color = Color.RED;
        }

        public void draw(Graphics2D g2d, int row, int col) {
            super.draw(g2d, row, col);
            int x2 = (col + 1) * CELL_SIZE - CELL_PADDING;
            int y2 = (row + 1) * CELL_SIZE - CELL_PADDING;
            g2d.drawLine(_x, _y, x2, y2);
            g2d.drawLine(x2, _y, _x, y2);
        }
    }

    class Nought extends Seed {
        public Nought() {
            _symbol = 'O';
        }

        public void draw(Graphics2D g2d, int row, int col){
            super.draw(g2d,row,col);
            g2d.drawOval(_x, _y, SYMBOL_SIZE, SYMBOL_SIZE);
        }
    }

    // Use an abstract class (inner class) to represent the various states of the game
    abstract class GameState {
        abstract void updateStatus(JLabel statusBar, Seed currentPlayer);
    }

    class PlayingState extends GameState {
        void updateStatus(JLabel statusBar, Seed currentPlayer) {
            statusBar.setForeground(Color.BLACK);
            statusBar.setText(currentPlayer.symbol() + "'s Turn");
        }
    }

    class DrawState extends GameState {
        void updateStatus(JLabel statusBar, Seed currentPlayer) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("It's a Draw! Click to play again.");
        }
    }

    class WonState extends GameState {
        void updateStatus(JLabel statusBar, Seed currentPlayer) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'" + currentPlayer.symbol() + "' Won! Click to play again.");
        }
    }

   private Seed currentPlayer;  // the current player
   private Seed[][] board   ; // Game board of ROWS-by-COLS cells
   private GameState currentState;  // the current game state
   private DrawingCanvas canvas; // Drawing canvas (JPanel) for the game board
   private JLabel statusBar;  // Status Bar
 
   /** Constructor to setup the game and the GUI components */
   public TTTGraphics2P() {
      canvas = new DrawingCanvas();  // Construct a drawing canvas (a JPanel)
      canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
      addMouseListener();
 
      // Setup the status bar (JLabel) to display status message
      statusBar = new JLabel("  ");
      statusBar.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 15));
      statusBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));
 
      Container cp = getContentPane();
      cp.setLayout(new BorderLayout());
      cp.add(canvas, BorderLayout.CENTER);
      cp.add(statusBar, BorderLayout.PAGE_END); // same as SOUTH
 
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      pack();  // pack all the components in this JFrame
      setTitle("Tic Tac Toe");
      setVisible(true);  // show this JFrame
 
      board = new Seed[ROWS][COLS]; // allocate array
      initGame(); // initialize the game board contents and game variables
   }
 
   /** Add mouse listener to drawing canvas **/
   private void addMouseListener() {
     // The canvas (JPanel) fires a MouseEvent upon mouse-click
      canvas.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {  // mouse-clicked handler
            int mouseX = e.getX();
            int mouseY = e.getY();
            // Get the row and column clicked
            int rowSelected = mouseY / CELL_SIZE;
            int colSelected = mouseX / CELL_SIZE;
 
            if (currentState == PLAYING_STATE) {
               if (rowSelected >= 0 && rowSelected < ROWS && colSelected >= 0 &&
                   colSelected < COLS && board[rowSelected][colSelected].equals(EMPTY_SEED)) {
                  board[rowSelected][colSelected] = currentPlayer; // Make a move
                  updateGame(currentPlayer, rowSelected, colSelected); // update state
                  // Switch player
                  currentPlayer = (currentPlayer.equals(CROSS_SEED) ? NOUGHT_SEED : CROSS_SEED);
               }
            } else {       // game over
               initGame(); // restart the game
            }
            // Refresh the drawing canvas
            repaint();  // Call-back paintComponent().
         }
      });
   }
 
   /** Initialize the game-board contents and the status */
   public void initGame() {
      for (int row = 0; row < ROWS; ++row) {
         for (int col = 0; col < COLS; ++col) {
            board[row][col] = EMPTY_SEED; // all cells empty
         }
      }
      currentState = PLAYING_STATE; // ready to play
      currentPlayer = CROSS_SEED;       // cross plays first
   }
 
   /** Update the currentState after the player with "theSeed" has placed on
       (rowSelected, colSelected). */
   public void updateGame(Seed theSeed, int rowSelected, int colSelected) {
      if (isWon(theSeed, rowSelected, colSelected)) {  // check for win
         currentState = WON_STATE;
      } else if (isDraw()) {  // check for draw
         currentState = DRAW_STATE;
      }
      // Otherwise, no change to current state (still PLAYING_STATE).
   }
 
   /** Return true if it is a draw (i.e., no more empty cell) */
   public boolean isDraw() {
      for (int row = 0; row < ROWS; ++row) {
         for (int col = 0; col < COLS; ++col) {
            if (board[row][col] == EMPTY_SEED) {
               return false; // an empty cell found, not draw, exit
            }
         }
      }
      return true;  // no more empty cell, it's a draw
   }
 
   /** Return true if the player with "theSeed" has won after placing at
       (rowSelected, colSelected) */
   public boolean isWon(Seed theSeed, int rowSelected, int colSelected) {
     return isRowWon(theSeed, rowSelected) ||                      // 3-in-the-row
            isColWon(theSeed, colSelected) ||                     // 3-in-the-column
            isFwdDiagWon(theSeed, rowSelected, colSelected) ||    // 3-in-the-diagonal
            isRevDiagWon(theSeed, rowSelected, colSelected);      // 3-in-the-opposite-diagonal
   }
 
   /** Return true if seed player has won by placing an entire row **/
   boolean isRowWon(Seed seed, int row) {
     boolean result = (board[row][0] == seed);
     for (int col = 1; col < COLS; col++) {
       result &= (board[row][col].equals(seed));
     }
     return result;
   }

    /** Return true if seed player has won by placing an entire column **/
    boolean isColWon(Seed seed, int col) {
        boolean result = (board[0][col] == seed);
        for (int row = 1; row < ROWS; row++) {
            result &= (board[row][col].equals(seed));
        }
        return result;
    }

    /** Return true if seed player has won by placing a forward diagonal **/
    boolean isFwdDiagWon(Seed seed, int row, int col) {
        boolean result = (row == col);
        if (result) {
            for (int diag = 0; diag < ROWS; diag++) {
                result &= (board[diag][diag].equals(seed));
            }
        }
        return result;
    }

    /** Return true if seed player has won by placing a reverse diagonal **/
    boolean isRevDiagWon(Seed seed, int row, int col) {
        boolean result = (row + col == ROWS - 1);
        if (result) {
            for (int diag = 0; diag < ROWS; diag++) {
                int r = diag;
                int c = COLS - diag - 1;
                result &= (board[r][c].equals(seed));
            }
        }
        return result;
    }

   /**
    *  Inner class DrawingCanvas (extends JPanel) used for custom graphics drawing.
    */
   class DrawingCanvas extends JPanel {
      @Override
      public void paintComponent(Graphics g) {  // invoke via repaint()
         super.paintComponent(g);    // fill background
         setBackground(Color.WHITE); // set its background color
 
         drawGridLines(g);
         drawSeeds(g);
         updateStatusBar();
      }

      /** Draw grid lines on Tic-Tac-Toe board */
      private void drawGridLines(Graphics g) {
        // Draw the grid-lines
         g.setColor(Color.LIGHT_GRAY);
         for (int row = 1; row < ROWS; ++row) {
            g.fillRoundRect(0, CELL_SIZE * row - GRID_WIDHT_HALF,
                  CANVAS_WIDTH-1, GRID_WIDTH, GRID_WIDTH, GRID_WIDTH);
         }
         for (int col = 1; col < COLS; ++col) {
            g.fillRoundRect(CELL_SIZE * col - GRID_WIDHT_HALF, 0,
                  GRID_WIDTH, CANVAS_HEIGHT-1, GRID_WIDTH, GRID_WIDTH);
         }
      }

      /** Draw seed shapes **/
      private void drawSeeds(Graphics g) {
        // Draw the Seeds of all the cells if they are not empty
         // Use Graphics2D which allows us to set the pen's stroke
         Graphics2D g2d = (Graphics2D)g;
         g2d.setStroke(new BasicStroke(SYMBOL_STROKE_WIDTH, BasicStroke.CAP_ROUND,
               BasicStroke.JOIN_ROUND));  // Graphics2D only
         for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                board[row][col].draw(g2d, row, col);
            }
         }
      }

      /** Update status bar based on game state **/
      private void updateStatusBar() {
          // Print status-bar message
          currentState.updateStatus(statusBar, currentPlayer);
      }
   }
 
   /** The entry main() method */
   public static void main(String[] args) {
      // Run GUI codes in the Event-Dispatching thread for thread safety
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            new TTTGraphics2P(); // Let the constructor do the job
         }
      });
   }
}
