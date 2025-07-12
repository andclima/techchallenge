create table usuario (
    idUsuario       int auto_increment primary key,
    nome            varchar(255) null,
    email           varchar(255) null,
    username        varchar(255) null,
    password        varchar(255) null,
    endereco        varchar(255) null,
    dataAtualizacao datetime(6)  null
);

