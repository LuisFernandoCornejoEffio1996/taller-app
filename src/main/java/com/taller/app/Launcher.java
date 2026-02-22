package com.taller.app;

public class Launcher {
    public static void main(String[] args) {

        // Generar fecha ANTES de Logback, JavaFX y Spring Boot
        String today = java.time.LocalDate.now().toString();
        System.setProperty("CURRENT_DATE", today);

        // Lanzar JavaFX (que luego lanza Spring Boot)
        TallerApplication.main(args);
    }
}
