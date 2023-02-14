package cat.itacademy.barcelonactiva.fedoraeva.anna5.s05.t02.n01.S05T02N01FedoraevaAnna5.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;


import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "games_table")

public class Game {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "id_user", nullable = false)
	private User user;

	private int dice1;

	private int dice2;

	private String result;

	@Override
	public String toString() {
		return "Game [id: " + id + ", username: " + user.getUsername() + ", dice1: " + dice1 + ", dice2: " + dice2
				+ ", result: " + result + "]";
	}

}
