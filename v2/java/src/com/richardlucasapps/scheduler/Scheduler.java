package com.richardlucasapps.scheduler;

import com.google.play.developerapi.samples.UploadApkWithListing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by richard on 2/20/15.
 */
public class Scheduler {

    private static int SECONDS_IN_A_DAY = 86400;

    public static void main(String[] args){

        ZonedDateTime utcNow = ZonedDateTime.now(ZoneId.of("Z"));
        ZonedDateTime utcStart = utcNow.withHour(0).withMinute(0).withSecond(0);

        utcStart = utcStart.plusDays(1);

        Duration duration = Duration.between(utcNow, utcStart);
        long initialDelay = duration.getSeconds();


        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new MyRunnableTask(), 1,
                5, TimeUnit.SECONDS);

    }

    private static class MyRunnableTask implements Runnable {
        @Override
        public void run() {
            System.out.println("running");
            String properties;
            try {
                properties = getPropValues();
            } catch (IOException e) {
                e.printStackTrace();
            }

            UploadApkWithListing.main(null);

        }
    }

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
