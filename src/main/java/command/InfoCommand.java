package command;

import collections.MovieCollection;
import util.CommandPackage;
import util.ServerResponse;

public class InfoCommand implements Command{
    private MovieCollection collection;

    public InfoCommand(MovieCollection collection){
        this.collection=collection;
    }

    @Override
    public synchronized ServerResponse execute(CommandPackage arg) {
        return collection.info();
    }
}