package command;

import collections.MovieCollection;
import util.CommandPackage;
import util.ServerResponse;

public class CommandManager {
    Command add,update,remove,clear,minByOscarsCount,printAscending;
    Command show,save,load,execute,countGreater,help;
    Command exit,addIfMax,removeLower,returnHistory,info;

    public CommandManager(Command add,Command show,Command update,Command remove,Command clear,Command addIfMax,Command removeLower,Command minByOscarsCount, Command countGreater, Command printAscending,Command help,Command info){
        this.add=add;
        this.show=show;
        this.update=update;
        this.remove=remove;
        this.clear=clear;
        this.addIfMax=addIfMax;
        this.removeLower=removeLower;
        this.minByOscarsCount=minByOscarsCount;
        this.countGreater=countGreater;
        this.printAscending=printAscending;
        this.help=help;
        this.info=info;
    }

    public ServerResponse add(CommandPackage arg){
        return add.execute(arg);
    }
    public ServerResponse addIfMax(CommandPackage arg){return addIfMax.execute(arg);}
    public ServerResponse removeLower(CommandPackage arg){return removeLower.execute(arg);}

    public ServerResponse show(CommandPackage arg){
        return show.execute(arg);
    }
    public ServerResponse update(CommandPackage arg) {return update.execute(arg);}
    public ServerResponse remove(CommandPackage arg) {return remove.execute(arg);}
    public ServerResponse clear(CommandPackage arg) {return clear.execute(arg);}
    public ServerResponse minByOscarsCount(CommandPackage arg){return minByOscarsCount.execute(arg);}
    public ServerResponse countGreater(CommandPackage arg){return countGreater.execute(arg);}
    public ServerResponse printAscending(CommandPackage arg){return printAscending.execute(arg);}
    public ServerResponse help(CommandPackage arg){return help.execute(arg);}
    public ServerResponse info(CommandPackage arg){return info.execute(arg);}
}
