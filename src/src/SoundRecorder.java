package src;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class SoundRecorder {
	private static String name;
	private static long recordLength;
	private static String filePath;
	private static File wavFile;

	public SoundRecorder(String filePath) {
		SoundRecorder.filePath = filePath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		SoundRecorder.name = name;
	}

	public long getRecordLength() {
		return recordLength;
	}

	public void setRecordLength(long recordLength) {
		SoundRecorder.recordLength = recordLength;
	}

	public File record() throws LineUnavailableException {
		AudioFormat format = new AudioFormat(48000, 16, 2, true, true);
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
		if (!AudioSystem.isLineSupported(info)) {
			System.out.println("Line not supported!");
		}

		TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
		targetDataLine.open();
		System.out.println("starting");
		targetDataLine.start();

		Thread stopper = new Thread(new Runnable() {
			public void run() {
				AudioInputStream audioStream = new AudioInputStream(targetDataLine);
				wavFile = new File(filePath + "/" + name + ".wav");
				try {
					AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, wavFile);
				}

				catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		stopper.start();
		try {
			Thread.sleep(recordLength);
			targetDataLine.stop();
			targetDataLine.close();
			System.out.println("End");
		}

		catch (InterruptedException e) {
			e.printStackTrace();
		}

		return wavFile;
	}
}
