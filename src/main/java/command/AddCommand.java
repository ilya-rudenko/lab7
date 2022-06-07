package command;

import collections.MovieCollection;
import util.CommandPackage;
import util.ServerResponse;


public class AddCommand implements Command{
    private MovieCollection collection;

    public AddCommand(MovieCollection collection){
        this.collection=collection;
    }

    @Override
    public synchronized ServerResponse execute(CommandPackage arg) {
        return collection.add(arg);
    }
}
