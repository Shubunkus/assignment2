// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
    	if (message.startsWith("#")) {
    		handleCommand(message.substring(1));
    	} else {
    		sendToServer(message);
    	}
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  private void handleCommand(String command) {
	  if (command.startsWith("sethost")) {
		  if (isConnected()) {
			  clientUI.display("Cannot set host while logged on.");
		  } else {
			  setHost(command.substring(command.indexOf(' ')+1));
		  }
	  } else if (command.startsWith("setport")) {
		  if (isConnected()) {
			  clientUI.display("Cannot set port while logged on.");
		  } else {
			  setPort(Integer.parseInt(command.substring(command.indexOf(' ')+1)));
		  }
	  } else {
	  
		  switch (command) {
		  case "quit":
			  quit();
			  break;
			  
		  case "logoff":
			  try {
				  closeConnection();
			  } catch (IOException e) {}
			  break;
			  
		  case "login":
			  if (isConnected()) {
				  clientUI.display("Already connected");
			  } else {
				  try {
					  openConnection();
				  } catch (IOException e) {}
			  }
			  break;
			  
		  case "gethost":
			  clientUI.display(getHost());
			  break;
			  
		  case "getport":
			  clientUI.display(String.valueOf(getPort()));
			  break;
			  
		  default:
			  clientUI.display("Command " + command + " not found");
			  break;
		  }
	  }
	  
}
	  
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  /**
	 * Implements hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
  	@Override
	protected void connectionException(Exception exception) {
  		clientUI.display("The server has shut down.");
  		System.exit(0);
	}
  	
  	/**
	 * Implements hook method called after the connection has been closed. The default
	 * implementation does nothing.
	 */
  	@Override
	protected void connectionClosed() {
		clientUI.display("Connection closed.");
		//System.exit(0);
	}
}
//End of ChatClient class
