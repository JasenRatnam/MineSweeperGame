/*
 * Jasen Ratnam
 * Program for Computer Science with Maths.
 * In Vanier College.
 */
package minesweepergame;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

/**
 * Main_Window of the mine sweeper Game.
 * Has a New Game button to start a new game.
 * The game Lost or the game Won are added after each round.
 * Message Bar shows the state of the game.
 * Assignment 1
 * Date : 2017-09-20
 * @author Jasen Ratnam
 */
public class MineSweeperGame_MainWindow extends JFrame
{
    // Singleton
    private static MineSweeperGame_MainWindow instance = null;

    // Swing Components:
    private JLabel Games_Won;           // Label to show the amount of games Won.
    private JLabel Games_Lost;          // Label to show the amount of game Lost.
        
    private JButton NewGame_Button;     // Button to start a New MineSweeper Game.
    
    private JLabel messageLabel;        // Label to give messages while game is being played.
    
    private static int gameWon = 0;      // Int to save the amount of rounds Won.
    
    private static int gameLost = 0;     // Int to save amount of rounds Loss.
    
    // Mine Sweeper Game!
    private MineSweeperGame game = null; // The actual game to be played by the main window.
    
    /**
     * Constructor of the Main Window.
     * Creates the Window GUI with the new GAme button, the game lost and game won labels and the message bar.
     */
    protected MineSweeperGame_MainWindow()
    {
        // Set title of Main Window.
        // Set size of Window.
        super("Mine Sweeper Menu!");
        setSize(400, 300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        Games_Won = new JLabel("Games Won: " + gameWon);     // Set Label.
        Games_Lost = new JLabel("Games Lost: " + gameLost);  // Set Label.
        
        messageLabel = new JLabel("Press 'New Game' to start!");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setVerticalAlignment(SwingConstants.CENTER);
        
        NewGame_Button = new JButton("New Game");
        
        // If the New Game Button is pressed start a new MineSweeper Game.
        // Disable the button.
        NewGame_Button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae) 
            {
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
                game = new MineSweeperGame();
                NewGame_Button.setEnabled(false);
            }
        });
        
        
        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());
        
        // Add the buttons and Labels to the pane.
        pane.add(Games_Won, BorderLayout.LINE_START);
        pane.add(NewGame_Button, BorderLayout.CENTER);
        pane.add(Games_Lost, BorderLayout.LINE_END);
        pane.add(messageLabel, BorderLayout.PAGE_END);
        
        // Create Menu Bar and Menu Category (File)
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menuBar.add(menu);

        // Create Menu item
        // - add action listener
        // - add shortcut key (CTRL-Q)
        // - add to menu
        JMenuItem menuItem = new JMenuItem("Quit");
        menuItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                dispatchEvent(new WindowEvent(MineSweeperGame_MainWindow.this, WindowEvent.WINDOW_CLOSING));
            }
        });
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
        menu.add(menuItem);
        
        // The following adds the menu bar to the JFrame
        setJMenuBar(menuBar);
        
        instance = this;
    }
    
    /**
     * Increments the amount of Game Won when called.
     * Refresh The Label in the Window.
     */
    public static void gameWon()
    {
        gameWon++;
        setMessage("You Won!");
        enableNewGameButton();
        
       if (instance != null)
        {
            instance.Games_Won.setText("Games Won: " + gameWon);
        }
    }
    
    /**
     * Increments the amount of Game Lost when called.
     * Refresh The Label in the Window.
     */
    public static void gameLost()
    {
       enableNewGameButton();
       setMessage("You Lost!");
       gameLost++;

       if (instance != null)
        {
            instance.Games_Lost.setText("Games Lost: " + gameLost);
        }
    }
    
    /**
     * Change the message on the message bar with the message given.
     * @param message Message given.
     */
    public static void setMessage(String message)
    {
        if (instance != null)
        {
            instance.messageLabel.setText(message);
        }
    }
    
    /**
     * Enables the NewGame button when called.
     */
    public static void enableNewGameButton()
    {
        if (instance != null)
        {
            instance.NewGame_Button.setEnabled(true);
        }
    }
    
    /**
     * Main method that starts the main window and makes it visible.
     * @param args
     */
    public static void main(String[] args) 
    {
        MineSweeperGame_MainWindow Window = new MineSweeperGame_MainWindow();
        Window.setVisible(true);
    }   
}
