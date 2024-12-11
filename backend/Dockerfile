FROM rust:1.83.0-alpine AS builder

COPY . .

RUN apk add --no-cache clang lld musl-dev git
RUN cargo build --release --locked

FROM alpine:3.18 AS final

COPY --from=builder target/release/backend /usr/local/bin/backend

CMD ["/usr/local/bin/backend"]