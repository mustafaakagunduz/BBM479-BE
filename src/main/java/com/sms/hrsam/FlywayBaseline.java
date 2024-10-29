package com.sms.hrsam;

import org.flywaydb.core.Flyway;

public class FlywayBaseline {
    public static void main(String[] args) {
        Flyway flyway = Flyway.configure().dataSource("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres").load();
        // Veritabanında mevcut olan tablo sayısına göre baseline ayarı yapın.
        flyway.baseline();
    }
}
