package dreamlink.menu.component.core.button;

public class StaticButtonComponentProvider implements IButtonComponentProvider {

    private Runnable onClick;

    public StaticButtonComponentProvider(Runnable onClick) {
        this.onClick = onClick;
    }

    @Override
    public void onButtonClick() {
        this.onClick.run();
    }

    @Override
    public boolean isButtonDisabled() {
        return false;
    }
    
}
