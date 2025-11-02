package com.example.gestionlaboratorios.comun.controller;

import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Profile;

@Profile("dev")
@RestController
@RequestMapping("/api/oracle-test")
public class OracleTestController {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @GetMapping("/connection")
    public String testOracleConnection() {
        try {
            // Consulta simple a Oracle
            String result = jdbcTemplate.queryForObject(
                "SELECT 'Conexión exitosa a Oracle Cloud: ' || TO_CHAR(SYSDATE, 'DD-MM-YYYY HH24:MI:SS') FROM DUAL",
                String.class
            );
            return result;
        } catch (Exception e) {
            return "Error al conectar con Oracle Cloud: " + e.getMessage();
        }
    }
    
    @GetMapping("/version")
    public String getOracleVersion() {
        try {
            return jdbcTemplate.queryForObject(
                "SELECT BANNER FROM v$version WHERE ROWNUM = 1",
                String.class
            );
        } catch (Exception e) {
            return "Error al obtener versión: " + e.getMessage();
        }
    }


    @GetMapping("/connection-info")
    public Map<String, String> getConnectionInfo() {
        try {
            String databaseName = jdbcTemplate.queryForObject("SELECT global_name FROM global_name", String.class);
            String userName = jdbcTemplate.queryForObject("SELECT USER FROM dual", String.class);
            return Map.of("databaseName", databaseName, "userName", userName);
        } catch (Exception e) {
            return Map.of("error", "Error al obtener información de conexión: " + e.getMessage());
        }
    }

    @GetMapping("/tables")
    public List<String> getTableNames() {
        try {
            return jdbcTemplate.queryForList(
                    "SELECT table_name FROM user_tables",
                    String.class
            );
        } catch (Exception e) {
            return List.of("Error al obtener nombres de tablas: " + e.getMessage());
        }
    }

    @GetMapping("/usuarios")
    public List<Map<String, Object>> listarUsuarios() {
        String sql = """
            SELECT ID, RUT, DV, NOMBRE, EMAIL, ROL_ID
            FROM USUARIOS
            ORDER BY ID
        """;
        return jdbcTemplate.queryForList(sql);
    }

    @GetMapping("/roles-usuario")
    public List<Map<String, Object>> listarRolesUsuario() {
        String sql = """
            SELECT ID, NOMBRE_ROL
            FROM ROLES_USUARIO
            ORDER BY ID
        """;
        return jdbcTemplate.queryForList(sql);
    }

}
