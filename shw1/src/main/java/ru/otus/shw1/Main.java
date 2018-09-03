package ru.otus.shw1;

import lombok.extern.java.Log;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


@Log
public class Main {
    public static void main(String ... args) {
        if(args.length < 2){
            log.severe("USAGE: java -jar jcp <SOURCE> <TARGET>");
            System.exit(1);
        }

        List<File> files = Arrays.stream(args).map(File::new).collect(Collectors.toList());

        if(!files.get(0).isFile()){
            log.severe(files.get(0).getName().concat(" is not a file!"));
            System.exit(2);
        }

        if(files.get(1).isFile()){
            Scanner in = new Scanner(System.in);
            log.info(files.get(1).getName() + " already exists. Would you like to override? [yY/nN]:");
            while(in.hasNext()) {
                String answer = in.next();
                if(answer.matches("[yY]")){ break; }
                else {
                    if (answer.matches("[nN]")) System.exit(0);
                }
            }
        }

        try {
            FileUtils.copyFile(files.get(0), files.get(1));
        } catch (IOException e) {
            log.severe("Failed to copy file:".concat(e.getMessage()));
        }

    }
}
