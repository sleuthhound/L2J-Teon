-- Update Npc
Update npc set rhand = 8208, aggro = 0 where id = 29062;
Update npc set rhand = 8208, aggro = 0 where id = 29063;
Update npc set rhand = 8207, lhand = 8207, aggro = 0 where id = 29064;
Update npc set faction_id = "VANHALTER", faction_range = 1800 where id in (22191,22192,22193,29062);
Update npc set `type` = "L2Monster" , walkspd = 0 ,runspd = 0, aggro = 500 where id in (32051,32058,32059,32060,32061,32062,32063,32064,32065,32066,32067,32068);

-- Update minions
Delete From minions where boss_id in (29062,22188,22191);
INSERT INTO minions
  (boss_id, minion_id, amount_min, amount_max)
VALUES
  ("29062", "29063", "1", "1"),
  ("29062", "29064", "3", "3"),
  ("22188", "22189", "4", "4"),
  ("22188", "22190", "1", "1"),
  ("22191", "22192", "1", "1"),
  ("22191", "22193", "1", "1");

-- Update spawnlist
Delete From raidboss_spawnlist where boss_id = 29062;
Delete From spawnlist where npc_templateId in
(22175,22176,22188,22189,22190,22191,22192,22193,22195,29062,29063,29064,32038,32051,32058,32059,32060,32061,32062,32063,32064,32065,32066,32067,32068);