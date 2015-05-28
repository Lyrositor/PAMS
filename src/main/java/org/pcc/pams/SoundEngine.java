package org.pcc.pams;

import org.pcc.pams.objects.Bubble;
import org.pcc.pams.objects.Wall;

import javax.sound.midi.*;
import java.io.File;

/**
 * Generates all the sounds for the simulation.
 */
class SoundEngine implements PhysicsListener {

    /**
     * The default fundamental, which is C (Mi).
     */
    private static final int DEFAULT_FUNDAMENTAL = 4;
    /**
     * The standard note length, which is a half-note.
     */
    private static final int NOTE_LENGTH = 32;
    /**
     * The resultant melody's tempo.
     */
    private static final int TEMPO = 120;
    /**
     * The notes' max volume.
     */
    private static final int MAX_VELOCITY = 64;
    /**
     * The sequencer used to order the sounds and produce a MIDI file.
     */
    private final Sequencer sequencer;
    /**
     * The synthesizer used to generate the sounds.
     */
    private final Synthesizer synthesizer;
    /**
     * The sequencer's first (and only) track.
     */
    private Track track1;
    /**
     * Whether or not harmonic sounds are being generated.
     * <p>
     * This is used to produce a more melodious recording, and can noticeably
     * change the sound when there are many bubbles.
     */
    private boolean harmonic = false;
    /**
     * The current fundamental in use.
     */
    private int fundamental = DEFAULT_FUNDAMENTAL;
    /**
     * The current harmonic chord associated with the fundamental.
     *
     * Must be updated each time the fundamental changes.
     */
    private int[] harmonicChord = createMajorChord(fundamental);

    /**
     * Prepares the sound engine for first-time use.
     *
     * @throws MidiUnavailableException Thrown if unable to initialize systems.
     * @throws InvalidMidiDataException Thrown if unable to create sequence.
     */
    public SoundEngine() throws MidiUnavailableException,
            InvalidMidiDataException {
        sequencer = MidiSystem.getSequencer();
        synthesizer = MidiSystem.getSynthesizer();

        sequencer.open();
        synthesizer.open();

        reset();
    }

    /**
     * Create a table containing only the notes from a perfect major chord.
     *
     * @param fundamental The note number of the fundamental to use.
     * @return An array of note numbers representing the chord.
     */
    private static int[] createMajorChord(int fundamental) {
        int[] tableau = new int[32];
        tableau[0] = fundamental + 12;
        tableau[1] = tableau[0] + 4;
        tableau[2] = tableau[1] + 3;
        tableau[3] = tableau[2] + 5;

        for (int i = 4; i < tableau.length; i++) {
            if (tableau[i - 1] - tableau[i - 2] == 5)
                tableau[i] = tableau[i - 1] + 4;
            else if (tableau[i - 1] - tableau[i - 2] == 4)
                tableau[i] = tableau[i - 1] + 3;
            else
                tableau[i] = tableau[i - 1] + 5;
        }

        return tableau;
    }

    /**
     * Erases existing recorded data and re-initializes all systems.
     *
     * @throws InvalidMidiDataException Thrown if it fails to create sequence.
     */
    public void reset() throws InvalidMidiDataException {
        synchronized (sequencer) {
            // Disable ongoing recordings.
            if (sequencer.isRecording()) {
                sequencer.recordDisable(track1);
                sequencer.stopRecording();
            }

            // Create a new sequence and start recording on it.
            Sequence sequence = new Sequence(Sequence.PPQ, 16);
            track1 = sequence.createTrack();
            sequencer.setSequence(sequence);
            sequencer.setTempoInBPM(TEMPO);
            sequencer.recordEnable(track1, -1);
            sequencer.startRecording();
        }
    }

    /**
     * Toggles the harmonic mode.
     * <p>
     * When in harmonic mode, notes are taken from a pre-generated pool of
     * values.
     *
     * @param isHarmonic True if harmonic, false otherwise.
     */
    public void setHarmonic(boolean isHarmonic) {
        harmonic = isHarmonic;
    }

    /**
     * Changes the value of the fundamental note.
     *
     * @param newFundamental The new value of the fundamental.
     */
    public void setFundamental(int newFundamental) {
        fundamental = newFundamental;
        harmonicChord = createMajorChord(fundamental);
    }

    /**
     * Saves record data to the specified file.
     *
     * @param file The file to save the data to.
     * @return True if file write successful, false otherwise.
     */
    public boolean save(File file) {
        Sequence sequence = sequencer.getSequence();
        int[] fileTypes = MidiSystem.getMidiFileTypes(sequence);
        if (fileTypes.length > 0) {
            try {
                MidiSystem.write(sequence, fileTypes[0], file);
                return true;
            } catch (Exception e) {
                System.err.println("ERROR: Failed to write to file.");
                e.printStackTrace();
            }
        } else
            System.err.println("ERROR: Failed to find valid MIDI file types.");
        return false;
    }

    /**
     * Called when a bubble collides into another bubble.
     *
     * @param bubble      The first colliding bubble.
     * @param otherBubble The second colliding bubble.
     */
    @Override
    public void bubbleToBubbleCollision(Bubble bubble, Bubble otherBubble) {
        synchronized (sequencer) {
            double relativeSize = 1 - bubble.getRadius() / Bubble.MAX_RADIUS;
            double relativeSpeed = bubble.getSpeed().norm() / Bubble.MAX_SPEED;
            int note;

            // harmonicChord is made up only of the notes from a perfect major
            // chord, that is 132/12 + 12 = 32 notes in total (+12 in order to
            // avoid having sounds that are too low).
            if (harmonic)
                note = harmonicChord[(int) (relativeSize * harmonicChord.length)];
            else
                note = (int) (132 * relativeSize);
            long length = (long) ((1 - relativeSpeed) * NOTE_LENGTH);
            int velocity = (int) (relativeSpeed * MAX_VELOCITY);
            addNote(track1, note, sequencer.getTickPosition(), length,
                    velocity);
            sequencer.setTickPosition(sequencer.getTickPosition() + length);
        }
    }

    /**
     * Called when a bubble collides into a wall.
     *
     * @param bubble The colliding bubble.
     * @param wall The colliding wall.
     */
    @Override
    public void bubbleToWallCollision(Bubble bubble, Wall wall) {
        synchronized (sequencer) {
            double relativeSize = 1 - bubble.getRadius() / Bubble.MAX_RADIUS;
            double relativeSpeed = bubble.getSpeed().norm() / Bubble.MAX_SPEED;
            int note;

            // harmonicChord is made up only of the notes from a perfect major
            // chord, that is 132/12 + 12 = 32 notes in total (+12 in order to
            // avoid having sounds that are too low).
            if (harmonic)
                note = harmonicChord[(int) (relativeSize * harmonicChord.length)];
            else
                note = (int) (132 * relativeSize);
            long length = (long) ((1 - relativeSpeed) * NOTE_LENGTH);
            int velocity = (int) (relativeSpeed * MAX_VELOCITY);
            addNote(track1, note, sequencer.getTickPosition(), length,
                    velocity);
            sequencer.setTickPosition(sequencer.getTickPosition() + length);
        }
    }

    /**
     * Adds a new note to the track and plays it.
     *
     * @param track The track to add the note to.
     * @param note The note's value (from 0 to 127).
     * @param startTick The position on the track (in ticks) for the note.
     * @param tickLength The length of the note (in ticks).
     * @param velocity The volume of the note.
     */
    private void addNote(Track track, int note, long startTick, long tickLength,
                         int velocity) {
        ShortMessage msgOn = new ShortMessage();
        ShortMessage msgOff = new ShortMessage();

        try {
            msgOn.setMessage(ShortMessage.NOTE_ON, 0, note, velocity);
            msgOff.setMessage(ShortMessage.NOTE_OFF, 0, note, velocity);
            Receiver synthesizerReceiver = synthesizer.getReceiver();
            track.add(new MidiEvent(msgOn, startTick));
            track.add(new MidiEvent(msgOff, startTick + tickLength));
            synthesizerReceiver.send(msgOn, -1);
            synthesizerReceiver.send(msgOff, -1);
        } catch (Exception e) {
            System.err.println("ERROR: Failed to play sound.");
            e.printStackTrace();
        }
    }
}