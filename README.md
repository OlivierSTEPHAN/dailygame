# DailyGames Docker Setup

## Docker Commands

### Build and Push Docker Image

Build the image:
``
docker build -t zytoune29/dailygames .
``

Push the image to Docker Hub:
``
docker push zytoune29/dailygames
``

### Running Docker Compose

#### Development Mode

Run the docker-compose file in development mode:
``
docker-compose --env-file .env.dev up --build -d
``
#### Production Mode
Run the docker-compose file in production mode:
``
docker-compose --env-file .env.prod up --build -d
``

### Stopping Docker Compose
    
Stop the docker-compose file:
``
docker-compose down
``

## Initialization on a New Machine

1. **Install Docker**  
   Get Docker from [here](https://www.docker.com/products/docker-desktop) and install it on your machine.

2. **Login to Docker Hub**

Login to Docker Hub:
``
docker login
``

3. **Pull the Docker Image**
``
docker pull zytoune29/dailygames:latest
``

4. **Create and Configure Environment File**
Fill it with the required variables. You can find an example in the `.env.example` file.

``
nano .env.dev // nano .env.prod
``

5. **Run the Docker Compose File**
``
docker-compose --env-file .env.dev up --build -d
``

## Updating the Docker Image

``
docker service update --image zytoune29/dailygames:latest dailygame_app
``

## Checking Logs

To check the logs of the running containers, use the following command:

``  
docker logs root_app_1
``

## Database Connection

You can connect to the database using the following interface:

{host}:9080

---

Feel free to modify this as per your requirements.