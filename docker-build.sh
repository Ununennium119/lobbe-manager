docker build --tag spring-boot-app:latest \
    --security-opt=no-new-privileges \
    --no-cache .  # Ensures fresh dependencies
