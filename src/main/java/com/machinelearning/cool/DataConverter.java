package com.machinelearning.cool;

import java.io.*;

public class DataConverter {

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader("src/data/u.data"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("src/data/ratings.csv"));

        String line;
        while((line = br.readLine()) != null) {
            String[] values = line.split("\\t", -1);
            bw.write(values[0] + "," + values[1] + "," + values[2] + "\n");
        }

        br.close();
        bw.close();

    }
}
