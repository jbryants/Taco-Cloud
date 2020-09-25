package tacos.dataJDBC;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import tacos.Ingredient;
import tacos.Taco;

@Repository
public class JdbcTacoRepository implements TacoRepository {

	private JdbcTemplate jdbc;

	@Autowired
	public JdbcTacoRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public Taco save(Taco taco) {
		try {
			long tacoId = saveTacoInfo(taco);
			taco.setId(tacoId);
			for (Ingredient ingredient : taco.getIngredients()) {
				saveIngredientToTaco(ingredient, tacoId);
			}
		} catch (NullPointerException e) {
			System.out.println("Handling Null pointer exception");
		}

		return taco;
	}

	private long saveTacoInfo(Taco taco) {
		taco.setCreatedAt(new Date());

		// PreparedStatementCreator and KeyHolder can be used together when you
		// need to know the value of a database-generated ID.

		PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
				"INSERT INTO Taco (name, createdAt) VALUES (?, ?)", Types.VARCHAR, Types.TIMESTAMP);

		// By default, returnGeneratedKeys = false so change it to true
		pscf.setReturnGeneratedKeys(true);

		PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
				Arrays.asList(taco.getName(), new Timestamp(taco.getCreatedAt().getTime())));

		System.out.println("Created at: " + new Timestamp(taco.getCreatedAt().getTime()));

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbc.update(psc, keyHolder);

		return keyHolder.getKey().longValue();
	}

	private void saveIngredientToTaco(Ingredient ingredient, long tacoId) {
		jdbc.update("INSERT INTO Taco_Ingredients (taco, ingredient) VALUES (?, ?)", tacoId, ingredient.getId());
	}

}
