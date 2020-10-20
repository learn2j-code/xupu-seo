package com.seo.demo;  
  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.OutputStream;  
import java.net.SocketException;  
  
import org.apache.commons.net.ftp.FTPClient;  
  
/** 
 *  
 * @author muyunfei  牟云飞 
 * 
 */  
public class FTPUtil {  
  
  /*  public static void main(String[] args) {  
        FTPUtil util = new FTPUtil();  
//      //上传  
//      File localFile=new File("G:\\photo2222222222222.png");  
//      try {  
//          boolean flag=util.uploadFile(localFile, "muyunfei.png");  
//          System.out.println(flag +"   判断上传结果");  
//      } catch (SocketException e) {  
//          // TODO Auto-generated catch block  
//          e.printStackTrace();  
//      } catch (IOException e) {  
//          // TODO Auto-generated catch block  
//          e.printStackTrace();  
//      }  
          
        //下载  
        File localFile=new File("G:\\APP-release.apk");  
        try {  
            localFile.createNewFile();  
            boolean flag=util.downloadFile("APP-release.apk",localFile);  
            System.out.println(flag +"   判断下载结果");  
        } catch (SocketException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
          
        //删除  
//      try {  
//          boolean flag=util.deleteFile("muyunfei.png");  
//          System.out.println(flag +"   判断删除结果");  
//      } catch (SocketException e) {  
//          // TODO Auto-generated catch block  
//          e.printStackTrace();  
//      } catch (IOException e) {  
//          // TODO Auto-generated catch block  
//          e.printStackTrace();  
//      }  
          
    }  
      */
      
    public static String ftpHost;  
    public static int port;  
    public static String userName;  
    public static String passWord;  
    public static String path; //ftp下哪个目录------------------  
    public static String ftpEncode;  
    public static int defaultTimeout;  
    /** 
     * 静态块，初始化ftp数据 
     */  
    static{  
        try{  
              
            ftpHost = "120.78.173.202";//linux  
            //ftpHost = "192.168.1.103";//windows  
            port = 21;            
            userName = "ftp_alfieri";  
            passWord = "ftp_alfieri";  
            path = "/mnt/ftp/upload/seo"; //ftp下哪个目录------------------  
            ftpEncode="UTF-8";  
            defaultTimeout = 30000;  
        }catch(Exception e){  
            e.printStackTrace();  
        }  
          
    }  
    /** 
     * 上传ftp 
     * @param localFile 
     * @param fileNewName 
     * @return 
     * @throws SocketException 
     * @throws IOException 
     */  
    public boolean uploadFile(File localFile,String fileNewName) throws SocketException, IOException{  
        boolean flag=true;  
        //获得文件流  
        FileInputStream is=new FileInputStream(localFile);  
        //保存至Ftp  
        FTPClient ftpClient = new FTPClient();// ftpHost为FTP服务器的IP地址，port为FTP服务器的登陆端口,ftpHost为String型,port为int型。  
        ftpClient.setControlEncoding(ftpEncode); // 中文支持  
        ftpClient.connect(ftpHost);  
        ftpClient.login(userName, passWord);// userName、passWord分别为FTP服务器的登陆用户名和密码  
        ftpClient.setDefaultPort(port);  
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);  
        ftpClient.enterLocalPassiveMode(); // 用被动模式传输,解决linux服务长时间等待，导致超时问题  
        ftpClient.setDefaultTimeout(defaultTimeout);// 设置默认超时时间  
        ftpClient.setBufferSize(1024*1024);//设置缓存区，默认缓冲区大小是1024，也就是1K  
        //切换目录，目录不存在创建目录  
        boolean chagenDirFlag=ftpClient.changeWorkingDirectory(path);  
        if(chagenDirFlag==false){  
            ftpClient.makeDirectory(path);  
            ftpClient.changeWorkingDirectory(path);  
        }  
        //生成新的ftp文件名  
//      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");  
//      Date curDate = new Date();  
//      String newFileName = fileName.substring(0, fileName.indexOf(".zip"))+sdf.format(curDate)+".zip";  
        String newFileName = fileNewName;  
        //上传至Ftp  
        flag=ftpClient.storeFile(newFileName, is);  
        is.close();  
        //关闭连接  
        ftpClient.logout();  
        ftpClient.disconnect();  
        if(true==flag){  
            System.out.println(fileNewName+"上传图片成功");  
        }else{  
            System.out.println(fileNewName+"上传图片失败.......");  
        }  
        System.out.println("FTP文件名——"+newFileName);  
        return flag;  
    }  
      
    /** 
     * 下载FTP 
     * @param ftpName  ftp上的文件名 
     * @param localFile  保存的本地地址 
     * @return 
     * @throws SocketException 
     * @throws IOException 
     */  
    public boolean downloadFile(String ftpName,File localFile) throws SocketException, IOException{  
        boolean flag=true;  
        //保存至Ftp  
        FTPClient ftpClient = new FTPClient();// ftpHost为FTP服务器的IP地址，port为FTP服务器的登陆端口,ftpHost为String型,port为int型。  
        ftpClient.setControlEncoding(ftpEncode); // 中文支持  
        ftpClient.connect(ftpHost);  
        ftpClient.login(userName, passWord);// userName、passWord分别为FTP服务器的登陆用户名和密码  
        ftpClient.setDefaultPort(port);  
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);  
        ftpClient.enterLocalPassiveMode(); // 用被动模式传输,解决linux服务长时间等待，导致超时问题  
        ftpClient.setDefaultTimeout(defaultTimeout);// 设置默认超时时间  
        ftpClient.setBufferSize(1024*1024);//设置缓存区，默认缓冲区大小是1024，也就是1K  
        //切换目录，目录不存在创建目录  
        boolean chagenDirFlag=ftpClient.changeWorkingDirectory(path);  
        if(chagenDirFlag==false){  
            System.out.println("ftp上目录切换失败");  
            return false;  
        }  
        OutputStream os = new FileOutputStream(localFile);  
        flag = ftpClient.retrieveFile(ftpName, os);  
        if(true==flag){  
            System.out.println(ftpName+"   文件下载成功");  
        }else{  
            System.out.println(ftpName+"   文件下载失败");  
        }  
        os.flush();  
        os.close();  
        //关闭连接  
        ftpClient.logout();  
        ftpClient.disconnect();  
        System.out.println("FTP文件名——"+ftpName);  
        return flag;  
    }  
      
    /** 
     * 删除FTP 
     * @param ftpName  ftp上的文件名 
     * @param localFile  保存的本地地址 
     * @return 
     * @throws SocketException 
     * @throws IOException 
     */  
    public boolean deleteFile(String ftpName) throws SocketException, IOException{  
        boolean flag=true;  
        //保存至Ftp  
        FTPClient ftpClient = new FTPClient();// ftpHost为FTP服务器的IP地址，port为FTP服务器的登陆端口,ftpHost为String型,port为int型。  
        ftpClient.setControlEncoding(ftpEncode); // 中文支持  
        ftpClient.connect(ftpHost);  
        ftpClient.login(userName, passWord);// userName、passWord分别为FTP服务器的登陆用户名和密码  
        ftpClient.setDefaultPort(port);  
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);  
        ftpClient.enterLocalPassiveMode(); // 用被动模式传输,解决linux服务长时间等待，导致超时问题  
        ftpClient.setDefaultTimeout(defaultTimeout);// 设置默认超时时间  
        //切换目录，目录不存在创建目录  
        boolean chagenDirFlag=ftpClient.changeWorkingDirectory(path);  
        if(chagenDirFlag==false){  
            System.out.println("ftp上目录切换失败");  
            return false;  
        }  
        flag = ftpClient.deleteFile(ftpName);  
        if(true==flag){  
            System.out.println(ftpName+"   文件删除成功");  
        }else{  
            System.out.println(ftpName+"   文件删除失败");  
        }  
        //关闭连接  
        ftpClient.logout();  
        ftpClient.disconnect();  
        System.out.println("FTP文件名——"+ftpName);  
        return flag;  
    }  
}  