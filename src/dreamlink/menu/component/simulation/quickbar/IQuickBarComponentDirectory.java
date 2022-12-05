package dreamlink.menu.component.simulation.quickbar;

import dreamlink.zone.IStamp;

public interface IQuickBarComponentDirectory {

    public int getSelectedSlotID();

    public void setSelectedSlotID(int slotID);

    public IStamp getStamp(int slotID);

    public void setStamp(int slotID, IStamp stamp);
    
}
