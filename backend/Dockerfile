FROM rust:latest AS builder

COPY ./Cargo.toml ./Cargo.lock ./
RUN cargo fetch
COPY ./src ./src
RUN cargo build --release

FROM debian:bullseye

RUN apt-get update && apt-get install -y libc6
COPY --from=builder target/release/backend /usr/local/bin/backend

CMD ["/usr/local/bin/backend"]