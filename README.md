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
  pastebook-backend:
    build:
      context: ./backend
      dockerfile: Dockerfile
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
    build:
      context: ./frontend
      dockerfile: Dockerfile
      args:
        VITE_API_URL: ""
    ports:
      - "80:3000"
    depends_on:
      - pastebook-backend
    networks:
      - pastebook-network

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

volumes:
  mongo-data:

networks:
  pastebook-network:
    driver: bridge
```
### Configuration
All of the following values must be changed.
<br>

`S3_ACCESS_KEY_ID` - The access key associated with your R2 bucket, S3 bucket, etc.

`S3_SECRET_ACCESS_KEY` - The secret access key associated with your R2 bucket, S3 bucket, etc.

`S3_ENDPOINT` - The endpoint associated with your R2 bucket, S3 bucket, etc. 

`VITE_API_URL` - The full endpoint of the API. When locally testing this is `http://localhost:8080`. In my case, when in production, it would be `https://api.pastebook.dev/`.

### Creation
Run the following.
```
docker-compose up
```

Awesome! PasteBook is now running.

## Nginx Configuration
In order to run PasteBook under a domain, you will need to use a reverse proxy. The following will serve as a guide to setting up Nginx.

### Prerequisites
- PasteBook is fully installed with the instructions above.
- A working Nginx installation. Learn more [here](https://nginx.org/en/linux_packages.html#instructions).
- A working Certbot installation. Learn more [here](https://certbot.eff.org/instructions?ws=nginx&os=snap).
## Preparation
Navigate to `/etc/nginx/sites-enabled`.
Create a file under the name `pastebook.conf` and add the following content:
```nginx
server {
    listen 80;
    server_name <DOMAIN>

    location / {
        return 301 https://$host$request_uri;
    }
}

server {
    listen 443 ssl;
    server_name <DOMAIN>

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
    server_name api.<DOMAIN>

    location / {
        return 301 https://$host$request_uri;
    }
}

server {
    listen 443 ssl;
    server_name api<DOMAIN>

    location / {
        proxy_buffering off;  
        proxy_set_header X-Real-IP $remote_addr;                                                                                                
        proxy_set_header X-Forwarded-Host $host;                                                                                                
        proxy_set_header X-Forwarded-Port $server_port;                                                                                         
        proxy_pass http://localhost:8080/;   
    }
}
```

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

# Final Notes
Wow. There was a lot that can go wrong there. I'm not an expert. If you need help, you can email me at contact@loudbook.dev or find me elsewhere.
💜
