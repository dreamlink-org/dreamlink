package dreamlink.menu.component.core.border.dialog;

public class StaticDialogBorderComponentProvider implements IDialogBorderComponentProvider {

    private DialogState state;

    public StaticDialogBorderComponentProvider(DialogState state) {
        this.state = state;
    }

    @Override
    public DialogState getDialogState() {
        return this.state;
    }
    
}
