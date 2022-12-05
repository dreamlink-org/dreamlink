package dreamlink.menu.component.core.tab;

import dreamlink.menu.component.core.BaseMenuComponent;
import dreamlink.menu.component.core.PaddingComponent;
import dreamlink.menu.component.core.WrapperComponent;
import dreamlink.menu.component.core.border.tab.TabBodyBorderComponent;
import dreamlink.menu.component.core.choice.ChoiceComponent;
import dreamlink.menu.component.core.span.SpanAlignment;
import dreamlink.menu.component.core.span.SpanComponent;
import dreamlink.menu.component.core.span.SpanOrientation;

public class TabViewComponent extends WrapperComponent {

    private class InternalTabComponentProvider implements ITabComponentProvider {

        private int tabID;

        public InternalTabComponentProvider(int tabID) {
            this.tabID = tabID;
        }

        @Override
        public boolean isTabSelected() {
            return this.tabID == TabViewComponent.this.selectedTabID;
        }
        
        @Override
        public void onTabSelect() {
            TabViewComponent.this.selectedTabID = this.tabID;
        }

    }

    private static int padding = 2;

    private SpanComponent tabGroupComponent;
    private ChoiceComponent<Integer> viewComponent;
    private BaseMenuComponent component;

    private int selectedTabID = 0;
    private int tabIDGenerator = 0;

    public TabViewComponent() {
        this.tabGroupComponent = new SpanComponent(SpanOrientation.horizontal, SpanAlignment.start, 0);
        this.viewComponent = new ChoiceComponent<>(this::getSelectedTabID);
        this.component = new SpanComponent(SpanOrientation.vertical, SpanAlignment.start, 0)
            .addComponent(this.tabGroupComponent)
            .addComponent(
                new TabBodyBorderComponent(
                    new PaddingComponent(
                        this.viewComponent,
                        TabViewComponent.padding
                    )
                )
            );
    }

    public TabViewComponent addTab(BaseMenuComponent tabContent, BaseMenuComponent content) {
        this.viewComponent.addComponent(this.tabIDGenerator, content);

        this.tabGroupComponent.addComponent(
            new TabComponent(
                new InternalTabComponentProvider(this.tabIDGenerator),
                tabContent
            )
        );

        this.tabIDGenerator += 1;
        return this;
    }

    private int getSelectedTabID() {
        return this.selectedTabID;
    }

    @Override
    public BaseMenuComponent getComponent() {
        return this.component;
    }
    
}
