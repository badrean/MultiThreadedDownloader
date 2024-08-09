package org.example;

import org.apache.commons.io.FilenameUtils;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloaderTask implements Runnable {
    private String url;
    private String outputPath;
    private long start;
    private long end;
    private int partNumber = -1;

    public DownloaderTask(String url, String outputPath, long start, long end, int partNumber) {
        this.url = url;
        this.outputPath = outputPath;
        this.start = start;
        this.end = end;
        this.partNumber = partNumber;
    }

    public DownloaderTask(String url, String outputPath, long start, long end) {
        this.url = url;
        this.outputPath = outputPath;
        this.start = start;
        this.end = end;
    }
    @Override
    public void run() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(this.url).openConnection();
            String byteRange = start + "-" + end;
            connection.setRequestProperty("Range", "bytes=" + byteRange);
            connection.connect();

            System.out.println(byteRange);

            InputStream inputStream = connection.getInputStream();

            BufferedOutputStream outputStream;
            if (partNumber == -1) {
                outputStream = new BufferedOutputStream(new FileOutputStream(this.outputPath, true));
            } else {
                outputStream = new BufferedOutputStream(new FileOutputStream(this.outputPath + ".part" + partNumber, true));
            }

            int bufferSize = 1024 * 50;
            byte[] buffer = new byte[bufferSize];
            int bytesRead;
            while ((bytesRead = (inputStream.read(buffer, 0, bufferSize))) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                DownloadProgressMonitor.addBytesToTotal(bytesRead);
            }

            inputStream.close();
            outputStream.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
