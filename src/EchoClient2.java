import java.io.*;
import java.net.*;

public class EchoClient2 {
    public static void main(String[] args) throws IOException {

        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            echoSocket = new Socket("127.0.0.1", 7734);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                                        echoSocket.getInputStream()));
            
           
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: 127.0.0.1");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                               + "the connection to: taranis.");
            System.exit(1);
        }

	BufferedReader stdIn = new BufferedReader(
                                   new InputStreamReader(System.in));
	String userInput;
	
	String serverInput;
	boolean flag_m=false,fl=true,flag_end=false;
       
        System.out.println("Enter the type of Operation");
        System.out.println("1. Add");
        System.out.println("2. Lookup");
        System.out.println("3. List All");
        System.out.println("4. Contact Peer");  
        System.out.println("5. Exit");                                

     while(true){  	         	 
        	if((userInput= stdIn.readLine())!=null){
        		
            	if(userInput.equals("1")){
            		System.out.println("Processing 1");
//	            	out.println(userInput);
            	}
            	else if(userInput.equals("2")){
            		System.out.println("Processing 2");            	
            	}
            	else if(userInput.equals("3")){
            	
            	}
            	else if(userInput.equals("4")){
            	
            	}
            	else if (userInput.equals("5")){
                    break;
                }
            	// while((serverInput = in.readLine())!=null){    	
//             		if(serverInput.equals("eee"))
//             			flag_end=true;
//             		if(serverInput.equals(""))
//             			break;
//             		System.out.println(serverInput);
//             	}
        
        	}                	           
       }

        
	   out.close();
	   in.close();
	   stdIn.close();
	   echoSocket.close();
    }
}
