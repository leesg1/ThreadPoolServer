import java.io.*;
import java.net.*;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

class Client {
	static FileInputStream fis = null;
	static BufferedInputStream bis = null;
	static Socket socket;
	static int testFileNum = 0;
	static OutputStream outToServer;
	static BufferedReader inFromServer;
	static String dir = "C:\\Users\\Sean\\workspace2\\";
	public static int port;

	Client() {
		// which calls the common constructor with the GUI set to null
		this("localhost", port);
	}

	public Client(String string, int i) {
		// TODO Auto-generated constructor stub
	}

	public static void sendFile(String file) throws IOException {
		File myFile = new File(dir + file);
		byte[] mybytearray = new byte[(int) myFile.length()];
		fis = new FileInputStream(myFile);
		bis = new BufferedInputStream(fis);
		bis.read(mybytearray, 0, mybytearray.length);
		outToServer = socket.getOutputStream();
		System.out.println("Sending " + file + "(" + mybytearray.length
				+ " bytes)");
		outToServer.write(mybytearray, 0, mybytearray.length);
		outToServer.flush();
		System.out.println("Done.");
	}

	
	
	public static void receiveFile(String fileName) throws IOException{
		
		DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());		
	//	 DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		 DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		
		 String sentence = "DownloadFile";
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		outToServer.writeBytes("Download " + fileName + "\n");
	//	inFromServer.close();
		 
		 
		 
		 
		 
		 
		 
		 
		 byte[] bytes = new byte[1024];
 	    try {
			in.read(bytes);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
//	    System.out.println("File is in" +bytes);

	    FileOutputStream fos = null;
		try {
			fos = new FileOutputStream("test" + testFileNum + ".txt");
			testFileNum++;
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	    try {
			fos.write(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    fos.flush();
		fos.close();
		System.out.println("Received File complete\n");
	}
	
	
	public static void main(String[] argv) throws Exception {
		int port = Integer.parseInt(argv[0]);
//		BufferedReader br = null;
		System.out.println("Do you want to upload[1] or download[2] a file?\n");
		Scanner scan = new Scanner(System.in);
		int s = scan.nextInt();
		socket = new Socket("localhost", 1500);
		DataOutputStream outToServer = new DataOutputStream(
				socket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(
				new InputStreamReader(socket.getInputStream()));
		
		
		if (s == 1) {
			System.out.println("What file do you want to upload?\n");
			String fileName = scan.next();
			sendFile(fileName);
		}
		else if(s == 2){
			System.out.println("What file do you want to download?\n");
			String fileName = scan.next();
			receiveFile(fileName);
			
		}
		inFromServer.close();
		outToServer.close();
		socket.close();
	}

}