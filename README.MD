# Sistema de Gerenciamento de Estoque:

## Descrição: 
Nossa empresa tem feito o controle de estoque de forma manual a bastante tempo porém está na hora de uma modernização!
As mercadorias em estoque estão todas em um arquivo do tipo CSV, as colunas são ID do produto, nome, quantidade, cateogia, preço. 
Nossa aplicação deve ser capaz de ler este arquivo CSV e gravar em uma tabela de nossa base de dados estas informações.
Após gravadas as informações alguns relatórios serão necessários (utilize streams para gera-los), crie métodos que respondam as seguintes perguntas:
- Quantas categorias de produtos temos.
- Quantos produtos de cada categoria tem em estoque.
- Valor médio dos produtos
- Produtos com menos de 3 unidades devem ser colocados em uma lista de "estoque baixo"