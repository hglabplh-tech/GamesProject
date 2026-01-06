package io.github.hglabplh_tech.mines.test.utils;

import io.github.hglabplh_tech.mines.backend.Labyrinth;
import io.github.hglabplh_tech.mines.backend.SweeperLogic;
import io.github.hglabplh_tech.mines.backend.jsonstore.JSONUtil;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static io.github.hglabplh_tech.mines.backend.jsonstore.JSONUtil.*;


public class JSONTestUtil {

    public static final String outFilePath;

    public static final String completePath;

    public static final File pathFile;

    public static final File dataFile;


    static {
        outFilePath = System.getProperty("user.home") +
                "/gamesTest/";
        completePath = outFilePath + "sweeplogic.tmp";

        pathFile = new File(outFilePath);

        dataFile = new File(completePath);
    }

    public static void writeSweepLogicToFile(SweeperLogic logic, String fileName) throws IOException {
        String fullName = outFilePath + fileName;
        FileWriter writer = new FileWriter(fullName);
        storeSweepLogic(logic, writer);
        writer.close();
    }

    public static SweeperLogic loadSweepLogicFromFile(String fileName) throws IOException {
        String fullName = outFilePath + fileName;
        FileReader reader = new FileReader(fullName);
        SweeperLogic logic = loadSweepLogic(reader);
        reader.close();
        return logic;
    }

    public static void writeLabyrinthToFile(Labyrinth labyrinth, String fileName) throws IOException {
        String fullName = outFilePath + fileName;
        FileWriter writer = new FileWriter(fullName);
        storeLabyrinth(labyrinth, writer);
        writer.close();
    }

    public static Labyrinth loadLabyrinthFromFile(String fileName) throws IOException {
        String fullName = outFilePath + fileName;
        FileReader reader = new FileReader(fullName);
        Labyrinth labyrinth = loadLabyrinth(reader);
        reader.close();
        return labyrinth;
    }



}
