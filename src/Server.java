import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server
{
  ServerSocket     ss;
  Socket           s;
  DataInputStream  dis;
  DataOutputStream dos;
  public Server()
  {
    try
    {
      System.out.println("Server Started");
      ss=new ServerSocket(10);
      s=ss.accept();
      System.out.println(s);
      System.out.println("CLIENT CONNECTED");
      dis= new DataInputStream(s.getInputStream());
      dos= new DataOutputStream(s.getOutputStream());
      ServerChat();
    }
    catch(Exception e)
    {
      System.out.println(e);
    }
  }

  public void ServerChat() throws IOException
  {
    System.out.println("You are now connected in a chat room with the client. Enter \"exit\" to exit.");
    System.out.println("Please enter the pseudonyme you want to use : ");
    Scanner scanner = new Scanner(System.in);
    String  pseudo  = scanner.nextLine();
    System.out.println("\n");

    String in_message, out_message;
    do
    {
      in_message=dis.readUTF();
      System.out.println(in_message);

      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      out_message=br.readLine();
      dos.writeUTF("[" + pseudo + "] : " + out_message);
      dos.flush();
    }
    while(!out_message.equals("exit"));
  }
}
