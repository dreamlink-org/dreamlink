package dreamlink.menu.component.simulation.edit;

import dreamlink.menu.component.core.BaseMenuComponent;
import dreamlink.menu.component.core.WrapperComponent;
import dreamlink.menu.component.core.box.BoxComponent;
import dreamlink.menu.component.core.box.BoxDimension;
import dreamlink.menu.component.core.span.SpanAlignment;
import dreamlink.menu.component.core.span.SpanComponent;
import dreamlink.menu.component.core.span.SpanOrientation;
import dreamlink.menu.component.simulation.edit.stamppalette.IStampPaletteComponentProvider;
import dreamlink.menu.component.simulation.edit.stamppalette.StampPaletteComponent;
import dreamlink.player.Player;
import dreamlink.zone.IStamp;

public class BlockListComponent extends WrapperComponent {

    private class InternalListComponentProvider implements IStampPaletteComponentProvider {

        @Override
        public int getTotalStampCount() {
            return Player.instance.getZone().getUserBlockCount();
        }

        @Override
        public IStamp getStampByIndex(int index) {
            return Player.instance.getZone().getUserBlockByIndex(index);
        }

    }

    private static int itemSpacing = 5;

    private BaseMenuComponent component;
    private StampPaletteComponent blockListComponent;

    public BlockListComponent() {
        this.blockListComponent = new StampPaletteComponent(new InternalListComponentProvider());
        this.component = new SpanComponent(SpanOrientation.vertical, SpanAlignment.start, BlockListComponent.itemSpacing)
            .addComponent(
                new BoxComponent(
                    this.blockListComponent,
                    BoxDimension.grow(), 
                    BoxDimension.grow()
                )
            );
    }

    @Override
    public BaseMenuComponent getComponent() {
        return this.component;
    }
    
}
