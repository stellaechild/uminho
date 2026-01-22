## Estrutura

- **Declarações** (`decls`): Onde declaramos variáveis, coleções e funções
- **Instruções** (`insts`): Onde escrevemos as instruções a serem executadas

> **ATENÇÃO**: Todas as declarações devem vir antes de todas as instruções. Não é permitido misturar zonas de declaração e instrução. Todas as variáveis, coleções e funções devem ser declaradas antes de qualquer instrução.

## Declarações

1. **Declarações Simples**: Para variáveis de tipos básicos
   ```
   int numero
   string nome
   bool ativo
   ```

2. **Declarações de Coleções**: Para arrays, conjuntos ou tuplos
   ```
   array int numeros
   tuple string dados
   ```

3. **Declarações de Funções**: Para definir funções com parâmetros e tipo de retorno
   ```
   function soma(int a, int b): int
     return a + b
   end
   ```

   > **ATENÇÃO**: As variáveis usadas dentro de uma função devem ser ou parâmetros declarados no cabeçalho da função ou variáveis declaradas dentro da própria função

## Instruções

- **Atribuição**: `nome = valor`
- **Entrada/Saída**: `read(variavel)` e `write(expressao)`
- **Seleção**: Estruturas `if-then-else` e `case`
- **Repetição**: Ciclos `while`, `repeat-until` e `for`
- **Chamadas de Função**: `nomeFuncao(tipo var1, ...):tipo_return`
- **Retorno**: `return expressao`

## Expressões

- Números inteiros, strings ou booleanos
- Variáveis
- Operações aritméticas (+, -, *, /, %)
- Acesso a elementos de arrays ou tuplos
- Operações em coleções (cons, snoc, head, tail)
- Chamadas de funções

## Exemplo de código

```
int x
int y
array int numeros
int primeiro
function soma(int a, int b): int
    int resultado
    resultado = a + b
    return resultado
end

function dobro(int n): int
    return n * 2
end

x = 10
numeros = []
numeros[5] = 3
if in(3, numeros) then
    write("3 está na coleção")
end
primeiro = head(numeros)
y = dobro(primeiro)
```
