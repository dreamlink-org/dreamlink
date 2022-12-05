package dreamlink.menu.component.core.scroll;

import dreamlink.window.Window;

import org.joml.Vector2i;

import dreamlink.graphics.sprite.SpriteBatch;
import dreamlink.graphics.sprite.SpriteHeight;
import dreamlink.graphics.sprite.template.BorderSpriteTemplate;
import dreamlink.graphics.sprite.template.SolidSpriteTemplate;
import dreamlink.menu.component.core.BaseMenuComponent;
import dreamlink.menu.component.core.EmptyComponent;
import dreamlink.menu.component.core.WrapperComponent;
import dreamlink.menu.component.core.background.BackgroundComponent;
import dreamlink.menu.component.core.background.StaticBackgroundComponentProvider;
import dreamlink.menu.component.core.box.BoxComponent;
import dreamlink.menu.component.core.box.BoxDimension;
import dreamlink.menu.event.DragEvent;
import dreamlink.menu.event.IEvent;
import dreamlink.menu.event.PressStartEvent;

public class ScrollBarComponent extends WrapperComponent {

    private static int scrollBarWidth = 16;
    
    private int scrollIndex;
    private int startScrollIndex;

    private Vector2i startMousePosition;
    private IScrollBarComponentProvider provider;
    private BaseMenuComponent component;

    public ScrollBarComponent(IScrollBarComponentProvider provider) {
        this.startMousePosition = new Vector2i();
        this.provider = provider;
        this.component = new BackgroundComponent(
            new StaticBackgroundComponentProvider(SolidSpriteTemplate.overlayWindow),
            new BoxComponent(
                new EmptyComponent(),
                BoxDimension.fixed(scrollBarWidth),
                BoxDimension.grow()
            )
        );
    }

    public void setScrollIndex(int scrollIndex) {
        this.scrollIndex = scrollIndex;
    }

    // It is necessary to clamp the scroll index as the total number of items could change
    // such that the scroll index exceeds it.
    public int getScrollIndex() {
        var maxIndex = this.provider.getTotalItemCount() - this.provider.getVisibleItemCount();
        return Math.max(0, Math.min(this.scrollIndex, maxIndex));
    }

    private boolean isDisabled() {
        return this.provider.getTotalItemCount() <= this.provider.getVisibleItemCount();
    }

    public void scrollToBottom() {
        this.scrollIndex = this.provider.getTotalItemCount() - this.provider.getVisibleItemCount();
    }

    @Override
    public int getDragDistanceThreshold() {
        return 0;
    }

    @Override
    public BaseMenuComponent getHoveredComponent() {
        if(this.isDisabled() || !this.isMouseOver()) {
            return null;
        }
        return this;
    }

    @Override
    public BaseMenuComponent getComponent() {
        return this.component;
    }

    @Override
    public void onEvent(IEvent event) {
        super.onEvent(event);
        var scrollIndex = this.getScrollIndex();
        var maxIndex = this.provider.getTotalItemCount() - this.provider.getVisibleItemCount();
        var scrollFactor = (float)scrollIndex / maxIndex;
        var scrollHandleSizeFactor = (float)this.provider.getVisibleItemCount() / this.provider.getTotalItemCount();
        var scrollHandleHeight = (int)(this.getDimensions().y * Math.min(Math.max(scrollHandleSizeFactor, 0.1f), 1.0f));
        var scrollRange = this.getDimensions().y - scrollHandleHeight;
        var scrollHandleStart = (int)(scrollRange * scrollFactor) + this.getPosition().y;
        var scrollHandleEnd = scrollHandleStart + scrollHandleHeight;

        var mousePosition = Window.instance.getMousePosition();
        if(event instanceof PressStartEvent) {
            this.startScrollIndex = scrollIndex;
            if(mousePosition.y < scrollHandleStart || mousePosition.y > scrollHandleEnd) {
                this.startMousePosition.set(mousePosition.x, scrollHandleStart + scrollHandleHeight / 2);
            } else {
                this.startMousePosition.set(mousePosition);
            }
        }

        if(event instanceof PressStartEvent || event instanceof DragEvent) {
            var mouseDelta = mousePosition.y - this.startMousePosition.y;
            var numOptions = this.provider.getTotalItemCount();
            var incrementalScrollFactor = (float)mouseDelta / this.getDimensions().y;
            var incrementalScrollIndex = (int)(incrementalScrollFactor * (numOptions - 1));
            this.scrollIndex = Math.max(Math.min(this.startScrollIndex + incrementalScrollIndex, maxIndex), 0);
        }
    }

    @Override
    public void writeToSpriteBatch(SpriteBatch spriteBatch) {
        super.writeToSpriteBatch(spriteBatch);

        if(this.isDisabled()) {
            BorderSpriteTemplate.buttonDisabled.writeToSpriteBatch(
                spriteBatch,
                this.getPosition(),
                this.getDimensions(),
                SpriteHeight.menu
            );
        } else {
            var maxIndex = this.provider.getTotalItemCount() - this.provider.getVisibleItemCount();
            var scrollFactor = (float)this.getScrollIndex() / maxIndex;
            var scrollHandleSizeFactor = (float)this.provider.getVisibleItemCount() / this.provider.getTotalItemCount();
            var scrollHandleHeight = (int)(this.getDimensions().y * Math.min(Math.max(scrollHandleSizeFactor, 0.1f), 1.0f));
            var scrollRange = this.getDimensions().y - scrollHandleHeight;
            var scrollHandleStart = (int)(scrollRange * scrollFactor) + this.getPosition().y;
            BorderSpriteTemplate.button.writeToSpriteBatch(
                spriteBatch,
                new Vector2i(this.getPosition().x, scrollHandleStart),
                new Vector2i(ScrollBarComponent.scrollBarWidth, scrollHandleHeight),
                SpriteHeight.menu
            );
        }
    }
}
