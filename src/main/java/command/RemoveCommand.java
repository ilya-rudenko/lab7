package command;

import collections.MovieCollection;
import util.CommandPackage;
import util.ServerResponse;

public class RemoveCommand implements Command{
    private MovieCollection collection;

    public RemoveCommand(MovieCollection collection){
        this.collection=collection;
    }

    @Override
    public synchronized ServerResponse execute(CommandPackage arg) {
        return collection.remove(arg);
    }
}