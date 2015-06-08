package com.wemakeprice.simpletool;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.integration.endpoint.SourcePollingChannelAdapter;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;

import com.jcraft.jsch.ChannelSftp.LsEntry;

@ImportResource("classpath:/spring/SftpInboundReceiveSample-context.xml")
@SpringBootApplication
public class Application {

    @Value("${ssh1.host}")
    private String host;
    @Value("${ssh.privateKey}")
    private String privateKey;
    @Value("${ssh1.user}")
    String user;

    @Value("${ssh1.port}")
    private int port;

    public static void main(String[] args) throws IOException {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        pollingLog(ctx);

        System.out.println("Let's inspect the beans provided by Spring Boot:");

    }

    private static void pollingLog(ApplicationContext ctx) {
        PollableChannel localFileChannel = ctx.getBean("receiveChannel", PollableChannel.class);
        SessionFactory<LsEntry> sessionFactory = ctx.getBean(CachingSessionFactory.class);

        //SftpTestUtils.createTestFiles(template, file1, file2, file3);

        SourcePollingChannelAdapter adapter = ctx.getBean(SourcePollingChannelAdapter.class);
        adapter.start();

        while (true) {
            Message<?> received = localFileChannel.receive();
            System.out.println("Expected file : " + received);
        }
    }

    @Bean
    public DefaultSftpSessionFactory defaultSessionFactory() {
        DefaultSftpSessionFactory sftpSession = new DefaultSftpSessionFactory();
        sftpSession.setHost(host);
        Resource resource = new FileSystemResource(new File(privateKey));
        sftpSession.setPrivateKey(resource);
        //sftpSession.setPassword("wmp!@#$");
        sftpSession.setUser(user);
        sftpSession.setPort(port);
        return sftpSession;
    }

    @Bean
    public CachingSessionFactory sftpSessionFactory() {
        CachingSessionFactory cachingSessionFactory = new CachingSessionFactory(defaultSessionFactory());
        return cachingSessionFactory;
    }

}
