package dreamlink.menu.component.core.tab;

import dreamlink.menu.component.core.BaseMenuComponent;
import dreamlink.menu.component.core.PaddingComponent;
import dreamlink.menu.component.core.WrapperComponent;
import dreamlink.menu.component.core.border.tab.TabBorderComponent;
import dreamlink.menu.component.core.box.BoxComponent;
import dreamlink.menu.component.core.box.BoxDimension;
import dreamlink.menu.event.IEvent;
import dreamlink.menu.event.PressStartEvent;

public class TabComponent extends WrapperComponent {

    private static int padding = 2;

    private ITabComponentProvider provider;
    private TabBorderComponent component;

    public TabComponent(
        ITabComponentProvider provider,
        BaseMenuComponent component
    ) {
        this.provider = provider;
        this.component = new TabBorderComponent(
            this.provider::isTabSelected,
            new BoxComponent(
                new PaddingComponent(component, padding),
                BoxDimension.grow(),
                BoxDimension.wrap()
            )
        );
    }

    @Override
    public BaseMenuComponent getComponent() {
        return this.component;
    }

    @Override
    public void onEvent(IEvent event) {
        if(event instanceof PressStartEvent)  {
            this.provider.onTabSelect();
        }
    }

    @Override
    public BaseMenuComponent getHoveredComponent() {
        if(this.provider.isTabSelected() || !this.isMouseOver()) {
            return null;
        }
        return this;
    }

}
