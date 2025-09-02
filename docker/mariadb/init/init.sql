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

create table restaurante (
    idRestaurante int auto_increment primary key,
    nome                    varchar(255) not null,
    endereco                varchar(255) not null,
    tipoCozinha             varchar(255) not null,
    horarioFuncionamento    varchar(255) not null,
    constraint fk_dono_restaurante
        foreign key (idDono) references donorestaurante(idUsuario),
    dataAtualizacao datetime(6)

)

create table itenscardarpio(
    idItem int auto_increment primary key,
    nome varchar(255) not null,
    descricao varchar(255) null,
    preco decimal(10,2) not null,
    apenasLocal boolean not null default false,
    foto varchar(255) null,
    constraint fk_id_restaurante
        foreign key (idRestaurante) references restaurante(idRestaurante),
    dataAtualizacao datetime(6)
)






insert into usuario (nome, email, username, password, endereco, dataAtualizacao)
       values ('Administrador', 'admin@gmaillcom', 'admin',
               '$2a$10$wK1mm5fCmRSZtxtcK6Bp7.TAZRzZltKnozkVphS480ni.J/pHGHLC',
                'Rua Cafundó de Judas, 123 - Centro', current_date);

insert into donorestaurante (idUsuario, nomeDoRestaurante)
       values (1, 'Restaurante do Sr Admin');



insert into restaurante (nome, endereco, tipoCozinha, horarioFuncionamento, idDono, dataAtualizacao)
        values ('Para Lanches', 'esquina, 1444', 'Lanchonete', 'Seg-Qua:11h-22h; Sab-Dom: 11h-01h', 1, current_date);

insert into itenscardapio (nome, descricao, peco, apenasLocal, foto, idRestaurante, dataAtualizacao)
        values ('X-burguer', 'Pão, Carne e Queijo', 17.99, false, '', 1, current_date)






