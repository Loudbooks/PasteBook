FROM rust:latest AS builder

COPY ./Cargo.toml ./Cargo.lock ./
RUN cargo fetch
COPY ./src ./src
RUN cargo build --release --target x86_64-unknown-linux-musl

FROM alpine:latest

COPY --from=builder target/release/backend /usr/local/bin/backend

CMD ["/usr/local/bin/backend"]