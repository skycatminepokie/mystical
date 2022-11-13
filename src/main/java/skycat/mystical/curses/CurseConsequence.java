package skycat.mystical.curses;

public class CurseConsequence<T> {
    T callback;

    public CurseConsequence(T callback) {
        this.callback = callback;
    }
}
