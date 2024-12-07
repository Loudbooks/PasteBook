#!/bin/bash

install_packages() {
    echo
    echo "Installing packages..."
    echo
    apt-get update
    apt-get install -y curl nginx certbot python3-certbot-nginx
    echo
    echo "Packages have been installed."
}

download_docker_compose() {
    echo
    echo "Downloading docker-compose.yml..."
    curl -sSL https://raw.githubusercontent.com/Loudbooks/PasteBook/refs/heads/master/docker-compose.yml -o docker-compose.yml
    echo "docker-compose.yml has been created."
}

create_env_file() {
    echo
    echo "Creating .env file..."
    echo
    read -p "Enter the title to be used for PasteBook (leave blank for default): " TITLE
    read -p "Enter the description to be used for the home page of PasteBook (leave blank for default): " DESCRIPTION
    read -p "Disable new paste creation? (yes/no): " DISABLE_NEW

    cat <<EOL > .env
TITLE=${TITLE}
DESCRIPTION=${DESCRIPTION}
DISABLE_NEW=${DISABLE_NEW}
EOL
    echo
    echo ".env file has been created."
}

download_and_configure_nginx() {
    echo
    echo "Downloading default Nginx configuration..."
    echo

    curl -sSL https://raw.githubusercontent.com/Loudbooks/PasteBook/refs/heads/master/pastebook.conf -o /etc/nginx/sites-available/pastebook.conf

    read -p "Enter your domain name (e.g., pastebook.dev): " DOMAIN
    sed -i "s/<DOMAIN>/${DOMAIN}/g" /etc/nginx/sites-available/pastebook.conf

    ln -s /etc/nginx/sites-available/pastebook.conf /etc/nginx/sites-enabled/
    echo
    echo "Nginx configuration completed."
    echo

    echo 
    echo Have you added an A record for your domain pointing to this server\'s IP address, and an A record for api.\<your-domain\> pointing to the same IP address?
    read -p "Press Enter to continue..."

    echo "Setting up SSL with Certbot..."
    sudo certbot --nginx -d ${DOMAIN} -d api.${DOMAIN}
    echo
    echo "SSL setup completed."

    systemctl restart nginx
}

ready() {
    echo
    echo PasteBook is ready!
    echo You can start PasteBook by running:
    echo docker compose up -d
    echo
}

install_packages
download_docker_compose
create_env_file
download_and_configure_nginx
ready