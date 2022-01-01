package renderengine.gui.font;

import org.joml.Vector2i;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class needs work!
 *
 * @author Mirco Werner
 */
public class Character {
    private static boolean initialized = false;
    public static final Map<java.lang.Character, Character> characters = new HashMap<>();

    public static void initialize() throws IOException {
        if (initialized) {
            return;
        }
        initialized = true;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Character.class.getClassLoader().getResourceAsStream("fonts/arial.fnt"))));
        String line;
        if (bufferedReader.skip(4) < 4) {
            return;
        }
        while ((line = bufferedReader.readLine()) != null) {
            if (!line.startsWith("char id=")) {
                continue;
            }
            String[] split = line.split("\\s+");
            if (split.length != 11) {
                return;
            }

            try {
                char id = (char) Short.parseShort(split[1].split("=")[1]);
                int x = Integer.parseInt(split[2].split("=")[1]);
                int y = Integer.parseInt(split[3].split("=")[1]);
                int width = Integer.parseInt(split[4].split("=")[1]);
                int height = Integer.parseInt(split[5].split("=")[1]);
                int xOffset = Integer.parseInt(split[6].split("=")[1]);
                int yOffset = Integer.parseInt(split[7].split("=")[1]);
                int xAdvance = Integer.parseInt(split[8].split("=")[1]);
                characters.put(id, new Character(id, new Vector2i(x, y), new Vector2i(width, height), new Vector2i(xOffset, yOffset), xAdvance));
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        bufferedReader.close();
    }

    private final char character;

    public final Vector2i charPosition;
    public final Vector2i charDimension;
    public final Vector2i charOffset;
    public final int charXAdvance;

    public Character(char character, Vector2i charPosition, Vector2i charDimension, Vector2i charOffset, int charXAdvance) {
        this.character = character;
        this.charPosition = charPosition;
        this.charDimension = charDimension;
        this.charOffset = charOffset;
        this.charXAdvance = charXAdvance;
    }
}
