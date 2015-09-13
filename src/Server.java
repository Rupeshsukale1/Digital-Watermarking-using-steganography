import java.io.IOException;
import java.net.ServerSocket;
import java.util.Vector;
class Server implements Runnable {
	
    private ServerSocket connect;
    private static Vector<BackHandler> sockets = null;
    private static int portNumber = 8000, maxClients = 10;
    private Thread thread;
    private Image_Watermark steg;

    public Server(Image_Watermark stegano){
        System.out.println("Server");
        steg = stegano;
        thread = new Thread(this) ;
        sockets = new Vector<BackHandler>();
        try {
            connect = new ServerSocket(portNumber, maxClients);
            thread.start();
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }
    @SuppressWarnings("static-access")
    public void run(){
        try{
            while (true) {
                sockets.addElement(new BackHandler(connect.accept(),steg));
                thread.yield();
            }
        }
        catch(Exception ex){
            System.out.println("run : " + ex.toString()) ;
        }
    }

    @SuppressWarnings("deprecation")
	public void stopServer(){
        try{
            connect.close() ;
            thread.stop() ;
        }
        catch(Exception ex){
            System.out.println("Error : " + ex.toString()) ;
        }
    }
}
