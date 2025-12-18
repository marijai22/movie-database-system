package rs.ac.bg.etf.sab.student;

import rs.ac.bg.etf.sab.operations.UsersOperations;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class im210261_UsersOperations implements UsersOperations {

    private final Connection connection = DB.getInstance().getConnection();

    @Override
    public Integer addUser(String username) {
        if (username == null) {
            return null;
        }

        String sql = "INSERT INTO Users (Username, Rewards) VALUES (?, 0)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, username);
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
    public Integer updateUser(Integer id, String newUsername) {
        if (id == null || newUsername == null) {
            return null;
        }

        String sql = "UPDATE Users SET Username = ? WHERE Id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newUsername);
            ps.setInt(2, id);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0 ? id : null;

        } catch (SQLException e) {
        }

        return null;
    }

    @Override
    public Integer removeUser(Integer id) {
        if (id == null) {
            return null;
        }

        String sql = "DELETE FROM Users WHERE Id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0 ? id : null;

        } catch (SQLException e) {
        }

        return null;
    }

    @Override
    public boolean doesUserExist(String username) {
        if (username == null) {
            return false;
        }

        String sql = "SELECT 1 FROM Users WHERE Username = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
        }

        return false;
    }

    @Override
    public Integer getUserId(String username) {
        if (username == null) {
            return null;
        }

        String sql = "SELECT Id FROM Users WHERE Username = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);

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
    public List<Integer> getAllUserIds() {
        List<Integer> userIds = new ArrayList<>();

        String sql = "SELECT Id FROM Users ORDER BY Id";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                userIds.add(rs.getInt("Id"));
            }

        } catch (SQLException e) {
        }

        return userIds;
    }

    @Override
    public Integer getRewards(Integer userId) {
        if (userId == null) {
            return null;
        }

        String sql = "SELECT Rewards FROM Users WHERE Id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Rewards");
                }
            }

        } catch (SQLException e) {
        }

        return null;
    }

    @Override
    public List<Integer> getRecommendedMoviesFromFavoriteGenres(Integer userId) {
        List<Integer> recommendedMovies = new ArrayList<>();

        if (userId == null) {
            return recommendedMovies;
        }

        String sql =
                "WITH FavoriteGenres AS ( " +
                        "    SELECT mg.GenreId " +
                        "    FROM Ratings r " +
                        "    JOIN MovieGenres mg ON r.MovieId = mg.MovieId " +
                        "    WHERE r.UserId = ? " +
                        "    GROUP BY mg.GenreId " +
                        "    HAVING AVG(CAST(r.Score AS DECIMAL(10,3))) >= 8.0 " +
                        "), " +
                        "MovieStats AS ( " +
                        "    SELECT " +
                        "        m.Id AS MovieId, " +
                        "        COUNT(r.Score) AS RatingCount, " +
                        "        AVG(CAST(r.Score AS DECIMAL(10,3))) AS AvgRating " +
                        "    FROM Movies m " +
                        "    LEFT JOIN Ratings r ON m.Id = r.MovieId " +
                        "    GROUP BY m.Id " +
                        ") " +
                        "SELECT ms.MovieId " +
                        "FROM MovieStats ms " +
                        "JOIN MovieGenres mg ON ms.MovieId = mg.MovieId " +
                        "JOIN FavoriteGenres fg ON mg.GenreId = fg.GenreId " +
                        "WHERE ms.MovieId NOT IN ( " +
                        "    SELECT MovieId FROM Ratings WHERE UserId = ? " +
                        ") " +
                        "AND ms.MovieId NOT IN ( " +
                        "    SELECT MovieId FROM Watchlists WHERE UserId = ? " +
                        ") " +
                        "AND ( " +
                        "    (ms.RatingCount >= 4 AND ms.AvgRating >= 7.5) " +
                        "    OR (ms.RatingCount < 4 AND ms.AvgRating >= 9.0) " +
                        ") " +
                        "GROUP BY ms.MovieId, ms.AvgRating " +
                        "ORDER BY ms.AvgRating DESC, ms.MovieId ASC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ps.setInt(3, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    recommendedMovies.add(rs.getInt("MovieId"));
                }
            }

        } catch (SQLException e) {
        }

        return recommendedMovies;
    }

    @Override
    public List<String> getThematicSpecializations(Integer userId) {
        List<String> tags = new ArrayList<>();

        if (userId == null) {
            return tags;
        }

        String sql =
                "SELECT mt.TagName " +
                        "FROM Ratings r " +
                        "JOIN MovieTags mt ON r.MovieId = mt.MovieId " +
                        "WHERE r.UserId = ? AND r.Score >= 8 " +
                        "GROUP BY mt.TagName " +
                        "HAVING COUNT(*) >= 2 " +
                        "ORDER BY mt.TagName";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tags.add(rs.getString("TagName"));
                }
            }

        } catch (SQLException e) {
        }

        return tags;
    }

    @Override
    public String getUserDescription(Integer userId) {
        if (userId == null) {
            return "undefined";
        }

        String sql =
                "SELECT " +
                        "    COUNT(DISTINCT r.MovieId) AS MovieCount, " +
                        "    COUNT(DISTINCT mt.TagName) AS TagCount " +
                        "FROM Ratings r " +
                        "LEFT JOIN MovieTags mt ON r.MovieId = mt.MovieId " +
                        "WHERE r.UserId = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int movieCount = rs.getInt("MovieCount");
                    int tagCount = rs.getInt("TagCount");

                    if (movieCount >= 10 && tagCount >= 10) {
                        return "curious";
                    } else if (movieCount >= 10 && tagCount < 10) {
                        return "focused";
                    }
                }
            }

        } catch (SQLException e) {
        }

        return "undefined";
    }

}