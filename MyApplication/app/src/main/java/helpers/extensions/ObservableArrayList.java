package helpers.extensions;

import java.util.ArrayList;

import helpers.extensions.listeners.EChange;
import helpers.extensions.listeners.IArrayChangeListener;


public class ObservableArrayList<E> extends ArrayList<E> {

    private IArrayChangeListener<E> listener;

    @Override
    public boolean add(E item) {
        if (super.add(item) && this.listener != null) {
            this.listener.onChange(EChange.added, item);
            return true;
        }
        return false;
    }

    @Override
    public E remove(int index) {
        E item = super.remove(index);
        if (item != null && this.listener != null) {
            this.listener.onChange(EChange.removed, item);
        }
        return item;
    }

    @Override
    public boolean remove(Object o) {
        if (super.remove(o) && this.listener != null) {
            this.listener.onChange(EChange.removed, (E) o);
            return true;
        }
        return false;
    }

    public void addChangeListener(IArrayChangeListener<E> listener) {
        this.listener = listener;
    }

}
