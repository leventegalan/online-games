
-- GLOBAL

SELECT SUM(match_count_total)/2 FROM match_done;
-- 640 games

SELECT m.game_type_fk, g.name, SUM(m.match_count_total)/2 FROM match_done m, game_type g where g.id = m.game_type_fk group by m.game_type_fk;
-- 1	Amőba	200.0000
-- 2	Dáma	220.0000
-- 3	Malom	220.0000

SELECT u.username, SUM(m.match_won_total), SUM(m.match_count_total) FROM match_done m, user u where u.id = m.player_fk group by m.player_fk order by sum(m.match_won_total) desc;
-- eddy		115		224
-- edd		100		195
-- test		73		203
-- álmos	64		103
-- ed		57		122



-- BY GAME TYPE

-- amőba

SELECT u.username, SUM(m.match_won_total), SUM(m.match_count_total) FROM match_done m, user u where u.id = m.player_fk and game_type_fk = 1 group by m.player_fk order by sum(m.match_won_total) desc limit 5;
-- töhötöm	45	50
-- test		38	93
-- edd		30	45
-- tas		29	60
-- ond		21	60
-- for other game_type change value of game_type_fk



-- PERSONAL

-- Összes nyert és lejátszott játék
SELECT SUM(match_won_total), SUM(match_count_total) FROM match_done where player_fk = 1;
-- 73	203
-- player_fk has to get parameter


-- Saját játszmák típusonként
SELECT g.name, SUM(m.match_won_total), SUM(m.match_count_total) FROM match_done m, game_type g where g.id = m.game_type_fk and m.player_fk = 1 group by m.game_type_fk order by sum(m.match_won_total) desc;
