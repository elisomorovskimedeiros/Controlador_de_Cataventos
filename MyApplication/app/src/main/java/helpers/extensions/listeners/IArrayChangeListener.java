package helpers.extensions.listeners;

public interface IArrayChangeListener<E> {
    void onChange(EChange action, E changed);
}
