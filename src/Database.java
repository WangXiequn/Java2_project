import java.sql.*;

/**
 * @author 王协群
 * @date 2021/5/19 16:49
 */
public class Database {
    private static int ID = 0;
    private Connection con = null;
    private ResultSet resultSet;
    Database(){
        try
        {


            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Java2_Project.db");

            Statement stat = conn.createStatement();
            stat.executeUpdate( "create table if not exists Matrix\n" +
                    "(\n" +
                    "\tID integer not null constraint table_name_pk primary key autoincrement,size int,\n" +
                    "\tFix_Matrix TEXT,\n" +
                    "\tUser_Matrix TEXT not null,\n" +
                    "\tSudoku_Or_Magic_Square integer\n" +
                    ");\n" +
                    "\n");
            resultSet =  stat.executeQuery("select max(ID) from Matrix");
            while (resultSet.next()){
                ID=resultSet.getInt(1)+1;
                System.out.println(ID);
            }
            conn.close(); //结束数据库的连接

        }
        catch( Exception e )
        {
            e.printStackTrace ( );
        }
    }
    public void connectDB(){
        try {
            Class.forName("org.sqlite.JDBC");

        } catch (Exception e) {
            System.err.println("Cannot find the SQLite driver. Check CLASSPATH.");
            System.exit(1);
        }

        try {
            String url = "jdbc:sqlite:Java2_Project.db";
            con = DriverManager.getConnection(url);

        } catch (SQLException e) {
            System.err.println("Database connection failed");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
    public void closeDB(){
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void Save(int size,String Fix_Matrix,String User_Matrix,int SudokuOrNot){
        connectDB();
        String sql = "insert into Matrix values (?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1,ID);
            preparedStatement.setInt(2,size);
            preparedStatement.setString(3,Fix_Matrix);
            preparedStatement.setString(4,User_Matrix);
            preparedStatement.setInt(5,SudokuOrNot);
            preparedStatement.execute();
            ID++;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeDB();
        }
    }
    public void Load(int ID){
        connectDB();
        String sql = "select * from Matrix where ID = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1,ID);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int size = resultSet.getInt("size");
                String Fix_Matrix = resultSet.getString("Fix_Matrix");
                String User_Matrix = resultSet.getString("User_Matrix");
                int SudokuORNot = resultSet.getInt("Sudoku_Or_Magic_Square");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDB();
        }

    };
    public void Update(int ID,String User_Matrix){
        connectDB();
        String sql = "update Matrix SET User_Matrix =? where ID = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1,User_Matrix);
            preparedStatement.setInt(2,ID);
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDB();
        }
    }
    public int[][] StringToMatrix(int Dimension,String Matrix){
        int[][] arr = new int[Dimension][Dimension];
        String[] nums = Matrix.split(" ");
        for (int i = 0; i < Dimension; i++) {
            for (int j = 0; j < Dimension; j++) {
                arr[i][j] = Integer.parseInt(nums[i*Dimension+j]);
            }
        }
        return arr;
    }

    public boolean[][] StringToBooleanMatrix(int Dimension, String Matrix){
        boolean[][] arr = new boolean[Dimension][Dimension];
        String[] fixs = Matrix.split(" ");
        for (int i = 0; i < Dimension; i++) {
            for (int j = 0; j < Dimension; j++) {
                arr[i][j] = Boolean.parseBoolean(fixs[i*Dimension+j]);
            }
        }
        return arr;
    }
    public static void main(String[] args) {
        Database database = new Database();
        database.Save(1,"2","3",1);

    }
}
