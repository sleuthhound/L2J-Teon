-- This updates the grades of the contestable clan halls to grade 3 (highest grade with core support).
-- This is completely OPTIONAL and may be safely ignored.
UPDATE `clanhall` SET `Grade` = '3' WHERE `id` IN ('21', '34', '35', '62', '63', '64');
