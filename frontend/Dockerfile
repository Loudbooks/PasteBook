FROM node:23-alpine AS build

RUN corepack enable && corepack prepare pnpm@latest --activate

WORKDIR /pastebook-frontend

ARG COMMIT_HASH
ARG RAILWAY_GIT_COMMIT_SHA

RUN SHORT_HASH=$(echo "${COMMIT_HASH:-$RAILWAY_GIT_COMMIT_SHA}" | cut -c1-7) && \
    if [ -n "$SHORT_HASH" ]; then echo "VITE_COMMIT_HASH=$SHORT_HASH" > .env; fi

COPY package.json pnpm-lock.yaml* ./
RUN pnpm install --frozen-lockfile

COPY . .
RUN pnpm build

FROM node:23-alpine

RUN corepack enable && corepack prepare pnpm@latest --activate

WORKDIR /pastebook-frontend

COPY --from=build /pastebook-frontend /pastebook-frontend

RUN pnpm install --prod --frozen-lockfile

EXPOSE 3000

CMD ["node", "build"]