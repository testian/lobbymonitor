/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lobbymonitor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author testi
 */
public class TiberianSunMonitor extends LobbyMonitor {

private HashMap<SocketAddress,Player> playerList;
private boolean ra2;

private volatile boolean proceed;

public TiberianSunMonitor(boolean ra2) {
playerList = new HashMap<SocketAddress,Player>();
this.ra2 = ra2;
}

public TiberianSunMonitor() {
this(false);
}

public void listen() throws IOException {
SharedSocket s = SharedSocket.getSocket(InetAddress.getByName("0.0.0.0"), 5000);
s.setSoTimeout(10000);
proceed = true;
while (proceed) {
    try {
    parse(s.receive());
    }
     catch (SocketTimeoutException ex) {

    }
    sortOut();

}


}

private void parse(DatagramPacket p) {

int nameOffset = ra2 ? 24 : 17;
int expPacketLength = ra2 ? 475 : 468;

/*for (int i = p.getOffset(); i < p.getLength() + p.getOffset(); i++) {
System.out.print(p.getData()[i] + " ");

}
System.out.println();
System.out.println(new String(p.getData(),p.getOffset(),p.getLength()));*/
    //System.out.println(p.getLength());
    if (p.getLength() != expPacketLength) return;
//if (p.getData()[p.getOffset()] != (byte)126 && p.getData()[p.getOffset()] != (byte)42) return;

//String s = new String(p.getData(),p.getOffset(),p.getLength());
int end = -1;
for (int i = p.getOffset() + nameOffset; i < p.getOffset() + p.getLength(); i++) {
byte b = p.getData()[i];
if ( b == 0) {end = i;break;}
}
String name;
try {
name = new String(p.getData(),p.getOffset()+nameOffset,end-p.getOffset()-nameOffset,"ISO-8859-1"); } catch(UnsupportedEncodingException ex) {name = new String(p.getData(),p.getOffset()+nameOffset,end-p.getOffset()-nameOffset);}
insert(p.getSocketAddress(),name);
if (end == -1) return;

}
public void stop() throws IOException {
proceed = false;
    //TODO: Cleanup code missing
}



    @Override
    public String getGameName() {
        return ra2 ? "Red Alert 2" : "Tiberian Sun";
    }

    private void insert(SocketAddress a, String name) {
    Player p = playerList.get(a);
    long now = System.currentTimeMillis();
    long cnow = now;
    String pName;
    if (p == null) {
    this.notifyJoin(name);
    pName = name;
    } else {
    pName = p.previousName;
    if (name.equals(p.name)) {
    cnow = p.lastNameChange;
    }
    }

    /*if (p != null && !p.name.equals(name)) {
    this.notifyRename(p.name, name);
    }*/
    
    
    playerList.put(a,new Player(name,now,pName,cnow));
    }

    private void sortOut() {
        Iterator<Map.Entry<SocketAddress, Player>> it = playerList.entrySet().iterator();
    while (it.hasNext()) {
    Map.Entry<SocketAddress,Player> e = it.next();
    long now = System.currentTimeMillis();
    if (now-e.getValue().stamp > 30000) {
        this.notifyPart(e.getValue().name);
        it.remove();
    } else if (now-e.getValue().lastNameChange > 10000 && !e.getValue().name.equals(e.getValue().previousName)) {
    this.notifyRename(e.getValue().previousName, e.getValue().name);
    e.getValue().previousName = e.getValue().name;
    }
    }
    }
    private static class Player {
    public String name;
    public long stamp;
    public String previousName;
    public long lastNameChange;

        public Player(String name, long stamp, String previousName, long lastNameChange) {
            this.stamp = stamp;
            this.name = name;
            this.previousName = previousName;
            this.lastNameChange = lastNameChange;
        }


    }


}
