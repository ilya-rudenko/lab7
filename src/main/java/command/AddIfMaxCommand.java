package command;

import collections.MovieCollection;
import util.CommandPackage;
import util.ServerResponse;

public class AddIfMaxCommand implements Command{
    private MovieCollection collection;

    public AddIfMaxCommand(MovieCollection collection){
        this.collection=collection;
    }

    @Override
    public synchronized ServerResponse execute(CommandPackage arg) {
        return collection.addIfMax(arg);
    }
}