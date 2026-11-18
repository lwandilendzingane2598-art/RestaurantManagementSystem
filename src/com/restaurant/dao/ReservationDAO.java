package com.restaurant.dao;

import com.restaurant.models.Reservation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    private static final String TABLE = "reservations_902012598_902012661_902011766";
    private static final String CUSTOMER_TABLE = "customers_902012598_902012661_902011766";
    private static final String TABLE_TABLE = "tables_902012598_902012661_902011766";

    public boolean add(Reservation res) {
        String sql = "INSERT INTO " + TABLE + " (cust_id, table_id, reservation_date, reservation_time, guests, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, res.getCustomerId());
            ps.setInt(2, res.getTableId());
            ps.setDate(3, res.getReservationDate());
            ps.setTime(4, res.getReservationTime());
            ps.setInt(5, res.getGuests());
            ps.setString(6, res.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Reservation res) {
        String sql = "UPDATE " + TABLE + " SET cust_id=?, table_id=?, reservation_date=?, reservation_time=?, guests=?, status=? WHERE res_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, res.getCustomerId());
            ps.setInt(2, res.getTableId());
            ps.setDate(3, res.getReservationDate());
            ps.setTime(4, res.getReservationTime());
            ps.setInt(5, res.getGuests());
            ps.setString(6, res.getStatus());
            ps.setInt(7, res.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM " + TABLE + " WHERE res_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Reservation> findAll() {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT r.res_id, r.cust_id, c.cust_name, r.table_id, t.table_number, r.reservation_date, r.reservation_time, r.guests, r.status " +
                     "FROM " + TABLE + "r " +
                     "LEFT JOIN " + CUSTOMER_TABLE + " c ON r.cust_id = c.cust_id " +
                     "LEFT JOIN " + TABLE_TABLE + " t ON r.table_id = t.table_id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Reservation res = new Reservation();
                res.setId(rs.getInt("res_id"));
                res.setCustomerId(rs.getInt("cust_id"));
                res.setCustomerName(rs.getString("cust_name"));
                res.setTableId(rs.getInt("table_id"));
                res.setTableNumber(rs.getString("table_number"));
                res.setReservationDate(rs.getDate("reservation_date"));
                res.setReservationTime(rs.getTime("reservation_time"));
                res.setGuests(rs.getInt("guests"));
                res.setStatus(rs.getString("status"));
                list.add(res);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Reservation findById(int id) {
        String sql = "SELECT * FROM " + TABLE + " WHERE res_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Reservation res = new Reservation();
                res.setId(rs.getInt("res_id"));
                res.setCustomerId(rs.getInt("cust_id"));
                res.setTableId(rs.getInt("table_id"));
                res.setReservationDate(rs.getDate("reservation_date"));
                res.setReservationTime(rs.getTime("reservation_time"));
                res.setGuests(rs.getInt("guests"));
                res.setStatus(rs.getString("status"));
                return res;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
