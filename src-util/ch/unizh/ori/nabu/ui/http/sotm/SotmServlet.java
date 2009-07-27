/*
 * Created on Jul 14, 2004
 *
 */
package ch.unizh.ori.nabu.ui.http.sotm;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import com.sun.speech.freetts.util.Utilities;

/**
 * @author pht
 *  
 */
public class SotmServlet extends HttpServlet {
	private Voice voice8k;
	private String voice8kName = Utilities.getProperty("voice8kName", "kevin");

	// 16k Voice
	private Voice voice16k;
	private String voice16kName = Utilities.getProperty("voice16kName",
			"kevin16");
	
//	private StreamAudioPlayer streamAudioPlayer;
	
	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	public void init() throws ServletException {
		try {
			System.setProperty("freetts.voices","com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
			VoiceManager voiceManager = VoiceManager.getInstance();
			voice8k = voiceManager.getVoice(voice8kName);
			voice16k = voiceManager.getVoice(voice16kName);
			voice8k.allocate();
			voice16k.allocate();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String text = request.getParameter("text");
		response.setContentType("audio/wav");

		Voice voice;
		int sampleRate = 16000;

		if (sampleRate == 8000) {
			voice = voice8k;
		} else if (sampleRate == 16000) {
			voice = voice16k;
		} else {
			// TODO invalid sample rate
			return;
		}

		StreamAudioPlayer audioPlayer = new StreamAudioPlayer(response.getOutputStream());
		voice.setAudioPlayer(audioPlayer);
		voice.speak(text);
		audioPlayer.close();
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#destroy()
	 */
	public void destroy() {
		super.destroy();
		voice8k.deallocate();
		voice16k.deallocate();
	}

}