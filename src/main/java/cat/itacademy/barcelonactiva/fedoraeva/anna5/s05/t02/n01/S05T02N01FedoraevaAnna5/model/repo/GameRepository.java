package cat.itacademy.barcelonactiva.fedoraeva.anna5.s05.t02.n01.S05T02N01FedoraevaAnna5.model.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cat.itacademy.barcelonactiva.fedoraeva.anna5.s05.t02.n01.S05T02N01FedoraevaAnna5.model.domain.Game;
import cat.itacademy.barcelonactiva.fedoraeva.anna5.s05.t02.n01.S05T02N01FedoraevaAnna5.model.domain.User;

public interface GameRepository extends JpaRepository<Game, Long> {

	public List<Game> findAllByUser(User user);

	public List<Game> findByUserId(Long id);

}
