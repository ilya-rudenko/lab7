package command;

import collections.MovieCollection;
import util.CommandPackage;
import util.ServerResponse;

public class CountGreaterCommand implements Command{
    private MovieCollection collection;

    public CountGreaterCommand(MovieCollection collection){
        this.collection=collection;
    }

    @Override
    public synchronized ServerResponse execute(CommandPackage arg) {
        return collection.countGreater(arg);
    }
}