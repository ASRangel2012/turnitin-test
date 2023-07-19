FROM php:8.2.7-apache

RUN mkdir /srv/app
COPY ./vhost.conf /etc/apache2/sites-available/000-default.conf

RUN chown -R www-data:www-data /srv/app \
    && a2enmod rewrite

RUN apt-get update -y && apt-get install git unzip libpq-dev -y

RUN docker-php-ext-install pgsql