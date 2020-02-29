# ~{projectName} #

Template variables to `sed`ify:

- *projectName*: Project name, used for all namespaces etc.
- *projectDB*: Database name
- *projectDBSuperUser*: Super user for development DB
- *projectDBSuperUserPassword*: Password for development DB super user
- *projectDBUser*: User for development DB
- *projectDBUserPassword*: Password for development DB user
- *projectDBMigrationUser*: User for running migrations on the development DB
- *projectDBMigrationUserPassword*: Password for development DB migrations user

All are enclosed in `~{}` for easy `sed`ing.

## Running in development ##

Run:

```bash
clj -A:dev:test:shadow-cljs ~{projectName}
```

This starts up shadow-cljs with a socket REPL, to which you can connect your editor.

### Atom with Chlorine:

1. Connect a Clojure Socket REPL in Atom
2. Start the system from the `~{projectName}.system` namespace
3. Open the webserver URL in a browser
4. Connect an Embedded REPL in Atom

This will give you a Clojure REPL and a Clojurescript browser-connected REPL.

## Developing

### Database migrations

This template uses [migrer](https://github.com/mdiin/migrer) to handle database migrations.
