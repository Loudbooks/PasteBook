#!/bin/sh

export BACKEND_HOST="${BACKEND_HOST:-backend:8080}"
export FRONTEND_HOST="${FRONTEND_HOST:-frontend:3000}"

envsubst '${BACKEND_HOST}' < /etc/nginx/templates/default.conf.template > /etc/nginx/conf.d/default.conf
envsubst '${FRONTEND_HOST}' < /etc/nginx/templates/default.conf.template > /etc/nginx/conf.d/default.conf

exec "$@"
