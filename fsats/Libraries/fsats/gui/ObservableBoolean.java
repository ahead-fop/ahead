package fsats.gui;

import java.util.*;

class ObservableBoolean
    extends Observable
{
    private boolean value; 

    public ObservableBoolean() { this.value=false; }
    public ObservableBoolean(boolean value) { this.value=value; }

    public void set(boolean value)
    {
        this.value=value;
        setChanged();
        notifyObservers();
    }

    public boolean isSet() { return this.value; }
}

