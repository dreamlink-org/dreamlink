package dreamlink.menu.component.core.window;

import org.joml.Vector2i;

import dreamlink.graphics.sprite.template.ISpriteTemplate;
import dreamlink.menu.component.core.BaseMenuComponent;
import dreamlink.menu.component.core.WrapperComponent;
import dreamlink.menu.component.core.border.WindowBorderComponent;
import dreamlink.menu.component.core.span.SpanAlignment;
import dreamlink.menu.component.core.span.SpanComponent;
import dreamlink.menu.component.core.span.SpanOrientation;
import dreamlink.window.Window;

public class WindowComponent extends WrapperComponent {

    private static int spacing = 3;

    private BaseMenuComponent component;

    public WindowComponent(
        ISpriteTemplate icon, 
        String title, 
        BaseMenuComponent content
    ) {
        this.component = new WindowBorderComponent(
            new SpanComponent(SpanOrientation.vertical, SpanAlignment.start, WindowComponent.spacing)
                .addComponent(new WindowTitleComponent(icon, title))
                .addComponent(content)
        );
    }

    @Override
    public BaseMenuComponent getComponent() {
        return this.component;
    }

    @Override
    public void computeDimensions(Vector2i availableSpace) {
        super.computeDimensions(this.getInitialDimensions());
    }

    @Override
    public void finalizeLayout(BaseMenuComponent parent, Vector2i position) {
        var resolution = Window.instance.getResolution();
        var dimensions = this.getDimensions();
        var windowPosition = new Vector2i(
            (resolution.x - dimensions.x) / 2,
            (resolution.y - dimensions.y) / 2
        );
        super.finalizeLayout(parent, windowPosition);
    }
}