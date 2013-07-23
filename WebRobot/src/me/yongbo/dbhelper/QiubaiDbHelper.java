package me.yongbo.dbhelper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import me.yongbo.bean.QiubaiObj;

public class QiubaiDbHelper extends BaseDbHelper {

	public void execute(String storedProcedure, List<QiubaiObj> qbObjs) {
		Connection conn = getConnection();
		CallableStatement cstmt = null;
		try {
			cstmt = conn.prepareCall("{CALL " + storedProcedure + "(?,?,?,?,?,?)}");
			for(QiubaiObj qboObj : qbObjs){
				cstmt.setString("id", qboObj.getId());
				cstmt.setString("content", qboObj.getContent());
				cstmt.setString("imgurl", qboObj.getImgUrl());
				cstmt.setString("savepath", qboObj.getSavePath());
				cstmt.setString("createtime", qboObj.getCreatetime());
				cstmt.setString("detailurl", qboObj.getDetailUrl());
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
