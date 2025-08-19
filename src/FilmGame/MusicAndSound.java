package FilmGame;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MusicAndSound 
{
		
	public static Clip playMusic(String Path, Clip clip) throws LineUnavailableException
	{
		File file=new File(Path);
		try {
			AudioInputStream audio=AudioSystem.getAudioInputStream(file);
			clip=AudioSystem.getClip();
			clip.open(audio);
			clip.loop(Clip.LOOP_CONTINUOUSLY);			
		} catch (UnsupportedAudioFileException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return clip;
	} 
	
	public static Clip soundEffect(String Path, Clip clip) throws LineUnavailableException {
		File file=new File(Path);
		try {
			AudioInputStream audio=AudioSystem.getAudioInputStream(file);
			clip=AudioSystem.getClip();
			clip.open(audio);
		} catch (UnsupportedAudioFileException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return clip;
	}
		
	
}
