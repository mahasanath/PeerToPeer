import java.net.*; 
import java.io.*; 
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 * @author maha sanath
 * This is a lister class which is spawned as soon as a client is added. This is executed
 * as a thread which listens to other Peers.
 *
 */

public class ClientListener extends Thread
{ 
 protected Socket clientSocket;
 protected int portNo;
 

 public ClientListener (int portNo)
 {
    this.portNo=portNo;
    start();
 }
   
  
 public void run()
 {
 
 ServerSocket serverSocket = null; 

    try { 

			
	  try { 
         serverSocket = new ServerSocket(portNo); 
         //clientSocket1 = serverSocket.accept();
         System.out.println ("Listener thread is created and running in the background");
         try { 
         String responseCode[];
              while (true)
              {
                  System.out.println ("Waiting for Connection");
                  clientSocket=serverSocket.accept(); 
                  
                  String inputLine;     
		 	      PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), 
                                      true);
		     	  BufferedReader in = new BufferedReader( 
                  new InputStreamReader( clientSocket.getInputStream())); 
		          while ((inputLine = in.readLine()) != null) { 
		          	 System.out.println(inputLine);
		             String arr[]=inputLine.split(" ");
        		     if(arr[0].equals("GET")){
        		     		System.out.println("Inside If");
        		     		Client c = new Client();
        		     		int rfcNumber= Integer.parseInt(arr[2]);
        		     		
        		     		String fileName = "RFC"+arr[2]+".txt";
        		     		File myFile = new File(fileName);

							
							String tempInput;
							tempInput = in.readLine();
							String arr1[]=tempInput.split(" ");
							String key = arr1[1]+portNo;
							
	            	 		tempInput = in.readLine();
							int count;
							if(myFile.exists()){
 							System.out.println("File found");
 							}
 							else{
 								System.out.println("File Not Found");
 							}
 							if(c.isFound(key,rfcNumber)){
 								System.out.println("Found in the map");
 							}
 							else{
 								System.out.println("Not found in map");
 							}
 							// Check if file exists and send the corresponding status code
 							if(myFile.exists() && c.isFound(key,rfcNumber)){
								  System.out.println("File existed");
								  out.println("-------------------------");
								  out.println("200 OK");
								  // byte stream is used to transfer the file to other Peer
								  byte[] mybytearray = new byte[1024];
					    		    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
							        OutputStream os   = clientSocket.getOutputStream();
							        System.out.println("Writing File");
							        while((count = bis.read(mybytearray)) >= 0){
					        			os.write(mybytearray,0,count);
							        	os.flush();					        						        	
					        		}
								  out.println("-------------------------");					        		
	            	 			  out.println("");
					  	    }else{
					  	    	// 404 status is sent if file is not found!!!
//								  System.out.println("File not found!");
								  out.println("-------------------------");
								  out.println("404 Not Found");
								  out.println("-------------------------");								  
   	            	 			  out.println("");
								  
					 	    } 				
			   				
			//		        os.close();
             		 }              
             		 else{
             		 //If wrong command is given, we reply as 400 Bad Request
            	 	    out.println("400 Bad Request");
	            		out.println("");
             	     }
             	     out.println(inputLine); 
         		  }
			      //bos.close();
 		 		  out.close(); 
         		  in.close(); 
         		  clientSocket.close();         
           	  }
         } 
         catch (IOException e) 
         { 
         	  // Checking and printing the stack trace for IO Exception
              System.err.println("Accept failed."); 
	 		  e.printStackTrace();	          
              System.exit(1); 
         } 
    } 
    catch (IOException e) 
    { //Catching IOException
         System.err.println("Could not listen on port:"+portNo); 
		 e.printStackTrace();
         System.exit(1); 
    } 	
	

	}
    catch (Exception e) 
    { 	// Checking all other exceptions
         System.err.println("Exception!!!:"+portNo);
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
              System.err.println("Could not close port:"+portNo); 
              System.exit(1); 
         } 
    } 
    System.out.println ("Communication Started");

   } 
} 


