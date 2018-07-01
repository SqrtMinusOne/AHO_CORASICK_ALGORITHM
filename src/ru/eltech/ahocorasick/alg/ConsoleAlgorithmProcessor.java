package ru.eltech.ahocorasick.alg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;


class StringHandler{
    public StringHandler(){
        command = "";
        nextHandler = null;
    }

    StringHandler(String str){
        command = str;
        nextHandler = null;
    }

    void Handle(String request) {
        if (request.startsWith(getCommand())){
            doHandle(request);
        }
        else{
            goNext(request);
        }
    }

    public ArrayList<String> nextCommands(ArrayList<String> list){
        list.add(command);
        if (nextHandler!=null)
            nextHandler.nextCommands(list);
        return list;
    }

    protected void doHandle(String request){

    }

    private void goNext(String request){
        if (nextHandler!=null){
            nextHandler.Handle(request);
        }
    }
    StringHandler addNext(StringHandler handler){
        if (nextHandler!=null)
            nextHandler.addNext(handler);
        else
            nextHandler = handler;
        return handler;
    }

    private String getCommand() {
        return command;
    }

    private StringHandler nextHandler;
    String command;
}

public class ConsoleAlgorithmProcessor {
    public ConsoleAlgorithmProcessor(PrintStream ostream, InputStream istream) {
        this.ostream = ostream;
        this.istream = istream;
    }

    public void startConsole() {
        scanner = new Scanner(istream);
        String command;
        StringHandler handler = new HelpHandler();
        handler.addNext(new ExitHandler()).addNext(new AboutHandler()).addNext(new InitHandler()).
                addNext(new ResetHandler()).addNext(new RestartHandler()).addNext(new PrintBohrHandler()).
                addNext(new AddHandler()).addNext(new SetTextHandler()).addNext(new TextHandler()).
                addNext(new FileTextHandler()).addNext(new FileStringHandler()).addNext(new StepHandler()).
                addNext(new ResultsHandler()).addNext(new FinishHandler()).addNext(new PrintStringsHandler());
        handler.addNext(new DefaultHandler());
        do {
            ostream.print("AC> ");
            command = scanner.nextLine();
            try {
                handler.Handle(command);
            } catch (NullPointerException n) {
                processNPE(n);
            }
        } while (!toExit);
    }



    private void processNPE(NullPointerException n){
        ostream.println("NullPointerException");
        if (algorithm == null) {
            ostream.println("Algorithm is not initialized");
        }
        else{
            n.printStackTrace(ostream);
        }
    }

    private boolean toExit;

    class ExitHandler extends StringHandler{
        ExitHandler() {
            super("exit");
        }

        @Override
        protected void doHandle(String request) {
            toExit = true;
        }
    }

    class HelpHandler extends StringHandler{
        HelpHandler() {
            super("help");
        }

        @Override
        protected void doHandle(String request) {
            ostream.println("Available commands:");
            ArrayList<String> list = super.nextCommands(new ArrayList<>());
            list.sort(String::compareTo);
            for (String com : list){
                ostream.println(com);
            }
        }
    }

    class AboutHandler extends StringHandler{
        AboutHandler() {
            super("about");
        }

        @Override
        protected void doHandle(String request) {
            ostream.println("Aho-Corasick Algorithm implementation\n" +
                    "Authors:\n" +
                    " - Vinogradov Kirill, 6304\n" +
                    " - Korytov Pavel, 6304\n" +
                    " - Tsyganov Mikhail, 6304");
        }
    }

    class InitHandler extends StringHandler{
        InitHandler() {
            super("init");
        }

        @Override
        protected void doHandle(String request) {
            algorithm = new Algorithm();
        }
    }

    class PrintStringsHandler extends StringHandler{
        PrintStringsHandler() {
            super("print strings");
        }

        @Override
        protected void doHandle(String request) {
            for (String str : algorithm.getStrings()){
                ostream.println(str);
            }
        }
    }

    class ResetHandler extends StringHandler{
        ResetHandler() {
            super("reset");
        }

        @Override
        protected void doHandle(String request) {
            algorithm.reset();
        }
    }

    class RestartHandler extends StringHandler{
        RestartHandler() {
            super("restart");
        }

        @Override
        protected void doHandle(String request) {
            algorithm.restart();
        }
    }

    class PrintBohrHandler extends StringHandler{
        PrintBohrHandler() {
            super("print bohr");
        }

        @Override
        protected void doHandle(String request) {
            ostream.println(algorithm);
        }
    }

    class AddHandler extends StringHandler{
        AddHandler() {
            super("add");
        }

        @Override
        protected void doHandle(String request) {
            algorithm.addString(request.substring(4));
        }
    }

    class SetTextHandler extends StringHandler{
        SetTextHandler() {
            super("settext");
        }

        @Override
        protected void doHandle(String request) {
            algorithm.setText(request.substring(8));
        }
    }

    class TextHandler extends StringHandler{
        TextHandler() {
            super("text");
        }

        @Override
        protected void doHandle(String request) {
            ostream.println(algorithm.getText());
        }
    }

    class FileTextHandler extends StringHandler{
        FileTextHandler() {
            super("open text");
        }

        @Override
        protected void doHandle(String request) {
            ostream.print("Enter text file name: ");
            Scanner fileScanner;
            String fileName = scanner.nextLine();
            try {
                fileScanner = new Scanner(new File(fileName));
            }
            catch (FileNotFoundException e){
                ostream.println("File not found");
                return;
            }
            StringBuilder sb = new StringBuilder();
            while (fileScanner.hasNextLine()){
                sb.append(fileScanner.nextLine().replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase()).append(" ");
            }
            algorithm.setText(sb.toString());
        }
    }

    class FileStringHandler extends StringHandler{
        FileStringHandler() {
            super("open strings");
        }

        @Override
        protected void doHandle(String request) {
            ostream.print("Enter string file name: ");
            Scanner fileScanner;
            String fileName = scanner.nextLine();
            try {
                fileScanner = new Scanner(new File(fileName));
            }
            catch (FileNotFoundException e){
                ostream.println("File not found");
                return;
            }
            while (fileScanner.hasNext()){
                String str = fileScanner.nextLine().replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase();
                for (String strx : str.split(" ")){
                    algorithm.addString(strx);
                }
            }
        }
    }

    class StepHandler extends StringHandler{
        StepHandler() {
            super("step");
        }

        @Override
        protected void doHandle(String request) {
            boolean res = algorithm.doStep();
            if (res)
                ostream.println("Step done. Position: " + algorithm.getTextPosition());
            else
                ostream.println("Algorithm is done");
        }
    }

    class ResultsHandler extends StringHandler{
        ResultsHandler() {
            super("results");
        }

        @Override
        protected void doHandle(String request) {
            for (AlgorithmResult res : algorithm.getResults()){
                ostream.print("[" + res.getIndex() + ":" + res.getPatternNumber() + "] ");
            }
            ostream.println();
        }


    }

    class FinishHandler extends StringHandler{
        FinishHandler() {
            super("finish");
        }

        @Override
        protected void doHandle(String request) {
            algorithm.finishAlgorithm();
        }
    }

    class DefaultHandler extends StringHandler{
        DefaultHandler() {
            super("");
        }

        @Override
        protected void doHandle(String request) {
            ostream.println("Command not found");
        }
    }

    private PrintStream ostream;
    private InputStream istream;
    private Scanner scanner;

    private Algorithm algorithm;
}
