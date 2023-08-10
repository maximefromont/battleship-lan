import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class Game
{

  //PUBLIC INTERFACE
  public Game(DataInputStream data_input_stream, DataOutputStream data_output_stream)
  {
    _data_input_stream = data_input_stream;
    _data_output_stream = data_output_stream;
  }

  public void initDefenseBoats()
  {
    _defense_array.placeRandomBoat(BATTLESHIP_BOATS_TYPES);
  }

  public void launchAttackTurn()
  {
    String out_message = "ERROR OUT_MESSAGE";
    String in_message = "ERROR IN_MESSAGE";
    Scanner scanner = new Scanner(System.in);
    Coordinate attack_coordinate = null;

    boolean replay = false;
    do
    {
      System.out.println(""); //Give the UI a little bit of space
      System.out.println("Here is your ATTACK ARRAY : ");
      System.out.println(_attack_array.toString());

      //Attack attempt
      boolean valid_coordinate = false;
      while(!valid_coordinate)
      {
        System.out.print("Where do you want to attack in form of a letter between A and J and a number between 1 and 10 (A8, J10, ...) : ");
        out_message = scanner.next();
        try
        {
          attack_coordinate = new Coordinate(out_message);
          valid_coordinate = true;
          sendMessage(out_message);
        }
        catch(IllegalBattleshipCoordinateException e)
        {
          System.out.println("Please enter valid coordinates composed of a letter between A and J and a number between 1 and 10.");
        }
      }

      //Response from enemy
      in_message = recieveMessage();
      if(in_message.equals(ALREADY_HIT))
      {
        System.out.println("You already attacked those coordinates. Please try again.");
        replay = true;
      }
      else
      {
        int cell_value = Integer.parseInt(in_message);
        _attack_array.setArrayCell(attack_coordinate.get_x_coordinate(), attack_coordinate.get_y_coordinate(), cell_value);
        switch (cell_value)
        {
          case BattleshipArray.MISS_CELL:
            System.out.println("It's a MISS.");
            replay = false;
            break;
          case BattleshipArray.HIT_CELL:
            System.out.println("It's a HIT !");
            replay = true;
            break;
        }
      }
    } while (replay == true);
    sendMessage(END_OF_TURN);
  }

  public void launchDefenseTurn()
  {
    String out_message = "ERROR OUT_MESSAGE";
    String in_message = "ERROR IN_MESSAGE";
    Scanner scanner = new Scanner(System.in);
    Coordinate coordinate = null;

    System.out.println("");  //Give the UI a little bit of space
    System.out.println("Wait for your opponent to play.");
    while (!in_message.equals(END_OF_TURN))
    {
      in_message = recieveMessage();
      if(!in_message.equals(END_OF_TURN))
      {
        try
        {
          coordinate = new Coordinate(in_message);
        }
        catch (IllegalBattleshipCoordinateException e)
        {
          e.printStackTrace();
        }
        if (_defense_array.getArrayCell(coordinate.get_x_coordinate(), coordinate.get_y_coordinate()) >= BattleshipArray.SUBMARINE_CELL)
        {
          _defense_array.setArrayCell(coordinate.get_x_coordinate(), coordinate.get_y_coordinate(), BattleshipArray.BOAT_HIT_CELL);
          System.out.println("Your opponent HIT you in " + coordinate.get_text_coordinate() +".");
          sendMessage(String.valueOf(BattleshipArray.HIT_CELL));
        }
        else
        {
          if(_defense_array.getArrayCell(coordinate.get_x_coordinate(), coordinate.get_y_coordinate()) != BattleshipArray.EMPTY_CELL)
          {
            sendMessage(ALREADY_HIT);
          }
          else
          {
            _defense_array.setArrayCell(coordinate.get_x_coordinate(), coordinate.get_y_coordinate(), BattleshipArray.MISS_CELL);
            System.out.println("Your opponent MISSED in " + coordinate.get_text_coordinate() +".");
            sendMessage(String.valueOf(BattleshipArray.MISS_CELL));
          }
        }
      }
    }

    System.out.println("Here is you DEFENSE ARRAY after this enemy attack : ");
    System.out.println(_defense_array.toString());
  }

  //PRIVATE INTERFACE
  private void sendMessage(String out_message) {
    try
    {
      _data_output_stream.writeUTF(out_message);
      _data_output_stream.flush();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private String recieveMessage() {
    String in_message = "ERROR WHILE RECEIVING IN_MESSAGE";
    try
    {
      return _data_input_stream.readUTF();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return in_message;
  }

  //PRIVATE ATTRIBUTES
  private BattleshipArray _attack_array = new BattleshipArray(BATTLESHIP_ARRAY_SIZE);
  private BattleshipArray _defense_array = new BattleshipArray(BATTLESHIP_ARRAY_SIZE);
  private DataInputStream _data_input_stream;
  private DataOutputStream _data_output_stream;

  //PRIVATE CONSTANTS
  public static final int BATTLESHIP_ARRAY_SIZE = 10;
  public static final int[] BATTLESHIP_BOATS_TYPES = {4, 3, 2, 1};
  public static final String END_OF_TURN = "EOT";
  public static final String ALREADY_HIT = "AH";
}
