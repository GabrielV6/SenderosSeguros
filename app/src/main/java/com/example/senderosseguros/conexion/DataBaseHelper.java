package com.example.senderosseguros.conexion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    // Nombre y versión de la base de datos
    private static final String NOMBRE_DB = "autenticacion_usuarios.db";
    private static final int VERSION_DB = 1;

    // Nombre de la tabla y las columnas
    private static final String NOMBRE_TABLA = "intentos_login";
    private static final String COLUMNA_USUARIO = "usuario";
    private static final String COLUMNA_INTENTOS_FALLIDOS = "intentos_fallidos";
    private static final String COLUMNA_TIEMPO_BLOQUEO = "tiempo_bloqueo";

    public DataBaseHelper(Context context) {
        super(context, NOMBRE_DB, null, VERSION_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear la tabla para almacenar intentos fallidos y tiempo de bloqueo
        String crearTabla = "CREATE TABLE " + NOMBRE_TABLA + " (" +
                COLUMNA_USUARIO + " TEXT PRIMARY KEY, " +
                COLUMNA_INTENTOS_FALLIDOS + " INTEGER, " +
                COLUMNA_TIEMPO_BLOQUEO + " INTEGER);";
        db.execSQL(crearTabla);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eliminar la tabla si existe y crearla nuevamente
        db.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
        onCreate(db);
    }

    // Incrementa el número de intentos fallidos de un usuario
    public void incrementarIntentosFallidos(String usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Comprobar si el usuario ya existe en la base de datos
        Cursor cursor = db.query(NOMBRE_TABLA, new String[]{COLUMNA_INTENTOS_FALLIDOS}, COLUMNA_USUARIO + "=?",
                new String[]{usuario}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            // Incrementar el contador de intentos fallidos
            int intentosFallidos = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMNA_INTENTOS_FALLIDOS)) + 1;
            ContentValues valores = new ContentValues();
            valores.put(COLUMNA_INTENTOS_FALLIDOS, intentosFallidos);
            db.update(NOMBRE_TABLA, valores, COLUMNA_USUARIO + "=?", new String[]{usuario});
        } else {
            // Si el usuario no existe, lo insertamos con 1 intento fallido
            ContentValues valores = new ContentValues();
            valores.put(COLUMNA_USUARIO, usuario);
            valores.put(COLUMNA_INTENTOS_FALLIDOS, 1);
            valores.put(COLUMNA_TIEMPO_BLOQUEO, 0); // Inicialmente sin bloqueo
            db.insert(NOMBRE_TABLA, null, valores);
        }

        cursor.close();
        db.close();
    }

    // Obtiene el número de intentos fallidos de un usuario
    public int obtenerIntentosFallidos(String usuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(NOMBRE_TABLA, new String[]{COLUMNA_INTENTOS_FALLIDOS}, COLUMNA_USUARIO + "=?",
                new String[]{usuario}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int intentosFallidos = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMNA_INTENTOS_FALLIDOS));
            cursor.close();
            db.close();
            return intentosFallidos;
        }

        cursor.close();
        db.close();
        return 0;
    }

    // Reinicia los intentos fallidos a cero
    public void resetearIntentosFallidos(String usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(COLUMNA_INTENTOS_FALLIDOS, 0);
        db.update(NOMBRE_TABLA, valores, COLUMNA_USUARIO + "=?", new String[]{usuario});
        db.close();
    }

    // Establece el tiempo de bloqueo de un usuario
    public void establecerTiempoBloqueo(String usuario, long tiempoMillis) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(COLUMNA_TIEMPO_BLOQUEO, tiempoMillis);
        db.update(NOMBRE_TABLA, valores, COLUMNA_USUARIO + "=?", new String[]{usuario});
        db.close();
    }

    // Obtiene el tiempo de bloqueo de un usuario
    public long obtenerTiempoBloqueo(String usuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(NOMBRE_TABLA, new String[]{COLUMNA_TIEMPO_BLOQUEO}, COLUMNA_USUARIO + "=?",
                new String[]{usuario}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            long tiempoBloqueo = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMNA_TIEMPO_BLOQUEO));
            cursor.close();
            db.close();
            return tiempoBloqueo;
        }

        cursor.close();
        db.close();
        return 0;
    }

    // Verifica si un usuario está bloqueado
    public boolean estaBloqueado(String usuario, long duracionBloqueoMillis) {
        long tiempoBloqueo = obtenerTiempoBloqueo(usuario);
        if (tiempoBloqueo == 0) {
            return false; // La cuenta no está bloqueada si no hay tiempo de bloqueo
        }

        long tiempoActual = System.currentTimeMillis();
        return tiempoActual - tiempoBloqueo < duracionBloqueoMillis;
    }
}
