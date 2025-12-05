package server.websocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, CopyOnWriteArraySet<Session>> connections = new ConcurrentHashMap<>();
    private final Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .create();

    public void add(Integer gameID, Session session) {
        connections.computeIfAbsent(gameID, k -> new CopyOnWriteArraySet<>()).add(session);
    }

    public void remove(Integer gameID, Session session) {
        connections.get(gameID).remove(session);
    }

    public void broadcast(Integer gameID, Session excludeSession, ServerMessage message) throws IOException {
        String msg = gson.toJson(message);
        for (Session c : connections.get(gameID)) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }
}