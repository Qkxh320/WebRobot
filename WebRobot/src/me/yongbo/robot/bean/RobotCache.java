package me.yongbo.robot.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class RobotCache {
//	/**
//	 * 保存对象
//	 * @param ser
//	 * @param file
//	 * @throws IOException
//	 */
//	public boolean saveObject(Serializable ser, String key) {
//		FileOutputStream fos = null;
//		ObjectOutputStream oos = null;
//		try{
//			fos = openFileOutput(key, MODE_PRIVATE);
//			oos = new ObjectOutputStream(fos);
//			oos.writeObject(ser);
//			oos.flush();
//			return true;
//		}catch(Exception e){
//			e.printStackTrace();
//			return false;
//		}finally{
//			try {
//				oos.close();
//			} catch (Exception e) {}
//			try {
//				fos.close();
//			} catch (Exception e) {}
//		}
//	}
//	
//	/**
//	 * 读取对象
//	 * @param file
//	 * @return
//	 * @throws IOException
//	 */
//	public Serializable readObject(String key){
//		if(!isExistDataCache(key))
//			return null;
//		FileInputStream fis = null;
//		ObjectInputStream ois = null;
//		try{
//			fis = openFileInput(key);
//			ois = new ObjectInputStream(fis);
//			return (Serializable)ois.readObject();
//		}catch(FileNotFoundException e){
//		}catch(Exception e){
//			e.printStackTrace();
//			//反序列化失败 - 删除缓存文件
//			if(e instanceof InvalidClassException){
//				File data = getFileStreamPath(key);
//				data.delete();
//			}
//		}finally{
//			try {
//				ois.close();
//			} catch (Exception e) {}
//			try {
//				fis.close();
//			} catch (Exception e) {}
//		}
//		return null;
//	}
//	/**
//	 * 判断缓存是否存在
//	 * @param cachefile
//	 * @return
//	 */
//	private boolean isExistDataCache(String cachefile)
//	{
//		boolean exist = false;
//		File data = getFileStreamPath(cachefile);
//		if(data.exists())
//			exist = true;
//		return exist;
//	}
}
