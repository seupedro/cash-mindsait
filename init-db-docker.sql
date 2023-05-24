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

create table public.daily_report
(
    id                  uuid default gen_random_uuid() not null primary key,
    count_of_credits    bigint,
    count_of_debits     bigint,
    count_of_financing  bigint,
    count_of_investing  bigint,
    count_of_operations bigint,
    created_date        timestamp(6) with time zone not null default current_timestamp(6),
    report_date         date,
    total_balance       numeric(38, 2),
    total_credit        numeric(38, 2),
    total_debit         numeric(38, 2),
    updated_date        timestamp(6) with time zone not null default current_timestamp(6)
);

alter table public.daily_report
    owner to postgres;

alter table public.transaction
    owner to postgres;