package com.example.gerenciador;

import com.example.gerenciador.modelo.Produto;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class GerenciadorApplication implements CommandLineRunner {
	private final JdbcTemplate jdbc;
	public GerenciadorApplication(JdbcTemplate jdbc) {this.jdbc = jdbc;}
	public static void main(String[] args) {SpringApplication.run(GerenciadorApplication.class, args);}
	@Override
	public void run(String... args) throws Exception {
		List<Produto> produtos = carregarProdutosDoCSV("assets/lista.csv");
		produtos.forEach(this::inserirProduto);
		System.out.println("Quantidade de categorias: " + contarCategorias());
		System.out.println("\nQuantidade por categoria: ");
		listarQuantidadeProdutosPorCategoria().forEach(mapCategoria -> System.out.printf("%s: %d%n", mapCategoria.get("categoria"), mapCategoria.get("total_quantidade")));
		System.out.printf("\nMédia do preço geral: %.2f%n", calcularMediaPreco());
		List<Produto> baixoEstoque = obterProdutosComBaixoEstoque();
		System.out.println("\nProdutos com baixo estoque:");
		baixoEstoque.forEach(System.out::println);
	}
	public List<Produto> carregarProdutosDoCSV(String filePath) {
		try {
			Reader reader = abrirArquivo(filePath);
			CsvToBean<Produto> csvToBean = criarCsvToBean(reader);
			return analisarCsv(csvToBean);
		} catch (IOException e) {
			System.out.println("Erro ao carregar produtos do CSV: " + e.getMessage());
			return null;
		}
	}
	private Reader abrirArquivo(String filePath) throws IOException {
		try {
			return Files.newBufferedReader(Paths.get(filePath));
		} catch (NoSuchFileException e) {
			System.out.println("Arquivo não encontrado: " + filePath);
			throw e;
		}
	}
	private CsvToBean<Produto> criarCsvToBean(Reader reader) {return new CsvToBeanBuilder<Produto>(reader).withType(Produto.class).withIgnoreLeadingWhiteSpace(true).build();}
	private List<Produto> analisarCsv(CsvToBean<Produto> csvToBean) {return csvToBean.parse();}
	public void inserirProduto(Produto produto) {
		if (produto != null) {
			try {
				String sql = "INSERT INTO produtos (nome, quantidade, categoria, preco) VALUES (?, ?, ?, ?)";
				jdbc.update(sql, produto.getNome(), produto.getQuantidade(), produto.getCategoria(), produto.getPreco());
				System.out.println("Produto inserido com sucesso.");
			} catch (DataAccessException e) {
				System.out.println("Erro ao inserir produto: " + e.getMessage());
			}
		} else {
			System.out.println("Erro ao salvar produto: Produto é nulo.");
		}
	}
	public List<Produto> obterProdutosComBaixoEstoque() {
		String sql = "SELECT * FROM produtos WHERE quantidade < 3";
		RowMapper<Produto> rowMapper = (rs, rowNum) -> new Produto(
				rs.getLong("id"),
				rs.getString("nome"),
				rs.getInt("quantidade"),
				rs.getString("categoria"),
				rs.getBigDecimal("preco")
		);
		return jdbc.query(sql, rowMapper);
	}
	public Integer contarCategorias() {return jdbc.queryForObject("SELECT COUNT(DISTINCT categoria) FROM produtos", Integer.class);}
	public List<Map<String, Object>> listarQuantidadeProdutosPorCategoria() {return jdbc.queryForList("SELECT categoria, SUM(quantidade) AS total_quantidade FROM produtos GROUP BY categoria");}
	public BigDecimal calcularMediaPreco() {return jdbc.queryForObject("SELECT AVG(preco) FROM produtos", BigDecimal.class);}
}