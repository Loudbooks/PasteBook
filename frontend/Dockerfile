FROM node:23-alpine AS build

WORKDIR /pastebook-frontend

ARG COMMIT_HASH
ARG RAILWAY_GIT_COMMIT_SHA

RUN SHORT_HASH=$(echo "${COMMIT_HASH:-$RAILWAY_GIT_COMMIT_SHA}" | cut -c1-7) && \
    if [ -n "$SHORT_HASH" ]; then echo "VITE_COMMIT_HASH=$SHORT_HASH" > .env; fi

COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

FROM node:23-alpine

WORKDIR /pastebook-frontend

COPY --from=build /pastebook-frontend /pastebook-frontend

RUN npm install --production

EXPOSE 3000

CMD ["node", "build"]
