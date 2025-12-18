package rs.ac.bg.etf.sab.student;

import rs.ac.bg.etf.sab.operations.GeneralOperations;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class im210261_GeneralOperations implements GeneralOperations {

    private final Connection connection = DB.getInstance().getConnection();

    @Override
    public void eraseAll() {

        try (Statement stmt = connection.createStatement()) {
            connection.setAutoCommit(false);

            stmt.execute("ALTER TABLE Ratings NOCHECK CONSTRAINT ALL");
            stmt.execute("ALTER TABLE Watchlists NOCHECK CONSTRAINT ALL");
            stmt.execute("ALTER TABLE MovieTags NOCHECK CONSTRAINT ALL");
            stmt.execute("ALTER TABLE MovieGenres NOCHECK CONSTRAINT ALL");

            stmt.execute("DISABLE TRIGGER TR_BLOCK_EXTREME_im210261 ON Ratings");

            stmt.execute("DELETE FROM Ratings");
            stmt.execute("DELETE FROM Watchlists");
            stmt.execute("DELETE FROM MovieTags");
            stmt.execute("DELETE FROM MovieGenres");
            stmt.execute("DELETE FROM Movies");
            stmt.execute("DELETE FROM Genres");
            stmt.execute("DELETE FROM Users");

            stmt.execute("DBCC CHECKIDENT ('Users', RESEED, 0)");
            stmt.execute("DBCC CHECKIDENT ('Genres', RESEED, 0)");
            stmt.execute("DBCC CHECKIDENT ('Movies', RESEED, 0)");

            stmt.execute("ENABLE TRIGGER TR_BLOCK_EXTREME_im210261 ON Ratings");

            stmt.execute("ALTER TABLE Ratings CHECK CONSTRAINT ALL");
            stmt.execute("ALTER TABLE Watchlists CHECK CONSTRAINT ALL");
            stmt.execute("ALTER TABLE MovieTags CHECK CONSTRAINT ALL");
            stmt.execute("ALTER TABLE MovieGenres CHECK CONSTRAINT ALL");

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            try { connection.rollback(); } catch (SQLException ignored) {}
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }
}