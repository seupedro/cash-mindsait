CREATE EXTENSION pgcrypto;

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
