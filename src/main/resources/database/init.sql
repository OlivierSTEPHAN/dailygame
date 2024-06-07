-- 1. Créer les tables de référence

CREATE TABLE "enum_platform_category" (
  "id" integer PRIMARY KEY,
  "name" varchar
);

CREATE TABLE "enum_game_category" (
  "id" integer PRIMARY KEY,
  "name" varchar
);

CREATE TABLE "enum_tag" (
  "id" integer PRIMARY KEY,
  "name" varchar
);

CREATE TABLE "collection" (
  "id" integer PRIMARY KEY,
  "as_child_relations" integer,
  "as_parent_relations" integer,
  "games" integer,
  "name" varchar,
  "slug" varchar
);


CREATE TABLE "franchise" (
  "id" integer PRIMARY KEY,
  "name" varchar,
  "slug" varchar
);

CREATE TABLE "game_engine" (
  "id" integer PRIMARY KEY,
  "description" text,
  "name" varchar,
  "slug" varchar,
  "url" varchar
);

CREATE TABLE "game_mode" (
  "id" integer PRIMARY KEY,
  "name" varchar,
  "slug" varchar
);

CREATE TABLE "genre" (
  "id" integer PRIMARY KEY,
  "name" varchar,
  "slug" varchar,
  "url" varchar
);

CREATE TABLE "keywords" (
  "id" integer PRIMARY KEY,
  "name" varchar,
  "slug" varchar
);

CREATE TABLE "platform_family" (
  "id" integer PRIMARY KEY,
  "name" varchar,
  "slug" varchar
);

CREATE TABLE "theme" (
  "id" integer PRIMARY KEY,
  "name" varchar,
  "slug" varchar
);

CREATE TABLE "company" (
  "id" integer PRIMARY KEY,
  "changed_company_id" integer,
  "country" integer,
  "description" text,
  "name" varchar,
  "parent" integer,
  "slug" varchar,
  "start_date" integer,
  "start_date_category" integer,
  "url" varchar,
  "websites" integer[]
);

CREATE TABLE "games" (
  "id" integer PRIMARY KEY,
  "name" varchar,
  "alternative_names" integer[],
  "category" integer,
  "collection" integer,
  "collections" integer[],
  "first_release_date" integer,
  "franchise" integer,
  "franchises" integer[],
  "game_engines" integer[],
  "game_modes" integer[],
  "genres" integer[],
  "involved_companies" integer[],
  "keywords" integer[],
  "parent_game" integer,
  "platforms" integer[],
  "player_perspectives" integer[],
  "ports" integer[],
  "release_dates" integer[],
  "remakes" integer[],
  "remasters" integer[],
  "screenshots" integer[],
  "similar_games" integer[],
  "slug" varchar,
  "storyline" text,
  "summary" text,
  "total_rating" double precision,
  "total_rating_count" integer,
  "updated_at" timestamp,
  "url" varchar,
  "version_parent" integer,
  "version_title" varchar
);

CREATE TABLE "alternative_names" (
  "id" integer PRIMARY KEY,
  "comment" text,
  "game" integer,
  "name" varchar,
  CONSTRAINT fk_game FOREIGN KEY ("game") REFERENCES "games" ("id")
);

CREATE TABLE "involved_company" (
  "id" integer PRIMARY KEY,
  "company" integer,
  "developer" boolean,
  "game" integer,
  "porting" boolean,
  "publisher" boolean,
  "supporting" boolean,
  CONSTRAINT fk_company FOREIGN KEY ("company") REFERENCES "company" ("id"),
  CONSTRAINT fk_game FOREIGN KEY ("game") REFERENCES "games" ("id")
);

CREATE TABLE "platform" (
  "id" integer PRIMARY KEY,
  "abbreviation" varchar,
  "alternative_name" varchar,
  "category" integer,
  "generation" integer,
  "name" varchar,
  "platform_family" integer,
  "slug" varchar,
  "summary" text,
  CONSTRAINT fk_category FOREIGN KEY ("category") REFERENCES "enum_platform_category" ("id"),
  CONSTRAINT fk_platform_family FOREIGN KEY ("platform_family") REFERENCES "platform_family" ("id")
);

CREATE TABLE "release_date" (
  "id" integer PRIMARY KEY,
  "category" integer,
  "date" timestamp,
  "game" integer,
  "human" varchar,
  "m" integer,
  "platform" integer,
  "y" integer,
  CONSTRAINT fk_game FOREIGN KEY ("game") REFERENCES "games" ("id"),
  CONSTRAINT fk_platform FOREIGN KEY ("platform") REFERENCES "platform" ("id")
);

CREATE TABLE "screenshots" (
  "id" integer PRIMARY KEY,
  "alpha_channel" boolean,
  "animated" boolean,
  "game" integer,
  "height" integer,
  "image_id" varchar,
  "url" varchar,
  "width" integer,
  CONSTRAINT fk_game FOREIGN KEY ("game") REFERENCES "games" ("id")
);

-- 2. Créer les tables de jonction pour les relations many-to-many

CREATE TABLE "game_alternative_names" (
  "game_id" integer,
  "alternative_name_id" integer,
  CONSTRAINT fk_game FOREIGN KEY ("game_id") REFERENCES "games" ("id"),
  CONSTRAINT fk_alternative_name FOREIGN KEY ("alternative_name_id") REFERENCES "alternative_names" ("id")
);

CREATE TABLE "game_collections" (
  "game_id" integer,
  "collection_id" integer,
  CONSTRAINT fk_game FOREIGN KEY ("game_id") REFERENCES "games" ("id"),
  CONSTRAINT fk_collection FOREIGN KEY ("collection_id") REFERENCES "collection" ("id")
);

CREATE TABLE "game_franchises" (
  "game_id" integer,
  "franchise_id" integer,
  CONSTRAINT fk_game FOREIGN KEY ("game_id") REFERENCES "games" ("id"),
  CONSTRAINT fk_franchise FOREIGN KEY ("franchise_id") REFERENCES "franchise" ("id")
);

CREATE TABLE "game_game_engines" (
  "game_id" integer,
  "game_engine_id" integer,
  CONSTRAINT fk_game FOREIGN KEY ("game_id") REFERENCES "games" ("id"),
  CONSTRAINT fk_game_engine FOREIGN KEY ("game_engine_id") REFERENCES "game_engine" ("id")
);

CREATE TABLE "game_game_modes" (
  "game_id" integer,
  "game_mode_id" integer,
  CONSTRAINT fk_game FOREIGN KEY ("game_id") REFERENCES "games" ("id"),
  CONSTRAINT fk_game_mode FOREIGN KEY ("game_mode_id") REFERENCES "game_mode" ("id")
);

CREATE TABLE "game_genres" (
  "game_id" integer,
  "genre_id" integer,
  CONSTRAINT fk_game FOREIGN KEY ("game_id") REFERENCES "games" ("id"),
  CONSTRAINT fk_genre FOREIGN KEY ("genre_id") REFERENCES "genre" ("id")
);

CREATE TABLE "game_involved_companies" (
  "game_id" integer,
  "involved_company_id" integer,
  CONSTRAINT fk_game FOREIGN KEY ("game_id") REFERENCES "games" ("id"),
  CONSTRAINT fk_involved_company FOREIGN KEY ("involved_company_id") REFERENCES "involved_company" ("id")
);

CREATE TABLE "game_keywords" (
  "game_id" integer,
  "keyword_id" integer,
  CONSTRAINT fk_game FOREIGN KEY ("game_id") REFERENCES "games" ("id"),
  CONSTRAINT fk_keyword FOREIGN KEY ("keyword_id") REFERENCES "keywords" ("id")
);

CREATE TABLE "game_platforms" (
  "game_id" integer,
  "platform_id" integer,
  CONSTRAINT fk_game FOREIGN KEY ("game_id") REFERENCES
"games" ("id"),
  CONSTRAINT fk_platform FOREIGN KEY ("platform_id") REFERENCES "platform" ("id")
);

-- Contrainte de clé étrangère sur la table player_perspectives
CREATE TABLE "player_perspectives" (
  "id" integer PRIMARY KEY,
  "name" varchar,
  "slug" varchar
);

CREATE TABLE "game_player_perspectives" (
  "game_id" integer,
  "player_perspective_id" integer,
  CONSTRAINT fk_game FOREIGN KEY ("game_id") REFERENCES "games" ("id"),
  CONSTRAINT fk_player_perspective FOREIGN KEY ("player_perspective_id") REFERENCES "player_perspectives" ("id")
);

CREATE TABLE "game_ports" (
  "game_id" integer,
  "port_id" integer,
  CONSTRAINT fk_game FOREIGN KEY ("game_id") REFERENCES "games" ("id"),
  CONSTRAINT fk_port FOREIGN KEY ("port_id") REFERENCES "games" ("id")
);

CREATE TABLE "game_screenshots" (
  "game_id" integer,
  "screenshot_id" integer,
  CONSTRAINT fk_game FOREIGN KEY ("game_id") REFERENCES "games" ("id"),
  CONSTRAINT fk_screenshot FOREIGN KEY ("screenshot_id") REFERENCES "screenshots" ("id")
);

CREATE TABLE "game_release_dates" (
  "game_id" integer,
  "release_date_id" integer,
  CONSTRAINT fk_game FOREIGN KEY ("game_id") REFERENCES "games" ("id"),
  CONSTRAINT fk_release_date FOREIGN KEY ("release_date_id") REFERENCES "release_date" ("id")
);

CREATE TABLE "game_remakes" (
  "game_id" integer,
  "remake_id" integer,
  CONSTRAINT fk_game FOREIGN KEY ("game_id") REFERENCES "games" ("id"),
  CONSTRAINT fk_remake FOREIGN KEY ("remake_id") REFERENCES "games" ("id")
);

CREATE TABLE "game_remasters" (
  "game_id" integer,
  "remaster_id" integer,
  CONSTRAINT fk_game FOREIGN KEY ("game_id") REFERENCES "games" ("id"),
  CONSTRAINT fk_remaster FOREIGN KEY ("remaster_id") REFERENCES "games" ("id")
);

CREATE TABLE "game_similar_games" (
  "game_id" integer,
  "similar_game_id" integer,
  CONSTRAINT fk_game FOREIGN KEY ("game_id") REFERENCES "games" ("id"),
  CONSTRAINT fk_similar_game FOREIGN KEY ("similar_game_id") REFERENCES "games" ("id")
);

CREATE TABLE "game_tags" (
  "game_id" integer,
  "tag_id" integer,
  CONSTRAINT fk_game FOREIGN KEY ("game_id") REFERENCES "games" ("id"),
  CONSTRAINT fk_tag FOREIGN KEY ("tag_id") REFERENCES "enum_tag" ("id")
);

CREATE TABLE "game_themes" (
  "game_id" integer,
  "theme_id" integer,
  CONSTRAINT fk_game FOREIGN KEY ("game_id") REFERENCES "games" ("id"),
  CONSTRAINT fk_theme FOREIGN KEY ("theme_id") REFERENCES "theme" ("id")
);

-- Contrainte de clé étrangère sur la table involved_company
--ALTER TABLE "company" ADD FOREIGN KEY ("changed_company_id") REFERENCES "company" ("id"); <- ne marche pas
--ALTER TABLE "company" ADD FOREIGN KEY ("parent") REFERENCES "company" ("id"); <- ne marche pas

ALTER TABLE "collection" ADD CONSTRAINT fk_as_child_relations FOREIGN KEY ("as_child_relations") REFERENCES "collection" ("id");

ALTER TABLE "collection" ADD CONSTRAINT fk_as_parent_relations FOREIGN KEY ("as_parent_relations") REFERENCES "collection" ("id");

ALTER TABLE "collection" ADD CONSTRAINT fk_games FOREIGN KEY ("games") REFERENCES "games" ("id");

