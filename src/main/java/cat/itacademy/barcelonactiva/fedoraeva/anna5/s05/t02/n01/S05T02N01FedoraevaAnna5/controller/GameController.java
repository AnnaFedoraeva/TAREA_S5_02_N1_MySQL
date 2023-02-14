package cat.itacademy.barcelonactiva.fedoraeva.anna5.s05.t02.n01.S05T02N01FedoraevaAnna5.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.itacademy.barcelonactiva.fedoraeva.anna5.s05.t02.n01.S05T02N01FedoraevaAnna5.model.domain.Game;
import cat.itacademy.barcelonactiva.fedoraeva.anna5.s05.t02.n01.S05T02N01FedoraevaAnna5.model.services.UserService;
import cat.itacademy.barcelonactiva.fedoraeva.anna5.s05.t02.n01.S05T02N01FedoraevaAnna5.model.utils.AvSuccesRate;

@RestController
@RequestMapping("/players")

public class GameController {

	@Autowired
	UserService userService;

	// POST /players/{id}/games: un jugador/a específico realiza un tirón de los
	// dados.
	@PostMapping("/{id}/games")
	public ResponseEntity<?> playGame(@PathVariable(value = "id") Long id) {
		try {
			// String username = userService.findById(id).getUsername();
			Game game = userService.rollDice(id);

			return new ResponseEntity<>(game, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// DELETE /players/{id}/games: elimina las tiradas del jugador/a.
	@DeleteMapping("/{id}/games")
	public ResponseEntity<?> deleteGames(@PathVariable(value = "id") Long id) {
		try {
			String username = userService.findById(id).getUsername();
			List<Game> games = userService.findGamesByUser(id);
			if (games.isEmpty()) {
				return new ResponseEntity<>("Player " + username + " has not played any game yet.", HttpStatus.OK);
			}

			userService.deleteGamesByUser(id);

			return new ResponseEntity<>("All games of user: " + username + " has been deleted", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// GET /players/: returns the list of all players in the system with their
	// average success rate.
	@GetMapping("/rate")
	public List<AvSuccesRate> getUserByAvSucRate() {
		return userService.listRate();
	}

	// GET /players/{id}/games: returns the list of plays by a player.
	@GetMapping("/{id}/games")
	public ResponseEntity<?> getGamesByPlayer(@PathVariable Long id) {
		try {
			userService.findById(id);
			List<Game> games = userService.gamesByPlayer(id);
			if (games.isEmpty()) {
				return new ResponseEntity<>("The user has not played yet.", HttpStatus.OK);
			}

			return new ResponseEntity<>(games, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// GET /players/ranking: returns the average ranking of all players in the
		// system. That is, the average percentage of achievements.
		
	@GetMapping("/ranking")
	public ResponseEntity<?> getRanking () {
		return new ResponseEntity<> (userService.getAverageRanking(), HttpStatus.OK);
	}
	
	//GET /players/ranking/loser: returns the player with the worst success rate.
	@GetMapping("/loser")
	public ResponseEntity<?> getLoser() {
		return new ResponseEntity<> (userService.getLoser(), HttpStatus.OK);
		
	}
	
	//GET /players/ranking/winner: returns the player with the worst success rate.
	@GetMapping("/winner")
	public ResponseEntity<?> getWinner() {
		return new ResponseEntity<> (userService.getWinner(), HttpStatus.OK);
		
	}
}
