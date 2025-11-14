package br.edu.ibmec.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfessorDTO {

    @Min(1)
    private int codigo;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 1, max = 60, message = "Nome deve ter entre 1 e 60 caracteres")
    private String nome;
}

