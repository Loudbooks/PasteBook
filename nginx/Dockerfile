FROM nginx:latest

COPY nginx.conf.template /etc/nginx/templates/default.conf.template

RUN apt-get update && apt-get install -y gettext && rm -rf /var/lib/apt/lists/*

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]
CMD ["nginx", "-g", "daemon off;"]