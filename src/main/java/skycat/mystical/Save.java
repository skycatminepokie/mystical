package skycat.mystical;

import lombok.Getter;
import skycat.mystical.curses.CurseHandler;

import java.io.*;

import static skycat.mystical.MysticalServer.GSON;
import static skycat.mystical.MysticalServer.LOGGER;

/**
 * The main save file for Mystical. Includes information related to the game state, as well as a {@link Settings}.
 */
public class Save {
    Settings settings = new Settings(true); // Make a new one if it isn't loaded by GSON
    @Getter CurseHandler curseHandler = new CurseHandler();
    public static final File SAVE_FILE = new File("mysticalSave.json");

    public static Save load() {
        try (FileReader fileReader = new FileReader(SAVE_FILE)) {
            return GSON.fromJson(fileReader, Save.class);
        } catch (FileNotFoundException e) {
            LOGGER.info("No Mystical save file found, creating a new one.");
            return new Save();
        } catch (IOException e) {
            LOGGER.error("IOException thrown while trying to load Mystical save file. Throwing error.");
            throw new RuntimeException(e);
        }
    }

    public void save(File saveFile) throws IOException {
        if (!saveFile.exists()) {
            saveFile.createNewFile();
            Utils.log("Created new save file for Mystical.", Settings.LoggingSettings.getNewSaveFileCreated());
        }
        try (PrintWriter printWriter = new PrintWriter(saveFile)) {
            GSON.toJson(this, printWriter);
            Utils.log("Mystical saved.", Settings.LoggingSettings.getSaving());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public void save(String filePath) throws IOException {
        save(new File(filePath));
    }

    public void save() throws IOException {
        save(SAVE_FILE);
    }


}
