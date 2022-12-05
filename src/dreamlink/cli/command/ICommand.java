package dreamlink.cli.command;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

public interface ICommand {

    public String getCommandName();

    public void setupParser(Subparser subparser);

    public void run(Namespace namespace);
    
}
