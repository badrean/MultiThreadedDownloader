package org.example;

import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MultiThreadedFileDownloader {
    private String url;

    MultiThreadedFileDownloader(String url) {
        this.url = url;
    }

    public void startDownload() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(this.url).openConnection();
            long fileSize = connection.getContentLength();
            String fileName = Config.path + "/" + getFilename(connection);
            connection.disconnect();

            if (fileName == null || fileSize <= 0) {
                System.out.println("Invalid URL.");
                return;
            }

            DownloadProgressMonitor.setFileSize(fileSize);

            System.out.println(fileName + ", " + fileSize + " bytes");

            if (acceptsRange(connection)) {
                long chunkSize = fileSize / Config.connections;

                ExecutorService executor = Executors.newFixedThreadPool(Config.connections);
                for (int i = 0; i < Config.connections; i++) {
                    long start = i * chunkSize;
                    long end = (i == Config.connections - 1) ? (fileSize - 1) : (start + chunkSize - 1);
                    executor.execute(new DownloaderTask(this.url, fileName, start, end, i));
                }

                executor.shutdown();
                executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

                mergeParts(fileName);
            } else {
                ExecutorService executor = Executors.newFixedThreadPool(1);
                executor.execute(new DownloaderTask(this.url, fileName, 0, fileSize));
                System.out.println("Server accepts only 1 thread. Started working.");
                executor.shutdown();
                executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mergeParts(String outputPath) {
        try {
            BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(outputPath, true));

            System.out.println("Download finished. Merging parts...");

            for (int i = 0; i < Config.connections; i++) {
                String partFileName = outputPath + ".part" + i;
                BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(partFileName));

                int bufferSize = 1024 * 50;
                byte buffer[] = new byte[bufferSize];
                int bytesRead;
                while ((bytesRead = inStream.read(buffer, 0, bufferSize)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }

                inStream.close();

                File file = new File(partFileName);
                if (!file.delete()) {
                    System.out.println("Failed to delete file: " + partFileName);
                }
            }

            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean acceptsRange(HttpURLConnection connection) {
        String headerField = connection.getHeaderField("Accept-Ranges");

        return headerField != null && headerField.equals("bytes");
    }

    private String getFilename(HttpURLConnection connection) {
        String filename = FilenameUtils.getName(connection.getURL().getPath());
        String contentDisposition = connection.getHeaderField("Content-Disposition");

        if (contentDisposition != null) {
            String reg = "(?<=filename=)([^&]*)\"";

            Pattern p = Pattern.compile(reg);

            Matcher m = p.matcher(contentDisposition);
            if (m.find()) {
                String result = m.group();
                return result.substring(1, result.length() - 1);
            }
        }

        if (filename != null && !filename.isEmpty()) {
            return filename;
        }

        return null;
    }

    public static class Config {
        public static String path = "/Users/adrian/Downloads/Test";
        public static int connections = 8;
    }
}