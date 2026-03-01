package com.taller.app.modules.configuracion.empresa.service;

import com.taller.app.config.AppConfig;
import com.taller.app.modules.configuracion.empresa.dao.EmpresaConfigAuditoriaDAO;
import com.taller.app.modules.configuracion.empresa.model.EmpresaConfig;
import com.taller.app.modules.configuracion.empresa.repository.EmpresaConfigRepository;
import com.taller.app.modules.seguridad.usuario_sistema.model.UsuarioSistema;
import com.taller.app.modules.shared.auditoria.dao.AuditoriaDetalleDAO;
import com.taller.app.util.enums.EstadoRegistro;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmpresaConfigService {

    private final EmpresaConfigRepository repository;
    private final AuditoriaDetalleDAO auditoriaDAO;
    private final AppConfig appConfig;

    public EmpresaConfig obtenerActiva() {
        return repository.findFirstByEstado(EstadoRegistro.ACTIVO).orElse(null);
    }

    @Transactional
    public EmpresaConfig guardarOActualizar(EmpresaConfig nueva, UsuarioSistema usuarioActual) {

        validarRuc(nueva.getRuc());

        EmpresaConfig actual = obtenerActiva();

        // Si no existe registro, se crea uno nuevo
        if (actual == null) {
            nueva.setEstado(EstadoRegistro.ACTIVO);
            nueva.setUsuarioModificacion(usuarioActual);

            if (nueva.getLogoPath() != null) {
                nueva.setLogoPath(moverLogo(nueva.getLogoPath()));
            }

            return repository.save(nueva);
        }


        // Auditoría campo por campo
        registrarAuditoria(actual, nueva, usuarioActual);

        // Actualizar campos
        actual.setRuc(nueva.getRuc());
        actual.setRazonSocial(nueva.getRazonSocial());
        actual.setNombreComercial(nueva.getNombreComercial());
        actual.setDireccion(nueva.getDireccion());
        actual.setTelefono(nueva.getTelefono());
        actual.setEmail(nueva.getEmail());
        if (nueva.getLogoPath() != null) {
            actual.setLogoPath(moverLogo(nueva.getLogoPath()));
        }
        actual.setUsuarioModificacion(usuarioActual);

        return repository.save(actual);
    }

    private void validarRuc(String ruc) {
        if (ruc == null || !ruc.matches("\\d{11}")) {
            throw new IllegalArgumentException("El RUC debe tener 11 dígitos numéricos");
        }
        int[] factores = {5,4,3,2,7,6,5,4,3,2};
        int suma = 0;

        for (int i = 0; i < 10; i++) {
            suma += Character.getNumericValue(ruc.charAt(i)) * factores[i];
        }

        int resto = suma % 11;
        int digito = 11 - resto;

        if (digito == 10) digito = 0;
        if (digito == 11) digito = 1;

        if (digito != Character.getNumericValue(ruc.charAt(10))) {
            throw new IllegalArgumentException("RUC inválido según SUNAT");
        }
    }

    // ------------------------------
    // AUDITORÍA CAMPO POR CAMPO
    // ------------------------------
    private void registrarAuditoria(EmpresaConfig actual, EmpresaConfig nueva, UsuarioSistema usuario) {
        String entidad = "empresa_config";
        Integer idEntidad = actual.getIdEmpresaConfig();
        Integer idUsuario = usuario.getIdUsuario();

        compararYRegistrar(entidad, idEntidad, "razon_social", actual.getRazonSocial(), nueva.getRazonSocial(), idUsuario);
        compararYRegistrar(entidad, idEntidad, "ruc", actual.getRuc(), nueva.getRuc(), idUsuario);
        compararYRegistrar(entidad, idEntidad, "nombre_comercial", actual.getNombreComercial(), nueva.getNombreComercial(), idUsuario);
        compararYRegistrar(entidad, idEntidad, "direccion", actual.getDireccion(), nueva.getDireccion(), idUsuario);
        compararYRegistrar(entidad, idEntidad, "telefono", actual.getTelefono(), nueva.getTelefono(), idUsuario);
        compararYRegistrar(entidad, idEntidad, "email", actual.getEmail(), nueva.getEmail(), idUsuario);
        compararYRegistrar(entidad, idEntidad, "logo_path", actual.getLogoPath(), nueva.getLogoPath(), idUsuario);
    }

    private void compararYRegistrar(String entidad, Integer idEntidad, String campo, String valor_anterior, String valor_nuevo, Integer idUsuario) {
        if (!equalsNullable(valor_anterior, valor_nuevo)) {
            auditoriaDAO.registrarCambio(entidad, idEntidad, idUsuario, campo, valor_anterior, valor_nuevo);
        }
    }

    private boolean equalsNullable(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }

    private String moverLogo(String rutaOriginal) {

        if (rutaOriginal == null || rutaOriginal.isBlank()) {
            return null;
        }

        try {
            String destinoBase = appConfig.getStorage().getEmpresa().getLogoPath();
            Path destinoDir = Paths.get(destinoBase);

            Files.createDirectories(destinoDir);

            Path origen = Paths.get(rutaOriginal);
            if (!Files.exists(origen)) {
                throw new IllegalStateException("No se encontró el archivo de imagen.");
            }

            String extension = rutaOriginal.substring(rutaOriginal.lastIndexOf("."));
            String nombreArchivo = "logo_empresa" + extension;

            Path destino = destinoDir.resolve(nombreArchivo);

            Files.copy(origen, destino, StandardCopyOption.REPLACE_EXISTING);

            return destino.toString();

        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el logo de la empresa.");
        }
    }

}

