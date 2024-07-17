import core.AutograderBuddy;
import core.World;
import edu.princeton.cs.algs4.StdDraw;
import org.junit.jupiter.api.Test;
import tileengine.TERenderer;
import tileengine.TETile;

import static com.google.common.truth.Truth.assertThat;
import static core.AutograderBuddy.inputString;

public class WorldGenTests {
    @Test
    public void basicTest() {
        // put different seeds here to test different worlds
        TETile[][] tiles = AutograderBuddy.getWorldFromInput("n1234567890123456789s");
        World world = new World(1234567890123456789L);

        TERenderer ter = new TERenderer();
        ter.initialize(tiles.length, tiles[0].length);
        ter.renderFrame(world);
        StdDraw.pause(500); // pause for 5 seconds so you can see the output
    }

    @Test
    public void basicTest2() {
        TETile[][] tiles = AutograderBuddy.getWorldFromInput("n472972s");
        World world = new World(472972L);

        TERenderer ter = new TERenderer();
        ter.initialize(tiles.length, tiles[0].length);
        ter.renderFrame(world);
        StdDraw.pause(500); // pause for 5 seconds so you can see the output
    }

    @Test
    public void basicTest3() {
        // put different seeds here to test different worlds
        TETile[][] tiles = AutograderBuddy.getWorldFromInput("n759234914293s");
        World world = new World(759234914293L);

        TERenderer ter = new TERenderer();
        ter.initialize(tiles.length, tiles[0].length);
        ter.renderFrame(world);
        StdDraw.pause(500); // pause for 5 seconds so you can see the output
    }

    @Test
    public void basicTest4() {
        // put different seeds here to test different worlds
        TETile[][] tiles = AutograderBuddy.getWorldFromInput("n927502420268305s");
        World world = new World(927502420268305L);

        TERenderer ter = new TERenderer();
        ter.initialize(tiles.length, tiles[0].length);
        ter.renderFrame(world);
        StdDraw.pause(500); // pause for 5 seconds so you can see the output
    }

    @Test
    public void basicTest5() {
        // put different seeds here to test different worlds
        TETile[][] tiles = AutograderBuddy.getWorldFromInput("n999999999999s");
        World world = new World(999999999999L);

        TERenderer ter = new TERenderer();
        ter.initialize(tiles.length, tiles[0].length);
        ter.renderFrame(world);
        StdDraw.pause(500); // pause for 5 seconds so you can see the output
    }

    @Test
    public void basicTest6() {
        // put different seeds here to test different worlds
        TETile[][] tiles = AutograderBuddy.getWorldFromInput("n000000s");
        World world = new World(000000L);

        TERenderer ter = new TERenderer();
        ter.initialize(tiles.length, tiles[0].length);
        ter.renderFrame(world);
        StdDraw.pause(500); // pause for 5 seconds so you can see the output
    }

    @Test
    public void basicTest7() {
        // put different seeds here to test different worlds
        TETile[][] tiles = AutograderBuddy.getWorldFromInput("n0s");
        World world = new World(0L);

        TERenderer ter = new TERenderer();
        ter.initialize(tiles.length, tiles[0].length);
        ter.renderFrame(world);
        StdDraw.pause(500); // pause for 5 seconds so you can see the output
    }


    @Test
    public void input_string_seed() {
        assertThat(inputString("n30842038s")).isEqualTo(30842038);
        assertThat(inputString("n123456789s")).isEqualTo(123456789);
        assertThat(inputString("n12345s")).isEqualTo(12345);
        assertThat(inputString("n54321s")).isEqualTo(54321);
    }

//    @Test
//    public void basicInteractivityTest() {
//        // TODO: write a test that uses an input like "n123swasdwasd"
//        TETile[][] tiles = AutograderBuddy.getWorldFromInput("n1234567890123456789s");
//
//        TERenderer ter = new TERenderer();
//        ter.initialize(tiles.length, tiles[0].length);
//        ter.renderFrame(tiles);
//        StdDraw.pause(500); // pause for 5 seconds so you can see the output
//    }
//
//    @Test
//    public void basicSaveTest() {
//        // TODO: write a test that calls getWorldFromInput twice, with "n123swasd:q" and with "lwasd"
//    }
}
