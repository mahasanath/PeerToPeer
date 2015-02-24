import java.net.*; 
import java.io.*; 
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;

/**
 * 
 * @author maha sanath
 * Server class which listens to the clients (Peer) on Port No: 7734 
 * A new thread is spawned for each and every Peer
 *
 */

public class Server extends Thread
{ 
 protected Socket clientSocket;

 
 public static void main(String[] args) throws IOException 
 { 	 
    ServerSocket serverSocket = null; 
	Socket clientSocket1;
    try { 
         serverSocket = new ServerSocket(7734); 
         System.out.println ("Connection Socket Created");
         try { 
              while (true)
              {//Socket connection is created. Communication can happen.
                  System.out.println ("Waiting for Connection");
                  new Server (serverSocket.accept());         
           	  }
         } 
         catch (IOException e) 
         { //Catching the IO Exception
              System.err.println("Accept failed."); 
              System.exit(1); 
         } 
    } 
    catch (IOException e) 
    { 
         System.err.println("Could not listen on port: 7734."); 
         e.printStackTrace();
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

 private Server (Socket clientSoc)
 {	// Spawning a new thread for each client
    clientSocket = clientSoc;
    start();
 }
 static HashMap<Rfc,Peer> map = new HashMap<Rfc,Peer>(); 
 //An Arraylist which keeps track of the Active Peers and the corresponding RFCs
 static ArrayList<PeerList> activePeers = new ArrayList<PeerList>();
  
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
			 System.out.println(arr[0]);
			 if(arr[0].equals("REGISTER")){
			 	int port = Integer.parseInt(arr[1]);
			 	String host = arr[2];
			 	//Adding the peer to the peerlist
			 	PeerList p = new PeerList();
			 	p.setPortNo(port);
			 	p.setHostName(host);			 	
			 	activePeers.add(p);
				System.out.println("Peer Added");
            	out.println("Peer Added");
            	out.println("");

			 }
             else if(arr[0].equals("ADD")){
             	
             	 // ADD RFC operation
             	 int rfc_int = Integer.parseInt(arr[2]);             		            

            	 String tempHostName[]=in.readLine().split(" ");
            	 String hostName =  tempHostName[1];
            	 System.out.println(hostName);
            	 String temp[]= in.readLine().split(" ");
            	 int port = Integer.parseInt(temp[1]);
            	 System.out.println(port);

            	 temp= in.readLine().split(" ");
            	 String title = temp[1];
            	 System.out.println(title);
				 //Adding the RFC to the corresponding Peer           	 
            	 Iterator<PeerList> iterator = activePeers.iterator();
                 while(iterator.hasNext()){
                 	 PeerList p = iterator.next();
                	 if(p.getPortNo() == port && p.getHostName().equals(hostName)){                	 	
                	 	p.getRfcList().add(rfc_int);                		
                		p.setTitle(title);		
                	 }
                 }                 
            	 
            	 System.out.println("RFC Added");
            	 out.println("RFC Added");
            	 out.println("");
             }              
             else if (arr[0].equals("LOOKUP")){
             	 // LOOKUP Operation                       	
            	 int lookupRfc= Integer.parseInt(arr[2]);
            	 
            	 Iterator<PeerList> iterator = activePeers.iterator();
                 boolean found_flag=false;
                 //Iterate through the list and check if the given RFC exists
                 while(iterator.hasNext()){
                 	 PeerList p = iterator.next();
                 	 ArrayList<Integer> a = p.getRfcList();
                	 if(!a.isEmpty()){
                	 	Iterator<Integer> it = a.iterator();
                	 	while(it.hasNext()){
                	 	  if(it.next()==lookupRfc){                	 	
	                	    out.println("-----------------------------");
	                	    out.println("P2P-CI/1.0 200 OK");                 	 		
                	 		out.println(lookupRfc+" "+p.getTitle()+" "+p.getHostName()+" "
                	 		+p.getPortNo());	
                	 		found_flag=true;
	                		out.println("-----------------------------");
	                	  }		                		
	                	}	
                	 }
                 }   
            	 if(!found_flag){
               	 	 System.out.println("Not Found");   
               	     out.println("-----------------------------");               	 	               
 	                 out.println("Not Found");
	           	     out.println("-----------------------------"); 	                 

                  }                                     
          		  out.println("");                  
             }
             else if (arr[0].equals("LIST")){
                 // LIST Operation             	
            	 out.println("List of RFC");
				 
				 String hostArr[]=in.readLine().split(" ");
            	 String host=hostArr[1];
            	 
            	 String portArr[]=in.readLine().split(" ");
            	 int port=Integer.parseInt(portArr[1]);
            	 
            	 //Iterate through the activePeers list and send the values
              	 Iterator<PeerList> iterator = activePeers.iterator();
            	 while(iterator.hasNext()){
            	 	 PeerList p = iterator.next();
                 	 ArrayList<Integer> a = p.getRfcList();
	             
                	 if(!a.isEmpty()){
                   	    out.println("-----------------------------");
                	    out.println("P2P-CI/1.0 200 OK");             	            	 
                	 	Iterator<Integer> it = a.iterator();
                	 	while(it.hasNext()){
                	 		out.println(it.next()+" "+p.getTitle()+" "+p.getHostName()+" "
                	 		+p.getPortNo());
                  	 	    out.println("-----------------------------");	
                	 	}                	 	
                	 }                	 
                 }   
                 out.println("");                             	 	                 	 
             }
            //  else if(arr[0].equals("LIST-ACTIVE-PEERS")){
//              	 out.println("List of Active Peers");
//              	 
//              	 Iterator<PeerList> iterator = activePeers.iterator();
//             	 while(iterator.hasNext()){
//             	 	 PeerList p = iterator.next();
//                  	 ArrayList<Integer> a = p.getRfcList();
// 	             
//                 	// if(!a.isEmpty()){
//                    	    out.println("-----------------------------");
//                 	    out.println("P2P-CI/1.0 200 OK");             	            	 
//                 	 	out.println(p.getHostName()+" "+p.getPortNo());
//                 	 	out.println("-----------------------------");                	 	
//                 	 //}                	 
//                  }   
//                  out.println("");  
//              }
             else if (arr[0].equals("END")){
             	 // END Operation 
             	 int port = Integer.parseInt(arr[1]);
             	 String host = arr[2];

				 Iterator<PeerList> iterator = activePeers.iterator();
                 while(iterator.hasNext()){
                 	 PeerList p = iterator.next();
                	 if(p.getPortNo() == port && p.getHostName().equals(host)){                	 	
                	 	p.getRfcList().clear();
                		p.setTitle("");		
                	 }
                 }   
             	 
             	 System.out.println("Removing from list");
             	 break;
             }
             else{
            	// out.println("Invalid Params");
             }
             System.out.println("End of operation");
             
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


