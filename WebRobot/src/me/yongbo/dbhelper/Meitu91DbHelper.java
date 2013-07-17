package me.yongbo.dbhelper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import me.yongbo.bean.Entity;
import me.yongbo.bean.Meitu91Image;

public class Meitu91DbHelper extends BaseDbHelper {
	
	public void execute(String storedProcedure, List<Meitu91Image> imgs) {
		Connection conn = getConnection();
		CallableStatement cstmt = null;
		try {
			cstmt = conn.prepareCall("{CALL " + storedProcedure + "(?,?,?,?,?,?,?)}");
			for(Meitu91Image img : imgs) {
				cstmt.setString("content", img.getTitle());
				cstmt.setString("filename", img.getFilename());
				cstmt.setString("intro", img.getIntro());
				cstmt.setInt("width", img.getWidth());
				cstmt.setInt("height", img.getHeight());
				cstmt.setInt("objtype", 1);
				cstmt.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			close(null, cstmt, conn);
		}
	}
}
