import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Coordinate
{

  //PUBLIC INTERFACE
  public Coordinate(String battleship_coordinate)
  throws IllegalBattleshipCoordinateException
  {
    Pattern pattern = Pattern.compile(BATTLESHIP_COORDINATE_REGEX);
    Matcher matcher = pattern.matcher(battleship_coordinate);
    if (matcher.find())
    {
      _x_coordinate = getLetterToNumber(matcher.group(1));
      _y_coordinate    = Integer.parseInt(matcher.group(2)) - 1; //Minus one because A1 is 0 ; 0 and not 0 ; 1
      _text_coordinate = battleship_coordinate;
    }
    else
      throw new IllegalBattleshipCoordinateException("Invalid battleship coordinates");
  }

  public int get_x_coordinate()
  {
    return _x_coordinate;
  }

  public int get_y_coordinate()
  {
    return _y_coordinate;
  }

  public String get_text_coordinate()
  {
    return _text_coordinate;
  }

  //PRIVATE INTERFACE
  private static int getLetterToNumber(String letter)
  {
    switch (letter)
    {
      case "A":
        return 0;
      case "B":
        return 1;
      case "C":
        return 2;
      case "D":
        return 3;
      case "E":
        return 4;
      case "F":
        return 5;
      case "G":
        return 6;
      case "H":
        return 7;
      case "I":
        return 8;
      case "J":
        return 9;
    }
    return -1;
  }

  //PRIVATE ATTRIBUTES
  private int    _x_coordinate;
  private int    _y_coordinate;
  private String _text_coordinate;

  //PRIVATE CONSTANTS
  private String BATTLESHIP_COORDINATE_REGEX = "\\b([A-J])(10|[1-9])\\b";

}

//EXCEPTIONS
class IllegalBattleshipCoordinateException
        extends Exception
{
  // Parameterless Constructor
  public IllegalBattleshipCoordinateException() {}

  // Constructor that accepts a message
  public IllegalBattleshipCoordinateException(String message)
  {
    super(message);
  }
}