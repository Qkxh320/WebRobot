package me.yongbo.robot.dbhelper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import me.yongbo.robot.bean.MyImage;

public class ImageDbHelper extends BaseDbHelper {
	
	public void execute(String storedProcedure, List<MyImage> imgs) {
		Connection conn = getConnection();
		CallableStatement cstmt = null;
		try {
			cstmt = conn.prepareCall("{CALL " + storedProcedure + "(?,?,?,?,?,?)}");
			for(MyImage img : imgs) {
				cstmt.setString("id", img.getId());
				cstmt.setString("savePath", img.getSavePath());
				cstmt.setString("imgUrl", img.getImgUrl());
				cstmt.setInt("width", img.getWidth());
				cstmt.setInt("height", img.getHeight());
				cstmt.setInt("objType", img.getObjType());
				cstmt.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			close(null, cstmt, conn);
		}
	}
	
	public MyImage getData(){
		
		
		return null;
	}
}