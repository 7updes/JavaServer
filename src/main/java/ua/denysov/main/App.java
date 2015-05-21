package ua.denysov.main;

import ua.denysov.logic.Main;
import ua.denysov.weather.Yahoo;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by Alex on 06.05.2015.
 */
public class App {
    public static void main(String[] args) throws IOException {
        Main main = new Main(Integer.valueOf(args[0]));
        main.execute();





    }
}
