package client;

import java.util.Arrays;
import java.util.Scanner;
import exceptions.ErrorException;

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
        String cmd = cmd = args[0];
        String[] params = Arrays.copyOfRange(args, 1, args.length);
        try{
            return switch(cmd){
                case "register" -> register(params);
                case "help" -> help();
                case "quit" -> "quit";
                default -> "Invalid input: type help to see possible commands";
            };
        } catch (ErrorException e){
            return e.getMessage();
        }
    }

    public String register(String[] params) throws ErrorException{
        return "Register OK";
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
                    list- list all games
                    join <id> <WHITE|BLACK> - join a game
                    observe <id> - observe a game
                    logout - sign out of your account
                    quit - close program
                    help - list possible commands
                    """;
        }

    }
}
