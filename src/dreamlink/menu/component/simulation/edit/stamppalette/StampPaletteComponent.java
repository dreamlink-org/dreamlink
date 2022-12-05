package dreamlink.menu.component.simulation.edit.stamppalette;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2i;

import dreamlink.graphics.sprite.SpriteBatch;
import dreamlink.graphics.sprite.SpriteHeight;
import dreamlink.graphics.sprite.template.ISpriteTemplate;
import dreamlink.graphics.sprite.template.SolidSpriteTemplate;
import dreamlink.graphics.text.FontDecoration;
import dreamlink.menu.component.core.BaseMenuComponent;
import dreamlink.menu.component.core.EmptyComponent;
import dreamlink.menu.component.core.PaddingComponent;
import dreamlink.menu.component.core.WrapperComponent;
import dreamlink.menu.component.core.border.dialog.DialogBorderComponent;
import dreamlink.menu.component.core.border.dialog.DialogState;
import dreamlink.menu.component.core.border.dialog.StaticDialogBorderComponentProvider;
import dreamlink.menu.component.core.box.BoxComponent;
import dreamlink.menu.component.core.box.BoxDimension;
import dreamlink.menu.component.core.icon.IconComponent;
import dreamlink.menu.component.core.scroll.IScrollBarComponentProvider;
import dreamlink.menu.component.core.scroll.ScrollBarComponent;
import dreamlink.menu.component.core.span.SpanAlignment;
import dreamlink.menu.component.core.span.SpanComponent;
import dreamlink.menu.component.core.span.SpanOrientation;
import dreamlink.menu.component.core.text.TextCharacterState;
import dreamlink.menu.component.core.text.line.ITextLineComponentProvider;
import dreamlink.menu.component.core.text.line.TextLineComponent;
import dreamlink.menu.event.DragStartEvent;
import dreamlink.menu.event.HoverEndEvent;
import dreamlink.menu.event.HoverEvent;
import dreamlink.menu.event.IEvent;
import dreamlink.menu.event.PressStartEvent;
import dreamlink.utility.maths.IntMaths;
import dreamlink.utility.maths.Vector4fMaths;
import dreamlink.window.Window;
import dreamlink.zone.IStamp;

public class StampPaletteComponent extends WrapperComponent {

    private class InternalScrollBarComponentProvider implements IScrollBarComponentProvider {

        @Override
        public int getTotalItemCount() {
            return StampPaletteComponent.this.provider.getTotalStampCount();
        }

        @Override
        public int getVisibleItemCount() {
            return StampPaletteComponent.this.stampListComponent.visibleItemCount;
        }

    }

    private class InternalStampRowComponent extends WrapperComponent implements ITextLineComponentProvider {

        private static int rowHeight = 18;
        private static int minPadding = 1;
        private static Vector2i iconDimensions = new Vector2i(16);
        private static int itemSpacing = 5;

        private BoxComponent component;
        private IStamp stamp;
        private boolean isHighlighted;

        public InternalStampRowComponent() {
            this.component = new BoxComponent(
                new PaddingComponent(
                    new SpanComponent(SpanOrientation.horizontal, SpanAlignment.start, InternalStampRowComponent.itemSpacing)
                        .addComponent(
                            new IconComponent(
                                this::getSpriteTemplate,
                                InternalStampRowComponent.iconDimensions
                            )
                        )
                        .addComponent(new TextLineComponent(this)),
                    InternalStampRowComponent.minPadding
                ),
                BoxDimension.grow(),
                BoxDimension.fixed(InternalStampRowComponent.rowHeight)
            );
        }

        @Override
        public int getCharacterCount() {
            return this.stamp.getStampName().length();
        }

        @Override
        public void setCharacterState(int characterIndex, TextCharacterState state) {
            var stampName = this.stamp.getStampName();
            if(characterIndex >= stampName.length()) {
                state.clear();
                return;
            }

            state.set(
                stampName.charAt(characterIndex),
                FontDecoration.normal,
                this.isHighlighted ? Vector4fMaths.white : Vector4fMaths.black
            );
        }

        private ISpriteTemplate getSpriteTemplate() {
            return this.stamp.getStampSprite();
        }

        @Override
        public void writeToSpriteBatch(SpriteBatch spriteBatch) {
            if(this.isHighlighted) {
                SolidSpriteTemplate.overlayHighlight.writeToSpriteBatch(
                    spriteBatch, 
                    this.component.getPosition(), 
                    this.component.getDimensions(), 
                    SpriteHeight.menu
                );
            }
            super.writeToSpriteBatch(spriteBatch);
        }

        @Override
        public BaseMenuComponent getComponent() {
            return this.component;
        }
    }

    private class InternalStampListComponent extends WrapperComponent {

        private static int dragDistanceThreshold = 25;
        private static Vector2i dragSpriteDimensions = new Vector2i(32);

        private BoxComponent component;
        private int visibleItemCount; 
        private int hoveredIndex;
        private IStamp draggedStamp;
        private List<InternalStampRowComponent> stampRowComponents;

        public InternalStampListComponent() {
            this.hoveredIndex = -1;
            this.stampRowComponents = new ArrayList<>();
            this.component = new BoxComponent(
                new EmptyComponent(),
                BoxDimension.grow(),
                BoxDimension.grow()
            );
        }

        @Override
        public int getDragDistanceThreshold() {
            return InternalStampListComponent.dragDistanceThreshold;
        }

        @Override
        public void onEvent(IEvent event) {
            super.onEvent(event);

            if(event instanceof HoverEvent) {
                var mousePosition = Window.instance.getMousePosition();
                var deltaHeight = mousePosition.y - this.component.getPosition().y;
                var deltaOffset = deltaHeight / InternalStampRowComponent.rowHeight;
                var hoveredIndex = deltaOffset + StampPaletteComponent.this.scrollBarComponent.getScrollIndex();
                this.hoveredIndex = IntMaths.clamp(hoveredIndex, 0, StampPaletteComponent.this.provider.getTotalStampCount() - 1);
            } else if(event instanceof HoverEndEvent) {
                this.hoveredIndex = -1;
            } else if(event instanceof PressStartEvent) {
                var stamp = StampPaletteComponent.this.provider.getStampByIndex(this.hoveredIndex);
                this.draggedStamp = stamp;
            } else if(event instanceof DragStartEvent dragStartEvent) {
                dragStartEvent.payload = this.draggedStamp;
            }
        }

        @Override
        public BaseMenuComponent getHoveredComponent() {
            return this.isMouseOver() ? this : null;
        }

        @Override
        public void computeDimensions(Vector2i availableSpace) {
            super.computeDimensions(availableSpace);
            var innerDimensions = new Vector2i(this.component.getDimensions());
            this.visibleItemCount = innerDimensions.y / InternalStampRowComponent.rowHeight;
            innerDimensions.y = this.visibleItemCount * InternalStampRowComponent.rowHeight;
            super.computeDimensions(innerDimensions);
        }

        @Override
        public void finalizeLayout(BaseMenuComponent parent, Vector2i position) {
            super.finalizeLayout(parent, position);

            var positionCursor = new Vector2i(this.component.getPosition());
            var rowDimensions = new Vector2i(this.component.getDimensions().x, InternalStampRowComponent.rowHeight);
            for(var ix = 0; ix < this.visibleItemCount; ix += 1) {
                var stampRowComponent = new InternalStampRowComponent();
                stampRowComponent.computeInitialDimensions();
                stampRowComponent.computeDimensions(rowDimensions);
                stampRowComponent.finalizeLayout(this, positionCursor);
                positionCursor.y += InternalStampRowComponent.rowHeight;
                this.stampRowComponents.add(stampRowComponent);
            }
        }

        @Override
        public BaseMenuComponent getComponent() {
            return this.component;
        }

        @Override
        public void writeToSpriteBatch(SpriteBatch spriteBatch) {
            super.writeToSpriteBatch(spriteBatch);
            var stampCount = StampPaletteComponent.this.provider.getTotalStampCount();
            for(var ix = 0; ix < this.visibleItemCount; ix += 1) {
                var offsetIndex = ix + StampPaletteComponent.this.scrollBarComponent.getScrollIndex();
                if(offsetIndex >= stampCount) {
                    break;
                }

                var rowComponent = this.stampRowComponents.get(ix);
                rowComponent.stamp = StampPaletteComponent.this.provider.getStampByIndex(offsetIndex);
                rowComponent.isHighlighted = offsetIndex == this.hoveredIndex;
                rowComponent.writeToSpriteBatch(spriteBatch);
            }

            if(this.isDragged() && this.draggedStamp != null) {
                var mousePosition = Window.instance.getMousePosition();
                var spritePosition = new Vector2i(
                    mousePosition.x - InternalStampListComponent.dragSpriteDimensions.x / 2,
                    mousePosition.y - InternalStampListComponent.dragSpriteDimensions.y / 2
                );

                this.draggedStamp.getStampSprite().writeToSpriteBatch(
                    spriteBatch,
                    spritePosition,
                    InternalStampListComponent.dragSpriteDimensions,
                    SpriteHeight.cursorPayload
                );
                
            }
        }
    }

    private DialogBorderComponent component;
    private ScrollBarComponent scrollBarComponent;
    private IStampPaletteComponentProvider provider;
    private InternalStampListComponent stampListComponent;

    @Override
    public BaseMenuComponent getComponent() {
        return this.component;
    }

    public StampPaletteComponent(IStampPaletteComponentProvider provider) {
        this.provider = provider;

        this.scrollBarComponent = new ScrollBarComponent(
            new InternalScrollBarComponentProvider()
        );

        this.stampListComponent = new InternalStampListComponent();

        this.component = new DialogBorderComponent(
            new StaticDialogBorderComponentProvider(DialogState.focused),
                new SpanComponent(SpanOrientation.horizontal, SpanAlignment.start, 0)
                    .addComponent(this.stampListComponent)
                    .addComponent(this.scrollBarComponent)
            );
    }


}
