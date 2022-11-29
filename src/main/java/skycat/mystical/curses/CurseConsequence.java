package skycat.mystical.curses;

public class CurseConsequence<T> {
    T callback;
    Class<?> callbackType;

    public CurseConsequence(T callback, Class<?> callbackType) {
        this.callback = callback;
        this.callbackType = callbackType;
    }
}
