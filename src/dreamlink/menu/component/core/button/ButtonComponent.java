package dreamlink.menu.component.core.button;

import dreamlink.menu.component.core.BaseMenuComponent;
import dreamlink.menu.component.core.WrapperComponent;
import dreamlink.menu.component.core.border.button.ButtonBorderComponent;
import dreamlink.menu.component.core.border.button.ButtonState;
import dreamlink.menu.event.ClickEvent;
import dreamlink.menu.event.IEvent;

public class ButtonComponent extends WrapperComponent {

    private IButtonComponentProvider provider;
    private BaseMenuComponent component;

    public ButtonComponent(
        IButtonComponentProvider provider,
        BaseMenuComponent component
    ) {
        this.provider = provider;
        this.component = new ButtonBorderComponent(
            this::getButtonSchemaState,
            component
        );
    }

    private ButtonState getButtonSchemaState() {
        if(this.provider.isButtonDisabled()) {
            return ButtonState.disabled;
        } else if(this.isPressed() && this.isHovered()) {
            return ButtonState.pressed;
        } else {
            return ButtonState.normal;
        }
    } 

    @Override
    public void onEvent(IEvent event) {
        super.onEvent(event);
        if(event instanceof ClickEvent)  {
            this.provider.onButtonClick();
        }
    }

    @Override
    public BaseMenuComponent getComponent() {
        return this.component;
    }

    public BaseMenuComponent getHoveredComponent() {
        if(this.provider.isButtonDisabled() || !this.isMouseOver()) {
            return null;
        }

        return this;
    }

}
