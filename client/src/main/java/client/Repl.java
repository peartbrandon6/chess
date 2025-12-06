package client;

import java.util.Arrays;
import java.util.Scanner;

import chess.ChessGame;
import model.CreateGameRequest;
import model.JoinGameRequest;
import model.LoginRequest;
import model.UserData;
import ui.DrawBoard;

public class Repl {
    private final ServerFacade server;
    private State state;
    private ChessGame game;

    public Repl(String url){
        server = new ServerFacade(url);
        state = State.SIGNEDOUT;
    }

    public void start(){
        System.out.println("Welcome to Ultimate Chess Legends Online I");
        System.out.println(help());
        printConsole();

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while(!result.equals("quit")){
            String input = scanner.nextLine();

            try {
                result = evaluateInput(input);
                System.out.println(result);
                printConsole();
            } catch (Exception e) {
                var msg = e.getMessage();
                System.out.println(msg);

            }
        }
    }

    private void printConsole(){
        if(state.equals(State.SIGNEDOUT)){
            System.out.println("\n[SIGNED_OUT] >>> ");
        }
        else if(state.equals(State.SIGNEDIN)){
            System.out.println("\n[SIGNED_IN] >>> ");
        }
        else if(state.equals(State.INGAME)){
            System.out.println("\n[IN_GAME] >>> ");
        }

    }

    public String evaluateInput(String input){
        String[] args = input.toLowerCase().split(" ");
        String cmd = args[0];
        String[] params = Arrays.copyOfRange(args, 1, args.length);
        try{
            if (state.equals(State.SIGNEDOUT)) {
                return switch (cmd) {
                    case "register" -> register(params);
                    case "login" -> login(params);
                    case "help" -> help();
                    case "quit" -> "quit";
                    default -> "Invalid input: type help to see possible commands";
                };
            }
            else if (state.equals(State.SIGNEDIN)){
                return switch (cmd) {
                    case "logout" -> logout();
                    case "create" -> create(params);
                    case "list" -> list();
                    case "join" -> join(params);
                    case "observe" -> observe(params);
                    case "help" -> help();
                    case "quit" -> "quit";
                    default -> "Invalid input: type help to see possible commands";
                };
            }
            else if (state.equals(State.INGAME)){
                return switch (cmd) {
                    case "redraw" -> redrawChessBoard();
                    case "leave" -> leaveGame();
                    case "make_move" -> makeMove(params);
                    case "resign" -> resignGame();
                    case "highlight" -> highlightLegalMoves(params);
                    case "help" -> help();
                    default -> "Invalid input: type help to see possible commands";
                };
            }
        } catch (Exception e){
            return e.getMessage();
        }
        return "Error: Illegal state";
    }

    public String register(String[] params) throws Exception{
        if (params.length == 3){
            UserData data = new UserData(params[0],params[1],params[2]);
            var res = server.register(data);
            if(!res.contains("Error")) {
                this.state = State.SIGNEDIN;
            }
            return res;
        }
        else{
            return "Invalid number of arguments: type help to see possible commands";
        }
    }

    public String login(String[] params) throws Exception{
        if (params.length == 2){
            LoginRequest data = new LoginRequest(params[0],params[1]);
            var res = server.login(data);
            if(!res.contains("Error")){
                this.state = State.SIGNEDIN;
            }
            return res;
        }
        else{
            return "Invalid number of arguments: type help to see possible commands";
        }
    }

    public String logout() throws Exception{
        var res = server.logout();
        if(!res.contains("Error")){
            this.state = State.SIGNEDOUT;
        }
        return res;
    }

    public String create(String[] params) throws Exception{
        if (params.length == 1){
            return server.createGame(new CreateGameRequest(params[0]));
        }
        else{
            return "Invalid number of arguments: type help to see possible commands";
        }
    }

    public String list() throws Exception{
        return server.listGames();
    }

    public String join(String[] params) throws Exception{
        if (params.length == 2){
            int id;
            try{
                id = Integer.parseInt(params[0]);
            } catch (Exception e){
                return "Invalid game ID";
            }
            var res = server.joinGame(new JoinGameRequest(params[1].toUpperCase(), id));
            state = State.INGAME;
            return res;
        }
        else{
            return "Invalid number of arguments: type help to see possible commands";
        }
    }

    public String observe(String[] params) throws Exception{
        if (params.length == 1){
            int id;
            try{
                id = Integer.parseInt(params[0]);
            } catch (Exception e){
                return "Invalid game ID";
            }
            var res = server.observeGame(id);
            state = State.INGAME;
            return res;
        }
        else{
            return "Invalid number of arguments: type help to see possible commands";
        }
    }

    public String redrawChessBoard() {
        server.drawBoard();
        return "";
    }

    public String leaveGame() {
        // TODO: Implement leave logic
        return "";
    }

    public String makeMove(String[] params) {
        // TODO: Implement move logic
        return "";
    }

    public String resignGame() {
        // TODO: Implement resign logic
        return "";
    }

    public String highlightLegalMoves(String[] params) {
        // TODO: Implement highlight logic
        return "";
    }

    public String help(){
        if (state == State.SIGNEDOUT){
            return """
                    register <username> <password> <email> - create new account
                    login <username> <password> - sign in to your account
                    quit - close program
                    help - list possible commands""";
        }
        else if (state == State.SIGNEDIN) {
            return """
                    create <gamename> - create a new game
                    list - list all games
                    join <id> <WHITE|BLACK> - join a game
                    observe <id> - observe a game
                    logout - sign out of your account
                    quit - close program
                    help - list possible commands""";
        }
        else if (state == State.INGAME) {
            return """
                    redraw - redraw the chessboard
                    leave - leave the game
                    make_move <start> <finish> - make a move (e.g. make_move e2 e4)
                    resign - forfeit the match
                    highlight <piece>- highlights all legal moves a piece can make (e.g. highlight e2)
                    help - list possible commands""";
        }
        else{
            return "Error: Illegal state";
        }

    }
}
