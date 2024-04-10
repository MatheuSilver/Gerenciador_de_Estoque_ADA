package com.example.gerenciador.modelo;

public class Produto {
    private Long id;
    private String nome;
    private Integer quantidade;
    private String categoria;
    private java.math.BigDecimal preco;

    public Produto(){}

    public Produto(Long id, String nome, Integer quantidade, String categoria, java.math.BigDecimal preco){
        this.id = id;
        this.nome = nome;
        this.quantidade = quantidade;
        this.categoria = categoria;
        this.preco = preco;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public String getCategoria() {
        return categoria;
    }

    public java.math.BigDecimal getPreco() {
        return preco;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nId: ").append(this.id)
                .append("\nNome: ").append(this.nome)
                .append("\nQuantidade: ").append(this.quantidade)
                .append("\nCategoria: ").append(this.categoria)
                .append("\nPreço: ").append(this.preco);
        return sb.toString();
    }
}
