package grupo19.colereLNServer;

import Colere.*;
import Colere.Exceptions.CoordenadasInvalidas;
import Colere.Exceptions.GestorInexistente;
import Colere.Exceptions.LocalInexistente;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootApplication
@RestController
@CrossOrigin
public class ColereLnServerApplication {

	private IColereFacade colereFacade = new ColereFacade("root","root");

	public ColereLnServerApplication() throws SQLException {}

	public static void main(String[] args) {
		SpringApplication.run(ColereLnServerApplication.class, args);
	}


	/**
	 * Método Handler para pedido GET "/locais".
	 * Devolve todos os locais para serem apresentados no mapa.
	 * @return Lista de todos os locais.
	 */
	@GetMapping("/locais")
	public List<Local> allLocais() {
		return colereFacade.pesquisarLocais(new Localizacao(),10000);
	}


	/**
	 * Método Handler para pedido POST "/locais".
	 * Avalia um Local.
	 * @param post Argumentos do pedido POST.
	 * @return String de confirmação.
	 */
	@PostMapping("/locais")
	public String avaliaLocal(@RequestBody PostObject post)
	{
		try{
			colereFacade.avaliarLocal(post.getNomeLocal(), post.getIp(), Integer.parseInt(post.getClassificacao()));
			return "OK";
		}catch (LocalInexistente |SQLException e){
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,e.getMessage());
		}

	}

	/**
	 * Método Handler para pedido GET "/locais/percurso/avaliacao".
	 * Devolve a lista de N locais que seguem o critério de avaliacao
	 * @return Lista de locais
	 */
	@GetMapping("/locais/percurso/avaliacao")
	public List<Local> percursoAvaliacao(@RequestParam (value = "nLocais") String nLocais) {
		return colereFacade.pedirPercursoAvaliacao(Integer.parseInt(nLocais));
	}

	/**
	 * Método Handler para pedido GET "/locais/percurso/proximidade".
	 * Devolve a lista de N locais que seguem o critério de proximidade do utilizador
	 * @return Lista de locais
	 */
	@GetMapping("/locais/percurso/proximidade")
	public List<Local> percursoAvaliacao(@RequestParam (value = "nLocais") String nLocais,
										 @RequestParam (value = "latitude") String lat,
										 @RequestParam (value = "longitude") String lon)
	{
		try {
			Localizacao user = new Localizacao("User location",Double.parseDouble(lat),Double.parseDouble(lon));
			return colereFacade.pedirPercursoDistancia(Integer.parseInt(nLocais),user);
		}catch (CoordenadasInvalidas e){
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,e.getMessage());
		}
	}

	/**
	 * Método Handler para pedido POST "/login".
	 * Efetua o login do Gestor.
	 * @param body Argumentos do pedido POST.
	 * @return String de confirmação.
	 */
	@PostMapping("/login")
	public String login(@RequestBody PostObject body) {
		if (colereFacade.loginGestor(body.getEmail(), body.getPassword())) {
			return "OK";
		}
		else {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"Login Inválido");
		}
	}

	/**
	 * Método Handler para pedido GET "/menuGestor".
	 * Retorna o Local associado ao Gestor.
	 * @param email Email do Gestor.
	 * @return Local do Gestor.
	 */
	@GetMapping("/menuGestor")
	public Local getLocalGest(@RequestParam(value = "email") String email) {
		try{
			return colereFacade.getLocalGestor(email);
		} catch (GestorInexistente | LocalInexistente e){
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,e.getMessage());
		}

	}


	/**
	 * Método Handler para pedido POST "/menuGestor".
	 * Efetua modificações no Local associado ao Gestor.
	 * @param body Argumentos do pedido POST.
	 * @return String de confirmação.
	 */
	@PostMapping("/menuGestor")
	public String editCommand(@RequestBody PostObject body){

		try {
			LocalDateTime data = null;
			if (!body.getData().isEmpty()){
				data = LocalDateTime.parse(body.getData().replace("T"," "), DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss"));
			}

			switch (body.getComando()) {
				case "adicionarEvento":
					colereFacade.adicionarEvento(body.getEmail(), body.getNomeEvento(), data, body.getDescricao());
					break;

				case "editarEvento":
					colereFacade.editarEvento(body.getEmail(),body.getNomeEventoAntes(), body.getNomeEventoDepois(),data, body.getDescricao());
					break;

				case "removerEvento":
					colereFacade.removerEvento(body.getEmail(), body.getNomeEvento());
					break;

				case "editarLocal":
					colereFacade.editarLocal(body.getEmail(), body.getDescricao(), body.getHoraAbertura(), body.getHoraFecho(), body.getWebsite());
					break;

				default:
					throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Comando inválido");
			}
		}catch (Exception e){
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
		}

		return "OK";
	}

}
