package br.edu.ibmec.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ibmec.dto.ProfessorDTO;
import br.edu.ibmec.entity.Professor;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.repository.ProfessorRepository;

/**
 * Serviço para Professor usando Spring Data JPA Repository
 */
@Service("professorRepositoryService")
@Transactional
public class ProfessorRepositoryService {

    @Autowired
    private ProfessorRepository professorRepository;

    @Transactional(readOnly = true)
    public ProfessorDTO buscarProfessor(int codigo) throws DaoException {
        Professor professor = professorRepository.findByCodigo(codigo);
        if (professor == null) {
            throw new DaoException("Professor com código " + codigo + " não encontrado");
        }

        return convertToDTO(professor);
    }

    @Transactional(readOnly = true)
    public Collection<Professor> listarProfessores() throws DaoException {
        return professorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<ProfessorDTO> listarProfessoresCompletos() throws DaoException {
        List<Professor> professores = professorRepository.findAll();
        List<ProfessorDTO> professoresDTO = new ArrayList<>();

        for (Professor professor : professores) {
            professoresDTO.add(convertToDTO(professor));
        }

        return professoresDTO;
    }

    @Transactional
    public void cadastrarProfessor(ProfessorDTO professorDTO) throws ServiceException {
        if (professorDTO.getCodigo() < 1) {
            throw new ServiceException(ServiceException.ServiceExceptionEnum.CURSO_CODIGO_INVALIDO);
        }
        if (professorDTO.getNome() == null || professorDTO.getNome().trim().isEmpty()) {
            throw new ServiceException(ServiceException.ServiceExceptionEnum.CURSO_NOME_INVALIDO);
        }
        if (professorDTO.getNome().trim().length() > 60) {
            throw new ServiceException("Nome do professor deve ter no máximo 60 caracteres");
        }

        if (professorRepository.existsByCodigo(professorDTO.getCodigo())) {
            throw new ServiceException("Professor com código " + professorDTO.getCodigo() + " já existe");
        }

        Professor professor = convertToEntity(professorDTO);
        professorRepository.save(professor);
    }

    @Transactional
    public void alterarProfessor(ProfessorDTO professorDTO) throws ServiceException, DaoException {
        if (professorDTO.getCodigo() < 1) {
            throw new ServiceException(ServiceException.ServiceExceptionEnum.CURSO_CODIGO_INVALIDO);
        }
        if (professorDTO.getNome() == null || professorDTO.getNome().trim().isEmpty()) {
            throw new ServiceException(ServiceException.ServiceExceptionEnum.CURSO_NOME_INVALIDO);
        }
        if (professorDTO.getNome().trim().length() > 60) {
            throw new ServiceException("Nome do professor deve ter no máximo 60 caracteres");
        }

        Optional<Professor> professorOpt = professorRepository.findById(professorDTO.getCodigo());
        if (professorOpt.isEmpty()) {
            throw new DaoException("Professor com código " + professorDTO.getCodigo() + " não encontrado");
        }

        Professor professor = professorOpt.get();
        professor.setNome(professorDTO.getNome().trim());

        professorRepository.save(professor);
    }

    @Transactional
    public void removerProfessor(int codigo) throws DaoException {
        if (!professorRepository.existsById(codigo)) {
            throw new DaoException("Professor com código " + codigo + " não encontrado");
        }

        professorRepository.deleteById(codigo);
    }

    private ProfessorDTO convertToDTO(Professor professor) {
        ProfessorDTO dto = new ProfessorDTO();
        dto.setCodigo(professor.getCodigo());
        dto.setNome(professor.getNome());

        return dto;
    }

    private Professor convertToEntity(ProfessorDTO dto) {
        Professor professor = new Professor();
        professor.setCodigo(dto.getCodigo());
        professor.setNome(dto.getNome().trim());

        return professor;
    }
}

