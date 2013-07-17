package me.yongbo.robot.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertieUtil {
	/** 
     * 根据KEY，读取文件对应的值 
     * @param key 键 
     * @return key对应的值 
     */  
    public static String read(String key) {  
        Properties props = new Properties();  
        try {  
            props.load(new FileInputStream("log.properties"));  
            return props.getProperty(key, null);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
    /** 
     * 修改或添加键值对 如果key存在，修改, 反之，添加。 
     * @param key 键 
     * @param value 键对应的值 
     */  
    public static void write(String key, String value) {  
        Properties prop = new Properties();
        try {
        	prop.setProperty(key, value);
			prop.store(new FileOutputStream("log.properties"), null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }  
}
