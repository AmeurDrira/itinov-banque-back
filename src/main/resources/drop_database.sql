SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'banque'
  AND pid <> pg_backend_pid();

-- Connect with postgres 

DROP DATABASE IF EXISTS banque;
DROP ROLE IF EXISTS banque;