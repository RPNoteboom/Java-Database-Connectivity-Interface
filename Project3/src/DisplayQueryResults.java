// Richard Noteboom

import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;


public class DisplayQueryResults extends JPanel 
{
   // default query retrieves all data from bikes table
   static final String DEFAULT_QUERY = "SELECT * FROM bikes";
   
   private ResultSetTableModel tableModel;
   private JTextArea queryArea;
   private JButton connectButton, clearCButton, clearRButton, executeButton;
   private JLabel queryLabel, dbInfoLabel, jdbcLabel, urlLabel, userLabel, passwordLabel, statusLabel, windowLabel;
   private JComboBox driverCombo;
   private JComboBox urlCombo;
   private JTextField userText;
   private JPasswordField passwordText;
   private Connection connect;
   private TableModel empty;
   private JTable resultTable;
   
   
   // create ResultSetTableModel and GUI
   public DisplayQueryResults() 
   {   
	  //dropdown menu items
	   String[] driverItems = {"com.mysql.cj.jdbc.Driver"};
	   String[] urlItems = {"jdbc:mysql://localhost:3306/project3?useTimezone=true&serverTimezone=UTC"};
	  //GUI Components
	    //Buttons
	       // set up JButton for connecting to database
	       JButton connectButton = new JButton( "Connect to Database" );
	       connectButton.setBackground(Color.BLUE);
	       connectButton.setForeground(Color.YELLOW);
	       connectButton.setBorderPainted(false);
	       connectButton.setOpaque(true);
	       // set up JButton for clearing the command window
	       JButton clearCButton = new JButton( "Clear SQL Command" );
	       clearCButton.setBackground(Color.WHITE);
	       clearCButton.setForeground(Color.RED);
	       clearCButton.setBorderPainted(false);
	       clearCButton.setOpaque(true);
	       // set up JButton for executing commands
	       JButton executeButton = new JButton( "Execute SQL Command" );
	       executeButton.setForeground(Color.GREEN);
	       executeButton.setBorderPainted(false);
	       executeButton.setOpaque(true);
	       // set up JButton for clearing the table
	       JButton clearRButton = new JButton( "Clear Result Window" );
	       clearRButton.setBackground(Color.BLACK);
	       clearRButton.setForeground(Color.YELLOW);
	       clearRButton.setBorderPainted(false);
	       clearRButton.setOpaque(true);
	    //Labels
	       queryLabel = new JLabel();
	       queryLabel.setFont(new Font("Arial", Font.BOLD, 14));
	       queryLabel.setForeground(Color.BLUE);
	       queryLabel.setText("Enter an SQL Command");
	       dbInfoLabel = new JLabel();
	       dbInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));
	       dbInfoLabel.setForeground(Color.BLUE);
	       dbInfoLabel.setText("Enter Database Information");
	       jdbcLabel = new JLabel(" JDBC Driver");
	       jdbcLabel.setOpaque(true);
	       jdbcLabel.setForeground(Color.LIGHT_GRAY);
	       jdbcLabel.setBackground(Color.BLACK);
	       urlLabel = new JLabel(" Database URL");
	       urlLabel.setOpaque(true);
	       urlLabel.setForeground(Color.LIGHT_GRAY);
	       urlLabel.setBackground(Color.BLACK);
	       userLabel = new JLabel(" Username");
	       userLabel.setOpaque(true);
	       userLabel.setForeground(Color.LIGHT_GRAY);
	       userLabel.setBackground(Color.BLACK);
	       passwordLabel = new JLabel(" Password");
	       passwordLabel.setOpaque(true);
	       passwordLabel.setForeground(Color.LIGHT_GRAY);
	       passwordLabel.setBackground(Color.BLACK);
	       queryArea = new JTextArea (5, 5);
	       driverCombo = new JComboBox (driverItems);
	       urlCombo = new JComboBox (urlItems);
	       userText = new JTextField ("", 10);
	       passwordText = new JPasswordField ("", 10);
	       statusLabel = new JLabel("No Connection Now");
	       statusLabel.setOpaque(true);
	       statusLabel.setForeground(Color.RED);
	       statusLabel.setBackground(Color.BLACK);
	       windowLabel = new JLabel();
	       windowLabel.setFont(new Font("Arial", Font.BOLD, 14));
	       windowLabel.setForeground(Color.BLUE);
	       windowLabel.setText("SQL Execution Result Window");
	       resultTable = new JTable();
	       empty = new DefaultTableModel();
	     //Window Properties
	       setPreferredSize (new Dimension (880, 500));
	       setLayout (null);
	       final Box square = Box.createHorizontalBox();
	       square.add(new JScrollPane(resultTable));
	       Box sqlSquare = Box.createHorizontalBox();
	       sqlSquare.add(new JScrollPane(queryArea));
	       resultTable.setEnabled(false);
	       resultTable.setGridColor(Color.BLACK);
	    //Component Bounds
	       connectButton.setBounds(300, 187, 165, 25);
	       clearCButton.setBounds(482, 187, 175, 25);
	       executeButton.setBounds(670, 187, 175, 25);
	       clearRButton.setBounds(25, 460, 168, 25);
	       dbInfoLabel.setBounds(5, 0, 250, 25);
	       jdbcLabel.setBounds(5, 21, 85, 25);
	       urlLabel.setBounds(5, 49, 85, 25);
	       userLabel.setBounds(5, 78, 85, 25);
	       passwordLabel.setBounds(5, 107, 85, 25);
	       statusLabel.setBounds(5, 185, 290, 25);
	       windowLabel.setBounds(5, 211, 220, 25);
	       square.setBounds(5, 234, 841, 220);
	       queryLabel.setBounds(409, 0, 190, 25);
	       sqlSquare.setBounds(409, 22, 438, 125);
	       driverCombo.setBounds(90, 21, 290, 25);
	       urlCombo.setBounds(90, 49, 290, 25);
	       userText.setBounds(90, 78, 290, 25);
	       passwordText.setBounds(90, 106, 290, 25);
	       
	  //add components
	       add (connectButton);
	       add (clearCButton);
	       add (executeButton);
	       add (clearRButton);
	       add (dbInfoLabel);
	       add (jdbcLabel);
	       add (urlLabel);
	       add (userLabel);
	       add (passwordLabel);
	       add (statusLabel);
	       add (windowLabel);
	       add (square);
	       add (queryLabel);
	       add (sqlSquare);
	       add (driverCombo);
	       add (urlCombo);
	       add (userText);
	       add (passwordText);

 //Connect Button
         // create event listener for connectButton
         connectButton.addActionListener(
        	new ActionListener()
            {
        		public void actionPerformed( ActionEvent event )
        		{
                  // acquire connection
                  try 
                  {
                	  if(connect != null)
                		  connect.close();
                	  statusLabel.setText("No Connection Now");
                	  Class.forName((String)driverCombo.getSelectedItem());
                	  connect = DriverManager.getConnection((String)urlCombo.getSelectedItem(),userText.getText(),passwordText.getText());
                      statusLabel.setText("Connected to "+(String)urlCombo.getSelectedItem());
                	  //tableModel.setQuery( queryArea.getText() );
                  } // end try
                  catch ( SQLException e) 
                  {
                     JOptionPane.showMessageDialog( null, 
                        e.getMessage(), "Database error", 
                        JOptionPane.ERROR_MESSAGE );   
                  } // end e catch                   
                  catch (ClassNotFoundException classnotfound) 
                  {
                     JOptionPane.showMessageDialog( null, 
                        "MySQL Driver not found", "Driver not found", 
                        JOptionPane.ERROR_MESSAGE );   
                  } // end driver catch   
               } // end actionPerformed
            }  // end ActionListener inner class          
         ); // end call to addActionListener
//Clear Result Button
         // create event listener for clearRButton
         clearRButton.addActionListener(
        	new ActionListener()
            {
        		public void actionPerformed( ActionEvent event )
        		{
                //clear the result table
        		resultTable.setModel(empty);	
               } // end actionPerformed
            }  // end ActionListener inner class          
         ); // end call to addActionListener
//Clear Command Button
         // create event listener for clearCButton
         clearCButton.addActionListener(
        	new ActionListener()
            {
        		public void actionPerformed( ActionEvent event )
        		{
                //clear the result table
        		queryArea.setText("");	
               } // end actionPerformed
            }  // end ActionListener inner class          
         ); // end call to addActionListener
//Execute Button
      // create event listener for executeButton
         executeButton.addActionListener( 
            new ActionListener() 
            { // pass query to table model
               public void actionPerformed( ActionEvent event )
               { // perform a new query
                  try 
                  {
                	 resultTable.setEnabled(true);
                	 resultTable.setAutoscrolls(true);
                	 tableModel = new ResultSetTableModel(connect, queryArea.getText());
                     
                     if(queryArea.getText().toUpperCase().contains("SELECT"))
                     {
                    	 tableModel.setQuery(queryArea.getText());
                    	 resultTable.setModel(tableModel);
                     }
                     else 
                     {
                    	 tableModel.setUpdate(queryArea.getText());
                     }
                  } // end try
                  catch ( SQLException e) 
                  {
                     JOptionPane.showMessageDialog( null, 
                        e.getMessage(), "Database error", 
                        JOptionPane.ERROR_MESSAGE );   
                  } // end e catch                   
                  catch (ClassNotFoundException classnotfound) 
                  {
                     JOptionPane.showMessageDialog( null, 
                        "MySQL Driver not found", "Driver not found", 
                        JOptionPane.ERROR_MESSAGE );   
                  } // end driver catch   
               } // end actionPerformed
            }  // end ActionListener inner class          
         ); // end call to addActionListener
   } // end DisplayQueryResults constructor
   
   // execute application
   public static void main( String args[] ) 
   {
      JFrame frame = new JFrame("JDBC Project 3 - (RPN - CNT 4714 - Summer 2020)");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().add(new DisplayQueryResults());
      frame.pack();
      frame.setVisible(true);
   } // end main
} // end class DisplayQueryResults



