package org.ahamdah;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Service {
    public static List<Distribution> listAll() throws IOException, InterruptedException {
        ProcessBuilder processBuilder=new ProcessBuilder("cmd.exe", "/c","wsl -l -v");
        Process process=processBuilder.start();
        process.waitFor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        List<Distribution> distributions = new ArrayList<>();
        String line=reader.readLine();
        while ((line=reader.readLine()) != null) {
            String x1= line.replace('*',' ');
            String x2=x1.replaceAll("\\u0000", "");
            if(x2.length()!=0) {
                String[] parts = x2.split("\\s+");
                distributions.add(new Distribution(parts[1], parts[2], parts[3]));
            }
        }
        return distributions;
    }
    public static void turnOn(String name) throws IOException, InterruptedException {
        ProcessBuilder processBuilder=new ProcessBuilder("cmd.exe","/c",String.format("wsl -e %s",name));
        Process process=processBuilder.start();
        process.waitFor();
    }
    public static void turnOff(String name) throws IOException, InterruptedException {
        ProcessBuilder processBuilder=new ProcessBuilder("cmd.exe","/c",String.format("wsl -t %s",name));
        Process process=processBuilder.start();
        process.waitFor();
    }
}
