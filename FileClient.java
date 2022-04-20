import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.*;  
import java.io.*;
import java.util.*; 
import java.net.InetAddress; 
public class FileClient extends Thread {
	public static String nameport;
	public static void TrasferStatus(long a, long b)
	{
	    //Function to Print the Status bar.
	    System.out.println("File Transfer Complete Status: ");
	    int percentage =(int) ((a*100)/b);
	    System.out.printf("[");
	    for(int k=0;k<percentage/10;k++)
		System.out.printf("=");
	    for(int k=percentage/10;k<10;k++)
		System.out.printf(" ");
	    System.out.println("]           " +  String.valueOf(percentage) + "%");
	}
	public static void main(String[] args) {
        int type =0;
       	int client_port=3333,udp_client_port=9000;
		String nameport="thisuser";
		InputStream is = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        int bufferSize = 0;
		String serverip ="localhost";
        String str1="",str2="";
		String ourip ="";
		try{		
			InetAddress inetAddresslocal = InetAddress.getLocalHost();
		ourip = inetAddresslocal.getHostAddress();
		System.out.println(ourip);
		}
		catch(IOException e)
        {
            System.out.println("Error in reading Buffer: Please Check");
        }
	while(true)
	    {
		//Running the Client part in this loop(main thread.)
		BufferedReader buffer=new BufferedReader(new InputStreamReader(System.in));
		System.out.printf("$>>");
		try
        {
            str1=buffer.readLine();
        }
        catch(IOException e)
        {
            System.out.println("Error in reading Buffer: Please Check");
        }
		BufferedOutputStream bufferOutStream=null;
		String[] commands = str1.split(" ");
		String cmp1 = "upload",cmp2 ="upload_udp",cmp3="create_folder",cmp4="move_file",cmp5="create_user",cmp6="create_group",cmp7="list_groups";
		String cmp8 = "join_group",cmp9 = "leave_group",cmp10 = "list_detail",cmp11 = "get_file",cmp12 = "share_msg";
		if(commands[0].equals(cmp12)){
			try{
			//to other pc	//Connect   
			// Socket s= new Socket("10.1.34.33",client_port);
				Socket s= new Socket(serverip,client_port);
				DataInputStream dataInpStream=new DataInputStream(s.getInputStream());  
				DataOutputStream dataOutStream=new DataOutputStream(s.getOutputStream());
				byte[] contents;
				String msg = "";
				for(int i=1;i<commands.length;i++){
					msg += commands[i];
					msg += " ";
				}
				dataOutStream.writeUTF("sharemsg"+":" + msg);
				System.out.println("Message Sent to Server Completed");
				String response = "";
				response = dataInpStream.readUTF();
				System.out.println(response);
				dataOutStream.flush();  
				dataInpStream.close();
				s.close();
			}
			catch(IOException ex)
			{
				System.out.println("Unable to Move the Files " + commands[1]);
			}
		}
		
		if(commands.length == 3){
			if(commands[0].equals(cmp4)){
				try{
					//to other pc	//Connect
  				    // Socket s= new Socket("10.1.34.33",client_port);
					Socket s= new Socket(serverip,client_port);
				    DataInputStream dataInpStream=new DataInputStream(s.getInputStream());  
				    DataOutputStream dataOutStream=new DataOutputStream(s.getOutputStream());

                    byte[] contents;
				    dataOutStream.writeUTF("start"+":" + commands[0] +":"+commands[1]+":"+commands[2]+":"+ourip+":"+nameport+":"+"move");
                    System.out.println("Move File Completed");
					dataOutStream.flush();  
					dataInpStream.close();
					s.close();
				}
				catch(IOException ex)
				    {
					System.out.println("Unable to Move the Files " + commands[1]);
				    }
			    }
		}
		if(commands.length == 1){
			if(commands[0].equals(cmp7)){
				try{
					Socket s = new Socket(serverip,client_port);
					DataInputStream dataInpStream = new DataInputStream(s.getInputStream());
					DataOutputStream dataOutStream = new DataOutputStream(s.getOutputStream());
					dataOutStream.writeUTF("list"+":"+commands[0]+":"+ourip+":"+"listgroups");
					System.out.println("Following are the list of groups available");
					String response ="";
					response = dataInpStream.readUTF();
					System.out.println(response);
					dataOutStream.flush();
					dataInpStream.close();
					s.close();
				}
				catch(IOException e){
					System.out.println("Cant list the groups needed");
				}
			}
		}
        if(commands.length == 2){
			if(commands[0].equals(cmp11)){
				try{
				// System.out.print("Enter the file name");
				String[] fname = commands[1].split("/");
				String filename ="";
				for(int i=0;i<fname.length;i++){
					filename = fname[i];
				}
				Socket s = new Socket(serverip,client_port);									
					// sending the file name to server. Uses PrintWriter
				OutputStream  ostream = s.getOutputStream( );
				DataOutputStream dataOutStream = new DataOutputStream(s.getOutputStream());
					String namegrp = commands[1];
				    dataOutStream.writeUTF("Get"+":" + commands[0] +":"+nameport+":"+namegrp+":"+"getfile");  
								// receiving the contents from server.  Uses input stream
				InputStream istream = s.getInputStream();
				BufferedReader socketRead = new BufferedReader(new InputStreamReader(istream));
			
				String str;
				while((str = socketRead.readLine())  !=  null) // reading line-by-line
				{
					System.out.println(str);  
					try { 
			
						// Open given file in append mode. 
						BufferedWriter out = new BufferedWriter( 
							new FileWriter(filename, true)); 
						out.write(str); 
						out.write("\n");
						out.close(); 
					} 
					catch (IOException e) { 
						System.out.println("exception occoured" + e); 
					}         
				}
				socketRead.close();		
				}
				catch(Exception e){

				}
			}
			if(commands[0].equals(cmp10)){
				try{
					Socket s = new Socket(serverip,client_port);
					DataInputStream dataInpStream = new DataInputStream(s.getInputStream());
					DataOutputStream dataOutStream = new DataOutputStream(s.getOutputStream());
					String namegrp = commands[1];
				    dataOutStream.writeUTF("List"+":" + commands[0] +":"+nameport+":"+namegrp+":"+"listdetail");
                    System.out.println("Request to List Group Details Sent");
					String response ="";
					response = dataInpStream.readUTF();
					System.out.println(response);
					dataOutStream.flush();  
					dataInpStream.close();
					s.close();
				}
				catch(IOException ex)
				    {
					System.out.println("Unable to List the group " + commands[1]);
				    }				
			}
			if(commands[0].equals(cmp8)){
				try{
					Socket s = new Socket(serverip,client_port);
					DataInputStream dataInpStream = new DataInputStream(s.getInputStream());
					DataOutputStream dataOutStream = new DataOutputStream(s.getOutputStream());
					String namegrp = commands[1];
				    dataOutStream.writeUTF("Join"+":" + commands[0] +":"+nameport+":"+namegrp+":"+"joingroup");
                    System.out.println("Request to join Group Sent");
					String response ="";
					response = dataInpStream.readUTF();
					System.out.println(response);
					dataOutStream.flush();  
					dataInpStream.close();
					s.close();
				}
				catch(IOException ex)
				    {
					System.out.println("Unable to Join the group " + commands[1]);
				    }
				
			}
			if(commands[0].equals(cmp9)){
				try{
					Socket s = new Socket(serverip,client_port);
					DataInputStream dataInpStream = new DataInputStream(s.getInputStream());
					DataOutputStream dataOutStream = new DataOutputStream(s.getOutputStream());
					String namegrp = commands[1];
				    dataOutStream.writeUTF("Leave"+":" + commands[0] +":"+nameport+":"+namegrp+":"+"leavegroup");
                    System.out.println("Request to Leave Group Sent");
					String response ="";
					response = dataInpStream.readUTF();
					System.out.println(response);
					dataOutStream.flush();  
					dataInpStream.close();
					s.close();
				}
				catch(IOException ex)
				    {
					System.out.println("Unable to Leave the group " + commands[1]);
				    }
				
			}
			if(commands[0].equals(cmp6)){
				try{
					Socket s = new Socket(serverip,client_port);
					DataInputStream dataInpStream = new DataInputStream(s.getInputStream());
					DataOutputStream dataOutStream = new DataOutputStream(s.getOutputStream());
					String namegrp = commands[1];
				    dataOutStream.writeUTF("create"+":" + commands[0] +":"+nameport+":"+namegrp+":"+"creategroup");
                    System.out.println("Create  Group Completed");
					String response ="";
					response = dataInpStream.readUTF();
					System.out.println(response);
					dataOutStream.flush();  
					dataInpStream.close();
					s.close();
				}
				catch(IOException ex)
				    {
					System.out.println("Unable to Create the group " + commands[1]);
				    }
			}
			if(commands[0].equals(cmp5)){
				try{
					//to other pc	//Connect
  				    // Socket s= new Socket("10.1.34.33",client_port);
					Socket s= new Socket(serverip,client_port);
				    DataInputStream dataInpStream=new DataInputStream(s.getInputStream());  
				    DataOutputStream dataOutStream=new DataOutputStream(s.getOutputStream());

                    byte[] contents;
					nameport = commands[1];
				    dataOutStream.writeUTF("create"+":" + commands[0] +":"+ourip+":"+nameport+":"+"createuser");
                    System.out.println("Create  User Completed");
					String response ="";
					response = dataInpStream.readUTF();
					System.out.println(response);
					dataOutStream.flush();  
					dataInpStream.close();
					s.close();
				}
				catch(IOException ex)
				    {
					System.out.println("Unable to Create the User " + commands[1]);
				    }
			}
			if(commands[0].equals(cmp3)){
				try{
					//to other pc	//Connect
  				    // Socket s= new Socket("10.1.34.33",client_port);
					// String IPaddr = "10.1.34.33";
					Socket s= new Socket(serverip,client_port);
					System.out.println(s.getLocalAddress().getHostAddress());
				    DataInputStream dataInpStream=new DataInputStream(s.getInputStream());  
				    DataOutputStream dataOutStream=new DataOutputStream(s.getOutputStream());

                    byte[] contents;
				    dataOutStream.writeUTF(commands[0] +":"+commands[1]+":"+ ourip +":" + nameport +":"+ "create");
                    System.out.println("Directory Creation Completed");
					dataOutStream.flush();  
					dataInpStream.close();
					s.close();
				}
				catch(IOException ex)
				    {
					System.out.println("Unable to create the directory " + commands[1]);
				    }
			    }
            if(commands[0].equals(cmp1) || commands[0].equals(cmp2)){
                //upload request
                try{
                    File fileName = new File(commands[1]);
                    FileInputStream fileInpStream = new FileInputStream(fileName);
                    BufferedInputStream BuffInpStream = new BufferedInputStream(fileInpStream);
                    //Connect
  				    Socket s= new Socket(serverip,client_port);
				    DataInputStream dataInpStream=new DataInputStream(s.getInputStream());  
				    DataOutputStream dataOutStream=new DataOutputStream(s.getOutputStream());

                    byte[] contents;
				    long size = fileName.length(),sentSize=0;
				    dataOutStream.writeUTF(commands[0] +":" + String.valueOf(size)+":"+commands[1]+":"+ourip+":"+nameport+":"+ "uploaded");
                    if(commands[0].equals(cmp1)){
                        //TCP
                        while(sentSize!=size)
						{
						    int window = 10000;
						    if(size - sentSize >= window)
							sentSize += window;
						    else
							{
							    window = (int)(size - sentSize);
								sentSize+=window;								
							}
						    contents = new byte[window];
						    BuffInpStream.read(contents,0,window);
						    TrasferStatus(sentSize,size);
						    dataOutStream.write(contents);
						}
					    System.out.println("File Transfer Completed");
					    dataOutStream.flush();  
					    dataInpStream.close();
					    s.close();
                    }
                    else
					{
					    //UDP.
					    //Closing the existing TCP socket.
					    dataOutStream.flush();  
					    dataInpStream.close();
					    s.close();

					    //Creating the UDP socket and sending.
					    DatagramSocket udpSocket = new DatagramSocket();
					    InetAddress IPAddress = InetAddress.getByName(serverip);

					    // InetAddress IPAddress = InetAddress.getByName("localhost");
					     while(sentSize!=size)
						{
						    int window = 10000;
						    if(size - sentSize >= window)
							sentSize += window;
						    else
							{
							    window = (int)(size - sentSize);
								sentSize+=window;
							}
						    contents = new byte[window];
						    BuffInpStream.read(contents,0,window);
						    TrasferStatus(sentSize,size);
						    DatagramPacket udpPacket = new DatagramPacket(contents,window, IPAddress, udp_client_port);
						    udpSocket.send(udpPacket);
						}
					     contents = new String("UDPEND").getBytes();
					     DatagramPacket udpPacket = new DatagramPacket(contents,6, IPAddress, udp_client_port);
					     udpSocket.send(udpPacket);
					     udpSocket.close();
					    
					}
				}
				catch(FileNotFoundException ex)
				    {
					System.out.println( commands[1]  + " File not found.");
				    }
				catch(IOException ex)
				    {
					System.out.println("Unable to open the file " + commands[1]);
				    }
			    }	    
            }
        }
        }	
}
