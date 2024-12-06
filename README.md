# PasteBook 
An easy on the eyes, portable, fun paste bin written in Svelte and Kotlin.

## Installation
The following is a guide to get PasteBook running on your system under your domain with SSL. All of this was tested on Ubuntu Linux.

### Prerequisites
- An S3 bucket. In testing, CloudFlare R2 was used. CloudFlare R2 is free. You can learn more [here](https://www.cloudflare.com/developer-platform/products/r2/).
- Docker. Both the frontend and backend are to be installed with Docker. You can learn more [here](https://www.docker.com).

### Preparation
Start by creating a file named `docker-compose.yml`. Add the content below.
```yml
services:
  backend:
    image: ghcr.io/loudbooks/pastebook-backend:latest
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATA_MONGODB_URI=mongodb://mongo:27017/pastebook
      - SPRING_DATA_MONGODB_DATABASE=pastebook
      - S3_ACCESS_KEY_ID=
      - S3_SECRET_ACCESS_KEY=
      - S3_ENDPOINT=
      - S3_BUCKET=pastebook
    depends_on:
      - mongo
    networks:
      - pastebook-network

  frontend:
    image: ghcr.io/loudbooks/pastebook-frontend:latest
    ports:
      - "3000:3000"
    environment:
      - TITLE=
      - DESCRIPTION=
    depends_on:
      - backend
    networks:
      - pastebook-network
    pull_policy: always

  mongo:
    image: mongo:6.0
    container_name: mongo
    ports:
      - "27017:27017"
    environment:
      MONGO_INITDB_DATABASE: pastebook
    volumes:
      - mongo-data:/data/db
    networks:
      - pastebook-network
    pull_policy: always

volumes:
  mongo-data:

networks:
  pastebook-network:
    driver: bridge
```
### Configuration
> [!CAUTION]
> Do not change any prefilled configurations other than ones listen below. You will break things.

**Required configurations:**
<br>

`S3_ACCESS_KEY_ID` - The access key associated with your R2 bucket, S3 bucket, etc.

`S3_SECRET_ACCESS_KEY` - The secret access key associated with your R2 bucket, S3 bucket, etc.

`S3_ENDPOINT` - The endpoint associated with your R2 bucket, S3 bucket, etc. 

**Optional configurations:**
<br>
`TITLE` - The title to be used around PasteBook.

`DESCRIPTION` - The description to be used in embeds and on the home page of PasteBook.

### Creation
Run the following.
```
docker compose up -d
```

Awesome! PasteBook is now running.

## Nginx Configuration
In order to run PasteBook under a domain, you will need to use a reverse proxy. The following will serve as a guide to setting up Nginx.

### Prerequisites
- PasteBook is fully installed with the instructions above.
- A working Nginx installation. Learn more [here](https://nginx.org/en/linux_packages.html#instructions).
- A working Certbot installation. Learn more [here](https://certbot.eff.org/instructions?ws=nginx&os=snap).
## Nginx Preparation
Navigate to `/etc/nginx/sites-enabled`.
Create a file under the name `pastebook.conf` and add the following content:
```nginx
server {
    listen 80;
    server_name <DOMAIN>;

    location / {
        return 301 https://$host$request_uri;
    }
}

server {
    listen 443 ssl;
    server_name <DOMAIN>;

    location / {
        proxy_buffering off;  
        proxy_set_header X-Real-IP $remote_addr;                                                                                                
        proxy_set_header X-Forwarded-Host $host;                                                                                                
        proxy_set_header X-Forwarded-Port $server_port;                                                                                         
        proxy_pass http://localhost:3000/;   
    }
}

server {
    listen 80;
    server_name api.<DOMAIN>;

    location / {
        return 301 https://$host$request_uri;
    }
}

server {
    listen 443 ssl;
    server_name api.<DOMAIN>;

    client_max_body_size 6M;

    location / {
        proxy_buffering off;  
        proxy_set_header X-Real-IP $remote_addr;                                                                                                
        proxy_set_header X-Forwarded-Host $host;                                                                                                
        proxy_set_header X-Forwarded-Port $server_port;                                                                                         
        proxy_pass http://localhost:8080/;   
    }
}
```

## Domain Preparation
You will need to create a DNS A record pointing to your machine with the root and with the `api.` prefix. I use CloudFlare.

### Configuration 
`<DOMAIN>` - Change this to your domain name. For example, mine is `pastebook.dev`.

### SSL Configuration
Run the following, with `<DOMAIN>` changed to your domain.
```
sudo certbot --nginx -d <DOMAIN> -d api.<DOMAIN>
```

### Committing Changes
Run the following.
```
systemctl restart nginx
```

# Updating PasteBook
Run the following commands in succession.
```bash
docker compose stop
docker compose up -d
```

# Final Notes
Wow. There was a lot that can go wrong there. I'm not an expert. If you need help, you can email me at contact@loudbook.dev or find me elsewhere.
💜
