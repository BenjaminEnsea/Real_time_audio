package Audio;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

/** The main audio processing class, implemented as a Runnable so
 2 * as to be run in a separated execution Thread. */
public class AudioProcessor implements Runnable {

        private AudioSignal inputSignal, outputSignal;
        private TargetDataLine audioInput;
        private SourceDataLine audioOutput;
        private boolean isThreadRunning; // makes it possible to "terminate" thread

        private int FrameSize;

 /** Creates an AudioProcessor that takes input from the given TargetDataLine, and plays back
 11 * to the given SourceDataLine.
 12 * @param frameSize the size of the audio buffer. The shorter, the lower the latency. */
        public AudioProcessor(TargetDataLine audioInput, SourceDataLine audioOutput, int frameSize) throws LineUnavailableException {
            this.inputSignal = new AudioSignal(frameSize);
            this.outputSignal = new AudioSignal(frameSize);
            audioInput.open();audioInput.start();
            inputSignal.recordFrom(audioInput);
            audioOutput.open();audioOutput.start();
            outputSignal.playTo(audioOutput);
         }

        /** Audio processing thread code. Basically an infinite loop that continuously fills the sample
 17 * buffer with audio data fed by a TargetDataLine and then applies some audio effect, if any,
 18 * and finally copies data back to a SourceDataLine.*/
        @Override
        public void run() {
            isThreadRunning = true;
             while (isThreadRunning) {
                 TargetDataLine entree = null;
                 try {
                     entree = AudioIO.obtainAudioInput("Default Audio Device", 8000);
                 } catch (LineUnavailableException e) {
                     throw new RuntimeException(e);
                 }
                 inputSignal.recordFrom(entree);

             // your job: copy inputSignal to outputSignal with some audio effect
                 inputSignal=outputSignal;
                 SourceDataLine sortie = null;
                 try {
                     sortie = AudioIO.obtainAudioOutput("Default Audio Device", 8000);
                 } catch (LineUnavailableException e) {
                     throw new RuntimeException(e);
                 }
                 outputSignal.playTo(sortie);
            }
        }

        /** Tells the thread loop to break as soon as possible. This is an asynchronous process. */
        public void terminateAudioThread() throws InterruptedException {
            wait(3000);
            isThreadRunning=false;
        }

        // todo here: all getters and setters

        /* an example of a possible test code */
        public static void main(String[] args) throws LineUnavailableException {
            TargetDataLine inLine = AudioIO.obtainAudioInput("Default Audio Device", 8000);
            SourceDataLine outLine = AudioIO.obtainAudioOutput("Default Audio Device", 8000);
            inLine.open(); inLine.start(); outLine.open(); outLine.start();
            AudioProcessor as = new AudioProcessor(inLine, outLine, 64);
            new Thread(new AudioProcessor(inLine, outLine, 64)).start();
            System.out.println("A new thread has been created!");
        }
}
