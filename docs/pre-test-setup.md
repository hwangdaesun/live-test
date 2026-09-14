# Pre-Test Setup

Use this checklist before the live coding test to avoid spending test time on
environment setup.

## Verify tools

```bash
./gradlew test
docker info
ngrok version
```

## Run locally

Use the `local` profile for local development and tests. The local app uses
port `8090`.

Start only MySQL with Docker Compose when the local app needs a persistent
database:

```bash
docker compose --env-file .env.local -f docker-compose.local.yml up -d
```

Use the Testcontainers-backed local entrypoint when the feature needs MySQL:

```bash
./gradlew bootTestRun
```

Use the normal app entrypoint only when a database is already configured:

```bash
./gradlew bootRun
```

## Run with Docker Compose

```bash
docker compose --env-file .env.dev -f docker-compose.dev.yml up --build
```

The dev Compose stack uses the `dev` profile. The app listens on `8080` inside
the container and is exposed from Docker on `http://localhost:8080`. MySQL is
exposed on local port `3306`.

For a single-container run after building the image:

```bash
docker build -t live-test .
docker run --rm -e SPRING_PROFILES_ACTIVE=dev -p 8080:8080 live-test
```

## Expose with ngrok

Start the app first, then run:

```bash
ngrok http 8080
```

Use the generated ngrok URL as the `baseUrl` in `requests.http` or curl.

## Quick API checks

Open `requests.http` in the IDE and run:

- `GET /actuator/health`
- `POST /api/v1/users`

Use `baseUrl` for local development on `8090`, and `dockerUrl` for Docker/ngrok
checks on `8080`.
