package util;

import collections.MovieCollection;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import command.*;
import data.Movie;
import database.DBAnswer;
import database.DBConnector;
import database.DBManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import stream.OutputColor;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.apache.commons.lang3.SerializationUtils.deserialize;
import static org.apache.commons.lang3.SerializationUtils.serialize;

public class ServerConsole {
    private final static Logger logger = LogManager.getLogger();
    Gson gson = new Gson();

    static ByteBuffer inputBuffer;
    static ByteBuffer outputBuffer;
    static int PORT = 50001;
    SocketAddress remoteAdd;
    DatagramChannel server;

    CommandManager manager;
    String path;

    MovieCollection collection;
    public Map<String, CommandInterface> commandMap;
    LinkedList<String> commandHistory;
    public ServerResponse response;
    public CommandPackage commandPackage;
    ExecutorService service;
    Changer currentThread;

    DBManager dbManager;
    DBConnector dbConnector;

    public ServerConsole(){
        dbConnector=new DBConnector();
        try {
            dbManager = new DBManager(dbConnector.connect());
        }catch (Exception e){
            System.out.println("Cant connect to database");
            System.exit(-1);
        }
        collection=new MovieCollection(dbManager);
        commandHistory= new LinkedList<>();
        CommandPackage commandPackage;

        manager = new CommandManager(new AddCommand(collection),new ShowCommand(collection),new UpdateCommand(collection),new RemoveCommand(collection),new ClearCommand(collection),new AddIfMaxCommand(collection),new RemoveLowerCommand(collection),new MinByOscarsCountCommand(collection),new CountGreaterCommand(collection),new PrintAscendingCommand(collection),new HelpCommand(collection), new InfoCommand(collection));
        commandMap = new HashMap<>();
        commandMap.put("add",arg-> manager.add(arg));
        commandMap.put("show",arg-> manager.show(arg));
//        commandMap.put("exit",);
        commandMap.put("update",arg-> manager.update(arg));
        commandMap.put("remove_by_id",arg-> manager.remove(arg));
        commandMap.put("clear",arg-> manager.clear(arg));
        commandMap.put("add_if_max",arg->manager.addIfMax(arg));
        commandMap.put("remove_lower",arg->manager.removeLower(arg));
        commandMap.put("min_by_oscars_count",arg -> manager.minByOscarsCount(arg));
        commandMap.put("count_greater_than_oscars_count",arg->manager.countGreater(arg));
        commandMap.put("print_ascending",arg->manager.printAscending(arg));
        commandMap.put("help",arg -> manager.help(arg));
        commandMap.put("info",arg->manager.info(arg));
        commandMap.put("login", this::login);
        commandMap.put("sign_in", this::signin);

        try {
            server = DatagramChannel.open();

            InetSocketAddress iAdd = new InetSocketAddress("localhost", PORT);
            server.bind(iAdd);

            logger.info("Server Started: " + iAdd);
            inputBuffer = ByteBuffer.allocate(10240);
            outputBuffer = ByteBuffer.allocate(10240);
            server.socket().setSoTimeout(3000);
        }
        catch (IOException e) {
            logger.error("Can't establish a connection. Shutting down...");
            e.printStackTrace();
            System.exit(-1);
        }

        service = Executors.newFixedThreadPool(5);


    }

    public void run() {
        if(collection.setCollection()){
            logger.info("Collection was loaded");
        }
        else{
            logger.info("Collection wasn't loaded");
        }


        Runtime current = Runtime.getRuntime();
        current.addShutdownHook(new Thread() {
            public void run() {
                logInfo("Shutting down ...");
//                save(path);
            }
        });

//        streamManager.setManager(manager,path);

        while (true) {
            commandPackage=receivePackage();
            currentThread = new Changer(this);
            currentThread.start();
        }
    }

    public ServerResponse login(CommandPackage arg){
        if (dbManager.loginUser(arg.getUsername(),arg.getPassword())){
            return new ServerResponse("Success!", OutputColor.GREEN,Result.SUCCESS);
        }
        else{
            return new ServerResponse("Failure!", OutputColor.RED,Result.FAILURE);
        }
    }

    public ServerResponse signin(CommandPackage arg){
        if (dbManager.addUser(arg.getUsername(),arg.getPassword())){
            return new ServerResponse("Success!", OutputColor.GREEN,Result.SUCCESS);
        }
        else{
            return new ServerResponse("Failure!", OutputColor.RED,Result.FAILURE);
        }
    }

    Callable<SocketAddress> receiveBuffer = () ->{
        SocketAddress add= server.receive(inputBuffer);
        return add;
    };
    Callable sendBuffer = () ->{
        server.send(outputBuffer,remoteAdd);
        return null;
    };

    public CommandPackage receivePackage(){
        try {
            remoteAdd= service.submit(receiveBuffer).get();
            inputBuffer.flip();
            CommandPackage command = deserialize(inputBuffer.array());
            logInfo("Got package: " + command);
            inputBuffer.clear();

            return command;
        }
        catch (Exception e){
            logError("Wrong package: "+e.getMessage());
            return null;
        }

    }

    public void sendPackage(String line,OutputColor type,Result result){
        try {
            outputBuffer=ByteBuffer.allocate(10240);
            outputBuffer.put(serialize(new ServerResponse(line,type,result)));
            outputBuffer.flip();

            service.submit(sendBuffer);

            outputBuffer.clear();
            logInfo("Response was sent: "+new ServerResponse(line,type,result));
        }
        catch (Exception e){
            logError("Something went wrong with sending package: "+e.getMessage());
        }
    }


    public void logError(String line){
        logger.error(line);
    }
    public void logInfo(String line){
        logger.info(line);
    }

}
 class Changer extends Thread{
    ServerConsole con;
    ServerResponse res;
    Changer(ServerConsole con){
        this.con=con;
    }

    @Override
    public void run() {
        if (con.commandMap.get(con.commandPackage.getCommand()) != null) {
            res = con.commandMap.get(con.commandPackage.getCommand()).execute(con.commandPackage);
            con.sendPackage(res.getResponse(),res.getType(),res.getRes());
        } else {
            con.sendPackage("There is no such command", OutputColor.RED,Result.FAILURE);
        }
    }
}