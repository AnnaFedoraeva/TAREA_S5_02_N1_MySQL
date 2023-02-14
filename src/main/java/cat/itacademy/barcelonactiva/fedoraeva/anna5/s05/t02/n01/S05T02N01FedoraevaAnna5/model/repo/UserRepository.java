package cat.itacademy.barcelonactiva.fedoraeva.anna5.s05.t02.n01.S05T02N01FedoraevaAnna5.model.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cat.itacademy.barcelonactiva.fedoraeva.anna5.s05.t02.n01.S05T02N01FedoraevaAnna5.model.domain.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	boolean existsByUsername(String username);
	
	public List <User> findAll();

	boolean existsByEmail(String email);

	

	

}
