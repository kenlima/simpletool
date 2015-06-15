package com.wemakeprice.simpletool;

import java.io.File;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;

public class Test {
    public static void main(String[] args) throws FileSystemException {
        char ch = '@';
        String hex = String.format("%04x", (int) ch);
        System.out.println(hex);

        StandardFileSystemManager manager = new StandardFileSystemManager();
        String fileToDownload = "/contents-info.log";
        String serverAddress = "14.49.36.128:3322";
        String userId = "company";
        String password = "wmp!%40%23%24";
        //String password = "";
        String remoteDirectory = "usr/local/jboss-as/standalone/log";
        String localDirectory = "/Users/jwlee/Documents/my-dev-resources/simpletool/log";

        manager.init();

        FileSystemOptions opts = new FileSystemOptions();
        SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");
        SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, false);
        SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);

        //Create the SFTP URI using the host name, userid, password,  remote path and file name
        String sftpUri = "sftp://" + userId + ":" + password + "@" + serverAddress + "/" + remoteDirectory
                + fileToDownload;

        // Create local file object
        String filepath = localDirectory + fileToDownload;
        File file = new File(filepath);
        FileObject localFile = manager.resolveFile(file.getAbsolutePath());

        // Create remote file object
        FileObject remoteFile = manager.resolveFile(sftpUri, opts);

        // Copy local file to sftp server
        localFile.copyFrom(remoteFile, Selectors.SELECT_SELF);
        System.out.println("File download successful");
    }
}
