
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.newdawn.easyogg.OggClip;

public class Sound {

	Clip sounds[] = new Clip[12];
	Clip musik;
	OggClip music[] = new OggClip[5];
	private boolean playMusic = true, playSounds = true;

	public Sound() {
		musik = loadSound("Title.wav");
		for (int i = 0; i < 5; i++) {
			try {
				music[i] = new OggClip("snailrailstage" + (i + 1) + ".OGG");
			} catch (IOException e) {
				// TODO Auto-generated catch block

				e.printStackTrace();
			}

		}

	}

	public boolean canPlayMusic() {
		return playMusic;
	}

	public boolean canPlaySounds() {
		return playSounds;
	}

	public void switchMusic() {
		playMusic = !playMusic;
		if (playMusic == false) {
			musik.stop();
		}
	}

	public void switchSounds() {
		playSounds = !playSounds;
	}

	public void playTitle() {
		if (playMusic) {
			musik.loop(999);
			for (int i = 0; i < music.length; i++) {
				music[i].stop();

			}
		}
		/*
		 * if(!musik.isRunning()) { if(musik.getMicrosecondPosition()>0) {
		 * musik.start(); } else { musik.setMicrosecondPosition(0); musik.start(); } }
		 */
	}

	public void playMusic(int nr) {
		if (playMusic) {
			musik.stop();
			for (int i = 0; i < music.length; i++) {
				if (i != nr) {
					music[i].stop();
				}
			}

			music[nr].loop();
		}
	}

	public void stopTitle() {
		musik.stop();
	}

	public void playSound(int nr) {
		if (playSounds) {
			sounds[nr].setMicrosecondPosition(0);
			sounds[nr].start();
		}
	}

	public void loadSounds() {
		for (int i = 0; i < sounds.length; i++) {
			sounds[i] = loadSound("s" + i + ".wav");
		}
	}

	private Clip loadSound(String name) {
		Clip clip = null;
		try {
			try {
				// Open an audio input stream.
				java.net.URL url = this.getClass().getClassLoader().getResource(name);
				AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
				clip = AudioSystem.getClip();
				// Open audio clip and load samples from the audio input stream.
				clip.open(audioIn);
				clip.setMicrosecondPosition(0);

			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clip;
	}
}
