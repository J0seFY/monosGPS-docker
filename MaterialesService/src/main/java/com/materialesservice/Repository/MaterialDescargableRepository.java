package com.materialesservice.Repository;

import com.materialesservice.entity.MaterialDescargable;
import com.materialesservice.enumeraciones.Curso;
import com.materialesservice.enumeraciones.NivelEducativo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface MaterialDescargableRepository extends JpaRepository<MaterialDescargable, Long> {

    List<MaterialDescargable> findByNivelEducativoAndCurso(NivelEducativo nivelEducativo, Curso curso);

}