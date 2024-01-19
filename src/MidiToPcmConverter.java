import javax.sound.midi.*;
import javax.sound.sampled.*;
import java.io.*;

public class MidiToPcmConverter {


    public static void main(String[] args) {
        try {

            // 创建 AudioFormat 对象
            AudioFormat audioFormat = new AudioFormat(44100, 16, 2, true, false);

            // 创建 DataLine.Info 对象
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);

            // 获取默认的音频设备
            Mixer mixer = AudioSystem.getMixer(null);
            SourceDataLine sourceDataLine = (SourceDataLine) mixer.getLine(dataLineInfo);

            // 打开 SourceDataLine
            sourceDataLine.open(audioFormat);
            sourceDataLine.start();

            // 创建 Sequencer 对象
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();

            File file = new File("C:\\Users\\31414\\IdeaProjects\\test\\output.mid");
            System.out.println("exists:" + file.exists());
            InputStream inputStream = new FileInputStream(file);
            // 从文件中加载 Sequence 对象
//            Sequence sequence = MidiSystem.getSequence(MidiToPcmConverter.class.getResourceAsStream("C:\\Users\\31414\\IdeaProjects\\test\\output.mid"));
            Sequence sequence = MidiSystem.getSequence(inputStream);

            // 设置 Sequencer 的 Sequence
            sequencer.setSequence(sequence);

            // 获取 Sequencer 的传输器
            Transmitter transmitter = sequencer.getTransmitter();

            // 设置传输器的接收器
            Receiver receiver = new SynthesizerReceiver(sourceDataLine);
            transmitter.setReceiver(receiver);

            // 启动 Sequencer
            sequencer.start();

            // 等待 Sequencer 结束
            while (sequencer.isRunning()) {
                Thread.sleep(1000);
            }

            // 关闭资源
            sequencer.close();
            sourceDataLine.stop();
            sourceDataLine.close();

        } catch (MidiUnavailableException | InvalidMidiDataException | IOException | LineUnavailableException |
                 InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class SynthesizerReceiver implements Receiver {
    private final SourceDataLine sourceDataLine;
    private final FileOutputStream fileOutputStream;

    public SynthesizerReceiver(SourceDataLine sourceDataLine) {
        this.sourceDataLine = sourceDataLine;
        try {
            // 创建文件输出流，保存 PCM 数据到文件
            this.fileOutputStream = new FileOutputStream(".\\output.pcm");
        } catch (IOException e) {
            throw new RuntimeException("Error creating FileOutputStream", e);
        }
    }

    @Override
    public void send(MidiMessage message, long timeStamp) {
        if (message instanceof ShortMessage) {
            ShortMessage shortMessage = (ShortMessage) message;
            // 将 ShortMessage 转换为音频数据
            byte[] audioData = convertShortMessageToAudioData(shortMessage);
            // 将音频数据写入 SourceDataLine
            sourceDataLine.write(audioData, 0, audioData.length);
            // 将音频数据写入文件
            try {
                fileOutputStream.write(audioData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void close() {
        try {
            // 关闭文件输出流
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 关闭资源
        sourceDataLine.stop();
        sourceDataLine.close();
    }

    private byte[] convertShortMessageToAudioData(ShortMessage shortMessage) {
        // 这里的实现略去，你可以根据 ShortMessage 的内容生成相应的音频数据
        // 例如，你可以将音符信息转换为波形数据
        // 这个过程涉及音频合成和波形生成，具体实现取决于你的需求和音乐理论知识
        // 下面只是一个简单的示例，实际应用中可能需要更复杂的处理
        int noteValue = shortMessage.getData1();
        byte[] audioData = new byte[1024];  // 假设音频数据长度为 1024
        // 在实际应用中，你需要根据音符信息生成对应的波形数据，以下为示例代码
        for (int i = 0; i < audioData.length; i++) {
            double wave = Math.sin(2 * Math.PI * noteValue * i / 44100.0);
            audioData[i] = (byte) (wave * 127);
        }
        return audioData;
    }
}



