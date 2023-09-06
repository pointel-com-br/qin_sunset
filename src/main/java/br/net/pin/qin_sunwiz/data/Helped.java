package br.net.pin.qin_sunwiz.data;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import br.net.pin.qin_sunwiz.mage.WizData;

public class Helped implements Closeable {
    public Connection link;
    public Helper helper;

    public Helped() {}

    public Helped(Connection link) {
        this.link = link;
    }

    public Helped(Helper helper) {
        this.helper = helper;
    }

    public Helped(Connection link, Helper helper) {
        this.link = link;
        this.helper = helper;
    }

    public void begin() throws Exception {
        this.link.setAutoCommit(false);
    }

    public void commit() throws Exception {
        this.link.commit();
        this.link.setAutoCommit(true);
    }

    public void rollback() throws Exception {
        this.link.rollback();
        this.link.setAutoCommit(true);
    }

    public boolean execute(String sql, Object... params) throws Exception {
        if (params != null && params.length > 0) {
            var statement = this.link.prepareStatement(sql);
            WizData.setParams(statement, params);
            return statement.execute();
        } else {
            return this.link.createStatement().execute(sql);
        }
    }

    public int update(String sql, Object... params) throws Exception {
        if (params != null && params.length > 0) {
            var statement = this.link.prepareStatement(sql);
            WizData.setParams(statement, params);
            return statement.executeUpdate();
        } else {
            return this.link.createStatement().executeUpdate(sql);
        }
    }

    public List<List<Object>> select(String sql, Object... params) throws Exception {
        ResultSet rst;
        if (params != null && params.length > 0) {
            var statement = this.link.prepareStatement(sql);
            WizData.setParams(statement, params);
            rst = statement.executeQuery();
        } else {
            rst = this.link.createStatement().executeQuery(sql);
        }
        List<List<Object>> result = new ArrayList<>();
        var count = rst.getMetaData().getColumnCount();
        while (rst.next()) {
            List<Object> line = new ArrayList<>();
            for (int i = 1; i <= count; i++) {
                line.add(rst.getObject(i));
            }
            result.add(line);
        }
        return result;
    }

    public List<Object> selectRow(String sql, Object... params) throws Exception {
        ResultSet rst;
        if (params != null && params.length > 0) {
            var statement = this.link.prepareStatement(sql);
            WizData.setParams(statement, params);
            rst = statement.executeQuery();
        } else {
            rst = this.link.createStatement().executeQuery(sql);
        }
        List<Object> result = new ArrayList<>();
        var count = rst.getMetaData().getColumnCount();
        if (rst.next()) {
            for (int i = 1; i <= count; i++) {
                result.add(rst.getObject(i));
            }
        }
        return result;
    }

    public Object selectOne(String sql, Object... params) throws Exception {
        ResultSet rst;
        if (params != null && params.length > 0) {
            var statement = this.link.prepareStatement(sql);
            WizData.setParams(statement, params);
            rst = statement.executeQuery();
        } else {
            rst = this.link.createStatement().executeQuery(sql);
        }
        if (rst.next()) {
            return rst.getObject(1);
        }
        return null;
    }

    @Override
    public void close() throws IOException {
        try {
            this.link.close();
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }
}
