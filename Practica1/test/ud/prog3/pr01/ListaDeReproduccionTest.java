package ud.prog3.pr01;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import vlcj.ListaDeReproduccion;

public class ListaDeReproduccionTest {

	
	private ListaDeReproduccion lr1;
	private ListaDeReproduccion lr2;
	private ListaDeReproduccion lr3;
	
	private final File FIC_TEST1 = new File( "res/No del grupo.mp4" );
	private final File FIC_TEST2 = new File( "res/[Official Video] Draft Punk - Pentatonix.mp4" );
	private final File FIC_TEST3 = new File("res/video prueba 1.mp4");
	private final File FIC_TEST4 = new File("res/ video prueba 2.mp4");
	private final File FIC_TEST5 = new File("Yann Tiersen - Porz Goret.mp3");
	 
	 @Before
	 public void setUp() throws Exception {
	 lr1 = new ListaDeReproduccion(); 
	 lr2 = new ListaDeReproduccion();
	 lr3 = new ListaDeReproduccion();
	 
	 lr2.add( FIC_TEST1 );
	 lr2.add(FIC_TEST2);
	 lr2.add(FIC_TEST3);
	 
	 
	
	 } 
	 
	 @After
	 
	 public void tearDown() {
	 lr2.clear();
	 } 
	 
	// Chequeo de error por getFic(índice) por encima de final
	 @Test(expected = IndexOutOfBoundsException.class)
	 public void testGet_Exc1() {
	 lr1.getFic(0); // Debe dar error porque aún no existe la posición 0
	 }

	 // Chequeo de error por get(índice) por debajo de 0
	 @Test(expected = IndexOutOfBoundsException.class)
	 public void testGet_Exc2() {
	 lr2.getFic(-1); // Debe dar error porque aún no existe la posición -1
	 }

	 // Chequeo de funcionamiento correcto de get(índice)
	 @Test public void testGet() {
	 assertEquals( FIC_TEST1, lr2.getFic(0) ); // El único dato es el fic-test1
	 } 
	 
	 //Chequeo de intercambio
	 @Test public void testTintercambia() {
		lr2.intercambia(0, 1);	
		assertEquals(FIC_TEST2, lr2.getFic(0));		 
	 }
	 
	 //Chequeo de añadido y borrado
	 
	 @Test public void testAdd() {
		 //se añaden dos ficheros en @Before
		 assertEquals (FIC_TEST2,lr2.getFic(1));	 
	 } 
	 //Chequeo de tamaño
	 @Test public void testSize() {
		 assertEquals (0,lr3.size());
		 assertEquals (3,lr2.size());
	 }
	 
	 @Test public void testAddCarpeta() {
		 
		 String carpetaTest = "res";
		 String filtroTest = "*Pentatonix*.mp4"; 

		 ListaDeReproduccion lr = new ListaDeReproduccion();
		 int n = lr.add( carpetaTest, filtroTest );
		 assertEquals(3,n );
//		 fail( "Método sin acabar" );
		 }
	 
	 @Test public void testIrARandom() {
		 
		 assertEquals(true, lr2.irARandom());
		 
	 }
	 
	 
//	@Test
//	public void test() {
//		fail("Not yet implemented");
//	}

}
