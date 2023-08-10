import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client
{
  //PUBLIC INTERFACE
  public Client()
  {
    boolean valid_ip_adress = false;
    while (!valid_ip_adress)
    {
      Scanner scanner = new Scanner(System.in);
      System.out.println("Please enter the server ip address : ");
      String ip_adress = scanner.next();
      try
      {
        _socket             = new Socket(ip_adress, 10);
        _data_input_stream  = new DataInputStream(_socket.getInputStream());
        _data_output_stream = new DataOutputStream(_socket.getOutputStream());
        valid_ip_adress = true;
      } catch (IOException exception)
      {
        System.out.println("Please make sure the IP address is correctly written or ensure the host started their server before you try connecting.");
      }
    }

    runClientBattleshipGame();

    try
    {
      _socket.close();
      System.out.println("You have been successfully disconnected from the host.");
    }
    catch (IOException exception)
    {
      System.out.println("Something went wrong when trying to close the connection. Please report it to the developers.");
    }
  }

  //PRIVATE INTERFACE
  private void runClientBattleshipGame()
  {
    System.out.println("You are now connected with the host. Use \"ctrl+c\" to exit at any moment.");

    Game game = new Game(_data_input_stream, _data_output_stream);

    game.initDefenseBoats();

    //Game loop
    while(!game.isFinished())
    {
      game.launchAttackTurn();

      if(!game.isFinished())
        game.launchDefenseTurn();
    }

    game.printEndMessage();
  }

  //PRIVATE ATTRIBUTES
  Socket           _socket;
  DataInputStream  _data_input_stream;
  DataOutputStream _data_output_stream;
}
