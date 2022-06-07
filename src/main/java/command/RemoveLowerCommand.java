package command;

import collections.MovieCollection;
import util.CommandPackage;
import util.ServerResponse;

public class RemoveLowerCommand implements Command{
    private MovieCollection collection;

    public RemoveLowerCommand(MovieCollection collection){
        this.collection=collection;
    }

    @Override
    public synchronized ServerResponse execute(CommandPackage arg) {
        return collection.removeLower(arg);
    }
}