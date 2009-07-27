/*
 * Speak.java
 *
 * Created on 29. August 2003, 01:59
 */

package ch.unizh.ori.common.sotm.core;

import javax.speech.*;
import javax.speech.synthesis.*;
import java.util.Locale;

/**
 *
 * @author  pht
 */
public abstract class Speak {
	
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(Speak.class);
    
    private static Speak _default;
    public static Speak getDefault(){
        if(_default == null){
            if(doSpeak()){
                _default = new SpeakImpl();
            }else{
                _default = new Speak.NullSpeak();
            }
        }
        return _default;
    }
    private static boolean doSpeak = false;
    static{
        doSpeak = Boolean.getBoolean("nabu.sotm.doSpeak");
        log.debug("doSpeak="+doSpeak);
        // do we have the classes?
        try{
            Class.forName("javax.speech.Engine");
        }catch(Throwable t){
            log.error("No Speech classes!", t);
            doSpeak = false;
        }
        log.debug("doSpeak="+doSpeak);
    }
    public static boolean doSpeak(){
        return doSpeak;
    }
    
    public abstract void speak(String str);

    private static class NullSpeak extends Speak {
        private NullSpeak(){}
        public void speak(String str){};
    }
    
    private static class SpeakImpl extends Speak {

        Synthesizer synth;

        /** Creates a new instance of Speak */
        private SpeakImpl() {
            try{
                String synthesizerName = System.getProperty
                    ("nabu.sotm.synthesizerName",
                     "Unlimited domain FreeTTS Speech Synthesizer from Sun Labs");

                // Create a new SynthesizerModeDesc that will match the FreeTTS
                // Synthesizer.
                SynthesizerModeDesc desc = new SynthesizerModeDesc
                    (synthesizerName,
                     null,
                     Locale.US,
                     Boolean.FALSE,         // running?
                     null);                 // voice

                synth = Central.createSynthesizer(desc);

                // create the voice
                String voiceName = System.getProperty("voiceName", "kevin16");
                Voice voice = new Voice
                    (voiceName, Voice.GENDER_DONT_CARE, Voice.AGE_DONT_CARE, null);
                
                synth.allocate();
                synth.resume();
                synth.getSynthesizerProperties().setVoice(voice);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }

        public void speak(String str){
        	log.debug("speaking: "+str);
            try{
                // Speak the "Hello world" string
                synth.speakPlainText(str, null);

                // Wait till speaking is done
                //synth.waitEngineState(Synthesizer.QUEUE_EMPTY);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }

        private void deallocat(){
            try{
            synth.deallocate();
            synth = null;
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }

    }
    
}
