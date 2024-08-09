package org.example;

public class Main {
    public static void main(String[] args) {
        String url = "https://download.jetbrains.com/python/pycharm-professional-2024.1.6.dmg?_gl=1*1yp0boq*_gcl_au*MTU0ODU2OTc0MS4xNzE3ODQzMzcz*_ga*MTI1MTEzODgxNS4xNjQ5MzU2NzA5*_ga_9J976DJZ68*MTcyMzIyNTc0My4xNi4xLjE3MjMyMjU5NzkuNTguMC4w";

        MultiThreadedFileDownloader downloadManager = new MultiThreadedFileDownloader(url);
        downloadManager.startDownload();
    }
}
