package Lab10;

import org.apache.log4j.Logger;

/**
 * @author Banit Maxim
 */

public class Main {
    private static final Logger log = Logger.getLogger("Main.class");
    public static void main(String[] args) {
        log.info("Запуск приложения");
        Action action = new Action();
        action.show();
    }
}
