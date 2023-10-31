package example;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.KeyStore;


public class SSLSocketServer
{
    private static String kpath = "D:\\Github\\Data_Security\\keytool\\sslserver.keystore";
    private static String tpath = "D:\\Github\\Data_Security\\keytool\\sslserver.keystore";
    private static char[] password = "123456789".toCharArray();

    public static void main(String[] args)
    {
        boolean flag = true;
        SSLContext context = null;
        try
        {
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream(kpath), password);
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, password);
            KeyManager[] km = kmf.getKeyManagers();
            KeyStore ts = KeyStore.getInstance("JKS");
            ts.load(new FileInputStream(tpath), password);
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ts);
            TrustManager[] tm = tmf.getTrustManagers();
            context = SSLContext.getInstance("SSL");
            context.init(km, tm, null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        SSLServerSocketFactory ssf = (SSLServerSocketFactory) context.getServerSocketFactory();
        try
        {
            SSLServerSocket ss = (SSLServerSocket) ssf.createServerSocket(8000);
            //ss.setNeedClientAuth(true);
            //ss.setUseClientMode(true);
            //ss.setWantClientAuth(true);
            System.out.println("Server: waiting for the connection....");
            while (flag)
            {
                Socket s = ss.accept();
                System.out.println("Server: received the request from client");
                ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
                os.writeObject("Reply from Server : Authentication success for client request");
                os.flush();
                os.close();
                System.out.println();
                s.close();
            }
            ss.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
