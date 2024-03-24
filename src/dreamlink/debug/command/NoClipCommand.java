package dreamlink.debug.command;

import dreamlink.logger.Logger;
import dreamlink.player.Player;

public class NoClipCommand implements IDebugCommand {

    private static String commandName = "player.noclip";

    @Override
    public String getCommandName() {
        return NoClipCommand.commandName;
    }

    @Override
    public void run(String[] args) {
        if(args.length != 2) {
            throw new InvalidCommandException();
        }

        if(args[1].equals("true")) {
            var msg = String.format("%s enabled", NoClipCommand.commandName);
            Player.instance.noClip = true;
            Logger.instance.info(msg);
        } else if(args[1].equals("false")) {
            Player.instance.noClip = false;
            var msg = String.format("%s disabled", NoClipCommand.commandName);
            System.out.println(msg);
        } else {
            throw new InvalidCommandException();
        }
    }
    
}
