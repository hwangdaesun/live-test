---
name: live-test-dev-tunnel
description: Run only this live-test project dev Docker Compose stack on port 8080 and expose it with ngrok for external testing.
---

# Live Test Dev Tunnel

Use this skill only for the `live-test` project at
`/Users/dustin.hwang/IdeaProjects/live-test`. It is not a general Spring Boot,
Docker, or ngrok workflow.

## Project Guard

Before running Docker or ngrok commands, verify all of the following from the
project root:

- `pwd` is `/Users/dustin.hwang/IdeaProjects/live-test`, or switch to that
  directory explicitly.
- `settings.gradle` contains `rootProject.name = 'live-test'`.
- `docker-compose.dev.yml` contains `SPRING_PROFILES_ACTIVE: dev`.
- `docker-compose.dev.yml` maps the app as `8080:8080`.
- `.env.dev` exists for the dev Compose environment.

If any guard fails, stop and tell the user this skill is project-specific.

## Workflow

Run the Docker `dev` profile app and expose the same port externally:

Do not ask conversational confirmation before running this workflow. Start the
dev stack and ngrok directly when the user asks for the tunnel. If the runtime
approval policy requires command approval for Docker, host-port curl, or ngrok,
request that approval through the command tool only and continue immediately
after approval.

1. Validate configuration with
   `docker compose --env-file .env.dev -f docker-compose.dev.yml config`.
2. Start the stack with
   `docker compose --env-file .env.dev -f docker-compose.dev.yml up -d --build`.
3. Check status with
   `docker compose --env-file .env.dev -f docker-compose.dev.yml ps`.
4. Verify the app with `curl http://localhost:8080/health`.
5. Start ngrok with `ngrok http 8080`.
6. Get the public URL from ngrok output or
   `http://127.0.0.1:4040/api/tunnels`.
7. Report both `http://localhost:8080` and the ngrok HTTPS URL to the user.

Keep local development on port `8090`; this workflow is only for the Docker
`dev` profile and external testing on `8080`.

## Cleanup

When asked to stop the environment:

- Stop the app stack with `docker compose down`.
- For the split dev file, use
  `docker compose --env-file .env.dev -f docker-compose.dev.yml down`.
- Stop the ngrok process/session that was started for `8080`.

Follow the active command-approval policy before running Docker, curl against
host ports, or ngrok commands. Do not hardcode or persist ngrok URLs.
