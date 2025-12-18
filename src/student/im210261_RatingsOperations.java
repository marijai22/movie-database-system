package rs.ac.bg.etf.sab.student;

import rs.ac.bg.etf.sab.operations.RatingsOperations;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class im210261_RatingsOperations implements RatingsOperations {

    private final Connection connection = DB.getInstance().getConnection();

    @Override
    public boolean addRating(Integer userId, Integer movieId, Integer score) {
        if (userId == null || movieId == null || score == null) {
            return false;
        }

        String insertSql = "INSERT INTO Ratings (UserId, MovieId, Score) VALUES (?, ?, ?)";
        String callProcSql = "{CALL SP_REWARD_USER_im210261(?, ?)}";

        try {
            try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
                ps.setInt(1, userId);
                ps.setInt(2, movieId);
                ps.setInt(3, score);
                ps.executeUpdate();
            }

            try (CallableStatement cs = connection.prepareCall(callProcSql)) {
                cs.setInt(1, userId);
                cs.setInt(2, movieId);
                cs.execute();
            }

            return true;

        } catch (SQLException e) {

        }

        return false;
    }

    @Override
    public boolean updateRating(Integer userId, Integer movieId, Integer newScore) {
        if (userId == null || movieId == null || newScore == null) {
            return false;
        }

        String sql = "UPDATE Ratings SET Score = ? WHERE UserId = ? AND MovieId = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, newScore);
            ps.setInt(2, userId);
            ps.setInt(3, movieId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
        }

        return false;
    }

    @Override
    public boolean removeRating(Integer userId, Integer movieId) {
        if (userId == null || movieId == null) {
            return false;
        }

        String sql = "DELETE FROM Ratings WHERE UserId = ? AND MovieId = ?";

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
    public Integer getRating(Integer userId, Integer movieId) {
        if (userId == null || movieId == null) {
            return null;
        }

        String sql = "SELECT Score FROM Ratings WHERE UserId = ? AND MovieId = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, movieId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Score");
                }
            }

        } catch (SQLException e) {
        }

        return null;
    }

    @Override
    public List<Integer> getRatedMoviesByUser(Integer userId) {
        List<Integer> movieIds = new ArrayList<>();

        if (userId == null) {
            return movieIds;
        }

        String sql = "SELECT MovieId FROM Ratings WHERE UserId = ? ORDER BY MovieId";

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
    public List<Integer> getUsersWhoRatedMovie(Integer movieId) {
        List<Integer> userIds = new ArrayList<>();

        if (movieId == null) {
            return userIds;
        }

        String sql = "SELECT UserId FROM Ratings WHERE MovieId = ? ORDER BY UserId";

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