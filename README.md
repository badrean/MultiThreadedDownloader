# Multithread File Downloader
## Introduction 
The Multithreaded File Downloader is a Java application that downloads files from a given URL using multiple threads or a single thread. The application decides whether to use multiple threads based on whether the server supports byte-range requests. The number of threads and the download directory can be configured through the **Config** class within the **MultiThreadedFileDownloader** class.

## Features
* **Multi-Threaded Downloading:** If the server supports byte-range requests, the application can split the download into multiple parts, with each part downloaded by a separate thread, significantly speeding up the download process.
* **Single-Threaded Downloading:** If the server does not support byte-range requests, the application gracefully falls back to single-threaded downloading.
* **Configurable Settings:** The number of download threads and the target directory for downloaded files can be customized through the Config class.

## Installation
1. <b>Clone the repository:</b>
  ```
  git clone git@github.com:badrean/SimpleBankingApplication.git
  cd SimpleBankingApplication
  ```

2. <b>Build and Run the Application:</b>
   * Add your configuration:
     Set your configurations and URL. Example:
     ```java
     public static void main(String[] args) {
        String url = "https://download.jetbrains.com/python/pycharm-professional-2024.1.6.dmg?_gl=1*1yp0boq*_gcl_au*MTU0ODU2OTc0MS4xNzE3ODQzMzcz*_ga*MTI1MTEzODgxNS4xNjQ5MzU2NzA5*_ga_9J976DJZ68*MTcyMzIyNTc0My4xNi4xLjE3MjMyMjU5NzkuNTguMC4w";
        MultiThreadedFileDownloader.Config.connections = 8;                    // set the number of connections
        MultiThreadedFileDownloader.Config.path = "/Users/youruser/Downloads"; // set the download path

        MultiThreadedFileDownloader downloadManager = new MultiThreadedFileDownloader(url);
        downloadManager.startDownload();
     }
     ```
   * Compile the Project:
     ```
       mvn clean compile
     ```
   * Run the Application:
     ```
       mvn exec:java -Dexec.mainClass="org.example.Main"
     ```

## Usage
1. **Set the URL:** Provide the URL of the file you want to download.
2. **Configure the Settings:**
   * **Number of Threads:** Set the desired number of connections/threads using MultiThreadedFileDownloader.Config.connections.
   * **Download Directory:** Set the directory where the file will be saved using MultiThreadedFileDownloader.Config.path.
3. **Start the Download:** Call the startDownload() method to begin the download process.

## Future Improvements
* **Pause/Resume Functionality:** Implement the ability to pause and resume downloads, which would allow users to manage their downloads more flexibly.
* **User Interface:** A minimal user interface from which the URL and configurations can be set.

## Contributing
Contributions are welcome!

## License
This project is licensed under the MIT License - see the [LICENSE](https://github.com/badrean/MultiThreadedDownloader/blob/main/LICENSE) file for details.
