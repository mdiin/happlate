/*
{:dependencies #{
  "V__create_default_role.sql"
  "V__create_user_role.sql"
}}
*/
-- This is where accessible data which is simply not directly exposed lives.
create schema ~{projectName}_hidden;
grant usage on schema ~{projectName}_hidden to ~{projectName}_anonymous, ~{projectName}_user;
