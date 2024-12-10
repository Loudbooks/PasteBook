FROM rust:latest as builder

COPY ./Cargo.toml ./Cargo.lock ./
COPY ./src ./src
RUN cargo build

FROM rust:latest

COPY --from=builder target/debug/backend /usr/local/bin/backend

CMD ["/usr/local/bin/backend"]