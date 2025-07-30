create table usuario (
    idUsuario       int auto_increment primary key,
    nome            varchar(255) null,
    email           varchar(255) null,
    username        varchar(255) null,
    password        varchar(255) null,
    endereco        varchar(255) null,
    dataAtualizacao datetime(6)  null
);

CREATE TABLE
  `donorestaurante` (
    `nomeDoRestaurante` varchar(255) DEFAULT NULL,
    `idUsuario` bigint(20) NOT NULL,
    PRIMARY KEY (`idUsuario`),
    CONSTRAINT `FKq081yncxo1lfga3dhev2pg5xf` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`)
  ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci

CREATE TABLE
  `cliente` (
    `numeroFidelidade` varchar(255) NOT NULL,
    `idUsuario` bigint(20) NOT NULL,
    PRIMARY KEY (`idUsuario`),
    UNIQUE KEY `UKghec93o4x8q2ylxsixu8k0wik` (`numeroFidelidade`),
    CONSTRAINT `FKnl7htkbulq3ys5k2529ix6q8o` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`)
  ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci