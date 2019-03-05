package ru.ifmo.rain.kudaiberdieva.walk;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class Walk {
    private static Path inputFile;
    private static Path outputFile;

    public static void main(String[] args) {
        if (args == null || args.length != 2 || args[0] == null || args[1] == null) {
            System.err.println("\nMissing argument. Try again, please.\n");
            return;
        }

        try {
            inputFile = Paths.get(args[0]);
            outputFile = Paths.get(args[1]);
            if (outputFile.getParent() != null)
                Files.createDirectories(outputFile.getParent());
        } catch (IllegalArgumentException e) {
            System.err.println("\nWrong format for URI.\n");
            return;
        } catch (FileSystemNotFoundException e) {
            System.err.println("\nThe file doesn't exist or can not be created automatically.\n");
            return;
        } catch (SecurityException e) {
            System.err.println("\nPermission denied.\n");
            return;
        } catch (IOException e) {
            //ignored
        }

        try (BufferedReader br = Files.newBufferedReader(inputFile, StandardCharsets.UTF_8)) {
            try (BufferedWriter bw = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8)) {
                int hash = 0;
                String currentFileName;
                while ((currentFileName = br.readLine()) != null) {
                    hash = getHash(currentFileName);
                    write(bw, hash, currentFileName);
                }
            } catch (IOException e) {
                System.err.println("\nCan't open file for writing.\n");
            }
        } catch (IOException e) {
            System.err.println("\nCan't open file for writing.\n");
        }
    }

    public static void write(BufferedWriter bw, int hash, String fileName) throws IOException {
        bw.write(String.format("%08x %s%n", hash, fileName));
    }

    public static int getHash(String fileName) {
        int BUFFER_SIZE = 4096;
        byte[] BUFFER = new byte[BUFFER_SIZE];

        int hash;
        try (FileInputStream bis = new FileInputStream(fileName)) {
            hash = 0x811c9dc5;
            int amountOfBytes;

            while ((amountOfBytes = bis.read(BUFFER)) != -1) {
                for (int i = 0; i < amountOfBytes; ++i) {
                    hash = (hash * 0x01000193) ^ (BUFFER[i] & 0xFF);
                }
            }
        } catch (IOException e) {
            hash = 0;
        }
        return hash;
    }

}
