//package aaron.plugin.sparx;
//
//import org.firebirdsql.jdbc.FBDriver;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//import java.io.File;
//import java.net.URL;
//import java.sql.*;
//
//public class FeapTest {
//
//    @Test
//    public void test() throws ClassNotFoundException, SQLException {
//        Class.forName("org.firebirdsql.jdbc.FBDriver");
//        FBDriver fbDriver = new FBDriver();
//
//        URL resource = ClassLoader.getSystemResource("Test.feap");
//        File file = new File(resource.getFile());
//        Assertions.assertNotNull(file);
//
//        String url = "jdbc:firebird:java:" + file.getAbsolutePath() + "?encoding=UTF8";
//        System.out.println(url);
//        System.out.println(fbDriver.acceptsURL(url));
//        try (Connection connection = fbDriver.connect(url, null)){
//            Statement statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery("SELECT Name FROM t_object;");
//            while(resultSet.next()){
//                //Display values
//                System.out.print("Name: " + resultSet.getString("Name"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}
