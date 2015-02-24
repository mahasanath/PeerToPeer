import java.net.*; 
import java.io.*; 
import java.util.ArrayList;
import java.util.Iterator;


public class EchoServer2 extends Thread
{ 
 protected Socket clientSocket;
 protected Socket clientSocket1;

 static int count=3,rn_count=0,rn_count1=0;
 
 public static void main(String[] args) throws IOException 
 { 
	 
    ServerSocket serverSocket = null; 

    try { 
         serverSocket = new ServerSocket(7734); 
         System.out.println ("Connection Socket Created");
         try { 
              while (true)
              {
                  System.out.println ("Waiting for Connection");
                  new EchoServer2 (serverSocket.accept());         
           	  }
         } 
         catch (IOException e) 
         { 
              System.err.println("Accept failed."); 
              System.exit(1); 
         } 
    } 
    catch (IOException e) 
    { 
         System.err.println("Could not listen on port: 7734."); 
         System.exit(1); 
    } 
    finally
    {
         try {
              serverSocket.close(); 
       	 }
         catch (IOException e)
         { 
              System.err.println("Could not close port: 7734."); 
              System.exit(1); 
         } 
    }
 }

 private EchoServer2 (Socket clientSoc)
 {
    clientSocket = clientSoc; 
    start();
 }
  

 
 static ArrayList<Peer> peer = new ArrayList<Peer>();
 static ArrayList<Rfc>  rfc  = new ArrayList<Rfc>();
 
 static boolean flag_8=false;
 static int cnf_no=1000;
 
 public void run()
 {
 
     System.out.println ("Communication Started");
     
     try {          
	     String inputLine;     
	     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), 
                                      true);
    	 BufferedReader in = new BufferedReader( 
                 new InputStreamReader( clientSocket.getInputStream())); 
         while ((inputLine = in.readLine()) != null) { 
             String arr[]=inputLine.split(" ");
             if(arr[0].equals("ADD")){
            	 String hostName =  in.readLine();
            	 int port = Integer.parseInt(in.readLine());
            	 Peer p = new Peer();
            	 p.setHostName(hostName);
            	 p.setPortNo(port);
            	 peer.add(p);
            	 Rfc r = new Rfc();
            	 r.setHostName(hostName);
            	 r.setRfc(Integer.parseInt(arr[2]));
            	 String temp[]= in.readLine().split(":");;
            	 String title = temp[1];
            	 r.setTitle(title);
            	 rfc.add(r);
            	 out.print("RFC Added");
             }              
             else if (arr[0].equals("LOOKUP")){
            	 
            	 int lookupRfc= Integer.parseInt(arr[2]);
             	 Iterator<Rfc> iterator = rfc.iterator();

                 while(iterator.hasNext()){
                	 if(iterator.next().getRfc() == lookupRfc){
                		 
                	 }
                 }                 
             }
             else if (arr[0].equals("LIST ALL")){
            	 out.print("List of RFC");
            	 Iterator<Rfc> iterator = rfc.iterator();
                 while(iterator.hasNext()){
                	 System.out.println(iterator.next().getRfc());
                	out.println(iterator.next().getRfc());	                 	 
                 }
             }
             else{
            	 out.println("Invalid Params");
             }
             out.println(inputLine); 
           }
 
 		 out.close(); 
         in.close(); 
         clientSocket.close(); 
    } 
    catch (IOException e) 
    { 
         System.err.println("Problem with Communication Server");
         System.exit(1); 
    } 
   }
 
 
} 



