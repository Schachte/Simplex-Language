package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Simplex {
    private static boolean hadError = false;

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Usage: slex [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    /**
     * Read in file bytes given input path
     **/
    private static void runFile(String path) throws IOException {
        byte[] inputBytes = Files.readAllBytes(Paths.get(path));
        run(new String(inputBytes, Charset.defaultCharset()));
    }

    /**
     * Run an interactive REPL to execute slex scripts directly
     **/
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (; ; ) {
            System.out.print("> ");
            run(reader.readLine());
        }
    }

    private static void run(String source) {
        Scanner scanner = new Scanner(source);
//        List<Token> tokens = scanner.scanTokens();
//
//        for (Token token : tokens) {
//            System.out.println(tokens);
//        }
    }

    public static void error(int line, String message) {
        report(line, "", message);
    }

    private static void report(int line, String where, String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }

}
