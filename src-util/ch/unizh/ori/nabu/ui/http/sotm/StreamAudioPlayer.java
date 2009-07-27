/*
 * Created on Jul 14, 2004
 *
 */
package ch.unizh.ori.nabu.ui.http.sotm;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.util.Vector;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.apache.log4j.Logger;

import com.sun.speech.freetts.audio.AudioPlayer;
import com.sun.speech.freetts.util.Utilities;

/**
 * @author pht
 *
 */
public class StreamAudioPlayer implements AudioPlayer {
	
	private OutputStream out;

	private boolean debug = false;
	private AudioFormat currentFormat = null;
	private byte[] outputData;
	private int curIndex = 0;
	private int totBytes = 0;
	private AudioFileFormat.Type outputType;
	private Vector outputList;

	/**
	 * Constructs a FileAudioPlayer 
	 *
	 * @param baseName the base name of the audio file
	 * @param type the type of audio output
	 *
	 */
	public StreamAudioPlayer(OutputStream out, AudioFileFormat.Type type) {
		this.out = out;
		this.outputType = type;
		debug = Utilities
				.getBoolean("com.sun.speech.freetts.audio.AudioPlayer.debug");
		outputList = new Vector();
	}

	/**
	 * Creates a default audio player for an AudioFileFormat of type
	 * WAVE.  Reads the "com.sun.speech.freetts.AudioPlayer.baseName"
	 * property for the base filename to use, and will produce a file
	 * of the form &lt;baseName>.wav.  The default value for the
	 * base name is "freetts".
	 */
	public StreamAudioPlayer(OutputStream out) {
		this(out, AudioFileFormat.Type.WAVE);
	}

	/**
	 * Sets the audio format for this player
	 *
	 * @param format the audio format
	 *
	 * @throws UnsupportedOperationException if the line cannot be opened with
	 *     the given format
	 */
	public synchronized void setAudioFormat(AudioFormat format) {
		currentFormat = format;
	}

	/**
	 * Gets the audio format for this player
	 *
	 * @return format the audio format
	 */
	public AudioFormat getAudioFormat() {
		return currentFormat;
	}

	/**
	 * Pauses audio output
	 */
	public void pause() {
	}

	/**
	 * Resumes audio output
	 */
	public synchronized void resume() {
	}

	/**
	 * Cancels currently playing audio
	 */
	public synchronized void cancel() {
	}

	/**
	 * Prepares for another batch of output. Larger groups of output
	 * (such as all output associated with a single FreeTTSSpeakable)
	 * should be grouped between a reset/drain pair.
	 */
	public synchronized void reset() {
	}

	/**
	 * Starts the first sample timer
	 */
	public void startFirstSampleTimer() {
	}

	private static final Logger log = Logger.getLogger(StreamAudioPlayer.class);
	
	/**
	 * Closes this audio player
	 */
	public synchronized void close() {
		try {
			InputStream is = new SequenceInputStream(outputList.elements());
			AudioInputStream ais = new AudioInputStream(is, currentFormat,
					totBytes / currentFormat.getFrameSize());
			if (false) {
				log.trace("Avail " + ais.available());
				log.trace("totBytes " + totBytes);
				log.trace("FS " + currentFormat.getFrameSize());
			}
			//System.out.println("Wrote synthesized speech to " + baseName);
//			System.out.println(out);
			AudioSystem.write(ais, outputType, out);
		} catch (IOException ioe) {
			log.error("Can't write audio to out", ioe);
		} catch (IllegalArgumentException iae) {
			log.error("Can't write audio type " + outputType, iae);
		}
	}

	/**
	 * Returns the current volume.
	 *
	 * @return the current volume (between 0 and 1)
	 */
	public float getVolume() {
		return 1.0f;
	}

	/**
	 * Sets the current volume.
	 *
	 * @param volume  the current volume (between 0 and 1)
	 */
	public void setVolume(float volume) {
	}

	/**
	 *  Starts the output of a set of data. Audio data for a single
	 *  utterance should be grouped between begin/end pairs.
	 *
	 * @param size the size of data between now and the end
	 */
	public void begin(int size) {
		outputData = new byte[size];
		curIndex = 0;
	}

	/**
	 *  Marks the end of a set of data. Audio data for a single 
	 *  utterance should be groupd between begin/end pairs.
	 *
	 *  @return true if the audio was output properly, false if the
	 *      output was cancelled or interrupted.
	 *
	 */
	public boolean end() {
		outputList.add(new ByteArrayInputStream(outputData));
		totBytes += outputData.length;
		return true;
	}

	/**
	 * Waits for all queued audio to be played
	 *
	 * @return true if the audio played to completion, false if
	 *   the audio was stopped
	 */
	public boolean drain() {
		return true;
	}

	/**
	 * Gets the amount of played since the last mark
	 *
	 * @return the amount of audio in milliseconds
	 */
	public synchronized long getTime() {
		return -1L;
	}

	/**
	 * Resets the audio clock
	 */
	public synchronized void resetTime() {
	}

	/**
	 * Writes the given bytes to the audio stream
	 *
	 * @param audioData audio data to write to the device
	 *
	 * @return <code>true</code> of the write completed successfully, 
	 *       	<code> false </code>if the write was cancelled.
	 */
	public boolean write(byte[] audioData) {
		return write(audioData, 0, audioData.length);
	}

	/**
	 * Writes the given bytes to the audio stream
	 *
	 * @param bytes audio data to write to the device
	 * @param offset the offset into the buffer
	 * @param size the size into the buffer
	 *
	 * @return <code>true</code> of the write completed successfully, 
	 *       	<code> false </code>if the write was cancelled.
	 */
	public boolean write(byte[] bytes, int offset, int size) {
		System.arraycopy(bytes, offset, outputData, curIndex, size);
		curIndex += size;
		return true;
	}

	/**
	 * Waits for resume. If this audio player
	 * is paused waits for the player to be resumed.
	 * Returns if resumed, cancelled or shutdown.
	 *
	 * @return true if the output has been resumed, false if the
	 *     output has been cancelled or shutdown.
	 */
	private synchronized boolean waitResume() {
		return true;
	}

	/**
	 * Returns the name of this audioplayer
	 *
	 * @return the name of the audio player
	 */
	public String toString() {
		return "StreamAudioPlayer";
	}

	/**
	 * Outputs a debug message if debugging is turned on
	 *
	 * @param msg the message to output
	 */
	private void debugPrint(String msg) {
		if (debug) {
			log.debug(toString() + ": " + msg);
		}
	}

	/**
	 * Shows metrics for this audio player
	 */
	public void showMetrics() {
	}

}