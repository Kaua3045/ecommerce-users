# 1.0.0
- Foi adicionado a configuração do oauth2 resource server e a conversão do jwt token
- Foi adicionado as permissões nas rotas

# 0.9.0
- Atualizado as versões do spring de 3.1.2 para 3.1.4

# 0.8.0
- Foi adicionado o swagger e suas configurações

# 0.7.0
- Foi adicionado a invalidação do cache quando alguma coisa da role é alterada
- Foi adicionado a invalidação do cache quando alguma coisa da permission é alterada
- Foi adicionado o cache de account
- Foi adicionado a paginação de accounts
- Foi corrigido o erro quando tentava adicionar uma permissão que já existe a uma role
- Foi alterado o modo de carregamento das roles e permissions, antes era tudo carregado logo no inicio, agora só carregamos quando precisamos

# 0.5.0
- Os usecases foram movido agora para a pasta usecases na application layer

# 0.4.0
- Adicionado o endpoit para remover uma permissão de uma role
- Adicionado o nome das permissões no endpoit de busca de uma role pelo id

# 0.3.0
- Adiconado o relacionamento entre role e permission, agora uma role pode ter N permissions

# 0.2.0
- Adicionado o endpoint para criar uma permissão
- Adicionado o endpoint para pegar uma permissão pelo id
- Adicionado o endpoint para buscar todas as permissões de forma paginada
- Adicionado o endpoint para atualizar uma permissão
- Adicionado o endpoint para deletar uma permissão

# 0.1.0
- Adicionado o endpoint para criar um usuário
- Adicionado o endpoint para pegar um usuário pelo id
- Adicionado o endpoint para atualizar um usuário
- Adicionado o endpoint para deletar um usuário
- Adicionado o endpoint para criar uma role
- Adicionado o endpoint para pegar uma role pelo id
- Adicionado o endpoint para atualizar uma role
- Adicionado o endpoint para deletar uma role