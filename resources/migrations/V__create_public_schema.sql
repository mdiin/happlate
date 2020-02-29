/*
{:dependencies #{
  "V__create_default_role.sql"
  "V__create_user_role.sql"
}}
*/
-- This is where exposed data lives.
create schema ~{projectName}_public;
grant usage on schema ~{projectName}_public to ~{projectName}_anonymous, ~{projectName}_user;
