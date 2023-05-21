# snippet-runner
snippet runner for ingsis

## Functionalities:

run

lint

format (debe conectarse con el snippet manager debido a que se tiene que guardar el snippet)

test

liverun

## Endpoints:

GET - /api/snippet-runner/:snippetId -> run a snippet

PUT - /api/snippet-runner/format/:snippetId -> format a snippet

GET - /api/snippet-runner/lint/:snippetId -> validate linting rules on a snippet

GET - /api/snippet-runner/test/:testId -> test a snippet
