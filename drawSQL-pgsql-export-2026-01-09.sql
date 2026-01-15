-- Active: 1752483638349@@127.0.0.1@5432@taxibrousse
CREATE TABLE "voiture"(
    "id" INTEGER NOT NULL,
    "capacite" INTEGER NULL,
    "matricule" TEXT NOT NULL
);
ALTER TABLE
    "voiture" ADD PRIMARY KEY("id");
CREATE TABLE "gare"(
    "id" INTEGER NOT NULL,
    "nom" INTEGER NOT NULL,
    "ville" INTEGER NOT NULL
);
ALTER TABLE
    "gare" ADD PRIMARY KEY("id");
CREATE TABLE "trajet"(
    "id" INTEGER NOT NULL,
    "idDepart" INTEGER NOT NULL,
    "idArrive" INTEGER NOT NULL
);
ALTER TABLE
    "trajet" ADD PRIMARY KEY("id");
CREATE TABLE "voyage"(
    "id" INTEGER NOT NULL,
    "idVoiture" INTEGER NOT NULL,
    "idTrajet" BIGINT NOT NULL,
    "dateHeure" DATE NOT NULL,
    "dureVoyage" INTEGER NOT NULL
);
ALTER TABLE
    "voyage" ADD PRIMARY KEY("id");
CREATE TABLE "reservation"(
    "id" INTEGER NOT NULL,
    "idVoyage" INTEGER NOT NULL,
    "nomClient" TEXT NOT NULL,
    "nbPlaces" INTEGER NOT NULL,
    "idStatus" INTEGER NOT NULL
);
ALTER TABLE
    "reservation" ADD PRIMARY KEY("id");
CREATE TABLE "status"(
    "id" INTEGER NOT NULL,
    "lib" INTEGER NOT NULL
);
ALTER TABLE
    "status" ADD PRIMARY KEY("id");
ALTER TABLE
    "trajet" ADD CONSTRAINT "trajet_idarrive_foreign" FOREIGN KEY("idArrive") REFERENCES "gare"("id");
ALTER TABLE
    "voyage" ADD CONSTRAINT "voyage_idvoiture_foreign" FOREIGN KEY("idVoiture") REFERENCES "voiture"("id");
ALTER TABLE
    "reservation" ADD CONSTRAINT "reservation_idvoyage_foreign" FOREIGN KEY("idVoyage") REFERENCES "voyage"("id");
ALTER TABLE
    "trajet" ADD CONSTRAINT "trajet_iddepart_foreign" FOREIGN KEY("idDepart") REFERENCES "gare"("id");
ALTER TABLE
    "reservation" ADD CONSTRAINT "reservation_idstatus_foreign" FOREIGN KEY("idStatus") REFERENCES "status"("id");
ALTER TABLE
    "voyage" ADD CONSTRAINT "voyage_idtrajet_foreign" FOREIGN KEY("idTrajet") REFERENCES "trajet"("id");