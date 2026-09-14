---
name: code-review
description: Review changes in this live-test repository for correctness, regressions, missing tests, architecture violations, and maintainability risks.
---

# Code Review

Use this skill for code review requests in the `live-test` repository,
including change reviews, branch reviews, and PR-style reviews.

## Purpose

Protect production behavior, architectural boundaries, and maintainability before
style preferences. Review as someone accountable for the system after it ships.

## Review Priorities

Find issues in this order:

- Correctness bugs, data loss, security problems, and runtime failures.
- Behavior regressions against existing API, domain, and test expectations.
- Missing tests for changed behavior, error paths, or boundary conditions.
- Architecture violations against `docs/architecture.md` and package-level
  `AGENTS.md` files.
- Maintainability risks that will make the next change materially harder.

Do not lead with formatting, naming, or personal style unless it hides a real
defect or violates a project rule.

## Review Method

- Read the relevant production code, tests, configuration, and documentation
  before judging the change.
- Compare the implementation with the user request and with existing behavior.
- Trace data flow across controller, service, repository, domain, and external
  boundaries when a change crosses layers.
- Treat generated code and OpenAPI contracts as public surfaces when they affect
  clients.
- Verify whether the test suite exercises the changed behavior, not only whether
  tests exist.

## Output

Use a code-review format:

- Start with findings, ordered by severity.
- Include file and line references for each finding.
- Explain the concrete failure mode and when it happens.
- Suggest the smallest safe direction for the fix.
- If there are no findings, say so clearly and mention any residual test or
  verification gaps.

Keep summaries brief and place them after findings.

## Escalation Standard

Block or strongly object when a change can break production behavior, corrupt
data, weaken security, violate enforced architecture, or ship without meaningful
coverage for a high-risk path.
