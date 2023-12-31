package br.com.aloisiop1.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class Product {
    public Long id;
    @NotBlank(message = "Campo rótulo não pode estar vazio")
    public String label;
    @NotBlank(message = "Descrição não pode estar vazia")
    public String description;
    @Min(message = "Quantidade não pode ser zero", value=1)
    public Integer quantity;
    @Min(message = "Preço precisa ser maior que zero", value = 1)
    public double price;
}
