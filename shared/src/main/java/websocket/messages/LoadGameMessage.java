package websocket.messages;

public class LoadGameMessage extends ServerMessage {
    private String game;    // Change this to a different object if better
    public LoadGameMessage(String game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public String getGame() {
        return game;
    }
}
