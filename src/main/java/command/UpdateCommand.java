package command;

import collections.MovieCollection;
import util.CommandPackage;
import util.ServerResponse;

public class UpdateCommand implements Command{
    private MovieCollection collection;

    public UpdateCommand(MovieCollection collection){
        this.collection=collection;
    }

    @Override
    public synchronized ServerResponse execute(CommandPackage arg) {
        return collection.update(arg);
    }
}