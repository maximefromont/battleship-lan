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
      _socket        = _server_socket.accept();
      System.out.println("The hosting server is up.");
      _data_input_stream  = new DataInputStream(_socket.getInputStream());
      _data_output_stream = new DataOutputStream(_socket.getOutputStream());
    } catch (IOException exception)
    {
      System.out.println("Something went wrong with the server or the client connection. Please try again.");
    }

    serverBattleshipGame();
  }

  //PRIVATE INTERFACE
  private void serverBattleshipGame()
  {
    System.out.println("You are now connected with the client. Use \"ctrl+c\" to exit at any moment.");

    Game game = new Game(_data_input_stream, _data_output_stream);

    game.initDefenseBoats();

    //Game loop
    while (true)
    {
      game.launchDefenseTurn();
      game.launchAttackTurn();
    }
  }

  //PRIVATE ATTRIBUTES
  ServerSocket     _server_socket;
  Socket           _socket;
  DataInputStream  _data_input_stream;
  DataOutputStream _data_output_stream;
}
