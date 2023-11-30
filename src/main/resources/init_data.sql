INSERT INTO public.client (client_id, client_lastname, client_firstname) VALUES (1, 'salin', 'ervan');
INSERT INTO public.client (client_id, client_lastname, client_firstname) VALUES (2, 'gaultier', 'jean-paul');
INSERT INTO public.client (client_id, client_lastname, client_firstname) VALUES (3, 'Harmon', 'terry');
INSERT INTO public.client (client_id, client_lastname, client_firstname) VALUES (4, 'Dupuis', 'Auguste');

INSERT INTO public.account (account_id, account_creation_date, account_autorised_discover, account_balance, account_type, client_id) VALUES (1, '2023-11-27 14:48:30.000000', 500.00, 0.00, 'CC', 1);
INSERT INTO public.account (account_id, account_creation_date, account_autorised_discover, account_balance, account_type, client_id) VALUES (2, '2023-11-27 14:48:30.000000', 200.00, 0.00, 'CC', 2);
INSERT INTO public.account (account_id, account_creation_date, account_autorised_discover, account_balance, account_type, client_id) VALUES (3, '2023-11-27 14:48:30.000000', 0.00, 0.00, 'LA', 1);
INSERT INTO public.account (account_id, account_creation_date, account_autorised_discover, account_balance, account_type, client_id) VALUES (4, '2023-11-27 14:48:30.000000', 0.00, 0.00, 'LDD', 1);
INSERT INTO public.account (account_id, account_creation_date, account_autorised_discover, account_balance, account_type, client_id) VALUES (5, '2023-11-27 14:48:30.000000', 1200.00, 0.00, 'CC', 3);
INSERT INTO public.account (account_id, account_creation_date, account_autorised_discover, account_balance, account_type, client_id) VALUES (6, '2023-11-27 14:48:30.000000', 100.00, 0.00, 'CC', 4);
INSERT INTO public.account (account_id, account_creation_date, account_autorised_discover, account_balance, account_type, client_id) VALUES (7, '2023-11-27 14:48:30.000000', 0.00, 0.00, 'LA', 4);

INSERT INTO public.devise (devise_name, devise_rate, devise_code) VALUES ('DOLLAR', 0.8500, 'USD');
INSERT INTO public.devise (devise_name, devise_rate, devise_code) VALUES ('YEN', 0.0075, 'JPY');
INSERT INTO public.devise (devise_name, devise_rate, devise_code) VALUES ('LIVRE_STERLING', 1.1500, 'GBP');

