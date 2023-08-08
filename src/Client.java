import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client
{

  Socket           s;
  DataInputStream  din;
  DataOutputStream dout;
  public Client()
  {
    Scanner scanner = new Scanner(System.in);
    System.out.println("This step will not be verified. Please make sure the following answer is correctly written.");
    System.out.println("Please enter the server ip adress : ");
    String ip_adress = scanner.next();

    try
    {
      //s=new Socket("10.10.0.3,10");
      s=new Socket(ip_adress,10);
      System.out.println(s);
      din= new DataInputStream(s.getInputStream());
      dout= new DataOutputStream(s.getOutputStream());
      ClientChat();
    }
    catch(Exception e)
    {
      System.out.println(e);
    }
  }
  public void ClientChat() throws IOException
  {
    System.out.println("You are now connected in a chat room with the server. Enter \"exit\" to exit.");
    System.out.println("Please enter the pseudonyme you want to use : ");
    Scanner scanner = new Scanner(System.in);
    String pseudo = scanner.nextLine();
    System.out.println("\n");

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String out_message;
    do
    {
      out_message=br.readLine();
      dout.writeUTF("[" + pseudo + "] : " + out_message);
      dout.flush();
      System.out.println(din.readUTF());
    }
    while(!out_message.equals("exit"));
  }


}
