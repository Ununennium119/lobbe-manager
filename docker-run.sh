docker run -d \
    --name spring-app \
    --read-only \                     # Immutable filesystem
    --tmpfs /tmp:rw,size=50m \        # Only writable directory
    --memory=512m \                   # Memory limit
    --cpus=1 \                        # CPU limit
    --publish 127.0.0.1:8080:8080 \   # Bind only to localhost
    --restart=on-failure:5 \          # Auto-restart (max 5 attempts)
    spring-boot-app:latest
