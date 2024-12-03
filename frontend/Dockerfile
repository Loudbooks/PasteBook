FROM node:23-alpine AS build

WORKDIR /pastebook-frontend

ARG VITE_API_URL

ENV VITE_API_URL=${VITE_API_URL}

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