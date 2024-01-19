import javax.sound.midi.*;
import java.io.IOException;

public class MidiSequenceCreator {

    public static void main(String[] args) {
        try {
            // Create a new sequence with 24 ticks per quarter note
            Sequence sequence = new Sequence(Sequence.PPQ, 24);

            // Create a new track in the sequence
            Track track = sequence.createTrack();

            // Add a note-on event to the track
            ShortMessage noteOn = new ShortMessage();
            noteOn.setMessage(ShortMessage.NOTE_ON, 0, 60, 93);
            MidiEvent noteOnEvent = new MidiEvent(noteOn, 0);
            track.add(noteOnEvent);

            // Add a note-off event to the track
            ShortMessage noteOff = new ShortMessage();
            noteOff.setMessage(ShortMessage.NOTE_OFF, 0, 60, 0);
            MidiEvent noteOffEvent = new MidiEvent(noteOff, 24);
            track.add(noteOffEvent);



                    try {
                        // 创建 Sequencer 对象
                        Sequencer sequencer = MidiSystem.getSequencer();
                        sequencer.open();

                        // 从文件中加载 Sequence 对象
//                        Sequence sequence = MidiSystem.getSequence(MidiPlayer.class.getResourceAsStream("your_midi_file.mid"));

                        // 将 Sequence 设置到 Sequencer
                        sequencer.setSequence(sequence);

                        // 播放 MIDI 文件
                        sequencer.start();
                        while (sequencer.isRunning()) {
                            // 等待播放结束
                        }
                        sequencer.close();

                        // 保存 MIDI 文件到磁盘
                        MidiSystem.write(sequence, 1, new java.io.File(".\\output.mid"));

                    } catch (MidiUnavailableException | InvalidMidiDataException
                             | IOException
                        e
                    ) {
                        e.printStackTrace();
                    }



        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }

    }
}
