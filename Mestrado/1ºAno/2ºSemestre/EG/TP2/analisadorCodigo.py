from lark import Lark, Visitor, Tree, Token
from collections import defaultdict

with open("lpi.lark", "r") as ficheiro_gramatica:
    gramatica_lpi = ficheiro_gramatica.read()

analisador = Lark(gramatica_lpi, start="start")

class AnalisadorLPI(Visitor):
    def __init__(self):
        # variáveis globais
        self.vars_declaradas = {}
        self.vars_redeclaradas = set()
        self.vars_nao_declaradas = {}
        self.vars_nao_inicializadas = set()
        self.vars_atribuidas = set()
        
        self.vars_por_tipo = defaultdict(list)
        self.instrucoes = {
            "atribuicoes": 0,
            "leituras": 0,
            "escritas": 0,
            "condicionais": 0,
            "ciclicas": 0,
        }
        
        # para lidar com funções e scope
        self.scope_atual = "global"
        self.scopes = {"global": {}}
        self.funcoes_declaradas = {}
        
        # para rastrear variáveis por scope
        self.vars_atribuidas_por_scope = {"global": set()}
        self.vars_usadas_por_scope = {"global": set()}
        
        # para controlar a visita dos nós
        self.processando_corpo_funcao = False

        # para lidar com estruturas de controlo
        self.controlo_aninhado = 0
        self.nivel_controlo = 0
        self.controlo_if = 0
        self.controlo_while = 0
        self.controlo_for = 0
        self.controlo_aninhado_total = 0
        self.nivel_controlo_geral = 0 

        # para rastrear ifs aninhados que podem ser simplificados
        self.ifs_simplificaveis = []
        self.parent_if_stack = []
        self.parent_if_conditions = []
        self.current_if_level = 0
        
        # para distinguir parâmetros de variáveis locais
        self.func_params = defaultdict(list)
        self.param_types = {}
        
    def visit(self, tree):
        # método sobrescrito para controlar o processo de visita recursiva
        f = getattr(self, tree.data, None)
        if f is not None:
            return f(tree)
        else:
            # visita recursiva padrão para nós sem métodos específicos
            for child in tree.children:
                if isinstance(child, Tree):
                    self.visit(child)
    
    def _obter_valor_nome(self, obj_nome):
        if isinstance(obj_nome, Token):
            return obj_nome.value
        elif isinstance(obj_nome, Tree) and obj_nome.children:
            return self._obter_valor_nome(obj_nome.children[0])
        return None
    
    def _check_var_scope(self, nome):
        # verifica se a variável existe no scope atual ou global
        if self.scope_atual != "global" and nome in self.scopes[self.scope_atual]:
            return True
        return nome in self.vars_declaradas
    
    def _check_var_visibility(self, nome):
        # variáveis em scopes de funções não são visíveis no scope global ou em outras funções
        if self.scope_atual == "global":
            return nome in self.vars_declaradas
        else:
            return nome in self.scopes[self.scope_atual] or nome in self.vars_declaradas
    
    def _add_var_to_scope(self, nome):
        if self.scope_atual == "global":
            if nome in self.vars_declaradas:
                self.vars_redeclaradas.add(nome)
            else:
                self.vars_declaradas[nome] = True
        else:
            # adiciona a variável ao scope da função atual
            if nome in self.scopes[self.scope_atual]:
                # redeclaração dentro do scope da função
                self.vars_redeclaradas.add(nome)
            else:
                self.scopes[self.scope_atual][nome] = True
    
    def _mark_var_used(self, nome):
        # regista que uma variável foi usada no scope atual
        if self.scope_atual not in self.vars_usadas_por_scope:
            self.vars_usadas_por_scope[self.scope_atual] = set()
        self.vars_usadas_por_scope[self.scope_atual].add(nome)
    
    def _mark_as_initialized(self, nome):
        # marca uma variável como inicializada no scope correto
        if self.scope_atual == "global":
            self.vars_atribuidas.add(nome)
        else:
            if self.scope_atual not in self.vars_atribuidas_por_scope:
                self.vars_atribuidas_por_scope[self.scope_atual] = set()
            self.vars_atribuidas_por_scope[self.scope_atual].add(nome)
    
    def simple_decl(self, arvore):
        nome = self._obter_valor_nome(arvore.children[1])
        tipo = self._obter_valor_nome(arvore.children[0]) 
        self._add_var_to_scope(nome)
        
        # adicionar à lista de variáveis por tipo apenas se não estiver em um contexto de parâmetro de função
        self.vars_por_tipo[tipo].append(nome)
    
    def collection_decl(self, arvore):
        nome = self._obter_valor_nome(arvore.children[2])
        collection_type = self._obter_valor_nome(arvore.children[0]) 
        basic_type = self._obter_valor_nome(arvore.children[1])
        tipo = f"{collection_type} {basic_type}"
        self._add_var_to_scope(nome)
        self.vars_por_tipo[tipo].append(nome)
    
    def function_decl(self, arvore):
        # extrair nome da função
        nome_funcao = self._obter_valor_nome(arvore.children[0])
        
        # registar a função como declarada
        self.funcoes_declaradas[nome_funcao] = True
        
        # criar um novo scope para a função
        self.scopes[nome_funcao] = {}
        self.vars_atribuidas_por_scope[nome_funcao] = set()
        self.vars_usadas_por_scope[nome_funcao] = set()
        
        # guardar scope anterior e mudar para o novo scope
        scope_anterior = self.scope_atual
        self.scope_atual = nome_funcao
        
        # processar parâmetros da função
        if len(arvore.children) > 1 and isinstance(arvore.children[1], Tree) and arvore.children[1].data == "param_list":
            param_list = arvore.children[1]
            for param in param_list.children:
                if isinstance(param, Tree) and param.data == "param" and len(param.children) >= 2:
                    tipo_param = self._obter_valor_nome(param.children[0])
                    nome_param = self._obter_valor_nome(param.children[1])
                    # adicionar parâmetro ao scope da função
                    self.scopes[nome_funcao][nome_param] = True
                    # marcar parâmetros como inicializados automaticamente
                    self._mark_as_initialized(nome_param)
                    # registrar como parâmetro ao invés de variável local
                    self.func_params[nome_funcao].append(nome_param)
                    self.param_types[(nome_funcao, nome_param)] = tipo_param
                    # NAO adicionar parâmetros à lista vars_por_tipo
        
        # processar corpo da função - Adicionando a visita do corpo da função
        for child in arvore.children:
            if isinstance(child, Tree) and (child.data == "function_body" or child.data == "decls_func"):
                self.visit(child)
        
        # restaurar scope
        self.scope_atual = scope_anterior
    
    def function_body(self, arvore):
        for child in arvore.children:
            if isinstance(child, Tree):
                self.visit(child)
    
    def decls_func(self, arvore):
        for child in arvore.children:
            if isinstance(child, Tree):
                self.visit(child)
    
    def insts(self, arvore):
        for child in arvore.children:
            if isinstance(child, Tree):
                self.visit(child)
    
    def function_call(self, arvore):
        nome_funcao = self._obter_valor_nome(arvore.children[0])
        
        # verificar se a função foi declarada
        if nome_funcao not in self.funcoes_declaradas:
            self.vars_nao_declaradas[nome_funcao] = self.scope_atual
        
        # processar argumentos da chamada
        for i in range(1, len(arvore.children)):
            if i < len(arvore.children):
                self._recolher_vars_usadas(arvore.children[i])
    
    def arg_list(self, arvore):
        for child in arvore.children:
            self._recolher_vars_usadas(child)
    
    def return_stmt(self, arvore):
        if len(arvore.children) > 0:
            self._recolher_vars_usadas(arvore.children[0])
    
    def if_stmt(self, arvore):
        self.instrucoes["condicionais"] += 1
        self.controlo_if += 1
        condicao = arvore.children[0]
        cond_text = self._get_condition_text(condicao)

        # verificar se estamos dentro de outra estrutura de controlo
        if self.nivel_controlo_geral > 0:
            self.controlo_aninhado_total += 1

        # guardar a posição atual
        posicao_atual = f"if_{self.controlo_if}"
        
        # verificar se este if está aninhado dentro de outro if
        if self.parent_if_stack and self.current_if_level > 0:
            parent_cond = self.parent_if_conditions[-1]
            
            # este if está diretamente aninhado no bloco "then" do if pai
            if len(self.parent_if_stack) == 1 and self.current_if_level == 1:
                self.ifs_simplificaveis.append({
                    "parent_condition": parent_cond,
                    "child_condition": cond_text,
                    "original": f"if {parent_cond} then\n    if {cond_text} then",
                    "simplified": f"if {parent_cond} and {cond_text} then"
                })
        
        # fazer stack este if como potencial pai para o próximo
        self.parent_if_stack.append(posicao_atual)
        self.parent_if_conditions.append(cond_text)
        self.current_if_level += 1
        
        self.nivel_controlo += 1
        self.nivel_controlo_geral += 1 
    
        # recolher variáveis da condição
        self._recolher_vars_usadas(condicao)
        
        # visita blocos then e else
        for i in range(1, len(arvore.children)):
            self.visit(arvore.children[i])
        
        # após visitar o bloco then, remover este if da stack
        if self.parent_if_stack:
            self.parent_if_stack.pop()
        if self.parent_if_conditions:
            self.parent_if_conditions.pop()
        self.current_if_level -= 1
        
        self.nivel_controlo -= 1
        self.nivel_controlo_geral -= 1

    def _get_condition_text(self, condition_node):
        # tratar tokens
        if isinstance(condition_node, Token):
            return condition_node.value
        
        # tratar nós do tipo tree
        elif isinstance(condition_node, Tree):
            # tratar chamadas de função
            if condition_node.data == 'function_call':
                # extrair nome da função
                func_name = self._get_condition_text(condition_node.children[0])
                args = []
                
                # extrair argumentos, se existirem
                for i in range(1, len(condition_node.children)):
                    if isinstance(condition_node.children[i], Tree) and condition_node.children[i].data == 'arg_list':
                        # processar cada argumento na lista de argumentos
                        for arg_child in condition_node.children[i].children:
                            args.append(self._get_condition_text(arg_child))
                    else:
                        # argumento de expressão direta
                        args.append(self._get_condition_text(condition_node.children[i]))
                
                # formatar chamada de função com os argumentos
                return f"{func_name}({', '.join(args)})"
            
            # tratar o caso especial do nó collection_op pai
            elif condition_node.data == 'collection_op':
                # passar para o filho específico da operação
                if len(condition_node.children) > 0:
                    return self._get_condition_text(condition_node.children[0])
                return "collection_op()"
            
            # tratar operações específicas de coleções
            elif condition_node.data.endswith('_op') and condition_node.data != 'collection_op':
                # extrair nome da operação sem o sufixo "_op"
                op_name = condition_node.data[:-3]
                
                # obter todos os nós de expressão nesta operação
                expr_nodes = [child for child in condition_node.children if isinstance(child, Tree) and child.data == 'expr']
                
                if len(expr_nodes) == 1:
                    # operação de um argumento (head, tail, is_empty)
                    arg = self._get_condition_text(expr_nodes[0])
                    return f"{op_name}({arg})"
                elif len(expr_nodes) == 2:
                    # operação de dois argumentos (in, cons, snoc)
                    arg1 = self._get_condition_text(expr_nodes[0])
                    arg2 = self._get_condition_text(expr_nodes[1])
                    return f"{op_name}({arg1}, {arg2})"
                else:
                    # alternativa para número inesperado de argumentos
                    return f"{op_name}()"
            
            # tratar condições 'not'
            elif condition_node.data == 'condition':
                if len(condition_node.children) > 0 and isinstance(condition_node.children[0], Token) and condition_node.children[0].value == 'not':
                    rest = self._get_condition_text(condition_node.children[1])
                    return f"not {rest}"
                elif len(condition_node.children) == 1 and isinstance(condition_node.children[0], Tree):
                    return self._get_condition_text(condition_node.children[0])
                elif len(condition_node.children) == 3:
                    left = self._get_condition_text(condition_node.children[0])
                    op = self._get_condition_text(condition_node.children[1])
                    right = self._get_condition_text(condition_node.children[2])
                    return f"{left} {op} {right}"
            
            # tratar factor/term/expr que possam conter collection_op
            elif condition_node.data in ('factor', 'term', 'expr'):
                # se só tiver um filho e for uma tree, passar através
                if len(condition_node.children) == 1 and isinstance(condition_node.children[0], Tree):
                    return self._get_condition_text(condition_node.children[0])
                
                # caso contrário, processar normalmente
                parts = []
                for child in condition_node.children:
                    parts.append(self._get_condition_text(child))
                return " ".join(parts)
            
            # processar recursivamente todos os filhos para outros tipos de nós
            parts = []
            for child in condition_node.children:
                parts.append(self._get_condition_text(child))
            return " ".join(parts) if parts else ""
        
        # alternativa por defeito
        return str(condition_node)

    def head_op(self, arvore):
        if len(arvore.children) >= 3:
            self._recolher_vars_usadas(arvore.children[2])
    
    def tail_op(self, arvore):
        if len(arvore.children) >= 3:
            self._recolher_vars_usadas(arvore.children[2])
    
    def is_empty_op(self, arvore):
        if len(arvore.children) >= 3:
            self._recolher_vars_usadas(arvore.children[2])
    
    def in_op(self, arvore):
        if len(arvore.children) >= 5:
            self._recolher_vars_usadas(arvore.children[2])
            self._recolher_vars_usadas(arvore.children[4])
    
    def cons_op(self, arvore):
        if len(arvore.children) >= 5:
            self._recolher_vars_usadas(arvore.children[2])
            self._recolher_vars_usadas(arvore.children[4])
    
    def snoc_op(self, arvore):
        if len(arvore.children) >= 5:
            self._recolher_vars_usadas(arvore.children[2])
            self._recolher_vars_usadas(arvore.children[4])

    def while_loop(self, arvore):
        self.instrucoes["ciclicas"] += 1
        self.controlo_while += 1
        
        # verificar se estamos dentro de outra estrutura de controlo
        if self.nivel_controlo_geral > 0:
            self.controlo_aninhado_total += 1
            
        self.nivel_controlo_geral += 1
        
        for child in arvore.children:
            if isinstance(child, Tree):
                self.visit(child)
                
        self.nivel_controlo_geral -= 1

    def for_loop(self, arvore):
        self.instrucoes["ciclicas"] += 1
        self.controlo_for += 1
        
        # verificar se estamos dentro de outra estrutura de controlo
        if self.nivel_controlo_geral > 0:
            self.controlo_aninhado_total += 1
            
        self.nivel_controlo_geral += 1 
        
        for child in arvore.children:
            if isinstance(child, Tree):
                self.visit(child)
                
        self.nivel_controlo_geral -= 1

    def input(self, arvore):
        self.instrucoes["leituras"] += 1
        if len(arvore.children) > 0:
            self._recolher_vars_usadas(arvore.children[0])
    
    def output(self, arvore):
        self.instrucoes["escritas"] += 1
        if len(arvore.children) > 0:
            self._recolher_vars_usadas(arvore.children[0])
    
    
    def condition(self, arvore):
        for child in arvore.children:
            self._recolher_vars_usadas(child)
    
    def expr(self, arvore):
        for child in arvore.children:
            self._recolher_vars_usadas(child)
    
    def term(self, arvore):
        for child in arvore.children:
            self._recolher_vars_usadas(child)
    
    def factor(self, arvore):
        for child in arvore.children:
            self._recolher_vars_usadas(child)
    
    def atom(self, arvore):
        for child in arvore.children:
            if isinstance(child, Token) and child.type == "NAME":
                self._recolher_vars_usadas(child)
    
    def collection_op(self, arvore):
        for child in arvore.children:
            self._recolher_vars_usadas(child)
    
    def assign(self, arvore):
        self.instrucoes["atribuicoes"] += 1
        nome = self._obter_valor_nome(arvore.children[0])
        if not self._check_var_scope(nome):
            self.vars_nao_declaradas[nome] = self.scope_atual
        else:
            self._mark_as_initialized(nome)  # a marcar como atribuída/inicializada
        # processar lado direito da atribuição
        if len(arvore.children) > 1:
            self._recolher_vars_usadas(arvore.children[1])
    
    def _recolher_vars_usadas(self, expressao):
        if isinstance(expressao, Token):
            if expressao.type == "NAME":
                nome = expressao.value
                
                # registar uso da variável no scope atual
                self._mark_var_used(nome)
                
                # verificar visibilidade da variável no scope atual
                if not self._check_var_visibility(nome):
                    # se não é visível no scope atual e não é uma função declarada
                    if nome not in self.funcoes_declaradas:
                        # guardar o nome e o scope atual onde a variável é usada mas não declarada
                        self.vars_nao_declaradas[nome] = self.scope_atual
                else:
                    if self.scope_atual == "global":
                        # no scope global, verificar se a variável é global
                        if nome not in self.vars_declaradas:
                            # se não está declarada no scope global, é não declarada
                            self.vars_nao_declaradas[nome] = self.scope_atual
                        elif nome not in self.vars_atribuidas:
                            # se está declarada globalmente mas não inicializada, é não inicializada
                            self.vars_nao_inicializadas.add(nome)
                    else:
                        if nome in self.scopes[self.scope_atual]:
                            # é uma variável local
                            if nome not in self.vars_atribuidas_por_scope.get(self.scope_atual, set()):
                                # é uma variável local não inicializada
                                self.vars_nao_inicializadas.add(nome)
                        elif nome in self.vars_declaradas:
                            # é uma variável global usada em um scope de função
                            if nome not in self.vars_atribuidas:
                                self.vars_nao_inicializadas.add(nome)

        elif isinstance(expressao, Tree):
            if expressao.data == "function_call":
                self.function_call(expressao)
            else:
                for child in expressao.children:
                    self._recolher_vars_usadas(child)
    
    def obter_vars_nao_mencionadas(self):
        vars_mencionadas = set()
        # adicionar variáveis usadas ou atribuídas
        for var in self.vars_usadas_por_scope.get("global", set()):
            vars_mencionadas.add(var)
        for var in self.vars_atribuidas:
            vars_mencionadas.add(var)
        
        # retornar variáveis declaradas que não estão em vars_mencionadas
        return set(self.vars_declaradas.keys()) - vars_mencionadas
    
    
    def gerar_analise(self, codigo_analisado):
        html = "<html><body>"
        html += "<h1>Análise de Programa LPI</h1>"
        html += f"<pre>{codigo_analisado}</pre>"

        # recolher nomes de variáveis em scope de funções para referência
        function_vars = set()
        for scope, vars_scope in self.scopes.items():
            if scope != "global":
                function_vars.update(vars_scope.keys())

        # função para formatar nomes de variáveis com scope
        def format_var_name(var_name, context=None):
            # para variáveis não declaradas, adicionar scope apenas se o nome da variável existir em qualquer scope
            if context == "nao_declaradas" and var_name in self.vars_nao_declaradas:
                uso_scope = self.vars_nao_declaradas[var_name]
                
                # verificar se este nome de variável existe em qualquer scope diferente de onde é usada
                var_exists_somewhere = False
                for scope, vars_scope in self.scopes.items():
                    if scope != uso_scope and var_name in vars_scope:
                        var_exists_somewhere = True
                        break
                
                # adicionar anotação de scope apenas se houver um potencial conflito
                if var_exists_somewhere:
                    return f"<i>{var_name} [{uso_scope}]</i>"
                else:
                    return f"<i>{var_name}</i>"
            
            # formatação normal para outros contextos - adicionar scope apenas se houver conflito
            if var_name in function_vars:
                # variável existe em scope de função
                if var_name in self.vars_declaradas:
                    # também existe no scope global - mostrar scope para desambiguar
                    return f"<i>{var_name} [global]</i>"
            
            for scope, vars_scope in self.scopes.items():
                if scope != "global" and var_name in vars_scope:
                    # verificar se este nome também existe no scope global ou noutras funções
                    name_conflict = var_name in self.vars_declaradas
                    if not name_conflict:
                        for other_scope, other_vars in self.scopes.items():
                            if other_scope != scope and var_name in other_vars:
                                name_conflict = True
                                break
                    
                    if name_conflict:
                        return f"<i>{var_name} [função: {scope}]</i>"
            
            return f"<i>{var_name}</i>"

        html += "<h3>1. Gestão de Variáveis</h3>"
        # vars redeclaradas
        formatted_redeclaradas = [format_var_name(var) for var in sorted(self.vars_redeclaradas)]
        html += f"<p>Variavéis Redeclaradas: {', '.join(formatted_redeclaradas) or 'Nenhuma'}</p>"
        
        # vars não declaradas
        formatted_nao_declaradas = [format_var_name(var, "nao_declaradas") for var in sorted(self.vars_nao_declaradas.keys())]
        html += f"<p>Variavéis Não Declaradas: {', '.join(formatted_nao_declaradas) or 'Nenhuma'}</p>"
        
        # vars não inicializadas
        formatted_nao_inicializadas = [format_var_name(var) for var in sorted(self.vars_nao_inicializadas)]
        html += f"<p>Variavéis Usadas Mas Não Inicializadas: {', '.join(formatted_nao_inicializadas) or 'Nenhuma'}</p>"
        
        # vars não mencionadas
        formatted_nao_mencionadas = [format_var_name(var) for var in sorted(self.obter_vars_nao_mencionadas())]
        html += f"<p>Variavéis Declaradas Mas Não Mencionadas: {', '.join(formatted_nao_mencionadas) or 'Nenhuma'}</p>"
        
        html += "<h3>2. Classificação por Tipos de Dados</h3>"
        html += "<table border='1' cellspacing='0' cellpadding='5'>"
        html += "<thead><tr><th>Tipo</th><th>Variáveis Declaradas</th><th>Total</th></tr></thead><tbody>"
        
        for tipo, vars in self.vars_por_tipo.items():
            html += f"<tr><td>{tipo}</td><td>"
            seen_vars = set()

            # incluir variáveis no scope global
            for var in vars:
                if var in self.vars_declaradas:
                    # verificar conflitos com variáveis de função
                    if var in function_vars:
                        global_var = f"<i>{var} [global]</i>"
                    else:
                        global_var = f"<i>{var}</i>"
                    
                    if global_var not in seen_vars:
                        html += f"{global_var}, "
                        seen_vars.add(global_var)
            
            # incluir apenas variáveis locais nas funções, excluindo parâmetros
            for scope, vars_scope in self.scopes.items():
                if scope != "global":
                    for var in vars_scope:
                        # dar skip se for um parâmetro de função
                        if var in self.func_params.get(scope, []):
                            continue
                        
                        # se var está na lista de variáveis deste tipo, é uma variável local
                        if var in vars:
                            scoped_var = f"<i>{var} [função: {scope}]</i>"
                            if scoped_var not in seen_vars:
                                html += f"{scoped_var}, "
                                seen_vars.add(scoped_var)
            
            html = html.rstrip(", ")
            html += f"</td><td>{len(seen_vars)}</td></tr>"
        
        html += "</tbody></table>"

        html += "<h3>3. Estatísticas do Código</h3>"
        for instrucao, contagem in self.instrucoes.items():
            html += f"<li>{instrucao.capitalize()}: {contagem}</li>"
        html += "</ul>"

        html += "<h3>4. Estruturas Aninhadas</h3>"
        html += f"<p>Total de situações em que estruturas surgem aninhadas: {self.controlo_aninhado_total}</p>"

        # 5 - ifs aninhados simplificáveis
        html += "<h3>5. Oportunidades de Otimização</h3>"
        html += "<p>Ifs aninhados que podem ser substituídos por um só if:</p>"
        if self.ifs_simplificaveis:
            html += "<ul>"
            for info in self.ifs_simplificaveis:
                html += "<li>"
                html += f"<pre>{info['original']}</pre>"
                html += "<small>Pode ser substituído por</small>"
                html += f"<pre>{info['simplified']}</pre>"
                html += "</li>"
            html += "</ul>"
        else:
            html += f"<p>Não foram ifs aninhados que possam ser simplificados (Total de ifs: {self.controlo_if}, Total aninhados: {self.controlo_aninhado_total}).</p>"
        html += "</body></html>"
        return html


if __name__ == "__main__":

    codigo_lpi = """
    int x
    int y
    array int numeros
    int primeiro
    string m
    array int numeros
    function dobro(int n): int
        return n * 2
    end
    function aumenta(int a): string
        string mensagem
        while dobro(a) > a do
            if ( a / 2 ) == 0 then
                a = a + 2
            else
                a = a + 3
            end
        end 
        mensagem = "Aumentei a"
        return mensagem
    end

    
    numeros[0] = x
    if not numeros == [ ] then
        if in(3, numeros) then
            write("3 está na coleção")
        end
    end
    primeiro = head(numeros)
    y = dobro(segundo)
    mensagem = aumenta(y)
    """

    arvore = analisador.parse(codigo_lpi)
    analisador_lpi = AnalisadorLPI()
    analisador_lpi.visit(arvore)
    analise = analisador_lpi.gerar_analise(codigo_lpi)
    with open("analise.html", "w") as ficheiro_analise:
        ficheiro_analise.write(analise)