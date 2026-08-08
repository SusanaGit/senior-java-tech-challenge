FROM ghcr.io/graalvm/native-image-community:21 AS build
USER root
RUN microdnf install -y findutils \
    && microdnf clean all
WORKDIR /project
COPY . .
RUN ./gradlew nativeCompile --no-daemon

FROM debian:bookworm-slim
RUN apt-get update \
    && apt-get install -y --no-install-recommends zlib1g \
    && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY --from=build \
    /project/build/native/nativeCompile/product-api \
    /app/product-api
EXPOSE 8080
ENTRYPOINT ["/app/product-api"]