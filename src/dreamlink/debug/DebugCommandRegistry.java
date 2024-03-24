package dreamlink.debug;

import dreamlink.debug.command.IDebugCommand;
import dreamlink.debug.command.InvalidCommandException;
import dreamlink.debug.command.NoClipCommand;
import dreamlink.debug.command.ShowReticuleCommand;
import dreamlink.logger.Logger;

public class DebugCommandRegistry {

    private static IDebugCommand[] commands = new IDebugCommand[] {
        new NoClipCommand(),
        new ShowReticuleCommand()
    };

    public static DebugCommandRegistry instance = new DebugCommandRegistry();

    public void runCommand(String commandStr) {
        var split = commandStr.split(" ");
        for(var command : DebugCommandRegistry.commands) {
            if(command.getCommandName().equals(split[0])) {
                var msg = String.format("Running command: %s", command.getCommandName());
                Logger.instance.info(msg);
                try {
                    command.run(split);
                } catch (InvalidCommandException e) {
                    var invalidMsg = String.format("Command invalid: %s", commandStr);
                    Logger.instance.error(invalidMsg);
                }
                return;
            }
        }

        var msg = String.format("Command not found: %s", split[0]);
        Logger.instance.error(msg);
    }
    
}
