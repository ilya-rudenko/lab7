package stream;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import data.EnumInterface;
import data.Movie;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.TreeSet;

public class StreamManager {
    private boolean fileMode;
    private BufferedReader reader;

    String ANSI_RESET = "\u001B[0m";
    String ANSI_RED = "\u001B[31m";
    String ANSI_GREEN = "\u001B[32m";
    String ANSI_BLUE = "\u001B[34m";

    Scanner scanner;
    Gson gson = new Gson();

    public StreamManager(boolean fileMode){
        scanner= new Scanner(System.in);
        this.fileMode=fileMode;
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    public void print(Object arg) {
        System.out.println(arg.toString());
    }

    public void print(Object arg,OutputColor c) {
        if (arg==null){
            if (c==OutputColor.RED) System.out.println(ANSI_RED+"null"+ANSI_RESET);
            else if (c==OutputColor.GREEN) System.out.println(ANSI_GREEN+"null"+ANSI_RESET);
            else if (c==OutputColor.BLUE) System.out.println(ANSI_BLUE+"null"+ANSI_RESET);
            return;
        }
        if (c==OutputColor.RED) System.out.println(ANSI_RED+arg+ANSI_RESET);
        else if (c==OutputColor.GREEN) System.out.println(ANSI_GREEN+arg+ANSI_RESET);
        else if (c==OutputColor.BLUE) System.out.println(ANSI_BLUE + arg + ANSI_RESET);
    }
    public void printWithoutN(Object arg){
        System.out.print(arg);
    }

    public String stringRequest(String arg,boolean nullable){
        if (fileMode){
            try {
                String line;
                if ((line = reader.readLine()) != null){
                    if (line.trim().equals("")) return null;
                    return line;}
                return null;
            }
            catch (Exception e){
                print(e,OutputColor.RED);
            }
        }
        else {
                printWithoutN(arg);
                String response = null;
                while (response == null || response.equals("")) {
                    try {
                        response = scanner.nextLine().trim();
                    }
                    catch (Exception e) {
                        print("End of file reached. Exiting program",OutputColor.GREEN);
                        System.exit(1);
                    }

                    if (nullable && response.trim().equals("")) return null;
                    if (!response.equals("")) break;
                    print("String cannot be null", OutputColor.RED);
                    printWithoutN(arg);
                }
                return response;
            }
        return null;
    }
    public Integer intRequest(String arg,boolean nullable,Integer min,Integer max){
        if(fileMode){
            try {
                String response = stringRequest(arg,nullable);
                if (response==null && nullable) return null;
                Integer answ = Integer.valueOf(response);
                if (min ==null && max==null) return answ;
                else if (min==null){
                    if (answ > max) print("Number cannot be bigger than "+max,OutputColor.RED);
                    else return answ;
                }
                else if (max==null) {
                    if (answ < min) print("Number cannot be lower than "+min,OutputColor.RED);
                    else return answ;
                }
                else {
                    if (answ > max) print("Number cannot be bigger than "+max,OutputColor.RED);
                    else if (answ < min) print("Number cannot be lower than "+min,OutputColor.RED);
                    else return answ;
                }
            }
            catch(Exception e) {
                print("It is not a number",OutputColor.RED);
            }
        }
        else {
            while (true){
                try {
                    String response = stringRequest(arg,nullable);
                    if (response==null && nullable) return null;
                    Integer answ = Integer.valueOf(response);
                    if (min ==null && max==null) return answ;
                    else if (min==null){
                        if (answ > max) print("Number cannot be bigger than "+max,OutputColor.RED);
                        else return answ;
                    }
                    else if (max==null) {
                        if (answ < min) print("Number cannot be lower than "+min,OutputColor.RED);
                        else return answ;
                    }
                    else {
                        if (answ > max) print("Number cannot be bigger than "+max,OutputColor.RED);
                        else if (answ < min) print("Number cannot be lower than "+min,OutputColor.RED);
                        else return answ;
                    }
                }
                catch(Exception e) {
                    print("It is not a number",OutputColor.RED);
                }
            }
        }
        return null;
    }

    public String enumRequest(String arg, EnumInterface enumTemplate, boolean nullable){
        while(true){
            String response = stringRequest(arg,nullable);
            if (response==null && nullable) return null;
            if (enumTemplate.includesInEnum(response)) return response;
            print("There is no such answer option",OutputColor.RED);
        }
    }

    public boolean writeToFile(String path,Object obj){
        try {
            new FileOutputStream(path).write(gson.toJson(obj).getBytes(StandardCharsets.UTF_8));
            return true;
        }
        catch (Exception e){
            print("You dont have access to the file. The collection will not be saved or loaded",OutputColor.RED);
            return false;
        }
    }
    public TreeSet<Movie> loadFromFile(String path){
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line="";
            StringBuilder jsonString= new StringBuilder();
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            return gson.fromJson(jsonString.toString(),new TypeToken<TreeSet<Movie>>() {}.getType());
        }
        catch (Exception e) {
            File file = new File(path);
            print("There's something wrong with the file. File will be overwritten ",OutputColor.RED);
        }
        return null;
    }
}
