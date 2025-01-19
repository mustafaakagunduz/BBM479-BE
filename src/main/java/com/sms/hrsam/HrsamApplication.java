package com.sms.hrsam;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication
public class HrsamApplication {

	@Autowired
	private DataSource dataSource;

	@PostConstruct
	public void checkDatabaseConnection() {
		try (Connection conn = dataSource.getConnection()) {
			System.out.println("Database connection successful!");
			System.out.println("Database: " + conn.getCatalog());
		} catch (SQLException e) {
			System.err.println("Database connection failed!");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(HrsamApplication.class, args);
	}
}