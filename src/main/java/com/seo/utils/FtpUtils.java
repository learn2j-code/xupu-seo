package com.seo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;


public class FtpUtils {
	
    private static Logger logger=Logger.getLogger(FtpUtil.class);
    
    private static FTPClient ftp;
    
    /**
     * 获取ftp连接
     * @param f
     * @return
     * @throws Exception
     */
    public static boolean connectFtp(Ftp f) throws Exception{
        ftp=new FTPClient();
        boolean flag=false;
        int reply;
        if (f.getPort()==null) {
            ftp.connect(f.getIpAddr(),21);
        }else{
            ftp.connect(f.getIpAddr(),f.getPort());
        }
        ftp.login(f.getUserName(), f.getPwd());
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        reply = ftp.getReplyCode();      
        if (!FTPReply.isPositiveCompletion(reply)) {      
              ftp.disconnect();      
              return flag;      
        }      
        ftp.changeWorkingDirectory(f.getPath());      
        flag = true;      
        return flag;
    }
    
    /**
     * 关闭ftp连接
     */
    public static void closeFtp(){
        if (ftp!=null && ftp.isConnected()) {
            try {
                ftp.logout();
                ftp.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * ftp上传文件
     * @param f
     * @throws Exception
     */
    public static void upload(File f) throws Exception{
        if (f.isDirectory()) {
            ftp.makeDirectory(f.getName());
            ftp.changeWorkingDirectory(f.getName());
            String[] files=f.list();
            for(String fstr : files){
                File file1=new File(f.getPath()+"/"+fstr);
                if (file1.isDirectory()) {
                    upload(file1);
                    ftp.changeToParentDirectory();
                }else{
                    File file2=new File(f.getPath()+"/"+fstr);
                    FileInputStream input=new FileInputStream(file2);
                    ftp.storeFile(file2.getName(),input);
                    input.close();
                }
            }
        }else{
            File file2=new File(f.getPath());
            FileInputStream input=new FileInputStream(file2);
//            ftp.enterLocalPassiveMode();
            ftp.storeFile(file2.getName(),input);
            input.close();
        }
    }
    
    
    /**
     * ftp上传文件  上传到指定目录
     * @param f
     * @throws Exception
     */
    public static void upload(File f,String pathName) throws Exception{
        if (f.isDirectory()) {
            ftp.makeDirectory(f.getName());
            ftp.changeWorkingDirectory(f.getName());
            String[] files=f.list();
            for(String fstr : files){
                File file1=new File(f.getPath()+"/"+fstr);
                if (file1.isDirectory()) {
                    upload(file1);
                    ftp.changeToParentDirectory();
                }else{
                    File file2=new File(f.getPath()+"/"+fstr);
                    FileInputStream input=new FileInputStream(file2);
                    ftp.storeFile(file2.getName(),input);
                    input.close();
                }
            }
        }else{
        	ftp.mkd(pathName);
        	ftp.changeWorkingDirectory(pathName);
            File file2=new File(f.getPath());
            FileInputStream input=new FileInputStream(file2);
            ftp.storeFile(file2.getName(),input);
            input.close();
        }
    }
    
    /**
     * 下载链接配置
     * @param f
     * @param localBaseDir 本地目录
     * @param remoteBaseDir 远程目录
     * @throws Exception
     */
    public static void startDown(Ftp f,String localBaseDir,String remoteBaseDir ) throws Exception{
        if (FtpUtils.connectFtp(f)) {
            
            try { 
                FTPFile[] files = null; 
                boolean changedir = ftp.changeWorkingDirectory(remoteBaseDir); 
                if (changedir) { 
                    ftp.setControlEncoding("GBK"); 
                    files = ftp.listFiles(); 
                    for (int i = 0; i < files.length; i++) { 
                        try{ 
                            downloadFile(files[i], localBaseDir, remoteBaseDir); 
                        }catch(Exception e){ 
                            logger.error(e); 
                            logger.error("<"+files[i].getName()+">下载失败"); 
                        } 
                    } 
                } 
            } catch (Exception e) { 
                logger.error(e); 
                logger.error("下载过程中出现异常"); 
            } 
        }else{
            logger.error("链接失败！");
        }
        
    }
    
    
    /** 
     * 
     * 下载FTP文件 
     * 当你需要下载FTP文件的时候，调用此方法 
     * 根据<b>获取的文件名，本地地址，远程地址</b>进行下载 
     * 
     * @param ftpFile 
     * @param relativeLocalPath 
     * @param relativeRemotePath 
     */ 
    private  static void downloadFile(FTPFile ftpFile, String relativeLocalPath,String relativeRemotePath) { 
        if (ftpFile.isFile()) {
            if (ftpFile.getName().indexOf("?") == -1) { 
                OutputStream outputStream = null; 
                try { 
                    File locaFile= new File(relativeLocalPath+ ftpFile.getName()); 
                    //判断文件是否存在，存在则返回 
                    if(locaFile.exists()){ 
                        return; 
                    }else{ 
                        outputStream = new FileOutputStream(relativeLocalPath+ ftpFile.getName()); 
                        ftp.retrieveFile(ftpFile.getName(), outputStream); 
                        outputStream.flush(); 
                        outputStream.close(); 
                    } 
                } catch (Exception e) { 
                    logger.error(e);
                } finally { 
                    try { 
                        if (outputStream != null){ 
                            outputStream.close(); 
                        }
                    } catch (IOException e) { 
                       logger.error("输出文件流异常"); 
                    } 
                } 
            } 
        } else { 
            String newlocalRelatePath = relativeLocalPath + ftpFile.getName(); 
            String newRemote = new String(relativeRemotePath+ ftpFile.getName().toString()); 
            File fl = new File(newlocalRelatePath); 
            if (!fl.exists()) { 
                fl.mkdirs(); 
            } 
            try { 
                newlocalRelatePath = newlocalRelatePath + '/'; 
                newRemote = newRemote + "/"; 
                String currentWorkDir = ftpFile.getName().toString(); 
                boolean changedir = ftp.changeWorkingDirectory(currentWorkDir); 
                if (changedir) { 
                    FTPFile[] files = null; 
                    files = ftp.listFiles(); 
                    for (int i = 0; i < files.length; i++) { 
                        downloadFile(files[i], newlocalRelatePath, newRemote); 
                    } 
                } 
                if (changedir){
                    ftp.changeToParentDirectory(); 
                } 
            } catch (Exception e) { 
                logger.error(e);
            } 
        } 
    } 
    /**
     * 特定上传（仅用于入职附件）
     * @throws Exception
     */
    public static boolean uploadimg(String filepath,String idfolder,String types) throws Exception{
    	Ftp f=new Ftp();
        f.setIpAddr("120.25.156.58");
        f.setUserName("root");
        f.setPwd("Selphina103");
        f.setPort(22);
        boolean isconnect=FtpUtils.connectFtp(f);
        ftp.enterLocalPassiveMode();
        boolean issuccess=false;
        if(isconnect){
        	File file = new File(filepath); 
        	ftp.makeDirectory(idfolder);
        	ftp.changeWorkingDirectory(idfolder);
        	ftp.makeDirectory(types);
        	ftp.changeWorkingDirectory(types);
        	File file2=new File(file.getPath());
        	FileInputStream input=new FileInputStream(file2);
            issuccess=ftp.storeFile(file2.getName(),input);
            input.close();
        }
    	return issuccess;
    }
    /**
     * 特定下载（仅用于入职附件）
     * @throws Exception
     */
    public boolean downloadimgs(String localBaseDir,String remoteBaseDir) throws Exception{
    	Ftp f=new Ftp();
        f.setIpAddr("182.139.182.10");
        f.setUserName("scfesco");
        f.setPwd("36x2R1bd");
        boolean isconnect=FtpUtils.connectFtp(f);
        ftp.enterLocalPassiveMode();
        boolean issuccess=false;
        if(isconnect){
        	startDown(f, localBaseDir, remoteBaseDir);
        }
    	return issuccess;
    }
	public static boolean downloadimg(String relativeLocalPath,String relativeRemotePath,String fileName) { 
        OutputStream outputStream = null; 
        boolean downloadstauts=false;
        try { 
            File locaFile= new File(relativeLocalPath+ fileName); 
            //判断文件是否存在，存在则返回 
            if(locaFile.exists()){ 
            	System.out.println("目标"+fileName+"已存在!");
                return downloadstauts; 
            }else{ 
            	Ftp f=new Ftp();
                f.setIpAddr("182.139.182.10");
                f.setUserName("scfesco");
                f.setPwd("36x2R1bd");
                FtpUtils.connectFtp(f);
            	ftp.enterLocalPassiveMode();
            	ftp.changeWorkingDirectory(relativeRemotePath);//切换到ftp目标文件目录
                outputStream = new FileOutputStream(relativeLocalPath+ fileName); 
                downloadstauts=ftp.retrieveFile(fileName, outputStream);//下载目标文件到本地目录
                outputStream.flush(); 
                outputStream.close(); 
                FtpUtils.closeFtp();
            } 
        } catch (Exception e) { 
            logger.error(e);
        } finally { 
            try { 
                if (outputStream != null){ 
                    outputStream.close(); 
                }
            } catch (IOException e) { 
               logger.error("输出文件流异常"); 
            } 
        } 
        return downloadstauts;
    }
    public static void main(String[] args) throws Exception{  
            Ftp f=new Ftp();
            f.setIpAddr("182.139.182.10");
            f.setUserName("scfesco");
            f.setPwd("36x2R1bd");
            FtpUtils.connectFtp(f);
            boolean c=downloadimg("D:/Eclipse/4.3/mydevolep/fesco_self/src/main/webapp/myupload/temp/db397b042ec54032b94b1639c164ccec/", "db397b042ec54032b94b1639c164ccec/idcard/", "1518167489895.jpg");
            System.out.println(c);
//           boolean a = ftp.changeWorkingDirectory("db397b042ec54032b94b1639c164ccec");
//           System.out.println("切换状态"+a);
//            startDown(f, "D:/Eclipse/4.3/mydevolep/fesco_self/src/main/webapp/myupload/temp/db397b042ec54032b94b1639c164ccec", "db397b042ec54032b94b1639c164ccec/idcard");
//            OutputStream outputStream = new FileOutputStream("D:/Eclipse/4.3/mydevolep/fesco_self/src/main/webapp/myupload/temp/db397b042ec54032b94b1639c164ccec/123.txt"); 
//            ftp.retrieveFile("123.txt", outputStream); 
//            outputStream.flush(); 
//            outputStream.close();
/*//          ftp.enterLocalPassiveMode();
            System.out.println("链接状态 "+FtpUtil.connectFtp(f));
            File file = new File("E:/ftptest/ftptest.txt"); 
            boolean b1=ftp.makeDirectory("test2");
            boolean b2=ftp.changeWorkingDirectory("test2");
            File file2=new File(file.getPath());
            FileInputStream input=new FileInputStream(file2);
            boolean b3=ftp.storeFile(file2.getName(),input);
            input.close();
//            FtpUtil.upload(file);//把文件上传在ftp上
            //FtpUtil.upload(file,"helloworld"); //把文件上传到指定目录ftp上
           // FtpUtil.startDown(f, "e:/",  "/FTPROOT");//下载ftp文件测试
            System.out.println("ok");*/
            FtpUtils.closeFtp();
          
       }  
    
}