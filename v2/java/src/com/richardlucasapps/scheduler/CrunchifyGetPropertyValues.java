package com.richardlucasapps.scheduler;

/**
 * Created by richard on 2/24/15.
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

/**
 * @author Crunchify.com
 *
 */

public class CrunchifyGetPropertyValues {

    private static String getPropValues() throws IOException {

        String result = "";
        Properties prop = new Properties();
        String propFileName = "version.properties";

        //InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
        InputStream inputStream = new FileInputStream("/Users/richard/Google Drive/Dev/updayte/app/version.properties");

        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
        }

        Date time = new Date(System.currentTimeMillis());

        // get the property value and print it out
        String versionCode = prop.getProperty("VERSION_CODE");

        System.out.println(versionCode);
        return result;
    }
}
