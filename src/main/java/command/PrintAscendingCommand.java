

package command;

import collections.MovieCollection;
import util.CommandPackage;
import util.ServerResponse;


public class PrintAscendingCommand implements Command{

    private MovieCollection collection;

    public PrintAscendingCommand(MovieCollection collection){
        this.collection=collection;
    }

    @Override
    public synchronized ServerResponse execute(CommandPackage arg) {
        return collection.printAscending(arg);
    }
}