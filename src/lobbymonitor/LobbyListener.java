/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lobbymonitor;

/**
 *
 * @author testi
 */
public interface LobbyListener {

    public void onJoin(String gameName, String playerName);
    public void onPart(String gameName, String playerName);
    public void onRename(String gameName, String from, String to);

}
