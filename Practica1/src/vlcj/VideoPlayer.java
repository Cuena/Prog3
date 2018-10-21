package vlcj;

import java.awt.*;
import java.awt.event.*;


import javax.swing.ImageIcon;
import javax.swing.*;
import java.io.File;
import javax.imageio.*;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.windows.Win32FullScreenStrategy;

/** Ventana principal de reproductor de vídeo
 * Utiliza la librería VLCj que debe estar instalada y configurada
 *     (http://www.capricasoftware.co.uk/projects/vlcj/index.html)
 * @author Andoni Eguíluz Morán
 * Facultad de Ingeniería - Universidad de Deusto
 */
public class VideoPlayer extends JFrame {
	private static final long serialVersionUID = 1L;
	
	// Varible de ventana principal de la clase
	private static VideoPlayer miVentana;

	// Atributo de VLCj
	private EmbeddedMediaPlayerComponent mediaPlayerComponent;
	// Atributos manipulables de swing
	private JList<String> lCanciones = null;  // Lista vertical de vídeos del player
	private JProgressBar pbVideo = null;      // Barra de progreso del vídeo en curso
	private JCheckBox cbAleatorio = null;     // Checkbox de reproducción aleatoria
	private JLabel lMensaje = null;           // Label para mensaje de reproducción
	// Datos asociados a la ventana
	private ListaDeReproduccion listaRepVideos;  // Modelo para la lista de vídeos

	public VideoPlayer() {
		// Creación de datos asociados a la ventana (lista de reproducción)
		listaRepVideos = new ListaDeReproduccion();
		
		// Creación de componentes/contenedores de swing
		lCanciones = new JList<String>( listaRepVideos );
		pbVideo = new JProgressBar( 0, 10000 );
		cbAleatorio = new JCheckBox("Rep. aleatoria");
		lMensaje = new JLabel( "" );
		JPanel pBotonera = new JPanel();
		//NO FUNCIONABA DE LA OTRA FORMA
		JButton bAnyadir = new JButton(new ImageIcon( VideoPlayer.class.getClassLoader().getResource("imgs/Button Add.png")));
		JButton bAtras = new JButton( new ImageIcon( VideoPlayer.class.getClassLoader().getResource("imgs/Button Rewind.png")) );
		JButton bPausaPlay = new JButton( new ImageIcon( VideoPlayer.class.getClassLoader().getResource("imgs/Button Play Pause.png")) );
		JButton bAdelante = new JButton( new ImageIcon( VideoPlayer.class.getClassLoader().getResource("imgs/Button Fast Forward.png")) );
		JButton bMaximizar = new JButton( new ImageIcon( VideoPlayer.class.getClassLoader().getResource("imgs/Button Maximize.png")) );
		
		// Componente de VCLj
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent() {
			private static final long serialVersionUID = 1L;
			@Override
            protected FullScreenStrategy onGetFullScreenStrategy() {
                return new Win32FullScreenStrategy(VideoPlayer.this);
            }
        };

		// Configuración de componentes/contenedores
		setTitle("Video Player - Deusto Ingeniería");
		setLocationRelativeTo( null );  // Centra la ventana en la pantalla
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		setSize( 800, 600 );
		lCanciones.setPreferredSize( new Dimension( 200,  500 ) );
		pBotonera.setLayout( new FlowLayout( FlowLayout.LEFT ));
		
		// Enlace de componentes y contenedores
		pBotonera.add( bAnyadir );
		pBotonera.add( bAtras );
		pBotonera.add( bPausaPlay );
		pBotonera.add( bAdelante );
		pBotonera.add( bMaximizar );
		pBotonera.add( cbAleatorio );
		pBotonera.add( lMensaje );
		getContentPane().add( mediaPlayerComponent, BorderLayout.CENTER );
		getContentPane().add( pBotonera, BorderLayout.NORTH );
		getContentPane().add( pbVideo, BorderLayout.SOUTH );
		getContentPane().add( new JScrollPane( lCanciones ), BorderLayout.WEST );
		
		// Escuchadores
		bAnyadir.addActionListener( new ActionListener() { //añade el fichero seleccionado de cualquier formato
			@Override
			public void actionPerformed(ActionEvent e) {
				File fPath = pedirCarpeta();
				if (fPath==null) return;
				path = fPath.getAbsolutePath();
				
				ficheros = fPath.getName();
				System.out.println(ficheros);
				System.out.println(path);
				listaRepVideos.add(fPath);
				listaRepVideos.add(path, ficheros);
				lCanciones.repaint();
			}
		});
		bAtras.addActionListener( new ActionListener() { // no tiene mucho sentido que al ir para atras se aleatorio
			@Override
			public void actionPerformed(ActionEvent e) {
				paraVideo();
				listaRepVideos.irAAnterior();
				lanzaVideo();
			}
		});
		bAdelante.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				paraVideo();
				if (cbAleatorio.isSelected() && listaRepVideos.irARandom()) {
					lanzaVideo();
					
				}else {
					
				listaRepVideos.irASiguiente();
				lanzaVideo();
				}
			}
		});
		bPausaPlay.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (mediaPlayerComponent.getMediaPlayer().isPlayable()) {
					if (mediaPlayerComponent.getMediaPlayer().isPlaying()) {
						// TODO: hacer pausa
						mediaPlayerComponent.getMediaPlayer().pause();
					} else {
						// TODO: hacer play
						mediaPlayerComponent.getMediaPlayer().play();
					}
				} else {
					lanzaVideo();
				}
			}
		});
		bMaximizar.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (mediaPlayerComponent.getMediaPlayer().isFullScreen())
			        mediaPlayerComponent.getMediaPlayer().setFullScreen(false);
				else
					mediaPlayerComponent.getMediaPlayer().setFullScreen(true);
			}
		});
		
		
		pbVideo.addMouseListener(new MouseListener() {
			
			
			@Override
			public void mouseClicked(MouseEvent e) { 
				
	
				 long len = mediaPlayerComponent.getMediaPlayer().getLength();
				 int v = pbVideo.getValue();
			       

			       //Retrieves the mouse position relative to the component origin.
			       int mouseX = e.getX();

			       //Computes how far along the mouse is relative to the component width then multiply it by the progress bar's maximum value.
			       int progressBarVal = (int)Math.round(((double)mouseX / (double)pbVideo.getWidth()) * pbVideo.getMaximum());
			       
			      
			       int po = progressBarVal/100;
			       System.out.println(po);
			       long irA = (len*po)/100;
			      
			       //pbVideo.setValue(progressBarVal);
			       mediaPlayerComponent.getMediaPlayer().setTime(irA);
			  } 
			

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				mediaPlayerComponent.getMediaPlayer().stop();
				mediaPlayerComponent.getMediaPlayer().release();
			}
		});
		mediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener( 
			new MediaPlayerEventAdapter() {
				@Override
				public void finished(MediaPlayer mediaPlayer) {
					listaRepVideos.irASiguiente();
					lanzaVideo();
				}
				@Override
				public void error(MediaPlayer mediaPlayer) {
					listaRepVideos.irASiguiente();
					lanzaVideo();
					lCanciones.repaint();
				}
			    @Override
			    public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
					pbVideo.setValue( (int) (10000.0 * 
							mediaPlayerComponent.getMediaPlayer().getTime() /
							mediaPlayerComponent.getMediaPlayer().getLength()) );
					pbVideo.repaint();
			    }
		});
	}

	//
	// Métodos sobre el player de vídeo
	//
	
	// Para la reproducción del vídeo en curso
	private void paraVideo() {
		if (mediaPlayerComponent.getMediaPlayer()!=null)
			mediaPlayerComponent.getMediaPlayer().stop();
	}

	// Empieza a reproducir el vídeo en curso de la lista de reproducción
	private void lanzaVideo() {
		if (mediaPlayerComponent.getMediaPlayer()!=null &&
			listaRepVideos.getFicSeleccionado()!=-1) {
			File ficVideo = listaRepVideos.getFic(listaRepVideos.getFicSeleccionado());
			mediaPlayerComponent.getMediaPlayer().playMedia( 
				ficVideo.getAbsolutePath() );
			lCanciones.setSelectedIndex( listaRepVideos.getFicSeleccionado() );
			
			date = DATE_FORMAT.format(ficVideo.lastModified());
			lMensaje.setText(date);
		} else {
			lCanciones.setSelectedIndices( new int[] {} );
		}
	}
	
	String date;
	SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
	
	
	// Pide interactivamente una carpeta para coger vídeos
	// (null si no se selecciona)
	
	private static File pedirCarpeta() {
		// TODO: Pedir la carpeta usando JFileChooser
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	
		int returnValue = fileChooser.showOpenDialog(null);
		
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			return selectedFile;
		}
		
		return null;
	}

		private static String ficheros;
		private static String path;
	/** Ejecuta una ventana de VideoPlayer.
	 * El path de VLC debe estar en la variable de entorno "vlc".
	 * Comprobar que la versión de 32/64 bits de Java y de VLC es compatible.
	 * @param args	Un array de dos strings. El primero es el nombre (con comodines) de los ficheros,
	 * 				el segundo el path donde encontrarlos.  Si no se suministran, se piden de forma interactiva. 
	 */
	public static void main(String[] args) {
		// Para probar carga interactiva descomentar o comentar la línea siguiente:
		//args = new String[] { "*Pentatonix*.mp4", "C:\\Users\\kikel_000\\lemon\\Practica01\\res" };
		if (args.length < 2) {
			// No hay argumentos: selección manual
			
			//-----------------------------------------------------------------------------------------------
			//voy a pedir el formato de archivo al iniciar el programa
			//se añadiran a la lista los archivos de la carpeta seleccionada que sean del formato introducido
			//con el boton añadir se pueden meter todo tipo de archivos
			//-----------------------------------------------------------------------------------------------
			
			
			ficheros = JOptionPane.showInputDialog("Introducir formato ej: mp3,mp4\ny luego elegir la carpeta"); //se podria hacer con nombre de artiasta o fichero
			File fPath = pedirCarpeta();
			if (fPath==null) return;
			path = fPath.getAbsolutePath();
			
			ficheros = "*." + ficheros;
			//ficheros = "*.mp4";
			System.out.println(ficheros);
			System.out.println(path);
			
		} else {
			ficheros = args[0];
			path = args[1];
		}
		
		// Inicializar VLC.
		// Probar con el buscador nativo...
		boolean found = new NativeDiscovery().discover();
    	// System.out.println( LibVlc.INSTANCE.libvlc_get_version() );  // Visualiza versión de VLC encontrada
    	// Si no se encuentra probar otras opciones:
    	if (!found) {
			// Buscar vlc como variable de entorno
			String vlcPath = System.getenv().get( "vlc" );
			if (vlcPath==null) {  // Poner VLC a mano
	        	System.setProperty("jna.library.path", "C:/Program Files/VideoLAN/VLC");
			} else {  // Poner VLC desde la variable de entorno
				System.setProperty( "jna.library.path", vlcPath );
			}
		}
    	
    	// Lanzar ventana
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				miVentana = new VideoPlayer();
				// Descomentar estas dos líneas para ver un vídeo de ejemplo
				// miVentana.listaRepVideos.ficherosLista = new ArrayList<File>();
				// miVentana.listaRepVideos.ficherosLista.add( new File("test/res/[Official Video] Daft Punk - Pentatonix.mp4") );				
				miVentana.setVisible( true );
				miVentana.listaRepVideos.add( path, ficheros );
				
				
				
				
				miVentana.listaRepVideos.irAPrimero();
				miVentana.lanzaVideo();
			}
		});
	}
	
}
