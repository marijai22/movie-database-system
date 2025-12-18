package rs.ac.bg.etf.sab.student;

import rs.ac.bg.etf.sab.operations.TagsOperations;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class im210261_TagsOperations implements TagsOperations {

    private final Connection connection = DB.getInstance().getConnection();

    @Override
    public Integer addTag(Integer movieId, String tag) {
        if (movieId == null || tag == null) {
            return null;
        }

        String sql = "INSERT INTO MovieTags (MovieId, TagName) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, movieId);
            ps.setString(2, tag);
            ps.executeUpdate();

            return movieId;

        } catch (SQLException e) {
        }

        return null;
    }

    @Override
    public Integer removeTag(Integer movieId, String tag) {
        if (movieId == null || tag == null) {
            return null;
        }

        String sql = "DELETE FROM MovieTags WHERE MovieId = ? AND TagName = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, movieId);
            ps.setString(2, tag);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0 ? movieId : null;

        } catch (SQLException e) {
        }

        return null;
    }

    @Override
    public int removeAllTagsForMovie(Integer movieId) {
        if (movieId == null) {
            return 0;
        }

        String sql = "DELETE FROM MovieTags WHERE MovieId = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, movieId);

            return ps.executeUpdate();

        } catch (SQLException e) {
        }

        return 0;
    }

    @Override
    public boolean hasTag(Integer movieId, String tag) {
        if (movieId == null || tag == null) {
            return false;
        }

        String sql = "SELECT 1 FROM MovieTags WHERE MovieId = ? AND TagName = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, movieId);
            ps.setString(2, tag);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
        }

        return false;
    }

    @Override
    public List<String> getTagsForMovie(Integer movieId) {
        List<String> tags = new ArrayList<>();

        if (movieId == null) {
            return tags;
        }

        String sql = "SELECT TagName FROM MovieTags WHERE MovieId = ? ORDER BY TagName";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, movieId);

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
    public List<Integer> getMovieIdsByTag(String tag) {
        List<Integer> movieIds = new ArrayList<>();

        if (tag == null) {
            return movieIds;
        }

        String sql = "SELECT MovieId FROM MovieTags WHERE TagName = ? ORDER BY MovieId";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, tag);

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
    public List<String> getAllTags() {
        List<String> tags = new ArrayList<>();

        String sql = "SELECT DISTINCT TagName FROM MovieTags ORDER BY TagName";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                tags.add(rs.getString("TagName"));
            }

        } catch (SQLException e) {
        }

        return tags;
    }
}