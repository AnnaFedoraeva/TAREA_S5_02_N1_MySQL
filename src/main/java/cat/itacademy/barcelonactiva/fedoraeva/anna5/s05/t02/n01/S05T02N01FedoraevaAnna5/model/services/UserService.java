package cat.itacademy.barcelonactiva.fedoraeva.anna5.s05.t02.n01.S05T02N01FedoraevaAnna5.model.services;

import java.util.List;

import cat.itacademy.barcelonactiva.fedoraeva.anna5.s05.t02.n01.S05T02N01FedoraevaAnna5.model.domain.Game;
import cat.itacademy.barcelonactiva.fedoraeva.anna5.s05.t02.n01.S05T02N01FedoraevaAnna5.model.domain.User;
import cat.itacademy.barcelonactiva.fedoraeva.anna5.s05.t02.n01.S05T02N01FedoraevaAnna5.model.utils.AvSuccesRate;

public interface UserService {

	public List<User> getUser();

	public void save(User user);

	public User findById(Long id);

	public void delete(User user);

	public boolean existedUsername(String username);

	public Game rollDice(Long id);

	public List<Game> findGamesByUser(Long id);

	public List<Game> deleteGamesByUser(Long id);

	public String getPlayerAvSucRate(User user);

	public List<AvSuccesRate> listRate();

	public List<Game> gamesByPlayer(Long id);

	public String getAverageRanking();

	public List<AvSuccesRate> getLoser();

	public List<AvSuccesRate> getWinner();

	public boolean existedEmail(String email);

}
