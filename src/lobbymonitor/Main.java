/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lobbymonitor;

import java.net.DatagramPacket;
import java.net.InetAddress;

/**
 *
 * @author testi
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       
        TiberianSunMonitor tsm = new TiberianSunMonitor(true);
        tsm.addListener(new LobbyListener() {

            public void onJoin(String gameName, String playerName) {
                System.out.println(playerName + " joined " + gameName  + " lobby");
            }

            public void onPart(String gameName, String playerName) {
                System.out.println(playerName + " left " + gameName  + " lobby");
            }

            public void onRename(String gameName, String from, String to) {
                System.out.println(from + " is now known as " + to);
            }

        
        });
        try {
        tsm.listen();
        } catch (Exception ex) {System.err.println(ex);ex.printStackTrace();}
        /* Runnable r = new Runnable() {

            public void run() {
                try {
                SharedSocket s = SharedSocket.getSocket(InetAddress.getByName("0.0.0.0"), 5000);
                DatagramPacket p = s.receive();
                String str = new String(p.getData(),p.getOffset(),p.getLength());
                
                
                System.out.println("Received message: " + str);

                System.out.println(str.indexOf("Collada"));
                for (int i = p.getOffset(); i < p.getLength() + p.getOffset(); i++) {
                System.out.print(p.getData()[i] + " ");
                }
                System.out.println();


                } catch (Exception ex) {System.err.println(ex);}
            }

        };

        //r.run();
        for (int i = 0; i < 100; i++) {
        r.run();
        //Thread t = new Thread(r);
        //t.start();
        }*/
    }

}
