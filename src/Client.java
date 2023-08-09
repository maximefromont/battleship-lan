import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client
{

  Socket          socket;
  DataInputStream data_input_stream;
  DataOutputStream data_output_stream;
  public Client()
  {
    _scanner = new Scanner(System.in);
    System.out.println("This step will not be verified. Please make sure the following answer is correctly written.");
    System.out.println("Please enter the server ip adress : ");
    String ip_adress = _scanner.next();

    try
    {
      socket             = new Socket(ip_adress,10);
      data_input_stream  = new DataInputStream(socket.getInputStream());
      data_output_stream = new DataOutputStream(socket.getOutputStream());

      clientBattleshipGame();
    }
    catch(Exception e)
    {
      System.out.println(e);
    }
  }

  private void clientBattleshipGame() throws IOException
  {
    BattleshipArray attack_array = new BattleshipArray(Main.BATTLESHIP_ARRAY_SIZE);
    BattleshipArray defense_array = new BattleshipArray(Main.BATTLESHIP_ARRAY_SIZE);

    System.out.println("You are now connected with the host. Enter \"exit\" to exit.");

    defense_array.placeRandomBoat(Main.BATTLESHIP_BOATS_TYPES);

    String out_message = "ERROR OUT_MESSAGE";
    String in_message = "ERROR IN_MESSAGE";
    do
    {
      //Client turn
      boolean replay = false;
      do
      {
        System.out.println("You defense array : ");
        System.out.println(defense_array.toString());
        System.out.println("You attack array : ");
        System.out.println(attack_array.toString());

        //Attack attempt
        System.out.print("Where do you want to attack in form of one letter + a number from 1 to 10 (Example : A8 or A10) : ");
        out_message = _scanner.next();
        data_output_stream.writeUTF(out_message);
        data_output_stream.flush();

        //Response from enemy
        in_message = data_input_stream.readUTF();
        int[] coordinates = BattleshipArray.getCoordinateFromBattleshipCoordinate(out_message);
        attack_array.setArrayCell(coordinates[0], coordinates[1], Integer.parseInt(in_message));
        switch (Integer.parseInt(in_message))
        {
          case BattleshipArray.MISS_CELL:
            System.out.println("It's a miss.");
            replay = false;
            break;
          case BattleshipArray.HIT_CELL:
            System.out.println("It's a hit !");
            replay = true;
            break;
        }
      } while (replay == true);
      data_output_stream.writeUTF("endOfTurn");
      data_output_stream.flush();

      //Attack from enemy
      while (!in_message.equals("endOfTurn"))
      {
        in_message = data_input_stream.readUTF();
        if(!in_message.equals("endOfTurn"))
        {
          int[] coordinates = BattleshipArray.getCoordinateFromBattleshipCoordinate(in_message);
          if (defense_array.getArrayCell(coordinates[0], coordinates[1]) >= BattleshipArray.SUBMARINE_CELL)
          {
            defense_array.setArrayCell(coordinates[0], coordinates[1], BattleshipArray.BOAT_HIT_CELL);
            data_output_stream.writeUTF(String.valueOf(BattleshipArray.HIT_CELL));
          }
          else
          {
            defense_array.setArrayCell(coordinates[0], coordinates[1], BattleshipArray.MISS_CELL);
            data_output_stream.writeUTF(String.valueOf(BattleshipArray.MISS_CELL));
          }


          data_output_stream.flush();
        }
      };
    }
    while(!out_message.equals("exit"));
  }

  private Scanner _scanner;



}
