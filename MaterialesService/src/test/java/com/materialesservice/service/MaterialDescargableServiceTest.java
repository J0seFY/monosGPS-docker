package com.materialesservice.service;

import com.materialesservice.Repository.MaterialDescargableRepository;
import com.materialesservice.Service.MaterialDescargableService;
import com.materialesservice.entity.MaterialDescargable;
import com.materialesservice.entity.MaterialDescargableDTO;
import com.materialesservice.enumeraciones.Curso;
import com.materialesservice.enumeraciones.NivelEducativo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MaterialDescargableServiceTest {

    @Mock
    private MaterialDescargableRepository repository;

    @InjectMocks
    private MaterialDescargableService service;

    @BeforeEach
    void setup() throws Exception {
    service = new MaterialDescargableService();

    // Inyectar repository por reflexión
    Field repoField = MaterialDescargableService.class.getDeclaredField("repository");
    repoField.setAccessible(true);
    repoField.set(service, repository);

    // Inyectar uploadPath por reflexión
    Field pathField = MaterialDescargableService.class.getDeclaredField("uploadPath");
    pathField.setAccessible(true);
    pathField.set(service, "uploads");
}

    @Test
    public void testSubirArchivoExitosamente() throws IOException {
        MockMultipartFile archivo = new MockMultipartFile("archivo", "test.pdf",
                "application/pdf", "Contenido de prueba".getBytes());

        MaterialDescargable materialGuardado = new MaterialDescargable(
                "uuid.pdf", "test.pdf", "uploads/uuid.pdf", NivelEducativo.BASICA, Curso.PRIMERO_BASICO);

        when(repository.save(any(MaterialDescargable.class))).thenReturn(materialGuardado);

        MaterialDescargableDTO dto = service.subirArchivo(archivo, NivelEducativo.BASICA, Curso.PRIMERO_BASICO);

        assertEquals("test.pdf", dto.getNombreOriginal());
        assertEquals(NivelEducativo.BASICA, dto.getNivelEducativo());
        assertEquals(Curso.PRIMERO_BASICO, dto.getCurso());
    }

    @Test
    public void testSubirArchivoVacioLanzaExcepcion() {
        MockMultipartFile archivo = new MockMultipartFile("archivo", "", "text/plain", new byte[0]);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                service.subirArchivo(archivo, NivelEducativo.MEDIA, Curso.PRIMERO_MEDIO)
        );

        assertEquals("El archivo no puede estar vacío", exception.getMessage());
    }

    @Test
    public void testObtenerArchivosPorNivelYCurso() {
        MaterialDescargable m1 = new MaterialDescargable("a.pdf", "a.pdf", "ruta/a", NivelEducativo.BASICA, Curso.PRIMERO_BASICO);
        when(repository.findByNivelEducativoAndCurso(NivelEducativo.BASICA, Curso.PRIMERO_BASICO))
                .thenReturn(List.of(m1));

        List<MaterialDescargableDTO> lista = service.obtenerArchivosPorNivelYCurso(NivelEducativo.BASICA, Curso.PRIMERO_BASICO);

        assertEquals(1, lista.size());
        assertEquals("a.pdf", lista.get(0).getNombreOriginal());
    }

    @Test
    public void testDescargarArchivoExitosamente() throws IOException {
        MaterialDescargable material = new MaterialDescargable("a.pdf", "a.pdf", "uploads/a.pdf", NivelEducativo.BASICA, Curso.PRIMERO_BASICO);

        Path filePath = Path.of("uploads/a.pdf");
        Files.createDirectories(filePath.getParent());
        Files.writeString(filePath, "contenido");

        when(repository.findById(1L)).thenReturn(Optional.of(material));

        Resource resource = service.descargarArchivo(1L);

        assertTrue(resource.exists());
        assertTrue(resource.isReadable());

        Files.deleteIfExists(filePath); // limpiar
    }

    @Test
    public void testDescargarArchivoNoExisteEnBD() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            service.descargarArchivo(1L);
        });

        assertEquals("Archivo no encontrado o inactivo", ex.getMessage());
    }
}
