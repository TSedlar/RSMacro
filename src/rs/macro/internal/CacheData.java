package rs.macro.internal;

import rs.macro.util.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Tyler Sedlar
 * @since 10/21/15
 */
public class CacheData {

    private static String user;
    private static String pass;
    private static String pin;

    private static int world;

    protected static String user() {
        return user;
    }

    protected static String pass() {
        return pass;
    }

    public static String pin() {
        return pin;
    }

    public static int world() {
        return world;
    }

    public static void parseLogin() {
        List<String> list = new ArrayList<>(2);
        try (Stream<String> stream = Files.lines(Paths.get(Configuration.CACHE +
                "login.ini"))) {
            stream.forEachOrdered(list::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
        user = list.get(0);
        pass = list.get(1);
        pin = list.get(2);
    }

    public static void parseWorld() {
        List<String> list = new ArrayList<>(1);
        try (Stream<String> stream = Files.lines(Paths.get(Configuration.CACHE +
                "world.ini"))) {
            stream.forEachOrdered(list::add);
        } catch (IOException ignored) {
            list.add(0, "12");
        }
        world = Integer.parseInt(list.get(0));
    }
}
