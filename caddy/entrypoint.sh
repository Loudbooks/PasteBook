set -euo pipefail

export FRONTEND_DOMAIN=${FRONTEND_DOMAIN:-${FRONTEND_HOST%:*}}
export FRONTEND_PORT=${FRONTEND_PORT:-${FRONTEND_HOST##*:}}

export BACKEND_DOMAIN=${BACKEND_DOMAIN:-${BACKEND_HOST%:*}}
export BACKEND_PORT=${BACKEND_PORT:-${BACKEND_HOST##*:}}

FRONTEND_DOMAIN=${FRONTEND_DOMAIN##*://}
BACKEND_DOMAIN=${BACKEND_DOMAIN##*://}

echo Using frontend: ${FRONTEND_DOMAIN} with port: ${FRONTEND_PORT}
echo Using backend: ${BACKEND_DOMAIN} with port: ${BACKEND_PORT}

exec caddy run --config Caddyfile --adapter caddyfile 2>&1