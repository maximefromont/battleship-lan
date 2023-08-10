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
      System.out.println("Please enter the server ip adress : ");
      String ip_adress = scanner.next();
      try
      {
        _socket             = new Socket(ip_adress, 10);
        _data_input_stream  = new DataInputStream(_socket.getInputStream());
        _data_output_stream = new DataOutputStream(_socket.getOutputStream());
        valid_ip_adress = true;
      } catch (IOException exception)
      {
        System.out.println("Please make sure the IP adress is correctly written or ensure the host started their server before you try connecting.");
      }
    }

    runClientBattleshipGame();
  }

  //PRIVATE INTERFACE
  private void runClientBattleshipGame()
  {
    System.out.println("You are now connected with the host. Use \"ctrl+c\" to exit at any moment.");

    Game game = new Game(_data_input_stream, _data_output_stream);

    game.initDefenseBoats();

    //Game loop
    while(true)
    {
      game.launchAttackTurn();
      game.launchDefenseTurn();
    }
  }

  //PRIVATE ATTRIBUTES
  Socket           _socket;
  DataInputStream  _data_input_stream;
  DataOutputStream _data_output_stream;
}
