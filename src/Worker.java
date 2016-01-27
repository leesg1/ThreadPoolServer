import java.io.*;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Worker implements Runnable {
	static FileInputStream fis = null;
	static BufferedInputStream bis = null;
	static OutputStream outToClient;

	Socket socket;
	public Worker(Socket socket) throws IOException, InterruptedException {
		this.socket = socket;
		BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String message = inFromClient.readLine();
		System.out.println("Message from client:" + message);
	//	Thread.sleep(1000);
		if(message.equals("HELO text\n".trim())){
			dealWithHELO(socket);
		}
		else if(message.equals("KILL_SERVICE\n".trim())){
			dealWithKill(socket);
		}
		else if(message.equals("Upload\n".trim())){
			receiveFile();
		}
		else if(message.contains("Download".trim())){
			String fileName = inFromClient.readLine();
			sendFile(fileName);
		}
	}
	private void saveFile() throws IOException{
		System.out.println("In saveFile");
	//	 DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		 DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		 byte[] bytes = new byte[1024];
 	    try {
			in.read(bytes);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	    System.out.println(bytes);

	    FileOutputStream fos = null;
		try {
			fos = new FileOutputStream("testSavingOnServer.txt");
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
	}
	
	// can refactor this. 2 methods is unnecesary 
	private void receiveFile() {
		Path dir = Paths.get("");
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
		    for (Path file: stream) {
		        System.out.println(file.getFileName());
		    }
		saveFile();    
		System.out.println("Saved File");
		} catch (IOException | DirectoryIteratorException x) {
		    // IOException can never be thrown by the iteration.
		    System.err.println(x);
		}
	}
	private void sendFile(String fileName){
		String file = "C:\\Users\\Sean\\";
		File myFile = new File(file + fileName);
		byte[] mybytearray = new byte[(int) myFile.length()];
		try {
			fis = new FileInputStream(myFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bis = new BufferedInputStream(fis);
		try {
			bis.read(mybytearray, 0, mybytearray.length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			outToClient = socket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Sending " + file + "(" + mybytearray.length
				+ " bytes)");
		try {
			outToClient.write(mybytearray, 0, mybytearray.length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			outToClient.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Done.");
	}

	private void dealWithHELO(Socket socket) throws UnknownHostException {
		System.out.println("HELO text\nIP:" + Inet4Address.getLocalHost()+"\nPort:"
			+socket.getLocalPort()+"\nStudentID:1ee9647a04c08c2a6d5896be1df1d272d50e01606f56790c53f1d406336b1609\n");
	}
	
	private void dealWithKill(Socket socket) throws IOException {
		socket.close();
		ThreadPool.executor.shutdownNow();
	}

	public void run() {
	}
}