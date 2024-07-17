package saveload;
import core.World;
import utils.FileUtils;

public class SaveLoad {

    /** method writes into a file. in the case that one doesn't exist, write a new one to write into **/
    public static void processFile(String fileName, String content) {

        // if the file doesn't exist yet, write a new one with basic information
        if (!FileUtils.fileExists(fileName)) {
            FileUtils.writeFile(fileName, content);
        } else {
            FileUtils.writeFile(fileName, content);
        }
    }

    public static void saveWorldToFile(World world, String fileName) {
        System.out.println("world saving");

        // obtain all the relevant save information
        String currSeed = String.valueOf(world.getSeed());

        System.out.println("currSeed: " + currSeed);
        System.out.println("world.getSeed(): " + world.getSeed());


        String currInputs = world.getAvatar().getInputs();
        boolean sightToggle = world.lineOfSightActivated();
        boolean lightToggle = world.getAvatar().isLightsOn();

        String save = currSeed + " " + currInputs + " " + sightToggle + " " + lightToggle;

        FileUtils.writeFile(fileName, save);
    }

    // Additional method to load world progress
    public static World loadWorldFromFile(String fileName) {
        String save = FileUtils.readFile(fileName);
        System.out.println("world loading");
        String[] saveInfo = save.split(" ");

        Long seed = Long.valueOf(saveInfo[0]);
        String inputs = (saveInfo[1]);
        boolean sightToggle = Boolean.parseBoolean((saveInfo[2]));
        boolean lightToggle = Boolean.parseBoolean((saveInfo[3]));

        World load = new World(seed);

        // iterate through each input and update the avatar accordingly
        char[] inputSplit = inputs.toCharArray();
        for (char userInput : inputSplit) {
            load.getAvatar().handleInputManual(userInput);
        }

        // begin the world based off the most recent toggle settings
        load.getAvatar().setLineOfSightActive(sightToggle);
        load.getAvatar().setLightsOn(lightToggle);

        return load;
    }
}
