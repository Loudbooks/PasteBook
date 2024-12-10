FROM rust:latest

COPY ./ ./

RUN cargo build

RUN cp ./target/debug/backend ./
RUN rm -rf ./src ./Cargo.toml ./Cargo.lock ./target

CMD ["./backend"]
