import objects.Bubble;

import javax.sound.midi.*;
import java.io.File;


class SoundEngine implements PhysicsListener {

    private static final int NOTE_LENGTH = 32;  // Half Note
    private static final int TEMPO = 120;
    private static final int VELOCITY = 64;  // Middle Volume
    private static final int FONDAMENTAL = 4;  // On peut choisir quel est le
    // case qui fait que les sons des
    // nouvelle sbulles appartiennent à un
    // accord parfait majeur.
    // fondamental de l'accord (la
    // note la plus basse).
    private static final int[] harmonicChord = createMajorChord();
    private final Sequencer sequencer;
    private final Synthesizer synthesizer;
    private Track track1;
    private boolean harmonic = false;  // En mode "harmonieux", on coche une
    // Par exemple si la note fondamentale est do, l'accord sera do-mi-sol, si c'est la, ce sera la-do-mi, etc...
    // Il faudrait donc un curseur pour choisir notre fondamentale :-)
    // ({Do =0; Re = 2; Mi=4; Fa=5; Sol = 7; La = 9; Si=11} [12])
    // 0<=fondamentale<12


    /**
     * Prepares the sound engine for first-time use.
     *
     * @throws MidiUnavailableException
     * @throws InvalidMidiDataException
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
     * @return An array of note numbers representing the chord.
     */
    private static int[] createMajorChord() {
        int[] tableau = new int[32];
        tableau[0] = FONDAMENTAL + 12;
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
     * @throws MidiUnavailableException
     * @throws InvalidMidiDataException
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

    public void setHarmonic(boolean isHarmonic) {
        harmonic = isHarmonic;
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
            int note;
            if (harmonic)
                note = harmonicChord[(int) (relativeSize * harmonicChord.length)];  // harmonicChord n'est composé que des notes d'un accord parfait majeur, soit une 132/12 + 12 = 32 (+12 pour ne pas avoir des sons trop graves) en tout.
            else
                note = (int) (132 * relativeSize);
            long length = (long) ((1 - bubble.getSpeed().norm() / Bubble.MAX_SPEED) * NOTE_LENGTH);
            int velocity = (int) (bubble.getSpeed().norm() / Bubble.MAX_SPEED * VELOCITY);
            addNote(track1, note, sequencer.getTickPosition(), length, velocity);
            sequencer.setTickPosition(sequencer.getTickPosition() + length);
        }
    }

    /**
     * Called when a bubble collides into a wall.
     *
     * @param bubble The colliding bubble.
     */
    @Override
    public void bubbleToWallCollision(Bubble bubble) {
        synchronized (sequencer) {
            double relativeSize = 1 - bubble.getRadius() / Bubble.MAX_RADIUS;
            int note;
            if (harmonic)
                note = harmonicChord[(int) (relativeSize * harmonicChord.length)];  // harmonicChord n'est composé que des notes d'un accord parfait majeur, soit une 132/12 + 12 = 32 (+12 pour ne pas avoir des sons trop graves) en tout.
            else
                note = (int) (132 * relativeSize);
            long length = (long) ((1 - bubble.getSpeed().norm() / Bubble.MAX_SPEED) * NOTE_LENGTH);
            int velocity = (int) (bubble.getSpeed().norm() / Bubble.MAX_SPEED * VELOCITY);
            addNote(track1, note, sequencer.getTickPosition(), length, velocity);
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
    private void addNote(
            Track track, int note, long startTick, long tickLength, int velocity) {
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