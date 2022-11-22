package Audio;

import javax.sound.sampled.*;

/** A container for an audio signal backed by a double buffer so as to allow floating point calculation
 2 * for signal processing and avoid saturation effects. Samples are 16 bit wide in this implementation. */
public class AudioSignal {

    private final double[] sampleBuffer; // floating point representation of audio samples
    private double dBlevel; // current signal level
    //private int length; //taille du signal
    private int FrameSize;


 /** Construct an AudioSignal that may contain up to "frameSize" samples.
 9 * @param frameSize the number of samples in one audio frame */

    public AudioSignal(int FrameSize) {
        this.sampleBuffer = new double[FrameSize];

    }

 /** Sets the content of this signal from another signal.
 13 * @param other other.length must not be lower than the length of this signal. */

    public void setFrom(AudioSignal other) {
        System.arraycopy(other.sampleBuffer, 0, this.sampleBuffer, 0, other.sampleBuffer.length);
    }

    /** Fills the buffer content from the given input. Byte's are converted on the fly to double's.
* @return false if at end of stream */

    public boolean recordFrom(TargetDataLine audioInput) {
        byte[] byteBuffer = new byte[sampleBuffer.length * 2]; // 16 bit samples
        if (audioInput.read(byteBuffer, 0, byteBuffer.length) == -1) return false;
        int i;
        for (i = 0; i < sampleBuffer.length-1; i++) {
            //sampleBuffer[i] = audioInput.
            audioInput.read(byteBuffer, 0, byteBuffer.length);
            sampleBuffer[i] = ((byteBuffer[2 * i] << 8) + byteBuffer[2 * i + 1]) / 32768.0; // big endian
            // ... TODO : dBlevel = update signal level in dB here ...
            dBlevel = 20 * Math.log10(Math.abs(sampleBuffer[i]));
        }
        return true;
    }

        /** Plays the buffer content to the given output.
 28 * @return false if at end of stream */

        public boolean playTo(SourceDataLine audioOutput) {
            byte[] byteBufferout = new byte[sampleBuffer.length * 2];
            if (audioOutput.write(byteBufferout, 0, byteBufferout.length) == -1)return false;
            int i;
            for (i = 0; i < sampleBuffer.length-1; i++)
            {
                byte a = 3;
                byteBufferout[2*i+1] = a;
                }
            audioOutput.write( byteBufferout , 0, byteBufferout.length);
            return true;
        }

        // your job: add getters and setters ...
        // double getSample(int i)
        // void setSample(int i, double value)
        // double getdBLevel()
        // int getFrameSize()
        // Can be implemented much later: Complex[] computeFFT()

    public double getSample(int i) {
        return sampleBuffer[i];
    }

    public double getdBlevel() {
        return dBlevel;
    }

    public int getFrameSize(){
        return FrameSize;
    }

    public void setSample(int i, double Sample) {
        this.sampleBuffer[i] = Sample;
    }

    public static void main(String[] args) {
        try {
            AudioSignal A1 = new AudioSignal(1000);
            AudioFormat format = new AudioFormat(8000, 16, 1, true, true);
            DataLine.Info dataInfo= new DataLine.Info(TargetDataLine.class, format);
            if(!AudioSystem.isLineSupported(dataInfo)){
                System.out.println("not supported");
            }
            TargetDataLine line = AudioSystem.getTargetDataLine(format);
            line.open(format);
            line.start();
            System.out.println("Starting...");
            System.out.println("Start recording...");

            //System.out.println(line);

            A1.recordFrom(line);
            System.out.println(A1.dBlevel);
            final SourceDataLine lineout = AudioSystem.getSourceDataLine(format);
            System.out.println("Start playing...");
            A1.playTo(lineout);

        }
        catch(Exception e){
            System.out.println(e);
        }

    }
}
