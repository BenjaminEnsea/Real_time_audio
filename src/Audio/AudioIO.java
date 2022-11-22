package Audio;

import javax.sound.sampled.*;
import java.util.Arrays;

public class AudioIO {

    /**
     * Displays every audio mixer available on the current system.
     */
    public static void printAudioMixers() {
        System.out.println("Mixers:");
        Arrays.stream(AudioSystem.getMixerInfo())
                .forEach(e -> System.out.println("- name=\"" + e.getName() + "\" description=\"" + e.getDescription() + " by " + e.getVendor() + "\""));
    }

    /**
     * @return a Mixer.Info whose name matches the given string.
     * 13 Example of use: getMixerInfo("Macbook default output")
     */
    public static Mixer.Info getMixerInfo(String mixerName) {
        // see how the use of streams is much more compact than for() loops!
        return Arrays.stream(AudioSystem.getMixerInfo())
                .filter(e -> e.getName().equalsIgnoreCase(mixerName)).findFirst().get();
    }

    /** Return a line that's appropriate for recording sound from a microphone.
     21 * Example of use:
     22 * TargetDataLine line = obtainInputLine("USB Audio Device", 8000);
     23 * @param mixerName a string that matches one of the available mixers.
     24 * @see AudioSystem.getMixerInfo() which provides a list of all mixers on your system.
     25 */
   public static TargetDataLine obtainAudioInput(String mixerName, int sampleRate) throws LineUnavailableException {

       Mixer.Info mixerInfo =  getMixerInfo(mixerName);
       AudioFormat format = new AudioFormat(sampleRate, 16, 1, true , true);
       return AudioSystem.getTargetDataLine(format, mixerInfo);

    }

    /**
     * Return a line that's appropriate for playing sound to a loudspeaker.
     */
    public static SourceDataLine obtainAudioOutput(String mixerName, int sampleRate) throws LineUnavailableException {
        Mixer.Info mixerInfo =  getMixerInfo(mixerName);
        AudioFormat format = new AudioFormat(sampleRate,16, 1, true , true);
        return AudioSystem.getSourceDataLine(format, mixerInfo);

    }



    public void startAudioProcessing(String inputMixer, String outputMixer, int sampleRate, int frameSize) throws LineUnavailableException {
        TargetDataLine inLine = obtainAudioInput(inputMixer, sampleRate);
        SourceDataLine outLine = obtainAudioOutput(outputMixer, sampleRate);
        inLine.open(); inLine.start(); outLine.open(); outLine.start();
        AudioProcessor as = new AudioProcessor(inLine, outLine, frameSize);
        new Thread(new AudioProcessor(inLine, outLine, frameSize)).start();
        System.out.println("A new thread has been created!");
    }





    public static void main(String[] args) throws LineUnavailableException {
        printAudioMixers();
        TargetDataLine input = obtainAudioInput("Microphone MacBook Pro",8000);
        SourceDataLine output = obtainAudioOutput("Haut-parleurs MacBook Pro", 8000);


    }

}
