# AAroN-Docker tests

## Credential stripping

Verifies that `strip_credentials` in [`../aaron-import.sh`](../aaron-import.sh)
removes the `dbsToImport[].password` key from the AARON config after an import
while leaving the rest of the config intact.

Requires [`yq`](https://github.com/mikefarah/yq) (mikefarah v4) on `PATH` — the
same tool the container uses.

```bash
AAroN-Docker/test/strip_credentials_test.sh
```

Exit codes: `0` all checks passed, `1` a check failed, `2` `yq` is not installed.

The fixtures under `fixtures/` use a fake password (`Secret12345678!`) — never a
real secret.
