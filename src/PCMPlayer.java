import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PCMPlayer {
    public static void main(String[] args) {
        // 指定PCM文件路径
        String pcmFilePath = ".\\output.pcm";

        try {
            // 设置音频格式
            AudioFormat audioFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    44100,   // 采样率
                    16,      // 采样位数
                    2,       // 声道数 (1 for mono, 2 for stereo)
                    4,       // 每个帧的字节数
                    44100,   // 每秒的帧数
                    false);  // 大端字节序

            // 从文件中读取PCM数据
            InputStream pcmStream = new FileInputStream(pcmFilePath);
            AudioInputStream audioInputStream = new AudioInputStream(pcmStream, audioFormat, AudioSystem.NOT_SPECIFIED);

            // 根据音频格式打开音频输出设备
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(audioFormat);
            line.start();

            // 从输入流读取PCM数据并写入输出设备
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = audioInputStream.read(buffer)) != -1) {
                line.write(buffer, 0, bytesRead);
            }

            // 关闭音频输出设备和输入流
            line.drain();
            line.stop();
            line.close();
            audioInputStream.close();

        } catch (IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
