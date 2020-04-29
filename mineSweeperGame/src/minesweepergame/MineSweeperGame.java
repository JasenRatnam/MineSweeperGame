/*
 * Jasen Ratnam
 * Program for Computer Science with Maths.
 * In Vanier College.
 */
package minesweepergame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

/**
 * The mine sweeper Game.
 * Starts the board, places the mines and check if the mines are clicked.
 * If a mine is clicked, disable every buttons on board and end game, add one to gameLost in main_Window.
 * if no mine is clicked, disable all buttons and end game, add one to Game one.
 * If 0 value button is clicked propagate until a number is shown.
 * Assignment 1
 * Date : 2017-09-20
 * @author Jasen Ratnam
 */
public class MineSweeperGame extends JFrame
{

    /**
     * Create an array of buttons that makes the game board.
     */
    protected JButton[][] boardGUI;

    /**
     * Create an int array that will hold the values of each button.
     */
    protected int [][] boardGUIValue;
    
    // The Side of the board.
    private final int side = 8;
    
    // The limit that a loop can go to to not overcharge the array.
    private final int limit = side-2;
    
    // ArrayLists to hold the X and Y values of each mine.
    private ArrayList<Integer> minesLocationx;
    private ArrayList<Integer> minesLocationy;
    
    // Number of buttons are not clicked.
    private int unopenedButton = side * side; 
    
    // Number of bombs in the Game.
    private int numberOfBombs = 10;
    
    private boolean DEBUGGING = false;

    /**
     * Constructor of the MineSeeper Game.
     * Creates the Game GUI. 
     */
    protected MineSweeperGame()
    {
        // Set title of Game.
        // Set size of Game.
        super("Mine Sweeper Game!");
        setSize(600, 600);
        
        Container pane = getContentPane();
        pane.setLayout(new GridLayout(8, 8));
        
        // Set the size of the board buttons and its values.
        boardGUI = new JButton[8][8];
        boardGUIValue = new int [8][8];
        
        // Set meassage on main Window to "game in progress".
        MineSweeperGame_MainWindow.setMessage("Game In Progress!");
        
        // intialize the vlaues of the buttons to 0.
        for(int i = 0; i<side; i++)
        {
            for(int j = 0; j<side; j++)
            {
                boardGUIValue[i][j] = 0;
            }
        }
        
         
        for (int i=0; i<8; ++i)
        {
            for (int j=0; j<8; ++j)
            {
                // Create the buttons.
                boardGUI[i][j] = new JButton();
                // Set the font of the text on the butons.
                boardGUI[i][j].setFont(new Font("Tahoma", Font.BOLD, 13));
                
                final int x = i;
                final int y = j;
                
                // If a button is clicked:
                boardGUI[i][j].addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent ae) 
                    {
                       // Decrement the amount of buttons that are not clicked.
                       unopenedButton--;
                       isGameWon();
                       
                       // Disable the button that has been clicked.
                       boardGUI[x][y].setEnabled(false);
                       
                       // Play click sound
                       try
                           {
                               // Get sound clip and play it when a mine is clicked.
                                File file = new File("sounds/Click.wav");
                                AudioInputStream ais = AudioSystem.getAudioInputStream(file);
                                Clip clip = AudioSystem.getClip();

                                clip.open(ais);
                                clip.start();
                           }
                           catch(Exception e)
                           {
                                MineSweeperGame_MainWindow.setMessage("Error loading sound!");
                           }
                       
                       // Set the value of the button as the text on the button.
                       boardGUI[x][y].setText(Integer.toString(boardGUIValue[x][y]));
                      
                       // If the value of the button is a zero, make it propagate until a non-zero button is found.
                        if(boardGUI[x][y].getText().equals(Integer.toString(0)))
                        {
                            revealZeros(x,y);
                        }
                       
                      // If the value of the button is "-1", a Mine as been clicked.
                      // Set the ext of the button to "MINE!" and its color to red.
                      // Disable all other buttons and and game.
                       if(boardGUI[x][y].getText().equals(Integer.toString(-1)))
                       {
                           try
                           {
                               // Get sound clip and play it when a mine is clicked.
                                File file = new File("sounds/Click (2).wav");
                                AudioInputStream ais = AudioSystem.getAudioInputStream(file);
                                Clip clip = AudioSystem.getClip();

                                clip.open(ais);
                                clip.start();
                           }
                           catch(Exception e)
                           {
                                MineSweeperGame_MainWindow.setMessage("Error loading sound!");
                           }
                           
                           //Set the button image as a bomb image.
                           URL bombURL = getClass().getClassLoader().getResource("images/bomb.jpg");
                           ImageIcon icon = new ImageIcon(bombURL);
                           boardGUI[x][y].setText("");
                           boardGUI[x][y].setIcon(icon);
                          
                           for (int i=0; i<8; ++i)
                           {
                               for (int j=0; j<8; ++j)
                               {
                                   boardGUI[i][j].setEnabled(false);
                                   boardGUI[i][j].setText(Integer.toString(boardGUIValue[i][j]));
                                   if(boardGUI[i][j].getText().equals(Integer.toString(-1)))
                                   {
                                       boardGUI[i][j].setText("");
                                       boardGUI[i][j].setIcon(icon);
                                       boardGUI[i][j].setEnabled(true);
                                   }
                       
                               }
                           }
                           
                           // Add the amounts of rounds lost in the main Window.
                           MineSweeperGame_MainWindow.gameLost();
                       }
                    
                       if(DEBUGGING)
                       {
                           MineSweeperGame_MainWindow.setMessage(Integer.toString(unopenedButton));
                       }   //=====> used to see if the amount of buttons that is not clicked is correct. 

                    }
                });
                
                // If a rigth click is done set a flag on button.
                // Used if user thinks that the button is a bomb.
                boardGUI[x][y].addMouseListener(new MouseAdapter()
                {
                    @Override
                    public void mousePressed(MouseEvent e) 
                    {
                        if (e.getButton() == MouseEvent.BUTTON3) 
                        {
                            boardGUI[x][y].setText("MINE!");
                        }
                    }
                });
                
                // Add all buttons to the pane/
                pane.add(boardGUI[i][j]);  
            }
        }
      
        // Make the board visible.
        setVisible(true);
        
        // Initalise minesLocationx and minesLocationy.
        minesLocationx = new ArrayList<>();
        minesLocationy = new ArrayList<>();
        
        // Ints to save random value of x and y location of the mine,temporaly. 
        int random1;
        int random2;
        
        for(int i = 0; i<numberOfBombs;)
        {
            // Find two random numbers.
            random1 = (int)(Math.random()* (side));
            random2 = (int)(Math.random()* (side));
            if(!(minesLocationx.contains(random1)) || !(minesLocationy.contains(random2)))
            {
                // Add the random numbers to the arrayList minesLocationx and minesLocationy.
                minesLocationx.add(random1);
                minesLocationy.add(random2);
                
                // Add the values of the button at the location minesLocationx and minesLocationy "-1".
                boardGUIValue[minesLocationx.get(i)][minesLocationy.get(i)] = -1;
                i++;
            }
        
            
        } 
        
        // Set the value of the buttons that are near a mine.
        setValuesOfButtonNearMine();

        if(DEBUGGING)
        {
//          Use this code to show the placements of the bombs on the board whil playing for testing purposes.
            for(int x = 0; x<8; x++)
            {
                for(int y = 0; y<8; y++)
                {
                    boardGUI[x][y].setText(Integer.toString(boardGUIValue[x][y]));

                }
            }
        }

    } 
    
    /**
     * Check if the game is won.
     * Calls the gameWon() method in the main window class.
     * Disable all the buttons.
     */
    public void isGameWon()
   {
       if(unopenedButton == minesLocationx.size())
       {
           MineSweeperGame_MainWindow.gameWon();
           
           for(int x = 0; x<8; x++)
            {
                for(int y = 0; y<8; y++)
                {
                    boardGUI[x][y].setEnabled(false);
                }
            }
           

       }
       
   }
    
     /**
     * If a button value is zero this method is called and it reveals all buttons.
     * if a revealed button is again zero, the button propagate until a non-zero button is found.
     */
    private void revealZeros(int x, int y) 
    {
        boardGUI[x][y].doClick(5);
        if(boardGUI[x][y].getText().equals(Integer.toString(0)))
        {
            if (x > 0) 
            {
                if (y > 0)
                {
                    if (boardGUI[x - 1][y - 1].isEnabled())
                    {
                        revealZeros(x - 1, y - 1);
                    }
                }
                if (y < limit) 
                {
                    if (boardGUI[x - 1][y + 1].isEnabled())
                    {
                        revealZeros(x - 1, y + 1);
                    }
                }
                if (boardGUI[x - 1][y].isEnabled())
                {
                    revealZeros(x - 1, y);
                }
            }
            if (y > 0)
            {
                if (boardGUI[x][y - 1].isEnabled())
                {
                    revealZeros(x, y - 1);
                }
            }
            if (y < limit)
            {
                if (boardGUI[x][y + 1].isEnabled())
                {
                    revealZeros(x, y + 1);
                }
            }
            if (x < limit)
            {
                if (y > 0)
                {
                    if (boardGUI[x + 1][y - 1].isEnabled())
                    {
                        revealZeros(x + 1, y - 1);
                    }
                }
                if (y < limit)
                {
                    if (boardGUI[x + 1][y + 1].isEnabled())
                    {
                        revealZeros(x + 1, y + 1);
                    }
                }
                if (boardGUI[x + 1][y].isEnabled())
                {
                    revealZeros(x + 1, y);
                }
            }
        }


    }
    
    /**
     * Set the values of the button near a mines.
     * It shows how many mines are around a mine.
     */
    public void setValuesOfButtonNearMine()
    {
        for(int i = 0; i<side; i++)
        {
            for(int j = 0; j<side; j++)
            {
                 if(boardGUIValue[i][j] != -1)
                 {
                     if(j>=1 && boardGUIValue[i][j-1] == -1)
                     {
                         boardGUIValue[i][j] += 1; 
                     }
                     if(j<= limit && boardGUIValue[i][j+1] == -1) 
                     {
                         boardGUIValue[i][j] += 1;
                     }
                     if(i>=1 && boardGUIValue[i-1][j] == -1) 
                     {
                         boardGUIValue[i][j] += 1;
                     }
                     if(i<= limit && boardGUIValue[i+1][j] == -1)
                     {
                         boardGUIValue[i][j] += 1;
                     }
                     if(i>=1 && j>= 1 && boardGUIValue[i-1][j-1]== -1)
                     {
                         boardGUIValue[i][j] += 1;
                     }
                     if(i<= limit && j<= limit && boardGUIValue[i+1][j+1] == -1)
                     {
                         boardGUIValue[i][j] += 1;
                     }
                     if(i>=1 && j<= limit && boardGUIValue[i-1][j+1] == -1)
                     {
                         boardGUIValue[i][j] += 1;
                     }
                     if(i<= limit && j>= 1 && boardGUIValue[i+1][j-1] == -1)
                     {
                         boardGUIValue[i][j] += 1;
                     }
                 }
            }
        }
    }
}
   
