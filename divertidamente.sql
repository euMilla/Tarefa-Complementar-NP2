-- ✅ SCRIPT MELHORADO - CRIAÇÃO AUTOMÁTICA DO BANCO
DROP DATABASE IF EXISTS divertidamente;
CREATE DATABASE divertidamente CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE divertidamente;

-- 🧍 TABELA: jogadores
CREATE TABLE jogadores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    nivel_autoconhecimento INT DEFAULT 0,
    ilhas_descobertas INT DEFAULT 0,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 🏝️ TABELA: ilhas (SISTEMA SIMPLIFICADO - 3 ILHAS)
CREATE TABLE ilhas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    tema VARCHAR(50) NOT NULL,
    descricao TEXT,
    nivel_requerido INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ❓ TABELA: perguntas
CREATE TABLE perguntas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ilha_id INT NOT NULL,
    texto TEXT NOT NULL,
    tipo VARCHAR(50) DEFAULT 'texto',
    FOREIGN KEY (ilha_id) REFERENCES ilhas(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 💬 TABELA: respostas
CREATE TABLE respostas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    jogador_id INT NOT NULL,
    pergunta_id INT NOT NULL,
    resposta_texto TEXT NOT NULL,
    data_resposta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (jogador_id) REFERENCES jogadores(id) ON DELETE CASCADE,
    FOREIGN KEY (pergunta_id) REFERENCES perguntas(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 🎯 INSERIR ILHAS (SISTEMA DE 3 ILHAS)
INSERT INTO ilhas (nome, tema, descricao, nivel_requerido) VALUES
('Ilha da Memória', 'memoria', 'Explore suas lembranças e experiências passadas que moldaram quem você é.', 0),
('Ilha da Criatividade', 'criatividade', 'Descubra seu potencial criativo e imagine futuras possibilidades.', 60),
('Ilha das Emoções', 'emocao', 'Navegue pelo mar das suas emoções e aprenda a entendê-las melhor.', 120);

-- 🧠 INSERIR PERGUNTAS (3 por ilha)
INSERT INTO perguntas (ilha_id, texto, tipo) VALUES
-- Ilha 1 - Memória
(1, 'Qual é a sua memória mais feliz da infância? Descreva esse momento especial.', 'texto'),
(1, 'Que experiência do passado mais te marcou e por quê?', 'texto'),
(1, 'Qual pessoa foi mais importante na sua formação? O que você aprendeu com ela?', 'texto'),

-- Ilha 2 - Criatividade
(2, 'O que você gostaria de criar se não houvesse limites? Descreva sua ideia.', 'texto'),
(2, 'Como você expressa sua criatividade no dia a dia?', 'texto'),
(2, 'Qual é o seu projeto dos sonhos? Algo que você gostaria de realizar.', 'texto'),

-- Ilha 3 - Emoções
(3, 'Como você lida com sentimentos difíceis como tristeza ou frustração?', 'texto'),
(3, 'Qual emoção você sente com mais frequência no seu dia a dia?', 'texto'),
(3, 'O que te faz sentir gratidão atualmente?', 'texto');

-- ilhas 👀 VERIFICAR DADOS
SELECT '=== BANCO CONFIGURADO ===' AS '';
SELECT 'Jogadores:' AS '', COUNT(*) AS total FROM jogadores;
SELECT 'Ilhas:' AS '', COUNT(*) AS total FROM ilhas;
SELECT 'Perguntas:' AS '', COUNT(*) AS total FROM perguntas;
SELECT 'Respostas:' AS '', COUNT(*) AS total FROM respostas;

-- MOSTRAR ILHAS INSERIDAS
SELECT '=== ILHAS INSERIDAS ===' AS '';
SELECT id, nome, tema, nivel_requerido FROM ilhas ORDER BY nivel_requerido;

-- MOSTRAR PERGUNTAS INSERIDAS
SELECT '=== PERGUNTAS INSERIDAS ===' AS '';
SELECT p.id, i.nome as ilha, p.texto 
FROM perguntas p 
JOIN ilhas i ON p.ilha_id = i.id 
ORDER BY p.ilha_id, p.id;

