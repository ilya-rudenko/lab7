package command;

import util.CommandPackage;
import util.ServerResponse;

@FunctionalInterface
public interface CommandInterface {
    ServerResponse execute(CommandPackage arg);
}
