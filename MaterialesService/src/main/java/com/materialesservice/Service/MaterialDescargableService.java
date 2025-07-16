package com.materialesservice.Service;

import com.materialesservice.Repository.MaterialDescargableRepository;
import com.materialesservice.entity.MaterialDescargable;
import com.materialesservice.entity.MaterialDescargableDTO;
import com.materialesservice.enumeraciones.Curso;
import com.materialesservice.enumeraciones.NivelEducativo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MaterialDescargableService {

    private static final Logger logger = LoggerFactory.getLogger(MaterialDescargableService.class);

    @Autowired
    private MaterialDescargableRepository repository;

    @Value("${file.upload.path}")
    private String uploadPath;

    public MaterialDescargableDTO subirArchivo(MultipartFile archivo, NivelEducativo nivelEducativo,
                                               Curso curso) throws IOException {

        // Validar archivo
        if (archivo.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacío");
        }

        // Crear directorio si no existe
        Path uploadDir = Paths.get(uploadPath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        // Generar nombre único para el archivo
        String nombreOriginal = archivo.getOriginalFilename();
        String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
        String nombreArchivo = UUID.randomUUID().toString() + extension;

        // Guardar archivo físicamente
        Path rutaCompleta = uploadDir.resolve(nombreArchivo);
        Files.copy(archivo.getInputStream(), rutaCompleta, StandardCopyOption.REPLACE_EXISTING);

        // Crear entidad
        MaterialDescargable material = new MaterialDescargable(
                nombreArchivo,
                nombreOriginal,
                rutaCompleta.toString(),
                nivelEducativo,
                curso
        );

        // Guardar en BD
        MaterialDescargable materialGuardado = repository.save(material);

        logger.info("Archivo subido exitosamente: {}", nombreOriginal);

        return convertirADTO(materialGuardado);
    }


    public List<MaterialDescargableDTO> obtenerArchivosPorNivelYCurso(NivelEducativo nivel, Curso curso) {
        return repository.findByNivelEducativoAndCurso(nivel, curso).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Resource descargarArchivo(Long id) throws IOException {
        Optional<MaterialDescargable> materialOpt = repository.findById(id);

        if (materialOpt.isEmpty()) {
            throw new IllegalArgumentException("Archivo no encontrado o inactivo");
        }

        MaterialDescargable material = materialOpt.get();
        Path rutaArchivo = Paths.get(material.getRutaArchivo());

        if (!Files.exists(rutaArchivo)) {
            throw new IOException("El archivo físico no existe");
        }

        try {
            Resource resource = new UrlResource(rutaArchivo.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new IOException("No se puede leer el archivo");
            }
        } catch (MalformedURLException e) {
            throw new IOException("Error al formar la URL del archivo", e);
        }
    }


    private MaterialDescargableDTO convertirADTO(MaterialDescargable material) {
        return new MaterialDescargableDTO(
                material.getId(),
                material.getNombreOriginal(),
                material.getNivelEducativo(),
                material.getCurso()
        );
    }
}