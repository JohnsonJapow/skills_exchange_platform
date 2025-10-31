package com.skills.user.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

public class Database {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Database.class);

    private String defaultDriver = "";
    private String defaultUrl = "";
    private String defaultUsername = "";
    private String defaultPassword = "";

    private Vector<CachedConnection> cache;

    public Database() {
        cache = new Vector<CachedConnection>();
    }

    public Database(String defaultDriver, String defaultUrl, String defaultUsername, String defaultPassword) {
        this.defaultDriver = defaultDriver;
        this.defaultUrl = defaultUrl;
        this.defaultUsername = defaultUsername;
        this.defaultPassword = defaultPassword;
        cache = new Vector<CachedConnection>();
    }

    public CachedConnection getConnection(String driver, String url, String username, String password) {
        CachedConnection cachedConn = null;
        cachedConn = getExistingConnection(url,username,password);
        
        if (cachedConn != null)
            return cachedConn;

        cachedConn = getNewConnection(driver,url,username,password);
        return cachedConn;
    }

    public CachedConnection getConnection() {
        return getConnection(defaultDriver, defaultUrl, defaultUsername, defaultPassword);
    }
    
    private synchronized CachedConnection getExistingConnection(String url, String username, String password) {
        CachedConnection cachedConn;

        for (int i=0; i < cache.size(); i++) {
            cachedConn = (CachedConnection)cache.get(i);

            if (!cachedConn.isInUse() && cachedConn.equals(url,username,password)) {
                cachedConn.setInUse(true);
                return cachedConn;
            }
        }
        return null;  // None found
    }

    private CachedConnection getNewConnection(String driver, String url,
                                             String username, String password) {
        // Note that only the section which updates the cache is synchronized. This
        // allows other cached and new connections to be obtained in parallel.
        CachedConnection cachedConnection = null;
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url,username,password);
            cachedConnection = new CachedConnection(conn,url,username,password);
        } catch (ClassNotFoundException cnf) {
            log.error("Can't load database driver: " + cnf.getMessage());
        } catch (SQLException se) {
            log.error("SQL Exception: " + se.getMessage());
        }

        return cachedConnection;
    }

}
