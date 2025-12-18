package rs.ac.bg.etf.sab.student;

import rs.ac.bg.etf.sab.operations.GenresOperations;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class im210261_GenresOperations implements GenresOperations {

    private final Connection connection = DB.getInstance().getConnection();

    @Override
    public Integer addGenre(String name) {
        if (name == null) return null;

        String sql = "INSERT INTO Genres (Name) VALUES (?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
        }

        return null;
    }

    @Override
    public Integer updateGenre(Integer id, String newName) {
        if (id == null) return null;
        if (newName == null) return null;

        String sql = "UPDATE Genres SET Name = ? WHERE Id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newName);
            ps.setInt(2, id);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0 ? id : null;

        } catch (SQLException e) {
        }

        return null;
    }

    @Override
    public Integer removeGenre(Integer id) {
        if (id == null) return null;

        String sql = "DELETE FROM Genres WHERE Id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0 ? id : null;

        } catch (SQLException e) {
        }

        return null;
    }

    @Override
    public boolean doesGenreExist(String name) {
        if (name == null) return false;

        String sql = "SELECT 1 FROM Genres WHERE Name = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
        }

        return false;
    }

    @Override
    public Integer getGenreId(String name) {
        if (name == null) return null;

        String sql = "SELECT Id FROM Genres WHERE Name = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Id");
                }
            }

        } catch (SQLException e) {
        }

        return null;
    }

    @Override
    public List<Integer> getAllGenreIds() {
        List<Integer> genreIds = new ArrayList<>();

        String sql = "SELECT Id FROM Genres ORDER BY Id";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                genreIds.add(rs.getInt("Id"));
            }

        } catch (SQLException e) {
        }

        return genreIds;
    }
}