package command;

import collections.MovieCollection;
import util.CommandPackage;
import util.ServerResponse;

public class ClearCommand implements Command{
    private MovieCollection collection;

    public ClearCommand(MovieCollection collection){
        this.collection=collection;
    }

    @Override
    public synchronized ServerResponse execute(CommandPackage arg) {
        return collection.clear(arg);
    }
}
