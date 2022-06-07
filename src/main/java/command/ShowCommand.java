package command;

import collections.MovieCollection;
import util.CommandPackage;
import util.ServerResponse;

public class ShowCommand implements Command{
    private MovieCollection collection;

    public ShowCommand(MovieCollection collection){
        this.collection=collection;
    }

    @Override
    public synchronized ServerResponse execute(CommandPackage arg) {
        return collection.print(arg);
    }
}
