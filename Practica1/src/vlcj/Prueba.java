package vlcj;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.windows.Win32FullScreenStrategy;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class Prueba { //prueba git 2
	
	public static void main(String[] args) {
		  //gui to display video
		
		JFrame f = new JFrame();
		f.setLocation(100, 100);
		f.setSize(1000,600);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		
		
		//instance of canvas wish will be used to diaplay video
		
		Canvas c = new Canvas(); 
		
		c.setBackground(Color.BLACK);
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		
		p.add(c);
		f.add(p);
		
		//read video file
		
		//load native library of vlc from the directory
		
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "C:/Program Files/VideoLAN/VLC");
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		
		//initializa the media player
		
		MediaPlayerFactory mpf = new MediaPlayerFactory();
		
		//controll interactions
		
		
	EmbeddedMediaPlayer emp = mpf.newEmbeddedMediaPlayer(new Win32FullScreenStrategy(f));
	emp.setVideoSurface(mpf.newVideoSurface(c));
	//fullscreen
	emp.toggleFullScreen();
	//hide cursor
	emp.setEnableMouseInputHandling(false);
	//disable kayboard
	emp.setEnableKeyInputHandling(false);
	
	String file = "res/[Official Video] Daft Punk - Pentatonix.mp4";
	
	emp.prepareMedia(file);
	emp.play();
	}

}
