package command;

import util.CommandPackage;
import util.ServerResponse;

public interface Command {
    ServerResponse execute(CommandPackage arg);
}
