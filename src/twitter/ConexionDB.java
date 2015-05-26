package twitter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author emmanuel
 */
public class ConexionDB {
    public static Connection GetConnection()
    {
        Connection conexion=null;
      
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            String servidor = "jdbc:mysql://192.168.1.80/Twitter";
            String usuarioDB="root";
            String passwordDB="monitoreo12";
            conexion= DriverManager.getConnection(servidor,usuarioDB,passwordDB);
        }
        catch(ClassNotFoundException ex)
        {
            JOptionPane.showMessageDialog(null, ex, "Error1 en la Conexión con la BD "+ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            conexion=null;
        }
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex, "Error2 en la Conexión con la BD "+ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            conexion=null;
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, ex, "Error3 en la Conexión con la BD "+ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            conexion=null;
        }
        finally
        {
            return conexion;
        }
    }
    public ArrayList<String> getUsers (){
        ArrayList<String> usuarios = new ArrayList<>();
        Connection conexion;
        conexion = ConexionDB.GetConnection();
        
        try {
            Statement st = conexion.createStatement();
            String query = "SELECT usuario FROM Usuarios";
            
            ResultSet result = st.executeQuery(query);
            
            while (result.next()){
                usuarios.add(result.getString("usuario"));
            }
            conexion.close();
        } catch (SQLException ex) {
            System.out.println("Error en getUsers: " + ex);
        }
        
        return usuarios;
    }
    public void setDataUsers (String insert){
        Connection conexion;
        conexion = ConexionDB.GetConnection();
        
        try {
            Statement st = conexion.createStatement();
            
            st.executeUpdate(insert);
            conexion.close();
            
        } catch (SQLException ex) {
            System.out.println("Error en setDataUsers: " + ex);
        }
    }
    public void setTweets (String insert){
        Connection conexion;
        conexion = ConexionDB.GetConnection();
        try {
            Statement st = conexion.createStatement();
            
            st.executeUpdate(insert);
            conexion.close();
            
        } catch (SQLException ex) {
            System.out.println("Error en setTweets: " + ex);
        }
    }
    public void setTweets2 (String insert){
        Connection conexion;
        conexion = ConexionDB.GetConnection();
        try {
            Statement st = conexion.createStatement();
            
            st.executeUpdate(insert);
            conexion.close();
            
        } catch (SQLException ex) {
            System.out.println("Error en setTweets2: " + ex);
        }
    }
    public void setUsuario (String user){
        String insert = "INSERT IGNORE INTO Usuarios (usuario) VALUES('" + user + "')";
        Connection conexion;
        conexion = ConexionDB.GetConnection();
        
        try {
            Statement st = conexion.createStatement();
            
            st.executeUpdate(insert);
            conexion.close();
            
        } catch (SQLException ex) {
            System.out.println("Error en setUsuario: " + ex);
        }
    }
    public void setUserMentions (long idUsuario, String nombre, String usuario, String textoMencion, long idUsuarioPrincipal){
        nombre = nombre.replace("'", "");
        textoMencion = textoMencion.replace("'", "");
        String setUserMentions = "INSERT IGNORE INTO UsuariosMencionados (id, Nombre, Usuario, textoMencion, idUsuarioPrincipal) VALUES (" + idUsuario + ", '" + nombre + "', '" + usuario + "', '" + textoMencion + "', " + idUsuarioPrincipal + ")";
        Connection conexion;
        conexion = ConexionDB.GetConnection();
        
        try {
            Statement st = conexion.createStatement();
            
            st.executeUpdate(setUserMentions);
            conexion.close();
            
        } catch (SQLException ex) {
            System.out.println("Error en setUserMentions: " + ex);
        }
    }
    public int getIdUsuario (String nombre){
        int id = 0;
        String select = "SELECT id FROM Usuarios WHERE (Usuario = '" + nombre + "')";
        Connection conexion;
        conexion = ConexionDB.GetConnection();
        
        try {
            Statement st = conexion.createStatement();
            
            ResultSet result = st.executeQuery(select);
            while (result.next()){
                id = result.getInt("id");
            }
            conexion.close();
            
        } catch (SQLException ex) {
            System.out.println("Error en getIdUsuario: " + ex);
        }
        return id;
    }
    public void updateGeoLocation (double latitude, double longitude, long idTweet){
        String update = "update Tweets set Latitud = " + latitude + ", Longitud = " + longitude + " where idTweet = " + idTweet;
        Connection conexion;
        conexion = ConexionDB.GetConnection();
        
        try {
            Statement st = conexion.createStatement();
            
            st.executeUpdate(update);
            conexion.close();
            
        } catch (SQLException ex) {
            System.out.println("Error en updateGeoLocation: " + ex);
        }
    }
    public void insertListStatusesAgencias(long id, String text, String format, double latitude, double longitude, int retweetCount, boolean favorited, boolean retweeted, int favoriteCount, long id0) {
        String insert2 = "INSERT IGNORE INTO TweetsAgenciasNoticias (idTweet, Tweet, fechaPublicado, Latitud, Longitud, retweetedCount, isFavourite, isRetweeted, favouriteCount, idUsuarioPrincipal) VALUES(" + id + ", '" + text + "', '"
                            + format + "', " + latitude + ", " + longitude + ", " + retweetCount + ", '" + favorited + "', '" + retweeted + "', " + favoriteCount + ", " + id0 + ")";
        Connection conexion2;
        conexion2 = ConexionDB.GetConnection();
        
        try {
            Statement st2 = conexion2.createStatement();
            
            st2.executeUpdate(insert2);
            conexion2.close();
            
        } catch (SQLException ex) {
            System.out.println("Error en insertListStatusesAgencias: " + ex);
        }
    }
    public void insertListStatusesPeriodicos(long id, String text, String format, double latitude, double longitude, int retweetCount, boolean favorited, boolean retweeted, int favoriteCount, long id0) {
        String insert2 = "INSERT IGNORE INTO TweetsPeriodicos (idTweet, Tweet, fechaPublicado, Latitud, Longitud, retweetedCount, isFavourite, isRetweeted, favouriteCount, idUsuarioPrincipal) VALUES(" + id + ", '" + text + "', '"
                            + format + "', " + latitude + ", " + longitude + ", " + retweetCount + ", '" + favorited + "', '" + retweeted + "', " + favoriteCount + ", " + id0 + ")";
        Connection conexion2;
        conexion2 = ConexionDB.GetConnection();
        
        try {
            Statement st2 = conexion2.createStatement();
            
            st2.executeUpdate(insert2);
            conexion2.close();
            
        } catch (SQLException ex) {
            System.out.println("Error en insertListStatusesPeriodicos: " + ex);
        }
    }
    public void insertListStatusesColumnistas(long id, String text, String format, double latitude, double longitude, int retweetCount, boolean favorited, boolean retweeted, int favoriteCount, long id0) {
        String insert2 = "INSERT IGNORE INTO TweetsColumnistas (idTweet, Tweet, fechaPublicado, Latitud, Longitud, retweetedCount, isFavourite, isRetweeted, favouriteCount, idUsuarioPrincipal) VALUES(" + id + ", '" + text + "', '"
                            + format + "', " + latitude + ", " + longitude + ", " + retweetCount + ", '" + favorited + "', '" + retweeted + "', " + favoriteCount + ", " + id0 + ")";
        Connection conexion2;
        conexion2 = ConexionDB.GetConnection();
        
        try {
            Statement st2 = conexion2.createStatement();
            
            st2.executeUpdate(insert2);
            conexion2.close();
            
        } catch (SQLException ex) {
            System.out.println("Error en insertListStatusesColumnistas: " + ex);
        }
    }
    public void setHashTags(String insert){
        Connection conex;
        conex = ConexionDB.GetConnection();
        
        try{
            try (Statement s = conex.createStatement()) {
                s.executeUpdate(insert);
                conex.close();
            }
        }
        catch(SQLException ew ){
            System.out.println("Error en setHashTags: " + ew);
        }
    }
    public String[] getKey (int id){
        String[] keys = new String [4];
        Connection conex;
        conex = ConexionDB.GetConnection();
        
        String getLlave = "SELECT consumerKey, secretConsumerKey, accessToken, secretAccessToken FROM llaves WHERE (id = " + id + ")";
        
        try{
            try (Statement s = conex.createStatement()) {
                ResultSet result = s.executeQuery(getLlave);
                while (result.next()){
                    keys[0] = result.getString("consumerKey");
                    keys[1] = result.getString("secretConsumerKey");
                    keys[2] = result.getString("accessToken");
                    keys[3] = result.getString("secretAccessToken");
                }
                conex.close();
            }
        }
        catch(SQLException ew){
            System.out.println("Error en seleccion de key: " + ew);
        }
        return keys;
    }
    public void updateTipoMedia(long idTweetPrincipal, String tipo, String medio){
        Connection conex;
        conex = ConexionDB.GetConnection();
        
        String update = "UPDATE Tweets SET tipo = '" + tipo + "', media = '" + medio + "' WHERE idTweet = " + idTweetPrincipal;
        
        try{
            try (Statement s = conex.createStatement()) {
                s.executeUpdate(update);
                conex.close();
            }
        }
        catch(SQLException ew){
            System.out.println("Error en actualizacion de tipo media: " + ew);
        }
    }
    public void updateTipoMedia2(long idTweetPrincipal, String tipo, String medio){
        Connection conex;
        conex = ConexionDB.GetConnection();
        
        String update = "UPDATE searchTwitter SET tipo = '" + tipo + "', media = '" + medio + "' WHERE idTweet = " + idTweetPrincipal;
        
        try{
            try (Statement s = conex.createStatement()) {
                s.executeUpdate(update);
                conex.close();
            }
        }
        catch(SQLException ew){
            System.out.println("Error en actualizacion de tipo media: " + ew);
        }
    }
    public void updateUsuarioOrigen(long idUsuarioPrincipal, String usuarioOrigen, long idUsuarioOrigen){
        Connection conex;
        conex = ConexionDB.GetConnection();
        
        String update = "UPDATE Tweets SET idUser = '" + idUsuarioOrigen + "' WHERE idUsuarioPrincipal = " + idUsuarioPrincipal;
        
        try{
            try (Statement s = conex.createStatement()) {
                s.executeUpdate(update);
                conex.close();
            }
        }
        catch(SQLException ew){
            System.out.println("Error en actualizacion de idUsuarioOrigen y nombreUsuario: " + ew);
        }
    }
}
