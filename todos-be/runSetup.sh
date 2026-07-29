#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "logger: run database setup..."
if [[ -f "$SCRIPT_DIR/dbCreation.sh" ]]; then
  bash "$SCRIPT_DIR/dbCreation.sh"
else
  echo "logger: dbCreation.sh not found"
  exit 1
fi

echo "logger: run gradle wrapper..."
cd "$SCRIPT_DIR"
if [[ -x "$SCRIPT_DIR/gradlew" ]]; then
  "$SCRIPT_DIR/gradlew" "$@"
elif [[ -f "$SCRIPT_DIR/gradlew.bat" ]] && command -v cmd.exe >/dev/null 2>&1; then
  if [[ $# -gt 0 ]]; then
    cmd.exe /c "call gradlew.bat $*"
  else
    cmd.exe /c "call gradlew.bat"
  fi
else
  echo "logger: gradle wrapper not found"
  exit 1
fi

