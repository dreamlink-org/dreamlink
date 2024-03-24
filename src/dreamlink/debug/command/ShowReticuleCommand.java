package dreamlink.debug.command;

import dreamlink.hud.Reticule;
import dreamlink.logger.Logger;

public class ShowReticuleCommand implements IDebugCommand {

    private static String commandName = "hud.showreticule";

    @Override
    public String getCommandName() {
        return ShowReticuleCommand.commandName;
    }

    @Override
    public void run(String[] args) {
        if(args.length != 2) {
            throw new InvalidCommandException();
        }

        if(args[1].equals("true")) {
            var msg = String.format("%s enabled", ShowReticuleCommand.commandName);
            Reticule.instance.showReticule = true;
            Logger.instance.info(msg);
        } else if(args[1].equals("false")) {
            Reticule.instance.showReticule = false;
            var msg = String.format("%s disabled", ShowReticuleCommand.commandName);
            System.out.println(msg);
        } else {
            throw new InvalidCommandException();
        }
    }
    
}
