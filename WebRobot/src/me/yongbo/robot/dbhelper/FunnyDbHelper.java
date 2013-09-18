package me.yongbo.robot.dbhelper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import me.yongbo.robot.bean.FunnyObj;

public class FunnyDbHelper extends BaseDbHelper {

	public void execute(String storedProcedure, List<FunnyObj> objs) {
		Connection conn = getConnection();
		CallableStatement cstmt = null;
		try {
			cstmt = conn.prepareCall("{CALL " + storedProcedure + "(?,?,?,?,?)}");
			for(FunnyObj obj : objs){
				cstmt.setString("id", obj.getId());
				cstmt.setString("content", obj.getContent());
				cstmt.setString("from", obj.getFrom());
				cstmt.setString("imgurl", obj.getImgUrl());
				cstmt.setString("source", obj.getSource());
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
