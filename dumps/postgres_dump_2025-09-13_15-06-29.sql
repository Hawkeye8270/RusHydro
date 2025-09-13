--
-- PostgreSQL database dump
--

-- Dumped from database version 16.6 (Debian 16.6-1.pgdg120+1)
-- Dumped by pg_dump version 16.9 (Debian 16.9-1.pgdg110+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: data; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.data (
    id bigint NOT NULL,
    date date,
    ges character varying(50),
    level numeric(10,2),
    river character varying(50)
);


ALTER TABLE public.data OWNER TO postgres;

--
-- Name: data_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.data_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.data_id_seq OWNER TO postgres;

--
-- Name: data_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.data_id_seq OWNED BY public.data.id;


--
-- Name: data id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.data ALTER COLUMN id SET DEFAULT nextval('public.data_id_seq'::regclass);


--
-- Data for Name: data; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.data (id, date, ges, level, river) FROM stdin;
1	2023-03-17	Богучанская	207.28	Ангара
2	2023-03-18	Богучанская	207.29	Ангара
3	2023-03-19	Богучанская	207.30	Ангара
4	2023-03-20	Богучанская	207.26	Ангара
5	2023-03-21	Богучанская	207.30	Ангара
6	2023-03-22	Богучанская	207.30	Ангара
7	2023-03-23	Богучанская	207.31	Ангара
8	2023-03-24	Богучанская	207.30	Ангара
9	2023-03-25	Богучанская	207.30	Ангара
10	2023-03-26	Богучанская	207.32	Ангара
11	2023-03-27	Богучанская	207.27	Ангара
12	2023-03-28	Богучанская	207.23	Ангара
13	2023-03-29	Богучанская	207.26	Ангара
14	2023-03-30	Богучанская	207.21	Ангара
15	2023-03-31	Богучанская	207.21	Ангара
16	2023-04-01	Богучанская	207.22	Ангара
17	2023-04-02	Богучанская	207.23	Ангара
18	2023-04-03	Богучанская	207.21	Ангара
19	2023-04-04	Богучанская	207.19	Ангара
20	2023-04-05	Богучанская	207.18	Ангара
21	2023-04-06	Богучанская	207.20	Ангара
22	2023-04-07	Богучанская	207.18	Ангара
23	2023-04-08	Богучанская	207.17	Ангара
24	2023-04-09	Богучанская	207.20	Ангара
25	2023-04-10	Богучанская	207.14	Ангара
26	2023-04-11	Богучанская	207.19	Ангара
27	2023-04-12	Богучанская	207.14	Ангара
28	2023-04-13	Богучанская	207.16	Ангара
29	2023-04-14	Богучанская	207.13	Ангара
30	2023-04-15	Богучанская	207.11	Ангара
31	2023-04-16	Богучанская	207.12	Ангара
32	2025-08-13	Воткинская	88.12	Кама
33	2025-08-14	Воткинская	88.10	Кама
34	2025-08-15	Воткинская	88.08	Кама
35	2025-08-16	Воткинская	88.06	Кама
36	2025-08-17	Воткинская	88.02	Кама
37	2025-08-18	Воткинская	88.00	Кама
38	2025-08-19	Воткинская	88.00	Кама
39	2025-08-20	Воткинская	87.98	Кама
40	2025-08-21	Воткинская	87.96	Кама
41	2025-08-22	Воткинская	87.90	Кама
42	2025-08-23	Воткинская	87.90	Кама
43	2025-08-24	Воткинская	87.88	Кама
44	2025-08-25	Воткинская	87.86	Кама
45	2025-08-26	Воткинская	87.86	Кама
46	2025-08-27	Воткинская	87.84	Кама
47	2025-08-28	Воткинская	87.77	Кама
48	2025-08-29	Воткинская	87.76	Кама
49	2025-08-30	Воткинская	88.80	Кама
50	2025-08-31	Воткинская	87.80	Кама
51	2025-09-01	Воткинская	87.80	Кама
52	2025-09-02	Воткинская	87.82	Кама
53	2025-09-03	Воткинская	87.76	Кама
54	2025-09-04	Воткинская	87.82	Кама
55	2025-09-05	Воткинская	87.79	Кама
56	2025-09-06	Воткинская	87.80	Кама
57	2025-09-07	Воткинская	87.75	Кама
58	2025-09-08	Воткинская	87.69	Кама
59	2025-09-09	Воткинская	87.71	Кама
60	2025-09-10	Воткинская	87.67	Кама
61	2025-09-11	Воткинская	87.70	Кама
62	2025-09-12	Воткинская	87.66	Кама
\.


--
-- Name: data_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.data_id_seq', 62, true);


--
-- Name: data data_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.data
    ADD CONSTRAINT data_pkey PRIMARY KEY (id);


--
-- PostgreSQL database dump complete
--

