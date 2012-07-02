/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lobbymonitor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.HashMap;

/**
 *
 * @author Dimitri Nüscheler
 */
public class SharedSocket {

    public static int PACKET_SIZE = 2048;

    public synchronized static SharedSocket getSocket(InetAddress bind, int port) throws SocketException {
    SharedSocket existing = map.get(port);
    if (existing == null) {
    DatagramSocket ds = new DatagramSocket(port, bind);
    //ds.bind(new InetSocketAddress(bind,port));
    SharedSocket ss = new SharedSocket(ds,PACKET_SIZE);
    map.put(port, ss);
    return ss;
    }

    else {
    return existing;
    }
    }

    private static HashMap<Integer, SharedSocket> map = new HashMap<Integer, SharedSocket>(); //port can be reused on other interfaces, so this is not correct







    private DatagramSocket decorate;
    private boolean receiving;
    private Object rpack;
    private int recSize;

    private SharedSocket(DatagramSocket socket, int recSize) {
        this.decorate = socket;
        receiving = false;
        this.recSize = recSize;
    }

    public synchronized void setSoTimeout(int timeout) throws SocketException {
        decorate.setSoTimeout(timeout);
    }

    public synchronized int getSoTimeout() throws SocketException {
        return decorate.getSoTimeout();
    }

    public DatagramPacket receive() throws IOException {



    boolean rec;
        synchronized(this) {
        rec = receiving;
        receiving = true;
            if (rec) {
            try {
                wait();
            } catch (InterruptedException ex) {
                throw new IOException(ex);
            }
        }}

        if (!rec) {
            DatagramPacket p = new DatagramPacket(new byte[recSize], recSize);
            try {
                decorate.receive(p);
                rpack = p;
            } catch (IOException ex) {
                rpack = ex;
            }


            receiving = false;
            synchronized (this) {
            notifyAll();
            }

        }


        if (rpack instanceof DatagramPacket) {
            return (DatagramPacket) rpack;
        } else {
            throw (IOException) rpack;
        }

    }


}
