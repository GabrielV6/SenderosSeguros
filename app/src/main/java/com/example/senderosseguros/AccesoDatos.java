package com.example.senderosseguros;

import static com.example.senderosseguros.DataDB.pass;
import static com.example.senderosseguros.DataDB.user;

import android.os.Handler;
import android.os.Looper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class AccesoDatos {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    /*public String obtenerTextoDesdeBD(int ID) {
        Callable<String> tarea = () -> {
            String texto = "";
            try {
                Class.forName(DataDB.driver);
                try (Connection connection = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass)) {
                    String query = "SELECT Descripcion FROM Barrios WHERE ID_Barrio = ?";
                    PreparedStatement ps = connection.prepareStatement(query);
                    ps.setInt(1, ID); // Configura el parámetro
                    ResultSet resultSet = ps.executeQuery();

                    if (resultSet.next()) {
                        texto = resultSet.getString("Descripcion");
                    }

                    resultSet.close();
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return texto;
        };

        // Ejecuta la tarea y espera el resultado
        Future<String> future = executorService.submit(tarea);
        try {
            // Obtén el resultado de la tarea
            return future.get(5, TimeUnit.SECONDS); // Espera hasta 5 segundos
        } catch (InterruptedException | ExecutionException | java.util.concurrent.TimeoutException e) {
            e.printStackTrace();
        }
        return null; // Retorna null en caso de error
    }

    // Llamar a esto cuando ya no necesites ejecutar más tareas para cerrar el executorService
    public void shutdown() {
        executorService.shutdown();
    }*/

    public boolean obtenerTextoDesdeBD(int ID) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        final boolean[] existe = {false};

        executor.execute(() -> {
            Connection con = null;
            try {
                Class.forName(DataDB.driver);
                con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
                String query = "SELECT COUNT(*) AS total FROM Barrios WHERE ID_Barrio = ?";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setInt(1, ID);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    int count = rs.getInt("total");
                    existe[0] = count > 0;
                }

                rs.close();
                ps.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        try {
            // Espera que el hilo termine
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return existe[0];
    }

}
