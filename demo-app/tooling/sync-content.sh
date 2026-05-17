#!/usr/bin/env bash
# Sync templates/ and fixtures/ from repo root into app assets.
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/../.." && pwd)"
DEMO="$(cd "$(dirname "$0")/.." && pwd)"
ASSETS="$DEMO/app/src/main/assets"

rm -rf "$ASSETS/templates" "$ASSETS/fixtures"
mkdir -p "$ASSETS/templates/rules" "$ASSETS/fixtures"

cp "$ROOT/templates/rules/CLAUDE.md" "$ASSETS/templates/rules/"
cp "$ROOT/templates/release-checklist.md" "$ASSETS/templates/"

cp -R "$ROOT/fixtures/android-benchmark" "$ASSETS/fixtures/"

echo "Synced to $ASSETS"
find "$ASSETS" -type f | wc -l | xargs echo "files:"
