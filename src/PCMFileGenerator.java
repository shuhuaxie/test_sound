import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class PCMFileGenerator {
    public static void main(String[] args) {
        // 设置音频格式
        AudioFormat audioFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                44100,   // 采样率
                16,      // 采样位数
                2,       // 声道数 (1 for mono, 2 for stereo)
                4,       // 每个帧的字节数
                44100,   // 每秒的帧数
                false);  // 大端字节序

        // 指定生成的音频文件路径
        String outputFile = "output.wav";

        try {
            // 生成 PCM 数据
            byte[] pcmData = generatePCMData(audioFormat);

            // 创建 ByteArrayInputStream 来包装 PCM 数据
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pcmData);

            // 创建 AudioInputStream
            AudioInputStream audioInputStream = new AudioInputStream(
                    byteArrayInputStream,
                    audioFormat,
                    pcmData.length / audioFormat.getFrameSize()
            );

            // 将 PCM 数据写入音频文件
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new File(outputFile));

            System.out.println("Audio file generated successfully: " + outputFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 生成3秒钟的 PCM 数据（示例）
    private static byte[] generatePCMData(AudioFormat audioFormat) {
        int durationInSeconds = 3;
        int sampleRate = (int) audioFormat.getSampleRate();
        int numChannels = audioFormat.getChannels();
        int numFrames = durationInSeconds * sampleRate;
        int numBytes = numFrames * numChannels * 2; // 16-bit PCM

        byte[] pcmData = new byte[numBytes];

        // 生成简单的 PCM 数据
        for (int i = 0; i < numFrames; i++) {
            short sample = (short) (Math.sin(2.0 * Math.PI * 440.0 * i / sampleRate) * Short.MAX_VALUE);
            pcmData[i * 2] = (byte) (sample & 0xFF);
            pcmData[i * 2 + 1] = (byte) ((sample >> 8) & 0xFF);
        }

        return pcmData;
    }
}

