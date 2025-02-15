package es.florida.Miniproyecto3_SpringBoot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/APIpelis") // Todas las peticiones comenzarán por esto
public class ControllerMain {

	public static String rutaPelis = "./target/pelis";
	public static JSONArray arrayPeliculas = new JSONArray();
	public static JSONObject jsonAMostrar = new JSONObject(); // Json para mostrar en la petición.

	public static String leerPeliculas(String rutaPelis) throws IOException {
		System.out.println("Entra en leer peliculas.");
		int id = 1;
		File carpetaPelis = new File(rutaPelis);
		File[] tituloPelis = carpetaPelis.listFiles();

		for (File peli : tituloPelis) { // Recorre tantos txt de la carpeta pelis como haya.
			try (BufferedReader br = new BufferedReader(new FileReader(peli))) {
				String titulo = br.readLine(); // Con esto recojo la primera línea de cada fichero.
				titulo = titulo.replace("Titulo:", ""); // Elimina Titulo: para que no se muestre dos veces.
				JSONObject jsonPelicula = new JSONObject();
				jsonPelicula.put("id", id);
				jsonPelicula.put("titulo", titulo);
				arrayPeliculas.put(jsonPelicula);
				id++;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		jsonAMostrar.put("titulos", arrayPeliculas);

		return jsonAMostrar.toString();
	}

	public static String buscarPoId(int id) throws IOException { // .getJSONObject(x) recojes de un JSONArray el objeto
																	// que esté en la posición de la x(indice)
		JSONObject jsonPeliculas = new JSONObject(leerPeliculas(rutaPelis));
		JSONArray arrayPeliculas = jsonPeliculas.getJSONArray("titulos");

		for (int i = 0; i < arrayPeliculas.length(); i++) {
			JSONObject peliculaSeleccionada = arrayPeliculas.getJSONObject(i);
			if (peliculaSeleccionada.getInt("id") == id) {
				File peliConReseña = new File(rutaPelis + "/" + id + ".txt");
				System.out.println("Pelicula que coincide con el id: " + peliConReseña);

				JSONArray reseñas = new JSONArray();
				try (BufferedReader br = new BufferedReader(new FileReader(peliConReseña))) {
					br.readLine(); // Salta la primera línea que es el título.
					String reseña;
					while ((reseña = br.readLine()) != null) { // En este while sin crear un JSONObject se imprimen
																// directamente las reseñas como estan en el txt.
						reseñas.put(reseña);
					}
//					while((reseña = br.readLine()) != null) {	//De esta forma se agragarian como objetos Json y habria que poner el nombre y el valor
//						JSONObject reseña = new JSONObject();
//			            reseña.put("usuario", reseñaPorUsuario[0]); 		"usuario" : "Usuario1"
//			            reseña.put("comentario", reseñaPorUsuario[1]); 		"comentario" : "reseña que escribio el usuario 1"
//			            reseñas.put(reseña);
//					  
//			        }
				}
				peliculaSeleccionada.put("reseñas", reseñas);
				return peliculaSeleccionada.toString();
			}
		}
		return "La pelicula no existe.";
	}

	public static void insertarReseña(String usuario, String id, String textoReseña) {
		String rutaFichero = "./target/pelis/" + id + ".txt";

		try {
			File fichero = new File(rutaFichero);

			if (!fichero.exists()) {
				System.out.println("El fichero al que intentas acceder no existe.");
				return;
			}

			FileWriter fw = new FileWriter(fichero, true); // Con el true hago que el contenido que se escriba se agrupe
															// al final del archivo sin borrar lo que habia antes.
			BufferedWriter bw = new BufferedWriter(fw);
			bw.newLine();// Con esto me aseguro que escribirá si o si en una línea nueva.
			bw.write(usuario + ": " + textoReseña);

			bw.close();
			fw.close();
			System.out.println("Reseña del usuario: " + usuario + "añadida a la pelicula con id: " + id);
		} catch (IOException e) {
			System.out.println("Error al escribir la reseña: " + e.getMessage());
		}
	}

	public static void insertarNuevaPeli(String titulo) throws IOException {
		String rutaDirectorio = "./target/pelis";
		File directorio = new File(rutaDirectorio);

		String[] peliculas = directorio.list();
		int nuevoNum = 1;
		int numeroMax = 0;

		for (String pelicula : peliculas) {
			int numPeli = Integer.parseInt(pelicula.replace(".txt", "")); // Recorro todas los nombres de los .txt y
																			// quito la extensión para poder comparar
																			// los numero
			if (numPeli > numeroMax) { // Si el numero de la pelicula que lee es mayor al número máximo acumulado en
										// numMax los iguala
				numeroMax = numPeli; // y vuelve a hacer el ciclo hasta que sea igual qu eentonces sale del bucle y
										// se le suma 1.
			}
		}
		nuevoNum = numeroMax + 1; // Este es el número que tendrá la nueva pelicula.

		File nuevaPeli = new File(rutaDirectorio + "/" + nuevoNum + ".txt");
		FileWriter fw = new FileWriter(nuevaPeli);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("Título: " + titulo);
		bw.flush();
		bw.close();
	}

	public static boolean esAutorizado(String usuario) {
		String rutaFichero = "./target/autorizados.txt";

		try (BufferedReader br = new BufferedReader(new FileReader(rutaFichero))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				if (linea.equals(usuario)) {
					return true;
				}
			}
		} catch (IOException e) {
			System.out.println("Error al verificar el usuario: " + e.getMessage());
		}
		System.out.println("El usuario no está autorizado.");
		return false;
	}
	
	public static void insertarNuevoUser(String usuario) {
		String rutaFichero = "./target/autorizados.txt";
		
		try {
			File fichero = new File(rutaFichero);

			if (!fichero.exists()) {
				System.out.println("El fichero al que intentas acceder no existe.");
				return;
			}

			FileWriter fw = new FileWriter(fichero, true); // Con el true hago que el contenido que se escriba se agrupe
															// al final del archivo sin borrar lo que habia antes.
			BufferedWriter bw = new BufferedWriter(fw);
			bw.newLine();// Con esto me aseguro que escribirá si o si en una línea nueva.
			bw.write(usuario);

			bw.close();
			fw.close();
			System.out.println("Usuario añadido a autorizados.txt");
		} catch (IOException e) {
			System.out.println("Error al escribir la reseña: " + e.getMessage());
		}
	}

	@GetMapping("/t")
	String mostrarTodas(@RequestParam(value = "id") String valor) throws IOException {
		if (valor.equals("all")) {
			return leerPeliculas(rutaPelis);
		}

		try {
			int id = Integer.parseInt(valor);
			return buscarPoId(id);
		} catch (NumberFormatException e) {
			return "ID no valido";
		}

	}

	@PostMapping("/nuevaReseña")
	ResponseEntity<Object> postBodyReseña(@RequestBody String cuerpoPeticion) throws IOException {
		JSONObject reseñaJson = new JSONObject(cuerpoPeticion);

		String usuario = reseñaJson.getString("usuario");
		String id = reseñaJson.getString("id");
		String textoReseña = reseñaJson.getString("reseña");
		if (esAutorizado(usuario)) {
			insertarReseña(usuario, id, textoReseña);

			System.out.println("Reseña de " + usuario + " sobre película " + id + ": " + textoReseña);

			return ResponseEntity.noContent().header("Content-Length", "0").build();
		}
		return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	}

	@PostMapping("/nuevaPeli")
	ResponseEntity<Object> postBodyNuevaPeli(@RequestBody String cuerpoPeticion) throws IOException {
		JSONObject nuevaPeliJson = new JSONObject(cuerpoPeticion);

		String usuario = nuevaPeliJson.getString("usuario");
		String titulo = nuevaPeliJson.getString("titulo");

		if (esAutorizado(usuario)) {
			insertarNuevaPeli(titulo);

			System.out.println("Nueva peli insertada por el usuario: " + usuario + "Con el título: " + titulo);

			return ResponseEntity.noContent().header("Content-Length", "0").build();
		}
		return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	}
	
	@PostMapping("/nuevoUsuario")
	ResponseEntity<Object> postBodyNuevoUser(@RequestBody String cuerpoPeticion) throws IOException {
		JSONObject nuevoUser = new JSONObject(cuerpoPeticion);
		
		String usuario = nuevoUser.getString("usuario");
		
		insertarNuevoUser(usuario);
		System.out.println("Nueva Usuario " + usuario + " insertado!");

		return ResponseEntity.noContent().header("Content-Length", "0").build();
	}

}
