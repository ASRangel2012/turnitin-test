FROM node:18.16.1

# set working directory
WORKDIR /app

# add `/app/node_modules/.bin` to $PATH
ENV PATH /app/node_modules/.bin:$PATH

# # add app
# RUN CI=true
# ARG BUILD


COPY package.json tsconfig.json /app/

RUN  npm install

# start app
CMD ["npm", "start"]