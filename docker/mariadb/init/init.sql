create table usuario (
    idUsuario       int auto_increment primary key,
    nome            varchar(255) null,
    email           varchar(255) null,
    username        varchar(255) null,
    password        varchar(255) null,
    endereco        varchar(255) null,
    dataAtualizacao datetime(6)  null
);

create table donorestaurante (
    idUsuario         int          null primary key,
    nomeDoRestaurante varchar(255) null,
    constraint donorestaurante_usuario_idUsuario_fk
        foreign key (idUsuario) references usuario (idUsuario)
);

create table cliente (
     idUsuario         int          null primary key,
     numeroFidelidade varchar(255) null unique,
     constraint cliente_usuario_idUsuario_fk
         foreign key (idUsuario) references usuario (idUsuario)
);

insert into usuario (nome, email, username, password, endereco, dataAtualizacao)
       values ('Administrador', 'admin@gmaillcom', 'admin',
               '$2a$10$wK1mm5fCmRSZtxtcK6Bp7.TAZRzZltKnozkVphS480ni.J/pHGHLC',
                'Rua Cafundó de Judas, 123 - Centro', current_date);

insert into donorestaurante (idUsuario, nomeDoRestaurante)
       values (1, 'Restaurante do Sr Admin');

CREATE TABLE tipo_cozinha(
    id BIGSERIAL PRIMARY KEY,
    tipo VARCHAR(255) NOT NULL
);
INSERT INTO tipo_cozinha(tipo) VALUES('Ilha');
INSERT INTO tipo_cozinha(tipo) VALUES('Zonas');
INSERT INTO tipo_cozinha(tipo) VALUES('Montagem');
INSERT INTO tipo_cozinha(tipo) VALUES('Paralela');
INSERT INTO tipo_cozinha(tipo) VALUES('Em U');

