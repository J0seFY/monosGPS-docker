package com.buscarpersonas.repository;

import com.buscarpersonas.Entity.RendimientoEscolar;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReporteRendimientoRepository extends CrudRepository<RendimientoEscolar, Long> {

    @Query(value = """
        SELECT c.nombre AS comuna, e.nombre AS establecimiento, AVG(r.promedio) AS promedio
        FROM rendimiento_escolar r
        JOIN establecimiento e ON e.id = r.establecimiento_id
        JOIN comuna c ON c.id = e.comuna_id
        GROUP BY c.nombre, e.nombre
        ORDER BY c.nombre, e.nombre
        """, nativeQuery = true)
    List<Object[]> obtenerPromedioRendimientoPorComunaYEstablecimiento();
}
