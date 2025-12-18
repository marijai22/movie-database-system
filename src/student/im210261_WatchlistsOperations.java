package rs.ac.bg.etf.sab.student;

import rs.ac.bg.etf.sab.operations.WatchlistsOperations;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class im210261_WatchlistsOperations implements WatchlistsOperations {

    private final Connection connection = DB.getInstance().getConnection();

    @Override
    public boolean addMovieToWatchlist(Integer userId, Integer movieId) {
        if (userId == null || movieId == null) {
            return false;
        }

        String sql = "INSERT INTO Watchlists (UserId, MovieId) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, movieId);
            ps.executeUpdate();

            return true;

        } catch (SQLException e) {
        }

        return false;
    }

    @Override
    public boolean removeMovieFromWatchlist(Integer userId, Integer movieId) {
        if (userId == null || movieId == null) {
            return false;
        }

        String sql = "DELETE FROM Watchlists WHERE UserId = ? AND MovieId = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, movieId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
        }

        return false;
    }

    @Override
    public boolean isMovieInWatchlist(Integer userId, Integer movieId) {
        if (userId == null || movieId == null) {
            return false;
        }

        String sql = "SELECT 1 FROM Watchlists WHERE UserId = ? AND MovieId = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, movieId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
        }

        return false;
    }

    @Override
    public List<Integer> getMoviesInWatchlist(Integer userId) {
        List<Integer> movieIds = new ArrayList<>();

        if (userId == null) {
            return movieIds;
        }

        String sql = "SELECT MovieId FROM Watchlists WHERE UserId = ? ORDER BY MovieId";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    movieIds.add(rs.getInt("MovieId"));
                }
            }

        } catch (SQLException e) {
        }

        return movieIds;
    }

    @Override
    public List<Integer> getUsersWithMovieInWatchlist(Integer movieId) {
        List<Integer> userIds = new ArrayList<>();

        if (movieId == null) {
            return userIds;
        }

        String sql = "SELECT UserId FROM Watchlists WHERE MovieId = ? ORDER BY UserId";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, movieId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    userIds.add(rs.getInt("UserId"));
                }
            }

        } catch (SQLException e) {
        }

        return userIds;
    }
}