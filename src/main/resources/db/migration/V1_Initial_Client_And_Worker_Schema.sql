create table public.client
(
    id            integer not null
        primary key,
    full_name     varchar(255),
    short_name    varchar(255),
    third_party_it varchar(255)
);

alter table public.client
    owner to postgres;

create table public.client_worker
(
    id           integer not null
        primary key,
    email        varchar(255),
    first_name   varchar(255),
    last_name    varchar(255),
    phone_number varchar(255),
    title        varchar(255),
    client_id    integer
        constraint client_worker_client_id_fk
            references public.client
);

alter table public.client_worker
    owner to postgres;

