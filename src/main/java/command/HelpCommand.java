package command;

import collections.MovieCollection;
import util.CommandPackage;
import util.ServerResponse;

public class HelpCommand implements Command{
    private MovieCollection collection;

    public HelpCommand(MovieCollection collection){
        this.collection=collection;
    }

    @Override
    public synchronized ServerResponse execute(CommandPackage arg) {
        return collection.help();
    }
}
