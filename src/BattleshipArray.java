import java.lang.reflect.Array;
import java.util.ArrayList;

public class BattleshipArray
{

  //PUBLIC INTERFACE
  public BattleshipArray(int size)
  {
    _size  = size;
    _array = new int[_size][_size];
  }

  public String toString()
  {
    String string = "";
    for (int y = 0; y < 10; y++)
    {
      for (int x = 0; x < 10; x++)
      {
        switch (_array[x][y])
        {
          case EMPTY_CELL:
            string += " 0 ";
            break;
          case MISS_CELL:
            string += " 1 ";
            break;
          case HIT_CELL:
            string += " 2 ";
            break;
          case BOAT_HIT_CELL:
            string += "(2)";
            break;
          case SUBMARINE_CELL:
            string += "(4)";
            break;
          case SMALLBOAT_CELL:
            string += "(5)";
            break;
          case CRUISER_CELL:
            string += "(6)";
            break;
          case CARRIER_CELL:
            string += "(7)";
            break;
        }
      }
      string += "\n";
    }
    return string;
  }

  public void placeRandomBoat(int[] boats_types)
  {
    try {
      for(int i = 0; i < boats_types.length; i++)
      {
        for(int j = 1; j <= boats_types[i]; j++)
        {
          generateBoat(j); //Size of current type is place in type array +1 since array start at 0
        }
      }
    } catch (ArrayIndexOutOfBoundsException e)
    {
      e.printStackTrace();
    }

  }

  public void setArrayCell(int x, int y, int value)
  {
    _array[x][y] = value;
  }

  public boolean addBoatInArray(int x_start, int y_start, int x_end, int y_end, int value)
  {
    ArrayList<int[]> listCoordinates = new ArrayList<>();
    for(int x = x_start; x <= x_end; x++)
    {
      for (int y = y_start; y <= y_end; y++)
      {
        if(_array[x][y] < SUBMARINE_CELL)
          listCoordinates.add(new int[] {x, y});
        else
          return false; //One boat is already there
      }
    }
    //If boat placement is valid, we fill in the values at the proper coordinates
    for(int[] coordinate : listCoordinates) _array[coordinate[0]][coordinate[1]] = value;
    return true; //No boats found on that boat path
  }

  public int getArrayCell(int x, int y)
  {
    return _array[x][y];
  }

  //PUBLIC CONSTANTS
  public static final int EMPTY_CELL = 0;
  public static final int MISS_CELL = 1;
  public static final int HIT_CELL = 2;
  public static final int BOAT_HIT_CELL = 3;
  public static final int SUBMARINE_CELL = 4;
  public static final int SMALLBOAT_CELL = 5;
  public static final int CRUISER_CELL = 6;
  public static final int CARRIER_CELL = 7;

  //PRIVATE INTERFACE
  private void generateBoat(int boat_size) throws ArrayIndexOutOfBoundsException
  {
    boolean validation = false;
    do {
      int x_start = (int) Math.floor(Math.random() * _size);
      int y_start = (int) Math.floor(Math.random() * _size);
      boolean orientation = Math.random() < 0.5;
      if(orientation)
      {
        if (x_start + (boat_size - 1) < _size)
          validation = addBoatInArray(x_start, y_start, x_start + (boat_size - 1), y_start, SUBMARINE_CELL + (boat_size - 1));
      }
      else
      {
        if (y_start + (boat_size - 1) < _size)
          validation = addBoatInArray(x_start, y_start, x_start, y_start + (boat_size - 1), SUBMARINE_CELL + (boat_size - 1));
      }
    } while (validation == false);
  }

  //PRIVATE ATTRIBUTES
  private int     _size;
  private int[][] _array;
}
