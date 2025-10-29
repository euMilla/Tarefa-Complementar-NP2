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

-- 🏝️ TABELA: ilhas 
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

-- 🎯 INSERIR ILHAS 
INSERT INTO ilhas (nome, tema, descricao, nivel_requerido) VALUES
('Ilha da Memória', 'memoria', 'Explore suas lembranças e experiências passadas que moldaram quem você é.', 0),
('Ilha da Criatividade', 'criatividade', 'Descubra seu potencial criativo e imagine futuras possibilidades.', 60),
('Ilha das Emoções', 'emocao', 'Navegue pelo mar das suas emoções e aprenda a entendê-las melhor.', 120);

-- 🧠 INSERIR PERGUNTAS MAIS PROFUNDAS (3 por ilha)
INSERT INTO perguntas (ilha_id, texto, tipo) VALUES
-- Ilha 1 - Memória 
(1, 'Qual experiência da sua vida te transformou profundamente e por quê? Como essa vivência mudou sua perspectiva sobre a vida?', 'texto'),
(1, 'Se você pudesse conversar com sua versão mais jovem, qual conselho daria baseado no que aprendeu até hoje? O que essa pessoa precisava ouvir naquela época?', 'texto'),
(1, 'Que valores e crenças você herdou da sua família ou comunidade? Quais você mantém e quais decidiu transformar ao longo do tempo?', 'texto'),

-- Ilha 2 - Criatividade 
(2, 'Se você tivesse recursos ilimitados e não pudesse falhar, que projeto ou sonho realizaria para impactar positivamente o mundo ou sua comunidade?', 'texto'),
(2, 'Como você expressa sua singularidade no dia a dia? De que forma sua maneira única de ser se manifesta nas pequenas escolhas e ações cotidianas?', 'texto'),
(2, 'Qual é a visão que você tem para o seu futuro? Descreva como seria um dia perfeito daqui a 5 anos, incluindo como você se sentiria e o que estaria realizando.', 'texto'),

-- Ilha 3 - Emoções 
(3, 'Como você lida com momentos de incerteza ou medo? Quais estratégias internas você desenvolveu para navegar por emoções desafiadoras?', 'texto'),
(3, 'O que verdadeiramente te faz feliz além das conquistas externas? Descreva aqueles momentos simples que trazem profunda satisfação e paz interior.', 'texto'),
(3, 'Que lições sobre relacionamentos e conexões humanas você carrega consigo? O que aprendeu sobre dar e receber amor, estabelecer limites e cultivar amizades?', 'texto');

-- 👀 VERIFICAR DADOS
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