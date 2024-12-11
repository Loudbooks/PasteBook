FROM rust:bookworm AS builder

COPY ./Cargo.toml ./Cargo.lock ./
RUN cargo fetch
COPY ./src ./src

RUN rustup target add x86_64-unknown-linux-musl
RUN apt-get update && apt-get install -y musl-tools
RUN cargo build --release --target x86_64-unknown-linux-musl

FROM debian:bookworm

RUN apt-get update && apt-get install -y libc6
COPY --from=builder target/x86_64-unknown-linux-musl/release/backend /usr/local/bin/backend

CMD ["/usr/local/bin/backend"]