package helpers.extensions.listeners;

public interface IKeyValueChangeListener<K, V> {
    void onChange(EChange action, K changedKey, V changedValue);
}
