package com.richardlucasapps.scheduler;

import com.google.play.developerapi.samples.UploadApkWithListing;

import java.io.*;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by richard on 2/20/15.
 */
public class Scheduler {

    private static int SECONDS_TO_REPEAT = 86400; //86400 is the number of seconds in one day

    public static void main(String[] args){

        ZonedDateTime utcNow = ZonedDateTime.now(ZoneId.of("Z"));

        ZonedDateTime utcStartUploadAPK = utcNow.withHour(0).withMinute(0).withSecond(0);
        utcStartUploadAPK = utcStartUploadAPK.plusDays(1);
        ZonedDateTime utcStartBuildAPK = utcNow.withHour(12).withMinute(0).withSecond(0);
        utcStartBuildAPK = utcStartBuildAPK.plusDays(1);

        Duration durationUploadAPK = Duration.between(utcNow, utcStartUploadAPK);
        long uploadApkInitialDelay = durationUploadAPK.getSeconds();

        Duration duration = Duration.between(utcNow, utcStartBuildAPK);
        long buildApkInitialDelay = duration.getSeconds();

        System.out.println("UploadApk Initial Delay: " + String.valueOf(uploadApkInitialDelay));
        System.out.println("BuildApk Initial Delay: " + String.valueOf(buildApkInitialDelay));

        new BuildApk().run(); //calling run directly will not start new thread, do this so we know we have a fresh apk
                              //ready for upload.

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new UploadApk(), uploadApkInitialDelay,
                SECONDS_TO_REPEAT, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(new BuildApk(), buildApkInitialDelay,
                SECONDS_TO_REPEAT, TimeUnit.SECONDS);
    }

    private static class UploadApk implements Runnable {
        @Override
        public void run() {
            ZonedDateTime utcNow = ZonedDateTime.now(ZoneId.of("Z"));
            System.out.println("Uploading APK, Current Time: " + utcNow.toString());
            String[] array = new String[]{utcNow.toString()};
            UploadApkWithListing.main(array);
        }
    }

    private static class BuildApk implements Runnable {
        @Override
        public void run() {
            ZonedDateTime utcNow = ZonedDateTime.now(ZoneId.of("Z"));
            System.out.println("Building APK, Current Time: " + utcNow.toString());

            String[] cmdAndArgs = {"cmd", "/c","build.bat"};
            File dir = new File("C:/Users/Richard/Desktop/");

            ProcessBuilder pb = new ProcessBuilder(cmdAndArgs);
            pb.directory(dir);
            try {
                Process p = pb.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
    }


}
