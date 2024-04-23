# Description: Start the development environment. Passes the env_file to docker-compose.yml
# could do: ENV_FILE=./.env.dev SECRET=secret docker-compose up -d --build
# but that goes against the entire point so we put it in at runtime

ENV_FILE=./.env.dev docker-compose up -d --build

# Set the ENV_FILE environment variable to the path of the development environment file
# ENV_FILE=./.env.dev

# Start the services defined in the docker-compose.yml file in detached mode and build the images before starting the containers
# docker-compose up -d --build

# Stop and remove the containers defined in the docker-compose.yml file
# docker-compose down

# Show the logs for a specific service
# docker-compose logs <service-name>

# List the status of the services defined in the docker-compose.yml file
# docker-compose ps

# Run a command in a running container
# docker-compose exec <service-name> <command>