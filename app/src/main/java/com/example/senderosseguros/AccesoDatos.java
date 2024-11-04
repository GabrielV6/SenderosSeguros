package com.example.senderosseguros;

import static com.example.senderosseguros.DataDB.pass;
import static com.example.senderosseguros.DataDB.urlPostgreSQL;
import static com.example.senderosseguros.DataDB.user;

import android.os.Handler;
import android.os.Looper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AccesoDatos {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();


    public String obtenerTextoDesdeBD() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        final String[] texto = {""};

        executor.execute(() -> {

            Connection connection =null;

            try {

                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(DataDB.urlPostgreSQL, DataDB.user, DataDB.pass);
                String query = "SELECT Descripcion FROM Barrios WHERE id_barrio = 53";
                PreparedStatement statement = connection.prepareStatement(query);

                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        texto[0] = resultSet.getString("Descripcion");
                    }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });



        return texto[0];
    }


}
