import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileTest {

    public static void main(String[] args) {
        try {
            // 指定要列举的文件夹路径
            String folderPath = "C:\\Users\\31414\\IdeaProjects\\test\\";
            Path directory = Paths.get(folderPath);

            // 使用DirectoryStream列举文件
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
                for (Path file : stream) {
                    System.out.println(file.getFileName());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
