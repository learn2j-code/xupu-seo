package com.seo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Log;
import com.jfinal.upload.UploadFile;
import com.seo.model.Clanhall;
import com.seo.model.Images;
import com.seo.utils.FtpUtil;
import com.seo.utils.ImageUtil;
import com.seo.utils.StringUtils;
import com.seo.vo.AjaxResult;

/**
 * @author Javen
 */
public class FileController extends Controller {
	AjaxResult result = new AjaxResult();
	private Log log = Log.getLog(FileController.class);
	
	public void index() {
		render("file.jsp");
	}

	public void add() {
		StringBuffer sbf = new StringBuffer();
		// List<UploadFile> files = getFiles();
		Date date = new Date();
		String strDate = new SimpleDateFormat("yyyyMMdd").format(date);
		UploadFile file = getFile("/temp/" + strDate);
		String uploadPath = null;
		// if (null!=files && files.size()>0) {
		// 获取长传文件的路径
		// uploadPath=files.get(0).getUploadPath();
		// }
		uploadPath = file.getUploadPath();
		UploadFile uploadFile = getFile("img");
		// for (int i = 0; i < files.size(); i++) {
		// sbf.append(files.get(i).getFileName());
		// files.get(i).getFile().renameTo(new
		// File(uploadPath+File.separator+"javen"+i+".jpg"));
		// }
		// Date date = new Date();
		// String strDate = new SimpleDateFormat("yyyyMMdd").format(date);
		String fileName = uploadPath + "/temp/" + strDate + "/" + System.currentTimeMillis()
				+ getFileExt(uploadFile.getFileName());
		boolean i = file.getFile().renameTo(new File(fileName));
		if (i) {
			System.err.println("1");
		}
		// for (UploadFile uploadFile : files) {
		// sbf.append(uploadFile.getFileName());
		//
		// }

		// UploadFile uploadFile = getFile("img");
		// 获取文件名称
		// String fileName=uploadFile.getFileName();
		// 获取长传文件的路径
		// uploadPath=uploadFile.getUploadPath();
		// 文件重命名
		// uploadFile.getFile().renameTo(new
		// File(uploadPath+File.separator+"javen.jpg"));

		renderText(uploadPath + " " + fileName + "  " + sbf.toString());
	}

	/**
	 * 获取文件后缀
	 * 
	 * @param @param
	 *            fileName
	 * @param @return
	 *            设定文件
	 * @return String 返回类型
	 */
	public static String getFileExt(String fileName) {
		return fileName.substring(fileName.lastIndexOf('.'), fileName.length());
	}

	/**
	 * 异步单文件上传
	 */
	public void uploadImg() {

		JSONObject json = new JSONObject();
		try {
			Date date = new Date();
			String strDate = new SimpleDateFormat("yyyyMMdd").format(date);
			UploadFile uploadfile = getFile("formFile", "/temp/" + strDate);
			File f = uploadfile.getFile();
			String imgName = System.currentTimeMillis() + getFileExt(uploadfile.getFileName());
			String fileName = "/myupload/temp/" + strDate + "/" + imgName;
			// String pathName="myupload\\temp\\"
			// +strDate+"\\"+session.getStr("id")+"\\"+imgName;
			f.renameTo(new File(PathKit.getWebRootPath() + fileName));

			// String binPath=System.getProperty("user.dir");
			// String home=System.getProperty("catalina.home");
			// System.out.println(fileName);
			// System.out.println(pathName);
			String path = f.getAbsolutePath();

			System.out.println(path.substring(0, path.lastIndexOf(File.separator)) + "\\" + imgName);

			json.put("status", 0);
			json.put("src", fileName);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			json.put("status", 1);
			json.put("message", e.getMessage() == null ? "上传文件出错" : e.getMessage());
		}
		renderJson(json.toJSONString());
	}

	public void uploadCoverPic() {
		JSONObject json = new JSONObject();
		UploadFile upfile = getFile();// JFinal规定getFile()必须最先执行
		File file = upfile.getFile();
		String filename = file.getName();
		String delfilename = filename;
		String id = getPara("id");
		
		if(!StringUtils.isNotNullOrEmptyStr(id)) {
			Clanhall clanhall = new Clanhall();
			clanhall.set("recommend", 0);// 推荐
			clanhall.set("del_flg", 1);// 默认为删除
			id = Clanhall.dao.create(clanhall);
		}
		
		if (filename != null && !filename.equals("")) {
			// filename = new SimpleDateFormat("yyyyMMddkkmmss").format(new
			// Date())+filename;
			/**
			 * 新保存的位置
			 */
			String path = getRequest().getSession().getServletContext().getRealPath("/");
			Date date = new Date();
			String strDate = new SimpleDateFormat("yyyyMMdd").format(date);
			String strDateTime = new SimpleDateFormat("HHmmss").format(date);
			String newPath = "/media/"+id + "/" + strDate + "/";// 自定义目录 用于存放图片
			String imgName = strDateTime+"_"+System.currentTimeMillis() + getFileExt(filename);
			/**
			 * 没有则新建目录
			 */
			File floder = new File(path + newPath);
			if (!floder.exists()) {
				floder.mkdirs();
			}
			
			/**
			 * 保存新文件
			 */
			FileInputStream fis = null;
			FileOutputStream fos = null;
			try {
				File savePath = new File(path + newPath + imgName);
				if (!savePath.isDirectory())
					savePath.createNewFile();
				fis = new FileInputStream(file);
				fos = new FileOutputStream(savePath);
				byte[] bt = new byte[300];
				while (fis.read(bt, 0, 300) != -1) {
					fos.write(bt, 0, 300);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (null != fis)
						fis.close();
					if (null != fos)
						fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//创建缩略图 250*170
			new ImageUtil().thumbnailImage(path + newPath + imgName, 250, 170,true);
			
			/**
			 * 删除原图片，JFinal默认上传文件路径为 /upload（自动创建）
			 */
			log.info("删除："+path + "myupload/" + delfilename);
			File delFile = new File(path + "myupload/" + delfilename);
			if (delFile.exists()) {
				delFile.delete();
			}
			log.info("返回："+newPath + imgName);
			json.put("status", 0);
			json.put("src", newPath + imgName);
			json.put("thumb", newPath + "thumb_" + imgName);
			json.put("id", id);
			//setAttr("msg", newPath + imgName);
			//setAttr("t", 1);
		} else {
			//setAttr("t", 0);
			json.put("status", 1);
			json.put("message", "上传文件出错");
			json.put("id", id);
		}
		renderJson(json.toJSONString());
	}

	public void uploadpic() {
		JSONObject json = new JSONObject();
		UploadFile upfile = getFile();// JFinal规定getFile()必须最先执行
		File file = upfile.getFile();
		String filename = file.getName();
		String delfilename = filename;
		String id = getPara("id");
		
//		if(!StringUtils.isNotNullOrEmptyStr(id)) {
//			Clanhall clanhall = new Clanhall();
//			clanhall.set("recommend", 0);// 推荐
//			clanhall.set("del_flg", 1);// 默认为删除
//			id = Clanhall.dao.create(clanhall);
//		}
		
		if (filename != null && !filename.equals("")) {
			// filename = new SimpleDateFormat("yyyyMMddkkmmss").format(new
			// Date())+filename;
			/**
			 * 新保存的位置
			 */
			String path = getRequest().getSession().getServletContext().getRealPath("/");
			Date date = new Date();
			String strDate = new SimpleDateFormat("yyyyMMdd").format(date);
			String strDateTime = new SimpleDateFormat("HHmmss").format(date);
			String newPath = "/media/"+id + "/" + strDate + "/";// 自定义目录 用于存放图片
			String imgName = strDateTime+"_"+System.currentTimeMillis() + getFileExt(filename);
			/**
			 * 没有则新建目录
			 */
			File floder = new File(path + newPath);
			if (!floder.exists()) {
				floder.mkdirs();
			}
			
			/**
			 * 保存新文件
			 */
			FileInputStream fis = null;
			FileOutputStream fos = null;
			try {
				File savePath = new File(path + newPath + imgName);
				if (!savePath.isDirectory())
					savePath.createNewFile();
				fis = new FileInputStream(file);
				fos = new FileOutputStream(savePath);
				byte[] bt = new byte[300];
				while (fis.read(bt, 0, 300) != -1) {
					fos.write(bt, 0, 300);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (null != fis)
						fis.close();
					if (null != fos)
						fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			//FTPUtil t=new FTPUtil();
			/*try {
				t.uploadFile(file, imgName);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			/*InputStream input;
			try {
				input = new FileInputStream(file);
				ftpFile(newPath,imgName,input,newPath + imgName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			/**
			 * 删除原图片，JFinal默认上传文件路径为 /upload（自动创建）
			 */
			log.info("删除："+path + "myupload/" + delfilename);
			File delFile = new File(path + "myupload/" + delfilename);
			if (delFile.exists()) {
				delFile.delete();
			}
			log.info("返回："+newPath + imgName);
			json.put("status", 0);
			json.put("src", newPath + imgName);
			json.put("id", id);
			//setAttr("msg", newPath + imgName);
			//setAttr("t", 1);
		} else {
			//setAttr("t", 0);
			json.put("status", 1);
			json.put("message", "上传文件出错");
			json.put("id", id);
		}
		renderJson(json.toJSONString());
	}
	
	/**
	 * 删除本地图片
	 */
	public void delFile() {
		String path = getPara("filepath");
		String filepath = getRequest().getSession().getServletContext().getRealPath("/");
		File delFile = new File(filepath + path);
		if (delFile.exists()) {
			delFile.delete();
		}
		String id = getPara("id");
		if(StringUtils.isNotNullOrEmptyStr(id)) {
			Images.dao.deleteById(id);
		}
		renderJson(result);
	}
	
	public void ftpFile(String localPath,String fileName,InputStream input,String filepath) {
		String ftpHost = "120.25.156.58";
		String ftpUserName = "seo";
		String ftpPassword = "dlm123";
		int ftpPort = 22;
		String ftpPath = "/mnt/ftp/upload/seo/";
		
		/*try {
			FtpUtils.uploadimg(filepath, "123", "123");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		FtpUtil.listFileNames(ftpHost,ftpPort,ftpUserName, ftpPassword,  ftpPath) ;
		/**  
	     * Description: 向FTP服务器上传文件  
	     * @param host FTP服务器hostname  
	     * @param port FTP服务器端口  
	     * @param username FTP登录账号  
	     * @param password FTP登录密码  
	     * @param basePath FTP服务器基础目录 
	     * @param filePath FTP服务器文件存放路径。例如分日期存放：/2015/01/01。文件的路径为basePath+filePath 
	     * @param filename 上传到FTP服务器上的文件名  
	     * @param input 输入流  
	     * @return 成功返回true，否则返回false  
	     */    
		//FtpUtil.uploadFile(ftpHost, ftpPort, ftpUserName, ftpPassword, ftpPath, localPath, fileName, input);
		//FtpUtil.uploadFile(ftpHost, ftpUserName, ftpPassword, ftpPort, ftpPath, localPath, fileName);
	}

}
