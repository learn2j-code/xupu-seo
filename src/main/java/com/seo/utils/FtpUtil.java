package com.seo.utils;  
  
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;  
  
  
public class FtpUtil {  
  
    private final static Log logger = LogFactory.getLog(FtpUtil.class);  
  
    /** 
     * 获取FTPClient对象 
     * 
     * @param ftpHost 
     *            FTP主机服务器 
     * @param ftpPassword 
     *            FTP 登录密码 
     * @param ftpUserName 
     *            FTP登录用户名 
     * @param ftpPort 
     *            FTP端口 默认为21 
     * @return 
     */  
    public static FTPClient getFTPClient(String ftpHost, String ftpUserName,  
            String ftpPassword, int ftpPort) {  
        FTPClient ftpClient = new FTPClient();  
        try {  
            ftpClient = new FTPClient();  
            ftpClient.connect(ftpHost, ftpPort);// 连接FTP服务器  
            ftpClient.login(ftpUserName, ftpPassword);// 登陆FTP服务器  
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {  
                logger.info("未连接到FTP，用户名或密码错误。");  
                ftpClient.disconnect();  
            } else {  
                logger.info("FTP连接成功。");  
            }  
        } catch (SocketException e) {  
            e.printStackTrace();  
            logger.info("FTP的IP地址可能错误，请正确配置。");  
        } catch (IOException e) {  
            e.printStackTrace();  
            logger.info("FTP的端口错误,请正确配置。");  
        }  
        return ftpClient;  
    }  
  
    /* 
     * 从FTP服务器下载文件 
     *  
     * @param ftpHost FTP IP地址 
     *  
     * @param ftpUserName FTP 用户名 
     *  
     * @param ftpPassword FTP用户名密码 
     *  
     * @param ftpPort FTP端口 
     *  
     * @param ftpPath FTP服务器中文件所在路径 格式： ftptest/aa 
     *  
     * @param localPath 下载到本地的位置 格式：H:/download 
     *  
     * @param fileName 文件名称 
     */  
    public static void downloadFtpFile(String ftpHost, String ftpUserName,  
            String ftpPassword, int ftpPort, String ftpPath, String localPath,  
            String fileName) {  
  
        FTPClient ftpClient = null;  
  
        try {  
            ftpClient = getFTPClient(ftpHost, ftpUserName, ftpPassword, ftpPort);  
            ftpClient.setControlEncoding("UTF-8"); // 中文支持  
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);  
            ftpClient.enterLocalPassiveMode();  
            ftpClient.changeWorkingDirectory(ftpPath);  
  
            File localFile = new File(localPath + File.separatorChar + fileName);  
            OutputStream os = new FileOutputStream(localFile);  
            ftpClient.retrieveFile(fileName, os);  
            os.close();  
            ftpClient.logout();  
  
        } catch (FileNotFoundException e) {  
            logger.error("没有找到" + ftpPath + "文件");  
            e.printStackTrace();  
        } catch (SocketException e) {  
            logger.error("连接FTP失败.");  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
            logger.error("文件读取错误。");  
            e.printStackTrace();  
        }  
  
    }  
  
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
    public static boolean uploadFile(String host, int port, String username, String password, String basePath,  
            String filePath, String filename, InputStream input) {  
        boolean result = false;  
        FTPClient ftp = new FTPClient();  
        try {  
            int reply;  
            ftp.connect(host, port);// 连接FTP服务器  
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器  
            ftp.login(username, password);// 登录  
            reply = ftp.getReplyCode();  
            if (!FTPReply.isPositiveCompletion(reply)) {  
                ftp.disconnect();  
                return result;  
            }  
            //切换到上传目录  
            if (!ftp.changeWorkingDirectory(basePath+filePath)) {  
                //如果目录不存在创建目录  
                String[] dirs = filePath.split("/");  
                String tempPath = basePath;  
                for (String dir : dirs) {  
                    if (null == dir || "".equals(dir)) continue;  
                    tempPath += "/" + dir;  
                    if (!ftp.changeWorkingDirectory(tempPath)) {  
                        if (!ftp.makeDirectory(tempPath)) {  
                            return result;  
                        } else {  
                            ftp.changeWorkingDirectory(tempPath);  
                        }  
                    }  
                }  
            }  
            //设置上传文件的类型为二进制类型  
            ftp.setFileType(FTP.BINARY_FILE_TYPE);  
            //上传文件  
            if (!ftp.storeFile(filename, input)) {  
                return result;  
            }  
            input.close();  
            ftp.logout();  
            result = true;  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (ftp.isConnected()) {  
                try {  
                    ftp.disconnect();  
                } catch (IOException ioe) {  
                }  
            }  
        }  
        return result;  
    }  
    
    public static List<String> listFileNames(String host, int port, String username, final String password, String dir) {  
        List<String> list = new ArrayList<String>();  
        ChannelSftp sftp = null;  
        Channel channel = null;  
        Session sshSession = null;  
        try {  
            JSch jsch = new JSch();  
            jsch.getSession(username, host, port);  
            sshSession = jsch.getSession(username, host, port);  
            sshSession.setPassword(password);  
            Properties sshConfig = new Properties();  
            sshConfig.put("StrictHostKeyChecking", "no");  
            sshSession.setConfig(sshConfig);  
            sshSession.connect();  
            //LOG.debug("Session connected!");  
            channel = sshSession.openChannel("sftp");  
            channel.connect();  
            //LOG.debug("Channel connected!");  
            sftp = (ChannelSftp) channel;  
            Vector<?> vector = sftp.ls(dir);  
            for (Object item:vector) {  
                LsEntry entry = (LsEntry) item;  
                System.out.println(entry.getFilename());  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            closeChannel(sftp);  
            closeChannel(channel);  
            closeSession(sshSession);  
        }  
        return list;  
    }  
  
    private static void closeChannel(Channel channel) {  
        if (channel != null) {  
            if (channel.isConnected()) {  
                channel.disconnect();  
            }  
        }  
    }  
  
    private static void closeSession(Session session) {  
        if (session != null) {  
            if (session.isConnected()) {  
                session.disconnect();  
            }  
        }  
    }  
    
  
}  