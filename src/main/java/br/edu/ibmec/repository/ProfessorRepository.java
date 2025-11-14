package br.edu.ibmec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.edu.ibmec.entity.Professor;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Integer> {
    Professor findByCodigo(int codigo);
    boolean existsByCodigo(int codigo);
}

