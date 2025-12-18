package rs.ac.bg.etf.sab.student;

import rs.ac.bg.etf.sab.operations.MoviesOperations;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class im210261_MoviesOperations implements MoviesOperations {

    private final Connection connection = DB.getInstance().getConnection();

    @Override
    public Integer addMovie(String title, Integer genreId, String director) {
        if (title == null || genreId == null || director == null) {
            return null;
        }

        String insertMovieSql = "INSERT INTO Movies (Title, Director) VALUES (?, ?)";
        String insertGenreSql = "INSERT INTO MovieGenres (MovieId, GenreId) VALUES (?, ?)";

        try {
            connection.setAutoCommit(false);

            int movieId;
            try (PreparedStatement ps = connection.prepareStatement(insertMovieSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, title);
                ps.setString(2, director);
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        movieId = rs.getInt(1);
                    } else {
                        connection.rollback();
                        return null;
                    }
                }
            }

            try (PreparedStatement ps = connection.prepareStatement(insertGenreSql)) {
                ps.setInt(1, movieId);
                ps.setInt(2, genreId);
                ps.executeUpdate();
            }

            connection.commit();
            return movieId;

        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ignored) {}
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException ignored) {}
        }

        return null;
    }

    @Override
    public Integer updateMovieTitle(Integer id, String newTitle) {
        if (id == null || newTitle == null) {
            return null;
        }

        String sql = "UPDATE Movies SET Title = ? WHERE Id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newTitle);
            ps.setInt(2, id);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0 ? id : null;

        } catch (SQLException e) {
        }

        return null;
    }

    @Override
    public Integer updateMovieDirector(Integer id, String newDirector) {
        if (id == null || newDirector == null) {
            return null;
        }

        String sql = "UPDATE Movies SET Director = ? WHERE Id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newDirector);
            ps.setInt(2, id);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0 ? id : null;

        } catch (SQLException e) {
        }

        return null;
    }

    @Override
    public Integer addGenreToMovie(Integer movieId, Integer genreId) {
        if (movieId == null || genreId == null) {
            return null;
        }

        String sql = "INSERT INTO MovieGenres (MovieId, GenreId) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, movieId);
            ps.setInt(2, genreId);
            ps.executeUpdate();

            return movieId;

        } catch (SQLException e) {
        }

        return null;
    }

    @Override
    public Integer removeGenreFromMovie(Integer movieId, Integer genreId) {
        if (movieId == null || genreId == null) {
            return null;
        }

        String sql = "DELETE FROM MovieGenres WHERE MovieId = ? AND GenreId = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, movieId);
            ps.setInt(2, genreId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0 ? movieId : null;

        } catch (SQLException e) {
        }

        return null;
    }

    @Override
    public Integer removeMovie(Integer id) {
        if (id == null) {
            return null;
        }

        String sql = "DELETE FROM Movies WHERE Id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0 ? id : null;

        } catch (SQLException e) {
        }

        return null;
    }

    @Override
    public List<Integer> getMovieIds(String title, String director) {
        List<Integer> movieIds = new ArrayList<>();

        if (title == null || director == null) {
            return movieIds;
        }

        String sql = "SELECT Id FROM Movies WHERE Title = ? AND Director = ? ORDER BY Id";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, director);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    movieIds.add(rs.getInt("Id"));
                }
            }

        } catch (SQLException e) {
        }

        return movieIds;
    }

    @Override
    public List<Integer> getAllMovieIds() {
        List<Integer> movieIds = new ArrayList<>();

        String sql = "SELECT Id FROM Movies ORDER BY Id";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                movieIds.add(rs.getInt("Id"));
            }

        } catch (SQLException e) {
        }

        return movieIds;
    }

    @Override
    public List<Integer> getMovieIdsByGenre(Integer genreId) {
        List<Integer> movieIds = new ArrayList<>();

        if (genreId == null) {
            return movieIds;
        }

        String sql = "SELECT MovieId FROM MovieGenres WHERE GenreId = ? ORDER BY MovieId";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, genreId);

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
    public List<Integer> getGenreIdsForMovie(Integer movieId) {
        List<Integer> genreIds = new ArrayList<>();

        if (movieId == null) {
            return genreIds;
        }

        String sql = "SELECT GenreId FROM MovieGenres WHERE MovieId = ? ORDER BY GenreId";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, movieId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    genreIds.add(rs.getInt("GenreId"));
                }
            }

        } catch (SQLException e) {
        }

        return genreIds;
    }

    @Override
    public List<Integer> getMovieIdsByDirector(String director) {
        List<Integer> movieIds = new ArrayList<>();

        if (director == null) {
            return movieIds;
        }

        String sql = "SELECT Id FROM Movies WHERE Director = ? ORDER BY Id";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, director);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    movieIds.add(rs.getInt("Id"));
                }
            }

        } catch (SQLException e) {
        }

        return movieIds;
    }
}