import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

class SimpleConnectionPool {
    private final List<Connection> availableConnections = new LinkedList<>();
    private final String url;
    private final String user;
    private final String password;

    public SimpleConnectionPool(int poolSize, String url, String user, String password) throws SQLException {
        this.url = url;
        this.user = user;
        this.password = password;
        for (int i = 0; i < poolSize; i++) {
            availableConnections.add(createNewConnection());
        }
    }

    private Connection createNewConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public synchronized Connection getConnection() throws InterruptedException {
        while (availableConnections.isEmpty()) {
            wait();
        }
        return availableConnections.remove(0);
    }

    public synchronized void releaseConnection(Connection connection) {
        if (connection != null) {
            availableConnections.add(connection);
            notify();
        }
    }

    public synchronized void closeAll() throws SQLException {
        for (Connection conn : availableConnections) {
            conn.close();
        }
        availableConnections.clear();
    }
}

class WorkerThread extends Thread {
    private final SimpleConnectionPool pool;
    private final Random random = new Random();
    private final int insertCount;

    public WorkerThread(SimpleConnectionPool pool, int insertCount) {
        this.pool = pool;
        this.insertCount = insertCount;
    }

    @Override
    public void run() {
        for (int i = 0; i < insertCount; i++) {
            Connection conn = null;
            try {
                conn = pool.getConnection();
                insertLog(conn);
                Thread.sleep(100 + random.nextInt(401));
            } catch (SQLException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    pool.releaseConnection(conn);
                }
            }
        }
    }

    private void insertLog(Connection conn) throws SQLException {
        var sql = "INSERT INTO Log (message, created_at) VALUES (?, NOW())";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "Mesaj de test de la " + Thread.currentThread().getName());
            ps.executeUpdate();
        }
    }
}


public class tema4_3 {
    public static void main(String[] args) throws SQLException, InterruptedException {
        final int POOL_SIZE = 2;
        final int THREAD_COUNT = 4;
        final int INSERTS_PER_THREAD = 3;

        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "mysecretpassword";
        //https://hub.docker.com/_/postgres/
        //$ docker run --name some-postgres -e POSTGRES_PASSWORD=mysecretpassword -d postgres -p 5432:5432

        SimpleConnectionPool pool = new SimpleConnectionPool(POOL_SIZE, url, user, password);

        Thread[] threads = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new WorkerThread(pool, INSERTS_PER_THREAD);
            threads[i].start();
        }
        for (Thread t : threads) {
            t.join();
        }

        try (Connection conn = DriverManager.getConnection(url, user, password); var stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Log")) {
            if (rs.next()) {
                System.out.println("Total înregistrări în Log: " + rs.getInt(1));
            }
        }

        try (Connection conn = DriverManager.getConnection(url, user, password); var cs = conn.prepareCall(" CALL delete_old_logs(1) ")) {
            cs.execute();
        }

        pool.closeAll();
    }
}
/*
CREATE TABLE Log (
    id SERIAL PRIMARY KEY,
    message TEXT,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE OR REPLACE PROCEDURE delete_old_logs(IN CEVA INT)
LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM Log WHERE created_at < NOW() - INTERVAL '1 hour';
END;
$$;


commit
 */