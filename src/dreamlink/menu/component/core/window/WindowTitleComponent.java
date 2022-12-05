package dreamlink.menu.component.core.window;

import org.joml.Vector2i;

import dreamlink.graphics.sprite.template.ISpriteTemplate;
import dreamlink.graphics.sprite.template.SolidSpriteTemplate;
import dreamlink.graphics.text.FontDecoration;
import dreamlink.menu.component.core.BaseMenuComponent;
import dreamlink.menu.component.core.PaddingComponent;
import dreamlink.menu.component.core.WrapperComponent;
import dreamlink.menu.component.core.background.BackgroundComponent;
import dreamlink.menu.component.core.background.StaticBackgroundComponentProvider;
import dreamlink.menu.component.core.box.BoxAlignment;
import dreamlink.menu.component.core.box.BoxComponent;
import dreamlink.menu.component.core.box.BoxDimension;
import dreamlink.menu.component.core.icon.IconComponent;
import dreamlink.menu.component.core.icon.StaticIconComponentProvider;
import dreamlink.menu.component.core.span.SpanAlignment;
import dreamlink.menu.component.core.span.SpanComponent;
import dreamlink.menu.component.core.span.SpanOrientation;
import dreamlink.menu.component.core.text.line.TextLineLabelComponent;
import dreamlink.utility.maths.Vector4fMaths;

public class WindowTitleComponent extends WrapperComponent {

    private static int titleHorizontalPadding = 5;
    private static int titleVerticalPadding = 2;
    private static int titleSpacing = 5;
    private static Vector2i iconDimensions = new Vector2i(16);

    private BaseMenuComponent component;

    public WindowTitleComponent(ISpriteTemplate icon, String title) {
        this.component = new BackgroundComponent(
            new StaticBackgroundComponentProvider(SolidSpriteTemplate.overlayHighlight),
            new BoxComponent(
                new PaddingComponent(
                    new SpanComponent(SpanOrientation.horizontal, SpanAlignment.start, WindowTitleComponent.titleSpacing)
                        .addComponent(
                            new IconComponent(
                                new StaticIconComponentProvider(icon),
                                WindowTitleComponent.iconDimensions
                            )
                        )
                        .addComponent(
                            new TextLineLabelComponent(
                                title, 
                                FontDecoration.underline,
                                Vector4fMaths.white
                            )
                        ),
                    WindowTitleComponent.titleVerticalPadding,
                    WindowTitleComponent.titleVerticalPadding,
                    WindowTitleComponent.titleHorizontalPadding,
                    WindowTitleComponent.titleHorizontalPadding
                ),
                BoxDimension.grow(BoxAlignment.start),
                BoxDimension.wrap()
            )
        );
    }

    @Override
    public BaseMenuComponent getComponent() {
        return this.component;
    }
    
}
