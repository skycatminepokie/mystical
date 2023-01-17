package skycat.mystical.spell;

import skycat.mystical.curses.CurseHandler;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import static skycat.mystical.Mystical.GSON;

public class SpellHandler {
    private static final File SAVE_FILE = new File("config/spellHandler.json");
    private final ArrayList<Spell> activeSpells = new ArrayList<>();

    public static CurseHandler loadOrNew() {
        try (Scanner scanner = new Scanner(SAVE_FILE)) {
            return GSON.fromJson(scanner.nextLine(), CurseHandler.class);
        } catch (IOException e) {
            // TODO: Logging
            return new CurseHandler();
        }
    }

    public void save() {
        try (PrintWriter pw = new PrintWriter(SAVE_FILE)) {
            pw.println(GSON.toJson(this));
        } catch (IOException e) {
            // TODO: Logging
        }
    }

    public void activateNewSpell() {
        // TODO
    }


}
