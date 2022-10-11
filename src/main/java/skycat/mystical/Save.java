package skycat.mystical;

import java.io.*;

import static skycat.mystical.MysticalServer.GSON;
import static skycat.mystical.MysticalServer.LOGGER;

public class Save {
    Settings settings = new Settings();
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


}
