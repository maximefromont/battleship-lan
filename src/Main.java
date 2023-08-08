import java.util.Scanner;

public class Main
{
  public static void main(String[] args)
  {
    Scanner scanner = new Scanner(System.in);
    String  answer  = "";
    while (!answer.equals("H") && !answer.equals("C"))
    {
      System.out.println("Do you want to host or connect to a bn ? H/C");
      answer = scanner.next();
      if (!answer.equals("H") && !answer.equals("C"))
      {
        System.out.println("Please, answer with \"H\" for hosting or \"C\" for connecting.\n");
      }
    }

    switch (answer)
    {
      case "H":
        new Server();
        break;
      case "C":
        new Client();
        break;
    }
  }
}