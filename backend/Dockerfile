FROM rust:1.83.0-alpine AS builder
RUN apk add --no-cache clang lld musl-dev git
WORKDIR /pastebook-backend

COPY Cargo.toml Cargo.lock ./
RUN mkdir -p src && echo "fn main() {}" > src/main.rs
RUN cargo build --release --locked
RUN rm -rf src

COPY src/ src/

RUN cargo build --release --locked

FROM alpine:3.18 AS final
COPY --from=builder /pastebook-backend/target/release/backend /usr/local/bin/backend
CMD ["/usr/local/bin/backend"]