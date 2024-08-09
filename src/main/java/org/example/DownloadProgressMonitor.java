package org.example;

public class DownloadProgressMonitor {
    private final static int THRESHOLD = 100;
    private static volatile long totalBytesRead = 0;
    private static long fileSize = 0;
    private static double currentProgressPercentage = 0.0;
    private static volatile int printCounter = 0;

    public static void setFileSize(long size) {
        fileSize = size;
    }

    public static synchronized void addBytesToTotal(long bytesRead) {
        totalBytesRead += bytesRead;
        currentProgressPercentage = calculatePercentage(fileSize, totalBytesRead);

        if(printCounter % THRESHOLD == 0) {
            String formattedPercentage = String.format("%.02f", currentProgressPercentage);
            System.out.println("Progress: " + formattedPercentage + "%");
        }

        printCounter = ++printCounter % THRESHOLD;
    }

    public synchronized static long getTotalBytesRead() {
        return totalBytesRead;
    }

    public synchronized static double getCurrentProgressPercentage() {
        return currentProgressPercentage;
    }

    private static double calculatePercentage(double totalFileSize, double currentReadSize) {
        double currentPercentage = currentReadSize * 100 / totalFileSize;
        return currentPercentage;
    }
}
