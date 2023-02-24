package cat.itacademy.barcelonactiva.fedoraeva.anna5.s05.t02.n01.S05T02N01FedoraevaAnna5.model.services;

import java.util.ArrayList;



import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import cat.itacademy.barcelonactiva.fedoraeva.anna5.s05.t02.n01.S05T02N01FedoraevaAnna5.exception.UserNotFoundException;
import cat.itacademy.barcelonactiva.fedoraeva.anna5.s05.t02.n01.S05T02N01FedoraevaAnna5.model.domain.Game;
import cat.itacademy.barcelonactiva.fedoraeva.anna5.s05.t02.n01.S05T02N01FedoraevaAnna5.model.domain.User;
import cat.itacademy.barcelonactiva.fedoraeva.anna5.s05.t02.n01.S05T02N01FedoraevaAnna5.model.repo.GameRepository;
import cat.itacademy.barcelonactiva.fedoraeva.anna5.s05.t02.n01.S05T02N01FedoraevaAnna5.model.repo.UserRepository;
import cat.itacademy.barcelonactiva.fedoraeva.anna5.s05.t02.n01.S05T02N01FedoraevaAnna5.model.utils.AvSuccesRate;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private GameRepository gameRepo;

	@Override
	public List<User> getUser() {
		return userRepo.findAll();
	}

	@Override
	public void save(User user) {
		userRepo.save(user);
	}

	public User findById(Long id) {
		return userRepo.findById(id).orElseThrow(() -> new UserNotFoundException(id));
	}

	@Override
	public void delete(User user) {
		userRepo.delete(user);
	}

	@Override
	public boolean existedUsername(String username) {
		return userRepo.existsByUsername(username);
	}

	@Override
	public boolean existedEmail(String email) {
		return userRepo.existsByEmail(email);
	}

	@Override
	public Game rollDice(Long id) {
		User user = findById(id);
		int dice1 = randomNumbers();
		int dice2 = randomNumbers();
		int sumDices = dice1 + dice2;

		Game game = new Game();
		game.setDice1(dice1);
		game.setDice2(dice2);
		game.setUser(user);

		if (sumDices == 7) {
			game.setResult("win");
		} else {
			game.setResult("lose");
		}
		user.getGames().add(game);
		gameRepo.save(game);

		return game;
	}

	// generating random numbers via the ThreadLocalRandom class
	public int randomNumbers() {

		int randomNum = ThreadLocalRandom.current().nextInt(1, 6);

		return randomNum;
	}

	public List<Game> findGamesByUser(Long id) {
		User user = findById(id);
		List<Game> games = user.getGames();
		return games;
	}

	public List<Game> getGamesByUser(Long id) {
		User user = findById(id);
		List<Game> games = gameRepo.findAllByUser(user);
		return games;
	}

	public List<Game> deleteGamesByUser(Long id) {
		List<Game> games = getGamesByUser(id);
		gameRepo.deleteAll(games);
		User user = findById(id);
		user.getGames().removeAll(games);
		userRepo.save(user);

		return games;
	}

	public String getPlayerAvSucRate(User user) {
		String result = "The user has not played yet.";
		int numberGames = 0;
		List<Game> games = new ArrayList<Game>();
		if (user.getGames() != null && user.getGames().size() > 0) {
			numberGames = user.getGames().size();
			games = user.getGames();
			float gamesWon = user.getGames().stream().filter(g -> g.getDice1() + g.getDice2() == 7).count();
			float succesRate = (gamesWon / numberGames) * 100;
			result = String.valueOf(succesRate);
		}

		return result;

	}

	// java8:
	public List<AvSuccesRate> listRate() {
		List<User> allUsers = userRepo.findAll();
		List<AvSuccesRate> listRate = allUsers.stream().filter(user -> user.getGames().size() > 0).map(u -> {
			AvSuccesRate uRate = new AvSuccesRate();
			uRate.setId(u.getId());
			uRate.setUsername(u.getUsername());
			uRate.setRate(getPlayerAvSucRate(u));
			return uRate;
		}).collect(Collectors.toList());
		return listRate;
	}

	@Override
	public List<Game> gamesByPlayer(Long id) {

		return gameRepo.findByUserId(id);
	}

	public String getAverageRanking() {
		List<User> users = userRepo.findAll(Sort.by(Sort.Order.asc("username"))).stream().toList();
		String message;

		if (users.isEmpty()) {
			message = "Zero users have played.";
		} else {
			double totalSum = users.stream().map(this::getPlayerAvSucRate)
					.filter(rate -> !rate.equals("The user has not played yet.")).mapToDouble(Double::parseDouble)
					.reduce(0d, Double::sum);
			long count = users.stream().filter(user -> user.getGames().size() > 0).count();
			if (count == 0) {
				message = "There are no games recorded yet";
			} else {
				message = String.valueOf(totalSum / count);

			}
		}
		return message;
	}

	public List<AvSuccesRate> getLoser() {

		List<AvSuccesRate> users = listRate().stream().sorted((Comparator.comparing(AvSuccesRate::getRate)))
				.collect(Collectors.toList());

		List<AvSuccesRate> loser = new ArrayList<>();
		boolean isLow = true;
		while (isLow) {
			for (AvSuccesRate u : users) {
				if (users.get(0).getRate().equals(u.getRate())) {
					loser.add(u);
				}
				isLow = false;
			}

		}
		// java8
		// List<AvSuccesRate> loser = users.stream().filter(u1 ->
		// u1.getRate().equals(users.get(0).getRate())).collect(Collectors.toList());

		return loser;
	}

	public List<AvSuccesRate> getWinner() {

		List<AvSuccesRate> users = listRate().stream().sorted((Comparator.comparing(AvSuccesRate::getRate).reversed()))
				.collect(Collectors.toList());

		List<AvSuccesRate> winner = new ArrayList<>();
		boolean isLow = true;
		while (isLow) {
			for (AvSuccesRate u : users) {
				if (users.get(0).getRate().equals(u.getRate())) {
					winner.add(u);
				}
				isLow = false;
			}
		}

		return winner;
	}
}
