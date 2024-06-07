-- Peupler enum_platform_category
INSERT INTO "enum_platform_category" (id, name) VALUES
(1, 'console'),
(2, 'arcade'),
(3, 'platform'),
(4, 'operating_system'),
(5, 'portable_console'),
(6, 'computer');

-- Peupler enum_game_category
INSERT INTO "enum_game_category" (id, name) VALUES
(0, 'main_game'),
(1, 'dlc_addon'),
(2, 'expansion'),
(3, 'bundle'),
(4, 'standalone_expansion'),
(5, 'mod'),
(6, 'episode'),
(7, 'season'),
(8, 'remake'),
(9, 'remaster'),
(10, 'expanded_game'),
(11, 'port'),
(12, 'fork'),
(13, 'pack'),
(14, 'update');

-- Peupler enum_tag
INSERT INTO "enum_tag" (id, name) VALUES
(0, 'Theme'),
(1, 'Genre'),
(2, 'Keyword'),
(3, 'Game'),
(4, 'Player Perspective');
