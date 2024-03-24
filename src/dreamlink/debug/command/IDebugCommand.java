package dreamlink.debug.command;

public interface IDebugCommand {

    public String getCommandName();

    public void run(String[] command);
    
}
