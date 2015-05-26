package twitter;

/**
 *
 * @author emmanuel
 */
import com.twitterapime.search.Tweet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.PagableResponseList;
import twitter4j.Paging;
import twitter4j.Place;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserMentionEntity;
import twitter4j.auth.AccessToken;

public class GetUserStatus {

    ConexionDB con;
    Twitter twitter;
    SimpleDateFormat formateador;
    SimpleDateFormat formateador2;
    int numSel;
    long cursor;
    String nombreUsuario;

    public GetUserStatus() {
        formateador = new SimpleDateFormat("yyyy-MM-dd");
        formateador2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //	My Applications Consumer and Auth Access Token
        con = new ConexionDB();
    }

    public void procesar() {
        System.out.println("Llave seleccionada: " + numSel);
        ArrayList<String> users = new ArrayList<>();
        users = con.getUsers();
        int contador = 1;
        for (String user : users) {
            if (contador <= 16) {
                twitter = new TwitterFactory().getInstance();
                setKey(1);
            } else if (contador > 15 && contador <= 30) {
                twitter = new TwitterFactory().getInstance();
                setKey(2);
            } else if (contador > 30 && contador <= 45) {
                twitter = new TwitterFactory().getInstance();
                setKey(3);
            } else if (contador > 45 && contador <= 60) {
                twitter = new TwitterFactory().getInstance();
                setKey(4);
            } else if (contador > 60 && contador <= 75) {
                twitter = new TwitterFactory().getInstance();
                setKey(5);
            } else if (contador > 75 && contador <= 90) {
                twitter = new TwitterFactory().getInstance();
                setKey(6);
            } else if (contador > 90 && contador <= 105) {
                twitter = new TwitterFactory().getInstance();
                setKey(7);
            }

            try {

                User a = twitter.showUser(user);
                nombreUsuario = a.getScreenName();
                System.out.println("************* Datos del usuario principal**************");
                System.out.println("\nid: " + a.getId());
                System.out.println("Nombre: " + a.getName());
                System.out.println("Usuario: " + nombreUsuario);
                System.out.println("Descripcion: " + a.getDescription());
                System.out.println("Ubicacion: " + a.getLocation());
                System.out.println("Se unio el: " + formateador.format(a.getCreatedAt()));
                System.out.println("Tweets: " + a.getStatusesCount());
                System.out.println("Siguiendo: " + a.getFriendsCount());
                System.out.println("Seguidores: " + a.getFollowersCount());
                System.out.println("Favoritos: " + a.getFavouritesCount());
                System.out.println("Listas: " + a.getListedCount());
                System.out.println("Foto: " + a.getBiggerProfileImageURL());
                cursor = -1;

                System.out.println("\n*************FIN DATOS DE USUARIO++++++++++++++++++");
                ConexionDB conexion = new ConexionDB();
                int id = conexion.getIdUsuario(a.getScreenName());
                String insert = "INSERT INTO users (id, Nombre, Usuario, Descripcion, Ubicacion, fechaCreacion, tweetsCount, friendsCount, followersCount, favouritesCount, listedCount, profileImageURL, idUsuario)"
                        + " values (" + a.getId() + ", '" + a.getName() + "', '" + a.getScreenName() + "', '" + a.getDescription() + "', '" + a.getLocation() + "', '" + formateador.format(a.getCreatedAt()) + "', " + a.getStatusesCount() + ", "
                        + "" + a.getFriendsCount() + ", " + a.getFollowersCount() + ", " + a.getFavouritesCount() + ", " + a.getListedCount() + ", '" + a.getBiggerProfileImageURL() + "', " + id + ") ON DUPLICATE KEY UPDATE "
                        + "id = VALUES(id), Nombre = VALUES(Nombre), Usuario = VALUES(Usuario), Descripcion = VALUES(Descripcion), Ubicacion = VALUES(Ubicacion), fechaCreacion = VALUES(fechaCreacion), tweetsCount = VALUES(tweetsCount), "
                        + "friendsCount = VALUES(friendsCount), followersCount = VALUES(followersCount), favouritesCount = VALUES(favouritesCount), listedCount = VALUES(listedCount), profileImageURL = VALUES(profileImageURL), idUsuario = VALUES(idUsuario)";

                conexion.setDataUsers(insert);
                setTweets("@" + a.getScreenName());
                setTweets(a.getName());
            } catch (Exception e) {
                System.err.println("Usuario: " + user + " Error: " + e);
            }

            contador++;
        }
        contador = 0;
    }

    public void setKey(int seleccion) {
        ConexionDB conx = new ConexionDB();
        String[] keyObtenida = new String[4];
        keyObtenida = conx.getKey(seleccion);

        twitter.setOAuthConsumer(keyObtenida[0], keyObtenida[1]);
        twitter.setOAuthAccessToken(new AccessToken(keyObtenida[2], keyObtenida[3]));

        System.out.println("**************************************************  " + seleccion + "  ************************************************************************************************************************************************************");
    }

    public void setTweets(String user) {
        System.out.println("Busqueda iniciada!");
        System.out.println("Buscando.....");
        Date fecha = new Date();
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        String busqueda = nombreUsuario;
        Query query = new Query(busqueda);
        query.since(formater.format(fecha));
        try {
            QueryResult result = twitter.search(query);
            System.out.println("Resultados encontrados: " + result.getTweets().size());

            for (Status tweet : result.getTweets()) {
                GeoLocation geoLocation = tweet.getGeoLocation();
                double latitude = 0;
                double longitude = 0;
                if (geoLocation != null) {
                    latitude = geoLocation.getLatitude();
                    longitude = geoLocation.getLongitude();
                }

                Place lugar = null;
                String lugarTweet = "";
                String lugarCompletoTweet = "";
                String ciudad = "";

                if (tweet.getPlace() != null) {
                    lugar = tweet.getPlace();
                    lugarTweet = lugar.getName();
                    lugarCompletoTweet = lugar.getFullName();
                    ciudad = lugar.getCountry();
                    System.out.println("lugar: " + lugarTweet);
                    System.out.println("Nombre completo lugar: " + lugarCompletoTweet);
                    System.out.println("Ciudad: " + ciudad);
                } else {
                    lugarTweet = null;
                    lugarCompletoTweet = null;
                    ciudad = null;
                }
                String medio = "";
                String tipo = "";
                long idTweetMedia = -1;

                MediaEntity[] media = tweet.getMediaEntities();
                if (media != null || !media.equals("")) {
                    for (MediaEntity media1 : media) {
                        System.out.println("media: " + media1.getMediaURL());
                        System.out.println("Tipo: " + media1.getType());
                        medio = media1.getMediaURL();
                        tipo = media1.getType();
                        idTweetMedia = tweet.getId();
                    }
                }

                String reemplazoTweet = tweet.getText().replace("'", "");
                String insert2 = "INSERT IGNORE INTO searchTwitter (idTweet, Tweet, fechaPublicado, Latitud, Longitud, retweetedCount, isFavourite, isRetweeted, favouriteCount, idUsuarioPrincipal, nombreLugar, nombreCompletoLugar, ciudad) VALUES(" + tweet.getId() + ", '" + reemplazoTweet + "', '"
                        + formateador2.format(tweet.getCreatedAt()) + "', " + latitude + ", " + longitude + ", " + tweet.getRetweetCount() + ", '" + tweet.isFavorited() + "', '" + tweet.isRetweetedByMe() + "', " + tweet.getFavoriteCount() + ", " + tweet.getUser().getId() + ", '" + lugarTweet + "', '" + lugarCompletoTweet + "', '" + ciudad + "')";
                con.setTweets2(insert2);
                con.updateTipoMedia2(idTweetMedia, tipo, medio);
            }
            System.out.println("Terminado!");

        } catch (TwitterException ex) {
            Logger.getLogger(GetUserStatus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    void procesarListas() {
//        try {
//            long cursor = -1;
//            User admonitor = twitter.showUser("MonitorOfficial");
//            Paging paging2 = new Paging(1, 100);
//            double latitude = 0;
//            double longitude = 0;
//
//            ResponseList<UserList> response = twitter.getUserLists(admonitor.getId());
//            for (UserList response1 : response) {
//                System.out.println("*********************TWEETS LISTAS DE ADMONITOR OFFICIAL********************");
//                System.out.println(response1.getName());
//                System.out.println(admonitor.getId());
//                switch (response1.getName()) {
//                    case "Agencias de Noticias":
//                        ResponseList<Status> statusList = twitter.getUserListStatuses(response1.getId(), paging2);
//
//                        for (Status status : statusList) {
//                            System.out.println("idTweet: " + status.getId());
//                            System.out.println("Nombre: " + status.getUser().getName());
//                            System.out.println("usuario: " + status.getUser().getScreenName());
//                            System.out.println("Tweet: " + status.getText());
//                            System.out.println("fechaPublicado: " + formateador2.format(status.getCreatedAt()));
//                            GeoLocation geoLocation2 = status.getGeoLocation();
//                            if (geoLocation2 != null) {
//                                latitude = geoLocation2.getLatitude();
//                                longitude = geoLocation2.getLongitude();
//                                System.out.println("Latitud: " + latitude);
//                                System.out.println("Longitud: " + longitude);
//                            }
//                            System.out.println("retweetedCount: " + status.getRetweetCount());
//                            System.out.println("isFavourite: " + status.isFavorited());
//                            System.out.println("isRetweeted: " + status.isRetweeted());
//                            System.out.println("favouriteCount: " + status.getFavoriteCount());
//                            System.out.println("usuario principal: " + admonitor.getId());
//                            System.out.println("------------------------------------------");
//                            String reemplazo = status.getText().replace("'", "");
//                            con.insertListStatusesAgencias(status.getId(), reemplazo, formateador2.format(status.getCreatedAt()), latitude, longitude, status.getRetweetCount(), status.isFavorited(), status.isRetweeted(), status.getFavoriteCount(), admonitor.getId());
//                        }
//                        System.out.println("*************FIN TWEETS LISTAS DE ADMONITOR OFFICIAL**************");
//
//                        break;
//                    case "Periodicos":
//                        ResponseList<Status> statusList2 = twitter.getUserListStatuses(response1.getId(), paging2);
//
//                        for (Status status2 : statusList2) {
//                            System.out.println("idTweet: " + status2.getId());
//                            System.out.println("Nombre: " + status2.getUser().getName());
//                            System.out.println("usuario: " + status2.getUser().getScreenName());
//                            System.out.println("Tweet: " + status2.getText());
//                            System.out.println("fechaPublicado: " + formateador2.format(status2.getCreatedAt()));
//                            GeoLocation geoLocation2 = status2.getGeoLocation();
//                            if (geoLocation2 != null) {
//                                latitude = geoLocation2.getLatitude();
//                                longitude = geoLocation2.getLongitude();
//                                System.out.println("Latitud: " + latitude);
//                                System.out.println("Longitud: " + longitude);
//                            }
//                            System.out.println("retweetedCount: " + status2.getRetweetCount());
//                            System.out.println("isFavourite: " + status2.isFavorited());
//                            System.out.println("isRetweeted: " + status2.isRetweeted());
//                            System.out.println("favouriteCount: " + status2.getFavoriteCount());
//                            System.out.println("usuario principal: " + admonitor.getId());
//                            System.out.println("------------------------------------------");
//                            String reemplazo = status2.getText().replace("'", "");
//                            con.insertListStatusesPeriodicos(status2.getId(), reemplazo, formateador2.format(status2.getCreatedAt()), latitude, longitude, status2.getRetweetCount(), status2.isFavorited(), status2.isRetweeted(), status2.getFavoriteCount(), admonitor.getId());
//                        }
//                        System.out.println("*************FIN TWEETS LISTAS DE ADMONITOR OFFICIAL**************");
//
//                        break;
//                    case "Columnistas":
//                        ResponseList<Status> statusList3 = twitter.getUserListStatuses(response1.getId(), paging2);
//
//                        for (Status status3 : statusList3) {
//                            System.out.println("idTweet: " + status3.getId());
//                            System.out.println("Nombre: " + status3.getUser().getName());
//                            System.out.println("usuario: " + status3.getUser().getScreenName());
//                            System.out.println("Tweet: " + status3.getText());
//                            System.out.println("fechaPublicado: " + formateador2.format(status3.getCreatedAt()));
//                            GeoLocation geoLocation2 = status3.getGeoLocation();
//                            if (geoLocation2 != null) {
//                                latitude = geoLocation2.getLatitude();
//                                longitude = geoLocation2.getLongitude();
//                                System.out.println("Latitud: " + latitude);
//                                System.out.println("Longitud: " + longitude);
//                            }
//                            System.out.println("retweetedCount: " + status3.getRetweetCount());
//                            System.out.println("isFavourite: " + status3.isFavorited());
//                            System.out.println("isRetweeted: " + status3.isRetweeted());
//                            System.out.println("favouriteCount: " + status3.getFavoriteCount());
//                            System.out.println("usuario principal: " + admonitor.getId());
//                            System.out.println("------------------------------------------");
//                            String reemplazo = status3.getText().replace("'", "");
//                            con.insertListStatusesColumnistas(status3.getId(), reemplazo, formateador2.format(status3.getCreatedAt()), latitude, longitude, status3.getRetweetCount(), status3.isFavorited(), status3.isRetweeted(), status3.getFavoriteCount(), admonitor.getId());
//                        }
//                        System.out.println("*************FIN TWEETS LISTAS DE ADMONITOR OFFICIAL**************");
//                        break;
//                }
//
//            }
//        } catch (TwitterException ex) {
//            Logger.getLogger(GetUserStatus.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}

//for (Status statuse : statuses) {
//                    HashtagEntity[] hashtags = statuse.getHashtagEntities();
//                    lista = statuse.getContributors();
//                    System.out.println("***************DATOS DEL TWEET****************");
//                    System.out.println("\nid: " + statuse.getId());
//                    System.out.println("Tweet: " + statuse.getText());
//                    GeoLocation geoLocation = statuse.getGeoLocation();
//                    if (geoLocation != null) {
//                        latitude = geoLocation.getLatitude();
//                        longitude = geoLocation.getLongitude();
//                        System.out.println("Latitud: " + latitude);
//                        System.out.println("Longitud: " + longitude);
//                    }
//                    String medio = "";
//                    String tipo = "";
//                    long idTweetMedia = -1;
//
//                    MediaEntity[] media = statuse.getMediaEntities();
//                    if (media != null || !media.equals("")) {
//                        for (MediaEntity media1 : media) {
//                            System.out.println("media: " + media1.getMediaURL());
//                            System.out.println("Tipo: " + media1.getType());
//                            medio = media1.getMediaURL();
//                            tipo = media1.getType();
//                            idTweetMedia = statuse.getId();
//                        }
//                    }
//                    System.out.println("retweetedCount: " + statuse.getRetweetCount());
//
//                    if (statuse.isFavorited()) {
//                        System.out.println("isFavourite: Si");
//                    } else {
//                        System.out.println("isFavourite: No");
//                    }
//
//                    if (statuse.isRetweetedByMe()) {
//                        System.out.println("isRetweeted: Si");
//                    } else {
//                        System.out.println("isRetweeted: No");
//                    }
//
//                    Status sta = statuse.getRetweetedStatus();
//                    if (sta != null) {
//                        System.out.println("Origen del retweet: ");
//                        System.out.println("                  id: " + sta.getUser().getId());
//                        System.out.println("                  Nombre: " + sta.getUser().getName());
//                        System.out.println("                  Usuario: " + sta.getUser().getScreenName());
//
//                        String insert2 = "INSERT INTO users (id, Nombre, Usuario, Descripcion, Ubicacion, fechaCreacion, tweetsCount, friendsCount, followersCount, favouritesCount, listedCount, profileImageURL, idUsuario)"
//                                + " values (" + sta.getUser().getId() + ", '" + sta.getUser().getName() + "', '" + sta.getUser().getScreenName() + "', '" + sta.getUser().getDescription() + "', '" + sta.getUser().getLocation() + "', '" + formateador.format(sta.getUser().getCreatedAt()) + "', " + sta.getUser().getStatusesCount() + ", "
//                                + "" + sta.getUser().getFriendsCount() + ", " + sta.getUser().getFollowersCount() + ", " + sta.getUser().getFavouritesCount() + ", " + sta.getUser().getListedCount() + ", '" + sta.getUser().getBiggerProfileImageURL() + "', null) ON DUPLICATE KEY UPDATE "
//                                + "id = VALUES(id), Nombre = VALUES(Nombre), Usuario = VALUES(Usuario), Descripcion = VALUES(Descripcion), Ubicacion = VALUES(Ubicacion), fechaCreacion = VALUES(fechaCreacion), tweetsCount = VALUES(tweetsCount), friendsCount = VALUES(friendsCount), "
//                                + "followersCount = VALUES(followersCount), favouritesCount = VALUES(favouritesCount), listedCount = VALUES(listedCount), profileImageURL = VALUES(profileImageURL), idUsuario = VALUES(idUsuario)";
//
//                        con.updateUsuarioOrigen(a.getId(), sta.getUser().getScreenName(), sta.getUser().getId());
//                        con.setDataUsers(insert2);
//                    }
//
//                    System.out.print("ContributorsID: ");
//                    for (int i = 0; i < lista.length; i++) {
//                        System.out.print(lista[i] + " ");
//                    }
//                    System.out.print("\nHashtags:");
//
//                    if (hashtags != null) {
//                        for (HashtagEntity hashtag : hashtags) {
//                            System.out.print(" #" + hashtag.getText());
//                            String insertHash = "insert ignore into HashTag (idTweet, hashtag) values (" + statuse.getId() + ", '#" + hashtag.getText() + "')";
//                            conexion.setHashTags(insertHash);
//                        }
//                    }
//                    Place lugar = null;
//                    String lugarTweet = "";
//                    String lugarCompletoTweet = "";
//                    String ciudad = "";
//
//                    if (statuse.getPlace() != null) {
//                        lugar = statuse.getPlace();
//                        lugarTweet = lugar.getName();
//                        lugarCompletoTweet = lugar.getFullName();
//                        ciudad = lugar.getCountry();
//                        System.out.println("lugar: " + lugarTweet);
//                        System.out.println("Nombre completo lugar: " + lugarCompletoTweet);
//                        System.out.println("Ciudad: " + ciudad);
//                    } else {
//                        lugarTweet = null;
//                        lugarCompletoTweet = null;
//                        ciudad = null;
//                    }
//
//                    System.out.println();
//                    userMentions = statuse.getUserMentionEntities();
//
//                    String reemplazoTweet = statuse.getText().replace("'", "");
//                    String insert2 = "INSERT IGNORE INTO Tweets (idTweet, Tweet, fechaPublicado, Latitud, Longitud, retweetedCount, isFavourite, isRetweeted, favouriteCount, idUsuarioPrincipal, nombreLugar, nombreCompletoLugar, ciudad) VALUES(" + statuse.getId() + ", '" + reemplazoTweet + "', '"
//                            + formateador2.format(statuse.getCreatedAt()) + "', " + latitude + ", " + longitude + ", " + statuse.getRetweetCount() + ", '" + statuse.isFavorited() + "', '" + statuse.isRetweetedByMe() + "', " + statuse.getFavoriteCount() + ", " + a.getId() + ", '" + lugarTweet + "', '" + lugarCompletoTweet + "', '" + ciudad + "',)";
//
//                    conexion.setTweets(insert2);
//                    con.updateTipoMedia(idTweetMedia, tipo, medio);
//                    System.out.println();
//                    System.out.println("userMentionEntities: ");
//                    if (userMentions != null) {
//
//                        for (UserMentionEntity userMention : userMentions) {
//                            System.out.println("                     id: " + userMention.getId());
//                            System.out.println("                     Nombre: " + userMention.getName());
//                            System.out.println("                     usuario: " + userMention.getScreenName());
//                            System.out.println("                     texto: " + userMention.getText() + "\n");
////                        User c = twitter.showUser(userMention.getScreenName());
////                        System.out.println("---------------------------> DATOS DE USUARIO MENCIONADO");
////                        System.out.println("                             id: " + c.getId());
////                        System.out.println("                             Nombre: " + c.getName());
////                        System.out.println("                             Usuario: " + c.getScreenName());
////                        System.out.println("                             Descripcion: " + c.getDescription());
////                        System.out.println("                             Ubicacion: " + c.getLocation());
////                        System.out.println("                             Link Facebook: " + c.getURL());
////                        System.out.println("                             Se unio el: " + formateador.format(c.getCreatedAt()));
////                        System.out.println("                             Tweets: " + c.getStatusesCount());
////                        System.out.println("                             Siguiendo: " + c.getFriendsCount());
////                        System.out.println("                             Seguidores: " + c.getFollowersCount());
////                        System.out.println("                             Favoritos: " + c.getFavouritesCount());
////                        System.out.println("                             Listas: " + c.getListedCount());
////                        System.out.println("                             Foto: " + c.getBiggerProfileImageURL());
////                        System.out.println("---------------------------> FIN DATOS DE USUARIO MENCIONADO");
//
//                            conexion.setUserMentions(userMention.getId(), userMention.getName(), userMention.getScreenName(), userMention.getText(), a.getId());
//                        }
//                    }
//
//                    System.out.println("\n******************FIN DATOS DEL TWEET***************");
//                }
//                cursor = -1;
//                ResponseList<UserList> response = twitter.getUserLists(a.getId());
//                if (response != null) {
//                    for (UserList response1 : response) {
//                        System.out.println("*************LISTAS****************");
//                        System.out.println(response1.getName());
//                        System.out.println(response1.getId());
//                        PagableResponseList<User> usersList = twitter.getUserListMembers(a.getId(), response1.getName(), cursor);
//                        for (User usersList1 : usersList) {
//                            System.out.println("      " + usersList1.getId());
//                            System.out.println("      " + usersList1.getName());
//                            System.out.println("      " + usersList1.getScreenName());
//                            System.out.println("      " + usersList1.getDescription());
//                            System.out.println("      " + usersList1.getStatusesCount());
//                            System.out.println("------------------------------------------");
//                        }
//                        System.out.println("*************FIN LISTAS**************");
//                    }
//                }
//
//            } catch (Exception e) {
//                System.err.println("Usuario: " + user + " Error: " + e);
//            }
//            String busqueda = nombreUsuario;
//            Query query = new Query(busqueda);
//            query.since("2015-04-06");
//            try {
//                QueryResult result = twitter.search(query);
//                System.out.println("Resultados encontrados: " + result.getTweets().size());
//                
//                for (Status tweet : result.getTweets()) {
//                    GeoLocation geoLocation = tweet.getGeoLocation();
//                    double latitude = 0;
//                    double longitude = 0;
//                    if (geoLocation != null) {
//                        latitude = geoLocation.getLatitude();
//                        longitude = geoLocation.getLongitude();
//                    }
//                    
//                    Place lugar = null;
//                    String lugarTweet = "";
//                    String lugarCompletoTweet = "";
//                    String ciudad = "";
//
//                    if (tweet.getPlace() != null) {
//                        lugar = tweet.getPlace();
//                        lugarTweet = lugar.getName();
//                        lugarCompletoTweet = lugar.getFullName();
//                        ciudad = lugar.getCountry();
//                        System.out.println("lugar: " + lugarTweet);
//                        System.out.println("Nombre completo lugar: " + lugarCompletoTweet);
//                        System.out.println("Ciudad: " + ciudad);
//                    } else {
//                        lugarTweet = null;
//                        lugarCompletoTweet = null;
//                        ciudad = null;
//                    }
//                    String medio = "";
//                    String tipo = "";
//                    long idTweetMedia = -1;
//
//                    MediaEntity[] media = tweet.getMediaEntities();
//                    if (media != null || !media.equals("")) {
//                        for (MediaEntity media1 : media) {
//                            System.out.println("media: " + media1.getMediaURL());
//                            System.out.println("Tipo: " + media1.getType());
//                            medio = media1.getMediaURL();
//                            tipo = media1.getType();
//                            idTweetMedia = tweet.getId();
//                        }
//                    }
//                    
//                    String reemplazoTweet = tweet.getText().replace("'", "");
//                    String insert2 = "INSERT IGNORE INTO Tweets2 (idTweet, Tweet, fechaPublicado, Latitud, Longitud, retweetedCount, isFavourite, isRetweeted, favouriteCount, idUsuarioPrincipal, nombreLugar, nombreCompletoLugar, ciudad) VALUES(" + tweet.getId() + ", '" + reemplazoTweet + "', '"
//                            + formateador2.format(tweet.getCreatedAt()) + "', " + latitude + ", " + longitude + ", " + tweet.getRetweetCount() + ", '" + tweet.isFavorited() + "', '" + tweet.isRetweetedByMe() + "', " + tweet.getFavoriteCount() + ", " + tweet.getUser().getId() + ", '" + lugarTweet + "', '" + lugarCompletoTweet + "', '" + ciudad + "')";
//                    con.setTweets2(insert2);
//                    con.updateTipoMedia2(idTweetMedia, tipo, medio);
//                }
//                PagableResponseList<User> listaFollowers = twitter.getFollowersList(user, cursor);
//                System.out.println("************* Followers **********************************");
//                for (User listaFollower : listaFollowers) {
//                    System.out.println("Followers: ");
//                    System.out.println("       ID: " + listaFollower.getId());
//                    System.out.println("       Nombre: " + listaFollower.getName());
//                    System.out.println("       Usuario: " + listaFollower.getScreenName());
//                    System.out.println("       Descripcion: " + listaFollower.getDescription());
//                    System.out.println("       Ubicacion: " + listaFollower.getLocation());
//                    System.out.println("       Se unio el: " + formateador.format(listaFollower.getCreatedAt()));
//                    System.out.println("       Tweets: " + listaFollower.getStatusesCount());
//                    System.out.println("       Siguiendo: " + listaFollower.getFriendsCount());
//                    System.out.println("       Seguidores: " + listaFollower.getFollowersCount());
//                    System.out.println("       Favoritos: " + listaFollower.getFavouritesCount());
//                    System.out.println("       Listas: " + listaFollower.getListedCount());
//                    System.out.println("       Foto: " + listaFollower.getBiggerProfileImageURL());
//                }
//                System.out.println("************* FIN Followers ******************************");
//                PagableResponseList<User> listaFriends = twitter.getFriendsList(user, cursor);
//                System.out.println("************* Friends ************************************");
//                for (User listaFriend : listaFriends) {
//                    System.out.println("Friends: ");
//                    System.out.println("       ID: " + listaFriend.getId());
//                    System.out.println("       Nombre: " + listaFriend.getName());
//                    System.out.println("       Usuario: " + listaFriend.getScreenName());
//                    System.out.println("       Descripcion: " + listaFriend.getDescription());
//                    System.out.println("       Ubicacion: " + listaFriend.getLocation());
//                    System.out.println("       Se unio el: " + formateador.format(listaFriend.getCreatedAt()));
//                    System.out.println("       Tweets: " + listaFriend.getStatusesCount());
//                    System.out.println("       Siguiendo: " + listaFriend.getFriendsCount());
//                    System.out.println("       Seguidores: " + listaFriend.getFollowersCount());
//                    System.out.println("       Favoritos: " + listaFriend.getFavouritesCount());
//                    System.out.println("       Listas: " + listaFriend.getListedCount());
//                    System.out.println("       Foto: " + listaFriend.getBiggerProfileImageURL());
//                }
//                System.out.println("************* FIN Friends *********************************");
