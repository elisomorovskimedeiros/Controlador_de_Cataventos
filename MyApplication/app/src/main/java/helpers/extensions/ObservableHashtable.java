package helpers.extensions;

import java.util.Hashtable;

import helpers.extensions.listeners.EChange;
import helpers.extensions.listeners.IKeyValueChangeListener;

public class ObservableHashtable<K, V> extends Hashtable<K, V> {

    private IKeyValueChangeListener<K, V> listener;

    @Override
    public V put(K key, V value) {
        if (super.put(key, value) != null && this.listener != null) {
            this.listener.onChange(EChange.added, key, value);
            return value;
        }
        return null;
    }

    @Override
    public V remove(Object key) {
        V item = super.remove(key);
        if (item != null && this.listener != null) {
            this.listener.onChange(EChange.removed, (K) key, item);
        }
        return item;
    }

    public void addChangeListener(IKeyValueChangeListener<K, V> listener) {
        this.listener = listener;
    }

}
