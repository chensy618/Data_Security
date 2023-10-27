package src;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.UnknownHostException;
import java.security.KeyStore;


public class SSLSocketClient
{
    private static String kpath = "D:\\Github\\Data_Security\\keytool\\sslclient.keystore";
    private static String tpath = "D:\\Github\\Data_Security\\keytool\\sslclient.keystore";
    private static char[] password = "123456789".toCharArray();

    /**
     * @param args
     */
    public static void main(String[] args)
    {
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
        SSLSocketFactory ssf = context.getSocketFactory();
        try
        {
            SSLSocket ss = (SSLSocket) ssf.createSocket("localhost", 8000);
            //ss.setNeedClientAuth(true);
            //ss.setUseClientMode(true);
            //ss.setWantClientAuth(true);
            System.out.println("Client : I'm client, requesting server ....");
            ObjectInputStream br = new ObjectInputStream(ss.getInputStream());
            try
            {
                System.out.println(br.readObject());
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
            br.close();
            ss.close();
            System.out.println("Client: Authentication success for server reply");
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

