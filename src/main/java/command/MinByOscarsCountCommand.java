package command;

import collections.MovieCollection;
import util.CommandPackage;
import util.ServerResponse;


public class MinByOscarsCountCommand implements Command{
    private MovieCollection collection;

    public MinByOscarsCountCommand(MovieCollection collection){
        this.collection=collection;
    }

    @Override
    public synchronized ServerResponse execute(CommandPackage arg) {
        return collection.minByOscarsCount(arg);
    }
}
