package com.agutsul.readme.reader;

public class Main {

    public static void main(String[] args) {
        ReadMeService readMeService = new ReadMeServiceImpl(args[0]);
        readMeService.printMostUsedWords();
    }
}
