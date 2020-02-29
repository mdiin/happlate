/*
{:dependencies #{
  "V__create_private_schema.sql"
  "V__create_public_schema.sql"
}}
*/
-- We want tight control over who is able to execute which functions.
alter default privileges revoke execute on functions from public;
