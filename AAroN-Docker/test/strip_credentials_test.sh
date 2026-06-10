#!/usr/bin/env bash
set -euo pipefail

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SCRIPT="$DIR/../aaron-import.sh"

command -v yq >/dev/null || { echo "yq required (mikefarah v4)"; exit 2; }

AARON_IMPORT_SOURCE_ONLY=1 source "$SCRIPT"

fail=0
check() { if eval "$2"; then echo "PASS: $1"; else echo "FAIL: $1"; fail=1; fi; }

tmp="$(mktemp)"; cp "$DIR/fixtures/db_config.yml" "$tmp"
strip_credentials "$tmp"
check "password key removed"      "! grep -qE '(^|[[:space:]])password:' '$tmp'"
check "username kept"             "grep -qE '(^|[[:space:]])username:' '$tmp'"
check "hostname kept"             "grep -qE '(^|[[:space:]])hostname:' '$tmp'"

strip_credentials "$tmp"
check "idempotent (still no password)" "! grep -qE '(^|[[:space:]])password:' '$tmp'"

tmp2="$(mktemp)"; cp "$DIR/fixtures/file_config.yml" "$tmp2"
before="$(cat "$tmp2")"; strip_credentials "$tmp2"
check "file-only config unchanged" "[ \"\$before\" = \"\$(cat '$tmp2')\" ]"

strip_credentials "$DIR/fixtures/does-not-exist.yml"
check "missing file is a no-op" "true"

# Regression guard: the conversion must capture its exit code with
# `|| aaronExitCode=$?` so a failed import does not abort the script under the
# neo4j entrypoint's `set -e` before the password is stripped.
check "import path is errexit-safe" "grep -qF '|| aaronExitCode=\$?' '$SCRIPT'"

rm -f "$tmp" "$tmp2"
exit $fail
