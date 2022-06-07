package util;

import collections.MovieCollection;
import command.*;
import exceptions.NoSuchCommandException;
import stream.StreamManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Console {
//
//    StreamManager streamManager;
//    MovieCollection collection;
//    CommandManager manager;
//
//    String path;
//
//    Map<String, CommandInterface> commandMap;
//    LinkedList<String> commandHistory;
//
//    Integer recursionLength;
//
//    public Console(){
//        streamManager = new StreamManager(false);
//        collection=new MovieCollection(streamManager);
//        commandHistory= new LinkedList<String>();
//        recursionLength=0;
//
//        manager = new CommandManager(new AddCommand(collection),new ShowCommand(collection),new ExitCommand(collection),new UpdateCommand(collection),new RemoveCommand(collection),new ClearCommand(collection),new SaveCommand(collection),new LoadCommand(collection),new ExecuteCommand(this),new AddIfMaxCommand(collection),new RemoveLowerCommand(collection),new HistoryCommand(this),new MinByOscarsCountCommand(collection),new CountGreaterCommand(collection),new PrintAscendingCommand(collection),new HelpCommand(collection), new InfoCommand(collection));
//        commandMap = new HashMap<String, CommandInterface>();
//        commandMap.put("add",arg-> manager.add());
//        commandMap.put("show",arg-> manager.show());
//        commandMap.put("exit",arg->manager.exit());
//        commandMap.put("update",arg-> manager.update(arg));
//        commandMap.put("remove_by_id",arg-> manager.remove(arg));
//        commandMap.put("clear",arg-> manager.clear());
//        commandMap.put("save",arg->manager.save(path));
//        commandMap.put("load",arg->manager.load(path));
//        commandMap.put("execute_script",arg -> manager.execute(arg));
//        commandMap.put("add_if_max",arg->manager.addIfMax());
//        commandMap.put("remove_lower",arg->manager.removeLower());
//        commandMap.put("history",arg->manager.returnHistory());
//        commandMap.put("min_by_oscars_count",arg -> manager.minByOscarsCount());
//        commandMap.put("count_greater_than_oscars_count",arg->manager.countGreater(arg));
//        commandMap.put("print_ascending",arg->manager.printAscending());
//        commandMap.put("help",arg -> manager.help());
//        commandMap.put("info",arg->manager.info());
//    }
//
//    public void run(){
//        if (System.getenv("DATAPATH")!=null && collection.load(System.getenv("DATAPATH"))){
//            path=System.getenv("DATAPATH");
//        }
//        else{
//            streamManager.print("Collection wasn't loaded successfully, current file is \"data.json\" in project" ,0);
//            path="data.json";
//            collection.load(path);
//        }
//
//        String inputedCommand;
//        String command;
//        String argument;
//
//
//        while(true){
//            inputedCommand = streamManager.stringRequest("Enter the command: ", false);
//
//            String[] commandArray = inputedCommand.trim().split(" ");
//            if (commandArray.length>2) {
//                streamManager.print("Too much arguments in command",0);
//                continue;
//            }
//
//            command=commandArray[0];
//            if (commandArray.length==1)argument=null;
//            else argument=commandArray[1];
//
//            if (commandMap.get(command)!=null){
//                commandMap.get(command).execute(argument);
//                if (commandHistory.size()>=13){
//                    commandHistory.addFirst(inputedCommand);
//                    commandHistory.removeLast();
//                }
//                else{
//                    commandHistory.addFirst(inputedCommand);
//                }
//            }
//            else{
//                streamManager.print("There is no such command",0);
//            }
//        }
//    }
//    public void executeScript (String path){
//        if (recursionLength>=10) {
//            streamManager.print("Reached recursion length limit",0);
//            return;
//        }
//        recursionLength+=1;
//        try(BufferedReader br = new BufferedReader (new FileReader(path)))
//        {
//            StreamManager fileStreamManager=new StreamManager(true);
//            fileStreamManager.setReader(br);
//
//            collection.setStreamManager(fileStreamManager);
//
//            String command;
//            String argument;
//
//            String s;
//            while((s=br.readLine())!=null){
//
//                String[] commandArray = s.split(" ");
//                if (commandArray.length>2){
//                    streamManager.print("There is no such command",0);
//                    continue;
//                }
//
//                command=commandArray[0];
//                if (commandArray.length==1)argument=null;
//                else argument=commandArray[1];
//
//                if (commandMap.get(command)!=null){
//                    commandMap.get(command).execute(argument);
//                }
//                else{
//                    streamManager.print("There is no such command",0);
//                }
//            }
//            collection.setStreamManager(streamManager);
//        }
//        catch(Exception ex){
//            streamManager.print("Cannot open the file",0);
//        }
//    }
//
//    public void returnHistory(){
//        streamManager.print(commandHistory);
//    }

}
// execute_script script.txt