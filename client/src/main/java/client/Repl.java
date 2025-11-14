package client;

import java.util.Arrays;
import java.util.Scanner;
import exceptions.ErrorException;
import model.LoginRequest;
import model.UserData;

public class Repl {
    private final ServerFacade server;
    private final State state;

    public Repl(String url){
        server = new ServerFacade(url);
        state = State.SIGNEDOUT;
    }

    public void start(){
        System.out.println("Welcome to Ultimate Chess Legends Online I");
        System.out.println(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while(!result.equals("quit")){
            String input = scanner.nextLine();

            try {
                result = evaluateInput(input);
                System.out.println(result);
            } catch (Exception e) {
                var msg = e.getMessage();
                System.out.println(msg);
            }
        }
    }

    public String evaluateInput(String input){
        String[] args = input.toLowerCase().split(" ");
        String cmd = args[0];
        String[] params = Arrays.copyOfRange(args, 1, args.length);
        try{
            if(state.equals(State.SIGNEDOUT)) {
                return switch (cmd) {
                    case "register" -> register(params);
                    case "login" -> login(params);
                    case "help" -> help();
                    case "quit" -> "quit";
                    default -> "Invalid input: type help to see possible commands";
                };
            }
            else{
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
        } catch (Exception e){
            return e.getMessage();
        }
    }

    public String register(String[] params) throws Exception{
        if (params.length == 3){
            UserData data = new UserData(params[0],params[1],params[2]);
            return server.register(data);
        }
        else{
            return "Invalid number of arguments: type help to see possible commands";
        }
    }

    public String login(String[] params) throws Exception{
        if (params.length == 2){
            LoginRequest data = new LoginRequest(params[0],params[1]);
            return server.login(data);
        }
        else{
            return "Invalid number of arguments: type help to see possible commands";
        }
    }

    public String logout() throws ErrorException{
        return "Logout OK";
    }

    public String create(String[] params) throws ErrorException{
        if (params.length == 1){
            return "Create OK";
        }
        else{
            return "Invalid number of arguments: type help to see possible commands";
        }
    }

    public String list() throws ErrorException{
        return "List OK";
    }

    public String join(String[] params) throws ErrorException{
        if (params.length == 2){
            return "Join OK";
        }
        else{
            return "Invalid number of arguments: type help to see possible commands";
        }
    }

    public String observe(String[] params) throws ErrorException{
        if (params.length == 1){
            return "Observe OK";
        }
        else{
            return "Invalid number of arguments: type help to see possible commands";
        }
    }

    public String help(){
        if (state == State.SIGNEDOUT){
            return """
                    register <username> <password> <email> - create new account
                    login <username> <password> - sign in to your account
                    quit - close program
                    help - list possible commands
                    """;
        }
        else {
            return """
                    create <gamename> - create a new game
                    list - list all games
                    join <id> <WHITE|BLACK> - join a game
                    observe <id> - observe a game
                    logout - sign out of your account
                    quit - close program
                    help - list possible commands
                    """;
        }

    }
}
