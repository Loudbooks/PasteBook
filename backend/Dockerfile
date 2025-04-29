FROM lukemathwalker/cargo-chef:latest-rust-1 AS chef
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

FROM debian:bookworm-slim AS runtime
RUN apt-get update && apt-get install -y ca-certificates
COPY --from=builder /pastebook-backend/target/release/backend /usr/local/bin/backend
CMD ["/usr/local/bin/backend"]