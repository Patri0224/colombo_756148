package db;

import java.io.*;
import java.io.IOException;
import java.util.Properties;

public class DBConfigLoader {
    protected String dbUrl;
    protected String username;
    protected String password;

    protected DBConfigLoader() throws IOException{
    }

    protected void loadDBConfig() throws IOException {
        InputStream in = getClass().getResourceAsStream("/db.properties");
        Properties properties = new Properties();
        properties.load(in);
        dbUrl = properties.getProperty("db.url");
        username = properties.getProperty("db.username");
        password = properties.getProperty("db.password");
    }
}
