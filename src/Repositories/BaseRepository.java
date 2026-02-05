package Repositories;

import Exceptions.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRepository<T> implements IRepository<T> {
    protected final Connection connection;
    protected final String tableName;

    public BaseRepository(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;
    protected abstract void prepareAddStatement(PreparedStatement stmt, T entity) throws SQLException;

    @Override
    public List<T> getAll() {
        List<T> list = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName;
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching all from " + tableName, e);
        }
        return list;
    }

    @Override
    public T getById(int id) {
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapResultSetToEntity(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching ID " + id + " from " + tableName, e);
        }
        return null;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting from " + tableName, e);
        }
    }
}