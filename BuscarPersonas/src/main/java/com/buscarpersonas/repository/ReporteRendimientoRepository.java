package com.buscarpersonas.repository;

import com.buscarpersonas.Entity.RendimientoEscolar;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReporteRendimientoRepository extends CrudRepository<RendimientoEscolar, Long> {

    @Query(value = """
        SELECT est.comuna AS comuna,
               est.nombre AS establecimiento,
               AVG(r.nota) AS promedio
        FROM rendimiento r
        JOIN estudiante e ON e.rut = r.estudiante_rut
        JOIN establecimiento est ON est.id = e.establecimiento_id
        GROUP BY est.comuna, est.nombre
        ORDER BY est.comuna, est.nombre
        """, nativeQuery = true)
    List<Object[]> obtenerPromedioRendimientoPorComunaYEstablecimiento();
}
