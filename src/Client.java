
	import java.io.*;
	import java.net.*;
	import java.util.HashMap;
	import java.util.ArrayList;
	import java.util.Iterator;
	import java.net.URL;
	import java.nio.channels.ReadableByteChannel;
	import java.nio.channels.Channels;


	/**
	 * 
	 * @author maha sanath
	 * Peer class which listens to the other Peers
	 * This class also acts as a client and communicates to the Server for ADD, 
	 * LOOKUP and LIST ALL Operations.
	 *
	 */

	public class Client {
		// HashMap to keep track of the which client has what rfcs. 
		static HashMap<String,ArrayList<Integer>> mapRfc = new HashMap<String,ArrayList<Integer>>();

	    public static void main(String[] args) throws IOException {
	    	ArrayList<Integer> clientRfc = new ArrayList<Integer>();
		   // ArrayList<Integer> clientPort = new ArrayList<Integer>();
		    //port number of this client which will be initialized in the Register step
		    int myPort=0;
		    // Hostname of this client which will be initialized in the Register step
		    String myHostName="";
		    boolean end_flag=false;
	        Socket echoSocket = null;
	        // Input and output streams for socket communication
	        PrintWriter out = null;
	        BufferedReader in = null;
			String host=null;        
			BufferedReader stdIn = new BufferedReader(
	                                   new InputStreamReader(System.in));
	        
	        try {
	        	System.out.println("Enter the host where Server is running:");
				host=stdIn.readLine();
	        	        	
	            echoSocket = new Socket(InetAddress.getByName(host), 7734);
	            out = new PrintWriter(echoSocket.getOutputStream(), true);
	            in = new BufferedReader(new InputStreamReader(
	                                        echoSocket.getInputStream()));
	            
	           
	        } catch (UnknownHostException e) {
	        	//Catching the UnknownHost Exception
	            System.err.println("Don't know about host");
	            System.exit(1);
	        } catch (IOException e) {
	        	//Catching the IO Exception
	            System.err.println("Couldn't get I/O for "
	                               + "the connection to:"+host);
	            System.exit(1);
	        }


			String userInput;	
			String serverInput;
			boolean register_flag=false;
	                                           	    
	     	while(true){       	
	     		System.out.println("Enter the type of Operation");
		     	System.out.println("1. Register");
	    	    System.out.println("2. Add");
	        	System.out.println("3. Lookup");
		        System.out.println("4. List All");
	    	    System.out.println("5. Download a RFC");  
	        	System.out.println("6. Exit");    
	        	         	 
	        	if((userInput= stdIn.readLine())!=null){        		
	           		if(userInput.equals("1")){
	           		    if(register_flag){
	           		    //Check if the client is already registered
	           		    	System.out.println("Already Registered!!!");
	           		    	continue;
	           		    }
	            		System.out.println("Processing 1");
		            	System.out.println("Enter Port No:");
						userInput= stdIn.readLine();
						myPort= Integer.parseInt(userInput);
						String registerString="REGISTER "+myPort;
						System.out.println("Enter Host Name:");	            	
						userInput= stdIn.readLine();
						myHostName=userInput;
						registerString=registerString+" "+userInput;
						System.out.println(registerString);
		            	out.println(registerString);
		            	final int port = myPort;
						  	register_flag=true;
						  	//creating a new thread to listen to other peers
					    	Runnable r = new Runnable() {					    
					        	public void run() {
						            // runYourBackgroundTaskHere();
				            		ClientListener c = new ClientListener (port);         
	        	 				}         		
	     					};
	     			 		new Thread(r).start();
	     			 	
		            	
		            	register_flag=true;
		           	}
					else if(userInput.equals("2")){
						if(!register_flag){
							//Make sure that Register is the first operation a Peer can perform
							System.out.println("No Peer is registered!!!");
							continue;
						}
						//Constructing the URL to download a RFC
						
						String myUrl= "http://www.rfc-editor.org/rfc/";

						System.out.println("Enter the Rfc Number:");
	 					userInput=stdIn.readLine();
	 					//Constructing the command that can be sent to the Server
						String line1="ADD RFC "+userInput+" P2P-CI/1.0";
						String myFile="rfc"+userInput+".txt";
						myUrl+=myFile;					
						int myRfc = Integer.parseInt(userInput);
						System.out.println("Enter the Host Name:");
						userInput = stdIn.readLine();

					    if(!userInput.equals(myHostName)){
					    	// Check if the Peer uses the same Hostname
							System.out.println("HostName is different from the Peer's HostName");
							continue;
						}
						String temp=userInput;
						String line2="Host: "+userInput;
						System.out.println("Enter the Port Number");
						userInput = stdIn.readLine();
						final int port = Integer.parseInt(userInput);					
			
				  		if(port != myPort){
					    	// Check if the Peer uses the same PortNo		  		
				  			System.out.println("PortNo is different from the Peer's PortNo");
				  			continue;
				  		}
				  		//Downloading the RFC from the URL
				  		URL url = new URL(myUrl);
					    ReadableByteChannel rbc = Channels.newChannel(url.openStream());
					    FileOutputStream fos = new FileOutputStream(myFile);
					    fos.getChannel().transferFrom(rbc, 0, 1 << 24);
						
						String line3="Port: "+userInput;

						System.out.println("Enter the Title:");
						userInput = stdIn.readLine();
						String line4 = "Title: "+userInput;
						
						out.println(line1);
					  	out.println(line2);
						out.println(line3);
					  	out.println(line4);									  					  
						temp+=port;
						//Adding the Hostname-Portno into the Map
						ArrayList<Integer> tmp = mapRfc.get(temp);
						if(tmp != null){
							tmp.add(myRfc);
							mapRfc.put(temp,tmp);
						}
						else{
							tmp= new ArrayList<Integer>();
							tmp.add(myRfc);
							mapRfc.put(temp,tmp);
						}
									  	    
					}
					else if(userInput.equals("3")){
					//Handling the Lookup case            		            		
	            		System.out.println("Processing 3"); 
	            		
						System.out.println("Enter the Rfc Number to Lookup:");
	 					userInput=stdIn.readLine();
	 					//Constructing the LOOKUP command to communicate with the server.
						String line1="LOOKUP RFC "+userInput+" P2P-CI/1.0";					
						
						out.println(line1);
					  	
	            	}
	            	else if(userInput.equals("4")){
	            		//Handling the List All case
						System.out.println("Processing 4"); 
						//Constructing the LIST ALL command
						String line1 = "LIST ALL P2P-CI/1.0"; 
						System.out.println("Enter the Host Name:");
						userInput = stdIn.readLine();				    
						String line2="Host: "+userInput;
						
						System.out.println("Enter the Port Number");
						userInput = stdIn.readLine();					
						String line3="Port: "+userInput;
	            			            	
					  	out.println(line1);
					  	out.println(line2);
					  	out.println(line3);
	            	}
	            	else if(userInput.equals("5")){
	            		System.out.println("Processing 5"); 
	            		int port;
	            		System.out.println("Enter the Port No:");
					  	port = Integer.parseInt(stdIn.readLine());
					  	System.out.println("Please enter the GET commands");    	 
					  	userInput= stdIn.readLine();
					
						String arr[] = userInput.split(" "); 
						//Constructing the file name. 
						String fileName= "RFC"+arr[2]+"receiver.txt"; // Constructing the file Name
						int temp =	Integer.parseInt(arr[2]);
						String hostPort = myHostName+myPort;
						ArrayList<Integer> tmp = mapRfc.get(hostPort);
						if(tmp != null){
							if(tmp.contains(temp)){
							//Check if the RFC is already in the Peer
								System.out.println("RFC already exists in your Peer!!!");
								continue;
							}						
						}
		   		 		//Calling the method, CommunicatePeer to get the RFC
						communicatePeer(port,fileName,userInput);
						continue;
					  					  	           	
	            	}
	            	else if (userInput.equals("6")){
	            		//Exiting and closing the connection
	            		System.out.println("Processing 6");
	            		                   		            		       
					 	out.println("END "+myPort+" "+myHostName);
					 	System.out.println("END "+myPort+" "+myHostName);
					 	end_flag=true;
	                    break;
	                }
	                else{
	                	System.out.println("Enter between 1 and 6!!");
	                	continue;
	                }
	                if(!end_flag){
	        	        while((serverInput = in.readLine())!=null){
	    	            	//Getting the response from the Server. When we get an empty string we break
			                if(serverInput.equals(""))
	            				break;    	            		
	            			System.out.println(serverInput);
	            		}        
	            	}
	        	}                	           
	       }

		   out.close();
		   in.close();
		   stdIn.close();
		   echoSocket.close();
	    }
	    
	    public boolean isFound(String key,int rfc){
	    	ArrayList<Integer> temp =mapRfc.get(key);
	    	if(temp !=null){
	  			if(temp.contains(rfc)){
		  			return true;
	  			}
	  			return false;
	  		}	
	  		else {
	  			return false;	  
	  		}
	    }

	    private static void communicatePeer(int port,String fileName,String userInput){
	    	Socket echoSocket1 = null;
	        PrintWriter out1 = null;
	        BufferedReader in1 = null;
	        String serverInput;
	        BufferedReader stdIn = new BufferedReader(
	                                   new InputStreamReader(System.in));
	        String hostName = null;  
	        String userInput1 = null;                         
	        try {
	        	
	       		userInput1= stdIn.readLine();
	        	String arr[] = userInput1.split(" ");
				hostName=arr[1];
				
	            echoSocket1 = new Socket(InetAddress.getByName(hostName), port);
	            out1 = new PrintWriter(echoSocket1.getOutputStream(), true);
	            in1 = new BufferedReader(new InputStreamReader(
	                                        echoSocket1.getInputStream()));
	           
				//String userInput;                                   
		        //while(true){  	     
		        	
					
					
					out1.println(userInput);
	  				out1.println(userInput1);
	 				userInput= stdIn.readLine();
	 				out1.println(userInput);
	 				
	 				while((serverInput = in1.readLine())!=null){
	                	//Getting the response from the Server. When we get an empty string we break
						if(serverInput.equals("200 OK")){
							System.out.println(serverInput);
						// A 200 status message is obtained when the requested RFC file is found.
							InputStream is = echoSocket1.getInputStream();
							FileOutputStream fos = new FileOutputStream(fileName);
						    BufferedOutputStream bos = new BufferedOutputStream(fos);
				    		byte[] buffer = new byte[1024];
						    int count;
						    System.out.println("Receiving");
							while((count = is.read(buffer)) >= 0){
								fos.write(buffer,0,count);
						
								if (count < 1024) {
	                	 	 	   fos.flush();
				                   break;
	    			            }
							}
							break;	
						}
		                else if(serverInput.equals("")){
		                	// An empty string means end of communication.
		                	System.out.println("ending");
	            			break;    	           
	            		}	 		
	            		System.out.println(serverInput);
	            	}
			
	            	out1.close();
				    in1.close();
				    echoSocket1.close();
	         
	        } catch (UnknownHostException e) {
	            System.err.println("Don't know about host:"+hostName);
	            System.exit(1);
	            
	        } catch (IOException e) {
	            System.err.println("Couldn't get I/O for "
	                               + "the connection to:"+hostName);
	            System.exit(1);
	        }                                           
	    } 
	}

