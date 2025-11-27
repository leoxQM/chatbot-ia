package com.leodeev.project.whatsappbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WhatsappbotApplication {

	public static void main(String[] args) {
		System.setProperty("io.netty.incubator.codec.quic.QuicheQuicSslContext.disabled", "true");
		System.setProperty("reactor.netty.http.server.http3", "false");
		System.setProperty("reactor.netty.http.client.http3", "false");
		System.setProperty("io.netty.handler.codec.quic.Quic.disabled", "true");
		
		SpringApplication.run(WhatsappbotApplication.class, args);
	}

}
