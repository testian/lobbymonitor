/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lobbymonitor;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author testi
 */
public abstract class LobbyMonitor {

     private Set<LobbyListener> listener;

     public LobbyMonitor() {
     listener = new HashSet<LobbyListener>();
     }

     synchronized public void addListener(LobbyListener listener) {
     this.listener.add(listener);
     }
     synchronized public void removeListener(LobbyListener listener) {
     this.listener.remove(listener);
     }

     synchronized protected void notifyJoin(String playerName) {
     for (LobbyListener l : listener) {
     l.onJoin(getGameName(), playerName);
     }
     }

     synchronized protected void notifyPart(String playerName) {
     for (LobbyListener l : listener) {
     l.onPart(getGameName(), playerName);
     }
     }
     synchronized protected void notifyRename(String from, String to) {
     for (LobbyListener l : listener) {
     l.onRename(getGameName(), from, to);
     }
     }

     public abstract String getGameName();
     public abstract void listen() throws IOException;
     public abstract void stop() throws IOException;

}
