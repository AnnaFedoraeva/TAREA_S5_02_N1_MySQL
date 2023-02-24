package cat.itacademy.barcelonactiva.fedoraeva.anna5.s05.t02.n01.S05T02N01FedoraevaAnna5.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.itacademy.barcelonactiva.fedoraeva.anna5.s05.t02.n01.S05T02N01FedoraevaAnna5.model.domain.User;
import cat.itacademy.barcelonactiva.fedoraeva.anna5.s05.t02.n01.S05T02N01FedoraevaAnna5.model.services.UserService;

@RestController
@RequestMapping("/players")
public class UserController {

	@Autowired
	private UserService userService;

	public User saveUser(@RequestBody User user) {
		userService.save(user);
		user.setDate(LocalDateTime.now());
		return user;
	}

	// POST: /players: creates a player.

	@PostMapping("/")
	public ResponseEntity<?> createUser(@RequestBody User user) {
		if (user.getUsername() == null)
			user.setUsername("ANONYMOUS");

		if (userService.existedUsername(user.getUsername()) && (!user.getUsername().equalsIgnoreCase("ANONYMOUS")))
			return new ResponseEntity<>("A user with this username already exists", HttpStatus.BAD_REQUEST);

		if (userService.existedEmail(user.getEmail()))
			return new ResponseEntity<>("A user with this email already exists", HttpStatus.BAD_REQUEST);

		user.setDate(LocalDateTime.now());
		userService.save(user);
		return new ResponseEntity<>(
				"The user " + user.getUsername() + " has been successfully created with id: " + user.getId(),
				HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public User getUserById(@PathVariable Long id) {
		User user = userService.findById(id);
		return user;
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) {
		User user = userService.findById(id);
		userService.delete(user);
		return new ResponseEntity<>("The user " + user.getUsername() + " has been successfully deleted.",
				HttpStatus.OK);
	}

	@PutMapping("/updateAll/{id}")
	public User updateUser(@PathVariable Long id, @RequestBody User userDetail) {
		User user = userService.findById(id);
		user.setUsername(userDetail.getUsername());
		user.setEmail(userDetail.getEmail());
		user.setPassword(userDetail.getPassword());
		userService.save(user);
		return user;
	}

	// PUT /players: modifies the player's name.
	@PutMapping("/updateName/{id}")
	public ResponseEntity<?> updatePlayer(@RequestBody User user, @PathVariable Long id) {
		try {
			User userFound = userService.findById(id);
			userFound.setUsername(user.getUsername());
			userService.save(userFound);

			return new ResponseEntity<>("Username has been successfully updated.", HttpStatus.OK);
		} catch (DataIntegrityViolationException duplicateError) {
			return new ResponseEntity<>("A user with this username already exists.", HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>("Failed to update username.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
