package ae3.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

	@GetMapping("APIpelis/t")
	public ResponseEntity<String> obtenerInfo(@RequestParam(value = "id") String id) {
		String json = generarJson(id);
		if (json.equals("")) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(json);
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(json);
		}
	}

	@PostMapping("APIpelis/nuevaResenya")
	ResponseEntity<Object> nuevaResenya(@RequestBody String cuerpoPeticion) {
		JSONObject obj = new JSONObject(cuerpoPeticion);
		String usuario = (String) obj.get("usuario");
		String id = (String) obj.get("id");
		String resenya = (String) obj.get("resenya");
		if (usuarioAutorizado(usuario)) {
			insertarResenya(usuario, id, resenya);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

	}

	@PostMapping("APIpelis/nuevaPeli")
	ResponseEntity<Object> nuevaPeli(@RequestBody String cuerpoPeticion) {
		JSONObject obj = new JSONObject(cuerpoPeticion);
		String usuario = (String) obj.get("usuario");
		String titulo = (String) obj.get("titulo");
		if (usuarioAutorizado(usuario)) {
			crearPeli(usuario, titulo);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	@PostMapping("APIpelis/nuevoUsuario")
	ResponseEntity<Object> nuevoUsuario(@RequestBody String cuerpoPeticion) {
		JSONObject obj = new JSONObject(cuerpoPeticion);
		String usuario = (String) obj.get("usuario");
		registrarUsuario(usuario);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

	}

	private String generarJson(String id) {
		String json = "";
		if (id.equals("all")) {
			File directorio = new File("pelis");
			String[] listaFicheros = directorio.list(new FiltroExtension(".txt"));
			json = "{\"titulos\": [";
			for (int i = 0; i < listaFicheros.length; i++) {
				try {
					File ficheroActual = new File(directorio.getAbsolutePath() + File.separator + listaFicheros[i]);
					FileReader fr = new FileReader(ficheroActual);
					BufferedReader br = new BufferedReader(fr);
					String titulo = br.readLine().split("Titulo: ")[1];
					json += "{\"id\": \"" + listaFicheros[i].substring(0, listaFicheros[i].length() - 4)
							+ "\", \"titulo\": \"" + titulo + "\"}";
					if (i != (listaFicheros.length - 1)) {
						json += ",";
					}
					br.close();
					fr.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			json += "]}";
		} else {
			File ficheroPeli = new File("pelis/" + id + ".txt");
			if (ficheroPeli.exists()) {
				json = "{\"id\": \"" + id + "\",";
				try {
					FileReader fr = new FileReader(ficheroPeli);
					BufferedReader br = new BufferedReader(fr);
					String linea = br.readLine().split("Titulo: ")[1];
					json += "\"titulo\": \"" + linea + "\", ";
					json += "\"resenyas\": [";
					while ((linea = br.readLine()) != null) {
						json += "\"" + linea + "\",";
					}
					if (json.charAt(json.length() - 1) == ',') {
						json = json.substring(0, json.length() - 1); // Quitar ultima coma
					}
					br.close();
					fr.close();
				} catch (Exception e) {
					json = "Error";
					e.printStackTrace();
				}
				json += "]}";
			}
		}
		return json;
	}

	private void insertarResenya(String usuario, String id, String resenya) {
		File ficheroPeli = new File("pelis/" + id + ".txt");
		try {
			FileWriter fw = new FileWriter(ficheroPeli, true);
			fw.write("\n" + usuario + ": " + resenya);
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void crearPeli(String usuario, String titulo) {
		File directorio = new File("pelis");
		String[] listaFicheros = directorio.list(new FiltroExtension(".txt"));
		int idUltimo = listaFicheros.length;
		try {
			FileWriter fw = new FileWriter(new File("pelis" + File.separator + (idUltimo + 1) + ".txt"));
			fw.write("Titulo: " + titulo);
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private boolean usuarioAutorizado(String usuario) {
		boolean autorizado = false;
		try {
			FileReader fr = new FileReader("autorizados.txt");
			BufferedReader br = new BufferedReader(fr);
			String linea = "";
			while ((linea = br.readLine()) != null) {
				if (linea.equals(usuario)) {
					autorizado = true;
					break;
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return autorizado;
	}
	
	private void registrarUsuario(String usuario) {
		try {
			FileWriter fw = new FileWriter("autorizados.txt", true);
			fw.write("\n" + usuario);
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
