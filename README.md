# PasteBook 
An easy on the eyes, portable, lightning fast pastebin written in Svelte and Rust.

### Prerequisites
Docker. Both the frontend and backend are to be installed with Docker. You can learn more [here](https://www.docker.com).

## Installation
The following is a guide to get PasteBook running on your system under your domain with SSL. All of this was tested on Ubuntu Linux.

### Preparation
- Download `docker-compose.yml` [here](https://github.com/Loudbooks/PasteBook/blob/master/docker-compose.yml).

### Configuration
> [!CAUTION]
> Do not modify `docker-compose.yml` or `nginx.conf`. You will break things.

Create a file by the name of `.env` in the same directory as these other two files. Add the following.
```env
TITLE=
DESCRIPTION=
DISABLE_NEW=
FAVICON_URL=
MAX_PAYLOAD_SIZE=
```

**All of these options are optional.** You can leave it all blank, or not even have a `.env` file at all. 

By default, PasteBook will run on port 8080.

- `TITLE` - The title to be used around PasteBook.
- `DESCRIPTION` - The description to be used in embeds and on the home page of PasteBook.
- `DISABLE_NEW` - Disables the new paste page. API is still accessible.
- `FAVICON_URL` - The URL that the favicon will be provided with. 
- `MAX_PAYLOAD_SIZE` - The maximum paste size in megabytes.

### Creation
Run the following.
```bash
sudo systemctl enable docker
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

## Domain Preparation
You will need to create a DNS A record pointing to your machine. I use CloudFlare.

### Configuration 
Download `pastebook.conf` [here](https://github.com/Loudbooks/PasteBook/blob/master/pastebook.conf) and place it in `/etc/nginx/sites-enabled`.

`<DOMAIN>` - Change this to your domain name. For example, mine is `pastebook.dev`.

> [!NOTE]
> You can easily replace all instances of `<DOMAIN>` with your domain with the following command. In this example, I used `pastebook.dev`. Make sure to use yours.
> 
> ```bash
> DOMAIN=pastebook.dev && sudo sed -i "s/<DOMAIN>/${DOMAIN}/g" /etc/nginx/sites-available/pastebook.conf
> ```

### SSL Configuration
Run the following, with `<DOMAIN>` changed to your domain.
```bash
sudo certbot certonly --standalone -d <DOMAIN>
```
### Committing Changes
Run the following.
```bash
systemctl restart nginx
```

# Updating PasteBook

> [!NOTE]
> Make sure that `docker-compose.yml` and `pastebook.conf` are up to date before proceeding.

Run the following commands in succession.
```bash
docker compose stop
docker compose pull
docker compose up -d
```

# Final Notes
Wow. There was a lot that can go wrong there. I'm not an expert. If you need help, you can email me at contact@loudbook.dev or find me elsewhere.

💜
