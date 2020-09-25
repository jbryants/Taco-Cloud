package tacos.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import tacos.Ingredient;

@Repository
public class JdbcIngredientRepository implements IngredientRepository {

	private JdbcTemplate jdbc;

	@Autowired
	public JdbcIngredientRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public Iterable<Ingredient> findAll() {
		return jdbc.query("SELECT id, name, type FROM Ingredient", this::mapRowToIngredient);
	}

	// Java 8’s method references and lambdas (this::mapRowToIngredient) are
	// convenient when working with JdbcTemplate as an alternative to
	// an explicit RowMapper implementation.
	@Override
	public Ingredient findById(String id) {
		return jdbc.queryForObject("SELECT id, name, type FROM Ingredient WHERE id = ?", this::mapRowToIngredient, id);
	}

	// findById alternate implementation - using an explicit RowMapper
	// implementation
//	@Override
//	public Ingredient findById(String id) {
//		return jdbc.queryForObject("select id, name, type from Ingredient where id=?", new RowMapper<Ingredient>() {
//			public Ingredient mapRow(ResultSet rs, int rowNum) throws SQLException {
//				return new Ingredient(rs.getString("id"), rs.getString("name"),
//						Ingredient.Type.valueOf(rs.getString("type")));
//			};
//		}, id);
//	}

	private Ingredient mapRowToIngredient(ResultSet rs, int rowNum) throws SQLException {
		return new Ingredient(rs.getString("id"), rs.getString("name"), Ingredient.Type.valueOf(rs.getString("type")));
	}

	@Override
	public Ingredient save(Ingredient ingredient) {
		jdbc.update("INSERT INTO Ingredient (id, name, type) VALUES (?, ?, ?)", ingredient.getId(),
				ingredient.getName(), ingredient.getType().toString());
		return ingredient;

	}

}
