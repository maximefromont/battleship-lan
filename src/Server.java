import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server
{
  //PUBLIC INTERFACE
  public Server()
  {
    try
    {
      _server_socket = new ServerSocket(10);
      System.out.println("The hosting server is now up.");

      _socket             = _server_socket.accept();

      _data_input_stream  = new DataInputStream(_socket.getInputStream());
      _data_output_stream = new DataOutputStream(_socket.getOutputStream());
    } catch (IOException exception)
    {
      System.out.println("Something went wrong with the server or the client connection. Please try again.");
    }

    serverBattleshipGame();

    try {
      _socket.close();
      System.out.println("You have been successfully disconnected from the client.");
      _server_socket.close();
      System.out.println("The hosting server is now down.");
    }
    catch (IOException exception)
    {
      System.out.println("Something went wrong when trying to close the server. Please report it to the developers.");
    }
  }

  //PRIVATE INTERFACE
  private void serverBattleshipGame()
  {
    System.out.println("You are now connected with the client. Use \"ctrl+c\" to exit at any moment.");

    Game game = new Game(_data_input_stream, _data_output_stream);

    game.initDefenseBoats();

    //Game loop
    while (!game.isFinished())
    {
      game.launchDefenseTurn();

      if(!game.isFinished())
        game.launchAttackTurn();
    }

    game.printEndMessage();
  }

  //PRIVATE ATTRIBUTES
  ServerSocket     _server_socket;
  Socket           _socket;
  DataInputStream  _data_input_stream;
  DataOutputStream _data_output_stream;
}
