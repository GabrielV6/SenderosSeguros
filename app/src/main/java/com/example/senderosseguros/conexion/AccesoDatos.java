package com.example.senderosseguros.conexion;

import android.content.Context;

import com.example.senderosseguros.entidad.ItemReporte;
import com.example.senderosseguros.entidad.ObstaculoMarcadores;
import com.example.senderosseguros.entidad.Usuario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class AccesoDatos {

    private Context context;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public AccesoDatos (Context ct){
        context = ct;
    }

    public boolean obtenerTextoDesdeBD() {
        executor = Executors.newSingleThreadExecutor();
        final boolean[] existe = {false};

        executor.execute(() -> {
            Connection con = null;
            try {
                Class.forName(DataDB.driver);
                con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
                String query = "SELECT COUNT(*) AS total FROM Barrios";
                PreparedStatement ps = con.prepareStatement(query);
                /*ps.setInt(1, ID);*/
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
    public List<String> obtenerBarrios() {
        List<String> barrios = new ArrayList<>();

        executor.execute(() -> {
            Connection con = null;
            try {
                Class.forName(DataDB.driver);
                con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
                String query = "SELECT Descripcion FROM Barrios";
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    String descripcion = rs.getString("Descripcion");
                    barrios.add(descripcion);
                }

                rs.close();
                ps.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        try {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return barrios;
    }

    public List<ItemReporte> obtenerObstaculosPorBarrio(String nombreBarrio) {
        List<ItemReporte> obstaculos = new ArrayList<>();
        executor = Executors.newSingleThreadExecutor();

        Future<List<ItemReporte>> result = executor.submit(() -> {
            List<ItemReporte> listaObstaculos = new ArrayList<>();
            Connection con = null;
            try {
                Class.forName(DataDB.driver);
                con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);

                String query = "SELECT C.Descripcion AS Obstaculo, COUNT(O.ID_Obstaculo) AS Cantidad " +
                        "FROM Obstaculos O " +
                        "JOIN Puntos P ON O.ID_Punto = P.ID_Punto " +
                        "JOIN CatalogoObstaculos C ON O.ID_TipoObstaculo = C.ID_TipoObstaculo " +
                        "JOIN Barrios B ON P.ID_Barrio = B.ID_Barrio " +
                        "WHERE B.Descripcion = ? " +
                        "GROUP BY C.Descripcion";

                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, nombreBarrio);

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    String descripcion = rs.getString("Obstaculo");
                    int cantidad = rs.getInt("Cantidad");
                    listaObstaculos.add(new ItemReporte(descripcion, cantidad));
                }

                rs.close();
                ps.close();
                con.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return listaObstaculos;
        });

        try {
            obstaculos = result.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        return obstaculos;
    }

    public List<ItemReporte> obtenerObstaculosPorTipo(String tipo) {
        List<ItemReporte> obstaculos = new ArrayList<>();
        executor = Executors.newSingleThreadExecutor();

        Future<List<ItemReporte>> result = executor.submit(() -> {
            List<ItemReporte> listaObstaculos = new ArrayList<>();
            Connection con = null;
            try {
                Class.forName(DataDB.driver);
                con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);

                String query = "SELECT B.Descripcion AS Barrio, COUNT(O.ID_Obstaculo) AS Cantidad " +
                        "FROM Obstaculos O " +
                        "JOIN Puntos P ON O.ID_Punto = P.ID_Punto " +
                        "JOIN CatalogoObstaculos C ON O.ID_TipoObstaculo = C.ID_TipoObstaculo " +
                        "JOIN Barrios B ON P.ID_Barrio = B.ID_Barrio " +
                        "WHERE C.Descripcion = ? " +
                        "GROUP BY B.Descripcion";

                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, tipo);

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    String descripcion = rs.getString("Barrio");
                    int cantidad = rs.getInt("Cantidad");
                    listaObstaculos.add(new ItemReporte(descripcion, cantidad));
                }

                rs.close();
                ps.close();
                con.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return listaObstaculos;
        });

        try {
            obstaculos = result.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        return obstaculos;
    }

    public List<ItemReporte> obtenerObstaculosPorPeriodo(String fecha) {
        List<ItemReporte> obstaculos = new ArrayList<>();
        executor = Executors.newSingleThreadExecutor();

        Future<List<ItemReporte>> result = executor.submit(() -> {
            List<ItemReporte> listaObstaculos = new ArrayList<>();
            Connection con = null;
            try {
                Class.forName(DataDB.driver);
                con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);

                String query = "SELECT O.Estado, COUNT(O.ID_Obstaculo) AS Cantidad " +
                        "FROM Obstaculos O " +
                        "WHERE O.FechaCreacion >= ? " +
                        "GROUP BY O.Estado";

                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, fecha);

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    String descripcion = rs.getString("Estado");
                    int cantidad = rs.getInt("Cantidad");
                    listaObstaculos.add(new ItemReporte(descripcion, cantidad));
                }

                rs.close();
                ps.close();
                con.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return listaObstaculos;
        });

        try {
            obstaculos = result.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        return obstaculos;
    }


    public List<String> obtenerObstaculosActivos() {
        List<String> obstaculos = new ArrayList<>();

        executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Connection con = null;
            try {
                Class.forName(DataDB.driver);
                con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
                String query = "SELECT Descripcion FROM CatalogoObstaculos WHERE Estado = 1";
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    obstaculos.add(rs.getString("Descripcion"));
                }

                rs.close();
                ps.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        try {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return obstaculos;
    }

    //Metodo para agregar usuarios
    public boolean agregarUser(Usuario usuario){
        executor = Executors.newSingleThreadExecutor();
        Future<Boolean> result = executor.submit(() -> {
            try {
                Class.forName(DataDB.driver);
                String query = "INSERT INTO Usuarios (Nombre, Apellido, DNI, CorreoElectronico, Usuario, Contrasena, FechaRegistro, Puntaje, Estado) VALUES (?,?,?,?,?,?,?,?,?)";

                try (Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
                     PreparedStatement st = con.prepareStatement(query)) {

                    st.setString(1, usuario.getNombre());
                    st.setString(2, usuario.getApellido());
                    st.setString(3, usuario.getDNI());
                    st.setString(4, usuario.getCorreo());
                    st.setString(5, usuario.getUser());
                    st.setString(6, usuario.getPass());
                    st.setDate(7, new java.sql.Date(new java.util.Date().getTime()));
                    st.setInt(8, 0);
                    st.setBoolean(9, true);

                    int rowsAffected = st.executeUpdate();
                    return rowsAffected > 0;

                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        });

        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        } finally {
            executor.shutdown();  // Cierra el executor para evitar fugas de recursos
        }
    }

    //Metodo para chequear que usuario y pass existen en BD
    public boolean existeUserPass (String user, String pass) {

        executor = Executors.newSingleThreadExecutor();
        final boolean[] existe = {false};

        executor.execute(() -> {
            Connection con = null;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
                String query = "SELECT COUNT(*) AS total FROM Usuarios where usuario = ? AND contrasena = ?";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, user);
                ps.setString(2, pass);
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

    //Metodo para chequear que DNI no se encuentre en BD al registrar
    public boolean existeDNI (String dni) {

        executor = Executors.newSingleThreadExecutor();
        final boolean[] existe = {false};

        executor.execute(() -> {
            Connection con = null;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
                String query = "SELECT COUNT(*) AS total FROM Usuarios where dni = ?";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, dni);

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

    //Metodo para chequear que user no se encuentre en BD al registrar
    public boolean existeUser (String user) {

        executor = Executors.newSingleThreadExecutor();
        final boolean[] existe = {false};

        executor.execute(() -> {
            Connection con = null;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
                String query = "SELECT COUNT(*) AS total FROM Usuarios where Usuario = ?";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, user);

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

    //Metodo para traer correo de BD y mostrar en Home
    public String recuperarCorreo (String user){

        String correo = null;

        executor = Executors.newSingleThreadExecutor();
        Future<String> result = executor.submit(() -> {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                try (Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
                     PreparedStatement ps = con.prepareStatement("SELECT CorreoElectronico FROM Usuarios WHERE Usuario = ?")) {

                    ps.setString(1, user);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        return rs.getString("CorreoElectronico");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });

        try {
            correo = result.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        return correo;
    }

    public List<ObstaculoMarcadores> getObstaculos() {
        executor = Executors.newSingleThreadExecutor();
        List<ObstaculoMarcadores> obstacles = new ArrayList<>();

        executor.execute(() -> {
            Connection con = null;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);

                String query = "SELECT Obstaculos.ID_Obstaculo, CatalogoObstaculos.Descripcion AS Descripcion_Obstaculo, " +
                        "Obstaculos.ID_TipoObstaculo, Puntos.Latitud, Puntos.Longitud " +
                        "FROM Obstaculos " +
                        "JOIN Puntos ON Obstaculos.ID_Punto = Puntos.ID_Punto " +
                        "JOIN CatalogoObstaculos ON Obstaculos.ID_TipoObstaculo = CatalogoObstaculos.ID_TipoObstaculo " +
                        "WHERE Obstaculos.Estado = 1";

                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int idObstaculo = rs.getInt("ID_Obstaculo");
                    int idTipoObstaculo = rs.getInt("ID_TipoObstaculo");
                    String descripcionObstaculo = rs.getString("Descripcion_Obstaculo");
                    double latitud = rs.getDouble("Latitud");
                    double longitud = rs.getDouble("Longitud");

                    ObstaculoMarcadores obstaculo = new ObstaculoMarcadores(idObstaculo, idTipoObstaculo,descripcionObstaculo, latitud, longitud);
                    obstacles.add(obstaculo);
                }

                rs.close();
                ps.close();
                con.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        try {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return obstacles;
    }

    public int insertarPunto(double latitud, double longitud, int idBarrio) {
        final int[] idPunto = {-1}; // Usamos un array para obtener el valor dentro del Executor

        executor.execute(() -> {
            Connection con = null;
            try {
                Class.forName(DataDB.driver);
                String query = "INSERT INTO Puntos (Latitud, Longitud, ID_Barrio) VALUES (?, ?, ?)";

                try (Connection conn = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
                     PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

                    ps.setDouble(1, latitud);
                    ps.setDouble(2, longitud);
                    ps.setInt(3, idBarrio);

                    int rowsAffected = ps.executeUpdate();

                    if (rowsAffected > 0) {
                        ResultSet rs = ps.getGeneratedKeys();
                        if (rs.next()) {
                            idPunto[0] = rs.getInt(1); // Obtenemos el ID_Punto generado
                        }
                        rs.close();
                    }
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        });

        try {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return idPunto[0];
    }

    public boolean insertarObstaculo(int idTipoObstaculo, String comentarios, String imagen, int idUsuario, int idPunto, String fechaBaja, int contadorSolucion, int estado) {
        AtomicBoolean exito = new AtomicBoolean(false);

        executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Connection con = null;
            try {
                Class.forName(DataDB.driver);
                con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);

                String query = "INSERT INTO Obstaculos (ID_TipoObstaculo, Comentarios, Imagen, FechaCreacion, ID_Usuario, ID_Punto, FechaBaja, ContadorSolucion, Estado) " +
                        "VALUES (?, ?, ?, NOW(), ?, ?, ?, ?, ?)";

                try (PreparedStatement ps = con.prepareStatement(query)) {
                    ps.setInt(1, idTipoObstaculo);
                    ps.setString(2, comentarios);
                    ps.setString(3, imagen);
                    ps.setInt(4, idUsuario);
                    ps.setInt(5, idPunto);
                    ps.setString(6, fechaBaja);
                    ps.setInt(7, contadorSolucion);
                    ps.setInt(8, estado);

                    int rowsAffected = ps.executeUpdate();
                    exito.set(rowsAffected > 0);
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            } finally {
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        try {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return exito.get();
    }

    //Metodo para traer IDUser de BD y mostrar en Home
    public int recuperarIDUser(String user) {
        int IDUser = -1;

        executor = Executors.newSingleThreadExecutor();
        Future<Integer> result = executor.submit(() -> {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                try (Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
                     PreparedStatement ps = con.prepareStatement("SELECT ID_Usuario FROM Usuarios WHERE Usuario = ?")) {

                    ps.setString(1, user);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        return rs.getInt("ID_Usuario");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return -1; // Devuelve -1 si no se encuentra el usuario
        });

        try {
            IDUser = result.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        return IDUser;
    }

}
