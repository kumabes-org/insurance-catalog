create table coverages (
    id uuid not null,
    code varchar(32) not null,
    currency varchar(3) not null,
    mandatory boolean not null,
    max_limit numeric(15,2) not null,
    min_limit numeric(15,2) not null,
    name varchar(120) not null,
    product_id uuid not null,
    primary key (id)
);

create table products (
    id uuid not null,
    code varchar(32) not null,
    created_at timestamp(6) with time zone not null,
    line_of_business varchar(64) not null,
    name varchar(120) not null,
    status enum ('ACTIVE','ARCHIVED','DEPRECATED','DRAFT','SUSPENDED') not null,
    updated_at timestamp(6) with time zone not null,
    primary key (id)
);
