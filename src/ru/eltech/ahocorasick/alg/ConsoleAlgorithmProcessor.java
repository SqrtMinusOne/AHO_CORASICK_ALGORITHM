package ru.eltech.ahocorasick.alg;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


class StringHandler{
    StringHandler(){
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

    ArrayList<String> nextCommands(ArrayList<String> list){
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
    private final String command;
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
                addNext(new ResultsHandler()).addNext(new FinishHandler()).addNext(new PrintStringsHandler()).
                addNext(new StatusHandler()).addNext(new SaveHandler()).addNext(new OpenHandler()).
                addNext(new UndoHandler()).addNext(new RedoHandler());
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
            if (algorithm.getStrings()!=null) {
                for (String str : algorithm.getStrings()) {
                    ostream.println(str);
                }
            }
            else
                ostream.println("null");
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
            ostream.println(algorithm.resultsToString());
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

    class StatusHandler extends StringHandler{
        StatusHandler() {
            super("status");
        }

        @Override
        protected void doHandle(String request) {
            ostream.println(algorithm.getStatus());
        }
    }

    class SaveHandler extends StringHandler{
        SaveHandler() {
            super("save");
        }

        @Override
        protected void doHandle(String request) {
            ostream.print("Enter file name: ");
            String name = scanner.nextLine();
            try {
                FileWriter fw = new FileWriter(name);
                fw.write(algorithm.toString());
                fw.close();
            }
            catch (IOException e) {
                ostream.println("Output failed");
            }
        }
    }

    class OpenHandler extends StringHandler{
        OpenHandler() {
            super("open alg");
        }

        @Override
        protected void doHandle(String request) {
            ostream.print("Enter file name: ");
            String name = scanner.nextLine();
            try{
                Scanner fc = new Scanner(new File(name));
                String text = fc.useDelimiter("\\A").next();
                algorithm = Algorithm.fromString(text);
                fc.close();
                ostream.println("Reading complete. Status: " + algorithm.getStatus());
            }
            catch (IOException e){
                ostream.println("Reading failed");
            }
        }
    }

    class UndoHandler extends StringHandler{
        UndoHandler() {
            super("undo");
        }

        @Override
        protected void doHandle(String request) {
            Algorithm alg = algorithm.getHistory().undo(algorithm);
            if (alg!=null){
                algorithm = alg;
            }
            else
                ostream.println("History is empty");
        }
    }

    class RedoHandler extends StringHandler{
        RedoHandler() {super("redo"); }

        @Override
        protected void doHandle(String request) {
            Algorithm alg = algorithm.getHistory().redo();
            if (alg!=null){
                algorithm = alg;
            }
            else
                ostream.println("Nothing to redo");
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

    private final PrintStream ostream;
    private final InputStream istream;
    private Scanner scanner;

    private Algorithm algorithm;
}
