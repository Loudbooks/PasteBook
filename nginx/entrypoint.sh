#!/bin/sh

export BACKEND_HOST="${BACKEND_HOST:-backend:8080}"
export FRONTEND_HOST="${FRONTEND_HOST:-frontend:3000}"

touch /etc/nginx/conf.d/default.conf

sed -e "s|\${BACKEND_HOST}|${BACKEND_HOST}|g" \
    -e "s|\${FRONTEND_HOST}|${FRONTEND_HOST}|g" \
    /etc/nginx/templates/default.conf.template > /etc/nginx/conf.d/default.conf

exec "$@"
