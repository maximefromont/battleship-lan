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
    _defense_grid.placeRandomBoat(BATTLESHIP_BOATS_TYPES);
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
      System.out.println("Here is your ATTACK GRID : ");
      System.out.println(_attack_grid.toString());

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
        _attack_grid.setArrayCell(attack_coordinate.get_x_coordinate(), attack_coordinate.get_y_coordinate(), cell_value);
        switch (cell_value)
        {
          case BattleshipArray.MISS_CELL:
            System.out.println("It's a MISS.");
            replay = false;
            break;
          case BattleshipArray.HIT_CELL:
            System.out.println("It's a HIT !");
            if (!isWon())
            {
              System.out.println("You hit " + (_attack_grid.getAmountOfHit()*100)/getTotalLenghthOfBoats()+ "% of your enemy boats.");
              replay = true;
            }
            else
            {
              replay = false; //No replay because the user just won.
              _is_finished = true;
            }
            break;
        }
      }

    } while (replay == true);
    if(_is_finished)
    {
      sendMessage(END_OF_GAME);
    }
    else
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

      if(in_message.equals(END_OF_GAME))
      {
        _is_finished = true;
        return;
      }

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

        int type_of_boat = _defense_grid.getArrayCell(coordinate.get_x_coordinate(), coordinate.get_y_coordinate());
        if (type_of_boat >= BattleshipArray.SUBMARINE_CELL)
        {
          _defense_grid.setArrayCell(coordinate.get_x_coordinate(), coordinate.get_y_coordinate(), type_of_boat+4); //+4 to change type to hit type (See constants of BattleshipArray)
          System.out.println("Your opponent HIT you in " + coordinate.get_text_coordinate() +".");
          sendMessage(String.valueOf(BattleshipArray.HIT_CELL));
        }
        else
        {
          if(_defense_grid.getArrayCell(coordinate.get_x_coordinate(), coordinate.get_y_coordinate()) != BattleshipArray.EMPTY_CELL)
          {
            sendMessage(ALREADY_HIT);
          }
          else
          {
            _defense_grid.setArrayCell(coordinate.get_x_coordinate(), coordinate.get_y_coordinate(), BattleshipArray.MISS_CELL);
            System.out.println("Your opponent MISSED in " + coordinate.get_text_coordinate() +".");
            sendMessage(String.valueOf(BattleshipArray.MISS_CELL));
          }
        }
      }
    }

    System.out.println("Here is you DEFENSE GRID after this enemy attack : ");
    System.out.println(_defense_grid.toString());
  }

  public boolean isWon()
  {
    return _attack_grid.getAmountOfHit() >= getTotalLenghthOfBoats();
  }

  public void printEndMessage()
  {
    if(isWon()) System.out.println("Congratulations, you won the game !"); else System.out.println("Unfortunately, you lost the game.");
    System.out.println("Press enter to close the connection.");
    String enterPressed = new Scanner(System.in).nextLine(); //EnterPressed is never used because we're simply checking that the user press enter.
  }

  public boolean isFinished()
  {
    return _is_finished;
  }

  //PRIVATE INTERFACE
  private int getTotalLenghthOfBoats()
  {
    int lenght_of_all_boat = 0;

    for(int i = 0; i < BATTLESHIP_BOATS_TYPES.length; i++)
        lenght_of_all_boat += BATTLESHIP_BOATS_TYPES[i] * (i+1);

    return lenght_of_all_boat;
  }
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
  private BattleshipArray _attack_grid  = new BattleshipArray(BATTLESHIP_GRID_SIZE);
  private BattleshipArray _defense_grid = new BattleshipArray(BATTLESHIP_GRID_SIZE);
  private DataInputStream _data_input_stream;
  private DataOutputStream _data_output_stream;
  private boolean _is_finished = false;

  //PRIVATE CONSTANTS
  public static final int BATTLESHIP_GRID_SIZE = 10;
  public static final int[] BATTLESHIP_BOATS_TYPES = {1, 1, 0, 0};
  public static final String END_OF_TURN = "EOT";
  public static final String END_OF_GAME = "EOG";
  public static final String ALREADY_HIT = "AH";
}
