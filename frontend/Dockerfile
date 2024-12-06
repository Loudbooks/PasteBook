FROM node:23-alpine AS build

WORKDIR /pastebook-frontend

ARG COMMIT_HASH
ENV VITE_COMMIT_HASH=$COMMIT_HASH

RUN echo "VITE_COMMIT_HASH=$VITE_COMMIT_HASH" >> .env

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