FROM rust:1.83.0-alpine AS chef
RUN apk add --no-cache clang lld musl-dev git
RUN cargo install cargo-chef
WORKDIR /pastebook-backend

FROM chef AS planner
COPY . .
RUN cargo chef prepare --recipe-path recipe.json

FROM chef AS builder
COPY --from=planner /pastebook-backend/recipe.json recipe.json
RUN cargo chef cook --release --recipe-path recipe.json

COPY . .
RUN cargo build --release --bin backend

FROM alpine:3.18 AS final
RUN apk add --no-cache ca-certificates
COPY --from=builder /pastebook-backend/target/release/backend /usr/local/bin/backend
CMD ["/usr/local/bin/backend"]