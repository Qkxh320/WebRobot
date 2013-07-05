package me.yongbo.robot.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import me.yongbo.bean.Meitu91Image;

public class DbHelper {
	// 连接驱动
	private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	// 连接路径
	private static final String CONN_STRING = "jdbc:sqlserver://localhost:1433;databaseName=webapp";
	// 用户名
	private static final String USERNAME = "sa";
	// 密码
	private static final String PASSWORD = "sql@2013";

	
	// 静态代码块
	static {
		try {
			// 加载驱动
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public DbHelper(){}

	/**
	 * 获取数据库连接
	 */
	public Connection getConnection() {
		Connection conn = null;
		//System.out.println("开始连接数据库");
		try {
			conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
			//System.out.println("数据库连接失败");
		}
		return conn;
	}

	/**
	 * 关闭数据库连接，注意关闭的顺序
	 */
	public void close(ResultSet rs, PreparedStatement ps, Connection conn) {
		if (rs != null) {
			try {
				rs.close();
				rs = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (ps != null) {
			try {
				ps.close();
				ps = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
				conn = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void execute(String storedProcedure,List<Meitu91Image> imgs) {
		Connection conn = getConnection();
		CallableStatement cstmt = null;
		try {
			cstmt = conn.prepareCall("{CALL " + storedProcedure + "(?,?,?,?,?,?)}");
			for(Meitu91Image img : imgs){
				cstmt.setInt("id", img.getId());
				cstmt.setString("title", img.getTitle());
				cstmt.setString("filename", img.getFilename());
				cstmt.setString("intro", img.getIntro());
				cstmt.setInt("width", img.getWidth());
				cstmt.setInt("height", img.getHeight());
				cstmt.execute();
			}
		} catch (SQLException e) {
			System.out.println("存储过程执行失败");
			e.printStackTrace();
		}
		finally{
			close(null, cstmt, conn);
		}
	}
}
