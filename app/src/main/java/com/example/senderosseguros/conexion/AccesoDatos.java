package com.example.senderosseguros.conexion;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.senderosseguros.entidad.Barrio;
import com.example.senderosseguros.entidad.ItemReporte;
import com.example.senderosseguros.entidad.Obstaculo;
import com.example.senderosseguros.entidad.ObstaculoMarcadores;
import com.example.senderosseguros.entidad.Punto;
import com.example.senderosseguros.entidad.TipoObstaculo;
import com.example.senderosseguros.entidad.Usuario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

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
    public List<Barrio> obtenerBarrios() {
        List<Barrio> barrios = new ArrayList<>();
        executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {  // Usamos executor para ejecutar la tarea en segundo plano
            Connection con = null;
            try {
                Class.forName(DataDB.driver);
                con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
                String query = "SELECT ID_Barrio, Descripcion FROM Barrios"; // Traemos tanto el ID como la Descripción
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int idBarrio = rs.getInt("ID_Barrio"); // Traemos el ID
                    String descripcion = rs.getString("Descripcion");
                    barrios.add(new Barrio(idBarrio, descripcion)); // Agregamos el objeto Barrio a la lista
                }

                rs.close();
                ps.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Esperamos a que termine la tarea en segundo plano antes de continuar (pero esto es sólo para ejemplo, puede ser mejor usar un Callback).
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS); // Esto espera un máximo de 5 segundos para que se complete la tarea
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
                        "AND O.Estado = 1 " +
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
                        "AND O.Estado = 1 " +
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

                String query = "SELECT " +
                        "CASE WHEN O.Estado = 1 THEN 'Activo' ELSE 'Inactivo' END AS Estado, " +
                        "COUNT(O.ID_Obstaculo) AS Cantidad " +
                        "FROM Obstaculos O " +
                        "WHERE O.FechaCreacion >= ? " +
                        "GROUP BY Estado";

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

    public int insertarPunto(Punto punto) {
        final int[] idPunto = {-1}; // Usamos un array para obtener el valor dentro del Executor

        executor.execute(() -> {
            Connection con = null;
            try {
                Class.forName(DataDB.driver);
                String query = "INSERT INTO Puntos (Latitud, Longitud, ID_Barrio) VALUES (?, ?, ?)";

                try (Connection conn = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
                     PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

                    ps.setDouble(1, punto.getLatitud());
                    ps.setDouble(2, punto.getLongitud());
                    ps.setInt(3, punto.getBarrio().getIdBarrio());

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
    public Punto obtenerPuntoPorId(int idPunto) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future<Punto> futurePunto = executorService.submit(() -> {
            Punto punto = null;

            try {
                Class.forName(DataDB.driver);
                String query = "SELECT ID_Punto, Latitud, Longitud, Altitud, ID_Barrio FROM Puntos WHERE ID_Punto = ?";

                try (Connection conn = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
                     PreparedStatement ps = conn.prepareStatement(query)) {

                    ps.setInt(1, idPunto);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {

                            double latitud = rs.getDouble("Latitud");
                            double longitud = rs.getDouble("Longitud");
                            int idBarrio = rs.getInt("ID_Barrio");

                            Barrio barrio = obtenerBarrioPorId(idBarrio); // Asumimos que este método devuelve un Barrio
                            punto = new Punto(idPunto, latitud, longitud, barrio);
                        }
                    }
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }

            return punto;
        });

        try {
            return futurePunto.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            return null;
        } finally {
            executorService.shutdown();
        }
    }

    public Barrio obtenerBarrioPorId(int idBarrio) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future<Barrio> futureBarrio = executorService.submit(() -> {
            Barrio barrio = null;

            try {
                Class.forName(DataDB.driver);
                String query = "SELECT ID_Barrio, Descripcion FROM Barrios WHERE ID_Barrio = ?";

                try (Connection conn = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
                     PreparedStatement ps = conn.prepareStatement(query)) {

                    ps.setInt(1, idBarrio); // Establecemos el ID_Barrio a buscar

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            String nombre = rs.getString("Descripcion");
                            barrio = new Barrio(idBarrio, nombre);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            return barrio;
        });

        try {
            return futureBarrio.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            return null;
        } finally {
            executorService.shutdown();
        }
    }
    public Usuario obtenerUsuarioPorId(int idUsuario) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future<Usuario> futureUsuario = executorService.submit(() -> {
            Usuario usuario = null; // Variable local para almacenar el Usuario

            try {
                Class.forName(DataDB.driver);
                String query = "SELECT " +
                        "ID_Usuario, Nombre, Apellido, DNI, CorreoElectronico AS Correo, " +
                        "Usuario AS User, Contrasena AS Pass, FechaRegistro, Puntaje, Estado " +
                        "FROM Usuarios " +
                        "WHERE ID_Usuario = ?";

                try (Connection conn = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
                     PreparedStatement ps = conn.prepareStatement(query)) {

                    ps.setInt(1, idUsuario); // Establecemos el ID_Usuario a buscar

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            String nombre = rs.getString("Nombre");
                            String apellido = rs.getString("Apellido");
                            String dni = rs.getString("DNI");
                            String correo = rs.getString("Correo");
                            String user = rs.getString("User");
                            String pass = rs.getString("Pass");
                            Date fechaRegistro = rs.getDate("FechaRegistro");
                            int puntaje = rs.getInt("Puntaje");
                            boolean estado = rs.getBoolean("Estado");

                            usuario = new Usuario(idUsuario, nombre, apellido, dni, user, pass, correo, fechaRegistro, puntaje, estado);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            return usuario;
        });

        try {
            return futureUsuario.get(5, TimeUnit.SECONDS); // Espera hasta 5 segundos
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            return null;
        } finally {
            executorService.shutdown();
        }
    }
    public boolean insertarObstaculo(Obstaculo obstaculo) {
        AtomicBoolean exito = new AtomicBoolean(false);
        int estado = 1;
        executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Connection con = null;
            try {
                Class.forName(DataDB.driver);
                con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);

                String query = "INSERT INTO Obstaculos (ID_TipoObstaculo, Comentarios, Imagen, FechaCreacion, ID_Usuario, ID_Punto, FechaBaja, ContadorSolucion, Estado) " +
                        "VALUES (?, ?, ?, NOW(), ?, ?, ?, ?, ?)";

                try (PreparedStatement ps = con.prepareStatement(query)) {
                    ps.setInt(1, obstaculo.getTipoObstaculo().getIdTipo());
                    ps.setString(2, obstaculo.getComentarios());
                    ps.setString(3, obstaculo.getImagen());
                    ps.setInt(4, obstaculo.getUsuario().getID_Usuario());
                    ps.setInt(5, obstaculo.getPunto().getIdPunto());
                    if (obstaculo.getFechaBaja() != null) {
                        ps.setDate(6, new java.sql.Date(obstaculo.getFechaBaja().getTime()));
                    } else {
                        ps.setNull(6, java.sql.Types.DATE); // Si es nula, dejamos el campo como NULL
                    }
                    ps.setInt(7, obstaculo.getContadorSolucion());

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

    public TipoObstaculo obtenerTipoObstaculoPorId(int id) {
        AtomicReference<TipoObstaculo> tipoObstaculo = new AtomicReference<>();

        executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Connection con = null;
            try {
                Class.forName(DataDB.driver);
                con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
                // Consulta actualizada con los nombres de columna correctos
                String query = "SELECT ID_TipoObstaculo, Descripcion, Estado FROM CatalogoObstaculos WHERE ID_TipoObstaculo = ? AND Estado = 1";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setInt(1, id);  // Establecemos el valor del parámetro ID
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    tipoObstaculo.set(new TipoObstaculo(
                            rs.getInt("ID_TipoObstaculo"),
                            rs.getString("Descripcion")
                    ));
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

        return tipoObstaculo.get();
    }

    ///
    public void obtenerID_Punto(String latitud, String longitud, OnIDPuntoListener listener) {
        executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            Connection con = null;
            int idPunto = -1; // Valor por defecto si no se encuentra el ID_Punto

            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);

                String query = "SELECT ID_Punto FROM Puntos WHERE Latitud = ? AND Longitud = ?";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, latitud);
                ps.setString(2, longitud);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    idPunto = rs.getInt("ID_Punto");
                }

                rs.close();
                ps.close();
                con.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            // Llama al listener para pasar el resultado al hilo principal
            final int finalIdPunto = idPunto;
            new Handler(Looper.getMainLooper()).post(() -> {
                listener.onIDPuntoObtenido(finalIdPunto);
            });
        });
    }

    public boolean obtenerCantObstaculos(int idUsuLog, int idPunto) {

        executor = Executors.newSingleThreadExecutor();
        final boolean[] existe = {false};

        executor.execute(() -> {
            Connection con = null;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
                String query = "SELECT COUNT(*) AS cantPuntuada FROM PuntuacionUsuarios WHERE ID_Obstaculo = ? AND ID_Usuario_que_puntua = ?";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setInt(1, idPunto);
                ps.setInt(2, idUsuLog);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    int count = rs.getInt("cantPuntuada");
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

    public interface OnIDPuntoListener {
        void onIDPuntoObtenido(int idPunto);
    }

    public Obstaculo obtenerObstaculo(int ID_Punto) {
       Obstaculo obstaculoobj= null;
        String query = "SELECT * FROM Obstaculos WHERE ID_Punto = ? AND Estado = 1";

        // Ejecuta la consulta en un nuevo hilo
        executor = Executors.newSingleThreadExecutor();
        Future<Obstaculo> result = executor.submit(() -> {
            Obstaculo obstaculo = new Obstaculo();
            try {
                Class.forName("com.mysql.jdbc.Driver");
                try (Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
                     PreparedStatement ps = con.prepareStatement(query)) {

                    // Establece el parÃ¡metro de la consulta
                    ps.setInt(1, ID_Punto);

                    // Ejecuta la consulta y procesa el resultado
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {


                        obstaculo.setIdObstaculo(rs.getInt("ID_Obstaculo"));
                        obstaculo.setComentarios(rs.getString("Comentarios"));
                        obstaculo.setImagen(rs.getString("Imagen"));
                        obstaculo.setFechaCreacion(rs.getDate("FechaCreacion"));
                        obstaculo.setFechaBaja(rs.getDate("FechaBaja"));
                        obstaculo.setContadorSolucion(rs.getInt("ContadorSolucion"));
                        obstaculo.setEstado(rs.getBoolean("Estado"));

                        // Instancia TipoObstaculo y Punto a partir del resultado
                        TipoObstaculo tipoObstaculo = new TipoObstaculo();
                        tipoObstaculo.setIdTipo(rs.getInt("ID_Tipo"));
                        tipoObstaculo.setDescripcion(rs.getString("Descripcion"));
                        obstaculo.setTipoObstaculo(tipoObstaculo);

                        Punto punto = new Punto();
                        punto.setIdPunto(ID_Punto);
                        punto.setLatitud(rs.getDouble("Latitud"));
                        punto.setLongitud(rs.getDouble("Longitud"));
                        obstaculo.setPunto(punto);

                        // Si necesitas incluir datos de Usuario, puedes hacer una consulta o incluir los valores en la consulta SQL principal
                        Usuario usuario = new Usuario();
                        usuario.setID_Usuario(rs.getInt("ID_Usuario"));
                        usuario.setNombre(rs.getString("Nombre"));
                        usuario.setApellido(rs.getString("Apellido"));
                        usuario.setDNI(rs.getString("DNI"));
                        usuario.setUser(rs.getString("User"));
                        usuario.setPass(rs.getString("Pass"));
                        usuario.setCorreo(rs.getString("Correo"));
                        usuario.setFechaRegistro(rs.getDate("FechaRegistro"));
                        usuario.setPuntaje(rs.getInt("Puntaje"));
                        usuario.setEstado(rs.getBoolean("Estado"));
                        obstaculo.setUsuario(usuario);
                    }
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return obstaculo;
        });

        // Espera el resultado y devuelve el objeto
        try {
            obstaculoobj = result.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        return obstaculoobj;
    }



}
