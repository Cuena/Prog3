package vlcj;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

public class EjemploVLCJ extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private static EjemploVLCJ miVentana;
	private EmbeddedMediaPlayerComponent mediaPlayerComponent;

	public EjemploVLCJ() {
		setTitle("Prueba vlcj");
		setSize(800, 600);
		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		add( mediaPlayerComponent, BorderLayout.CENTER );
		JPanel pBotonera = new JPanel();
		JButton bPlayPausa = new JButton( "Play/Pausa" );
		pBotonera.add( bPlayPausa );
		add( pBotonera, BorderLayout.SOUTH );
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				mediaPlayerComponent.getMediaPlayer().stop();
				mediaPlayerComponent.getMediaPlayer().release();
			}
		});
		bPlayPausa.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (mediaPlayerComponent.getMediaPlayer().isPlaying())
					mediaPlayerComponent.getMediaPlayer().pause();
				else
					mediaPlayerComponent.getMediaPlayer().play();
			}
		});
	}

	private void lanza(String mrl) {
		mediaPlayerComponent.getMediaPlayer().setVolume( 100 );
		mediaPlayerComponent.getMediaPlayer().playMedia(mrl);
	}

	public static void main(String[] args) {
		boolean found = new NativeDiscovery().discover();
    	if (!found) System.setProperty("jna.library.path", "C:/Program Files/VideoLAN/VLC");
		miVentana = new EjemploVLCJ();
		miVentana.lanza(
				"res/[Official Video] Daft Punk - Pentatonix.mp4"
				);
	}
	
}