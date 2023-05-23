CREATE EXTENSION pgcrypto;
create table public.transaction
(
    id               uuid default gen_random_uuid() not null
        primary key,
    category         varchar(255)
        constraint transaction_category_check
            check ((category)::text = ANY
                   ((ARRAY ['OPERATIONAL'::character varying, 'FINANCING'::character varying, 'INVESTMENT'::character varying])::text[])),
    created_date     timestamp(6) with time zone    not null,
    transaction_date date                           not null,
    type             varchar(255)
        constraint transaction_type_check
            check ((type)::text = ANY ((ARRAY ['CREDIT'::character varying, 'DEBIT'::character varying])::text[])),
    updated_date     timestamp(6) with time zone,
    value            numeric(38, 2)
);

alter table public.transaction
    owner to postgres;



