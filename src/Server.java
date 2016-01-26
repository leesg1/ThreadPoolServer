import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server {

	private int port;
	private boolean keepGoing;
	public ServerSocket serverSocket;

	public Server(int port) {
		this.port = port;
	}

	public void start() throws UnknownHostException, IOException {
		keepGoing = true;
			serverSocket = new ServerSocket(port);
			System.out.println("IP address: " + Inet4Address.getLocalHost());
			while (keepGoing) {
				Socket socket = serverSocket.accept(); 
				try {
					ThreadPool.executor.execute(new Worker(socket));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (!keepGoing)
					break;
			}
		}

	public static void main(String[] args) throws UnknownHostException, IOException {
		int portNumber = Integer.parseInt(args[0]);
		Server server = new Server(portNumber);
		server.start();
	}
}













			// get the server to accept the file
			
			// things to do
			
			// create some sort of interface for the client to allow read/write 
			// 









